/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.jdf.stacks.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Runtime {

    private String name;

    private String groupId;

    private String artifactId;

    private String version;

    private String url;

    private String downloadUrl;

    private Properties labels = new Properties();
    
    private List<BomVersion> boms = new ArrayList<BomVersion>();

    private BomVersion defaultBom;

    private List<ArchetypeVersion> archetypes = new ArrayList<ArchetypeVersion>();

    private ArchetypeVersion defaultArchetype;
    
    private String license;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Properties getLabels() {
        return labels;
    }

    public void setLabels(Properties labels) {
        this.labels = labels;
    }

    public List<BomVersion> getBoms() {
        return boms;
    }

    public void setBoms(List<BomVersion> boms) {
        this.boms = boms;
    }

    public BomVersion getDefaultBom() {
        return defaultBom;
    }

    public void setDefaultBom(BomVersion defaultBom) {
        this.defaultBom = defaultBom;
    }

    public List<ArchetypeVersion> getArchetypes() {
        return archetypes;
    }

    public void setArchetypes(List<ArchetypeVersion> archetypes) {
        this.archetypes = archetypes;
    }

    public ArchetypeVersion getDefaultArchetype() {
        return defaultArchetype;
    }

    public void setDefaultArchetype(ArchetypeVersion defaultArchetype) {
        this.defaultArchetype = defaultArchetype;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

   
}
