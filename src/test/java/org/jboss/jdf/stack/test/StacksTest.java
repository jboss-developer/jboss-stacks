/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.it.Verifier;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.jboss.jdf.stacks.client.StacksClient;
import org.jboss.jdf.stacks.client.StacksClientConfiguration;
import org.jboss.jdf.stacks.model.Archetype;
import org.jboss.jdf.stacks.model.ArchetypeVersion;
import org.jboss.jdf.stacks.model.Bom;
import org.jboss.jdf.stacks.model.BomVersion;
import org.jboss.jdf.stacks.model.Runtime;
import org.jboss.jdf.stacks.model.Stacks;
import org.jboss.shrinkwrap.resolver.api.ResolutionException;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class StacksTest {

    private static StacksClient stacksClient;

    private static Log log = LogFactory.getLog(StacksTest.class);

    private String outputDir = System.getProperty("outPutDirectory");

    private String testOutputDirectory = System.getProperty("testOutputDirectory");

    private boolean skipArchetypeBuildTests = Boolean.parseBoolean(System.getProperty("skipArchetypeBuildTests"));

    private static File stacksFile;

    @BeforeClass
    public static void setupClient() throws MalformedURLException {

        stacksFile = new File("./stacks.yaml");

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
    public void testIdEqualsYamlLink() throws IOException {
        log.info("Testing if Ids are equals YAML links");
        BufferedReader br = new BufferedReader(new FileReader(stacksFile));
        try {
            String id = null;
            int lineNumber = 0;
            while (br.ready()) {
               lineNumber++;
                String line = br.readLine();
                if (line.contains("&")) {
                    int pos = line.indexOf('&') + 1;
                    id = line.substring(pos, line.length()).trim();
                }
                if (id != null && line.contains("id:")) {
                    int pos = line.lastIndexOf("id: ") + 3;
                    String value = line.substring(pos, line.length()).trim();
                    Assert.assertEquals("Id and Value should have the same value at line " + lineNumber, id, value);
                }
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    @Test
    public void testBomVersionUsed() {
        log.info("Test if a BomVersion is used on any Runtime");
        Stacks stacks = stacksClient.getStacks();
        for (BomVersion bomVersion : stacks.getAvailableBomVersions()) {
            boolean used = false;
            for (Runtime runtime : stacks.getAvailableRuntimes()) {
                if (runtime.getBoms() != null && runtime.getBoms().contains(bomVersion)) {
                    used = true;
                    break;
                }
            }
            Assert.assertTrue("BOM Version " + bomVersion + " must be used on any runtime", used);
        }
    }

    @Test
    public void testDefaultBomVersion() {
        log.info("Test if runtime default BOM is listed as a runtime BOM");
        for (Runtime runtime : stacksClient.getStacks().getAvailableRuntimes()) {
            if (runtime.getDefaultBom() != null && runtime.getBoms() != null) { // Some runtimes doesn't contain BOMs
                Assert.assertTrue("Runtime " + runtime + " default BOM is not one of runtime BOMS", runtime.getBoms().contains(runtime.getDefaultBom()));
            }
        }
    }

    @Test
    public void testDefaultArchetypeVersion() {
        log.info("Test if runtime default Archetype is listed as a runtime Archetype");
        for (Runtime runtime : stacksClient.getStacks().getAvailableRuntimes()) {
            if (runtime.getDefaultArchetype() != null && runtime.getArchetypes() != null) { // Some Runtimes doesn't have
                                                                                            // Archetypes
                Assert.assertTrue("Runtime " + runtime + " default Archetype is not one of runtime Archetypes", runtime.getArchetypes().contains(runtime.getDefaultArchetype()));
            }
        }
    }

    @Test
    public void testRecommendedBomVersions() {
        log.info("Testing if recommended Bom Version has the correspondent BomVersion");
        Stacks stacks = stacksClient.getStacks();
        for (Bom bom : stacks.getAvailableBoms()) {
            boolean hasBomRecommnendeVersion = false;
            String recommendedVersion = bom.getRecommendedVersion();
            for (BomVersion bomVersion : stacks.getAvailableBomVersions()) {
                if (bomVersion.getBom().equals(bom)) {
                    if (bomVersion.getVersion().equals(recommendedVersion)) {
                        hasBomRecommnendeVersion = true;
                    }
                }
            }
            Assert.assertTrue(bom + " recommendedVersion [" + recommendedVersion + "] doesn't have its correspondent BomVersion", hasBomRecommnendeVersion);
        }
    }

    @Test
    public void testRecommendedArchetypeVersions() {
        log.info("Testing if recommended Archetype Version has the correspondent ArchetypeVersion");
        Stacks stacks = stacksClient.getStacks();
        for (Archetype archetype : stacks.getAvailableArchetypes()) {
            boolean hasBomRecommnendeVersion = false;
            String recommendedVersion = archetype.getRecommendedVersion();
            for (ArchetypeVersion archetypeVersion : stacks.getAvailableArchetypeVersions()) {
                if (archetypeVersion.getArchetype().equals(archetype)) {
                    if (archetypeVersion.getVersion().equals(recommendedVersion)) {
                        hasBomRecommnendeVersion = true;
                    }
                }
            }
            Assert.assertTrue(archetype + " recommendedVersion [" + recommendedVersion + "] doesn't have its correspondent ArchetypeVersion", hasBomRecommnendeVersion);
        }
    }

    @Test
    public void testEarlyAcccessLabel() {
        log.info("Testing if all boms from a EarlyAccess runtime has also the EarlyAccess label");
        for (Runtime runtime : stacksClient.getStacks().getAvailableRuntimes()) {
            if (runtime.getLabels().get("EarlyAccess") != null) {
                for (BomVersion bomVersion : runtime.getBoms()) {
                    Assert.assertNotNull("A Bom " + bomVersion + " from a EarlyAccess Runtime " + runtime + " should also be labeled as EarlyAccess",
                        bomVersion.getLabels().get("EarlyAccess"));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBomResolver() {
        log.info("Testing if the BOM is resovable");
        Stacks stacks = stacksClient.getStacks();
        for (BomVersion bomVersion : stacks.getAvailableBomVersions()) {
            String artifact = String.format("%s:%s:pom:%s", bomVersion.getBom().getGroupId(), bomVersion.getBom().getArtifactId(), bomVersion.getVersion());
            try {
                if (bomVersion.getLabels().get("additionalRepositories") != null) {
                    Map<String, String> additionalRepositories = (Map<String, String>) bomVersion.getLabels().get("additionalRepositories");
                    ConfigurableMavenResolverSystem resolver = Maven.configureResolver();
                    for (String id : additionalRepositories.keySet()) {
                        String url = additionalRepositories.get(id);
                        resolver = resolver.withRemoteRepo(id, url, "default");
                    }
                    log.debug("Resolving BOM: " + artifact);
                    resolver.fromClassloaderResource("settings-centralonly.xml", getClass().getClassLoader()).resolve(artifact).withoutTransitivity().asFile();
                } else {
                    log.debug("Using none repository for " + bomVersion);
                    Maven.configureResolver().fromClassloaderResource("settings-centralonly.xml", getClass().getClassLoader()).resolve(artifact).withoutTransitivity().asFile();
                }
            } catch (ResolutionException e) {
                Assert.assertNull("Can't resolve Bom [" + artifact + "] - ID: " + bomVersion.getId(), e);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Assert.fail("Can't throw exception");
            }
        }
    }

    @Test
    public void testArchetypeResolver() {
        log.info("Testing if the Archetype is resovable");
        Stacks stacks = stacksClient.getStacks();
        for (ArchetypeVersion archetypeVersion : stacks.getAvailableArchetypeVersions()) {
            String artifact = String.format("%s:%s:%s", archetypeVersion.getArchetype().getGroupId(), archetypeVersion.getArchetype().getArtifactId(),
                archetypeVersion.getVersion());
            try {
                if (archetypeVersion.getRepositoryURL() != null) {
                    log.debug("Resolving Archetype: " + artifact);
                    Maven.configureResolver()
                        .withRemoteRepo("definedArchetypeURL", archetypeVersion.getRepositoryURL(), "default")
                        .fromClassloaderResource("settings-centralonly.xml", getClass().getClassLoader())
                        .resolve(artifact).withoutTransitivity().asFile();
                } else {
                    log.debug("Using none repository for " + archetypeVersion);
                    Maven.configureResolver().fromClassloaderResource("settings-centralonly.xml", getClass().getClassLoader()).resolve(artifact).withoutTransitivity().asFile();
                }
            } catch (ResolutionException e) {
                Assert.assertNull("Can't resolve Archetype [" + artifact + "] - ID:" + archetypeVersion.getId(), e);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Assert.fail("Can't throw exception");
            }
        }
    }

    @Test
    public void testRuntimeResolver() {
        log.info("Testing if the Runtime is resovable");
        Stacks stacks = stacksClient.getStacks();
        for (Runtime runtime : stacks.getAvailableRuntimes()) {
            // Only AS has maven artifact / EAP don't
            if (runtime.getLabels().get("runtime-type").equals("AS") && runtime.getGroupId() != null && runtime.getArtifactId() != null) {
                String artifact = String.format("%s:%s:pom:%s", runtime.getGroupId(), runtime.getArtifactId(), runtime.getVersion());
                try {
                    log.info("Resolving Runtime " + artifact);
                    Maven.configureResolver().withMavenCentralRepo(true).resolve(artifact).withoutTransitivity().asResolvedArtifact();
                } catch (ResolutionException e) {
                    Assert.assertNull("Can't resolve Runtime [" + artifact + "] ", e);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    Assert.fail("Can't throw exception");
                }
            }
        }
    }

    @Test
    public void testRuntimeCategoryLabel() {
        log.info("Testing if all runtimes have the 'runtime-category' label");
        Stacks stacks = stacksClient.getStacks();
        for (Runtime runtime : stacks.getAvailableRuntimes()) {
            Assert.assertNotNull(runtime + " should have 'runtime-category' label", runtime.getLabels().get("runtime-category"));
        }
    }

    @Test
    public void testArchetypesLabel() {
        log.info("Testing if all archetypes have the right labels");
        Stacks stacks = stacksClient.getStacks();
        String[] labels = new String[] { "type", "target", "environment" };
        for (ArchetypeVersion archetypeVersion : stacks.getAvailableArchetypeVersions()) {
            for (String label : labels) {
                Assert.assertNotNull(archetypeVersion + " should have '" + label + "' label", archetypeVersion.getLabels().get(label));
            }
        }
    }

    @Test
    public void testArchetypes() throws Exception {
        if (!skipArchetypeBuildTests) {
            Stacks stacks = stacksClient.getStacks();
            for (ArchetypeVersion archetypeVersion : stacks.getAvailableArchetypeVersions()) {
                executeCreateArchetype(archetypeVersion, false);
                executeCreateArchetype(archetypeVersion, true);
            }
        }
    }

    /**
     * @param archetypeVersion
     * @throws ComponentLookupException
     * @throws PlexusContainerException
     */
    private void executeCreateArchetype(ArchetypeVersion archetypeVersion, boolean eap) throws Exception {
        String archetype = String.format("%s:%s:%s", archetypeVersion.getArchetype().getGroupId(), archetypeVersion.getArchetype().getArtifactId(), archetypeVersion.getVersion());
        String archetypeWithEnterprise = archetype + (eap ? " - Enterprise" : "");
        log.info("Creating project from Archetype: " + archetypeWithEnterprise);
        String goal = "org.apache.maven.plugins:maven-archetype-plugin:2.2:generate";
        Properties properties = new Properties();
        if (eap) {
            properties.put("enterprise", "true");
        }
        properties.put("archetypeGroupId", archetypeVersion.getArchetype().getGroupId());
        properties.put("archetypeArtifactId", archetypeVersion.getArchetype().getArtifactId());
        properties.put("archetypeVersion", archetypeVersion.getVersion());
        properties.put("groupId", "org.jboss.as.quickstarts");
        String artifactId = System.currentTimeMillis() + "-" + archetype.replaceAll("[^a-zA-Z_0-9]", "") + (eap ? "-enterprise" : "");
        properties.put("artifactId", artifactId);
        properties.put("version", "0.0.1-SNAPSHOT");
        Verifier verifier = new org.apache.maven.it.Verifier(outputDir);
        verifier.setAutoclean(false);
        verifier.setSystemProperties(properties);
        verifier.setLogFileName(artifactId + "-generate.txt");
        verifier.executeGoal(goal);

        log.info("Building project from Archetype: " + archetypeWithEnterprise);
        Verifier buildVerifier = new Verifier(outputDir + File.separator + artifactId);
        buildVerifier.addCliOption("-s " + testOutputDirectory + File.separator + "settings-all.xml");
        buildVerifier.executeGoal("compile"); // buildVerifier log is inside each project
    }

}
