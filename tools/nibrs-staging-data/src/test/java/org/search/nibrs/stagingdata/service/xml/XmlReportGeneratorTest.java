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

import static org.junit.Assert.assertNotNull;

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
		
		administrativeSegmentRepository.findAll(); 
		
		administrativeSegment =  administrativeSegmentRepository.findByIncidentNumber("1234568910");
		assertNotNull(administrativeSegment);
		log.info(administrativeSegment);
		
		Document document = xmlReportGenerator.createGroupAIncidentReport(administrativeSegment);
		XmlUtils.printNode(document.getDocumentElement());
		
		administrativeSegment =  administrativeSegmentRepository.findByIncidentNumber("54236732");
		assertNotNull(administrativeSegment);
		log.info(administrativeSegment);
		
		document = xmlReportGenerator.createGroupAIncidentReport(administrativeSegment);
		XmlUtils.printNode(document.getDocumentElement());

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
	
}
