package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum for NIBRS OffenseSegment Codes.
 *
 */
public enum OffenseCode {
	
	_720("720", "Animal Cruelty"),
	_200("200", "Arson"),
	_13A("13A", "Aggravated Assault"),
	_13B("13B", "Simple Assault"),
	_13C("13C", "Intimidation"),
	_510("510", "Bribery"),
	_220("220", "Burglary/Breaking & Entering"),
	_250("250", "Counterfeiting/Forgery"),
	_290("290", "Destruction/Damage/Vandalism of PropertySegment"),
	_35A("35A", "Drug/Narcotic Violations"),
	_35B("35B", "Drug Equipment Violations"),
	_270("270", "Embezzlement"),
	_210("210", "Extortion/Blackmail"),
	_26A("26A", "False Pretenses/Swindle/Confidence Game"),
	_26B("26B", "Credit Card/Automated Teller Machine Fraud"),
	_26C("26C", "Impersonation"),
	_26D("26D", "Welfare Fraud"),
	_26E("26E", "Wire Fraud"),
	_26F("26F", "Identity Theft"),
	_26G("26G", "Hacking/Computer Invasion"),
	_39A("39A", "Betting/W agering"),
	_39B("39B", "Operating/Promoting/Assisting Society Gambling"),
	_39C("39C", "Gambling Equipment Violations"),
	_39D("39D", "Sports Tampering"),
	_09A("09A", "Murder & Non-negligent Manslaughter"),
	_09B("09B", "Negligent Manslaughter"),
	_09C("09C", "Justifiable Homicide"),
	_64A("64A", "Human Trafficking, Commercial Sex Acts"),
	_64B("64B", "Human Trafficking, Involuntary Servitude"),
	_100("100", "Kidnapping/Abduction AbstractPersonSegment"),
	_23A("23A", "Pocket-picking"),
	_23B("23B", "Purse-snatching "),
	_23C("23C", "Shoplifting "),
	_23D("23D", "Theft From Building"),
	_23E("23E", "Theft From Coin-Operated Machine or Device "),
	_23F("23F", "Theft From Motor Vehicle"),
	_23G("23G", "Theft of Motor Vehicle Parts or Accessories "),
	_23H("23H", "All Other Larceny"),
	_240("240", "Motor Vehicle Theft"),
	_370("370", "Pornography/Obscene Material "),
	_40A("40A", "Prostitution "),
	_40B("40B", "Assisting or Promoting Prostitution "),
	_40C("40C", "Purchasing Prostitution "),
	_120("120", "Robbery "),
	_11A("11A", "Rape "),
	_11B("11B", "Sodomy "),
	_11C("11C", "Sexual Assault With An Object"),
	_11D("11D", "Fondling "),
	_36A("36A", "Incest "),
	_36B("36B", "Statutory Rape "),
	_280("280", "Stolen PropertySegment Offenses "),
	_520("520", "Weapon Law Violations"),
	_90A("90A", "Bad Checks"),
	_90B("90B", "Curfew/Loitering/Vagrancy Violations"),
	_90C("90C", "Disorderly Conduct"),
	_90D("90D", "Driving Under the Influence"),
	_90E("90E", "Drunkenness"),
	_90F("90F", "Family Offenses, Nonviolent"),
	_90G("90G", "Liquor Law Violations"),
	_90H("90H", "Peeping Tom"),
	_90I("90I", "Runaway"),
	_90J("90J", "Trespass of Real PropertySegment")
	;

	public String code;
	public String description;

	private OffenseCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static final Set<OffenseCode> asSet() {
		return EnumSet.allOf(OffenseCode.class);
	}

}