package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum AggravatedAssaultHomicideCircumstancesCode {
	
	_13A("13A","Aggravated Assault"),
	_09A("09A","Murder and Non-negligent Manslaughter (enter up to two)"),
	_01("01","Argument"),
	_02("02","Assault on Law Enforcement Officer"),
	_03("03","Drug Dealing"),
	_04("04","Gangland (Organized Crime Involvement)"),
	_05("05","Juvenile Gang"),
	_06("06","Lovers’ Quarrel"),
	_07("07","Mercy Killing (Not applicable to Aggravated Assault)"),
	_08("08","Other Felony Involved"),
	_09("09","Other Circumstances"),
	_10("10","Unknown Circumstances"),
	_09B("09B","Negligent Manslaughter (enter only one)"),
	_30("30","Child Playing With Weapon"),
	_31("31","Gun-Cleaning Accident"),
	_32("32","Hunting Accident"),
	_33("33","Other Negligent Weapon Handling"),
	_34("34","Other Negligent Killing"),
	_09C("09C","Justifiable Homicide (enter only one)"),
	_20("20","Criminal Killed by Private Citizen"),
	_21("21","Criminal Killed by Police Officer");
			
	public String code;
	
	public String description;
	
	private AggravatedAssaultHomicideCircumstancesCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}

	public static Set<String> codeSet(){
	
		Set<String> rCodeSet = new HashSet<String>();
		
		for(AggravatedAssaultHomicideCircumstancesCode assaultCode : values()){
			
			rCodeSet.add(assaultCode.code);
		}
		return rCodeSet;
	}
	
}
