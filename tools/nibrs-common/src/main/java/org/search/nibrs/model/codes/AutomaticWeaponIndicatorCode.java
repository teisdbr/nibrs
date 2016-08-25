package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for automatic weapon indicator (data element 13)
 */
public enum AutomaticWeaponIndicatorCode {
	
	A("A", "Automatic"),
	_blank(" ", " ");
		
	private AutomaticWeaponIndicatorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<AutomaticWeaponIndicatorCode> asSet() {
		return EnumSet.allOf(AutomaticWeaponIndicatorCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (AutomaticWeaponIndicatorCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
