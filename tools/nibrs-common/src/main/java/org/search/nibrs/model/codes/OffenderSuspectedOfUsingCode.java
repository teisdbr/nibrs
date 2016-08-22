package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for suspected-of-using (data element 8)
 */
public enum OffenderSuspectedOfUsingCode {
	
	A("A", "Alcohol"),
	C("C", "Computer Equipment"),
	D("D", "Drugs/Narcotics"),
	N("N", "Not Applicable");
	
	private OffenderSuspectedOfUsingCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<OffenderSuspectedOfUsingCode> asSet() {
		return EnumSet.allOf(OffenderSuspectedOfUsingCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (OffenderSuspectedOfUsingCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
