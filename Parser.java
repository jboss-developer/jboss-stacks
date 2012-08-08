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
package org.jboss.stacks.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * This a Parser for the JBoss Stacks YAML version Beta2
 * 
 * @author benevides@redhat.com
 * 
 */
public class Parser {

    public Stacks parse(InputStream is) {
        CustomClassLoaderConstructor constructor = new CustomClassLoaderConstructor(Stacks.class, this.getClass()
                .getClassLoader());
        TypeDescription stackDescription = new TypeDescription(Stacks.class);
        stackDescription.putListPropertyType("availableBoms", Bom.class);
        stackDescription.putListPropertyType("availableBomVersions", BomVersion.class);
        stackDescription.putListPropertyType("availableRuntimes", Runtime.class);
        stackDescription.putListPropertyType("availableArchetypes", Archetype.class);
        stackDescription.putListPropertyType("availableArchetypeVersions", ArchetypeVersion.class);
        stackDescription.putListPropertyType("minorReleases", MinorRelease.class);
        stackDescription.putListPropertyType("majorReleases", MajorRelease.class);

        constructor.addTypeDescription(stackDescription);
        Yaml yaml = new Yaml(constructor);
        Stacks data = (Stacks) yaml.load(is);
        return data;
    }

    public class CustomClassLoaderConstructor extends Constructor {
        private ClassLoader loader = CustomClassLoaderConstructor.class.getClassLoader();

        public CustomClassLoaderConstructor(ClassLoader cLoader) {
            this(Object.class, cLoader);
        }

        public CustomClassLoaderConstructor(Class<? extends Object> theRoot, ClassLoader theLoader) {
            super(theRoot);
            if (theLoader == null) {
                throw new NullPointerException("Loader must be provided.");
            }
            this.loader = theLoader;
        }

        @Override
        protected Class<?> getClassForName(String name) throws ClassNotFoundException {
            return Class.forName(name, true, loader);
        }
    }

    public static class BomVersion {

        private Bom bom;

        private String version;

        private List<Properties> labels = new ArrayList<Properties>();

        public Bom getBom() {
            return bom;
        }

        public void setBom(Bom bom) {
            this.bom = bom;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<Properties> getLabels() {
            return labels;
        }

        public void setLabels(List<Properties> labels) {
            this.labels = labels;
        }

    }

    public static class Bom {

        private String name;

        private String description;

        private String groupId = "org.jboss.bom";

        private String artifactId;

        private String recommendedVersion;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            if (groupId != null) {
                this.groupId = groupId;
            }
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        public String getRecommendedVersion() {
            return recommendedVersion;
        }

        public void setRecommendedVersion(String recommendedVersion) {
            this.recommendedVersion = recommendedVersion;
        }

        @Override
        public String toString() {
            return this.getArtifactId();
        }
    }

    public static class Stacks {

        private List<Runtime> availableRuntimes;

        private List<Bom> availableBoms;

        private List<BomVersion> availableBomVersions;

        private List<Archetype> availableArchetypes;

        private List<ArchetypeVersion> availableArchetypeVersions;

        private List<MinorRelease> minorReleases;

        private List<MajorRelease> majorReleases;

        public List<Archetype> getAvailableArchetypes() {
            return availableArchetypes;
        }

        public void setAvailableArchetypes(List<Archetype> availableArchetypes) {
            this.availableArchetypes = availableArchetypes;
        }

        public void setAvailableRuntimes(List<Runtime> availableRuntimes) {
            this.availableRuntimes = availableRuntimes;
        }

        public List<Runtime> getAvailableRuntimes() {
            return availableRuntimes;
        }

        public List<Bom> getAvailableBoms() {
            return availableBoms;
        }

        public void setAvailableBoms(List<Bom> availableBoms) {
            this.availableBoms = availableBoms;
        }

        public List<MinorRelease> getMinorReleases() {
            return minorReleases;
        }

        public void setMinorReleases(List<MinorRelease> minorReleases) {
            this.minorReleases = minorReleases;
        }

        public List<MajorRelease> getMajorReleases() {
            return majorReleases;
        }

        public void setMajorReleases(List<MajorRelease> majorReleases) {
            this.majorReleases = majorReleases;
        }

        public List<BomVersion> getAvailableBomVersions() {
            return availableBomVersions;
        }

        public void setAvailableBomVersions(List<BomVersion> availableBomVersions) {
            this.availableBomVersions = availableBomVersions;
        }

        public List<ArchetypeVersion> getAvailableArchetypeVersions() {
            return availableArchetypeVersions;
        }

        public void setAvailableArchetypeVersions(List<ArchetypeVersion> availableArchetypeVersions) {
            this.availableArchetypeVersions = availableArchetypeVersions;
        }
    }

    public static enum RuntimeType {
        EAP, AS
    }

    public static class Runtime {

        private String name;
        private String groupId;
        private String artifactId;
        private String version;
        private String url;
        private RuntimeType type;
        private List<Properties> labels = new ArrayList<Properties>();

        private List<BomVersion> boms = new ArrayList<BomVersion>();
        private BomVersion defaultBom;

        private List<ArchetypeVersion> archetypes = new ArrayList<ArchetypeVersion>();
        private ArchetypeVersion defaultArchetype;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(final String groupId) {
            this.groupId = groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(final String artifactId) {
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

        public RuntimeType getType() {
            return type;
        }

        public void setType(RuntimeType type) {
            this.type = type;
        }

        public List<Properties> getLabels() {
            return labels;
        }

        public void setLabels(List<Properties> labels) {
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

        public void setDefaultArchetype(ArchetypeVersion defaultArchetype) {
            this.defaultArchetype = defaultArchetype;
        }

        public ArchetypeVersion getDefaultArchetype() {
            return defaultArchetype;
        }

    }

    public static class MinorRelease {

        private String name;

        private String version;

        private Runtime recommendedRuntime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Runtime getRecommendedRuntime() {
            return recommendedRuntime;
        }

        public void setRecommendedRuntime(Runtime recommendedRuntime) {
            this.recommendedRuntime = recommendedRuntime;
        }

    }

    public static class MajorRelease {

        private String name;

        private String version;

        private Runtime recommendedRuntime;

        private List<MinorRelease> minorReleases = new ArrayList<MinorRelease>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Runtime getRecommendedRuntime() {
            return recommendedRuntime;
        }

        public void setRecommendedRuntime(Runtime recommendedRuntime) {
            this.recommendedRuntime = recommendedRuntime;
        }

        public List<MinorRelease> getMinorReleases() {
            return minorReleases;
        }

        public void setMinorReleases(List<MinorRelease> minorReleases) {
            this.minorReleases = minorReleases;
        }
    }

    public static class ArchetypeVersion {
        private Archetype archetype;

        private String version;

        private List<Properties> labels = new ArrayList<Properties>();

        public Archetype getArchetype() {
            return archetype;
        }

        public void setArchetype(Archetype archetype) {
            this.archetype = archetype;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<Properties> getLabels() {
            return labels;
        }

        public void setLabels(List<Properties> labels) {
            this.labels = labels;
        }

    }

    public static class Archetype {

        private String name;

        private String description;

        private String groupId;

        private String artifactId;

        private String recommendedVersion;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getRecommendedVersion() {
            return recommendedVersion;
        }

        public void setRecommendedVersion(String recommendedVersion) {
            this.recommendedVersion = recommendedVersion;
        }

    }

}
