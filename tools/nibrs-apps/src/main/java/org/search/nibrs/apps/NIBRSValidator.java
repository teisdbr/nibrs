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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.errorexport.ErrorExporter;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.importer.ReportListener;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.validation.SubmissionValidator;

/**
 * Executable class (via main) that accepts a submission file (via stdin, or
 * optionally a specified file) and validates the submission, writing the error
 * report to stdout (or optionally to a specified file).
 */
public class NIBRSValidator {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(NIBRSValidator.class);
	
	public static void main(String[] args) throws ParseException, IOException {

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

			if (cl.hasOption("f")) {
				String fileName = cl.getOptionValue("f");
				File file = new File(fileName);
				if (file.exists()) {
					inputReader = new BufferedReader(new FileReader(file));
					readerLocationName = file.getAbsolutePath();
				} else {
					System.err.println("File " + fileName + " does not exist.");
					System.exit(1);
				}
			} else {
				inputReader = new BufferedReader(new InputStreamReader(System.in));
			}

			if (cl.hasOption("o")) {
				String fileName = cl.getOptionValue("o");
				File file = new File(fileName);
				outputWriter = new BufferedWriter(new FileWriter(file));
			} else {
				outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
			}

			IncidentBuilder incidentBuilder = new IncidentBuilder();
			SubmissionValidator submissionValidator = new SubmissionValidator();
			ErrorExporter errorExporter = ErrorExporter.getInstance();

			final List<NIBRSError> errorList = new ArrayList<>();

			incidentBuilder.addIncidentListener(new ReportListener() {
				@Override
				public void newReport(AbstractReport report, List<NIBRSError> el) {
					errorList.addAll(el);
					errorList.addAll(submissionValidator.validateReport(report));
				}
			});

			incidentBuilder.buildIncidents(inputReader, readerLocationName);

			errorExporter.createErrorReport(errorList, outputWriter);

			outputWriter.close();
			inputReader.close();

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
