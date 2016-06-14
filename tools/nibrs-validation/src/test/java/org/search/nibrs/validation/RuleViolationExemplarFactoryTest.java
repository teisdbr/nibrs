package org.search.nibrs.validation;

import org.junit.Test;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.xml.XmlUtils;
import org.search.nibrs.xml.exporter.XMLExporter;
import org.w3c.dom.Document;

public class RuleViolationExemplarFactoryTest {
	
	@Test
	public void test() throws Exception {
		NIBRSSubmission report = new NIBRSSubmission();
		report.addIncident(RuleViolationExemplarFactory.getInstance().getIncidentThatViolatesRule(115));
		Document d = new XMLExporter().convertNIBRSSubmissionToDocument(report);
		XmlUtils.printNode(d, System.out);
	}

}
