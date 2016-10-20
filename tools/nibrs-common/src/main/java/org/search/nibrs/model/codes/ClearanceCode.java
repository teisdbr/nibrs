package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum ClearanceCode {

	Y("Y","Yes (Clears the Case"), 
	N("N", "No (Already Cleared)");
	
	private String code;
	
	private String description;
	
	private ClearanceCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}

	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(ClearanceCode clearanceCode : values()){
			
			String iCode = clearanceCode.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;
	}	
}
