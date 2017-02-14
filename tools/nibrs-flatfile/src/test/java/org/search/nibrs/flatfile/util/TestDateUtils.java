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
