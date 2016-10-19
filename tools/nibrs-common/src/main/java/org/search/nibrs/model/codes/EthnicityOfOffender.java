package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;


public enum EthnicityOfOffender {

	H("H","Hispanic or Latino"),
	N("N","Not Hispanic or Latino"),
	U("U","Unknown");
	
	private EthnicityOfOffender(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(EthnicityOfOffender ethnicityOfOffender : values()){
			
			String iCode = ethnicityOfOffender.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;
	}
}
