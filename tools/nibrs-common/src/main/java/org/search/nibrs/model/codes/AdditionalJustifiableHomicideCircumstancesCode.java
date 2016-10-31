package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum AdditionalJustifiableHomicideCircumstancesCode {
	
	A("A","Criminal Attacked Police Officer and That Officer Killed Criminal"),
	B("B","Criminal Attacked Police Officer and Criminal Killed by Another Police Officer"),
	C("C","Criminal Attacked a Civilian"),
	D("D","Criminal Attempted Flight From a Crime"),
	E("E","Criminal Killed in Commission of a Crime"),
	F("F","Criminal Resisted Arrest"),
	G("G","Unable to Determine/Not Enough Information");
	
	private AdditionalJustifiableHomicideCircumstancesCode(String code, String description){

		this.code = code;
		
		this.description = description;
	}
	
	public String code;
	
	public String description;	
	
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(AdditionalJustifiableHomicideCircumstancesCode iCircsCode : values()){
			
			String sCode = iCircsCode.code;
			
			rCodeSet.add(sCode);
		}
		
		return rCodeSet;
	}
	
}
