/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
