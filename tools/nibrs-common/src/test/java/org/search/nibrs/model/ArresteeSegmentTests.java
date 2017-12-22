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
package org.search.nibrs.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArresteeSegmentTests {
	
	@Test
	public void testIsJuvenile() {
		ArresteeSegment as = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(as.isJuvenile());
		as.setAge(NIBRSAge.getNewbornAge());
		assertTrue(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(1, null));
		assertTrue(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(17, null));
		assertTrue(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(18, null));
		assertFalse(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(0, null));
		assertFalse(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(14, 15));
		assertTrue(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(14, 19));
		assertTrue(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(14, 39));
		assertFalse(as.isJuvenile());
		as.setAge(NIBRSAge.getAge(18, 39));
		assertFalse(as.isJuvenile());
	}

}
