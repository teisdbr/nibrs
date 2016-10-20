package org.search.nibrs.validation.groupb;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.model.GroupBIncidentReport;
import org.search.nibrs.validation.rules.Rule;

public class GroupBArrestReportSegmentFactory {

	private List<Rule<GroupBIncidentReport>> rulesList;

	public GroupBArrestReportSegmentFactory() {
	
		rulesList = new ArrayList<Rule<GroupBIncidentReport>>();
	}
	
	/**
	 * Get the list of rules for the GroupB Arrest Report segment factory.
	 * 
	 * @return the list of rules
	 */
	public List<Rule<GroupBIncidentReport>> getRulesList() {
		return rulesList;
	}	
}
