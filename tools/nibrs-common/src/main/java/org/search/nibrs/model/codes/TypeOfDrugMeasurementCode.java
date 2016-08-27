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
