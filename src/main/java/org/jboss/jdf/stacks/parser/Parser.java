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
package org.jboss.jdf.stacks.parser;

import java.io.InputStream;

import org.jboss.jdf.stacks.model.Archetype;
import org.jboss.jdf.stacks.model.ArchetypeVersion;
import org.jboss.jdf.stacks.model.Bom;
import org.jboss.jdf.stacks.model.BomVersion;
import org.jboss.jdf.stacks.model.MajorRelease;
import org.jboss.jdf.stacks.model.MinorRelease;
import org.jboss.jdf.stacks.model.ServerRuntime;
import org.jboss.jdf.stacks.model.Stacks;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;

/**
 * This a Parser for the JBoss Stacks YAML version Beta2
 * 
 * @author benevides@redhat.com
 * 
 */
public class Parser {

    public Stacks parse(InputStream is) {
        CustomClassloaderConstructor constructor = new CustomClassloaderConstructor(Stacks.class, this.getClass()
                .getClassLoader());
        TypeDescription stackDescription = new TypeDescription(Stacks.class);
        stackDescription.putListPropertyType("availableBoms", Bom.class);
        stackDescription.putListPropertyType("availableBomVersions", BomVersion.class);
        stackDescription.putListPropertyType("availableRuntimes", ServerRuntime.class);
        stackDescription.putListPropertyType("availableArchetypes", Archetype.class);
        stackDescription.putListPropertyType("availableArchetypeVersions", ArchetypeVersion.class);
        stackDescription.putListPropertyType("minorReleases", MinorRelease.class);
        stackDescription.putListPropertyType("majorReleases", MajorRelease.class);
   
        constructor.addTypeDescription(stackDescription);
        Yaml yaml = new Yaml(constructor);
        Stacks data = (Stacks) yaml.load(is);
        return data;
    }


 
}
