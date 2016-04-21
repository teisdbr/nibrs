package org.search.nibrs.ndexconversion.util;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.search.nibrs.ndexconversion.util.xml.NibrsNsContext;
import org.w3c.dom.Node;


public class NibrsUtils {

	
	public static final NibrsNsContext NIBRS_NS_CONTEXT = new NibrsNsContext();

	
	public static final String xPathStringSearch(Node context, String xPath)
			throws Exception {

		if (xPath == null) {
			return null;
		}
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(NIBRS_NS_CONTEXT);
		XPathExpression expression = xpath.compile(xPath);
		
		return (String) expression.evaluate(context, XPathConstants.STRING);
	}

	public static final Node xPathNodeSearch(Node context, String xPath)
			throws Exception {
		
		if (xPath == null) {
			return null;
		}
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(NIBRS_NS_CONTEXT);
		XPathExpression expression = xpath.compile(xPath);
		
		return (Node) expression.evaluate(context, XPathConstants.NODE);
	}

}
