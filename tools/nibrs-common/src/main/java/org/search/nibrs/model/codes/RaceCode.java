package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for Race
 */
public enum RaceCode {
	
	W("W","White"),
	B("B","Black or African American"),
	I("I","American Indian or Alaska Native"),
	A("A","Asian"),
	P("P","Native Hawaiian or Other Pacific Islander"),
	U("U","Unknown");
	
	private RaceCode(String code, String description){
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;

	public static Set<String> codeSet() {
		Set<String> codeSet = new HashSet<String>();
		for (RaceCode codeValue : values()) {
			codeSet.add(codeValue.code);
		}
		return codeSet;
	}

}
