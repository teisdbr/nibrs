package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.NIBRSAge;
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
		
		rulesList.add(typeOfVictimCrimeAgainstSocietyRule465());
		
		rulesList.add(typeOfVictimCrimeAgainstPropertyRule467());
		
		rulesList.add(typeOfVictimLawOfficerRule482());
		
		rulesList.add(typeOfficerActivityRequiredFieldsRule454());
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
	
	/**
	 * (Type of Victim) contains a Crime Against Society, but Data Element 25 (Type of Victim) is not S=Society.
	 */
	public Rule<VictimSegment> typeOfVictimCrimeAgainstSocietyRule465(){
				
		Rule<VictimSegment> crimeSocietyRule = new Rule<VictimSegment>(){
			
			List<String> crimeAgainstSocietyOffenseList = Arrays.asList(OffenseCode._720.code,
					OffenseCode._35A.code, OffenseCode._35B.code, OffenseCode._39A.code, OffenseCode._39B.code,
					OffenseCode._39C.code, OffenseCode._39D.code, OffenseCode._370.code, OffenseCode._40A.code,
					OffenseCode._40B.code, OffenseCode._40C.code, OffenseCode._520.code);			

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNibrsError = null;
				
				List<String> offenseCodeList = victimSegment.getUcrOffenseCodeList();
								
				boolean hasCrimeAgainstSociety = CollectionUtils.containsAny(offenseCodeList, crimeAgainstSocietyOffenseList); 
				
				String sVictimType = victimSegment.getTypeOfVictim();
				
				if(hasCrimeAgainstSociety 
					&& !TypeOfVictimCode.S.code.equals(sVictimType)){
					
					rNibrsError = victimSegment.getErrorTemplate();
					
					rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._465);
					rNibrsError.setDataElementIdentifier("25");					
				}
				
				return rNibrsError;
			}					
		};
						
		return crimeSocietyRule;
	}
	


	/**
	 * (Type of Victim) contains a Crime Against Property, but Data Element 25
	 * (Type of Victim) is S=Society. This is not an allowable code for Crime
	 * Against Property offenses.
	 */
	public Rule<VictimSegment> typeOfVictimCrimeAgainstPropertyRule467(){
		
		Rule<VictimSegment> typeVictimPropertyOffenseRule = new Rule<VictimSegment>(){

			List<String> propertyCrimeList = Arrays.asList(OffenseCode._200.code,
					OffenseCode._510.code, OffenseCode._220.code, OffenseCode._250.code,
					OffenseCode._290.code, OffenseCode._270.code, OffenseCode._210.code,
					OffenseCode._26A.code, OffenseCode._26B.code, OffenseCode._26C.code,
					OffenseCode._26D.code, OffenseCode._26E.code, OffenseCode._26F.code,
					OffenseCode._26G.code, OffenseCode._23A.code, OffenseCode._23B.code,
					OffenseCode._23C.code, OffenseCode._23D.code, OffenseCode._23E.code,
					OffenseCode._23F.code, OffenseCode._23G.code, OffenseCode._23H.code,
					OffenseCode._240.code, OffenseCode._120.code, OffenseCode._280.code					
			);			
			
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError rNIBRSError = null;
				
				List<String> offenseCodeList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstProperty = CollectionUtils.containsAny(offenseCodeList, propertyCrimeList);
				
				String sVictimType = victimSegment.getTypeOfVictim();
				
				if(TypeOfVictimCode.S.equals(sVictimType) 
						&& hasCrimeAgainstProperty){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					
					rNIBRSError.setDataElementIdentifier("25");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._467);
				}								
				return rNIBRSError;
			}
		};			
		return typeVictimPropertyOffenseRule;
	}
	
	
	/**
	 * (Type of Victim) cannot be L=Law Enforcement Officer unless Data Element
	 * 24 (Victim Connected to UCR Offense Code) is one of the following:
	 * 09A=Murder & Non-negligent Manslaughter 13A=Aggravated Assault 13B=Simple
	 * Assault 13C=Intimidation
	 */
	public Rule<VictimSegment> typeOfVictimLawOfficerRule482(){
		
		Rule<VictimSegment> typeVictimLawOfficerRule = new Rule<VictimSegment>(){

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
				
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean isLawOfficerVictimType = TypeOfVictimCode.L.equals(sVictimType);
				
				if(isLawOfficerVictimType){
					
					List<String> lawOfficerVictimOffenseList = Arrays.asList(
							OffenseCode._09A.code, OffenseCode._13A.code,
							OffenseCode._13B.code, OffenseCode._13C.code);
					
					List<String> victimOffenseList = victimSegment.getUcrOffenseCodeList();					
					
					for(String victimOffense : victimOffenseList){
						
						if(!lawOfficerVictimOffenseList.contains(victimOffense)){
							
							rNIBRSError = victimSegment.getErrorTemplate();
							
							rNIBRSError.setDataElementIdentifier("25");
							rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._482);
						};
					}					
				}
				
				return rNIBRSError;
			}			
		};
		
		return typeVictimLawOfficerRule;
	}
	
		
	/**
	 * (Type of Officer Activity/Circumstance) The referenced data element in a
	 * Group A Incident Report must be populated with a valid data value and
	 * cannot be blank.
	 */
	public Rule<VictimSegment> typeOfOfficerActivityCircumstance404Rule(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"typeOfOfficerActivityCircumstance", "25A", VictimSegment.class, NIBRSErrorCode._404, 
				TypeOfOfficerActivityCircumstance.codeSet());
		
		return validValueListRule;
	}
	
		
	/**
	 * (Type of Officer Activity/Circumstance), Data Element 25B (Officer
	 * Assignment Type), Data Element 26 (Age of Victim), Data Element 27 (Sex
	 * of Victim), and Data Element 28 (Race of Victim) must be entered when
	 * Data Element 25 (Type of Victim) is L=Law Enforcement Officer.
	 */
	public Rule<VictimSegment> typeOfficerActivityRequiredFieldsRule454(){
		
		Rule<VictimSegment> typeOfficerReqFieldsRule = new Rule<VictimSegment>(){

			NIBRSError rNibrsError = null;
			
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				String victimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsLawOfficer = TypeOfVictimCode.L.code.equals(victimType);
				
				if(victimIsLawOfficer){
										
					String circumstance = victimSegment.getTypeOfOfficerActivityCircumstance();
					
					String assignmentType = victimSegment.getOfficerAssignmentType();
					
					NIBRSAge age = victimSegment.getAge();
					
					String victimSex = victimSegment.getSex();
					
					String victimRace = victimSegment.getRace();	
					
					if(StringUtils.isEmpty(circumstance) 
							|| StringUtils.isEmpty(assignmentType)
							|| age == null
							|| StringUtils.isEmpty(victimSex)
							|| StringUtils.isEmpty(victimRace)){
						
						rNibrsError = victimSegment.getErrorTemplate();
						
						rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._454);
						rNibrsError.setDataElementIdentifier("25A");						
					}					
				}								
				return rNibrsError;
			}
		};			
		return typeOfficerReqFieldsRule;
	}
	
	
	
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
