package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum ResidentStatusCode {

	N("N","Nonresident"),
	R("R","Resident"),
	U("U","Unknown");
	
	private ResidentStatusCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(ResidentStatusCode iResidentStatusCode : values()){
			
			rCodeSet.add(iResidentStatusCode.code);
		}
		return rCodeSet;
	}
}
