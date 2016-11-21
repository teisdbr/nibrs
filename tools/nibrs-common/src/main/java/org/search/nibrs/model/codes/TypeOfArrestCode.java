package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for Type of Arrest (data element 43)
 */
public enum TypeOfArrestCode {

	O("O", "On-View Arrest"),
	S("S", "Summoned/Cited"),
	T("T", "Taken Into Custody");

	private TypeOfArrestCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String code;
	public String description;

	public static Set<String> codeSet() {
		Set<String> rCodeSet = new HashSet<>();
		for (TypeOfArrestCode iArrestCode : values()) {
			String sArrestCode = iArrestCode.code;
			rCodeSet.add(sArrestCode);
		}
		return rCodeSet;
	}

}
