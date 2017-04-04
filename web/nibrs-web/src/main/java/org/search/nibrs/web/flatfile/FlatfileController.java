package org.search.nibrs.web.flatfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FlatfileController {
	@RequestMapping("/")
    public String index(Model model) throws IOException {
		Reader inputReader = null;
		String readerLocationName = "console";
		
		File file;
		try {
			file = ResourceUtils.getFile("src/test/resources/test.txt");
			inputReader = new BufferedReader(new
					InputStreamReader(getClass().getClassLoader().getResourceAsStream("instances/test.txt")));
			readerLocationName = file.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

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
        return "flatfileUpload";
    }
}
