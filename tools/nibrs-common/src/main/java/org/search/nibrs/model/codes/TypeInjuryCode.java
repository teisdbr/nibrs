package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum TypeInjuryCode {

	N("N","None"),
	B("B","Apparent Broken Bones"),
	I("I","Possible Internal Injury"),
	L("L","Severe Laceration"),
	M("M","Apparent Minor Injury"),
	O("O","Other Major Injury"),
	T("T","Loss of Teeth"),
	U("U","Unconsciousness");
		
	private TypeInjuryCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	public String code;
	
	public String description;
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(TypeInjuryCode iCircsCode : values()){
			
			String sCode = iCircsCode.code;
			
			rCodeSet.add(sCode);
		}
		
		return rCodeSet;
	}	
	
}
