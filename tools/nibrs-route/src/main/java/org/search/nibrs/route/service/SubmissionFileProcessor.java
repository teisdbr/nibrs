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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.errorexport.ErrorExporter;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.importer.ReportListener;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.validation.SubmissionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SubmissionFileProcessor {
	private final Log log = LogFactory.getLog(SubmissionFileProcessor.class);

	@Autowired
	IncidentBuilder incidentBuilder;
	@Autowired
	SubmissionValidator submissionValidator;
	@Autowired
	ErrorExporter errorExporter;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		
	public ValidationResults validate(@Body File file) throws IOException{
		ValidationResults validationResults = new ValidationResults();
		
		Reader inputReader = new BufferedReader(new FileReader(file));
		String readerLocationName = file.getAbsolutePath();

		incidentBuilder.addIncidentListener(new ReportListener() {
			@Override
			public void newReport(AbstractReport report, List<NIBRSError> el) {
				validationResults.getErrorList().addAll(el);
				validationResults.getErrorList().addAll(submissionValidator.validateReport(report));
				addReportWithoutErrors(validationResults, report);
			}

		});

		incidentBuilder.buildIncidents(inputReader, readerLocationName);

		inputReader.close();
		return validationResults; 

	}
	
	private void addReportWithoutErrors(ValidationResults validationResults, AbstractReport report) {
		if (validationResults.getErrorList().isEmpty()){
			validationResults.getReportsWithoutErrors().add(report);
		}
		else{
			NIBRSError nibrsError = validationResults.getErrorList().get(validationResults.getErrorList().size()-1);
			if (report.getIdentifier() != null && 
					!report.getIdentifier().equals(nibrsError.getReportUniqueIdentifier())) {
				validationResults.getReportsWithoutErrors().add(report);
			}
		}
	}
	
	public File createErrorReport(@Body ValidationResults validationResults,
			@Header("CamelFileNameOnly") String fileNameOnly, @Header("CamelFileParent") String parentPath)
			throws IOException {
		
		String resultPath = parentPath.replace("/input", "/result"); 
		File resultDirectory = new File(resultPath);
		resultDirectory.mkdir();
		
		String fileName = resultPath + File.separator + fileNameOnly.replace(".", "-" + formatter.format(LocalDateTime.now()) + ".");
		File file = new File(fileName);
		FileWriter filewriter = new FileWriter(file);
		Writer outputWriter = new BufferedWriter(filewriter);
		errorExporter.createErrorReport(validationResults.getErrorList(), outputWriter);
		outputWriter.close();
		log.info("The error report is writen to " + fileName); 
		
		return file; 
	}
	
}