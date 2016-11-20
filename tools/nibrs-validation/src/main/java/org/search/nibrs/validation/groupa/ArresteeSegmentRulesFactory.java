package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.codes.DispositionOfArresteeUnder18Code;
import org.search.nibrs.model.codes.EthnicityOfArrestee;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.RaceOfArresteeCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexOfArresteeCode;
import org.search.nibrs.model.codes.TypeOfArrestCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.NotAllBlankRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class ArresteeSegmentRulesFactory {
	
	public static final String GROUP_A_ARRESTEE_MODE = "group-a-arrestee";
	public static final String GROUP_B_ARRESTEE_MODE = "group-b-arrestee";

	private List<Rule<ArresteeSegment>> rulesList;
	private String mode;
	
	public static final ArresteeSegmentRulesFactory instance(String mode) {
		return new ArresteeSegmentRulesFactory(mode);
	}	
	
	public List<Rule<ArresteeSegment>> getRulesList() {
		return rulesList;
	}	
	
	private ArresteeSegmentRulesFactory(String mode) {
		rulesList = new ArrayList<Rule<ArresteeSegment>>();
		this.mode = mode;
		initRules(rulesList);
	}
	
	private boolean isGroupAMode() {
		return GROUP_A_ARRESTEE_MODE.equals(mode);
	}
	
	
	private void initRules(List<Rule<ArresteeSegment>> rulesList) {
		rulesList.add(getRuleX01ForSequenceNumber());
//		rulesList.add(sequenceNumberNotBlank601Rule());
//		rulesList.add(arrestTransactionNumberNotBlank601Rule());
//		rulesList.add(arrestDateNotBlank601Rule());
//		rulesList.add(typeOfArrestRule());
//		rulesList.add(usrArrestOffenseCode());
//		rulesList.add(arresteeWasArmedWithNotAllEmpty601Rule());
//		rulesList.add(arresteeWasArmedWithNoDuplicates606Rule());
//		rulesList.add(ageOfArresteeNotBlank601Rule());
//		rulesList.add(sexOfArresteeNotBlank601Rule());
//		rulesList.add(sexOfArresteeValidValue667Rule());
//		rulesList.add(raceOfArresteeNotBlank601Rule());
//		rulesList.add(ethnicityOfArresteeNotBlank604Rule());
//		rulesList.add(residentStatusOfArresteeNotBlank604Code());
//		rulesList.add(dispositionOfArresteeUnder18ValidValue604Rule());
	}
	
	Rule<ArresteeSegment> getRuleX01ForSequenceNumber() {
		return new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {

				Integer arresteeSequenceNumber = arresteeSegment.getArresteeSequenceNumber();
				NIBRSError e = null;

				if (arresteeSequenceNumber == null || arresteeSequenceNumber < 1 || arresteeSequenceNumber > 99) {
					e = arresteeSegment.getErrorTemplate();
					e.setNIBRSErrorCode(isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
					e.setDataElementIdentifier("40");
					e.setValue(arresteeSequenceNumber);
				}

				return e;

			}
		};
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
	
	public Rule<ArresteeSegment> autoWeaponIndicatorNotBlank604Rule(){
		//TODO depends on 3rd element
		return null;
	}
	
	public Rule<ArresteeSegment> ageOfArresteeNotBlank601Rule(){
		
		//TODO validate range
		
		NotBlankRule<ArresteeSegment> notBlankRule = new NotBlankRule<ArresteeSegment>("age", 
				"47", ArresteeSegment.class, NIBRSErrorCode._601);
		
		return notBlankRule;
	}
	
	public Rule<ArresteeSegment> sexOfArresteeNotBlank601Rule(){
		
		NotBlankRule<ArresteeSegment> notBlankRule = new NotBlankRule<ArresteeSegment>("sex", 
				"49", ArresteeSegment.class, NIBRSErrorCode._601);
		
		return notBlankRule;
	}
	
	public Rule<ArresteeSegment> sexOfArresteeValidValue667Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("sex", 
		"49", ArresteeSegment.class, NIBRSErrorCode._667, SexOfArresteeCode.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<ArresteeSegment> raceOfArresteeNotBlank601Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("race", 
				"49", ArresteeSegment.class, NIBRSErrorCode._601, RaceOfArresteeCode.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<ArresteeSegment> ethnicityOfArresteeNotBlank604Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("ethnicity", 
				"50", ArresteeSegment.class, NIBRSErrorCode._604, EthnicityOfArrestee.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<ArresteeSegment> residentStatusOfArresteeNotBlank604Code(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("residentStatusOfArrestee", 
				"51", ArresteeSegment.class, NIBRSErrorCode._604, ResidentStatusCode.codeSet());
		
		return validValueListRule;		
	}
		
	
	public Rule<ArresteeSegment> dispositionOfArresteeUnder18ValidValue604Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("dispositionOfArresteeUnder18", 
				"52", ArresteeSegment.class, NIBRSErrorCode._604, DispositionOfArresteeUnder18Code.codeSet());
		
		return validValueListRule;
	}
	
// TODO see why not in pojo	
//	public Rule<ArresteeSegment> clearanceCodeValidValue604Rule(){
//		
//		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("TODO (not in pojo)", 
//			/* unnumbered */ "", ArresteeSegment.class, NIBRSErrorCode._604, ClearanceCode.codeSet());
//		
//		return validValueListRule;
//	}
	
}
