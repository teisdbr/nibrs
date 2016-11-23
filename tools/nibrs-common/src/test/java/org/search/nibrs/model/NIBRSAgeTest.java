package org.search.nibrs.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class NIBRSAgeTest {
	
	@Test
	public void testInvalidString() {
		NIBRSAge a = new NIBRSAge();
		a.setAgeString("AA", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		NIBRSError e = a.getError();
		assertNotNull(e);
		assertFalse(a.isNonNumeric());
		assertFalse(a.isUnknown());
		assertNull(a.getAgeMin());
		assertNull(a.getAgeMax());
		assertTrue(a.hasInvalidValue());
		assertFalse(a.hasInvalidLength());
		a.setAgeString("123", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		e = a.getError();
		assertNotNull(e);
		assertFalse(a.hasInvalidValue());
		assertTrue(a.hasInvalidLength());
	}
	
	@Test
	public void testAverage() {
		NIBRSAge a = new NIBRSAge();
		a.setAgeString("NB", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		Integer avg = a.getAverage();
		assertNull(avg);
		a.setAgeString("22  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		avg = a.getAverage();
		assertEquals(new Integer(22), avg);
		a.setAgeString("2224", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		avg = a.getAverage();
		assertEquals(new Integer(23), avg);
		a.setAgeString("2223", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		avg = a.getAverage();
		assertEquals(new Integer(22), avg);
	}
	
	@Test
	public void testValidAge() {
		NIBRSAge a = new NIBRSAge();
		a.setAgeString("24", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertNull(a.getError());
	}
	
	@Test
	public void testValidAgeRange() {
		NIBRSAge a = new NIBRSAge();
		a.setAgeString("2428", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertNull(a.getError());
	}

	@Test
	public void testInvalidAge() {
		NIBRSAge a = new NIBRSAge();
		a.setAgeString("XY", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		NIBRSError e = a.getError();
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._404, e.getNIBRSErrorCode());
	}

	@Test
	public void testInvalidAgeRange() {
		NIBRSAge a = new NIBRSAge();
		a.setAgeString("24BB", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		NIBRSError e = a.getError();
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._409, e.getNIBRSErrorCode());
	}

	@Test
	public void nullIsAgeRangeTest() {
		NIBRSAge nibrsAge = new NIBRSAge();
		assertFalse(nibrsAge.isAgeRange());
	}
	
	@Test
	public void testSingleAgeYounger() {
		NIBRSAge age1 = new NIBRSAge();
		age1.setAgeString("20  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		NIBRSAge age2 = new NIBRSAge();
		age2.setAgeString("20  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(age1.isYoungerThan(age2, false));
		assertFalse(age1.isYoungerThan(age2, true));
		age2.setAgeString("21  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1.setAgeString("22  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(age1.isYoungerThan(age2, false));
		assertFalse(age1.isYoungerThan(age2, true));
		age1.setAgeString("NN  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age2.setAgeString("BB  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
	}
	
	@Test
	public void testAgeRangeYounger() {
		NIBRSAge age1 = new NIBRSAge();
		age1.setAgeString("2025", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		NIBRSAge age2 = new NIBRSAge();
		age2.setAgeString("3035", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, false));
		age2.setAgeString("2335", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1.setAgeString("1825", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1.setAgeString("20  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1.setAgeString("BB  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, false));
		assertTrue(age1.isYoungerThan(age2, true));
		age1.setAgeString("20  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		age2.setAgeString("2025", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, true));
		assertFalse(age1.isYoungerThan(age2, false));
		age1.setAgeString("2030", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		age2.setAgeString("1025", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isYoungerThan(age2, true));
	}

	@Test
	public void testSingleAgeOlder() {
		NIBRSAge age1 = new NIBRSAge();
		age1.setAgeString("20  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		NIBRSAge age2 = new NIBRSAge();
		age2.setAgeString("20  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(age1.isOlderThan(age2, false));
		assertFalse(age1.isOlderThan(age2, true));
		age2.setAgeString("19  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age1.setAgeString("18  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(age1.isOlderThan(age2, false));
		assertFalse(age1.isOlderThan(age2, true));
		age2.setAgeString("NN  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age1.setAgeString("BB  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
	}
	
	@Test
	public void testAgeRangeOlder() {
		NIBRSAge age1 = new NIBRSAge();
		age1.setAgeString("2025", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		NIBRSAge age2 = new NIBRSAge();
		age2.setAgeString("1015", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isOlderThan(age2, false));
		age2.setAgeString("1823", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertFalse(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age1.setAgeString("25  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age2.setAgeString("BB  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isOlderThan(age2, false));
		assertTrue(age1.isOlderThan(age2, true));
		age1.setAgeString("25  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		age2.setAgeString("2025", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age1.isOlderThan(age2, true));
		assertFalse(age1.isOlderThan(age2, false));
	}
	
	@Test
	public void testUnknown() {
		NIBRSAge age = new NIBRSAge();
		age.setAgeString("00  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age.isUnknown());
		assertNull(age.getAgeMin());
	}
	
	@Test
	public void testBabyAge() {
		NIBRSAge age = new NIBRSAge();
		age.setAgeString("NB  ", VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER);
		assertTrue(age.isNonNumeric());
		assertEquals(new Integer(0), age.getAgeMin());
	}

}
