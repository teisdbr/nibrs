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
package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;


public enum EthnicityOfOffender {

	H("H","Hispanic or Latino"),
	N("N","Not Hispanic or Latino"),
	U("U","Unknown");
	
	private EthnicityOfOffender(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(EthnicityOfOffender ethnicityOfOffender : values()){
			
			String iCode = ethnicityOfOffender.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;
	}
}
