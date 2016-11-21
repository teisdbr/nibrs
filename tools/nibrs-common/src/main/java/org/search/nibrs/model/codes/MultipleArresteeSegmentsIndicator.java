package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for the Multiple Arrestee Segments Indicator (data element 44)
 */
public enum MultipleArresteeSegmentsIndicator {

	C("C", "Count Arrestee"),
	M("M", "Multiple"),
	N("N", "Not Applicable");

	public String code;
	public String description;

	private MultipleArresteeSegmentsIndicator(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static Set<String> codeSet() {
		Set<String> codeSet = new HashSet<>();
		for (MultipleArresteeSegmentsIndicator arrestCode : values()) {
			codeSet.add(arrestCode.code);
		}
		return codeSet;
	}

}
