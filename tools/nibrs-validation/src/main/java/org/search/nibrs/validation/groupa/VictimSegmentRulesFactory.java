/*******************************************************************************
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.AggravatedAssaultHomicideCircumstancesCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.OfficerAssignmentType;
import org.search.nibrs.model.codes.RaceOfOffenderCode;
import org.search.nibrs.model.codes.RelationshipOfVictimToOffenderCode;
import org.search.nibrs.model.codes.SexCode;
import org.search.nibrs.model.codes.SexOfOffenderCode;
import org.search.nibrs.model.codes.TypeInjuryCode;
import org.search.nibrs.model.codes.TypeOfOfficerActivityCircumstance;
import org.search.nibrs.model.codes.TypeOfVictimCode;
import org.search.nibrs.validation.PersonSegmentRulesFactory;
import org.search.nibrs.validation.rules.AbstractBeanPropertyRule;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.NullObjectRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class VictimSegmentRulesFactory {
	
	private static final Logger LOG = LogManager.getLogger(VictimSegmentRulesFactory.class);

	private List<Rule<VictimSegment>> rulesList;
	private PersonSegmentRulesFactory<VictimSegment> personSegmentRulesFactory;
	
	public static VictimSegmentRulesFactory instance() {
		return new VictimSegmentRulesFactory();
	}
	
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
	
	private static final class PersonVictimNotBlankRule<T extends VictimSegment> extends AbstractBeanPropertyRule<VictimSegment> {
		
		private String typeCode;
		
		public PersonVictimNotBlankRule(String propertyName, String dataElementIdentifier, NIBRSErrorCode errorCode, String typeCode) {
			super(propertyName, dataElementIdentifier, VictimSegment.class, errorCode);
			this.typeCode = typeCode;
		}

		@Override
		protected boolean propertyViolatesRule(Object value, VictimSegment subject) {
			return (typeCode.equals(subject.getTypeOfVictim()) && value == null);
		}

	}

	private static final class NonPersonVictimBlankRule<T extends VictimSegment> extends AbstractBeanPropertyRule<VictimSegment> {
		
		public NonPersonVictimBlankRule(String propertyName, String dataElementIdentifier, NIBRSErrorCode errorCode) {
			super(propertyName, dataElementIdentifier, VictimSegment.class, errorCode);
		}

		@Override
		protected boolean propertyViolatesRule(Object value, VictimSegment victimSegment) {
			List<String> offenseList = new ArrayList<>();
			offenseList.addAll(victimSegment.getUcrOffenseCodeList());
			offenseList.removeAll(NULL_STRING_LIST);
			return ((!victimSegment.isPerson() || !OffenseCode.containsCrimeAgainstPersonCode(offenseList)) && value != null);
		}

	}

	private VictimSegmentRulesFactory() {
		personSegmentRulesFactory = new PersonSegmentRulesFactory<VictimSegment>(VictimSegment.class);
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
		rulesList.add(getRule406ForTypeOfInjury());		
		rulesList.add(getRule406ForAggravatedAssaultHomicideCircumstances());		
		rulesList.add(getRule406ForVictimConnectedToUcrOffenseCode());
		rulesList.add(getRule406OffenderNumberToBeRelated());
		rulesList.add(getRule407());
		rulesList.add(getRule409());
		rulesList.add(getRule410ForAgeOfVictim());
		rulesList.add(getRule419ForAggravatedAssaultHomicideCircumstances());
		rulesList.add(getRule419ForTypeOfInjury());
		rulesList.add(getRule422ForAgeOfVictim());
		rulesList.add(getRule450ForAgeOfVictim());				
		rulesList.add(getRule453ForAgeOfVictim());
		rulesList.add(getRule453ForSexOfVictim());
		rulesList.add(getRule453ForRaceOfVictim());
		rulesList.add(getRule454ForTypeOfOfficerActivityCircumstance());
		rulesList.add(getRule454ForSexOfVictim());
		rulesList.add(getRule454ForRaceOfVictim());
		rulesList.add(getRule454ForAgeOfVictim());
		rulesList.add(getRule454ForOfficerAssignmentType());
		rulesList.add(getRule455ForAdditionalJustifiableHomicideCircsumstances());	
		rulesList.add(getRule456());
		rulesList.add(getRule457ForAdditionalJustifiableHomicideCircsumstances());
		rulesList.add(getRule458ForSexOfVictim());	
		rulesList.add(getRule458ForResidentStatusOfVictim());				
		rulesList.add(getRule458ForOffenderNumberToBeRelated());	
		rulesList.add(getRule458ForAgeOfVictim());
		rulesList.add(getRule458ForEthnicityOfVictim());
		rulesList.add(getRule458ForRaceOfVictim());
		rulesList.add(getRule459ForOffenderNumberToBeRelated());
		rulesList.add(getRule460ForRelationshipOfVictimToOffender());
		rulesList.add(getRule461ForTypeOfVictim());
		rulesList.add(getRule462());
		rulesList.add(getRule463());
		rulesList.add(getRule464ForTypeOfVictim());
		rulesList.add(getRule465ForTypeOfVictim());
		rulesList.add(getRule467ForTypeOfVictim());		
		rulesList.add(getRule469ForSexOfVictim());
		rulesList.add(getRule468ForRelationshipOfVictimToOffender());
		rulesList.add(getRule471());
		rulesList.add(getRule472());
		rulesList.add(getRule475());
		rulesList.add(getRule476());
		rulesList.add(getRule477());
		rulesList.add(getRule478());
		rulesList.add(getRule479());
		rulesList.add(getRule481ForAgeOfVictim());		
		rulesList.add(getRule482ForTypeOfVictim());
		rulesList.add(getRule483ForTypeOfOfficerActivity());
		rulesList.add(getRule483ForOfficerAssignmentType());
		rulesList.add(getRule483ForOfficerOtherJurisdictionORI());

		rulesList.add(getRule070());

	}
		
	public List<Rule<VictimSegment>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}
	
	Rule<VictimSegment> getRule070() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment subject) {
				NIBRSError e = null;
				GroupAIncidentReport parent = (GroupAIncidentReport) subject.getParentReport();
				List<Integer> related = new ArrayList<>();
				related.addAll(subject.getOffenderNumberRelatedList());
				related.removeIf(element -> element == null);
				for (Integer offenderNumber : related) {
					OffenderSegment matchedOffender = parent.getOffenderForSequenceNumber(offenderNumber);
					if (matchedOffender == null) {
						e = subject.getErrorTemplate();
						e.setNIBRSErrorCode(NIBRSErrorCode._070);
						e.setDataElementIdentifier("34");
						e.setValue(offenderNumber);
						break;
					}
				}
				return e;
			}
		};
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

	Rule<VictimSegment> getRule404ForOfficerOriOtherJurisdiction() {
		// note:  we cannot check if an ORI is in the FBI's database.  And the field is always optional, so nothing to test here
		return new NullObjectRule<>();
	}

	Rule<VictimSegment> getRule404ForOfficerAssignmentType() {
		return new ValidValueListRule<VictimSegment>("officerAssignmentType", "25B", VictimSegment.class, NIBRSErrorCode._404, OfficerAssignmentType.codeSet(), true);
	}

	Rule<VictimSegment> getRule404ForTypeOfOfficerActivityCircumstance() {
		return new ValidValueListRule<VictimSegment>("typeOfOfficerActivityCircumstance", "25A", VictimSegment.class, NIBRSErrorCode._404, TypeOfOfficerActivityCircumstance.codeSet(), true);
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
				if ((CollectionUtils.containsAny(offenseCodeList, INJURY_OFFENSE_LIST) && injuryTypeList.isEmpty()) ||
						(!injuryTypeList.isEmpty() && !CollectionUtils.containsAll(TypeInjuryCode.codeSet(), injuryTypeList))) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._404);
					e.setDataElementIdentifier("33");
					e.setValue(null);
				}

				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule404ForSexOfVictim(){
		return personSegmentRulesFactory.getSexValidNonBlankRule("27", NIBRSErrorCode._404);
	}
	
	Rule<VictimSegment> getRule404ForEthnicityOfVictim(){
		return personSegmentRulesFactory.getEthnicityValidNonBlankRule("29", NIBRSErrorCode._404, true);
	}
	
	Rule<VictimSegment> getRule404ForResidentStatusOfVictim(){
		return personSegmentRulesFactory.getResidentStatusValidNonBlankRule("30", NIBRSErrorCode._404, true);
	}
	
	Rule<VictimSegment> getRule404ForRaceOfVictim(){
		return personSegmentRulesFactory.getRaceValidNonBlankRule("28", NIBRSErrorCode._404);
	}
	
	Rule<VictimSegment> getRule404ForAgeOfVictim() {
		return personSegmentRulesFactory.getAgeValidRule("27", NIBRSErrorCode._404, false);
	}

	Rule<VictimSegment> getRule404ForAggravatedAssaultHomicideCircumstances() {
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

	Rule<VictimSegment> getRule455ForAdditionalJustifiableHomicideCircsumstances() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				List<String> assaultCircList = victimSegment.getAggravatedAssaultHomicideCircumstancesList();
				if ((assaultCircList.contains(AggravatedAssaultHomicideCircumstancesCode._20.code) ||
						assaultCircList.contains(AggravatedAssaultHomicideCircumstancesCode._21.code))
						&& victimSegment.getAdditionalJustifiableHomicideCircumstances() == null) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("32");
					e.setNIBRSErrorCode(NIBRSErrorCode._455);
					e.setValue(assaultCircList);
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule457ForAdditionalJustifiableHomicideCircsumstances() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError e = null;

				String ajhc = victimSegment.getAdditionalJustifiableHomicideCircumstances();
				List<String> validJustHomicideList = Arrays.asList(AggravatedAssaultHomicideCircumstancesCode._09C.code,
						AggravatedAssaultHomicideCircumstancesCode._20.code,
						AggravatedAssaultHomicideCircumstancesCode._21.code);

				if (ajhc != null &&
						!CollectionUtils.containsAny(victimSegment.getAggravatedAssaultHomicideCircumstancesList(), validJustHomicideList)) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("32");
					e.setNIBRSErrorCode(NIBRSErrorCode._457);
					e.setValue(ajhc);
				}

				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule406ForVictimConnectedToUcrOffenseCode() {
		return new DuplicateCodedValueRule<>("ucrOffenseCodeConnection", "24", VictimSegment.class, NIBRSErrorCode._406);
	}

	Rule<VictimSegment> getRule406ForAggravatedAssaultHomicideCircumstances() {
		return new DuplicateCodedValueRule<VictimSegment>("aggravatedAssaultHomicideCircumstances", "31", VictimSegment.class, NIBRSErrorCode._406);
	}

	Rule<VictimSegment> getRule406OffenderNumberToBeRelated() {
		return new DuplicateCodedValueRule<VictimSegment>("offenderNumberRelated", "34", VictimSegment.class, NIBRSErrorCode._406);
	}

	Rule<VictimSegment> getRule406ForTypeOfInjury() {
		return new DuplicateCodedValueRule<VictimSegment>("typeOfInjury", "33", VictimSegment.class, NIBRSErrorCode._406);
	}
	
	Rule<VictimSegment> getRule407() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				NIBRSError errorTemplate = victimSegment.getErrorTemplate();
				errorTemplate.setDataElementIdentifier("33");
				errorTemplate.setNIBRSErrorCode(NIBRSErrorCode._407);
				Set<String> noneDisallowed = new HashSet<>();
				noneDisallowed.addAll(TypeInjuryCode.codeSet());
				noneDisallowed.remove(TypeInjuryCode.N.code);
				Set<String> minorDisallowed = new HashSet<>();
				minorDisallowed.addAll(TypeInjuryCode.codeSet());
				minorDisallowed.remove(TypeInjuryCode.M.code);
				List<String> injuryList = new ArrayList<>();
				injuryList.addAll(victimSegment.getTypeOfInjuryList());
				injuryList.removeAll(NULL_STRING_LIST);
				errorTemplate.setValue(injuryList);
				if (injuryList.contains(TypeInjuryCode.M.code) && CollectionUtils.containsAny(injuryList, minorDisallowed)) {
					e = errorTemplate;
				} else if (injuryList.contains(TypeInjuryCode.N.code) && CollectionUtils.containsAny(injuryList, noneDisallowed)) {
					e = errorTemplate;
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule409() {
		return personSegmentRulesFactory.getAgeRangeLengthRule("26", NIBRSErrorCode._409);
	}
	
	Rule<VictimSegment> getRule410ForAgeOfVictim() {
		return personSegmentRulesFactory.getProperAgeRangeRule("26", NIBRSErrorCode._410);
	}

	Rule<VictimSegment> getRule419ForTypeOfInjury() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				List<String> offenseList = victimSegment.getUcrOffenseCodeList();
				List<String> typeOfInjuryList = new ArrayList<>();
				typeOfInjuryList.addAll(victimSegment.getTypeOfInjuryList());
				typeOfInjuryList.removeAll(NULL_STRING_LIST);

				if (!typeOfInjuryList.isEmpty() && (offenseList.isEmpty() || !OffenseCode.containsCrimeAgainstPersonCode(offenseList))) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("33");
					e.setNIBRSErrorCode(NIBRSErrorCode._419);
					e.setValue(typeOfInjuryList);
				}

				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule419ForAggravatedAssaultHomicideCircumstances() {
		return new Rule<VictimSegment>() {

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;

				List<String> aahc = new ArrayList<>();
				aahc.addAll(victimSegment.getAggravatedAssaultHomicideCircumstancesList());
				aahc.removeAll(NULL_STRING_LIST);

				List<String> ucrOffenseList = new ArrayList<>();
				ucrOffenseList.addAll(victimSegment.getUcrOffenseCodeList());
				ucrOffenseList.removeAll(NULL_STRING_LIST);

				List<String> assaultHomicideOffenses = Arrays.asList(OffenseCode._09A.code, OffenseCode._09B.code, OffenseCode._09C.code, OffenseCode._13A.code);

				if (!aahc.isEmpty() && !CollectionUtils.containsAny(ucrOffenseList, assaultHomicideOffenses)) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._419);
					e.setDataElementIdentifier("31");
					e.setValue(aahc);
				}

				return e;
			}
		};
	}

	Rule<VictimSegment> getRule422ForAgeOfVictim() {
		return personSegmentRulesFactory.getNonZeroAgeRangeMinimumRule("26", NIBRSErrorCode._422);
	}

	Rule<VictimSegment> getRule450ForAgeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (victimSegment.getVictimOffenderRelationshipList().contains(RelationshipOfVictimToOffenderCode.SE.code)) {
					NIBRSAge nibrsAge = victimSegment.getAge();
					if (nibrsAge != null) {
						Integer minAge = nibrsAge.getAgeMin();
						if (minAge != null && minAge < 10) {
							e = victimSegment.getErrorTemplate();
							e.setDataElementIdentifier("26");
							e.setNIBRSErrorCode(NIBRSErrorCode._450);
							e.setValue(nibrsAge);
						}
					}
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule453ForAgeOfVictim() {
		return new PersonVictimNotBlankRule<>("age", "26", NIBRSErrorCode._453, TypeOfVictimCode.I.code);
	}

	Rule<VictimSegment> getRule453ForSexOfVictim() {
		return new PersonVictimNotBlankRule<>("sex", "27", NIBRSErrorCode._453, TypeOfVictimCode.I.code);
	}

	Rule<VictimSegment> getRule453ForRaceOfVictim() {
		return new PersonVictimNotBlankRule<>("race", "28", NIBRSErrorCode._453, TypeOfVictimCode.I.code);
	}
	
	// TODO: need to be able to configure the factory for enforcing leoka rules, or not (for these next two rules)

	Rule<VictimSegment> getRule454ForTypeOfOfficerActivityCircumstance() {
		return new PersonVictimNotBlankRule<>("typeOfOfficerActivityCircumstance", "25A", NIBRSErrorCode._454, TypeOfVictimCode.L.code);
	}

	Rule<VictimSegment> getRule454ForOfficerAssignmentType() {
		return new PersonVictimNotBlankRule<>("officerAssignmentType", "25B", NIBRSErrorCode._454, TypeOfVictimCode.L.code);
	}

	Rule<VictimSegment> getRule454ForSexOfVictim() {
		return new PersonVictimNotBlankRule<>("sex", "27", NIBRSErrorCode._454, TypeOfVictimCode.L.code);
	}
	
	Rule<VictimSegment> getRule454ForRaceOfVictim() {
		return new PersonVictimNotBlankRule<>("race", "28", NIBRSErrorCode._454, TypeOfVictimCode.L.code);
	}
	
	Rule<VictimSegment> getRule454ForAgeOfVictim() {
		return new PersonVictimNotBlankRule<>("age", "26", NIBRSErrorCode._454, TypeOfVictimCode.L.code);
	}
	
	Rule<VictimSegment> getRule458ForAgeOfVictim() {
		return new NonPersonVictimBlankRule<>("age", "26", NIBRSErrorCode._458);
	}

	Rule<VictimSegment> getRule458ForEthnicityOfVictim() {
		return new NonPersonVictimBlankRule<>("ethnicity", "29", NIBRSErrorCode._458);
	}

	Rule<VictimSegment> getRule458ForSexOfVictim() {
		return new NonPersonVictimBlankRule<>("sex", "27", NIBRSErrorCode._458);
	}

	Rule<VictimSegment> getRule458ForRaceOfVictim() {
		return new NonPersonVictimBlankRule<>("race", "28", NIBRSErrorCode._458);
	}

	Rule<VictimSegment> getRule458ForResidentStatusOfVictim() {
		return new NonPersonVictimBlankRule<>("residentStatus", "30", NIBRSErrorCode._458);
	}

	Rule<VictimSegment> getRule458ForTypeOfInjury() {
		return new Rule<VictimSegment>() {

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError e = null;
				
				List<String> offenseList = new ArrayList<>();
				offenseList.addAll(victimSegment.getUcrOffenseCodeList());
				offenseList.removeAll(NULL_STRING_LIST);

				List<String> typeOfInjuryList = new ArrayList<>();
				typeOfInjuryList.addAll(victimSegment.getTypeOfInjuryList());
				typeOfInjuryList.removeAll(NULL_STRING_LIST);

				if (!(OffenseCode.containsCrimeAgainstPersonCode(offenseList) && victimSegment.isPerson()) && !typeOfInjuryList.isEmpty()) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("33");
					e.setNIBRSErrorCode(NIBRSErrorCode._458);
					e.setValue(typeOfInjuryList);
				}
				
				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule458ForOffenderNumberToBeRelated() {
		return new Rule<VictimSegment>() {

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				
				NIBRSError e = null;
				
				List<String> offenseList = new ArrayList<>();
				offenseList.addAll(victimSegment.getUcrOffenseCodeList());
				offenseList.removeAll(NULL_STRING_LIST);

				List<Integer> offenderList = new ArrayList<>();
				offenderList.addAll(victimSegment.getOffenderNumberRelatedList());
				offenderList.removeAll(NULL_INTEGER_LIST);

				if (!(OffenseCode.containsCrimeAgainstPersonCode(offenseList) && victimSegment.isPerson()) && !offenderList.isEmpty()) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("34");
					e.setNIBRSErrorCode(NIBRSErrorCode._458);
					e.setValue(offenderList);
				}
				
				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule459ForOffenderNumberToBeRelated() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;

				List<Integer> offenderNumberList = new ArrayList<>();
				offenderNumberList.addAll(victimSegment.getOffenderNumberRelatedList());
				offenderNumberList.removeAll(NULL_INTEGER_LIST);

				List<String> offenseCodeList = new ArrayList<>();
				offenseCodeList.addAll(victimSegment.getUcrOffenseCodeList());
				offenseCodeList.removeAll(NULL_STRING_LIST);

				if (!offenderNumberList.isEmpty() && !(OffenseCode.containsCrimeAgainstPersonCode(offenseCodeList) || offenseCodeList.contains(OffenseCode._120.code))) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("34");
					e.setNIBRSErrorCode(NIBRSErrorCode._459);
					e.setValue(offenderNumberList);
				}
				
				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule460ForRelationshipOfVictimToOffender() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				List<String> victimOffenderRelationshipList = victimSegment.getVictimOffenderRelationshipList();
				List<Integer> offenderNumRelatedList = victimSegment.getOffenderNumberRelatedList();
				
				for (int i=0;i < offenderNumRelatedList.size() && e == null;i++) {
					Integer offenderNumber = offenderNumRelatedList.get(i);
					if (offenderNumber != null && offenderNumber != 0) {
						String rel = victimOffenderRelationshipList.get(i);
						if (rel == null) {
							e = victimSegment.getErrorTemplate();
							e.setDataElementIdentifier("35");
							e.setNIBRSErrorCode(NIBRSErrorCode._460);
							e.setValue(offenderNumber);
						}
					}
				}

				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule461ForTypeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (victimSegment.getUcrOffenseCodeList().contains(OffenseCode._220.code) && TypeOfVictimCode.S.code.equals(victimSegment.getTypeOfVictim())) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._461);
					e.setDataElementIdentifier("25");
					e.setValue(TypeOfVictimCode.S.code);
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule476() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				GroupAIncidentReport parent = (GroupAIncidentReport) victimSegment.getParentReport();
				int spouseCount = 0;
				Integer[] offenderNumbersRelated = victimSegment.getOffenderNumberRelated();
				for (int i=0; i < offenderNumbersRelated.length;i++) {
					String relationship = victimSegment.getVictimOffenderRelationship(i);
					Integer offenderSequenceNumber = offenderNumbersRelated[i];
					if (offenderSequenceNumber != null && RelationshipOfVictimToOffenderCode.SE.code.equals(relationship)) {
						OffenderSegment os = parent.getOffenderForSequenceNumber(offenderSequenceNumber);
						List<VictimSegment> siblingVictims = new ArrayList<>();
						siblingVictims.addAll(parent.getVictimsOfOffender(os));
						siblingVictims.remove(victimSegment);
						for (VictimSegment siblingVictim : siblingVictims) {
							Integer[] siblingOffenderNumbersRelated = siblingVictim.getOffenderNumberRelated();
							for (int j=0;j < siblingOffenderNumbersRelated.length;j++) {
								if (offenderSequenceNumber.equals(siblingOffenderNumbersRelated[j])) {
									if (RelationshipOfVictimToOffenderCode.SE.code.equals(siblingVictim.getVictimOffenderRelationship(j))) {
										spouseCount++;
									}
								}
							}
						}
					}
				}
				if (spouseCount > 0) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._476);
					e.setDataElementIdentifier("35");
					e.setValue(spouseCount);
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule462() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				Set<String> allowedValues = new HashSet<>();
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._01.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._02.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._03.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._04.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._05.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._06.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._08.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._09.code);
				allowedValues.add(AggravatedAssaultHomicideCircumstancesCode._10.code);
				List<String> aahc = new ArrayList<>();
				aahc.addAll(victimSegment.getAggravatedAssaultHomicideCircumstancesList());
				aahc.removeAll(allowedValues);
				if (victimSegment.getUcrOffenseCodeList().contains(OffenseCode._13A.code) && aahc.size() > 0) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._462);
					e.setDataElementIdentifier("31");
					e.setValue(aahc);
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule463() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				List<String> aahc = victimSegment.getAggravatedAssaultHomicideCircumstancesList();
				if (victimSegment.getUcrOffenseCodeList().contains(OffenseCode._09C.code) &&
						!(aahc.contains(AggravatedAssaultHomicideCircumstancesCode._20.code) || aahc.contains(AggravatedAssaultHomicideCircumstancesCode._21.code))) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._463);
					e.setDataElementIdentifier("31");
					e.setValue(aahc);
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule464ForTypeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (OffenseCode.containsCrimeAgainstPersonCode(victimSegment.getUcrOffenseCodeList()) && !victimSegment.isPerson()) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("25");
					e.setNIBRSErrorCode(NIBRSErrorCode._464);
					e.setValue(victimSegment.getTypeOfVictim());
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule465ForTypeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (OffenseCode.containsCrimeAgainstSocietyCode(victimSegment.getUcrOffenseCodeList()) && !TypeOfVictimCode.S.code.equals(victimSegment.getTypeOfVictim())) {
					e = victimSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._465);
					e.setDataElementIdentifier("25");
					e.setValue(victimSegment.getUcrOffenseCodeList());
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule467ForTypeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (TypeOfVictimCode.S.code.equals(victimSegment.getTypeOfVictim()) && OffenseCode.containsCrimeAgainstPropertyCode(victimSegment.getUcrOffenseCodeList())) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("25");
					e.setNIBRSErrorCode(NIBRSErrorCode._467);
					e.setValue(TypeOfVictimCode.S.code);
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule468ForRelationshipOfVictimToOffender() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				List<String> victimOffenderRelationshipList = victimSegment.getVictimOffenderRelationshipList();
				List<Integer> offenderNumRelatedList = victimSegment.getOffenderNumberRelatedList();
				
				for (int i=0;i < offenderNumRelatedList.size() && e == null;i++) {
					Integer offenderNumber = offenderNumRelatedList.get(i);
					if (offenderNumber != null && offenderNumber == 0) {
						String rel = victimOffenderRelationshipList.get(i);
						if (rel != null) {
							e = victimSegment.getErrorTemplate();
							e.setDataElementIdentifier("35");
							e.setNIBRSErrorCode(NIBRSErrorCode._468);
							e.setValue(rel);
						}
					}
				}

				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule469ForSexOfVictim() {
		return new Rule<VictimSegment>() {

			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				
				List<String> offenses = Arrays.asList(new String[] {OffenseCode._36B.code, OffenseCode._11A.code});
				List<String> sexes = Arrays.asList(new String[] {SexCode.F.code, SexCode.M.code});

				if (CollectionUtils.containsAny(victimSegment.getUcrOffenseCodeList(), offenses) && !sexes.contains(victimSegment.getSex())) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("27");
					e.setNIBRSErrorCode(NIBRSErrorCode._469);
					e.setValue(victimSegment.getSex());
				}
				
				return e;
				
			}
		};
	}

	Rule<VictimSegment> getRule481ForAgeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				NIBRSAge age = victimSegment.getAge();

				if (victimSegment.getUcrOffenseCodeList().contains(OffenseCode._36B.code) && age != null && age.getAgeMax() >= 18) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("26");
					e.setNIBRSErrorCode(NIBRSErrorCode._481);
					e.setValue(age);
				}

				return e;

			}
		};
	}

	Rule<VictimSegment> getRule482ForTypeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				List<String> allowedOffenseList = Arrays.asList(OffenseCode._09A.code, OffenseCode._13A.code, OffenseCode._13B.code, OffenseCode._13C.code);

				if (TypeOfVictimCode.L.code.equals(victimSegment.getTypeOfVictim()) && !CollectionUtils.containsAny(victimSegment.getUcrOffenseCodeList(), allowedOffenseList)) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("25");
					e.setNIBRSErrorCode(NIBRSErrorCode._482);
					e.setValue(victimSegment.getTypeOfVictim());
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule483ForTypeOfOfficerActivity() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (victimSegment.getTypeOfOfficerActivityCircumstance() != null && !TypeOfVictimCode.L.code.equals(victimSegment.getTypeOfVictim())) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("25A");
					e.setNIBRSErrorCode(NIBRSErrorCode._483);
					e.setValue(victimSegment.getOfficerAssignmentType());
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule483ForOfficerAssignmentType() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (victimSegment.getOfficerAssignmentType() != null && !TypeOfVictimCode.L.code.equals(victimSegment.getTypeOfVictim())) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("25B");
					e.setNIBRSErrorCode(NIBRSErrorCode._483);
					e.setValue(victimSegment.getOfficerAssignmentType());
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule483ForOfficerOtherJurisdictionORI() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (victimSegment.getOfficerOtherJurisdictionORI() != null && !TypeOfVictimCode.L.code.equals(victimSegment.getTypeOfVictim())) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("25C");
					e.setNIBRSErrorCode(NIBRSErrorCode._483);
					e.setValue(victimSegment.getOfficerAssignmentType());
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule471() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				List<String> relationshipList = new ArrayList<>();
				relationshipList.addAll(victimSegment.getVictimOffenderRelationshipList());
				relationshipList.removeAll(NULL_STRING_LIST);
				List<Integer> offenderNumberList = new ArrayList<>();
				offenderNumberList.addAll(victimSegment.getOffenderNumberRelatedList());
				offenderNumberList.removeAll(NULL_INTEGER_LIST);
				if (relationshipList.contains(RelationshipOfVictimToOffenderCode.VO.code) && offenderNumberList.size() > 1) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("34");
					e.setNIBRSErrorCode(NIBRSErrorCode._471);
					e.setValue(RelationshipOfVictimToOffenderCode.VO.code);
				}
				return e;
			}
		};
	}

	Rule<VictimSegment> getRule472() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				GroupAIncidentReport incident = (GroupAIncidentReport) victimSegment.getParentReport();
				List<Integer> offenderNumberList = victimSegment.getOffenderNumberRelatedList();
				List<String> relationshipList = victimSegment.getVictimOffenderRelationshipList();
				for (int i=0;i < offenderNumberList.size() && e == null;i++) {
					Integer offenderNumber = offenderNumberList.get(i);
					String relationship = relationshipList.get(i);
					if (offenderNumber != null) {
						OffenderSegment os = incident.getOffenderForSequenceNumber(offenderNumber);
						if (os != null &&
							(os.getAge() == null ||
							 os.getSex() == null || os.getSex().equals(SexOfOffenderCode.U.code) ||
							 os.getRace() == null || os.getRace().equals(RaceOfOffenderCode.U.code)) && !relationship.equals(RelationshipOfVictimToOffenderCode.RU.code)) {
							e = victimSegment.getErrorTemplate();
							e.setDataElementIdentifier("35");
							e.setNIBRSErrorCode(NIBRSErrorCode._472);
							e.setValue(relationship);
						}
					}
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule475() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				List<String> relationshipList = new ArrayList<>();
				relationshipList.addAll(victimSegment.getVictimOffenderRelationshipList());
				relationshipList.removeAll(NULL_STRING_LIST);
				if (relationshipList.size() > 1) {
					int spouse = 0;
					for (String rel : relationshipList) {
						if (RelationshipOfVictimToOffenderCode.SE.code.equals(rel)) {
							spouse++;
						}
					}
					if (spouse > 1) {
						e = victimSegment.getErrorTemplate();
						e.setDataElementIdentifier("34");
						e.setNIBRSErrorCode(NIBRSErrorCode._475);
						e.setValue(RelationshipOfVictimToOffenderCode.SE.code);
					}
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule477() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				NIBRSError errorTemplate = victimSegment.getErrorTemplate();
				errorTemplate.setNIBRSErrorCode(NIBRSErrorCode._477);
				errorTemplate.setDataElementIdentifier("31");
				List<String> offenseList = new ArrayList<>();
				offenseList.addAll(victimSegment.getUcrOffenseCodeList());
				offenseList.removeAll(NULL_STRING_LIST);
				List<String> aahc = new ArrayList<>();
				aahc.addAll(victimSegment.getAggravatedAssaultHomicideCircumstancesList());
				aahc.removeAll(NULL_STRING_LIST);
				if (offenseList.contains(OffenseCode._13A.code) || offenseList.contains(OffenseCode._09A.code)) {
					Set<String> allowedSet = new HashSet<>();
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._01.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._02.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._03.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._04.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._05.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._06.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._07.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._08.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._09.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._10.code);
					List<String> aahc2 = new ArrayList<>();
					aahc2.addAll(aahc);
					aahc2.removeAll(allowedSet);
					if (aahc.size() > 2 || aahc2.size() > 0) {
						e = errorTemplate;
						e.setValue(aahc2);
					}
				} else if (offenseList.contains(OffenseCode._09B.code)) {
					Set<String> allowedSet = new HashSet<>();
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._30.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._31.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._32.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._33.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._34.code);
					List<String> aahc2 = new ArrayList<>();
					aahc2.addAll(aahc);
					aahc2.removeAll(allowedSet);
					if (aahc.size() > 1 || aahc2.size() > 0) {
						e = errorTemplate;
						e.setValue(aahc2);
					}
				} else if (offenseList.contains(OffenseCode._09C.code)) {
					Set<String> allowedSet = new HashSet<>();
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._20.code);
					allowedSet.add(AggravatedAssaultHomicideCircumstancesCode._21.code);
					List<String> aahc2 = new ArrayList<>();
					aahc2.addAll(aahc);
					aahc2.removeAll(allowedSet);
					if (aahc.size() > 1 || aahc2.size() > 0) {
						e = errorTemplate;
						e.setValue(aahc2);
					}
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule478() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				NIBRSError errorTemplate = victimSegment.getErrorTemplate();
				errorTemplate.setDataElementIdentifier("24");
				errorTemplate.setNIBRSErrorCode(NIBRSErrorCode._478);
				List<String> offenseList = new ArrayList<>();
				offenseList.addAll(victimSegment.getUcrOffenseCodeList());
				offenseList.removeAll(NULL_STRING_LIST);
				errorTemplate.setValue(offenseList);
				Set<String> murderSet = new HashSet<>();
				murderSet.add(OffenseCode._09B.code);
				murderSet.add(OffenseCode._13A.code);
				murderSet.add(OffenseCode._13B.code);
				murderSet.add(OffenseCode._13C.code);
				Set<String> aggAssaultSet = new HashSet<>();
				aggAssaultSet.add(OffenseCode._13B.code);
				aggAssaultSet.add(OffenseCode._13C.code);
				aggAssaultSet.add(OffenseCode._09A.code);
				aggAssaultSet.add(OffenseCode._120.code);
				aggAssaultSet.add(OffenseCode._11A.code);
				aggAssaultSet.add(OffenseCode._11B.code);
				aggAssaultSet.add(OffenseCode._11C.code);
				Set<String> simpleAssaultSet = new HashSet<>();
				simpleAssaultSet.add(OffenseCode._13C.code);
				simpleAssaultSet.add(OffenseCode._09A.code);
				simpleAssaultSet.add(OffenseCode._13A.code);
				simpleAssaultSet.add(OffenseCode._120.code);
				simpleAssaultSet.add(OffenseCode._11A.code);
				simpleAssaultSet.add(OffenseCode._11B.code);
				simpleAssaultSet.add(OffenseCode._11C.code);
				simpleAssaultSet.add(OffenseCode._11D.code);
				Set<String> intimidationSet = new HashSet<>();
				intimidationSet.add(OffenseCode._13B.code);
				intimidationSet.add(OffenseCode._09A.code);
				intimidationSet.add(OffenseCode._13A.code);
				intimidationSet.add(OffenseCode._120.code);
				intimidationSet.add(OffenseCode._11A.code);
				intimidationSet.add(OffenseCode._11B.code);
				intimidationSet.add(OffenseCode._11C.code);
				intimidationSet.add(OffenseCode._11D.code);
				Set<String> negligentSet = new HashSet<>();
				negligentSet.add(OffenseCode._09A.code);
				negligentSet.add(OffenseCode._13A.code);
				negligentSet.add(OffenseCode._13B.code);
				negligentSet.add(OffenseCode._13C.code);
				if (offenseList.contains(OffenseCode._09A.code) && CollectionUtils.containsAny(offenseList, murderSet) ||
						offenseList.contains(OffenseCode._13A.code) && CollectionUtils.containsAny(offenseList, aggAssaultSet) ||
						offenseList.contains(OffenseCode._13B.code) && CollectionUtils.containsAny(offenseList, simpleAssaultSet) ||
						offenseList.contains(OffenseCode._13C.code) && CollectionUtils.containsAny(offenseList, intimidationSet) ||
						offenseList.contains(OffenseCode._09B.code) && CollectionUtils.containsAny(offenseList, negligentSet)) {
					e = errorTemplate;
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule479() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				if (victimSegment.getUcrOffenseCodeList().contains(OffenseCode._13B.code) &&
						!(victimSegment.getTypeOfInjuryList().contains(TypeInjuryCode.M.code) || victimSegment.getTypeOfInjuryList().contains(TypeInjuryCode.N.code))) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("33");
					e.setNIBRSErrorCode(NIBRSErrorCode._479);
					List<String> il = new ArrayList<>();
					il.addAll(victimSegment.getTypeOfInjuryList());
					il.removeAll(NULL_STRING_LIST);
					e.setValue(il);
				}
				return e;
			}
		};
	}
	
	Rule<VictimSegment> getRule456() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				List<String> aahc = new ArrayList<>();
				aahc.addAll(victimSegment.getAggravatedAssaultHomicideCircumstancesList());
				aahc.removeAll(NULL_STRING_LIST);
				NIBRSError errorTemplate = victimSegment.getErrorTemplate();
				errorTemplate.setDataElementIdentifier("31");
				errorTemplate.setNIBRSErrorCode(NIBRSErrorCode._456);
				errorTemplate.setValue(aahc);
				if (aahc.contains(AggravatedAssaultHomicideCircumstancesCode._10.code) && aahc.size() > 1) {
					e = errorTemplate;
				} else if (aahc.size() > 1) {
					Set<String> categorySet = new HashSet<>();
					for (String s : aahc) {
						categorySet.add(s.substring(0, 1));
					}
					if (categorySet.size() > 1) {
						e = errorTemplate;
					}
				}
				return e;
			}
		};
	}

}
