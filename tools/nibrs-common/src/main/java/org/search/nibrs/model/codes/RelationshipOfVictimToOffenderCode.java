package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum RelationshipOfVictimToOffenderCode {

	SE("SE","Victim Was Spouse"),
	CS("CS","Victim Was Common-Law Spouse"),
	PA("PA","Victim Was Parent"),
	SB("SB","Victim Was Sibling"),
	CH("CH","Victim Was Child"),
	GP("GP","Victim Was Grandparent "),
	GC("GC","Victim Was Grandchild "),
	IL("IL","Victim Was In-law"),
	SP("SP","Victim Was Step-parent "),
	SC("SC","Victim Was Step-child"),
	SS("SS","Victim Was Step-sibling "),
	OF("OF","Victim Was Other Family Member "),
	AQ("AQ","Victim Was Acquaintance "),
	FR("FR","Victim Was Friend"),
	NE("NE","Victim Was Neighbor"),
	BE("BE","Victim Was Babysitter"),
	BG("BG","Victim Was Boyfriend/Girlfriend "),
	CF("CF","Victim Was Child of Boyfriend/Girlfriend "),
	HR("HR","Homosexual Relationship"),
	XS("XS","Victim Was Ex-Spouse"),
	EE("EE","Victim Was Employee"),
	ER("ER","Victim Was Employer"),
	OK("OK","Victim Was Otherwise Known "),
	RU("RU","Relationship Unknown"),
	ST("ST","Victim Was Stranger"),
	VO("VO","Victim Was Offender");
	
	private RelationshipOfVictimToOffenderCode(String code, String description){
		
		this.code = code;
				
		this.description = description;
	}
	
	private String code;
	
	private String description;
		
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(RelationshipOfVictimToOffenderCode relationshipCode : values()){
			
			String sCode = relationshipCode.code;
			
			rCodeSet.add(sCode);
		}		
		return rCodeSet;
	}		

}

