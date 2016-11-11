package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for sex.
 */
public enum SexCode {
	
	M("M","Male"),
	F("F","Female"),	
	U("U","Unknown");
	
	private SexCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;

	public static Set<String> codeSet() {
		Set<String> codeSet = new HashSet<String>();
		for(SexCode codeValue : values()){
			codeSet.add(codeValue.code);
		}
		return codeSet;
	}
	
}
