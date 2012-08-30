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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 * 
 */
public class DefaultStacksClientConfiguration implements StacksClientConfiguration {

    private URL url;

    private String proxyHost;

    private int proxyPort;

    private String proxyUser;

    private String proxyPassword;

    private boolean online = true;

    private int cacheRefreshPeriodSeconds = 86400;

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#getUrl()
     */
    @Override
    public URL getUrl() {
        if (url == null) {
            try {
                return new URL(DEFAULT_STACKS_REPO);
            } catch (MalformedURLException e) {
                throw new IllegalStateException(String.format("Could not create URL from '%s'", DEFAULT_STACKS_REPO));
            }
        } else {
            return url;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#getProxyHost()
     */
    @Override
    public String getProxyHost() {
        return proxyHost;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#getProxyPort()
     */
    @Override
    public int getProxyPort() {
        return proxyPort;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#getProxyUser()
     */
    @Override
    public String getProxyUser() {
        return proxyUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#getProxyPassword()
     */
    @Override
    public String getProxyPassword() {
        return proxyPassword;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#setUrl(java.net.URL)
     */
    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#setProxyHost(java.lang.String)
     */
    @Override
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#setProxyPort(int)
     */
    @Override
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#setProxyUser(java.lang.String)
     */
    @Override
    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#setProxyPassword(java.lang.String)
     */
    @Override
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#isOnline()
     */
    @Override
    public boolean isOnline() {
        return online;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#setOnline(boolean)
     */
    @Override
    public void setOnline(boolean online) {
        this.online = online;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#getCacheRefreshPeriodoInSeconds()
     */
    @Override
    public int getCacheRefreshPeriodInSeconds() {
        return cacheRefreshPeriodSeconds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.jdf.stacks.client.StacksClientConfiguration#setCacheRefreshPeriodInSeconds()
     */
    @Override
    public void setCacheRefreshPeriodInSeconds(int seconds) {
        this.cacheRefreshPeriodSeconds = seconds;

    }

}
