package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.codes.ArresteeWasArmedWithCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfArrestCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.NotAllBlankRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class ArresteeSegmentRulesFactory {

	private List<Rule<ArresteeSegment>> rulesList;

	
	private ArresteeSegmentRulesFactory() {
	
		rulesList = new ArrayList<Rule<ArresteeSegment>>();
		
		initRules(rulesList);
	}
	
	
	private void initRules(List<Rule<ArresteeSegment>> rulesList){
		
		rulesList.add(sequenceNumberNotBlank601Rule());
		
		rulesList.add(arrestTransactionNumberNotBlank601Rule());
		
		rulesList.add(arrestDateNotBlank601Rule());
		
		rulesList.add(typeOfArrestRule());
		
		rulesList.add(usrArrestOffenseCode());
		
		rulesList.add(arresteeWasArmedWithNotAllEmpty601Rule());
		
		rulesList.add(arresteeWasArmedWithNoDuplicates606Rule());
	}
	
	
	private Rule<ArresteeSegment> sequenceNumberNotBlank601Rule(){
		
		//TODO maybe check valid values
		NotBlankRule<ArresteeSegment> notBlankRule = new NotBlankRule<ArresteeSegment>("arresteeSequenceNumber", 
				"40", ArresteeSegment.class, NIBRSErrorCode._601);
		
		return notBlankRule;
	}
	
	private Rule<ArresteeSegment> arrestTransactionNumberNotBlank601Rule(){
		
		NotBlankRule<ArresteeSegment> notBlankRule = new NotBlankRule<ArresteeSegment>("arrestTransactionNumber", 
				"41", ArresteeSegment.class, NIBRSErrorCode._601);
		
		return notBlankRule;
	}
	
	private Rule<ArresteeSegment> arrestDateNotBlank601Rule(){
		
		//TODO validate value
		
		NotBlankRule<ArresteeSegment> notBlankRule = new NotBlankRule<ArresteeSegment>("arrestDate", 
				"42", ArresteeSegment.class, NIBRSErrorCode._601);
		
		return notBlankRule;
	}
	
	
	private Rule<ArresteeSegment> typeOfArrestRule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("typeOfArrest", 
				"43", ArresteeSegment.class, NIBRSErrorCode._601, TypeOfArrestCode.codeSet());
		
		return validValueListRule;
	}
	
	private Rule<ArresteeSegment> usrArrestOffenseCode(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("ucrArrestOffenseCode", 
				"45", ArresteeSegment.class, NIBRSErrorCode._601, OffenseCode.codeSet());
		
		return validValueListRule;
	}
	
	
	private Rule<ArresteeSegment> arresteeWasArmedWithNotAllEmpty601Rule(){
		
		NotAllBlankRule<ArresteeSegment> notAllBlankRule = new NotAllBlankRule<ArresteeSegment>("arresteeArmedWith", 
				"46", ArresteeSegment.class, NIBRSErrorCode._601);
						
		return notAllBlankRule;
	}
	
	
	private Rule<ArresteeSegment> arresteeWasArmedWithNoDuplicates606Rule(){
		
		DuplicateCodedValueRule<ArresteeSegment> duplicateValueRule = new DuplicateCodedValueRule<ArresteeSegment>("arresteeArmedWith", 
				"46", ArresteeSegment.class, NIBRSErrorCode._606);
		
		return duplicateValueRule;
	}
	
	
		
	public static ArresteeSegmentRulesFactory instance(){
		
		return new ArresteeSegmentRulesFactory();
	}	
	
	public List<Rule<ArresteeSegment>> getRulesList() {
		return rulesList;
	}	
}
