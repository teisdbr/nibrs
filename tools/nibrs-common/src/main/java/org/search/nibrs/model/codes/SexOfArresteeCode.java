package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum SexOfArresteeCode {
	
	M("M","Male"),
	F("F","Female");	
	
	private String code;
	
	private String description;
	
	private SexOfArresteeCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	
	public static final Set<String> codeSet() {
		
		Set<String> rCodeSet = new HashSet<>();
		
		for (SexOfArresteeCode v : values()) {
			
			rCodeSet.add(v.code);
		}
		
		return rCodeSet;
	}
}
