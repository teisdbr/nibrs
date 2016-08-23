package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.validation.rules.Rule;

/**
 * Class that validates a Group A Incident Report and all of its contained child segments.
 * 
 */
public class GroupAIncidentReportValidator {
	
	private List<Rule<GroupAIncidentReport>> incidentReportRules = new ArrayList<>();
	
	public GroupAIncidentReportValidator() {
		
		incidentReportRules = new GroupAIncidentReportRulesFactory().getRulesList();
		
	}

	public List<NIBRSError> validate(GroupAIncidentReport groupAIncidentReport) {
		
		List<NIBRSError> ret = new ArrayList<NIBRSError>();
		
		for (Rule<GroupAIncidentReport> r : incidentReportRules) {
			NIBRSError e = r.apply(groupAIncidentReport);
			if (e != null) {
				ret.add(e);
			}
		}
		
		return ret;
		
	}
	
}
