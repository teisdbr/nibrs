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
package org.search.nibrs.web.flatfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.flatfile.importer.ReportListener;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.validation.SubmissionValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FlatfileController {
	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
	
	    return "submissionForm";
	}
	
    @PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes, Model model) throws IOException {

		String readerLocationName = "console";
		
		Reader inputReader = new BufferedReader(new
				InputStreamReader(multipartFile.getInputStream()));
		
		IncidentBuilder incidentBuilder = new IncidentBuilder();
		SubmissionValidator submissionValidator = new SubmissionValidator();

		final List<NIBRSError> errorList = new ArrayList<>();

		incidentBuilder.addIncidentListener(new ReportListener() {
			@Override
			public void newReport(AbstractReport report, List<NIBRSError> el) {
				errorList.addAll(el);
				errorList.addAll(submissionValidator.validateReport(report));
			}
		});

		incidentBuilder.buildIncidents(inputReader, readerLocationName);

		model.addAttribute("errorList", errorList);
        return "validationReport :: #content";
    }

	
}

