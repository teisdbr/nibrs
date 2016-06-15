package org.search.nibrs.validation;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.search.nibrs.model.Incident;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.xml.XmlUtils;
import org.search.nibrs.xml.exporter.XMLExporter;
import org.w3c.dom.Document;

public class RuleViolationExemplarFactoryTest {
	
	@Test
	public void test() throws Exception {
		// This test doesn't really test anything meaningful...just demonstrates how to use the exemplar factory
		NIBRSSubmission report = new NIBRSSubmission();
		RuleViolationExemplarFactory exemplarFactory = RuleViolationExemplarFactory.getInstance();
		report.addIncident(exemplarFactory.getIncidentsThatViolateRule(115).get(0));
		Document d = new XMLExporter().convertNIBRSSubmissionToDocument(report);
		//XmlUtils.printNode(d, System.out);
		String incidentNumberValue = XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:Report/nc:Incident/nc:ActivityIdentification/nc:IdentificationID");
		assertTrue(incidentNumberValue.trim().contains(" "));
		List<Incident> incidents = exemplarFactory.getIncidentsThatViolateRule(119);
		assertEquals(2, incidents.size());
		report = new NIBRSSubmission();
		report.addIncidents(incidents);
		d = new XMLExporter().convertNIBRSSubmissionToDocument(report);
		//XmlUtils.printNode(d, System.out);
		assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:Report/j:Offense[nibrs:OffenseUCRCode='13A']"));
		assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:Report/j:Offense[nibrs:OffenseUCRCode='13B']"));
	}

}
