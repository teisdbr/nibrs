package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.validation.rules.Rule;

public class ArresteeSegmentRulesFactory {

	private List<Rule<ArresteeSegment>> rulesList;

	private ArresteeSegmentRulesFactory() {
	
		rulesList = new ArrayList<Rule<ArresteeSegment>>();
	}
		
	public static ArresteeSegmentRulesFactory instance(){
		
		return new ArresteeSegmentRulesFactory();
	}	
	
	/**
	 * Get the list of rules for the Arrestee segment.
	 * 
	 * @return the list of rules
	 */
	public List<Rule<ArresteeSegment>> getRulesList() {
		return rulesList;
	}	
}
