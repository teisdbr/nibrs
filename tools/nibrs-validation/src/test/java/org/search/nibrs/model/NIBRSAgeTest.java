package org.search.nibrs.model;

import junit.framework.Assert;

import org.junit.Test;

public class NIBRSAgeTest {
		
	@Test
	public void nullIsAgeRangeTest(){
		
		NIBRSAge nibrsAge = new NIBRSAge();
		
		boolean isAgeRange = nibrsAge.isAgeRange();
		
		Assert.assertFalse(isAgeRange);
	}	

}
