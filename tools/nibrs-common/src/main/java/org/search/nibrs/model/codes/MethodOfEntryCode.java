package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for method of entry (data element 11)
 */
public enum MethodOfEntryCode {
	
	F("F", "Force"),
	N("N", "No Force");
		
	private MethodOfEntryCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<MethodOfEntryCode> asSet() {
		return EnumSet.allOf(MethodOfEntryCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (MethodOfEntryCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}


}
