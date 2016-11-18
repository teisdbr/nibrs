package org.search.nibrs.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class NIBRSAgeTest {
	
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

}
