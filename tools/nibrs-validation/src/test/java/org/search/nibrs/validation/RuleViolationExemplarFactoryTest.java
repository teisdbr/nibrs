/*******************************************************************************
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.xml.XmlUtils;
import org.search.nibrs.xml.exporter.XMLExporter;
import org.w3c.dom.Document;

public class RuleViolationExemplarFactoryTest {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(RuleViolationExemplarFactoryTest.class);
	
	@Test
	public void test() throws Exception {
		// This test doesn't really test anything meaningful...just demonstrates how to use the exemplar factory
		NIBRSSubmission report = new NIBRSSubmission();
		RuleViolationExemplarFactory exemplarFactory = RuleViolationExemplarFactory.getInstance();
		AbstractReport incident = exemplarFactory.getGroupAIncidentsThatViolateRule(115).get(0);
		report.addReport(incident);
		Document d = new XMLExporter().convertNIBRSSubmissionToDocument(report, new ArrayList<NIBRSError>());
		//XmlUtils.printNode(d, System.out);
		String incidentNumberValue = XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport/nc:Incident/nc:ActivityIdentification/nc:IdentificationID");
		assertTrue(incidentNumberValue.trim().contains(" "));
		List<AbstractReport> incidents = new ArrayList<AbstractReport>();
		incidents.addAll(exemplarFactory.getGroupAIncidentsThatViolateRule(119));
		assertEquals(1, incidents.size());
		report = new NIBRSSubmission();
		report.addReports(incidents);
		d = new XMLExporter().convertNIBRSSubmissionToDocument(report, new ArrayList<NIBRSError>());
		//XmlUtils.printNode(d, System.out);
		assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport/j:OffenseSegment[nibrs:OffenseUCRCode='120']"));
	}
	
	@Test
	public void testOffenseLocationViolationExemplars() throws Exception {
		RuleViolationExemplarFactory exemplarFactory = RuleViolationExemplarFactory.getInstance();
		List<GroupAIncidentReport> incidents = exemplarFactory.getGroupAIncidentsThatViolateRule(252);
		assertEquals(2789, incidents.size());
	}

}
