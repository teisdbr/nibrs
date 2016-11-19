package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.validation.rules.Rule;

/**
 * Class that validates a Group A Incident Report and all of its contained child segments.
 * 
 */
public class GroupAIncidentReportValidator {
	
	private List<Rule<GroupAIncidentReport>> incidentReportRules = new ArrayList<>();
	private List<Rule<OffenseSegment>> offenseSegmentRules = new ArrayList<>();
	private List<Rule<PropertySegment>> propertySegmentRules = new ArrayList<>();
	private List<Rule<VictimSegment>> victimSegmentRules = new ArrayList<>();
	private List<Rule<OffenderSegment>> offenderSegmentRules = new ArrayList<>();
	private List<Rule<ArresteeSegment>> arresteeSegmentRules = new ArrayList<>();
	
	
	public GroupAIncidentReportValidator() {
		incidentReportRules = new GroupAIncidentReportRulesFactory().getRulesList();
		offenseSegmentRules = new OffenseSegmentRulesFactory().getRulesList();
		propertySegmentRules = new PropertySegmentRulesFactory().getRulesList();
		victimSegmentRules = VictimSegmentRulesFactory.instance().getRulesList();
		offenderSegmentRules = OffenderSegmentRulesFactory.instance().getRulesList();
		arresteeSegmentRules = ArresteeSegmentRulesFactory.instance().getRulesList();
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
			for (OffenseSegment s : groupAIncidentReport.getOffenses()) {
				NIBRSError e = r.apply(s);
				if (e != null) {
					ret.add(e);
				}
			}
		}
		
		for (Rule<PropertySegment> r : propertySegmentRules) {
			for (PropertySegment s : groupAIncidentReport.getProperties()) {
				NIBRSError e = r.apply(s);
				if (e != null) {
					ret.add(e);
				}
			}
		}

		for (Rule<VictimSegment> r : victimSegmentRules) {
			for (VictimSegment s : groupAIncidentReport.getVictims()) {
				NIBRSError nibrsError = r.apply(s);
				if (nibrsError != null) {
					ret.add(nibrsError);
				}
			}
		}

		for (Rule<OffenderSegment> r : offenderSegmentRules) {
			for (OffenderSegment s : groupAIncidentReport.getOffenders()) {
				NIBRSError nibrsError = r.apply(s);
				if (nibrsError != null) {
					ret.add(nibrsError);
				}
			}
		}

		for (Rule<ArresteeSegment> r : arresteeSegmentRules) {
			for (ArresteeSegment s : groupAIncidentReport.getArrestees()) {
				NIBRSError nibrsError = r.apply(s);
				if (nibrsError != null) {
					ret.add(nibrsError);
				}
			}
		}

		return ret;
		
	}
	
}
