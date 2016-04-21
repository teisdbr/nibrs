package org.search.nibrs.ndexconversion.util.xml;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class NibrsNsContext implements NamespaceContext{
	
	BiMap<String, String> uriToPrefixBiMap;
		
	public NibrsNsContext() {
		
		uriToPrefixBiMap = HashBiMap.create();
					
		uriToPrefixBiMap.put("http://fbi.gov/cjis/nibrs/4.0", "nibrs");
	}

	@Override
	public String getNamespaceURI(String prefix) {

		return uriToPrefixBiMap.inverse().get(prefix);
	}

	@Override
	public String getPrefix(String namespace) {

		return uriToPrefixBiMap.get(namespace);
	}

	@Override
	public Iterator getPrefixes(String arg0) {

		return null;
	}

}
