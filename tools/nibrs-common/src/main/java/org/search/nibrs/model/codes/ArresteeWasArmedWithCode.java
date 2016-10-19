package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

public enum ArresteeWasArmedWithCode {
	
	_01("01","Unarmed"),
	_11("11","Firearm (type not stated)"),
	_12("12","Handgun"),
	_13("13","Rifle"),
	_14("14","Shotgun"),
	_15("15","Other Firearm"),
	_16("16","Lethal Cutting Instrument"),
	_17("17","Club/Blackjack/Brass Knuckles");
	
	private ArresteeWasArmedWithCode(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;

	
	public static final Set<String> codeSet() {
		
		Set<String> rCodeSet = new HashSet<>();
		
		for (ArresteeWasArmedWithCode iArmedWithCode : values()) {
			
			rCodeSet.add(iArmedWithCode.code);
		}
		return rCodeSet;
	}
}
