package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for residence status
 */
public enum ResidentStatusCode {

	N("N","Nonresident"),
	R("R","Resident"),
	U("U","Unknown");

	private ResidentStatusCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String code;
	public String description;

	public static Set<String> codeSet() {
		Set<String> codeSet = new HashSet<String>();
		for (ResidentStatusCode codeValue : values()) {
			codeSet.add(codeValue.code);
		}
		return codeSet;
	}
	
}
