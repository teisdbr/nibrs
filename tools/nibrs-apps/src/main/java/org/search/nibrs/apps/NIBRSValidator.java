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
package org.search.nibrs.apps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.tika.exception.TikaException;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.errorexport.ErrorExporter;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.importer.ReportListener;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.validate.common.NibrsValidationUtils;
import org.search.nibrs.validation.SubmissionValidator;
import org.xml.sax.SAXException;

/**
 * Executable class (via main) that accepts a submission file (via stdin, or
 * optionally a specified file) and validates the submission, writing the error
 * report to stdout (or optionally to a specified file).
 */
public class NIBRSValidator {

	public static void main(String[] args) throws ParseException, IOException, ParserConfigurationException, TikaException, SAXException {

		CommandLineParser parser = new DefaultParser();
		Options options = buildOptions();
		CommandLine cl = parser.parse(options, args);

		if (cl.hasOption("h")) {
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("SubmissionValidator", options);
		} else {
			Reader inputReader = null;
			String readerLocationName = "console";
			Writer outputWriter = null;

			final List<NIBRSError> errorList = new ArrayList<>();
//			List<AbstractReport> incidentReports = new ArrayList<>();
			SubmissionValidator submissionValidator = new SubmissionValidator();
			ReportListener validatorlistener = new ReportListener() {
				@Override
				public void newReport(AbstractReport report, List<NIBRSError> el) {
					errorList.addAll(el);
					errorList.addAll(submissionValidator.validateReport(report));
				}
			};

			if (cl.hasOption("f")) {
				String fileName = cl.getOptionValue("f");
				File file = new File(fileName);

				if (file.exists()) {
					if (file.isFile()){
						NibrsValidationUtils.validateFile(validatorlistener, file);
					}
					else if (file.isDirectory()){
						for (final File fileEntry : file.listFiles()) {
					        if (fileEntry.isFile()) {
					        	NibrsValidationUtils.validateFile(validatorlistener, fileEntry);
					        } 
					    }
					}
				} else {
					System.err.println("File " + fileName + " does not exist.");
					System.exit(1);
				}
			} else {
				inputReader = new BufferedReader(new InputStreamReader(System.in));
				IncidentBuilder incidentBuilder = new IncidentBuilder();
				incidentBuilder.addIncidentListener(validatorlistener);
				incidentBuilder.buildIncidents(inputReader, readerLocationName);
				inputReader.close();
			}

			if (cl.hasOption("o")) {
				String fileName = cl.getOptionValue("o");
				File file = new File(fileName);
				outputWriter = new BufferedWriter(new FileWriter(file));
			} else {
				outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
			}

			ErrorExporter errorExporter = ErrorExporter.getInstance();
			
//			Set<String> officerOtherJurisdictionORIs = new HashSet<>();
//			List<GroupAIncidentReport> groupAIncidentReports = 	incidentReports.stream().filter(i -> (i instanceof GroupAIncidentReport))
//					.map(i->(GroupAIncidentReport) i).filter(i->i.getVictims().stream().anyMatch(victim->org.apache.commons.lang3.StringUtils.isNotBlank(victim.getOfficerOtherJurisdictionORI()))).collect(Collectors.toList());
//			incidentReports.stream().filter(i -> (i instanceof GroupAIncidentReport))
//				.map(i->(GroupAIncidentReport) i)
//				.filter(i -> i.getVictimCount() > 0)
//				.map(i->i.getVictims())
//				.forEach(i-> i.stream().map(victim->victim.getOfficerOtherJurisdictionORI())
//						.filter(org.apache.commons.lang3.StringUtils::isNotBlank)
//						.forEach(officerOtherJurisdictionORIs::add));
			
//			List<String> erroredIncidentNumbers = errorList.stream()
//					.map( item -> item.getReportUniqueIdentifier())
//					.distinct()
//					.collect( Collectors.toList()); 
//			System.out.println("Count of the incidents with errors: " + erroredIncidentNumbers.size());
//			List<AbstractReport> reportsWithoutErrors = 
//					incidentReports.stream()
//					.filter(item -> !erroredIncidentNumbers.contains(item.getIdentifier()))
//					.collect( Collectors.toList());
			errorExporter.createErrorReport(errorList, outputWriter);
			
//			System.out.println("The officerOtherJurisdictionORIs:");
//			System.out.println("Count of the incident: " + reportsWithoutErrors.size());
//			officerOtherJurisdictionORIs.forEach(System.out::println);

//			List<GroupAIncidentReport> groupAIncidentReports = 	incidentReports.stream()
//					.filter(i -> (i instanceof GroupAIncidentReport))
//					.map(i->(GroupAIncidentReport) i)
//					.collect(Collectors.toList());
//			List<LocalDate> incidentDates = groupAIncidentReports.stream()
//					.map(GroupAIncidentReport::getIncidentDate)
//					.map(i->Optional.ofNullable(i.getValue()).orElse(null))
//					.filter(Objects::nonNull)
//					.distinct()
//					.collect(Collectors.toList());
//			System.out.println("Incident Dates:");
//			incidentDates.forEach(System.out::println);
//			
//			List<LocalDate> exceptionalClearanceDates = groupAIncidentReports.stream()
//					.map(GroupAIncidentReport::getExceptionalClearanceDate)
//					.map(i->Optional.ofNullable(i.getValue()).orElse(null))
//					.filter(Objects::nonNull)
//					.distinct()
//					.collect(Collectors.toList());
//			System.out.println("Exceptional Clearance Dates:");
//			exceptionalClearanceDates.forEach(System.out::println);
			
//			List<Date> arrestDates = groupAIncidentReports.stream()
//					.map(GroupAIncidentReport::getArresteeWithEarliestArrestDate)
//					.filter(Objects::nonNull)
//					.map(ArresteeSegment::getArrestDate)
//					.map(ParsedObject::getValue)
//					.distinct()
//					.collect(Collectors.toList());
//			System.out.println("Arrest Dates:");
//			arrestDates.forEach(System.out::println);
			outputWriter.close();

		}

	}
	
	private static final Options buildOptions() {
		Options options = new Options();
		options.addOption("h", "help", false, "Print usage and options info");
		options.addOption("f", "in", true, "Input submission file");
		options.addOption("o", "out", true, "Output error file");
		return options;
	}

}
