/*******************************************************************************
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.validation.groupa.GroupAIncidentReportValidator;
import org.search.nibrs.validation.groupb.GroupBArrestReportValidator;
import org.search.nibrs.validation.zeroreport.ZeroReportValidator;

public class SubmissionValidator {

	private static final Logger LOG = Logger.getLogger(SubmissionValidator.class.getName());

	private ValidationListener validationListener;

	public SubmissionValidator(ValidationListener validationListener) {
		this.validationListener = validationListener;
	}

	/**
	 * Returns all validation errors. While processing, notifies listeners of errors found for each report that's validated.
	 * 
	 * @param nibrsSubmission
	 *            Should contain all the reports to be validated
	 * 
	 * @return A List of all validated errors, for all the reports
	 */
	public List<NIBRSError> validate(NIBRSSubmission nibrsSubmission) {

		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		List<AbstractReport> reportList = nibrsSubmission.getReports();

		for (AbstractReport report : reportList) {
			List<NIBRSError> singleReportErrorsList = validateReport(report);
			errorList.addAll(singleReportErrorsList);
			validationListener.validationAvailable(singleReportErrorsList);
		}

		return errorList;
		
	}

	private List<NIBRSError> validateReport(AbstractReport report) {

		List<NIBRSError> nibrsErrorList = new ArrayList<>();

		if (report instanceof ZeroReport) {
			ZeroReport zeroReport = (ZeroReport) report;
			ZeroReportValidator zeroReportValidator = new ZeroReportValidator();
			nibrsErrorList = zeroReportValidator.validate(zeroReport);
		} else if (report instanceof GroupAIncidentReport) {
			GroupAIncidentReport groupAIncidentReport = (GroupAIncidentReport) report;
			GroupAIncidentReportValidator groupAValidator = new GroupAIncidentReportValidator();
			groupAValidator.validate(groupAIncidentReport);
		} else if (report instanceof GroupBArrestReport) {
			GroupBArrestReport groupBIncidentReport = (GroupBArrestReport) report;
			GroupBArrestReportValidator groupBValidator = new GroupBArrestReportValidator();
			groupBValidator.validate(groupBIncidentReport);
		}

		return nibrsErrorList;
		
	}

}
