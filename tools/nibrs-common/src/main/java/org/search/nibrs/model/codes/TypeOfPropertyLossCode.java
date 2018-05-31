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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for type of property loss (data element 14)
 */
public enum TypeOfPropertyLossCode {
	
	/**
	 * None
	 */
	_1("1", "None", "NONE"),
	/**
	 * Burned (includes damage caused in fighting the fire)
	 */
	_2("2", "Burned (includes damage caused in fighting the fire)", "BURNED"),
	/**
	 * Counterfeited/Forged
	 */
	_3("3", "Counterfeited/Forged", "COUNTERFEITED"),
	/**
	 * Destroyed/Damaged/Vandalized
	 */
	_4("4", "Destroyed/Damaged/Vandalized", "DESTROYED_DAMAGED_VANDALIZED"),
	/**
	 * Recovered (to impound property that was previously stolen)
	 */
	_5("5", "Recovered (to impound property that was previously stolen)", "RECOVERED"),
	/**
	 * Seized (to impound property that was not previously stolen)
	 */
	_6("6", "Seized (to impound property that was not previously stolen)", "SEIZED"),
	/**
	 * Stolen/Etc
	 */
	_7("7", "Stolen/Etc. (includes bribed, defrauded, embezzled, extorted, ransomed, robbed, etc.)", "STOLEN"),
	/**
	 * Unknown
	 */
	_8("8", "Unknown", "UNKNOWN");
		
	private TypeOfPropertyLossCode(String code, String description, String iepdCode) {
		this.code = code;
		this.description = description;
		this.setIepdCode(iepdCode);
	}
	
	public String code;
	public String description;
	private String iepdCode; 
	
	public static final Set<TypeOfPropertyLossCode> asSet() {
		return EnumSet.allOf(TypeOfPropertyLossCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (TypeOfPropertyLossCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

	public static final Set<String> noneOrUnknownValueCodeSet() {
		Set<String> ret = new HashSet<>();
		ret.add(_1.code);
		ret.add(_8.code);
		return ret;
	}

	public static final Set<String> requirePropertyDescriptionValueCodeSet() {
		Set<String> ret = new HashSet<>();
		ret.add(_2.code);
		ret.add(_3.code);
		ret.add(_4.code);
		ret.add(_5.code);
		ret.add(_6.code);
		ret.add(_7.code);
		return ret;
	}

	public String getIepdCode() {
		return iepdCode;
	}

	public void setIepdCode(String iepdCode) {
		this.iepdCode = iepdCode;
	}
	
	public static final TypeOfPropertyLossCode valueOfIepdCode(String iepdCode){
		return	Arrays.stream(TypeOfPropertyLossCode.values()).filter(i->i.iepdCode.equals(iepdCode)).findFirst().orElse(null); 
	}

	public static final TypeOfPropertyLossCode valueOfCode(String code){
		return	Arrays.stream(TypeOfPropertyLossCode.values()).filter(i->i.code.equals(code)).findFirst().orElse(null); 
	}
	
	
}
