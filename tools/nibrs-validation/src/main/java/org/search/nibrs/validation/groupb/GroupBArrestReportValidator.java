package org.search.nibrs.validation.groupb;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.validation.ArresteeSegmentRulesFactory;
import org.search.nibrs.validation.rules.Rule;

/**
 * Class that validates a Group B Arrest Report and all of its contained child segments.
 * 
 */
public class GroupBArrestReportValidator {
	
	private List<Rule<GroupBArrestReport>> incidentReportRules = new ArrayList<>();
	private List<Rule<ArresteeSegment>> groupBArresteeSegmentRules = new ArrayList<>();
	
	public GroupBArrestReportValidator() {
		incidentReportRules = new GroupBArrestReportRulesFactory().getRulesList();
		groupBArresteeSegmentRules = ArresteeSegmentRulesFactory.instance(ArresteeSegmentRulesFactory.GROUP_B_ARRESTEE_MODE).getRulesList();
	}

	public List<NIBRSError> validate(GroupBArrestReport groupBIncidentReport) {
		
		List<NIBRSError> errorsList = new ArrayList<NIBRSError>();
		
		for (Rule<GroupBArrestReport> r : incidentReportRules) {
			NIBRSError e = r.apply(groupBIncidentReport);
			if (e != null) {
				errorsList.add(e);
			}
		}
		
		for (Rule<ArresteeSegment> r : groupBArresteeSegmentRules) {
			for (ArresteeSegment s : groupBIncidentReport.getArrestees()) {
				NIBRSError nibrsError = r.apply(s);
				if (nibrsError != null) {
					errorsList.add(nibrsError);
				}
			}
		}
		
		return errorsList;
		
	}
	
}
