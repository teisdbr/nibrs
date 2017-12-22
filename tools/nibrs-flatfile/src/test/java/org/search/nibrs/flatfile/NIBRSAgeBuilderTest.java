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
package org.search.nibrs.flatfile;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class NIBRSAgeBuilderTest {
	
	private static final VictimSegment TEST_SEGMENT = new VictimSegment();
	
	@Test
	public void testInvalidString() {
		NIBRSAge a = NIBRSAgeBuilder.buildAgeFromRawString("AA", TEST_SEGMENT);
		NIBRSError e = a.getError();
		assertNotNull(e);
		assertFalse(a.isNonNumeric());
		assertFalse(a.isUnknown());
		assertNull(a.getAgeMin());
		assertNull(a.getAgeMax());
		assertTrue(a.isInvalid());
		a = NIBRSAgeBuilder.buildAgeFromRawString("123", TEST_SEGMENT);
		e = a.getError();
		assertNotNull(e);
		assertTrue(a.isInvalid());
	}
	
	@Test
	public void testAverage() {
		NIBRSAge a = NIBRSAgeBuilder.buildAgeFromRawString("NB", TEST_SEGMENT);
		Integer avg = a.getAverage();
		assertNull(avg);
		a = NIBRSAgeBuilder.buildAgeFromRawString("22  ", TEST_SEGMENT);
		avg = a.getAverage();
		assertEquals(new Integer(22), avg);
		a = NIBRSAgeBuilder.buildAgeFromRawString("2224", TEST_SEGMENT);
		avg = a.getAverage();
		assertEquals(new Integer(23), avg);
		a = NIBRSAgeBuilder.buildAgeFromRawString("2223", TEST_SEGMENT);
		avg = a.getAverage();
		assertEquals(new Integer(22), avg);
	}
	
	@Test
	public void testValidAge() {
		NIBRSAge a = NIBRSAgeBuilder.buildAgeFromRawString("24", TEST_SEGMENT);
		assertNull(a.getError());
		assertFalse(a.isInvalid());
		assertFalse(a.isAgeRange());
	}
	
	@Test
	public void testValidAgeRange() {
		NIBRSAge a = NIBRSAgeBuilder.buildAgeFromRawString("2428", TEST_SEGMENT);
		assertNull(a.getError());
		assertTrue(a.isAgeRange());
		assertFalse(a.isInvalid());
	}

	@Test
	public void testInvalidAge() {
		NIBRSAge a = NIBRSAgeBuilder.buildAgeFromRawString("XY", TEST_SEGMENT);
		NIBRSError e = a.getError();
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._404, e.getNIBRSErrorCode());
		assertTrue(a.isInvalid());
	}

	@Test
	public void testInvalidAgeRange() {
		NIBRSAge a = NIBRSAgeBuilder.buildAgeFromRawString("24BB", TEST_SEGMENT);
		NIBRSError e = a.getError();
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._409, e.getNIBRSErrorCode());
		assertTrue(a.isInvalid());
	}

	@Test
	public void nullIsAgeRangeTest() {
		NIBRSAge nibrsAge = new NIBRSAge();
		assertFalse(nibrsAge.isAgeRange());
	}
	
	@Test
	public void testSingleAgeYounger() {
		NIBRSAge age1 = NIBRSAgeBuilder.buildAgeFromRawString("20  ", TEST_SEGMENT);
		NIBRSAge age2 = NIBRSAgeBuilder.buildAgeFromRawString("20  ", TEST_SEGMENT);
		assertFalse(age1.isYoungerThan(age2, false));
		assertFalse(age1.isYoungerThan(age2, true));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("21  ", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("22  ", TEST_SEGMENT);
		assertFalse(age1.isYoungerThan(age2, false));
		assertFalse(age1.isYoungerThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("NN  ", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("BB  ", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
	}
	
	@Test
	public void testAgeRangeYounger() {
		NIBRSAge age1 = NIBRSAgeBuilder.buildAgeFromRawString("2025", TEST_SEGMENT);
		NIBRSAge age2 = NIBRSAgeBuilder.buildAgeFromRawString("3035", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, false));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("2335", TEST_SEGMENT);
		assertFalse(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("1825", TEST_SEGMENT);
		assertFalse(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("20  ", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("BB  ", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("20  ", TEST_SEGMENT);
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("2025", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, true));
		assertFalse(age1.isYoungerThan(age2, false));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("2030", TEST_SEGMENT);
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("1025", TEST_SEGMENT);
		assertTrue(age1.isYoungerThan(age2, true));
	}

	@Test
	public void testSingleAgeOlder() {
		NIBRSAge age1 = NIBRSAgeBuilder.buildAgeFromRawString("20  ", TEST_SEGMENT);
		NIBRSAge age2 = NIBRSAgeBuilder.buildAgeFromRawString("20  ", TEST_SEGMENT);
		assertFalse(age1.isOlderThan(age2, false));
		assertFalse(age1.isOlderThan(age2, true));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("19  ", TEST_SEGMENT);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("18  ", TEST_SEGMENT);
		assertFalse(age1.isOlderThan(age2, false));
		assertFalse(age1.isOlderThan(age2, true));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("NN  ", TEST_SEGMENT);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("BB  ", TEST_SEGMENT);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
	}
	
	@Test
	public void testAgeRangeOlder() {
		NIBRSAge age1 = NIBRSAgeBuilder.buildAgeFromRawString("2025", TEST_SEGMENT);
		NIBRSAge age2 = NIBRSAgeBuilder.buildAgeFromRawString("1015", TEST_SEGMENT);
		assertTrue(age1.isOlderThan(age2, false));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("1823", TEST_SEGMENT);
		assertFalse(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age1 = NIBRSAgeBuilder.buildAgeFromRawString("25  ", TEST_SEGMENT);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("BB  ", TEST_SEGMENT);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age2 = NIBRSAgeBuilder.buildAgeFromRawString("2025", TEST_SEGMENT);
		assertTrue(age1.isOlderThan(age2, true));
		assertFalse(age1.isOlderThan(age2, false));
	}
	
	@Test
	public void testUnknown() {
		NIBRSAge age = NIBRSAgeBuilder.buildAgeFromRawString("00  ", TEST_SEGMENT);
		assertTrue(age.isUnknown());
		assertNull(age.getAgeMin());
	}
	
	@Test
	public void testInvalidUnknown() {
		NIBRSAge age = NIBRSAgeBuilder.buildAgeFromRawString("0020", TEST_SEGMENT);
		assertTrue(age.isInvalid());
		NIBRSError e = age.getError();
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._422, e.getNIBRSErrorCode());
	}
	
	@Test
	public void testNewbornAge() {
		NIBRSAge age = NIBRSAgeBuilder.buildAgeFromRawString("NB  ", TEST_SEGMENT);
		assertTrue(age.isNonNumeric());
		assertEquals(new Integer(0), age.getAgeMin());
		assertTrue(age.isNewborn());
	}

}
