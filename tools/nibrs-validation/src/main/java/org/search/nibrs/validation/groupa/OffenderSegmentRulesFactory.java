package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.EthnicityOfOffender;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.RaceOfOffenderCode;
import org.search.nibrs.model.codes.SexOfOffenderCode;
import org.search.nibrs.validation.PersonSegmentRulesFactory;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class OffenderSegmentRulesFactory {

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
		rulesList.add(sexOfOffenderValidValue504Rule());
		rulesList.add(raceOfOffenderValidValue504Rule());
		rulesList.add(ethnicityOfOffenderValidValue504Rule());
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

	public Rule<OffenderSegment> sexOfOffenderValidValue504Rule(){
		
		ValidValueListRule<OffenderSegment> validValueListRule = new ValidValueListRule<OffenderSegment>("sex", 
				"38", OffenderSegment.class, NIBRSErrorCode._504, SexOfOffenderCode.codeSet());
		
		return validValueListRule;
	}
	
	
	public Rule<OffenderSegment> raceOfOffenderValidValue504Rule(){
		
		ValidValueListRule<OffenderSegment> validValueListRule = new ValidValueListRule<OffenderSegment>("race", 
				"39", OffenderSegment.class, NIBRSErrorCode._504, RaceOfOffenderCode.codeSet());
		
		return validValueListRule;		
	}
	
	public Rule<OffenderSegment> ethnicityOfOffenderValidValue504Rule(){
		
		ValidValueListRule<OffenderSegment> validValueListRule = new ValidValueListRule<OffenderSegment>("ethnicity", 
				"39A", OffenderSegment.class, NIBRSErrorCode._504, EthnicityOfOffender.codeSet());
		
		return validValueListRule;
	}
	
}



