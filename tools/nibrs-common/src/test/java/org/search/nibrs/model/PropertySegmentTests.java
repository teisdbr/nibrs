package org.search.nibrs.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.model.codes.PropertyDescriptionCode;

public class PropertySegmentTests {

	@Test
	public void testContainsProperty() {
		PropertySegment ps = new PropertySegment();
		assertFalse(ps.containsPropertyDescription(PropertyDescriptionCode._01.code));
		ps.setPropertyDescription(0, PropertyDescriptionCode._01.code);
		ps.setPropertyDescription(1, PropertyDescriptionCode._02.code);
		assertTrue(ps.containsPropertyDescription(PropertyDescriptionCode._01.code));
		assertFalse(ps.containsPropertyDescription(PropertyDescriptionCode._03.code));
	}
	
}
