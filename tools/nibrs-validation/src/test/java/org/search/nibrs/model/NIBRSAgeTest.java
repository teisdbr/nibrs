package org.search.nibrs.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NIBRSAgeTest {

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
	}

}
