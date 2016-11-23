/*******************************************************************************
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArresteeSegmentTests {
	
	@Test
	public void testIsJuvenile() {
		ArresteeSegment as = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(as.isJuvenile());
		as.setAgeString("NB");
		assertTrue(as.isJuvenile());
		as.setAgeString("01");
		assertTrue(as.isJuvenile());
		as.setAgeString("17");
		assertTrue(as.isJuvenile());
		as.setAgeString("18");
		assertFalse(as.isJuvenile());
		as.setAgeString("00");
		assertFalse(as.isJuvenile());
		as.setAgeString("1415");
		assertTrue(as.isJuvenile());
		as.setAgeString("1419");
		assertTrue(as.isJuvenile());
		as.setAgeString("1439");
		assertFalse(as.isJuvenile());
		as.setAgeString("1819");
		assertFalse(as.isJuvenile());
	}

}
