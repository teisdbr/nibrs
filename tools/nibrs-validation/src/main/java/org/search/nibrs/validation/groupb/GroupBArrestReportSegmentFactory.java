package org.search.nibrs.validation.groupb;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.validation.rules.Rule;

public class GroupBArrestReportSegmentFactory {

	private List<Rule<PropertySegment>> rulesList;

	public GroupBArrestReportSegmentFactory() {
	
		rulesList = new ArrayList<Rule<PropertySegment>>();
	}
	
	/**
	 * Get the list of rules for the GroupB Arrest Report segment factory.
	 * 
	 * @return the list of rules
	 */
	public List<Rule<PropertySegment>> getRulesList() {
		return rulesList;
	}	
}
