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
package org.search.nibrs.validation.groupb;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.ArresteeSegmentRulesFactory;
import org.search.nibrs.validation.ValidatorProperties;
import org.search.nibrs.validation.rules.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that validates a Group B Arrest Report and all of its contained child segments.
 * 
 */
@Component
public class GroupBArrestReportValidator {
	
	private List<Rule<GroupBArrestReport>> incidentReportRules = new ArrayList<>();
	private List<Rule<ArresteeSegment>> groupBArresteeSegmentRules = new ArrayList<>();
	
	@Autowired
	public GroupBArrestReportValidator(ValidatorProperties validatorProperties) {
		incidentReportRules = new GroupBArrestReportRulesFactory().getRulesList();
		groupBArresteeSegmentRules = ArresteeSegmentRulesFactory
				.instance(ArresteeSegmentRulesFactory.GROUP_B_ARRESTEE_MODE, validatorProperties).getRulesList();
	}

	public List<NIBRSError> validate(GroupBArrestReport groupBIncidentReport) {
		
		List<NIBRSError> errorsList = new ArrayList<NIBRSError>();
		
		for (Rule<GroupBArrestReport> r : incidentReportRules) {
			NIBRSError e = r.apply(groupBIncidentReport);
			if (e != null) {
				errorsList.add(e);
			}
		}
		
		if (groupBIncidentReport.getArrestees().size() == 1){
			for (Rule<ArresteeSegment> r : groupBArresteeSegmentRules) {
				NIBRSError nibrsError = r.apply(groupBIncidentReport.getArrestees().get(0));
				if (nibrsError != null) {
					errorsList.add(nibrsError);
				}
			}
		}
		else if (groupBIncidentReport.getArrestees().size() > 1) {
			NIBRSError e = new NIBRSError();
			e.setReport(groupBIncidentReport);
			e.setContext(groupBIncidentReport.getSource());
			e.setReportUniqueIdentifier(groupBIncidentReport.getIdentifier());
			e.setValue(groupBIncidentReport.getArrestees().size());
			e.setCrossSegment(true);
			e.setWithinSegmentIdentifier(null); 
			e.setNIBRSErrorCode(NIBRSErrorCode._099);
			errorsList.add(e);
		}
		
		return errorsList;
		
	}
	
}
