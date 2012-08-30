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

package org.jboss.jdf.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.jboss.jdf.stacks.client.StacksClient;
import org.jboss.jdf.stacks.client.StacksClientConfiguration;
import org.jboss.jdf.stacks.model.Stacks;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class StacksClientTest {

    /**
     * Setup to print debug messages on Console
     */
    @BeforeClass
    public static void setup() {
        Logger jdfLogger = Logger.getLogger("org.jboss");
        Logger rootLogger = Logger.getLogger("");
        jdfLogger.setLevel(Level.ALL);
        for (Handler handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.ALL);
            }
        }
        Assert.assertTrue(jdfLogger.isLoggable(Level.FINEST));
    }

    @Test
    public void testGetStacks() {
        StacksClient stacksClient = new StacksClient();
        Stacks stacks = stacksClient.getStacks();
        Assert.assertNotNull("Stacks should be available", stacks);
    }

    @Test
    public void testGetStacksAfterCacheErased() {
        StacksClient stacksClient = new StacksClient();
        stacksClient.eraseRepositoryCache();
        Stacks stacks = stacksClient.getStacks();
        Assert.assertNotNull("Stacks should be working after cache erased", stacks);
    }

    @Test
    public void testGetLocalCacheFile() {
        StacksClient stacksClient = new StacksClient();
        stacksClient.eraseRepositoryCache();
        File localCacheFile = stacksClient.getLocalCacheFile();
        // Local cached should be erased
        Assert.assertFalse("Cache file should be erased", localCacheFile.exists());
        // Force initilization
        stacksClient.getStacks();
        Assert.assertTrue("Cache should be created after new initialization", localCacheFile.exists());
    }

    @Test
    public void testCacheUse() {
        StacksClient stacksClient = new StacksClient();
        // Get Stacks - Force cache creation
        stacksClient.getStacks();
        long lastChangedCache = stacksClient.getLocalCacheFile().lastModified();
        stacksClient.getStacks();
        long lastChangedCache2 = stacksClient.getLocalCacheFile().lastModified();

        Assert.assertEquals("Cache should be the same", lastChangedCache, lastChangedCache2);
    }

    @Test
    public void testCacheInvalidation() {
        StacksClient stacksClient = new StacksClient();
        stacksClient.getActualConfiguration().setCacheRefreshPeriodInSeconds(-1);
        stacksClient.getStacks();
        long lastChangedCache = stacksClient.getLocalCacheFile().lastModified();
        stacksClient.getStacks();
        long lastChangedCache2 = stacksClient.getLocalCacheFile().lastModified();

        Assert.assertTrue("Cache should be recreated", lastChangedCache != lastChangedCache2);
    }

    @Test
    public void testStacksWrongURL() throws MalformedURLException {
        StacksClient stacksClient = new StacksClient();
        stacksClient.getActualConfiguration().setUrl(new URL("file:///nonexistingfile.yaml"));
        try {
            stacksClient.getStacks();
            Assert.fail("should fail with wrong url");
        } catch (IllegalStateException e) {
            Assert.assertNotNull("No file available to get stacks information", e);
        }
    }

    @Test
    public void testCacheFallbackOfflineUse() throws MalformedURLException {
        StacksClient stacksClient = new StacksClient();
        stacksClient.eraseRepositoryCache();
        stacksClient.getStacks();
        StacksClientConfiguration config = stacksClient.getActualConfiguration();
        config.setCacheRefreshPeriodInSeconds(0);
        config.setOnline(false);
        Stacks stacks = stacksClient.getStacks();
        Assert.assertNotNull("Stacks should be available in forced offline use", stacks);
    }

    @Test
    public void testCacheFallbackFailNoCache() throws MalformedURLException {
        StacksClient stacksClient = new StacksClient();
        stacksClient.eraseRepositoryCache();
        stacksClient.getStacks();
        StacksClientConfiguration config = stacksClient.getActualConfiguration();
        config.setCacheRefreshPeriodInSeconds(0);
        config.setOnline(false);
        stacksClient.eraseRepositoryCache();
        try {
            stacksClient.getStacks();
            Assert.fail("should fail because we had no cache and it's not online");
        } catch (IllegalStateException e) {
            Assert.assertNotNull("Not online and no cache to use", e);
        }
    }

}
