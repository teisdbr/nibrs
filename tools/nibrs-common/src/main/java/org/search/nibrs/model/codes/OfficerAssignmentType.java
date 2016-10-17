package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum OfficerAssignmentType {
	
	F("F","Two-Officer Vehicle"),
	G("G","One-Officer Vehicle (Alone)"),
	H("H","One-Officer Vehicle (Assisted)"),
	I("I","Detective or Special Assignment (Alone)"),
	J("J","Detective or Special Assignment (Assisted)"),
	K("K","Other (Alone)"),
	L("L","Other (Assisted)");

	private String code;
	
	private String description;	
	
	private OfficerAssignmentType(String code, String description) {
	
		this.code = code;
		
		this.description = description;
	}
		
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(OfficerAssignmentType officerAssignmentType : values()){
			
			String iCode = officerAssignmentType.code;
			
			rCodeSet.add(iCode);
		}
		
		return rCodeSet;		
	}	

}
