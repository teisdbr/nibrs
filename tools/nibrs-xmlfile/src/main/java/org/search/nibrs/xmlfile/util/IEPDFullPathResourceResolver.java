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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.search.nibrs.xmlfile.util.IEPDResourceResolver.LSInputImpl;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A DOM Resource Resolver implementation that helps with reading schemas out of IEPDs.
 * 
 */
public class IEPDFullPathResourceResolver implements LSResourceResolver{

	private static final List<String> PATHS_FOUND_LIST = new ArrayList<String>();
	
	private Logger logger;
	
	private List<String> parentClassPathDirsToSearch;
	
	/**
	 * @param parentClasspathDirsToSearch
	 * 		a list of parent paths that will be used when searching for 
	 * 		child xsd files as subpaths
	 */
	public IEPDFullPathResourceResolver(List<String> pParentClasspathDirsToSearch) {	
		
		parentClassPathDirsToSearch = pParentClasspathDirsToSearch;
		logger = Logger.getLogger(IEPDFullPathResourceResolver.class.getName());
	}
	
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI,
			String publicId, String systemId, String baseURI) {

		List<String> possibleSysIdPathList = new ArrayList<String>();			
		possibleSysIdPathList.add(systemId);		
		String sysIdWithoutRelativeSlashes = systemId.replaceAll("\\.\\./", "");
		possibleSysIdPathList.add(sysIdWithoutRelativeSlashes);
																			
		List<String> pathsFailedList = new ArrayList<String>();
				
		
		for(String iBeginningOfPath : parentClassPathDirsToSearch){		
			
			for(String iEndOfPath : possibleSysIdPathList){		
							    
			    String urlString = iBeginningOfPath + "/" + iEndOfPath;
			    urlString = urlString.replace("//", "/");
				URL testUrl = getClass().getClassLoader().getResource(urlString);
			    			    								
				InputStream resourceInStream = null;
				
				try {													
					
					if(testUrl != null){
						
						resourceInStream = testUrl.openStream();
						
						logger.info("Found Resource: " + testUrl.toString());
					}										
				} catch (IOException e) {
					//ignore
				}
												
				if(resourceInStream != null){						
					PATHS_FOUND_LIST.add(testUrl.toString());					
					pathsFailedList.clear();								
					return new LSInputImpl(publicId, systemId, resourceInStream);					
				}else{					
					pathsFailedList.add(systemId);									
				}				
			}				
		}			
		
		String failPathsReadable = "";
		for(String iFail : pathsFailedList){
			failPathsReadable += "\n" + iFail;
		}
		
		String foundPathsReadable = "";
		for(String iPass : PATHS_FOUND_LIST){
			foundPathsReadable += "\n" + iPass;
		}		
		
		// would not arrive here if a path was found
		throw new RuntimeException("Did not resolve resource: \n "
				+ "type=" + type + ", namespaceURI=" + namespaceURI + ",publicId=" + publicId + ",systemId=" + systemId + ",baseURI=" + baseURI + "\n"
				+ "PATHS FAILED: \n" 
				+ failPathsReadable + "\n"
				+ "PATHS FOUND: \n"
				+ foundPathsReadable);	
	}
	
}


