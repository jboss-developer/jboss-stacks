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

package org.jboss.jdf.stack.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jdf.stacks.client.StacksClient;
import org.jboss.jdf.stacks.client.StacksClientConfiguration;
import org.jboss.jdf.stacks.model.Archetype;
import org.jboss.jdf.stacks.model.ArchetypeVersion;
import org.jboss.jdf.stacks.model.Bom;
import org.jboss.jdf.stacks.model.BomVersion;
import org.jboss.jdf.stacks.model.Runtime;
import org.jboss.jdf.stacks.model.Stacks;
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

    @BeforeClass
    public static void setupClient() throws MalformedURLException {
        
        // suppress shrinkwrap warning messages
        Logger shrinkwrapLogger = Logger.getLogger("org.jboss.shrinkwrap.resolver");
        shrinkwrapLogger.setLevel(Level.SEVERE);
        
        URL url = new File("./stacks.yaml").toURI().toURL();
        
        log.info("Testing file: " + url);

        stacksClient = new StacksClient();

        StacksClientConfiguration config = stacksClient.getActualConfiguration();

        config.setCacheRefreshPeriodInSeconds(-1);
        config.setUrl(url);
    }

    @Test
    public void testBasicParse() {
        stacksClient.getStacks();
    }

    @Test
    public void testRecommendedBomVersions() {
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
            Assert.assertTrue(bom + " recommendedVersion [" + recommendedVersion
                    + "] doesn't have its correspondent BomVersion", hasBomRecommnendeVersion);
        }
    }

    @Test
    public void testRecommendedArchetypeVersions() {
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
            Assert.assertTrue(archetype + " recommendedVersion [" + recommendedVersion
                    + "] doesn't have its correspondent ArchetypeVersion", hasBomRecommnendeVersion);
        }
    }

    @Test
    public void testBomResolver() {
        Stacks stacks = stacksClient.getStacks();
        for (BomVersion bomVersion : stacks.getAvailableBomVersions()) {
            String artifact = String.format("%s:%s:pom:%s", bomVersion.getBom().getGroupId(), bomVersion.getBom()
                    .getArtifactId(), bomVersion.getVersion());
            try {
                Maven.resolver().resolve(artifact).withMavenCentralRepo(true).withoutTransitivity().asFile();
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder("Can't resolve BOM [" + artifact + "] ");
                // Help the user to debug why the test is falling
                if (bomVersion.getLabels().get("WFK2RepositoryRequired") != null) {
                    sb.append(" - NOTE: This artifact needs a WFK 2.0 and EAP 6.0.0 repositories");
                }
                if (bomVersion.getLabels().get("WFK21RepositoryRequired") != null) {
                    sb.append(" - NOTE: This artifact needs a WFK 2.1 and EAP 6.0.0 repositories");
                }
                if (bomVersion.getLabels().get("EAP600RepositoryRequired") != null) {
                    sb.append(" - NOTE: This artifact needs a EAP 6.0.0 repository");
                }
                if (bomVersion.getLabels().get("EAP601RepositoryRequired") != null) {
                    sb.append(" - NOTE: This artifact needs a EAP 6.0.1 repository");
                }
                Assert.assertNull(sb.toString(), e);
            }
        }
    }

    @Test
    public void testArchetypeResolver() {
        Stacks stacks = stacksClient.getStacks();
        for (ArchetypeVersion archetypeVersion : stacks.getAvailableArchetypeVersions()) {
            String artifact = String.format("%s:%s:%s", archetypeVersion.getArchetype().getGroupId(), archetypeVersion
                    .getArchetype().getArtifactId(), archetypeVersion.getVersion());
            try {
                Maven.resolver().resolve(artifact).withMavenCentralRepo(true).withoutTransitivity().asFile();
            } catch (Exception e) {
                Assert.assertNull("Can't resolve Archetype [" + artifact + "] ", e);
            }
        }
    }
    
    @Test
    public void testRuntimeResolver() {
        Stacks stacks = stacksClient.getStacks();
        for (Runtime runtime : stacks.getAvailableRuntimes()) {
            //Only AS has maven artifact / EAP don't
            if (runtime.getLabels().get("runtime-type").equals("AS")){
                String artifact = String.format("%s:%s:pom:%s", runtime.getGroupId(), runtime.getArtifactId(), runtime.getVersion());
                try {
                    Maven.resolver().resolve(artifact).withMavenCentralRepo(true).withoutTransitivity().asResolvedArtifact();
                } catch (Exception e) {
                    Assert.assertNull("Can't resolve Runtime [" + artifact + "] ", e);
                }
            }
        }
    }
}
