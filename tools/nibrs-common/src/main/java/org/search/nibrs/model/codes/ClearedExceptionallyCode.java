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
 * Code enum for exceptional clearance (data element 4)
 */
public enum ClearedExceptionallyCode {
	
	A("A", "Death of Offender"),
	B("B", "Prosecution Declined (by the prosecutor for other than lack of probable cause)"),
	C("C", "In Custody of Other Jurisdiction"),
	D("D", "Victim Refused to Cooperate (in the prosecution)"),
	E("E", "Juvenile/No Custody (the handling of a juvenile without taking him/her into custody, but rather by oral or written notice given to the parents or legal guardian in a case involving a minor offense, such as petty larceny)."),
	N("N", "Not Applicable (not cleared exceptionally).");
	
	public String code;
	public String description;
	
	private ClearedExceptionallyCode(String code, String description){
		this.code = code;
		this.description = description;
	}
	
	public static final Set<ClearedExceptionallyCode> asSet() {
		return EnumSet.allOf(ClearedExceptionallyCode.class);
	}
	
	public static final Set<String> applicableCodeSet(){
		Set<String> ret = codeSet();
		ret.remove(ClearedExceptionallyCode.N.code);
		return ret;
	}
	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (ClearedExceptionallyCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
