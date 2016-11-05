package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.AgeOfVictimCode;
import org.search.nibrs.model.codes.AggravatedAssaultHomicideCircumstancesCode;
import org.search.nibrs.model.codes.EthnicityOfVictim;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.RaceOfVictimCode;
import org.search.nibrs.model.codes.RelationshipOfVictimToOffenderCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexOfVictimCode;
import org.search.nibrs.model.codes.TypeOfOfficerActivityCircumstance;
import org.search.nibrs.model.codes.TypeOfVictimCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.NullObjectRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;


public class VictimSegmentRulesFactory {

	private List<Rule<VictimSegment>> rulesList;
	
	public static VictimSegmentRulesFactory instance(){
		
		return new VictimSegmentRulesFactory();
	}
	
	private static final List<String> PERSON_OFFENSE_LIST = Arrays.asList(OffenseCode._13A.code, OffenseCode._13B.code, OffenseCode._13C.code, 
			OffenseCode._09A.code, OffenseCode._09B.code, OffenseCode._64A.code, OffenseCode._64B.code, OffenseCode._100.code,
			OffenseCode._11A.code, OffenseCode._11B.code, OffenseCode._11C.code, OffenseCode._11D.code, OffenseCode._36A.code,
			OffenseCode._36B.code);
	
	private static final List<String> PROPERTY_CRIME_LIST = Arrays.asList(OffenseCode._200.code,
			OffenseCode._510.code, OffenseCode._220.code, OffenseCode._250.code,
			OffenseCode._290.code, OffenseCode._270.code, OffenseCode._210.code,
			OffenseCode._26A.code, OffenseCode._26B.code, OffenseCode._26C.code,
			OffenseCode._26D.code, OffenseCode._26E.code, OffenseCode._26F.code,
			OffenseCode._26G.code, OffenseCode._23A.code, OffenseCode._23B.code,
			OffenseCode._23C.code, OffenseCode._23D.code, OffenseCode._23E.code,
			OffenseCode._23F.code, OffenseCode._23G.code, OffenseCode._23H.code,
			OffenseCode._240.code, OffenseCode._120.code, OffenseCode._280.code					
	);
	
	private static final List<String> INJURY_OFFENSE_LIST = Arrays.asList(OffenseCode._100.code,
			OffenseCode._11A.code,
			OffenseCode._11B.code,
			OffenseCode._11C.code,
			OffenseCode._11D.code,
			OffenseCode._120.code,
			OffenseCode._13A.code,
			OffenseCode._13B.code,
			OffenseCode._210.code,
			OffenseCode._64A.code,
			OffenseCode._64B.code
			);
	
	private static final List<String> NULL_STRING_LIST = Arrays.asList(new String[] {null});
	private static final List<Integer> NULL_INTEGER_LIST = Arrays.asList(new Integer[] {null});
	
	private VictimSegmentRulesFactory() {
		rulesList = new ArrayList<Rule<VictimSegment>>();
		initRules(rulesList);
	}
	
	private void initRules(List<Rule<VictimSegment>> rulesList){
		rulesList.add(getRule401ForSequenceNumber());
		rulesList.add(getRule401ForVictimConnectedToUcrOffenseCode());
		rulesList.add(getRule401ForTypeOfVictim());
		rulesList.add(getRule404ForTypeOfOfficerActivityCircumstance());
		rulesList.add(getRule404ForOfficerAssignmentType());
		rulesList.add(getRule404ForOfficerOriOtherJurisdiction());
		rulesList.add(getRule404ForAgeOfVictim());		
		rulesList.add(getRule404ForSexOfVictim());		
		rulesList.add(getRule404ForRaceOfVictim());		
		rulesList.add(getRule404ForEthnicityOfVictim());		
		rulesList.add(getRule404ForResidentStatusOfVictim());		
		rulesList.add(getRule404ForAggravatedAssaultHomicideCircumstances());
		rulesList.add(getRule404OffenderNumberToBeRelated());
		rulesList.add(getRule404ForTypeOfInjury());
		rulesList.add(getRule404ForRelationshipOfVictimToOffender());		
		rulesList.add(getRule404ForAdditionalJustifiableHomicideCircsumstances());
		rulesList.add(getRule455ForAdditionalJustifiableHomicideCircsumstances());		
		rulesList.add(getRule406ForTypeOfInjury());		
		rulesList.add(getRule406ForAggravatedAssaultHomicideCircumstances());		
		rulesList.add(getRule406ForVictimConnectedToUcrOffenseCode());
		rulesList.add(getRule410ForAgeOfVictim());
		rulesList.add(getRule422ForAgeOfVictim());
		rulesList.add(getRule450ForAgeOfVictim());				
		rulesList.add(getRule469ForSexOfVictim());
		rulesList.add(getRule453ForAgeOfVictim());
		rulesList.add(getRule453ForSexOfVictim());
		rulesList.add(getRule453ForRaceOfVictim());
		rulesList.add(getRule454ForTypeOfOfficerActivityCircumstance());
		rulesList.add(getRule454ForSexOfVictim());
		rulesList.add(getRule454ForOfficerAssignmentType());
		rulesList.add(getRule464ForTypeOfVictim());
		rulesList.add(getRule465ForTypeOfVictim());
		rulesList.add(getRule467ForTypeOfVictim());		
		rulesList.add(getRule458ForSexOfVictim());	
		rulesList.add(getRule458ForResidentStatusOfVictim());				
		rulesList.add(getRule458ForOffenderNumberToBeRelated());	
		rulesList.add(getRule458ForAgeOfVictim());
		rulesList.add(getRule458ForEthnicityOfVictim());
		rulesList.add(getRule458ForRaceOfVictim());
		rulesList.add(getRule468ForRelationshipOfVictimToOffender());
		rulesList.add(getRule481ForAgeOfVictim());		
		rulesList.add(getRule482ForTypeOfVictim());
		rulesList.add(getRule483ForTypeOfOfficerActivity());
	}
		
	public List<Rule<VictimSegment>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}
			
	Rule<VictimSegment> getRule401ForSequenceNumber() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment subject) {

				Integer victimSequenceNumber = subject.getVictimSequenceNumber();
				NIBRSError e = null;
				
				if(victimSequenceNumber ==  null || victimSequenceNumber < 1 || victimSequenceNumber > 999) {
					e = subject.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._401);
					e.setDataElementIdentifier("23");
					e.setValue(victimSequenceNumber);
				}	
				
				return e;
				
			}
		};
	}
	
	Rule<VictimSegment> getRule401ForVictimConnectedToUcrOffenseCode() {
		return new Rule<VictimSegment>() {

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				NIBRSError errorTemplate = victimSegment.getErrorTemplate();
				errorTemplate.setDataElementIdentifier("24");
				errorTemplate.setNIBRSErrorCode(NIBRSErrorCode._401);

				Set<String> offenseCodeSet = new HashSet<>();
				offenseCodeSet.addAll(victimSegment.getUcrOffenseCodeList());
				offenseCodeSet.removeAll(NULL_STRING_LIST);

				if (offenseCodeSet.isEmpty()) {
					
					e = errorTemplate;
					e.setValue(null);
					
				} else {

					GroupAIncidentReport parent = (GroupAIncidentReport) victimSegment.getParentReport();
					Set<String> incidentOffenseCodes = new HashSet<>();
					for (OffenseSegment os : parent.getOffenses()) {
						incidentOffenseCodes.add(os.getUcrOffenseCode());
					}

					offenseCodeSet.removeAll(incidentOffenseCodes);

					if (!offenseCodeSet.isEmpty()) {
						e = errorTemplate;
						e.setValue(offenseCodeSet);
					}
				}

				return e;

			}
		};
	}	
	
	Rule<VictimSegment> getRule404OffenderNumberToBeRelated() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError e = null;
				NIBRSError errorTemplate = victimSegment.getErrorTemplate();
				errorTemplate.setDataElementIdentifier("34");
				errorTemplate.setNIBRSErrorCode(NIBRSErrorCode._404);

				Set<Integer> offenderNumberSet = new HashSet<>();
				offenderNumberSet.addAll(victimSegment.getOffenderNumberRelatedList());
				offenderNumberSet.removeAll(NULL_INTEGER_LIST);

				if (offenderNumberSet.isEmpty()) {
					
					e = errorTemplate;
					e.setValue(null);
					
				} else {

					GroupAIncidentReport parent = (GroupAIncidentReport) victimSegment.getParentReport();
					Set<Integer> offenderNumbers = new HashSet<>();
					for (OffenderSegment os : parent.getOffenders()) {
						offenderNumbers.add(os.getOffenderSequenceNumber());
					}

					offenderNumberSet.removeAll(offenderNumbers);

					if (!offenderNumberSet.isEmpty()) {
						e = errorTemplate;
						e.setValue(offenderNumberSet);
					}
				}

				return e;

			}
		};
	}

	Rule<VictimSegment> getRule401ForTypeOfVictim() {
		return new ValidValueListRule<VictimSegment>("typeOfVictim", "25", VictimSegment.class, NIBRSErrorCode._401, TypeOfVictimCode.codeSet(), false);
	}

	Rule<VictimSegment> getRule404ForTypeOfOfficerActivityCircumstance() {
		return new ValidValueListRule<VictimSegment>("typeOfOfficerActivityCircumstance", "25A", VictimSegment.class, NIBRSErrorCode._404, TypeOfOfficerActivityCircumstance.codeSet(), true);
	}

	public Rule<VictimSegment> getRule404ForSexOfVictim(){
		return new ValidValueListRule<VictimSegment>(
				"sex", "27", VictimSegment.class, NIBRSErrorCode._404, SexOfVictimCode.codeSet(), false);
	}

	public Rule<VictimSegment> getRule404ForOfficerOriOtherJurisdiction(){
						
		Rule<VictimSegment> officerOriRule = new Rule<VictimSegment>(){

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
								
				String officerOri = victimSegment.getOfficerOtherJurisdictionORI();
				
				boolean oriValid = true;
				
				if(StringUtils.isNotEmpty(officerOri)){

					if(StringUtils.isNumeric(officerOri)){
						
						int iOri = Integer.parseInt(officerOri);
						
						if(iOri < 0 || iOri > 9){
							
							oriValid = false;
						}						
					}else{
						
						oriValid = StringUtils.isAlpha(officerOri) && StringUtils.isAllUpperCase(officerOri);						
					}					
				}else{
					oriValid = false;
				}
				
				if(!oriValid){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("25C");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._404);
				}								
				return rNIBRSError;
			}			
		};		
		return officerOriRule;						
	}

	public Rule<VictimSegment> getRule404ForOfficerAssignmentType(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"officerAssignmentType", "25B", VictimSegment.class, NIBRSErrorCode._404, 
				TypeOfOfficerActivityCircumstance.codeSet(), false);
		
		return validValueListRule;
	}

	public Rule<VictimSegment> getRule404ForEthnicityOfVictim(){
		
		//TODO account for element being optional based on typeOfVicitmValue  
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"ethnicity", "29", VictimSegment.class, NIBRSErrorCode._404, 
				EthnicityOfVictim.codeSet(), false);
		
		return validValueListRule;
	}

	Rule<VictimSegment> getRule404ForTypeOfInjury(){
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError e = null;

				List<String> injuryTypeList = new ArrayList<>();
				injuryTypeList.addAll(victimSegment.getTypeOfInjuryList());
				injuryTypeList.removeAll(NULL_STRING_LIST);
				List<String> offenseCodeList = victimSegment.getUcrOffenseCodeList();
				if (CollectionUtils.containsAny(offenseCodeList, INJURY_OFFENSE_LIST) && injuryTypeList.isEmpty()) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._404);
					e.setDataElementIdentifier("33");
					e.setValue(null);
				}

				return e;
				
			}
		};
	}

	public Rule<VictimSegment> getRule404ForResidentStatusOfVictim(){
		
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"residentStatusOfVictim", "30", VictimSegment.class, NIBRSErrorCode._404, 
				ResidentStatusCode.codeSet(), false);
		
		return validValueListRule;
	}

	public Rule<VictimSegment> getRule404ForAggravatedAssaultHomicideCircumstances() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError e = null;

				List<String> aahcList = new ArrayList<>();
				aahcList.addAll(victimSegment.getAggravatedAssaultHomicideCircumstancesList());
				aahcList.removeAll(NULL_STRING_LIST);
				List<String> offenseCodeList = victimSegment.getUcrOffenseCodeList();
				if (offenseCodeList.contains(OffenseCode._09C.code) && aahcList.isEmpty()) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._404);
					e.setDataElementIdentifier("31");
					e.setValue(null);
				}

				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule404ForRelationshipOfVictimToOffender() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError e = null;
				
				List<Integer> relatedOffenderNumbers = victimSegment.getOffenderNumberRelatedList();
				List<String> relationships = victimSegment.getVictimOffenderRelationshipList();
				
				for (int i=0;i < relatedOffenderNumbers.size();i++) {
					Integer offenderNumber = relatedOffenderNumbers.get(i);
					String relationship = relationships.get(i);
					if (((offenderNumber == null || offenderNumber == 0) && relationship != null) ||
							(relationship == null && offenderNumber != null && offenderNumber > 0) ||
							(relationship != null && !RelationshipOfVictimToOffenderCode.codeSet().contains(relationship))) {
						e = victimSegment.getErrorTemplate();
						e.setNIBRSErrorCode(NIBRSErrorCode._404);
						e.setDataElementIdentifier("35");
						e.setValue(relationship);
					}
				}

				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule404ForAdditionalJustifiableHomicideCircsumstances() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;

				String ajhc = victimSegment.getAdditionalJustifiableHomicideCircumstances();
				List<String> offenseCodeList = victimSegment.getUcrOffenseCodeList();
				if (offenseCodeList.contains(OffenseCode._09C.code) && ajhc == null) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._404);
					e.setDataElementIdentifier("32");
					e.setValue(null);
				}

				return e;

			}
		};
	}

	public Rule<VictimSegment> getRule455ForAdditionalJustifiableHomicideCircsumstances(){
						
		Rule<VictimSegment> homicide455Rule = new Rule<VictimSegment>(){

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError rNIBRSError = null;
								
				List<String> assaultCircList = victimSegment.getAggravatedAssaultHomicideCircumstancesList();
								
				boolean isJustAssaultHomicide = assaultCircList != null
						&& (assaultCircList.contains(AggravatedAssaultHomicideCircumstancesCode._20.code)
							 || assaultCircList.contains(AggravatedAssaultHomicideCircumstancesCode._21.code) 
							);
								
				String sAddnlHomicideCirc = victimSegment.getAdditionalJustifiableHomicideCircumstances();
				
				boolean hasAddnlHomicideCir = StringUtils.isNotEmpty(sAddnlHomicideCirc);
				
				if(isJustAssaultHomicide && !hasAddnlHomicideCir){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("32");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._455);
				}				
				return rNIBRSError;
			}					
		};		
		return homicide455Rule;
	}
	
	public Rule<VictimSegment> getRule457ForAdditionalJustifiableHomicideCircsumstances(){
		
		Rule<VictimSegment> addnlInfoWithoutJustHomicideRule = new Rule<VictimSegment>(){

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError rNIBRSError = null;
				
				String addnlInfo = victimSegment.getAdditionalJustifiableHomicideCircumstances();
				
				boolean hasAddnlInfo = StringUtils.isNotEmpty(addnlInfo);
								
				List<String> actualAssaultCircList = victimSegment
						.getAggravatedAssaultHomicideCircumstancesList();
								
				List<String> validJustHomicideList = Arrays.asList(
						AggravatedAssaultHomicideCircumstancesCode._09C.code,
						AggravatedAssaultHomicideCircumstancesCode._20.code,
						AggravatedAssaultHomicideCircumstancesCode._21.code);
				
				boolean hasValidJustHomicideCode = CollectionUtils.containsAny(actualAssaultCircList, validJustHomicideList);
				
				if(hasAddnlInfo && !hasValidJustHomicideCode){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					
					rNIBRSError.setDataElementIdentifier("32");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._457);
				}				
				
				return rNIBRSError;
			}			
		};
		
		return addnlInfoWithoutJustHomicideRule;
	}
	
	public Rule<VictimSegment> getRule404ForRaceOfVictim(){
	
		ValidValueListRule<VictimSegment> validValueListRule = new ValidValueListRule<VictimSegment>(
				"race", "28", VictimSegment.class, NIBRSErrorCode._404, RaceOfVictimCode.codeSet(),
				false);
		
		return validValueListRule;
	}

	Rule<VictimSegment> getRule404ForAgeOfVictim() {
		// note:  because we parse the age string when constructing the nibrsAge property of VictimSegment, it is not possible
		// to have a separate violation of rule 404 for victim age.
		return new NullObjectRule<VictimSegment>();
	}

	public Rule<VictimSegment> getRule406ForVictimConnectedToUcrOffenseCode(){
		
		DuplicateCodedValueRule<VictimSegment> duplicateCodedValueRule = new DuplicateCodedValueRule<>
			("ucrOffenseCodeConnection", "24", VictimSegment.class, NIBRSErrorCode._406);
		
		return duplicateCodedValueRule;
	}	
		
	public Rule<VictimSegment> getRule406ForAggravatedAssaultHomicideCircumstances(){
	
		DuplicateCodedValueRule<VictimSegment> duplicateValueRule = new DuplicateCodedValueRule<VictimSegment>(
				"aggravatedAssaultHomicideCircumstances", "31", VictimSegment.class, NIBRSErrorCode._406);
		
		return duplicateValueRule;		
	}

	public Rule<VictimSegment> getRule406OffenderNumberToBeRelated(){
		
		DuplicateCodedValueRule<VictimSegment> noDuplicateRule = new DuplicateCodedValueRule<VictimSegment>("offenderNumberRelated", 
				"34", VictimSegment.class, NIBRSErrorCode._406);
		
		return noDuplicateRule;
	}

	public Rule<VictimSegment> getRule406ForTypeOfInjury(){
		
		DuplicateCodedValueRule<VictimSegment> duplicateCodedValueRule = new DuplicateCodedValueRule<VictimSegment>(
				"typeOfInjury",	"33", VictimSegment.class, NIBRSErrorCode._406);
		
		return duplicateCodedValueRule;
	}

	public Rule<VictimSegment> getRule410ForAgeOfVictim(){
		
		Rule<VictimSegment> ageRangeOrderRule = new Rule<VictimSegment>(){
			
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
	
				NIBRSAge nibrsAge = victimSegment.getAge();
				
				Integer ageMin = nibrsAge.getAgeMin();
				Integer ageMax = nibrsAge.getAgeMax();
				
				if(ageMin != null && ageMax != null && ageMin > ageMax){
					
					rNIBRSError = victimSegment.getErrorTemplate();					
					rNIBRSError.setDataElementIdentifier("26");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._410);					
				}								
				return rNIBRSError;
			}
		};
		return ageRangeOrderRule;
	}

	public Rule<VictimSegment> getRule419ForTypeOfInjury(){
		
		Rule<VictimSegment> typeInjuryForNonPersonRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
								
				List<String> offenseList =  victimSegment.getUcrOffenseCodeList();
				
				if(offenseList != null && !offenseList.isEmpty() 
					&& !CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST)){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("33");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._419);
				}
				
				return rNIBRSError;
			}						
		};		
		return typeInjuryForNonPersonRule;
	}

	public Rule<VictimSegment> getRule419ForAggravatedAssaultHomicideCircumstances(){
		
		Rule<VictimSegment> assaultRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
	
				NIBRSError rNIBRSError = null;
								
				List<String> assaultCircList = victimSegment.getAggravatedAssaultHomicideCircumstancesList();
				
				boolean hasAssaultEntered = assaultCircList != null && !assaultCircList.isEmpty();
				
				
				List<String> ucrOffenseList = victimSegment.getUcrOffenseCodeList();
				
				List<String> offenseListForAssault = Arrays.asList(OffenseCode._09A.code,
						OffenseCode._09B.code, OffenseCode._09C.code, OffenseCode._13A.code);
				
				boolean hasAssaultOffense = CollectionUtils.containsAny(ucrOffenseList, offenseListForAssault);
				
				if(hasAssaultEntered && !hasAssaultOffense){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._419);
					rNIBRSError.setDataElementIdentifier("31");
				}
				
				return rNIBRSError;
			}						
		};
		return assaultRule;
	}

	public Rule<VictimSegment> getRule422ForAgeOfVictim(){
		
		Rule<VictimSegment> ageMin00Rule = new Rule<VictimSegment>(){
			
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
	
				NIBRSAge nibrsAge = victimSegment.getAge();
								
				if(nibrsAge != null 
						&& nibrsAge.isAgeRange()
						&& nibrsAge.getAgeMin() != null 
						&& nibrsAge.getAgeMin() == 00){
	
					rNIBRSError = victimSegment.getErrorTemplate();					
					rNIBRSError.setDataElementIdentifier("26");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._422);
				}																	
				return rNIBRSError;
			}
		};
		return ageMin00Rule;
	}

	public Rule<VictimSegment> getRule450ForAgeOfVictim(){
		
		Rule<VictimSegment> ageOfSpouse = new Rule<VictimSegment>(){
			
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
				
				List<String> relationshipList = victimSegment.getVictimOffenderRelationshipList();
				
				if(relationshipList != null 
						&& relationshipList.contains(RelationshipOfVictimToOffenderCode.SE.code)){
				
					NIBRSAge nibrsAge = victimSegment.getAge();
					
					Integer minAge = nibrsAge.getAgeMin();
					
					if(minAge != null && minAge < 10){
	
						rNIBRSError = victimSegment.getErrorTemplate();					
						rNIBRSError.setDataElementIdentifier("26");
						rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._450);
					}									
				}
				return rNIBRSError;
			}
		};
		return ageOfSpouse;
	}

	public Rule<VictimSegment> getRule453ForRaceOfVictim(){
		
		Rule<VictimSegment> raceForIndividualRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
	
				NIBRSError rNIBRSError = null;
				
				String race = victimSegment.getRace();
				
				boolean hasRace = StringUtils.isNotEmpty(race);
				
				String typeOfVictim = victimSegment.getTypeOfVictim();
				
				boolean isIndivideual = TypeOfVictimCode.I.code.equals(typeOfVictim);
				
				if(isIndivideual && !hasRace){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._453);
					rNIBRSError.setDataElementIdentifier("28");
				}				
				return rNIBRSError;
			}			
		};
		return raceForIndividualRule;
	}

	public Rule<VictimSegment> getRule453ForAgeOfVictim(){
		
		Rule<VictimSegment> ageOfIndividualRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNibrsError = null;
								
				String victimType = victimSegment.getTypeOfVictim();
				
				boolean isIndividual = TypeOfVictimCode.I.code.equals(victimType);
				
				NIBRSAge victimAge = victimSegment.getAge();
								
				boolean hasNumericAge = victimAge != null 
						&& victimAge.getAgeMin() != null
						&& victimAge.getAgeMax() != null;
				
				boolean hasTextAgeCode = victimAge != null
						&& StringUtils.isNotEmpty(victimAge.getNonNumericAge());
				
				boolean hasAge = hasNumericAge || hasTextAgeCode;
												
				if(isIndividual && !hasAge){
					
					rNibrsError = victimSegment.getErrorTemplate();
					
					rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._453);
					rNibrsError.setDataElementIdentifier("26");
				}								
				return rNibrsError;
			}						
		}; 		
		return ageOfIndividualRule;		
	}

	public Rule<VictimSegment> getRule453ForSexOfVictim(){
		
		Rule<VictimSegment> sexOfIndividualRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNibrsError = null;
								
				String victimType = victimSegment.getTypeOfVictim();
				
				boolean isIndividual = TypeOfVictimCode.I.code.equals(victimType);
				
				boolean containsSexCode = StringUtils.isNotEmpty(victimSegment.getSex());
				
				if(isIndividual && !containsSexCode){
					
					rNibrsError = victimSegment.getErrorTemplate();
					
					rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._453);
					rNibrsError.setDataElementIdentifier("27");
				}								
				return rNibrsError;
			}						
		}; 		
		return sexOfIndividualRule;		
	}

	public Rule<VictimSegment> getRule454ForTypeOfOfficerActivityCircumstance(){
		
		Rule<VictimSegment> typeOfficerReqFieldsRule = new Rule<VictimSegment>(){
							
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNibrsError = null;
	
				String victimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsLawOfficer = TypeOfVictimCode.L.code.equals(victimType);
				
				if(victimIsLawOfficer){
										
					String circumstance = victimSegment.getTypeOfOfficerActivityCircumstance();
											
					if(StringUtils.isEmpty(circumstance)){
						
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

	public Rule<VictimSegment> getRule454ForOfficerAssignmentType(){
		
		Rule<VictimSegment> assignment454Rule = new Rule<VictimSegment>(){
							
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNibrsError = null;
	
				String victimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsLawOfficer = TypeOfVictimCode.L.code.equals(victimType);
				
				if(victimIsLawOfficer){
																				
					String assignmentType = victimSegment.getOfficerAssignmentType();
																
					if(StringUtils.isEmpty(assignmentType)){
						
						rNibrsError = victimSegment.getErrorTemplate();
						
						rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._454);
						rNibrsError.setDataElementIdentifier("25B");						
					}					
				}								
				return rNibrsError;
			}
		};			
		return assignment454Rule;
	}	
	
	public Rule<VictimSegment> getRule454ForSexOfVictim(){
		
		Rule<VictimSegment> sexOfVictim454Rule = new Rule<VictimSegment>(){
							
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNibrsError = null;
	
				String victimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsLawOfficer = TypeOfVictimCode.L.code.equals(victimType);
				
				if(victimIsLawOfficer){
																				
					String sexOfVictim = victimSegment.getSex();
																
					if(StringUtils.isEmpty(sexOfVictim)){
						
						rNibrsError = victimSegment.getErrorTemplate();
						
						rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._454);
						rNibrsError.setDataElementIdentifier("27");						
					}					
				}								
				return rNibrsError;
			}
		};			
		return sexOfVictim454Rule;
	}		
	
	public Rule<VictimSegment> getRule454ForRaceOfVictim(){
		
		Rule<VictimSegment> raceOfVictim454Rule = new Rule<VictimSegment>(){
							
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNibrsError = null;
	
				String victimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsLawOfficer = TypeOfVictimCode.L.code.equals(victimType);
				
				if(victimIsLawOfficer){
																				
					String raceOfVictim = victimSegment.getRace();
																
					if(StringUtils.isEmpty(raceOfVictim)){
						
						rNibrsError = victimSegment.getErrorTemplate();
						
						rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._454);
						rNibrsError.setDataElementIdentifier("28");						
					}					
				}								
				return rNibrsError;
			}
		};			
		return raceOfVictim454Rule;
	}		
	
	public Rule<VictimSegment> getRule458ForAgeOfVictim(){
		
		Rule<VictimSegment> ageRule458 = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);				
				
				NIBRSAge age = victimSegment.getAge();
				
				if(hasCrimeAgainstPerson && !victimIsPerson && age != null){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("26");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._458);
				}
															
				return rNIBRSError;
			}			
		};		
		return ageRule458;		
	}

	public Rule<VictimSegment> getRule458ForEthnicityOfVictim(){
		
		Rule<VictimSegment> ethnicityRule458 = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);				
				
				String ethnicity = victimSegment.getEthnicity();
				
				boolean hasEthnicity = StringUtils.isNotEmpty(ethnicity);
				
				if(hasCrimeAgainstPerson && !victimIsPerson && hasEthnicity){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("29");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._458);
				}
															
				return rNIBRSError;
			}			
		};		
		return ethnicityRule458;		
	}

	public Rule<VictimSegment> getRule458ForSexOfVictim(){
		
		Rule<VictimSegment> sexOfVictimRule458 = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);				
				
				String sSexOfVictim = victimSegment.getSex();
				
				boolean isSexOfVictimEntered= StringUtils.isNotEmpty(sSexOfVictim);
								
				if(hasCrimeAgainstPerson && !victimIsPerson && isSexOfVictimEntered){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("27");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._458);
				}
															
				return rNIBRSError;
			}			
		};		
		return sexOfVictimRule458;		
	}

	public Rule<VictimSegment> getRule458ForRaceOfVictim(){
		
		Rule<VictimSegment> raceRule458 = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);				
				
				String victimRace = victimSegment.getRace();
				
				if(hasCrimeAgainstPerson && !victimIsPerson && StringUtils.isNotEmpty(victimRace)){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("28");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._458);
				}
															
				return rNIBRSError;
			}			
		};		
		return raceRule458;		
	}

	public Rule<VictimSegment> getRule458ForResidentStatusOfVictim(){
		
		Rule<VictimSegment> residentStatusRule458 = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);				
				
				String residentStatus = victimSegment.getResidentStatusOfVictim();
				
				boolean hasResidentStatus = StringUtils.isNotEmpty(residentStatus);
				
				if(hasCrimeAgainstPerson && !victimIsPerson && hasResidentStatus){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("30");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._458);
				}
															
				return rNIBRSError;
			}			
		};		
		return residentStatusRule458;		
	}

	public Rule<VictimSegment> getRule458ForTypeOfInjury(){
		
		Rule<VictimSegment> typeInjuryRule458 = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);				
				
				List<String> typeOfInjuryList = victimSegment.getTypeOfInjuryList();
				
				boolean hasTypeOfInjury = typeOfInjuryList != null && !typeOfInjuryList.isEmpty();
				
				if(hasCrimeAgainstPerson && !victimIsPerson && hasTypeOfInjury){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("33");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._458);
				}															
				return rNIBRSError;
			}			
		};		
		return typeInjuryRule458;		
	}

	public Rule<VictimSegment> getRule458ForOffenderNumberToBeRelated(){
		
		Rule<VictimSegment> offenderNumRelatedRule458 = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);				
				
				List<Integer> offenderRelatedList = victimSegment.getOffenderNumberRelatedList();
				
				boolean hasOffenderNumRelated = offenderRelatedList != null && !offenderRelatedList.isEmpty();
				
				if(hasCrimeAgainstPerson && !victimIsPerson && (offenderRelatedList != null && hasOffenderNumRelated)){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("34");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._458);
				}															
				return rNIBRSError;
			}			
		};		
		return offenderNumRelatedRule458;		
	}

	public Rule<VictimSegment> getRule459ForOffenderNumberToBeRelated(){
		
		Rule<VictimSegment> offenderNumPersonRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;				
				
				List<Integer> offenderNumberRelatedList = victimSegment.getOffenderNumberRelatedList();
				
				boolean hasOffenderNumberRelated = offenderNumberRelatedList != null && !offenderNumberRelatedList.isEmpty();
				
				if(hasOffenderNumberRelated){
					
					List<String> offenseCodeList = victimSegment.getUcrOffenseCodeList();
					
					if(!CollectionUtils.containsAny(offenseCodeList, PERSON_OFFENSE_LIST)
							&& !offenseCodeList.contains(OffenseCode._120)){
						
						rNIBRSError = victimSegment.getErrorTemplate();
						rNIBRSError.setDataElementIdentifier("34");
						rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._459);
					}										
				}											
				return rNIBRSError;
			}			
		};		
		return offenderNumPersonRule;
	}

	public Rule<VictimSegment> getRule460ForRelationshipOfVictimToOffender(){
		
		Rule<VictimSegment> relationshiOffenderRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
	
				NIBRSError rNIBRSError = null;
				
				List<String> victimOffenderRelationshipList = victimSegment.getVictimOffenderRelationshipList();
								
				List<Integer> offenderNumRelatedList = victimSegment.getOffenderNumberRelatedList();
				
				boolean hasValidOffenderNumRelated = false;
				
				for(Integer iOffenderNumRelatd : offenderNumRelatedList){
				
					if(iOffenderNumRelatd != null && iOffenderNumRelatd > 0){
						
						hasValidOffenderNumRelated = true;
					}					
				}
				
				boolean hasVictimOffenderRelationship = victimOffenderRelationshipList != null 
						&& !victimOffenderRelationshipList.isEmpty();				
								
				if(hasVictimOffenderRelationship && !hasValidOffenderNumRelated){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					rNIBRSError.setDataElementIdentifier("35");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._460);
				}				
				return rNIBRSError;
			}
			
		};
		return relationshiOffenderRule;
	}

	public Rule<VictimSegment> getRule461ForTypeOfVictim(){
		
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
	
	public Rule<VictimSegment> getRule464ForTypeOfVictim(){
		
		Rule<VictimSegment> personTypeRule = new Rule<VictimSegment>(){
									
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
			
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstPerson = CollectionUtils.containsAny(offenseList, PERSON_OFFENSE_LIST);
								
				String sVictimType = victimSegment.getTypeOfVictim();
				
				boolean victimIsPerson = TypeOfVictimCode.I.code.equals(sVictimType)
						|| TypeOfVictimCode.L.code.equals(sVictimType);
				
				if(hasCrimeAgainstPerson && !victimIsPerson){
					
					rNIBRSError = victimSegment.getErrorTemplate();
					
					rNIBRSError.setDataElementIdentifier("25");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._464);
				}
												
				return rNIBRSError;
			}			
		};		
		return personTypeRule;		
	}
	
	public Rule<VictimSegment> getRule465ForTypeOfVictim(){
				
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
	
	public Rule<VictimSegment> getRule467ForTypeOfVictim(){
		
		Rule<VictimSegment> typeVictimPropertyOffenseRule = new Rule<VictimSegment>(){
				
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError rNIBRSError = null;
				
				List<String> offenseCodeList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasCrimeAgainstProperty = CollectionUtils.containsAny(offenseCodeList, PROPERTY_CRIME_LIST);
				
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
	
	public Rule<VictimSegment> getRule468ForRelationshipOfVictimToOffender(){
		
		Rule<VictimSegment> unknownOffendersRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
					
				List<String> victimOffenderRelationshipList = victimSegment.getVictimOffenderRelationshipList();
								
				List<Integer> offenderNumberRelatedList = victimSegment.getOffenderNumberRelatedList();
				
				if(offenderNumberRelatedList != null && offenderNumberRelatedList.contains(0)){
					if(victimOffenderRelationshipList != null && !victimOffenderRelationshipList.isEmpty()){
						
						rNIBRSError = victimSegment.getErrorTemplate();
						rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._468);
						rNIBRSError.setDataElementIdentifier("35");						
					}					
				}								
				return rNIBRSError;
			}			
		};
		return unknownOffendersRule; 		
	}

	public Rule<VictimSegment> getRule469ForSexOfVictim(){
		
		Rule<VictimSegment> sexOfVictimForStatutoryRapeRule = new Rule<VictimSegment>(){

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
								
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasRapeOffense = offenseList.contains(OffenseCode._36B)
						|| offenseList.contains(OffenseCode._11A);				
				
				String victimSexCode = victimSegment.getSex();
				
				boolean isMaleOrFemale = SexOfVictimCode.F.code.equals(victimSexCode)
						|| SexOfVictimCode.M.code.equals(victimSexCode);
																
				if(hasRapeOffense && !isMaleOrFemale){
					
					rNIBRSError = victimSegment.getErrorTemplate();					
					rNIBRSError.setDataElementIdentifier("27");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._469);
				}				
				
				return rNIBRSError;
			}						
		};		
		return sexOfVictimForStatutoryRapeRule;
	}
	
	public Rule<VictimSegment> getRule481ForAgeOfVictim(){
		
		Rule<VictimSegment> ageOfVictimUnder18ForRapeRule = new Rule<VictimSegment>(){
	
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError rNIBRSError = null;
								
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				
				boolean hasStatRape = offenseList.contains(OffenseCode._36B);				
				
				NIBRSAge age = victimSegment.getAge();
				
				boolean ageLessThan18 = age != null && age.getAgeMax() < 18;
				
				if(hasStatRape && !ageLessThan18){
					
					rNIBRSError = victimSegment.getErrorTemplate();					
					rNIBRSError.setDataElementIdentifier("26");
					rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._481);
				}				
				
				return rNIBRSError;
			}						
		};		
		return ageOfVictimUnder18ForRapeRule;
	}

	public Rule<VictimSegment> getRule482ForTypeOfVictim(){
		
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
										
					if(!lawOfficerVictimOffenseList.containsAll(victimOffenseList)){
	
						rNIBRSError = victimSegment.getErrorTemplate();
						
						rNIBRSError.setDataElementIdentifier("25");
						rNIBRSError.setNIBRSErrorCode(NIBRSErrorCode._482);
					};															
				}				
				return rNIBRSError;
			}			
		};		
		return typeVictimLawOfficerRule;
	}
	
	public Rule<VictimSegment> getRule483ForTypeOfOfficerActivity(){
		
		Rule<VictimSegment> personReqFieldsRule = new Rule<VictimSegment>(){
	
			NIBRSError nibrsError = null;
			
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
	
				String assignmentType = victimSegment.getOfficerAssignmentType();
				String ori = victimSegment.getOfficerOtherJurisdictionORI();
				NIBRSAge age = victimSegment.getAge();
				String race = victimSegment.getRace();
				String sex = victimSegment.getSex();
				String ethnicity = victimSegment.getEthnicity();
				String residentStatus = victimSegment.getResidentStatusOfVictim();				
				List<Integer> relatedOffenderList = victimSegment.getOffenderNumberRelatedList();
				String typeOfVictim = victimSegment.getTypeOfVictim();
				
				boolean isPersonVictim = TypeOfVictimCode.I.code.equals(typeOfVictim) 
						|| TypeOfVictimCode.L.code.equals(typeOfVictim); 
								
				if(StringUtils.isNotEmpty(assignmentType)
					|| StringUtils.isNotEmpty(ori)
					|| age != null
					|| StringUtils.isNotEmpty(race)
					|| StringUtils.isNotEmpty(sex)
					|| StringUtils.isNotEmpty(ethnicity)
					|| StringUtils.isNotEmpty(residentStatus)
					|| (relatedOffenderList != null && !relatedOffenderList.isEmpty()) 
					&& !isPersonVictim){
					
					nibrsError = victimSegment.getErrorTemplate();
					nibrsError.setDataElementIdentifier("25B");
					nibrsError.setNIBRSErrorCode(NIBRSErrorCode._483);
				}								
				return nibrsError;
			}			
		};		
		return personReqFieldsRule;
	}
	
}
