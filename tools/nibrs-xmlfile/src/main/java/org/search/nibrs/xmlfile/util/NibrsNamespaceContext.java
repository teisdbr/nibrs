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
package org.search.nibrs.xmlfile.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class that contains all the NIBRS XML doc namespaces used across samples
 * 
 */
public final class NibrsNamespaceContext implements NamespaceContext {

	private static final Log log = LogFactory.getLog(NibrsNamespaceContext.class);
	
	public enum NIBRS_NAMESPACES {
		NIBRS("nibrs", "http://fbi.gov/cjis/nibrs/4.0" ), 
		CJIS("cjis", "http://fbi.gov/cjis/1.0"), 
		CJISCODES("cjiscodes", "http://fbi.gov/cjis/cjis-codes/1.0"), 
		I("i", "http://release.niem.gov/niem/appinfo/3.0/"), 
		UCR("ucr", "http://release.niem.gov/niem/codes/fbi_ucr/3.2/"), 
		J("j", "http://release.niem.gov/niem/domains/jxdm/5.2/"), 
		TERM("term", "http://release.niem.gov/niem/localTerminology/3.0/"), 
		NC("nc", "http://release.niem.gov/niem/niem-core/3.0/"), 
		NIEM_XSD("niem-xsd", "http://release.niem.gov/niem/proxy/xsd/3.0/"), 
		S("s", "http://release.niem.gov/niem/structures/3.0/"), 
		XSI("xsi", "http://www.w3.org/2001/XMLSchema-instance"), 
		XSD("xsd", "http://www.w3.org/2001/XMLSchema"), 
		NIBRSCODES("nibrscodes", "http://fbi.gov/cjis/nibrs/nibrs-codes/4.0");
		
		private String namespace;
		
		private String prefix;
		
		private NIBRS_NAMESPACES( String prefix, String namespace){
			
			this.prefix = prefix;
			this.namespace = namespace;
			
		}

		public String getNamespace() {
			return namespace;
		}
		
		public String getPrefix() {
			return prefix;
		}

	}
		
	private Map<String, String> prefixToUriMap;
	private Map<String, String> uriToPrefixMap;
	
	public NibrsNamespaceContext() {
		
		prefixToUriMap = new HashMap<String, String>();
		uriToPrefixMap = new HashMap<String, String>();	

		for (NIBRS_NAMESPACES nibrsNamespace : NIBRS_NAMESPACES.values()){
			prefixToUriMap.put(nibrsNamespace.getPrefix(), nibrsNamespace.getNamespace());
			uriToPrefixMap.put(nibrsNamespace.getNamespace(), nibrsNamespace.getPrefix());
		}

	}

	@Override
	public String getNamespaceURI(String prefix) {
		return prefixToUriMap.get(prefix);
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return uriToPrefixMap.get(namespaceURI);
	}

	@Override
	public Iterator<String> getPrefixes(String arg0) {
		return null;
	}

	/**
	 * This method collects all of the namespaces that are actually used, in this element and all of its descendants, and declares the proper prefixes
	 * on this element.
	 * 
	 * @param e
	 *            the element on which to declare the namespace prefixes for itself and its descendants
	 */
	public void populateRootNamespaceDeclarations(Element e) {
		Set<String> namespaceURIs = collectNamespaceURIs(e);
		for (String uri : namespaceURIs) {
			String prefix = getPrefix(uri);
			if (prefix == null) {
				if (!"http://www.w3.org/2000/xmlns/".equals(uri)) {
					log.warn("Namespace URI " + uri + " not found in OjbcNamespaceContext");
				}
			} else {
				e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, uri);
			}
		}
	}

	private Set<String> collectNamespaceURIs(Node e) {
		Set<String> ret = new HashSet<String>();
		String uri = e.getNamespaceURI();
		if (uri != null) {
			ret.add(uri);
		}
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				ret.addAll(collectNamespaceURIs(child));
			}
		}
		NamedNodeMap attributeMap = e.getAttributes();
		for (int i = 0; i < attributeMap.getLength(); i++) {
			Node attr = attributeMap.item(i);
			uri = attr.getNamespaceURI();
			if (uri != null) {
				ret.add(uri);
			}
		}
		return ret;
	}

}
