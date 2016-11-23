package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code list enum for Disposition of Arrestee Under 18 (data element 52)
 */
public enum DispositionOfArresteeUnder18Code {
	
	H("H","Handled Within Department"), 
	R("R","Referred to Other Authorities");
	
	private DispositionOfArresteeUnder18Code(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;

	public static Set<String> codeSet() {
		Set<String> codeSet = new HashSet<String>();
		for (DispositionOfArresteeUnder18Code code : values()) {
			codeSet.add(code.code);
		}
		return codeSet;
	}

}
