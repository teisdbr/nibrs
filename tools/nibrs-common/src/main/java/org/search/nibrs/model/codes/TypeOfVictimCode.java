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
package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum TypeOfVictimCode {
	
	B("B","Business"),
	F("F","Financial Institution"),
	G("G","Government"),
	I("I","Individual"),
	L("L","Law Enforcement Officer"),
	O("O","Other"),
	R("R","Religious Organization"),
	S("S","Society/Public"),
	U("U","Unknown");

	public String code;
	
	public String description;
	
	private TypeOfVictimCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(TypeOfVictimCode iVictimCodeType : values()){
			
			rCodeSet.add(iVictimCodeType.code);
		}
		return rCodeSet;
	}
}
