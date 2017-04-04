/*
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.ArresteeSegmentRulesFactory;
import org.search.nibrs.validation.rules.Rule;

/**
 * Class that validates a Group A Incident Report and all of its contained child segments.
 * 
 */
public class GroupAIncidentReportValidator {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(GroupAIncidentReportValidator.class);
	
	private List<Rule<GroupAIncidentReport>> incidentReportRules = new ArrayList<>();
	private List<Rule<OffenseSegment>> offenseSegmentRules = new ArrayList<>();
	private List<Rule<PropertySegment>> propertySegmentRules = new ArrayList<>();
	private List<Rule<VictimSegment>> victimSegmentRules = new ArrayList<>();
	private List<Rule<OffenderSegment>> offenderSegmentRules = new ArrayList<>();
	private List<Rule<ArresteeSegment>> groupAArresteeSegmentRules = new ArrayList<>();
	
	
	public GroupAIncidentReportValidator() {
		incidentReportRules = new GroupAIncidentReportRulesFactory().getRulesList();
		offenseSegmentRules = new OffenseSegmentRulesFactory().getRulesList();
		propertySegmentRules = new PropertySegmentRulesFactory().getRulesList();
		victimSegmentRules = VictimSegmentRulesFactory.instance().getRulesList();
		offenderSegmentRules = OffenderSegmentRulesFactory.instance().getRulesList();
		groupAArresteeSegmentRules = ArresteeSegmentRulesFactory.instance(ArresteeSegmentRulesFactory.GROUP_A_ARRESTEE_MODE).getRulesList();
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
				processVictimSegmentError(ret, nibrsError);
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

		for (Rule<ArresteeSegment> r : groupAArresteeSegmentRules) {
			for (ArresteeSegment s : groupAIncidentReport.getArrestees()) {
				NIBRSError nibrsError = r.apply(s);
				processArresteeSegmentError(ret, nibrsError);
			}
		}
		
		return ret;
		
	}

	private void processArresteeSegmentError(List<NIBRSError> ret,
			NIBRSError nibrsError) {
		if (nibrsError != null) {
			if (!nibrsError.getRuleNumber().equals(NIBRSErrorCode._071.getCode())){
				ret.add(nibrsError);
			}
			else{
				long count =  
					ret.stream()
					   	.filter(item->item.getRuleNumber().equals(NIBRSErrorCode._071.getCode()))
					   	.count();
				if (count == 0){
					ret.add(nibrsError);
				}
			}
		}
	}

	private void processVictimSegmentError(List<NIBRSError> ret, NIBRSError nibrsError) {
		if (nibrsError != null) {
			if (!nibrsError.getRuleNumber().equals(NIBRSErrorCode._070.getCode())){
				ret.add(nibrsError);
			}
			else{
				long count =  
					ret.stream()
						.filter(item->item.getRuleNumber().equals(NIBRSErrorCode._070.getCode()))
					   	.filter(item->nibrsError.getValue().equals(item.getValue()))
					   	.count();
				if (count == 0){
					ret.add(nibrsError);
				}
			}
		}
	}
	
}
