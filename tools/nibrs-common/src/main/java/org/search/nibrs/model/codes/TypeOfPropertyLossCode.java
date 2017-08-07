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
	_1("1", "None"),
	/**
	 * Burned (includes damage caused in fighting the fire)
	 */
	_2("2", "Burned (includes damage caused in fighting the fire)"),
	/**
	 * Counterfeited/Forged
	 */
	_3("3", "Counterfeited/Forged"),
	/**
	 * Destroyed/Damaged/Vandalized
	 */
	_4("4", "Destroyed/Damaged/Vandalized"),
	/**
	 * Recovered (to impound property that was previously stolen)
	 */
	_5("5", "Recovered (to impound property that was previously stolen)"),
	/**
	 * Seized (to impound property that was not previously stolen)
	 */
	_6("6", "Seized (to impound property that was not previously stolen)"),
	/**
	 * Stolen/Etc
	 */
	_7("7", "Stolen/Etc. (includes bribed, defrauded, embezzled, extorted, ransomed, robbed, etc.)"),
	/**
	 * Unknown
	 */
	_8("8", "Unknown");
		
	private TypeOfPropertyLossCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
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
	
}
