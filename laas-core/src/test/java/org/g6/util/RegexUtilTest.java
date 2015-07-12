package org.g6.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegexUtilTest {

    @Test
    public void testGetValues() {
        String str = "abc 123 456";
        String regex = "(abc) (123) (456)";

        String[] splits = RegexUtil.getValues(str, regex);

        assertEquals("abc", splits[0]);
        assertEquals("456", splits[2]);
    }

    @Test
    public void testIsMatched(){
        String str = "abc 123 456";

        assertTrue(RegexUtil.isMatched(str, "123"));
        assertTrue(RegexUtil.isMatched(str, "abc 123"));
        assertFalse(RegexUtil.isMatched(str, "abc123"));
    }

    @Test
    public void testIsFullyMatched(){
        String str = "abc 123 456";
        assertFalse(RegexUtil.isFullyMatched(str, "abc"));
        assertFalse(RegexUtil.isFullyMatched(str, "abc 123"));
        assertTrue(RegexUtil.isFullyMatched(str, "abc 123 456"));
    }

    @Test
    public void testGetValue(){
        String str = "abc 123 def 456";
        String regex = "123 def";

        assertEquals("123 def", RegexUtil.getValue(str,regex));
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
