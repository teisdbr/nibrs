package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.RelationshipOfVictimToOffenderCode;
import org.search.nibrs.validation.PersonSegmentRulesFactory;
import org.search.nibrs.validation.rules.AbstractBeanPropertyRule;
import org.search.nibrs.validation.rules.Rule;

public class OffenderSegmentRulesFactory {

	private static final class UnknownOffenderDemographicsRule extends AbstractBeanPropertyRule<OffenderSegment> {

		public UnknownOffenderDemographicsRule(String propertyName, String dataElementIdentifier) {
			super(propertyName, dataElementIdentifier, OffenderSegment.class, NIBRSErrorCode._552);
		}

		@Override
		protected boolean propertyViolatesRule(Object value, OffenderSegment subject) {
			Integer sequenceNumber = subject.getOffenderSequenceNumber();
			return sequenceNumber != null && sequenceNumber == 0 && value != null;
		}
		
	}
	
	private List<Rule<OffenderSegment>> rulesList;
	private PersonSegmentRulesFactory<OffenderSegment> personSegmentRulesFactory;
	
	public static OffenderSegmentRulesFactory instance(){
		return new OffenderSegmentRulesFactory();
	}
	
	OffenderSegmentRulesFactory() {
		personSegmentRulesFactory = new PersonSegmentRulesFactory<OffenderSegment>(OffenderSegment.class);
		rulesList = new ArrayList<Rule<OffenderSegment>>();
		initRulesList(rulesList);
	}
	
	private void initRulesList(List<Rule<OffenderSegment>> rulesList) {
		rulesList.add(getRule501());
		rulesList.add(getRule504ForAgeOfOffender());
		rulesList.add(getRule504ForSexOfOffender());
		rulesList.add(getRule504ForRaceOfOffender());
		rulesList.add(getRule504ForEthnicityOfOffender());
		rulesList.add(getRule510());
		rulesList.add(getRule522());
		rulesList.add(getRule552ForAge());
		rulesList.add(getRule552ForSex());
		rulesList.add(getRule552ForRace());
		rulesList.add(getRule552ForEthnicity());
	}

	public List<Rule<OffenderSegment>> getRulesList() {
		return rulesList;
	}	

	Rule<OffenderSegment> getRule501() {
		return new Rule<OffenderSegment>() {
			@Override
			public NIBRSError apply(OffenderSegment offenderSegment) {

				Integer offenderSequenceNumber = offenderSegment.getOffenderSequenceNumber();
				NIBRSError e = null;

				if (offenderSequenceNumber == null || offenderSequenceNumber < 1 || offenderSequenceNumber > 999) {
					e = offenderSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._501);
					e.setDataElementIdentifier("36");
					e.setValue(offenderSequenceNumber);
				}

				return e;

			}
		};
	}

	Rule<OffenderSegment> getRule557() {
		return new Rule<OffenderSegment>() {
			@Override
			public NIBRSError apply(OffenderSegment offenderSegment) {

				Integer offenderSequenceNumber = offenderSegment.getOffenderSequenceNumber();
				GroupAIncidentReport parent = (GroupAIncidentReport) offenderSegment.getParentReport();
				String exceptionalClearanceCode = parent.getExceptionalClearanceCode();
				
				NIBRSError e = null;

				if (offenderSequenceNumber != null && offenderSequenceNumber == 0 &&
					!(exceptionalClearanceCode == null || ClearedExceptionallyCode.N.code.equals(exceptionalClearanceCode))) {
					e = offenderSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._557);
					e.setDataElementIdentifier("36");
					e.setValue(offenderSequenceNumber);
				}

				return e;

			}
		};
	}

	Rule<OffenderSegment> getRule504ForAgeOfOffender() {
		return personSegmentRulesFactory.getAgeValidNonBlankRule();
	}

	Rule<OffenderSegment> getRule504ForSexOfOffender() {
		return personSegmentRulesFactory.getSexValidNonBlankRule("38", NIBRSErrorCode._504);
	}

	Rule<OffenderSegment> getRule504ForRaceOfOffender() {
		return personSegmentRulesFactory.getRaceValidNonBlankRule("39", NIBRSErrorCode._504);
	}

	Rule<OffenderSegment> getRule504ForEthnicityOfOffender() {
		return personSegmentRulesFactory.getEthnicityValidNonBlankRule("39A", NIBRSErrorCode._504, true);
	}
	
	Rule<OffenderSegment> getRule510() {
		return personSegmentRulesFactory.getProperAgeRangeRule("37", NIBRSErrorCode._510);
	}
	
	Rule<OffenderSegment> getRule522() {
		return personSegmentRulesFactory.getNonZeroAgeRangeMinimumRule("37", NIBRSErrorCode._522);
	}

	Rule<OffenderSegment> getRule550() {
		return new Rule<OffenderSegment>() {
			@Override
			public NIBRSError apply(OffenderSegment offenderSegment) {
				NIBRSError e = null;
				Integer offenderSequenceNumber = offenderSegment.getOffenderSequenceNumber();
				NIBRSAge age = offenderSegment.getAge();
				if (age != null && age.getAgeMin() < 10) {
					GroupAIncidentReport incident = (GroupAIncidentReport) offenderSegment.getParentReport();
					List<VictimSegment> victims = incident.getVictims();
					for (int i = 0; i < victims.size() && e == null; i++) {
						VictimSegment victim = victims.get(i);
						List<Integer> relatedOffenders = victim.getOffenderNumberRelatedList();
						for (int j = 0; j < relatedOffenders.size(); j++) {
							Integer relatedOffender = relatedOffenders.get(j);
							String relationship = victim.getVictimOffenderRelationship()[j];
							if (relatedOffender != null && relatedOffender == offenderSequenceNumber && relationship != null &&
									RelationshipOfVictimToOffenderCode.SE.code.equals(relationship)) {
								e = offenderSegment.getErrorTemplate();
								e.setDataElementIdentifier("37");
								e.setValue(age);
								e.setNIBRSErrorCode(NIBRSErrorCode._550);
							}
						}
					}
				}
				return e;
			}
		};
	}
	
	Rule<OffenderSegment> getRule552ForAge() {
		return new UnknownOffenderDemographicsRule("age", "37");
	}
	
	Rule<OffenderSegment> getRule552ForSex() {
		return new UnknownOffenderDemographicsRule("sex", "38");
	}
	
	Rule<OffenderSegment> getRule552ForRace() {
		return new UnknownOffenderDemographicsRule("race", "39");
	}
	
	Rule<OffenderSegment> getRule552ForEthnicity() {
		return new UnknownOffenderDemographicsRule("ethnicity", "39A");
	}
	
}
