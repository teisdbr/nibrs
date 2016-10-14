package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.validation.rules.Rule;


public class OffenderSegmentRulesFactory {

	private List<Rule<OffenderSegment>> rulesList;
	
	public static OffenderSegmentRulesFactory instance(){
		
		return new OffenderSegmentRulesFactory();
	}
	
	private OffenderSegmentRulesFactory() {
		
		rulesList = new ArrayList<Rule<OffenderSegment>>();
	}
	
	/**
	 * Get the list of rules for the Offender segment.
	 * 
	 * @return the list of rules
	 */
	public List<Rule<OffenderSegment>> getRulesList() {
		return rulesList;
	}	
	
}
