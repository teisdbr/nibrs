package org.search.nibrs.ndexconversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.junit.Test;
import org.search.nibrs.xml.XmlTestUtils;
import org.search.nibrs.xml.XmlUtils;
import org.search.nibrs.xml.XsltTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class NdexNibrsTransformTest {
		
	
	@Test
	public void nibrsTransformTest() throws Exception{
		
		XsltTransformer xsltTransformer = new XsltTransformer();
		
		InputStream inFileStream = new FileInputStream("src/test/resources/xml/NDEx-NIBRS.xml");		
		SAXSource inputFileSource = XmlUtils.createSaxSource(inFileStream);
						
		File xsltFile = new File("src/main/resources/xsl/NIBRS_Transform.xsl");
		StreamSource xsltSource = new StreamSource(xsltFile);				
		
		Map<String, Object> paramMap = new HashMap<String, Object>();		
		paramMap.put("paramKey1", "value1");
		paramMap.put("paramKey2", "value2");
				
		String transformedXml = xsltTransformer.transform(inputFileSource, xsltSource, paramMap);
		 				
		XmlTestUtils.compareDocs("src/test/resources/xml/NDEx-NIBRS.out.xml", transformedXml);
		
		Document trasformedDoc = XmlUtils.loadXMLFromString(transformedXml);
	
		Node reportHeaderNode = XmlUtils.xPathNodeSearch(trasformedDoc, "/nibrs:Report/nibrs:ReportHeader");
		
		String reportCatCode =  XmlUtils.xPathStringSearch(reportHeaderNode, 
				"//nibrs:NIBRSReportCategoryCode");
		
		Assert.assertEquals("GROUP A INCIDENT REPORT", reportCatCode);
	}
	
	@Test
	public void nibrsUtilsTest() throws Exception{
		
		Document ndexNibrsDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/NDEx-NIBRS.out.xml"));
		
		Node reportHeaderNode = XmlUtils.xPathNodeSearch(ndexNibrsDoc, "/nibrs:Report/nibrs:ReportHeader");
		
		String sReportCatCode =  XmlUtils.xPathStringSearch(reportHeaderNode, 
				"//nibrs:NIBRSReportCategoryCode");	
		
		Assert.assertEquals("GROUP A INCIDENT REPORT", sReportCatCode);
		
		Node reportCatCodeNode = XmlUtils.xPathNodeSearch(reportHeaderNode, "//nibrs:NIBRSReportCategoryCode");
		reportCatCodeNode.setTextContent("New Value");
		
		String sReportCatCodeNewValue = XmlUtils.xPathStringSearch(reportHeaderNode, "//nibrs:NIBRSReportCategoryCode");
		
		Assert.assertEquals("New Value", sReportCatCodeNewValue);		
	}
	
}
