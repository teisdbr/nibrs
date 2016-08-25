package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Enum for NIBRS OffenseSegment Codes.
 *
 */
public enum OffenseCode {
	
	_720("720", "Animal Cruelty", "A"),
	_200("200", "Arson", "A"),
	_13A("13A", "Aggravated Assault", "A"),
	_13B("13B", "Simple Assault", "A"),
	_13C("13C", "Intimidation", "A"),
	_510("510", "Bribery", "A"),
	_220("220", "Burglary/Breaking & Entering", "A"),
	_250("250", "Counterfeiting/Forgery", "A"),
	_290("290", "Destruction/Damage/Vandalism of PropertySegment", "A"),
	_35A("35A", "Drug/Narcotic Violations", "A"),
	_35B("35B", "Drug Equipment Violations", "A"),
	_270("270", "Embezzlement", "A"),
	_210("210", "Extortion/Blackmail", "A"),
	_26A("26A", "False Pretenses/Swindle/Confidence Game", "A"),
	_26B("26B", "Credit Card/Automated Teller Machine Fraud", "A"),
	_26C("26C", "Impersonation", "A"),
	_26D("26D", "Welfare Fraud", "A"),
	_26E("26E", "Wire Fraud", "A"),
	_26F("26F", "Identity Theft", "A"),
	_26G("26G", "Hacking/Computer Invasion", "A"),
	_39A("39A", "Betting/W agering", "A"),
	_39B("39B", "Operating/Promoting/Assisting Society Gambling", "A"),
	_39C("39C", "Gambling Equipment Violations", "A"),
	_39D("39D", "Sports Tampering", "A"),
	_09A("09A", "Murder & Non-negligent Manslaughter", "A"),
	_09B("09B", "Negligent Manslaughter", "A"),
	_09C("09C", "Justifiable Homicide", "A"),
	_64A("64A", "Human Trafficking, Commercial Sex Acts", "A"),
	_64B("64B", "Human Trafficking, Involuntary Servitude", "A"),
	_100("100", "Kidnapping/Abduction AbstractPersonSegment", "A"),
	_23A("23A", "Pocket-picking", "A"),
	_23B("23B", "Purse-snatching ", "A"),
	_23C("23C", "Shoplifting ", "A"),
	_23D("23D", "Theft From Building", "A"),
	_23E("23E", "Theft From Coin-Operated Machine or Device ", "A"),
	_23F("23F", "Theft From Motor Vehicle", "A"),
	_23G("23G", "Theft of Motor Vehicle Parts or Accessories ", "A"),
	_23H("23H", "All Other Larceny", "A"),
	_240("240", "Motor Vehicle Theft", "A"),
	_370("370", "Pornography/Obscene Material ", "A"),
	_40A("40A", "Prostitution ", "A"),
	_40B("40B", "Assisting or Promoting Prostitution ", "A"),
	_40C("40C", "Purchasing Prostitution ", "A"),
	_120("120", "Robbery ", "A"),
	_11A("11A", "Rape ", "A"),
	_11B("11B", "Sodomy ", "A"),
	_11C("11C", "Sexual Assault With An Object", "A"),
	_11D("11D", "Fondling ", "A"),
	_36A("36A", "Incest ", "A"),
	_36B("36B", "Statutory Rape ", "A"),
	_280("280", "Stolen PropertySegment Offenses ", "A"),
	_520("520", "Weapon Law Violations", "A"),
	_90A("90A", "Bad Checks", "B"),
	_90B("90B", "Curfew/Loitering/Vagrancy Violations", "B"),
	_90C("90C", "Disorderly Conduct", "B"),
	_90D("90D", "Driving Under the Influence", "B"),
	_90E("90E", "Drunkenness", "B"),
	_90F("90F", "Family Offenses, Nonviolent", "B"),
	_90G("90G", "Liquor Law Violations", "B"),
	_90H("90H", "Peeping Tom", "B"),
	_90I("90I", "Runaway", "B"),
	_90J("90J", "Trespass of Real PropertySegment", "B")
	;

	public String code;
	public String description;
	public String group;

	private OffenseCode(String code, String description, String group) {
		this.code = code;
		this.description = description;
		this.group = group;
	}
	
	public static final Set<OffenseCode> asSet() {
		return EnumSet.allOf(OffenseCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (OffenseCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}