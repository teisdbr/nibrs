/*******************************************************************************
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.model.codes;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for property description (data element 15)
 */
public enum PropertyDescriptionCode {
	
	_01("01", "Aircraft"),
	_02("02", "Alcohol"),
	_03("03", "Automobiles"),
	_04("04", "Bicycles"),
	_05("05", "Buses"),
	_06("06", "Clothes/Furs"),
	_07("07", "Computer Hardware/Software"),
	_08("08", "Consumable Goods"),
	_09("09", "Credit/Debit Cards"),
	_10("10", "Drugs/Narcotics"),
	_11("11", "Drug/Narcotic Equipment"),
	_12("12", "Farm Equipment"),
	_13("13", "Firearms"),
	_14("14", "Gambling Equipment"),
	_15("15", "Heavy Construction/Industrial Equipment"),
	_16("16", "Household Goods"),
	_17("17", "Jewelry/Precious Metals/Gems"),
	_18("18", "Livestock"),
	_19("19", "Merchandise"),
	_20("20", "Money"),
	_21("21", "Negotiable Instruments"),
	_22("22", "Nonnegotiable Instruments"),
	_23("23", "Office-type Equipment"),
	_24("24", "Other Motor Vehicles"),
	_25("25", "Purses/Handbags/Wallets"),
	_26("26", "Radios/TVs/VCRs/DVD Players"),
	_27("27", "Recordings–Audio/Visual"),
	_28("28", "Recreational Vehicles"),
	_29("29", "Structures–Single Occupancy Dwellings"),
	_30("30", "Structures–Other Dwellings"),
	_31("31", "Structures–Other Commercial/Business"),
	_32("32", "Structures–Industrial/Manufacturing"),
	_33("33", "Structures–Public/Community"),
	_34("34", "Structures–Storage"),
	_35("35", "Structures–Other"),
	_36("36", "Tools"),
	_37("37", "Trucks"),
	_38("38", "Vehicle Parts/Accessories"),
	_39("39", "Watercraft"),
	_41("41", "Aircraft Parts/Accessories"),
	_42("42", "Artistic Supplies/Accessories"),
	_43("43", "Building Materials"),
	_44("44", "Camping/Hunting/Fishing Equipment/Supplies"),
	_45("45", "Chemicals"),
	_46("46", "Collections/Collectibles"),
	_47("47", "Crops"),
	_48("48", "Documents/Personal or Business"),
	_49("49", "Explosives"),
	_59("59", "Firearm Accessories"),
	_64("64", "Fuel"),
	_65("65", "Identity Documents"),
	_66("66", "Identity–Intangible"),
	_67("67", "Law Enforcement Equipment"),
	_68("68", "Lawn/Yard/Garden Equipment"),
	_69("69", "Logging Equipment"),
	_70("70", "Medical/Medical Lab Equipment"),
	_71("71", "Metals, Non-Precious"),
	_72("72", "Musical Instruments"),
	_73("73", "Pets"),
	_74("74", "Photographic/Optical Equipment"),
	_75("75", "Portable Electronic Communications"),
	_76("76", "Recreational/Sports Equipment"),
	_77("77", "Other"),
	_78("78", "Trailers"),
	_79("79", "Watercraft Equipment/Parts/Accessories"),
	_80("80", "Weapons–Other"),
	_88("88", "Pending Inventory"),
	_99("99", "(blank)–this data value is not currently used by the FBI");
	
	private PropertyDescriptionCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<PropertyDescriptionCode> asSet() {
		return EnumSet.allOf(PropertyDescriptionCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (PropertyDescriptionCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}
	
	public static final boolean isMotorVehicleCode(String code) {
		return _03.code.equals(code) || _05.code.equals(code);
	}
	
	public static final boolean containsMotorVehicleCode(Collection<String> codes) {
		return codes.stream().anyMatch(code -> isMotorVehicleCode(code));
	}
	
}
