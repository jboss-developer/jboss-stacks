/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013-2018 Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jdf.stack.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;
import org.jboss.jdf.stacks.client.StacksClient;
import org.jboss.jdf.stacks.client.StacksClientConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Basic verification for minishift.yaml stack file.
 * @author odockal@redhat.com
 *
 */
public class ContainerRuntimesTest extends AbstractStacksTest {

    @BeforeClass
    public static void setupClient() throws MalformedURLException {

    	log = LogFactory.getLog(ContainerRuntimesTest.class);
    	
        stacksFile = new File("./minishift.yaml");

        URL url = stacksFile.toURI().toURL();

        log.info("Testing file: " + url);

        stacksClient = new StacksClient();

        StacksClientConfiguration config = stacksClient.getActualConfiguration();

        config.setCacheRefreshPeriodInSeconds(-1);
        config.setUrl(url);
    }
    
    @Test
    public void testMinishiftRuntimesAvailability() throws IOException {
    	testStacksRuntimeContent("minishift", HttpURLConnection.HTTP_OK);
    }
    
    @Test
    public void testCDKRuntimesAvailability() throws IOException {
    	testStacksRuntimeContent("cdk", HttpURLConnection.HTTP_UNAUTHORIZED);
    }
    
    private void testStacksRuntimeContent(String runtimeFilter, int expectedCode) throws IOException {
    	for (org.jboss.jdf.stacks.model.Runtime runtime : stacksClient.getStacks().getAvailableRuntimes()) {
    		if (runtime.getId().contains(runtimeFilter)) {
	    		log.info("Testing runtimes url availability" + runtime.getName());
	    		Properties properties = runtime.getLabels();
	    		String [] urls = properties.get("additionalDownloadURLs").toString().replaceAll("[{}]", " ").split(",");
	    		Properties allUrls = retrieveAllURLs(urls);
	    		for (Object key : allUrls.keySet()) {
	    			verifyURL(allUrls.get(key).toString(), expectedCode);
	    		}
    		}
    	}    	
    }
    
    private void verifyURL(String uri, int expexctedStatusCode) throws IOException {
    	log.info("URL to check: " + uri);
    	HttpURLConnection connection = constructHttpRequest(uri, "GET");
		int responseCode = connection.getResponseCode();
		connection.disconnect();
		assertEquals("Http response status code for " + uri + " does not match", expexctedStatusCode, responseCode);
    }
    
    private HttpURLConnection constructHttpRequest(String uri, String method) throws IOException {
    	URL url = null;
    	try {
			url = new URL(uri);
		} catch (MalformedURLException e) {
			fail("Malformed URL: " + uri);
		}
    	return setupHttpURLConnection(url, method);
    }
    
    private HttpURLConnection setupHttpURLConnection(URL url, String method) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		HttpURLConnection.setFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("Accept", "application/xml");
		connection.setReadTimeout(30000);
		return connection;
    }
    
    private Properties retrieveAllURLs(String [] urls) {
		Properties props = new Properties();
		for (String item : urls) {
			String key = item.trim().split("=")[0];
			props.setProperty(key, item.trim().split("=")[1]);
		}
		return props;
    }

}
