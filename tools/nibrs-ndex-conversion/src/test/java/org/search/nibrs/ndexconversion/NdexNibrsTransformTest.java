package org.search.nibrs.ndexconversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Test;
import org.search.nibrs.xml.NibrsNamespaceContext.Namespace;
import org.search.nibrs.xml.XmlTestUtils;
import org.search.nibrs.xml.XmlUtils;
import org.search.nibrs.xml.XsltTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
						
		reportCatCode = reportCatCode.trim();
		
		Assert.assertEquals("GROUP A INCIDENT REPORT", reportCatCode);
	}
	
	@Test
	public void nibrsUtilsTest() throws Exception{
		
		Document ndexNibrsDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/NDEx-NIBRS.out.xml"));
		
		Node reportHeaderNode = XmlUtils.xPathNodeSearch(ndexNibrsDoc, "/nibrs:Report/nibrs:ReportHeader");
		
		String sReportCatCode =  XmlUtils.xPathStringSearch(reportHeaderNode, 
				"//nibrs:NIBRSReportCategoryCode");	
		
		sReportCatCode = sReportCatCode.trim();
		
		Assert.assertEquals("GROUP A INCIDENT REPORT", sReportCatCode);
		
		Node reportCatCodeNode = XmlUtils.xPathNodeSearch(reportHeaderNode, "//nibrs:NIBRSReportCategoryCode");
		reportCatCodeNode.setTextContent("New Value");
		
		String sReportCatCodeNewValue = XmlUtils.xPathStringSearch(reportHeaderNode, "//nibrs:NIBRSReportCategoryCode");
		
		Assert.assertEquals("New Value", sReportCatCodeNewValue);		
	}
	
	
	
	@Test
	public void nibrsLexsStructPayloadRemovalTransformTest() throws Exception{
		
		XsltTransformer xsltTransformer = new XsltTransformer();
		
						
		File xsltFile = new File("src/main/resources/xsl/NIBRS_Transform.xsl");
		StreamSource xsltSource = new StreamSource(xsltFile);				
								
		Document inputFileDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/NDEx-NIBRS.xml"));		
		
		NodeList structPayloadNodeList = XmlUtils.xPathNodeListSearch(inputFileDoc, "//lexs31:StructuredPayload");
		
		// remove every occurrence of lexs31:StructuredPayload
		for(int i=0; i < structPayloadNodeList.getLength(); i++){
			
			Node strucPayloadNode = structPayloadNodeList.item(i);
			
			Node structPayloadParent = strucPayloadNode.getParentNode();
			
			structPayloadParent.removeChild(strucPayloadNode);
		}
									
		String sInputDoc = XmlUtils.getStringFromNode(inputFileDoc);
		
		SAXSource inputFileSource = XmlUtils.createSaxSource(sInputDoc);
		
		String transformedXml = xsltTransformer.transform(inputFileSource, xsltSource, null);
						
		Document trasformedDoc = XmlUtils.loadXMLFromString(transformedXml);
						
		Node offenseUcrCodeNode = XmlUtils.xPathNodeSearch(trasformedDoc, "/nibrs:Report/j:Offense/nibrs:OffenseUCRCode");

		// example printing document to console
		XmlUtils.printNode(trasformedDoc);
		
		Assert.assertNull(offenseUcrCodeNode);			
	}	
	
	
	@Test
	public void nibrsAddWarrantActivityTest() throws Exception{
		
		XsltTransformer xsltTransformer = new XsltTransformer();
								
		File xsltFile = new File("src/main/resources/xsl/NIBRS_Transform.xsl");
		StreamSource xsltSource = new StreamSource(xsltFile);				
					
		Document inputFileDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/NDEx-NIBRS.xml"));
						
		Node digestNode = XmlUtils.xPathNodeSearch(inputFileDoc, 
				"/lexspd31:doPublish/lexs31:PublishMessageContainer/lexs31:PublishMessage/lexs31:DataItemPackage/lexs31:Digest");
		
		
		
		//add Warrant section that should not affect the output		
		Element warrantEntityActivityEl = XmlUtils.appendChildElement(digestNode, Namespace.lexsdigest31, "EntityActivity");
		
		Element activityElement = XmlUtils.appendChildElement(warrantEntityActivityEl, Namespace.nc, "Activity");
		
		XmlUtils.addAttribute(activityElement, Namespace.structures20, "id", "Warrant_1");
		
		Element activityIdElement = XmlUtils.appendChildElement(activityElement, Namespace.nc, "ActivityIdentification");
		
		Element activityIdValueElement = XmlUtils.appendChildElement(activityIdElement, Namespace.nc, "IdentificationID");
		
		activityIdValueElement.setTextContent("1234567");
		// done adding warrant section				
		
		
		
		String sInputDoc = XmlUtils.getStringFromNode(inputFileDoc);
		
		SAXSource intputSaxSource =  XmlUtils.createSaxSource(sInputDoc);
		
		String transformedXml = xsltTransformer.transform(intputSaxSource, xsltSource, null);
		 				
		// should expect same transformation result - even though warrant section added above to input document
		XmlTestUtils.compareDocs("src/test/resources/xml/NDEx-NIBRS.out.xml", transformedXml);		
	}	
	
	
}

