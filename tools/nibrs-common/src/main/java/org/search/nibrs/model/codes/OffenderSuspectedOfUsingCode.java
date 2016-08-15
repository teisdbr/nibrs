package org.search.nibrs.model.codes;

public enum OffenderSuspectedOfUsingCode {
	
	A("A", "Alcohol"),
	C("C", "Computer Equipment"),
	D("D", "Drugs/Narcotics"),
	N("N", "Not Applicable");
	
	private OffenderSuspectedOfUsingCode(String code, String description) {

		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;
}
