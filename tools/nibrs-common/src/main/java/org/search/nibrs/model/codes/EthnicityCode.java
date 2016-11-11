package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for ethnicity
 */
public enum EthnicityCode {

	H("H","Hispanic or Latino"),
	N("N","Not Hispanic or Latino"),
	U("U","Unknown");

	private EthnicityCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String code;
	public String description;

	public static Set<String> codeSet() {
		Set<String> codeSet = new HashSet<String>();
		for (EthnicityCode codeValue : values()) {
			codeSet.add(codeValue.code);
		}
		return codeSet;
	}
}
