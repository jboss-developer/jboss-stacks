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

import java.util.List;


public class Stacks {

    private List<ServerRuntime> availableRuntimes;

    private List<Bom> availableBoms;

    private List<BomVersion> availableBomVersions;

    private List<Archetype> availableArchetypes;

    private List<ArchetypeVersion> availableArchetypeVersions;

    private List<MinorRelease> minorReleases;

    private List<MajorRelease> majorReleases;
    
    private List<String> licenses;

    public List<Archetype> getAvailableArchetypes() {
        return availableArchetypes;
    }

    public void setAvailableArchetypes(List<Archetype> availableArchetypes) {
        this.availableArchetypes = availableArchetypes;
    }

    public void setAvailableRuntimes(List<ServerRuntime> availableRuntimes) {
        this.availableRuntimes = availableRuntimes;
    }

    public List<ServerRuntime> getAvailableRuntimes() {
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
    
    public List<String> getLicenses() {
        return licenses;
    }
    
    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }
}
