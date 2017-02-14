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
package org.search.nibrs.ndexconversion;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.search.nibrs.xml.NibrsNamespaceContext.Namespace;
import org.search.nibrs.xml.XmlTestUtils;
import org.search.nibrs.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NdexNibrsTransformTest {		
	
	private static SAXSource createSaxSource(String xml) {
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		return new SAXSource(inputSource);
	}  
	
	private static SAXSource createSaxSource(InputStream inSream) {
		InputSource inputSource = new InputSource(inSream);
		return new SAXSource(inputSource);
	}	
	
	@Test
	@Ignore
//	TODO fix the junit test. 
	public void nibrsTransformTest() throws Exception{
		
		XsltTransformer xsltTransformer = new XsltTransformer();
		
		InputStream inFileStream = new FileInputStream("src/test/resources/xml/NDEx-NIBRS.xml");		
		SAXSource inputFileSource = createSaxSource(inFileStream);
						
		File xsltFile = new File("src/main/resources/xsl/NIBRS_Transform.xsl");
		StreamSource xsltSource = new StreamSource(xsltFile);				
		
		Map<String, Object> paramMap = new HashMap<String, Object>();		
		paramMap.put("paramKey1", "value1");
		paramMap.put("paramKey2", "value2");
				
		String transformedXml = xsltTransformer.transform(inputFileSource, xsltSource, paramMap);
		 				
		XmlTestUtils.compareDocuments("src/test/resources/xml/NDEx-NIBRS.out.xml", transformedXml);
		
		Document trasformedDoc = XmlUtils.toDocument(transformedXml);
	
		Node reportHeaderNode = XmlUtils.xPathNodeSearch(trasformedDoc, "/nibrs:AbstractReport/nibrs:ReportHeader");
		
		String reportCatCode =  XmlUtils.xPathStringSearch(reportHeaderNode, 
				"//nibrs:NIBRSReportCategoryCode");
						
		reportCatCode = reportCatCode.trim();
		
		Assert.assertEquals("GROUP A INCIDENT REPORT", reportCatCode);
	}
	
	@Test
	@Ignore
//	TODO fix the junit test. 
	public void nibrsUtilsTest() throws Exception{
		
		Document ndexNibrsDoc = XmlUtils.toDocument(new File("src/test/resources/xml/NDEx-NIBRS.out.xml"));
		
		Node reportHeaderNode = XmlUtils.xPathNodeSearch(ndexNibrsDoc, "/nibrs:AbstractReport/nibrs:ReportHeader");
		
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
								
		Document inputFileDoc = XmlUtils.toDocument(new File("src/test/resources/xml/NDEx-NIBRS.xml"));		
		
		NodeList structPayloadNodeList = XmlUtils.xPathNodeListSearch(inputFileDoc, "//lexs31:StructuredPayload");
		
		// remove every occurrence of lexs31:StructuredPayload
		for(int i=0; i < structPayloadNodeList.getLength(); i++){
			
			Node strucPayloadNode = structPayloadNodeList.item(i);
			
			Node structPayloadParent = strucPayloadNode.getParentNode();
			
			structPayloadParent.removeChild(strucPayloadNode);
		}
									
		String sInputDoc = XmlUtils.nodeToString(inputFileDoc);
		
		SAXSource inputFileSource = createSaxSource(sInputDoc);
		
		String transformedXml = xsltTransformer.transform(inputFileSource, xsltSource, null);
						
		Document trasformedDoc = XmlUtils.toDocument(transformedXml);
						
		Node offenseUcrCodeNode = XmlUtils.xPathNodeSearch(trasformedDoc, "/nibrs:AbstractReport/j:OffenseSegment/nibrs:OffenseUCRCode");

		// example printing document to console
		XmlUtils.printNode(trasformedDoc);
		
		Assert.assertNull(offenseUcrCodeNode);			
	}	
	
	
	@Test
	public void nibrsAddWarrantActivityTest() throws Exception{
		
		XsltTransformer xsltTransformer = new XsltTransformer();
								
		File xsltFile = new File("src/main/resources/xsl/NIBRS_Transform.xsl");
		StreamSource xsltSource = new StreamSource(xsltFile);				
					
		Document inputFileDoc = XmlUtils.toDocument(new File("src/test/resources/xml/NDEx-NIBRS.xml"));
						
		Node digestNode = XmlUtils.xPathNodeSearch(inputFileDoc, 
				"/lexspd31:doPublish/lexs31:PublishMessageContainer/lexs31:PublishMessage/lexs31:DataItemPackage/lexs31:Digest");
		
		
		
		//add Warrant section to input doc that should not affect the transformation output		
		Element warrantEntityActivityEl = XmlUtils.appendChildElement(digestNode, Namespace.lexsdigest31, "EntityActivity");
		
		Element activityElement = XmlUtils.appendChildElement(warrantEntityActivityEl, Namespace.nc, "Activity");
		
		XmlUtils.addAttribute(activityElement, Namespace.structures20, "id", "Warrant_1");
		
		Element activityIdElement = XmlUtils.appendChildElement(activityElement, Namespace.nc, "ActivityIdentification");
		
		Element activityIdValueElement = XmlUtils.appendChildElement(activityIdElement, Namespace.nc, "IdentificationID");
		
		activityIdValueElement.setTextContent("1234567");
		// done adding warrant section				
		
		
		
		String sInputDoc = XmlUtils.nodeToString(inputFileDoc);
		
		SAXSource intputSaxSource =  createSaxSource(sInputDoc);
		
		String transformedXml = xsltTransformer.transform(intputSaxSource, xsltSource, null);
		 				
		// should expect same transformation result - even though warrant section added above to input document
		XmlTestUtils.compareDocuments("src/test/resources/xml/NDEx-NIBRS.out.xml", transformedXml);		
	}	
	
	
}

