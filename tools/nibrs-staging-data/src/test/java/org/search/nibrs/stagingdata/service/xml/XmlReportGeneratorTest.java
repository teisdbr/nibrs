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
package org.search.nibrs.stagingdata.service.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepository;
import org.search.nibrs.stagingdata.repository.segment.ArrestReportSegmentRepository;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentFactory;
import org.search.nibrs.stagingdata.service.ArrestReportSegmentFactory;
import org.search.nibrs.stagingdata.service.ArrestReportService;
import org.search.nibrs.stagingdata.service.GroupAIncidentService;
import org.search.nibrs.stagingdata.util.BaselineIncidentFactory;
import org.search.nibrs.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlReportGeneratorTest {
	private static final Log log = LogFactory.getLog(XmlReportGeneratorTest.class);

	@Autowired
	public AdministrativeSegmentRepository administrativeSegmentRepository; 
	@Autowired
	public ArrestReportSegmentRepository arrestReportSegmentRepository; 
	@Autowired
	public AdministrativeSegmentFactory administrativeSegmentFactory; 
	@Autowired
	public ArrestReportSegmentFactory arrestReportSegmentFactory; 
	@Autowired
	public GroupAIncidentService groupAIncidentService;
	@Autowired
	public ArrestReportService arrestReportService;
	@Autowired
	public XmlReportGenerator xmlReportGenerator;
	

	@Test
	public void testCreateGroupAIncidentReport() throws Exception {
		AdministrativeSegment administrativeSegment = administrativeSegmentFactory.getBasicAdministrativeSegment();
		administrativeSegmentRepository.save(administrativeSegment);
		
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncident();
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		administrativeSegment =  administrativeSegmentRepository.findByIncidentNumber("1234568910");
		assertNotNull(administrativeSegment);
		log.info(administrativeSegment);
		
		Document document = xmlReportGenerator.createGroupAIncidentReport(administrativeSegment);
		XmlUtils.printNode(document.getDocumentElement());
		
//		File expectedFile = 
//				new File("src/test/resources/xmlInstances/groupAIncidentFromAdminsitrativeFactory.xml");
//		
//		Document expectedDocument = XmlUtils.parseFileToDocument(expectedFile);
//
//		XmlTestUtils.compareDocs(expectedDocument, document, new String[]{"nc:Date", "nc:DateTime"});
		
		administrativeSegment =  administrativeSegmentRepository.findByIncidentNumber("54236732");
		assertNotNull(administrativeSegment);
		log.info(administrativeSegment);
		
		document = xmlReportGenerator.createGroupAIncidentReport(administrativeSegment);
		toString(document);
		
		File expectedFile = 
				new File("src/test/resources/xmlInstances/groupAIncidentFromAdminsitrativeFactory.xml");
		Document expectedDocument = XmlUtils.parseFileToDocument(expectedFile);

		List<String> ignorableNames = Arrays.asList(new String[]{"cjis:MessageDateTime", "cjis:MessageIdentification", "nc:Item", "nc:Substance"}); 
		final Diff documentDiff = DiffBuilder
	            .compare(toString(expectedDocument))
	            .withTest(toString(document))
	            .normalizeWhitespace()
	            .ignoreComments()
	            .ignoreWhitespace()
	            .withNodeFilter(node -> !ignorableNames.contains(node.getNodeName()))
	            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
	            .checkForSimilar()
	            .build();
		assertThat(documentDiff.hasDifferences(), equalTo(false)); 
	}

	@Test
	public void testCreateGroupBArrestReport() throws Exception {
		GroupBArrestReport groupBArrestReport = BaselineIncidentFactory.getBaselineGroupBArrestReport();
		arrestReportService.saveGroupBArrestReports(groupBArrestReport);
		
		ArrestReportSegment arrestReportSegment = arrestReportSegmentFactory.getBasicArrestReportSegment();
		arrestReportSegmentRepository.save(arrestReportSegment);
		
		arrestReportSegment =  arrestReportSegmentRepository.findByArrestTransactionNumber("12345");
		assertNotNull(arrestReportSegment);
		log.info(arrestReportSegment);
		
		Document document = xmlReportGenerator.createGroupBArrestReport(arrestReportSegment);
		XmlUtils.printNode(document.getDocumentElement());
		
		arrestReportSegment =  arrestReportSegmentRepository.findByArrestTransactionNumber("arrestTr");
		assertNotNull(arrestReportSegment);
		log.info(arrestReportSegment);
		
		document = xmlReportGenerator.createGroupBArrestReport(arrestReportSegment);
		XmlUtils.printNode(document.getDocumentElement());
		
	}
	
	 private String toString(Document newDoc) throws Exception{
		    DOMSource domSource = new DOMSource(newDoc);
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    StringWriter sw = new StringWriter();
		    StreamResult sr = new StreamResult(sw);
		    transformer.transform(domSource, sr);
		    return sw.toString();  
	 }
}
