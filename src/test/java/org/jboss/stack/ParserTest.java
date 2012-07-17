package org.jboss.stack;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.jboss.stack.parser.Parser;
import org.jboss.stack.parser.YamlStacksParser;
import org.junit.Before;
import org.junit.Test;

public class ParserTest
{

   private Parser parser;
   private InputStream is;

   @Before
   public void setUp() throws Exception
   {
      parser = new YamlStacksParser();
      is = this.getClass().getResourceAsStream("/stacks.yaml");
   }

   @Test
   public void testParse()
   {
      Stacks stacks = parser.parse(is);
      assertEquals(6, stacks.getAvailableBoms().size());
      assertEquals(2, stacks.getAvailableRuntimes().size());
      assertEquals(RuntimeType.EAP, stacks.getAvailableRuntimes().get(0).getType());
   }

}
