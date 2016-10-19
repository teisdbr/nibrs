package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;


public enum MultipleArresteeSegmentsIndicator {
	
	C("C","Count Arrestee"),
	M("M","Multiple"),
	N("N","Not Applicable");
				
	private String code;
	
	private String description;
	
	private MultipleArresteeSegmentsIndicator(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}		
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(MultipleArresteeSegmentsIndicator arrestCode : values()){
			
			String sCode = arrestCode.code;
			
			rCodeSet.add(sCode);
		}
		
		return rCodeSet;
	}	
}
