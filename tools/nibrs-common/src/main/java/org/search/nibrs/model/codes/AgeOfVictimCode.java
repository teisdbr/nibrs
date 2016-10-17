package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum AgeOfVictimCode {

	NN("NN","Under 24 Hours"),
	NB("NB","1–6 Days Old"),
	BB("BB","7–364 Days Old"),
	
	// just a valid range(not a code)
	//_0198("01–98","Years Old"),
	
	_99("99","Over 98 Years Old"),
	_00("00","Unknown");
	
	
	private AgeOfVictimCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;
	
	
	public Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(AgeOfVictimCode iAgeOfVictimCode : values()){
			
			String iCode = iAgeOfVictimCode.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;
	}
}
