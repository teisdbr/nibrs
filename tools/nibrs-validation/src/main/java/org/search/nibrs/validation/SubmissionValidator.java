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
package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.validation.groupa.GroupAIncidentReportValidator;
import org.search.nibrs.validation.groupb.GroupBArrestReportValidator;
import org.search.nibrs.validation.zeroreport.ZeroReportValidator;

/**
 * Class that applies edits to validate the segments contained within a NIBRS Submission
 */
public class SubmissionValidator {

	private static final Logger LOG = LogManager.getLogger(SubmissionValidator.class);

	/**
	 * Apply edits to validate all Reports within the specified submission.
	 * 
	 * @param nibrsSubmission submission containing all the reports to be validated
	 * @return A List of all errors encountered in validating the submission
	 */
	public List<NIBRSError> validate(NIBRSSubmission nibrsSubmission) {

		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		List<AbstractReport> reportList = nibrsSubmission.getReports();

		for (AbstractReport report : reportList) {
			List<NIBRSError> singleReportErrorsList = validateReport(report);
			errorList.addAll(singleReportErrorsList);
		}

		return errorList;
		
	}

	/**
	 * Validate an individual Report within the specified submission
	 * @param report the report to be validated
	 * @return A List of all errors encountered in validating the report
	 */
	public List<NIBRSError> validateReport(AbstractReport report) {
		
		LOG.info("Validating report: " + report.getGloballyUniqueReportIdentifier());
		
		List<NIBRSError> nibrsErrorList = new ArrayList<>();

		if (report instanceof ZeroReport) {
			ZeroReport zeroReport = (ZeroReport) report;
			ZeroReportValidator zeroReportValidator = new ZeroReportValidator();
			nibrsErrorList = zeroReportValidator.validate(zeroReport);
		} else if (report instanceof GroupAIncidentReport) {
			GroupAIncidentReport groupAIncidentReport = (GroupAIncidentReport) report;
			GroupAIncidentReportValidator groupAValidator = new GroupAIncidentReportValidator();
			nibrsErrorList = groupAValidator.validate(groupAIncidentReport);
		} else if (report instanceof GroupBArrestReport) {
			GroupBArrestReport groupBIncidentReport = (GroupBArrestReport) report;
			GroupBArrestReportValidator groupBValidator = new GroupBArrestReportValidator();
			nibrsErrorList = groupBValidator.validate(groupBIncidentReport);
		}

		LOG.info("Found " + nibrsErrorList.size() + " errors");

		return nibrsErrorList;
		
	}

}
