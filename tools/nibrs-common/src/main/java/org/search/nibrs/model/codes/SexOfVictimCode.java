package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum SexOfVictimCode {
	
	M("M","Male"),
	F("F","Female"),	
	U("U","Unknown");
	
	private SexOfVictimCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;

	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(SexOfVictimCode iSexOfVictimCode : values()){
			
			String code = iSexOfVictimCode.code;
			
			rCodeSet.add(code);
		}
		
		return rCodeSet;
	}
}
