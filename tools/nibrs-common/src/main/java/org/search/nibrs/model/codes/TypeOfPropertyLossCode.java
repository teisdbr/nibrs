package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for type of property loss (data element 14)
 */
public enum TypeOfPropertyLossCode {
	
	_1("1", "None"),
	_2("2", "Burned (includes damage caused in fighting the fire)"),
	_3("3", "Counterfeited/Forged"),
	_4("4", "Destroyed/Damaged/Vandalized"),
	_5("5", "Recovered (to impound property that was previously stolen)"),
	_6("6", "Seized (to impound property that was not previously stolen)"),
	_7("7", "Stolen/Etc. (includes bribed, defrauded, embezzled, extorted, ransomed, robbed, etc.)"),
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

}
