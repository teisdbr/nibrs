package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.EthnicityOfOffender;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.RaceOfOffender;
import org.search.nibrs.model.codes.SexOfOffenderCode;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;


public class OffenderSegmentRulesFactory {

	private List<Rule<OffenderSegment>> rulesList;
	
	public static OffenderSegmentRulesFactory instance(){
		
		return new OffenderSegmentRulesFactory();
	}
	
	private OffenderSegmentRulesFactory() {
		
		rulesList = new ArrayList<Rule<OffenderSegment>>();
		
		initRulesList(rulesList);
	}

	
	private void initRulesList(List<Rule<OffenderSegment>> rulesList){
		
		rulesList.add(offenderSequenceNumberRule());
		
		rulesList.add(ageOfOffenderRule());
		
		rulesList.add(sexOfOffenderValidValue504Rule());
		
		rulesList.add(raceOfOffenderValidValue504Rule());
		
		rulesList.add(ethnicityOfOffenderValidValue504Rule());
	}


	public List<Rule<OffenderSegment>> getRulesList() {
		
		return rulesList;
	}	

	public Rule<OffenderSegment> offenderSequenceNumberRule(){
		
		NotBlankRule<OffenderSegment> notBlankRule = new NotBlankRule<OffenderSegment>("offenderSequenceNumber", 
				"36", OffenderSegment.class, NIBRSErrorCode._501);
		
		return notBlankRule;
	}
	
	public Rule<OffenderSegment> ageOfOffenderRule(){
		
		//TODO add range conditions for valid values
		NotBlankRule<OffenderSegment> notBlankRule = new NotBlankRule<OffenderSegment>("age", 
				"37", OffenderSegment.class, NIBRSErrorCode._504);
		
		return notBlankRule;
	}

	public Rule<OffenderSegment> sexOfOffenderValidValue504Rule(){
		
		ValidValueListRule<OffenderSegment> validValueListRule = new ValidValueListRule<OffenderSegment>("sex", 
				"38", OffenderSegment.class, NIBRSErrorCode._504, SexOfOffenderCode.codeSet());
		
		return validValueListRule;
	}
	
	
	public Rule<OffenderSegment> raceOfOffenderValidValue504Rule(){
		
		ValidValueListRule<OffenderSegment> validValueListRule = new ValidValueListRule<OffenderSegment>("race", 
				"39", OffenderSegment.class, NIBRSErrorCode._504, RaceOfOffender.codeSet());
		
		return validValueListRule;		
	}
	
	public Rule<OffenderSegment> ethnicityOfOffenderValidValue504Rule(){
		
		ValidValueListRule<OffenderSegment> validValueListRule = new ValidValueListRule<OffenderSegment>("ethnicity", 
				"39A", OffenderSegment.class, NIBRSErrorCode._504, EthnicityOfOffender.codeSet());
		
		return validValueListRule;
	}
	
}



