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

public enum SuspectedDrugTypeCode {
	
	_A("A", "Crack Cocaine"),
	_B("B", "Cocaine (All forms except Crack)"),
	_C("C", "Hashish"),
	_D("D", "Heroin"),
	_E("E", "Marijuana"),
	_F("F", "Morphine"),
	_G("G", "Opium"),
	_H("H", "Other Narcotics"),
	_I("I", "LSD"),
	_J("J", "PCP"),
	_K("K", "Other Hallucinogens"),
	_L("L", "Amphetamines/Methamphetamines"),
	_M("M", "Other Stimulants"),
	_N("N", "Barbiturates"),
	_O("O", "Other Depressants"),
	_P("P", "Other Drugs"),
	_U("U", "Unknown Drug Type"),
	_X("X", "Over 3 Drug Types");
	
	private SuspectedDrugTypeCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<SuspectedDrugTypeCode> asSet() {
		return EnumSet.allOf(SuspectedDrugTypeCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (SuspectedDrugTypeCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
