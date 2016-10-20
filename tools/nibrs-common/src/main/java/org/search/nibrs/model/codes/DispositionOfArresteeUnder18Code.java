package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum DispositionOfArresteeUnder18Code {
	
	H("H","Handled Within Department"), 
	R("R","Referred to Other Authorities");
	
	private DispositionOfArresteeUnder18Code(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}	
	
	private String code;
	
	private String description;
	
	
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<String>();
		
		for(DispositionOfArresteeUnder18Code iDisposition : values()){
			
			String sCode = iDisposition.code;
			
			rCodeSet.add(sCode);
		}
		
		return rCodeSet;
	}	

}
