package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfOfficerActivityCircumstance;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;


public class VictimSegmentRulesFactory {
		

	private List<Rule<VictimSegment>> rulesList;

	
	public static VictimSegmentRulesFactory instance(){
		
		return new VictimSegmentRulesFactory();
	}
	
	private VictimSegmentRulesFactory() {
		
		rulesList = new ArrayList<Rule<VictimSegment>>();
		
		initRules(rulesList);
	}
	
	private void initRules(List<Rule<VictimSegment>> rulesList){
		
		rulesList.add(victimSequenceNumber401Rule());
		
		rulesList.add(victimConnectedToUcrOffenseCode401Rule());
		
		rulesList.add(victimConnectedToUcrOffenseCodeDuplicateValues406Rule());
	}
	
	

	public List<Rule<VictimSegment>> getRulesList() {
		return rulesList;
	}
	
	
	public Rule<VictimSegment> victimSequenceNumber401Rule(){
		
		NotBlankRule<VictimSegment> notBlankRule = new NotBlankRule<VictimSegment>(
				"victimSequenceNumber", "23", VictimSegment.class, NIBRSErrorCode._401); 
		
		return notBlankRule;
	}
	
	
	public Rule<VictimSegment> victimConnectedToUcrOffenseCode401Rule() {
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"ucrOffenseCodeConnection", "24", VictimSegment.class, NIBRSErrorCode._401, OffenseCode.codeSet(), false);
		
		return validValueListRule;
	}	
	
	public Rule<VictimSegment> victimConnectedToUcrOffenseCodeDuplicateValues406Rule(){
		
		DuplicateCodedValueRule<VictimSegment> duplicateCodedValueRule = new DuplicateCodedValueRule<>
			("ucrOffenseCodeConnection", "24", VictimSegment.class, NIBRSErrorCode._406);
		
		return duplicateCodedValueRule;
	}	
	
	
	// TODO victimConnectedToUcrOffenseCodeMutexOffences478	
	
	public Rule<VictimSegment> typeOfVictim401Rule(){
		
		NotBlankRule<VictimSegment> notBlankRule = new NotBlankRule<VictimSegment>("typeOfVictim", 
				"25", VictimSegment.class, NIBRSErrorCode._401);
				
		return notBlankRule;
	}
	
	// TODO 461
	// TODO 464
	// TODO 465
	// TODO 467 
	// TODO 482
		
	public Rule<VictimSegment> typeOfOfficerActivityCircumstance404Rule(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"typeOfOfficerActivityCircumstance", "25A", VictimSegment.class, NIBRSErrorCode._404, 
				TypeOfOfficerActivityCircumstance.codeSet());
		
		return validValueListRule;
	}
	
				
}


