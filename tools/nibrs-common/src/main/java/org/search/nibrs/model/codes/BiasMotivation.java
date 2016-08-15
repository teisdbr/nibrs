package org.search.nibrs.model.codes;

public enum BiasMotivation {
	
	_11("11" , "Anti-White"),
	_12("12" , "Anti-Black or African American"),
	_13("13" , "Anti-American Indian or Alaska Native"),
	_14("14" , "Anti-Asian"),
	_15("15" , "Anti-Multiple Races, Group"),
	_16("16" , "Anti-Native Hawaiian or Other Pacific Islander"),
	_31("31" , "Anti-Arab"),
	_32("32" , "Anti-Hispanic or Latino"),
	_33("33" , "Anti-Other Race/Ethnicity/Ancestry"),
	_21("21" , "Anti-Jewish"),
	_22("22" , "Anti-Catholic"),
	_23("23" , "Anti-Protestant"),
	_24("24" , "Anti-Islamic (Muslim)"),
	_25("25" , "Anti-Other Religion"),
	_26("26" , "Anti-Multiple Religions, Group"),
	_27("27" , "Anti-Atheism/Agnosticism"),
	_28("28" , "Anti-Mormon"),
	_29("29" , "Anti-Jehovah’s Witness"),
	_81("81" , "Anti-Eastern Orthodox (Russian, Greek, Other)"),
	_82("82" , "Anti-Other Christian"),
	_83("83" , "Anti-Buddhist"),
	_84("84" , "Anti-Hindu"),
	_85("85" , "Anti-Sikh"),
	_41("41" , "Anti-Gay"),
	_42("42" , "Anti-Lesbian"),
	_43("43" , "Anti-Lesbian, Gay, Bisexual, or Transgender (Mixed Group)"),	
	_44("44","Anti-Heterosexual"),
	_45("45","Anti-Bisexual"),
	_51("51","Anti-Physical Disability "),
	_52("52","Anti-Mental Disability"),
	_61("61","Anti-Male"),
	_62("62","Anti-Female"),
	_71("71","Anti-Transgender"),
	_72("72","Anti-Gender Non-Conforming"),
	_88("88","None (no bias)"),
	_99("99","Unknown (offender’s motivation not known)");
		
	private BiasMotivation(String code, String description){
		
		this.code = code;
		
		this.description = description;
	}
	
	private String code;
	
	private String description;

}
