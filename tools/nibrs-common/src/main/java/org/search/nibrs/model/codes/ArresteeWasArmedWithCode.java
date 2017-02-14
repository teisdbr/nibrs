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
import java.util.List;
import java.util.Set;

/**
 * Code enum for Arrestee Was Armed With (data element 46)
 */
public enum ArresteeWasArmedWithCode {
	
	_01("01","Unarmed"),
	_11("11","Firearm (type not stated)"),
	_12("12","Handgun"),
	_13("13","Rifle"),
	_14("14","Shotgun"),
	_15("15","Other Firearm"),
	_16("16","Lethal Cutting Instrument"),
	_17("17","Club/Blackjack/Brass Knuckles");
	
	private ArresteeWasArmedWithCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	private static final List<ArresteeWasArmedWithCode> FIREARMS = Arrays.asList(new ArresteeWasArmedWithCode[] {_11, _12, _13, _14, _15});
	
	public static final Set<String> codeSet() {
		Set<String> codeSet = new HashSet<>();
		for (ArresteeWasArmedWithCode code : values()) {
			codeSet.add(code.code);
		}
		return codeSet;
	}

	public static final Set<ArresteeWasArmedWithCode> asSet() {
		return EnumSet.allOf(ArresteeWasArmedWithCode.class);
	}
	
	public static final ArresteeWasArmedWithCode forCode(String code) {
		ArresteeWasArmedWithCode ret = null;
		for(ArresteeWasArmedWithCode c : asSet()) {
			if (c.code.equals(code)) {
				ret = c;
				break;
			}
		}
		return ret;
	}

	public boolean isFirearm() {
		return FIREARMS.contains(this);
	}
	
}
