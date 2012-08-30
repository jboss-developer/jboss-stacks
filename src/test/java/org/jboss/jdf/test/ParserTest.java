package org.jboss.jdf.test;

import java.io.FileNotFoundException;
import java.io.InputStream;

import junit.framework.Assert;

import org.jboss.jdf.stacks.model.Stacks;
import org.jboss.jdf.stacks.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void test() throws FileNotFoundException {
        InputStream is = this.getClass().getResourceAsStream("/stacks.yaml");
        Parser p = new Parser();
        Stacks stacks = p.parse(is);
        Assert.assertEquals(stacks.getAvailableBoms().size(), 7);
    }
}
