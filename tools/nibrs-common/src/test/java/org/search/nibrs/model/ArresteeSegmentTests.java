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
