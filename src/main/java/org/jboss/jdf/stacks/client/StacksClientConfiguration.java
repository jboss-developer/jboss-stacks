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

import java.net.URL;

/**
 * A configuration for the stacks.
 * 
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 */
public interface StacksClientConfiguration {
    
    public final String FORMAT = "Beta6";

    public final String DEFAULT_STACKS_REPO = "https://raw.github.com/jboss-jdf/jdf-stack/" + FORMAT +  "/stacks.yaml";

    /**
     * The URL to connect to.
     * 
     * @return the url
     */
    public URL getUrl();

    /**
     * Set the URL to connect to
     * 
     * @param url
     */
    public void setUrl(URL url);

    /**
     * The proxy host.
     * 
     * @return the proxy host or {@code null} if no proxy is specified
     */
    public String getProxyHost();

    /**
     * Set the proxy host
     * 
     * @param proxyHost
     */
    public void setProxyHost(String proxyHost);

    /**
     * The proxy port.
     * 
     * @return the proxy port or 0 if not specified
     */
    public int getProxyPort();

    /**
     * Set the proxy port
     * 
     * @param proxyPort
     */
    public void setProxyPort(int proxyPort);

    /**
     * The proxy connection user.
     * 
     * @return the user or {@code null} if not specified
     */
    public String getProxyUser();

    /**
     * Set the proxy connection user.
     * 
     * @param proxyUser
     */
    public void setProxyUser(String proxyUser);

    /**
     * The proxy connection password.
     * 
     * @return the password or {@code null} if not specified
     */
    public String getProxyPassword();

    /**
     * Set the proxy connection password
     * 
     * @param proxyPassword
     */
    public void setProxyPassword(String proxyPassword);
    
    
    /**
     * If this client is using in a online/offline environment
     * 
     * @return
     */
    public boolean isOnline();
    
    /**
     * Set online/offline environment
     * 
     * @param online
     */
    public void setOnline(boolean online);
    
    /**
     * Refresh period of local cache.
     * Default to 86400 seconds (24 hours).
     * Use -1 to disable cache.
     * 
     * @return
     */
    public int getCacheRefreshPeriodInSeconds();
    
    
    /**
     * Set the refresh period of local cache in seconds
     * 
     * @param seconds
     */
    public void setCacheRefreshPeriodInSeconds(int seconds);
    
}
