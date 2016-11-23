package org.search.nibrs.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.model.codes.ArresteeWasArmedWithCode;

public class CodeEnumTests {
	
	@Test
	public void testFirearmCode() {
		assertTrue(ArresteeWasArmedWithCode._11.isFirearm());
		assertFalse(ArresteeWasArmedWithCode._01.isFirearm());
	}

}
