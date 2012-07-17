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
package org.jboss.stack;

import java.util.ArrayList;
import java.util.List;

public class Runtime
{

   private String name;
   private String version;
   private String url;
   private RuntimeType type;
   private List<String> labels = new ArrayList<String>();

   private List<Bom> boms = new ArrayList<Bom>();
   private Bom defaultBom;

   private List<Archetype> archetypes = new ArrayList<Archetype>();
   private Archetype defaultArchetype;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getVersion()
   {
      return version;
   }

   public void setVersion(String version)
   {
      this.version = version;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
   }

   public RuntimeType getType()
   {
      return type;
   }

   public void setType(RuntimeType type)
   {
      this.type = type;
   }

   public List<String> getLabels()
   {
      return labels;
   }

   public void setLabels(List<String> labels)
   {
      this.labels = labels;
   }

   public List<Bom> getBoms()
   {
      return boms;
   }

   public void setBoms(List<Bom> boms)
   {
      this.boms = boms;
   }

   public Bom getDefaultBom()
   {
      return defaultBom;
   }

   public void setDefaultBom(Bom defaultBom)
   {
      this.defaultBom = defaultBom;
   }

   public List<Archetype> getArchetypes()
   {
      return archetypes;
   }

   public void setArchetypes(List<Archetype> archetypes)
   {
      this.archetypes = archetypes;
   }

   public void setDefaultArchetype(Archetype defaultArchetype)
   {
      this.defaultArchetype = defaultArchetype;
   }

   public Archetype getDefaultArchetype()
   {
      return defaultArchetype;
   }

}
