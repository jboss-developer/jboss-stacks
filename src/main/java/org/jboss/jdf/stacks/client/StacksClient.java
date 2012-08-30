/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.jdf.stacks.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.jdf.stacks.client.messages.JBossLoggingMessages;
import org.jboss.jdf.stacks.client.messages.StacksMessages;
import org.jboss.jdf.stacks.model.Stacks;
import org.jboss.jdf.stacks.parser.Parser;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class StacksClient {

    private StacksClientConfiguration actualConfiguration;

    private StacksMessages msg;

    public StacksClient() {
        this(new DefaultStacksClientConfiguration());
    }

    public StacksClient(StacksClientConfiguration configuration) {
        this(configuration, new JBossLoggingMessages());
    }

    public StacksClient(StacksClientConfiguration configuration, StacksMessages stacksMessagesImpl) {
        this.msg = stacksMessagesImpl;
        this.actualConfiguration = configuration;
    }

    private Stacks initializeStacks() {
        try {
            // Retrieve inputStream (local cache or remote)
            InputStream inputStream = getStacksInputStream();
            Stacks stacks = null;
            if (inputStream != null) {
                stacks = new Parser().parse(inputStream);
                inputStream.close();
            }
            return stacks;
        } catch (IOException e) {
            msg.showErrorMessageWithCause("IO Exception", e);
            return null;
        }
    }

    /**
     * @return the stacks
     */
    public Stacks getStacks() {
        Stacks stacks = initializeStacks();
        if (stacks == null) {
            throw new IllegalStateException("Error getting Stacks info");
        }
        return stacks;
    }

    /**
     * @return the actualConfiguration
     */
    public StacksClientConfiguration getActualConfiguration() {
        return actualConfiguration;
    }

    /**
     * @return
     * @throws FileNotFoundException
     * 
     */
    private InputStream getStacksInputStream() throws FileNotFoundException {
        InputStream repoStream = getCachedRepoStream(false);
        URL url = actualConfiguration.getUrl();
        // if cache expired
        if (repoStream == null && actualConfiguration.isOnline()) {
            msg.showDebugMessage("Local cache file " + getLocalCacheFile() + " doesn't exist or cache has been expired");
            try {
                msg.showInfoMessage("Retrieving Stacks from Remote repository " + url);
                repoStream = retrieveStacksFromRemoteRepository(url);
                setCachedRepoStream(repoStream);
                msg.showDebugMessage("Forcing the use of local cache after download file without error from " + url);
                repoStream = getCachedRepoStream(true);
            } catch (Exception e) {
                msg.showWarnMessage("It was not possible to contact the repository at " + url + " . Cause " + e.getMessage());
                msg.showWarnMessage("Falling back to cache!");
                repoStream = getCachedRepoStream(true);
            }
        }
        // If the Repostream stills empty after falling back to cache
        if (repoStream == null) {
            msg.showErrorMessage("The Cache is empty. Try going online to get the list of available JDF Stacks!");
            return null;
        } else {
            return repoStream;
        }

    }

    private InputStream retrieveStacksFromRemoteRepository(final URL url) throws Exception {
        if (url.getProtocol().startsWith("http")) {
            HttpGet httpGet = new HttpGet(url.toURI());
            DefaultHttpClient client = new DefaultHttpClient();
            configureProxy(client);
            HttpResponse httpResponse = client.execute(httpGet);
            switch (httpResponse.getStatusLine().getStatusCode()) {
                case 200:
                    msg.showDebugMessage("Connected to repository! Getting available Stacks");
                    break;

                case 404:
                    msg.showErrorMessage("Failed! (Stacks file not found: " + url + ")");
                    return null;

                default:
                    msg.showErrorMessage("Failed! (server returned status code: "
                            + httpResponse.getStatusLine().getStatusCode());
                    return null;
            }
            return httpResponse.getEntity().getContent();
        } else if (url.getProtocol().startsWith("file")) {
            return new FileInputStream(new File(url.toURI()));
        }
        return null;
    }

    private void configureProxy(DefaultHttpClient client) {
        if (actualConfiguration.getProxyHost() != null && !actualConfiguration.getProxyHost().isEmpty()) {
            String proxyHost = actualConfiguration.getProxyHost();
            int proxyPort = actualConfiguration.getProxyPort();
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            String proxyUsername = actualConfiguration.getProxyUser();
            if (proxyUsername != null && !proxyUsername.isEmpty()) {
                String proxyPassword = actualConfiguration.getProxyPassword();
                AuthScope authScope = new AuthScope(proxyHost, proxyPort);
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
                client.getCredentialsProvider().setCredentials(authScope, credentials);
            }
        }
    }

    /**
     * Get the local file cache for each repository url
     * 
     * @return
     */
    public File getLocalCacheFile() {
        // Remove no word character from the repo url
        String repo = actualConfiguration.getUrl().toString().replaceAll("[^a-zA-Z_0-9]", "");
        return new File(System.getProperty("java.io.tmpdir"), repo + "stacks.yaml");
    }

    private InputStream getCachedRepoStream(final boolean force) throws FileNotFoundException {
        final String logmessage = "Local file %1s %2s used! Reason: Force:[%3b] - Online:[%4b] - LastModification: %5d/%6d";
        File localCacheFile = getLocalCacheFile();
        if (localCacheFile.exists()) {
            int cacheSeconds = actualConfiguration.getCacheRefreshPeriodInSeconds();
            boolean online = actualConfiguration.isOnline();
            long cachedvalidity = 1000 * cacheSeconds;
            long lastModified = localCacheFile.lastModified();
            long timeSinceLastModification = System.currentTimeMillis() - lastModified;
            // if online, consider the cache valid until it expires
            if (force || !online || timeSinceLastModification <= cachedvalidity) {
                msg.showDebugMessage(String.format(logmessage, localCacheFile, "was", force, online, timeSinceLastModification,
                        cachedvalidity));
                return new FileInputStream(localCacheFile);
            }
            msg.showDebugMessage(String.format(logmessage, localCacheFile, "was not", force, online, timeSinceLastModification,
                    cachedvalidity));
        }
        return null;
    }

    private void setCachedRepoStream(final InputStream stream) throws IOException {
        File localCacheFile = getLocalCacheFile();
        msg.showDebugMessage("Content stored at " + localCacheFile);
        if (!localCacheFile.exists()) {
            localCacheFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(localCacheFile);

        int i = 0;
        while ((i = stream.read()) != -1) {
            fos.write(i);
        }
        fos.close();
    }

    /**
     * This will drop the local cache to force an online update.
     */
    public void eraseRepositoryCache() {
        File localFile = getLocalCacheFile();
        localFile.delete();
        msg.showDebugMessage("Cache erased");
    }
}
