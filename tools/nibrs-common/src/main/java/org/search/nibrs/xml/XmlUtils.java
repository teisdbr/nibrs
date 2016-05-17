package org.search.nibrs.xml;

import java.io.File;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.search.nibrs.xml.NibrsNamespaceContext.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Utilities class for handling common XML tasks.
 *
 */
public class XmlUtils {
	
	private static final NamespaceContext NAMESPACE_CONTEXT = new NibrsNamespaceContext();
	
	/**
	 * Create a new element as a child of the specified parent and append it.  Use the specified namespace context to set the element's namespace prefix
	 * explicitly.
	 * @param parent the new element's parent
	 * @param namespace the namespace URI of the new element
	 * @param name the name of the new element
	 * @return the new element
	 */
	public static final Element appendChildElement(Node parent, Namespace namespace, String name) {
		
		Document doc = parent instanceof Document ? (Document) parent : parent.getOwnerDocument();
		Element ret = doc.createElementNS(namespace.uri, name);
		parent.appendChild(ret);
		ret.setPrefix(namespace.prefix);
		return ret;
	}
	
	/**
	 * Add an attribute to the specified element
	 * @param e the element
	 * @param namespace the namespace of the attribute
	 * @param attributeName the name of the attribute
	 * @param attributeValue the value of the attribute
	 * @return the element
	 */
	public static final Element addAttribute(Element e, Namespace namespace, String attributeName, String attributeValue) {
		e.setAttributeNS(namespace.uri, namespace.prefix + ":" + attributeName, attributeValue);
		return e;
	}

	/**
	 * Create a new DOM document that is namespace-aware.
	 * @return the new document
	 * @throws ParserConfigurationException
	 */
	public static final Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().newDocument();
	}
	
	/**
     * Print the specified XML DOM node to the specified output stream
     * 
     * @param n the node to print
     * @param os the output stream to print to
     * @throws Exception
     */
    public static void printNode(Node n, OutputStream os) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = setupTransformerOptions(tf.newTransformer());
        t.transform(new DOMSource(n), new StreamResult(os));
    }
    
    /**
     * Print the specified XML DOM node to System.out
     * 
     * @param n
     *            the node to print
     * @throws Exception
     */
    public static final void printNode(Node n) throws Exception {
        printNode(n, System.out);
    }    
    
    /**
     * Read the contents of the specified file into a DOM document
     * @param f the input XML file
     * @return the document
     * @throws Exception
     */
    public static final Document toDocument(File f) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(f);
    }    
    
    /**
     * Read the contents of the specified String into a DOM document
     * @param xml the String containing the XML
     * @return the document
     * @throws Exception
     */
    public static final Document toDocument(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }	
    
    /**
     * Write the specified DOM node into a String.
     * @param node the node to write
     * @return the xml string
     * @throws Exception
     */
    public static final String nodeToString(Node node) throws Exception {
    	StringWriter writer = new StringWriter();
    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
    	setupTransformerOptions(transformer);
    	transformer.transform(new DOMSource(node), new StreamResult(writer));
    	return writer.toString();
    }
    
    /**
     * Search the context node for a node that matches the specified xpath
     * 
     * @param context the node that's the context for the xpath
     * @param xPath the xpath query
     * @return the matching object, or null if no match
     * @throws Exception
     */
    public static final Node xPathNodeSearch(Node context, String xPath) throws Exception {
        if (xPath == null)
        {
            return null;
        }
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        XPathExpression expression = xpath.compile(xPath);
        return (Node) expression.evaluate(context, XPathConstants.NODE);
    }

    /**
     * Search the context node for a String that matches the specified xpath
     * @param context the node that's the context for the xpath
     * @param xPath the xpath query
     * @return the matching string, or null if no match
     * @throws Exception
     */
    public static final String xPathStringSearch(Node context, String xPath) throws Exception {
    	
        if (xPath == null) {
            return null;
        }
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        XPathExpression expression = xpath.compile(xPath);
        return (String) expression.evaluate(context, XPathConstants.STRING);
        
    }    
    
    /**
     * Search the context node for a node set that matches the specified xpath
     * @param context the node that's the context for the xpath
     * @param xPath the xpath query
     * @return the matching node, or null if no match
     * @throws Exception
     */
    public static final NodeList xPathNodeListSearch(Node context, String xPath) throws Exception {
    	
        if (xPath == null) {
            return null;
        }
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        XPathExpression expression = xpath.compile(xPath);
        return (NodeList) expression.evaluate(context, XPathConstants.NODESET);        

    }
    
    private static final Transformer setupTransformerOptions(Transformer t) {
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		return t;
    }
    
}

