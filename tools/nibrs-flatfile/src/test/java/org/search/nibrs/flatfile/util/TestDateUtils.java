package org.search.nibrs.flatfile.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.search.nibrs.flatfile.util.DateUtils;

public class TestDateUtils
{

    @Test
    public void testMakeDate()
    {
        Date d = DateUtils.makeDate(2005, Calendar.JANUARY, 20);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        assertEquals(c.get(Calendar.YEAR), 2005);
        assertEquals(c.get(Calendar.MONTH), Calendar.JANUARY);
        assertEquals(c.get(Calendar.DAY_OF_MONTH), 20);
    }
    
    @Test
    public void testConvertMonthValue()
    {
        assertEquals(Calendar.JANUARY, DateUtils.convertMonthValue(1));
    }

}
