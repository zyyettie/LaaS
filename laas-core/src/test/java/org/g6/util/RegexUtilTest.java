package org.g6.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegexUtilTest {

    @Test
    public void testGetValues() {
        String str = "abc 123 456";
        String regex = "(abc) (123) (456)";

        String[] splits = RegexUtil.getValues(str, regex);

        assertEquals("abc", splits[0]);
        assertEquals("456", splits[2]);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
