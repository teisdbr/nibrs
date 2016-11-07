package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.AggravatedAssaultHomicideCircumstancesCode;
import org.search.nibrs.model.codes.EthnicityOfVictim;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.OfficerAssignmentType;
import org.search.nibrs.model.codes.RaceOfVictimCode;
import org.search.nibrs.model.codes.RelationshipOfVictimToOffenderCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexOfVictimCode;
import org.search.nibrs.model.codes.TypeOfOfficerActivityCircumstance;
import org.search.nibrs.model.codes.TypeOfVictimCode;
import org.search.nibrs.validation.rules.AbstractBeanPropertyRule;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.NullObjectRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;


public class VictimSegmentRulesFactory {

	private List<Rule<VictimSegment>> rulesList;
	
	public static VictimSegmentRulesFactory instance(){
		
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
	
	private static final class PersonVictimValidValueRule<T extends VictimSegment> extends AbstractBeanPropertyRule<VictimSegment> {
		
		private Set<String> allowedValueSet;
		private boolean allowNull;

		public PersonVictimValidValueRule(String propertyName, String dataElementIdentifier, NIBRSErrorCode errorCode, Set<String> allowedValueSet, boolean allowNull) {
			super(propertyName, dataElementIdentifier, VictimSegment.class, errorCode);
			this.allowedValueSet = allowedValueSet;
			this.allowNull = allowNull;
		}

		@Override
		protected boolean propertyViolatesRule(Object value, VictimSegment subject) {
			return (subject.isPerson() && ((!allowNull && value == null) || (value != null && !allowedValueSet.contains(value))));
		}

	}

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
		rulesList.add(getRule454ForOfficerAssignmentType());
		rulesList.add(getRule455ForAdditionalJustifiableHomicideCircsumstances());	
		rulesList.add(getRule457ForAdditionalJustifiableHomicideCircsumstances());
		rulesList.add(getRule458ForSexOfVictim());	
		rulesList.add(getRule458ForResidentStatusOfVictim());				
		rulesList.add(getRule458ForOffenderNumberToBeRelated());	
		rulesList.add(getRule458ForAgeOfVictim());
		rulesList.add(getRule458ForEthnicityOfVictim());
		rulesList.add(getRule458ForRaceOfVictim());
		rulesList.add(getRule459ForOffenderNumberToBeRelated());
		rulesList.add(getRule461ForTypeOfVictim());
		rulesList.add(getRule462());
		rulesList.add(getRule464ForTypeOfVictim());
		rulesList.add(getRule465ForTypeOfVictim());
		rulesList.add(getRule467ForTypeOfVictim());		
		rulesList.add(getRule469ForSexOfVictim());
		rulesList.add(getRule468ForRelationshipOfVictimToOffender());
		rulesList.add(getRule471());
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

	Rule<VictimSegment> getRule404ForSexOfVictim(){
		return new PersonVictimValidValueRule<VictimSegment>("sex", "27", NIBRSErrorCode._404, SexOfVictimCode.codeSet(), false);
	}
	
	Rule<VictimSegment> getRule404ForEthnicityOfVictim(){
		return new PersonVictimValidValueRule<VictimSegment>("ethnicity", "29", NIBRSErrorCode._404, EthnicityOfVictim.codeSet(), true);
	}
	
	Rule<VictimSegment> getRule404ForResidentStatusOfVictim(){
		return new PersonVictimValidValueRule<VictimSegment>("residentStatusOfVictim", "30", NIBRSErrorCode._404, ResidentStatusCode.codeSet(), true);
	}
	
	Rule<VictimSegment> getRule404ForRaceOfVictim(){
		return new PersonVictimValidValueRule<VictimSegment>("race", "28", NIBRSErrorCode._404, RaceOfVictimCode.codeSet(), false);
	}
	
	Rule<VictimSegment> getRule404ForAgeOfVictim() {
		// note:  because we parse the age string when constructing the nibrsAge property of VictimSegment, it is not possible
		// to have a separate violation of rule 404 for victim age.
		return new NullObjectRule<VictimSegment>();
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

	Rule<VictimSegment> getRule410ForAgeOfVictim() {
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {

				NIBRSError e = null;
				NIBRSAge nibrsAge = victimSegment.getAge();
				if (nibrsAge.isAgeRange()) {

					Integer ageMin = nibrsAge.getAgeMin();
					Integer ageMax = nibrsAge.getAgeMax();

					if (ageMin > ageMax) {
						e = victimSegment.getErrorTemplate();
						e.setDataElementIdentifier("26");
						e.setNIBRSErrorCode(NIBRSErrorCode._410);
						e.setValue(nibrsAge);
					}
					
				}
				return e;
			}
		};
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
		return new Rule<VictimSegment>() {
			@Override
			public NIBRSError apply(VictimSegment victimSegment) {
				NIBRSError e = null;
				NIBRSAge nibrsAge = victimSegment.getAge();
				if (nibrsAge != null && nibrsAge.isAgeRange() && nibrsAge.getAgeMin() != null && nibrsAge.getAgeMin() == 00) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("26");
					e.setNIBRSErrorCode(NIBRSErrorCode._422);
					e.setValue(nibrsAge);
				}
				return e;
			}
		};
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
		return new NonPersonVictimBlankRule<>("residentStatusOfVictim", "30", NIBRSErrorCode._458);
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
				List<String> sexes = Arrays.asList(new String[] {SexOfVictimCode.F.code, SexOfVictimCode.M.code});

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
				if (victimSegment.getOfficerAssignmentType() != null && !TypeOfVictimCode.L.code.equals(victimSegment.getTypeOfVictim())) {
					e = victimSegment.getErrorTemplate();
					e.setDataElementIdentifier("25B");
					e.setNIBRSErrorCode(NIBRSErrorCode._484);
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

}
