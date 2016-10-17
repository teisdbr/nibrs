package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum TypeOfVictimCode {
	
	B("B","Business"),
	F("F","Financial Institution"),
	G("G","Government"),
	I("I","Individual"),
	L("L","Law Enforcement Officer"),
	O("O","Other"),
	R("R","Religious Organization"),
	S("S","Society/Public"),
	U("U","Unknown");

	private String code;
	
	private String description;
	
	private TypeOfVictimCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(TypeOfVictimCode iVictimCodeType : values()){
			
			rCodeSet.add(iVictimCodeType.code);
		}
		return rCodeSet;
	}
}
