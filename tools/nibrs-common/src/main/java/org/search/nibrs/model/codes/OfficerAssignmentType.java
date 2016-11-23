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

public enum OfficerAssignmentType {
	
	F("F","Two-Officer Vehicle"),
	G("G","One-Officer Vehicle (Alone)"),
	H("H","One-Officer Vehicle (Assisted)"),
	I("I","Detective or Special Assignment (Alone)"),
	J("J","Detective or Special Assignment (Assisted)"),
	K("K","Other (Alone)"),
	L("L","Other (Assisted)");

	public String code;
	
	public String description;	
	
	private OfficerAssignmentType(String code, String description) {
	
		this.code = code;
		
		this.description = description;
	}
		
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(OfficerAssignmentType officerAssignmentType : values()){
			
			String iCode = officerAssignmentType.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;		
	}	

}
