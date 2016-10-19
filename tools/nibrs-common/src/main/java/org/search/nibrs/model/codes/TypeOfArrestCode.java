package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum TypeOfArrestCode {
	
	O("O","On-View Arrest"),
	S("S","Summoned/Cited"),
	T("T","Taken Into Custody");	
	
	private TypeOfArrestCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
		
	private String code;
	
	private String description;
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(TypeOfArrestCode iArrestCode : values()){
			
			String sArrestCode = iArrestCode.code;
			
			rCodeSet.add(sArrestCode);
		}
		
		return rCodeSet;
	}	

}
