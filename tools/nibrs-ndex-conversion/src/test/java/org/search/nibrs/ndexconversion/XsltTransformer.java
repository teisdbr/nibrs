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

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;


public class XsltTransformer {
	
	public String transform(Source sourceXml, Source sourceXsl, Map<String,Object> params) {

		if (sourceXml == null) {
			return null;
		}

		StringWriter stringWriter = new StringWriter();
		
		try {			
			String saxonTransformerName = net.sf.saxon.TransformerFactoryImpl.class.getCanonicalName();
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance(saxonTransformerName, null);
			
			Transformer transformer;
			if (sourceXsl == null) {
				transformer = transformerFactory.newTransformer();
			} else {
				transformer = transformerFactory.newTransformer(sourceXsl);
			}

			addParams(transformer,params);
			transformer.transform(sourceXml, new StreamResult(stringWriter));
			return stringWriter.toString();

		} catch (TransformerException e) {
			throw new RuntimeException("An error occured when applying XSLT - " + e.getMessage(), e);
		}

	}

	private void addParams(Transformer transformer,Map<String,Object> params) {
		if(params == null){
			return;
		}
		for(Entry<String, Object> entry: params.entrySet()){
			transformer.setParameter(entry.getKey(), entry.getValue());
		}
    }
	
}

