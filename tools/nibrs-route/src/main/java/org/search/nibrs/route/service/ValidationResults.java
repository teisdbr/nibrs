/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
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
package org.search.nibrs.route.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;

public class ValidationResults{

	private final List<NIBRSError> errorList;
	private List<AbstractReport> incidentReports;
	
	public ValidationResults() {
		super();
		errorList = new ArrayList<>();
		setIncidentReports(new ArrayList<>());
	}

	public List<NIBRSError> getErrorList() {
		return errorList;
	}

	public List<AbstractReport> getIncidentReports() {
		return incidentReports;
	}

	public void setIncidentReports(List<AbstractReport> incidentReports) {
		this.incidentReports = incidentReports;
	}

	public List<AbstractReport> getIncidentReportsWithoutErrors() {
		List<String> erroredIncidentNumbers = errorList.stream()
				.map( item -> item.getReportUniqueIdentifier())
				.distinct()
				.collect( Collectors.toList()); 
		List<AbstractReport> reportsWithoutErrors = 
				incidentReports.stream()
				.filter(item -> !erroredIncidentNumbers.contains(item.getIdentifier()))
				.collect( Collectors.toList());
		return reportsWithoutErrors;
	}

	@Override
	public String toString() {
		return "ValidationResults [errorList=" + errorList + ", incidentReports=" + incidentReports
				+ ", incidentReportsWithoutErrors=" + getIncidentReportsWithoutErrors() + "]";
	}

}