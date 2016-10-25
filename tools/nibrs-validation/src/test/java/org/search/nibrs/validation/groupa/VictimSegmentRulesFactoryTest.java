package org.search.nibrs.validation.groupa;

import org.junit.Test;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.validation.rules.Rule;

public class VictimSegmentRulesFactoryTest {
	
	private VictimSegmentRulesFactory victimRulesFactory = VictimSegmentRulesFactory.instance();
	
	@Test
	public void testRule401ForSequenceNumber(){
		
		
		Rule<VictimSegment> rule401 = victimRulesFactory.getRule401ForSequenceNumber();
		
//		Group
		
	}

}
