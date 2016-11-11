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
		rulesList.add(getRule504ForSexOfOffender());
		rulesList.add(getRule504ForRaceOfOffender());
		rulesList.add(getRule504ForEthnicityOfOffender());
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

}
