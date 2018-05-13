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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.exception.TikaException;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.errorexport.ErrorExporter;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.importer.ReportListener;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.util.NibrsFileUtils;
import org.search.nibrs.validation.SubmissionValidator;
import org.search.nibrs.xmlfile.importer.XmlIncidentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
@Scope("prototype")
public class SubmissionFileProcessor {
	private final Log log = LogFactory.getLog(SubmissionFileProcessor.class);

	@Autowired
	IncidentBuilder incidentBuilder;
	@Autowired
	XmlIncidentBuilder xmlIncidentBuilder;
	@Autowired
	SubmissionValidator submissionValidator;
	@Autowired
	ErrorExporter errorExporter;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		
	public ValidationResults validate(@Body File file) throws IOException, ParserConfigurationException, TikaException, SAXException{
		ValidationResults validationResults = new ValidationResults();
		
		ReportListener validatorListener = new ReportListener() {
			@Override
			public void newReport(AbstractReport report, List<NIBRSError> el) {
				validationResults.getErrorList().addAll(el);
				validationResults.getErrorList().addAll(submissionValidator.validateReport(report));
				addReportWithoutErrors(validationResults, report);
			}
		};

		validateFile(validatorListener, file);

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
		
		String baseName = FilenameUtils.getBaseName(fileNameOnly);
		
		String fileName = resultPath + File.separator + baseName + "-" + formatter.format(LocalDateTime.now()) + ".txt";
		File file = new File(fileName);
		FileWriter filewriter = new FileWriter(file);
		Writer outputWriter = new BufferedWriter(filewriter);
		errorExporter.createErrorReport(validationResults.getErrorList(), outputWriter);
		outputWriter.close();
		log.info("The error report is writen to " + fileName); 
		
		return file; 
	}
	
	//TODO create another common project to contain the validateFile() and validateInputStream() methods. 
	private static void validateFile(ReportListener validatorlistener, 
			File file) throws ParserConfigurationException, IOException, TikaException, SAXException {
		String fileType = NibrsFileUtils.getMediaType(file);
		FileInputStream inputStream = new FileInputStream(file);

		switch (fileType){
		case "text/xml":
		case "application/xml":
			XmlIncidentBuilder xmlIncidentBuilder = new XmlIncidentBuilder();
			xmlIncidentBuilder.addIncidentListener(validatorlistener);
			xmlIncidentBuilder.buildIncidents(inputStream, file.getAbsolutePath());
			break; 
		case "text/plain": 
		case "application/octet-stream": 
			Reader inputReader = new BufferedReader(new InputStreamReader(inputStream));
			IncidentBuilder incidentBuilder = new IncidentBuilder();
			incidentBuilder.addIncidentListener(validatorlistener);
			incidentBuilder.buildIncidents(inputReader, file.getAbsolutePath());
			inputReader.close();
			break;
		default:
			System.err.println("The file type " + fileType + " is not supported"); 
		}
		
	}
	
}