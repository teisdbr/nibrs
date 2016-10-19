package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum RaceOfOffender {
	
	W("W","White"),
	B("B","Black or African American"),
	I("I","American Indian or Alaska Native"),
	A("A","Asian"),
	P("P","Native Hawaiian or Other Pacific Islander"),
	U("U","Unknown");
	
	private String code;
	
	private String description;
	
	private RaceOfOffender(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	public static final Set<String> codeSet() {
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(RaceOfOffender v : values()) {
			
			rCodeSet.add(v.code);
		}		
		return rCodeSet;
	}		

}
