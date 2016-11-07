package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum SexOfOffenderCode {
	
	F("F","Female"),
	M("M","Male"),
	U("U","Unknown");	
	
	private SexOfOffenderCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	public String code;
	
	private String description;
		
	public static final Set<String> codeSet() {
		
		Set<String> rCodeSet = new HashSet<>();
		
		for (SexOfOffenderCode v : values()) {
			
			rCodeSet.add(v.code);
		}
		
		return rCodeSet;
	}	

}
