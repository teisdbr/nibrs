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
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.XsltTransformer;
import org.search.nibrs.ndexconversion.util.NibrsUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class NdexNibrsTransformTest {
		
	
	@Test
	public void nibrsTransformTest() throws Exception{
		
		XsltTransformer xsltTransformer = new XsltTransformer();
		
		InputStream inFileStream = new FileInputStream("src/test/resources/xml/NDEx-NIBRS.xml");		
		SAXSource inputFileSource = OJBUtils.createSaxSource(inFileStream);
						
		File xsltFile = new File("src/main/resources/xsl/NIBRS_Transform.xsl");
		StreamSource xsltSource = new StreamSource(xsltFile);				
		
		Map<String, Object> paramMap = new HashMap<String, Object>();		
		paramMap.put("paramKey1", "value1");
		paramMap.put("paramKey2", "value2");
				
		String transformedXml = xsltTransformer.transform(inputFileSource, xsltSource, paramMap);
		 				
		XmlTestUtils.compareDocs("src/test/resources/xml/NDEx-NIBRS.out.xml", transformedXml);
		
		Document trasformedDoc = OJBUtils.loadXMLFromString(transformedXml);
	
		Node reportHeaderNode = NibrsUtils.xPathNodeSearch(trasformedDoc, "/nibrs:Report/nibrs:ReportHeader");
		
		String reportCatCode =  NibrsUtils.xPathStringSearch(reportHeaderNode, 
				"//nibrs:NIBRSReportCategoryCode");
		
		Assert.assertEquals("GROUP A INCIDENT REPORT", reportCatCode);
	}
	
	@Test
	public void nibrsUtilsTest() throws Exception{
		
		Document ndexNibrsDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/NDEx-NIBRS.out.xml"));
		
		Node reportHeaderNode = NibrsUtils.xPathNodeSearch(ndexNibrsDoc, "/nibrs:Report/nibrs:ReportHeader");
		
		String reportCatCode =  NibrsUtils.xPathStringSearch(reportHeaderNode, 
				"//nibrs:NIBRSReportCategoryCode");	
		
		Assert.assertEquals("GROUP A INCIDENT REPORT", reportCatCode);
	}
	
}
