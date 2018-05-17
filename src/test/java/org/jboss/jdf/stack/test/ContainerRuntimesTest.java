package org.jboss.jdf.stack.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
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
public class ContainerRuntimesTest {

    private static StacksClient stacksClient;

    private static Log log = LogFactory.getLog(ContainerRuntimesTest.class);

    private static File stacksFile;

    @BeforeClass
    public static void setupClient() throws MalformedURLException {

        stacksFile = new File("./minishift.yaml");

        URL url = stacksFile.toURI().toURL();

        log.info("Testing file: " + url);

        stacksClient = new StacksClient();

        StacksClientConfiguration config = stacksClient.getActualConfiguration();

        config.setCacheRefreshPeriodInSeconds(-1);
        config.setUrl(url);
    }
    
    @Test
    public void testBasicParse() {
        log.info("Testing Basic parsing");
        stacksClient.getStacks();
    }
    
    @Test
    public void testMinishiftRuntimesAvailability() throws IOException {
    	log.info("Testing runtimes url availability");
    	for (org.jboss.jdf.stacks.model.Runtime runtime : stacksClient.getStacks().getAvailableRuntimes()) {
    		if (runtime.getId().contains("minishift")) {
	    		log.info("Verifying runtime: " + runtime.getName());
	    		Properties properties = runtime.getLabels();
	    		String [] urls = properties.get("additionalDownloadURLs").toString().replaceAll("[{}]", " ").split(",");
	    		Properties allUrls = retrieveAllURLs(urls);
	    		for (Object key : allUrls.keySet()) {
	    			verifyURL(allUrls.get(key).toString());
	    		}
    		}
    	}
    }
    
    private void verifyURL(String uri) throws IOException {
    	log.info("URL to check: " + uri);
    	URL url = null;
    	try {
			url = new URL(uri);
		} catch (MalformedURLException e) {
			fail("Malformed URL: " + uri);
		}
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		HttpURLConnection.setFollowRedirects(true);
		int responseCode = connection.getResponseCode();
		log.info("HTTP response code: " + responseCode);
		assertEquals("Http Get response code is not 200 for " + uri, HttpURLConnection.HTTP_OK, responseCode);
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
