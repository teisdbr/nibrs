package org.search.nibrs.flatfile.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.search.nibrs.flatfile.util.DateUtils;
import org.search.nibrs.flatfile.util.StringUtils;

public class TestStringUtils
{

	@Test
    public void testGetStringBetween()
    {
        assertEquals("a", StringUtils.getStringBetween(1, 1, "abc"));
        assertEquals("b", StringUtils.getStringBetween(2, 2, "abc"));
        assertEquals("ab", StringUtils.getStringBetween(1, 2, "abc"));
    }
    
	@Test
    public void testNullHandling()
    {
        assertNull(StringUtils.getStringBetween(1, 1, ""));
        assertNull(StringUtils.getStringBetween(1, 2, "       "));
        assertNull(StringUtils.getStringBetween(1, 1, null));
    }
    
	@Test
    public void testGetDateBetween()
    {
        assertEquals(DateUtils.makeDate(2005, Calendar.JANUARY, 5), StringUtils.getDateBetween(1, 8, "20050105"));
        assertNull(StringUtils.getDateBetween(1, 1, ""));
        assertNull(StringUtils.getDateBetween(1, 8, "        "));
    }
    
	@Test
    public void testGetIntegerBetween()
    {
        assertEquals(new Integer(10), StringUtils.getIntegerBetween(1, 2, "10"));
        assertNull(StringUtils.getIntegerBetween(1, 1, " "));
    }

}
