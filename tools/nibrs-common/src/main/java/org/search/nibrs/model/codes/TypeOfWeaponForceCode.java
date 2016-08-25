package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for type of weapon/force (data element 13)
 */
public enum TypeOfWeaponForceCode {
	
	_11("11", "Firearm"),
	_12("12", "Handgun"),
	_13("13", "Rifle"),
	_14("14", "Shotgun"),
	_15("15", "Other Firearm"),
	_20("20", "Knife/Cutting Instrument"),
	_30("30", "Blunt Object"),
	_35("35", "Motor Vehicle"),
	_40("40", "Personal Weapons"),
	_50("50", "Poison"),
	_60("60", "Explosives"),
	_65("65", "Fire/Incendiary Device"),
	_70("70", "Drugs/Narcotics/Sleeping Pills"),
	_85("85", "Asphyxiation"),
	_90("90", "Other"),
	_95("95", "Unknown"),
	_99("99", "None");
		
	private TypeOfWeaponForceCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<TypeOfWeaponForceCode> asSet() {
		return EnumSet.allOf(TypeOfWeaponForceCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (TypeOfWeaponForceCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}



}
