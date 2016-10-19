package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;


public enum EthnicityOfArrestee {

	H("H","Hispanic or Latino"),
	N("N","Not Hispanic or Latino"),
	U("U","Unknown");
	
	private EthnicityOfArrestee(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(EthnicityOfArrestee ethnicityOfVictim : values()){
			
			String iCode = ethnicityOfVictim.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;
	}
}
