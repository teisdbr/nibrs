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
package org.search.nibrs.web.uploadfile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.importer.ReportListener;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.util.NibrsFileUtils;
import org.search.nibrs.validation.SubmissionValidator;
import org.search.nibrs.xmlfile.importer.XmlIncidentBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadFileController {
	private final Log log = LogFactory.getLog(this.getClass());

	final List<String> acceptedFileTypes = Arrays.asList("application/zip", "text/plain", "application/octet-stream", "text/xml", "application/xml");
	
	@GetMapping("/")
	public String getFileUploadForm(Model model) throws IOException {
	
	    return "index";
	}
	
    @PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile[] multipartFiles,
			RedirectAttributes redirectAttributes, Model model) throws IOException, ParserConfigurationException {

		log.info("processing file: " + multipartFiles.length);
		
		SubmissionValidator submissionValidator = new SubmissionValidator();
		final List<NIBRSError> errorList = new ArrayList<>();
		ReportListener validatorlistener = new ReportListener() {
			@Override
			public void newReport(AbstractReport report, List<NIBRSError> el) {
				errorList.addAll(el);
				errorList.addAll(submissionValidator.validateReport(report));
			}
		};
		
		for (MultipartFile multipartFile: multipartFiles){
			if (!acceptedFileTypes.contains(multipartFile.getContentType())){
				throw new IllegalArgumentException("The file type is not supported"); 
			}
			
			if (multipartFile.getContentType().equals("application/zip")){
				validateZippedFile( validatorlistener, multipartFile.getInputStream());
			}
			else {
				validateInputStream(validatorlistener, multipartFile.getContentType(), multipartFile.getInputStream());
			}
			
		}
		
		List<NIBRSError> filteredErrorList = errorList.stream()
				.filter(error->error.getReport() != null)
				.collect(Collectors.toList()); 
		model.addAttribute("errorList", filteredErrorList);
        return "validationReport :: #content";
    }

	public void validateInputStream(ReportListener validatorlistener, String fileContentType,
			InputStream stream) throws ParserConfigurationException, IOException {
		String readerLocationName = "console";

		switch (fileContentType){
		case "text/xml":
		case "application/xml":
			XmlIncidentBuilder xmlIncidentBuilder = new XmlIncidentBuilder();
			xmlIncidentBuilder.addIncidentListener(validatorlistener);
			xmlIncidentBuilder.buildIncidents(stream, getClass().getName());

			break; 
		case "text/plain": 
		case "application/octet-stream": 
			Reader inputReader = new BufferedReader(new InputStreamReader(stream));
			IncidentBuilder incidentBuilder = new IncidentBuilder();

			incidentBuilder.addIncidentListener(validatorlistener);

			incidentBuilder.buildIncidents(inputReader, readerLocationName);
			inputReader.close();
			break;
		default:
			log.warn("The file type " + fileContentType + " is not supported"); 
		}
		
		stream.close();
	}

	@GetMapping("/about")
	public String getAbout(Model model){
	
	    return "about";
	}
	
	@GetMapping("/toolLimitations")
	public String getToolLimitations(Model model){
		
		return "toolLimitations";
	}
	
	@GetMapping("/resources")
	public String getResources(Model model){
		
		return "resources";
	}
	
	@GetMapping("/testFiles")
	public String getTestFiles(Model model){
		
		return "testFiles";
	}
	

	/**
	 * Get only the first entry of the zipped resource. 
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	private void validateZippedFile(ReportListener validatorlistener, InputStream inputStream) throws IOException {
		ZipInputStream zippedStream = new ZipInputStream(inputStream);

		ZipEntry zipEntry = zippedStream.getNextEntry();
		while ( zipEntry != null)
		{
			log.info("Unzipping " + zipEntry.getName());
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        for (int c = zippedStream.read(); c != -1; c = zippedStream.read()) {
	          bout.write(c);
	        }
	        
	        zippedStream.closeEntry();
			
			ByteArrayInputStream inStream = new ByteArrayInputStream( bout.toByteArray() );
			bout.close();
			
		    String mediaType = NibrsFileUtils.getMediaType(inStream);

		    try {
				validateInputStream(validatorlistener, mediaType, inStream);
			} catch (ParserConfigurationException e) {
				log.error("Got exception while parsing the file " + zipEntry.getName(), e);
			}
			zipEntry = zippedStream.getNextEntry();
		}
		zippedStream.close();
        inputStream.close();
	}
	
}

