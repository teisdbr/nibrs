package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.validation.rules.Rule;

/**
 * Class that validates a Group A Incident Report and all of its contained child segments.
 * 
 */
public class GroupAIncidentReportValidator {
	
	private List<Rule<GroupAIncidentReport>> incidentReportRules = new ArrayList<>();
	private List<Rule<OffenseSegment>> offenseSegmentRules = new ArrayList<>();
	private List<Rule<PropertySegment>> propertySegmentRules = new ArrayList<>();
	
	public GroupAIncidentReportValidator() {
		incidentReportRules = new GroupAIncidentReportRulesFactory().getRulesList();
		offenseSegmentRules = new OffenseSegmentRulesFactory().getRulesList();
		propertySegmentRules = new PropertySegmentRulesFactory().getRulesList();
	}

	public List<NIBRSError> validate(GroupAIncidentReport groupAIncidentReport) {
		
		List<NIBRSError> ret = new ArrayList<NIBRSError>();
		
		for (Rule<GroupAIncidentReport> r : incidentReportRules) {
			NIBRSError e = r.apply(groupAIncidentReport);
			if (e != null) {
				ret.add(e);
			}
		}
		
		for (Rule<OffenseSegment> r : offenseSegmentRules) {
			for (OffenseSegment os : groupAIncidentReport.getOffenses()) {
				NIBRSError e = r.apply(os);
				if (e != null) {
					ret.add(e);
				}
			}
		}
		
		for (Rule<PropertySegment> r : propertySegmentRules) {
			for (PropertySegment ps : groupAIncidentReport.getProperties()) {
				NIBRSError e = r.apply(ps);
				if (e != null) {
					ret.add(e);
				}
			}
		}
		
		return ret;
		
	}
	
}
