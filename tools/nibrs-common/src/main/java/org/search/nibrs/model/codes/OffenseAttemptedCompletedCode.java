package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for offense attempted/completed code (data element 7)
 */
public enum OffenseAttemptedCompletedCode {
	
	A("A", "Attempted"),
	C("C", "Completed");
		
	private OffenseAttemptedCompletedCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<OffenseAttemptedCompletedCode> asSet() {
		return EnumSet.allOf(OffenseAttemptedCompletedCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (OffenseAttemptedCompletedCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
