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

public enum TypeOfDrugMeasurementCode {
	
	_DU("DU", "Dosage Units/Items"),
	_FO("FO", "Fluid Ounce"),
	_GL("GL", "Gallon"),
	_GM("GM", "Gram"),
	_KG("KG", "Kilogram"),
	_LB("LB", "Pound"),
	_LT("LT", "Liter"),
	_ML("ML", "Milliliter"),
	_NP("NP", "Number of Plants"),
	_OZ("OZ", "Ounce"),
	_XX("XX", "Not Reported");
	
	private TypeOfDrugMeasurementCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<TypeOfDrugMeasurementCode> asSet() {
		return EnumSet.allOf(TypeOfDrugMeasurementCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (TypeOfDrugMeasurementCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
