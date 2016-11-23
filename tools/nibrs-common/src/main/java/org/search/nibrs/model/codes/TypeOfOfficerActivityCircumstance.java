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

public enum TypeOfOfficerActivityCircumstance {

	_01("01","Responding to Disturbance Call(Family Quarrels, Person with Firearm, Etc.)"),
	_02("02","Burglaries in Progress or Pursuing Burglary Suspects"),
	_03("03","Robberies in Progress or Pursuing Robbery Suspects"),
	_04("04","Attempting Other Arrests"),
	_05("05","Civil Disorder (Riot, Mass Disobedience)"),
	_06("06","Handling, Transporting, Custody of Prisoners"),
	_07("07","Investigating Suspicious Persons or Circumstances"),
	_08("08","Ambush–No Warning"),
	_09("09","Handling Persons with Mental Illness"),
	_10("10","Traffic Pursuits and Stops"),
	_11("11","All Other");
	
	public String code;
	
	public String description;
	
	private TypeOfOfficerActivityCircumstance(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}

	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(TypeOfOfficerActivityCircumstance iTypeOfOfficerActivityCircumstance : values()){
			
			String iCode = iTypeOfOfficerActivityCircumstance.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;		
	}
}
