package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum RaceOfVictimCode {
	
	W("W","White"),
	B("B","Black or African American"),
	I("I","American Indian or Alaska Native"),
	A("A","Asian"),
	P("P","Native Hawaiian or Other Pacific Islander"),
	U("U","Unknown");
	
	private RaceOfVictimCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;

	
	public static Set<String> codeSet(){		
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(RaceOfVictimCode victimCode : values()){
			
			String iCode = victimCode.code;
			
			rCodeSet.add(iCode);
		}				
		return rCodeSet;
	}
	
}
