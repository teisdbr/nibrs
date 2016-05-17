package org.search.nibrs.xml;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;

/**
 * A class of utilities to support assertions/comparisons of XML documents.
 *
 */
public class XmlTestUtils {
	
	static {		
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	/**
	 * Test whether an XML document matches an expected document
	 * @param expectedXML the "gold standard" expected XML
	 * @param testXML the XML to compare against the expected document
	 * @throws Exception
	 */
	public static final void compareDocuments(String expectedXML, String testXML) throws Exception {
		File xmlFile = new File(expectedXML);
		Document expectedXmlDoc = XmlUtils.toDocument(xmlFile);
		Document actualXmlDoc = XmlUtils.toDocument(testXML);
		compareDocuments(expectedXmlDoc, actualXmlDoc);		
	}	
	
	/**
	 * Test whether an XML document matches an expected document
	 * @param expectedXML the "gold standard" expected XML
	 * @param testXML the XML to compare against the expected document
	 * @throws Exception
	 */
	public static final void compareDocuments(String expectedXML, Document testXML) throws Exception {
		File xmlFile = new File(expectedXML);
		Document expectedXmlDoc = XmlUtils.toDocument(xmlFile);
		compareDocuments(expectedXmlDoc, testXML);		
	}
	
	/**
	 * Compare two documents and assert that the number of differences is zero.
	 * @param expectedDocument the "gold standard" expected XML
	 * @param testDocument the XML to compare against the expected document
	 */
	public static final void compareDocuments(Document expectedDocument, Document testDocument) {

		Diff diff = new Diff(expectedDocument, testDocument);						
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		@SuppressWarnings("all")
		List<Difference> diffList = detailedDiff.getAllDifferences();		
		int diffCount = diffList == null ? 0 : diffList.size();
		
		Assert.assertEquals(detailedDiff.toString(), 0, diffCount);
		
	}
	
}
