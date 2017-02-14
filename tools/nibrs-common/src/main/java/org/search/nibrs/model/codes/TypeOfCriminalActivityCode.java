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

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for type of criminal activity / gang info (data element 12)
 */
public enum TypeOfCriminalActivityCode {
	
	A("A", "Simple/Gross Neglect (unintentionally, intentionally, or knowingly failing to provide food, water, shelter, veterinary care, hoarding, etc.)"),
	B("B", "Buying/Receiving"),
	C("C", "Cultivating/Manufacturing/Publishing (i.e., production of any type)"),
	D("D", "Distributing/Selling"),
	F("F", "Organized Abuse (Dog Fighting and Cock Fighting)"),
	E("E", "Exploiting Children"),
	I("I", "Intentional Abuse and Torture (tormenting, mutilating, maiming, poisoning, or abandonment)"),
	O("O", "Operating/Promoting/Assisting"),
	P("P", "Possessing/Concealing"),
	S("S", "Animal Sexual Abuse (Bestiality)"),
	T("T", "Transporting/Transmitting/Importing"),
	U("U", "Using/Consuming"),
	J("J", "Juvenile Gang"),
	G("G", "Other Gang"),
	N("N", "None/Unknown");
		
	private TypeOfCriminalActivityCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<TypeOfCriminalActivityCode> asSet() {
		return EnumSet.allOf(TypeOfCriminalActivityCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (TypeOfCriminalActivityCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}
	
	public static final Set<String> noneOrUnknownValueCodeSet() {
		return Collections.singleton(N.code);
	}

}
