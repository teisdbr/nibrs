/*******************************************************************************
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.util;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * An implementation of the NamespaceContext interface that uses a bi-directional map to associate prefixes with URIs.
 *
 */
public final class MapNamespaceContext implements NamespaceContext {
	
	private BidiMap<String, String> prefixUriMap = new DualHashBidiMap<String, String>();
	
	/**
	 * Add a prefix-URI mapping to the context
	 * @param prefix the prefix
	 * @param uri the URI
	 */
	public void add(String prefix, String uri) {
		prefixUriMap.put(prefix, uri);
	}

	public String getNamespaceURI(String prefix) {
		return prefixUriMap.get(prefix);
	}

	public String getPrefix(String namespaceURI) {
		return prefixUriMap.getKey(namespaceURI);
	}

	public Iterator<String> getPrefixes(String namespaceURI) {
		final String prefix = getPrefix(namespaceURI);
		return new Iterator<String>() {
			boolean hasBeenCalled = false;
			public boolean hasNext() {
				return !hasBeenCalled;
			}
			public String next() {
				hasBeenCalled = true;
				return prefix;
			}
		};
	}

}
