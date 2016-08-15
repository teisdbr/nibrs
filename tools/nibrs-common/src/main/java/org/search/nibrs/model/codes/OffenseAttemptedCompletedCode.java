package org.search.nibrs.model.codes;

public enum OffenseAttemptedCompletedCode {
	
	A("A", "Attempted"),
	C("C", "Completed");
		
	private OffenseAttemptedCompletedCode(String code, String description) {
	
		this.code = code;
		this.description = description;
	}
	
	private String code;
	
	private String description;
	
}
