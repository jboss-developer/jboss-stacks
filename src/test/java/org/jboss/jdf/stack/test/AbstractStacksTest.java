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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jboss.jdf.stacks.client.StacksClient;
import org.jboss.jdf.stacks.model.Runtime;
import org.jboss.jdf.stacks.model.Stacks;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.logging.Log;

/**
 * Abstract stacks test class
 * @author odockal@redhat.com
 *
 */
public abstract class AbstractStacksTest {

    protected static StacksClient stacksClient;

    protected static File stacksFile;
    
    protected static Log log;

    @Test
    public void testBasicParse() {
        log.info("Testing Basic parsing");
        stacksClient.getStacks();
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
}
