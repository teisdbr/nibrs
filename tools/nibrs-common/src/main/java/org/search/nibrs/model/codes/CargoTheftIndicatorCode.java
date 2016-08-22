package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum CargoTheftIndicatorCode {
	
	Y("Y" , "Yes"),
	N("N" , "No");
	
	public String code;
	public String description;
	
	private CargoTheftIndicatorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static final Set<CargoTheftIndicatorCode> asSet() {
		return EnumSet.allOf(CargoTheftIndicatorCode.class);
	}
	
	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (CargoTheftIndicatorCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
