package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.AdditionalJustifiableHomicideCircumstancesCode;
import org.search.nibrs.model.codes.EthnicityOfVictim;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.RaceOfVictimCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexOfVictimCode;
import org.search.nibrs.model.codes.TypeOfOfficerActivityCircumstance;
import org.search.nibrs.model.codes.TypeOfVictimCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.NotAllBlankRule;
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
		
		rulesList.add(victimConnectedToUcrOffenseCodeNotAllBlank401Rule());
		
		rulesList.add(victimConnectedToUcrOffenseCodeNoDuplicateValues406Rule());
				
		rulesList.add(typeOfVictim401Rule());
		
		rulesList.add(typeOfOfficerActivityCircumstance404Rule());
		
		rulesList.add(officerAssignmentType404Rule());
				
		rulesList.add(officerOriOtherJurisdiction404Rule());
		
		rulesList.add(ageOfVictim404Rule());
		
		rulesList.add(sexOfVictim404Rule());		
		
		rulesList.add(raceOfVictim404Rule());		
		
		rulesList.add(ethnicityOfVictim404Rule());
		
		rulesList.add(residentStatusOfVictim404Rule());
		
		rulesList.add(aggravatedAssaultHomicideNotBlank404Rule());
		
		rulesList.add(aggravatedAssaultHomicideNoDuplicates406Rule());
		
		rulesList.add(additionalJustifiableHomicideCircumstances404Rule());
		
		rulesList.add(typeInjuryNotAllBlank404Rule());
		
		rulesList.add(typeInjuryNoDuplicates406Rule());
		
		rulesList.add(offenderNumberToBeRelatedNotAllBlank404Rule());
		
		rulesList.add(offenderNumberToBeRelatedNotAllBlank404Rule());
		
		rulesList.add(relationshipOfVictimToOffenderNotAllBlank404Rule());
	}
	
		
	public List<Rule<VictimSegment>> getRulesList() {
		return rulesList;
	}
		
	
	// (Victim Sequence Number The referenced data element in a Group A Incident Report must 
	// 	be populated with a valid data value and cannot be blank.
	public Rule<VictimSegment> victimSequenceNumber401Rule(){
		
		Rule<VictimSegment> sequenceNumberNotEmptyRule = new Rule<VictimSegment>(){

			@Override
			public NIBRSError apply(VictimSegment subject) {

				Integer iSeqNum = subject.getVictimSequenceNumber();
				
				NIBRSError rSeqNumInvalidError = null;
				
				if(iSeqNum ==  null || !(iSeqNum >= 1 && iSeqNum <= 999) ){
					
					rSeqNumInvalidError = subject.getErrorTemplate();				
					rSeqNumInvalidError.setNIBRSErrorCode(NIBRSErrorCode._401);					
					rSeqNumInvalidError.setDataElementIdentifier("23");															
				}
				
				return rSeqNumInvalidError;
			}};		
									
		return sequenceNumberNotEmptyRule;
	}
	
				
	//	(Victim Connected to UCR Offense Code) The referenced data element in a Group A Incident Report 
	//	must be populated with a valid data value and cannot be blank.
	public Rule<VictimSegment> victimConnectedToUcrOffenseCodeNotAllBlank401Rule() {		
		
		NotAllBlankRule<VictimSegment> ucrOffenseNotAllBlankRule = new NotAllBlankRule<VictimSegment>("ucrOffenseCodeConnection", 
				"24", VictimSegment.class, NIBRSErrorCode._401);		
		
		return ucrOffenseNotAllBlankRule;
	}	
	
	
	// (Victim Connected to UCR Offense Code) The referenced data element in
	// error is one that contains multiple data values. When more than one code
	// is entered, none can be duplicate codes.
	public Rule<VictimSegment> victimConnectedToUcrOffenseCodeNoDuplicateValues406Rule(){
		
		DuplicateCodedValueRule<VictimSegment> duplicateCodedValueRule = new DuplicateCodedValueRule<>
			("ucrOffenseCodeConnection", "24", VictimSegment.class, NIBRSErrorCode._406);
		
		return duplicateCodedValueRule;
	}	
		
	
	// TODO victimConnectedToUcrOffenseCodeMutexOffences478		
		

	// (Type of Victim) The referenced data element in a Group A Incident Report
	// must be populated with a valid data value and cannot be blank.
	public Rule<VictimSegment> typeOfVictim401Rule(){
								
		ValidValueListRule<VictimSegment> typeOfVictimValidValue401Rule = new ValidValueListRule<VictimSegment>(
				"typeOfVictim", "25", VictimSegment.class, NIBRSErrorCode._401, 
				TypeOfVictimCode.codeSet());
		
		return typeOfVictimValidValue401Rule;
	}
	
	// TODO 461
	
	// (Type of Victim) cannot have a value of S=Society/Public when the offense
	// is 220=Burglary/Breaking and Entering.
	public Rule<VictimSegment> typeOfVictimBurglary461Rule(){
		
		Rule<VictimSegment> societyBurglaryRule = new Rule<VictimSegment>(){

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError rNibrsError = null;
				
				String sVictimType = victimSegment.getTypeOfVictim();
													
				List<String> ucrOffenseCodeList = victimSegment.getUcrOffenseCodeList();
				
				if(ucrOffenseCodeList.contains(OffenseCode._220.code)
					&& TypeOfVictimCode.S.code.equals(sVictimType)){
										
					rNibrsError = victimSegment.getErrorTemplate();
					
					rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._461);
					rNibrsError.setDataElementIdentifier("25");					
				}												
				return rNibrsError;
			}};
		
		return societyBurglaryRule;
	}
	

	
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
	
	// TODO 454
	// TODO 483
	
	public Rule<VictimSegment> officerAssignmentType404Rule(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"officerAssignmentType", "25B", VictimSegment.class, NIBRSErrorCode._404, 
				TypeOfOfficerActivityCircumstance.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<VictimSegment> officerOriOtherJurisdiction404Rule(){

		NotBlankRule<VictimSegment> notBlankRule = new NotBlankRule<VictimSegment>(
				"officerOtherJurisdictionORI", "25C", VictimSegment.class, NIBRSErrorCode._404);
		
		//TODO validate values: A-Z, 0-9
		
		return notBlankRule;
	}
	
	
	public Rule<VictimSegment> ageOfVictim404Rule(){
		
		//TODO maybe use AgeOfVictimCode enum to validate values. range is tricky
		
		NotBlankRule<VictimSegment> notBlankRule = new NotBlankRule<VictimSegment>(
				"age", "26", VictimSegment.class, NIBRSErrorCode._404);
		
		return notBlankRule;
	}

	public Rule<VictimSegment> sexOfVictim404Rule(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"sex", "27", VictimSegment.class, NIBRSErrorCode._404, SexOfVictimCode.codeSet());
		
		return validValueListRule;
	}
	
	
	public Rule<VictimSegment> raceOfVictim404Rule(){
	
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"race", "28", VictimSegment.class, NIBRSErrorCode._404, RaceOfVictimCode.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<VictimSegment> ethnicityOfVictim404Rule(){
		
		//TODO account for element being optional based on typeOfVicitmValue  
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"ethnicity", "29", VictimSegment.class, NIBRSErrorCode._404, EthnicityOfVictim.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<VictimSegment> residentStatusOfVictim404Rule(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"residentStatusOfVictim", "30", VictimSegment.class, NIBRSErrorCode._404, 
				ResidentStatusCode.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<VictimSegment> aggravatedAssaultHomicideNotBlank404Rule(){
		
		//TODO add condition on victimConnected-to-ucrOffense code
		
		NotAllBlankRule<VictimSegment> notAllBlankRule = new NotAllBlankRule<VictimSegment>(
				"aggravatedAssaultHomicideCircumstances", "31", VictimSegment.class, NIBRSErrorCode._404);
				
		return notAllBlankRule;
	}
	
	public Rule<VictimSegment> aggravatedAssaultHomicideNoDuplicates406Rule(){

		DuplicateCodedValueRule<VictimSegment> duplicateValueRule = new DuplicateCodedValueRule<VictimSegment>(
				"aggravatedAssaultHomicideCircumstances", "31", VictimSegment.class, NIBRSErrorCode._406);
		
		return duplicateValueRule;		
	}
	
	public Rule<VictimSegment> additionalJustifiableHomicideCircumstances404Rule(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"additionalJustifiableHomicideCircumstances", "32", VictimSegment.class, 
				NIBRSErrorCode._404, AdditionalJustifiableHomicideCircumstancesCode.codeSet());
		
		return validValueListRule;
	}
	
	
	public Rule<VictimSegment> typeInjuryNotAllBlank404Rule(){
		
		NotAllBlankRule<VictimSegment> notAllBlankRule = new NotAllBlankRule<VictimSegment>("typeOfInjury", 
				"typeOfInjury", VictimSegment.class, NIBRSErrorCode._404);
		
		return notAllBlankRule;
	}
	
	public Rule<VictimSegment> typeInjuryNoDuplicates406Rule(){
		
		DuplicateCodedValueRule<VictimSegment> duplicateCodedValueRule = new DuplicateCodedValueRule<VictimSegment>(
				"typeOfInjury",	"33", VictimSegment.class, NIBRSErrorCode._406);
		
		return duplicateCodedValueRule;
	}
		
	public Rule<VictimSegment> offenderNumberToBeRelatedNotAllBlank404Rule(){
		
		//TODO add condition		
		NotAllBlankRule<VictimSegment> notAllBlankRule = new NotAllBlankRule<VictimSegment>("offenderNumberRelated", 
				"34", VictimSegment.class, NIBRSErrorCode._404);
		
		return notAllBlankRule;
	}
	
	public Rule<VictimSegment> offenderNumberToBeRelatedNoDuplicates406Rule(){
		
		DuplicateCodedValueRule<VictimSegment> noDuplicateRule = new DuplicateCodedValueRule<VictimSegment>("offenderNumberRelated", 
				"34", VictimSegment.class, NIBRSErrorCode._406);
		
		return noDuplicateRule;
	}
	
	public Rule<VictimSegment> relationshipOfVictimToOffenderNotAllBlank404Rule(){
		
		//TODO add condition
		NotAllBlankRule<VictimSegment> notAllBlankRule = new NotAllBlankRule<VictimSegment>("victimOffenderRelationship", 
				"35", VictimSegment.class, NIBRSErrorCode._404);
		
		return notAllBlankRule;		
	}
	
}
