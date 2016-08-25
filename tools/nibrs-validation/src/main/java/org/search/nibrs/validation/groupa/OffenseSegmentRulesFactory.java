package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenderSuspectedOfUsingCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.validation.rules.NotAllBlankRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class OffenseSegmentRulesFactory {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(OffenseSegmentRulesFactory.class);
	
	private List<Rule<OffenseSegment>> rulesList = new ArrayList<>();
	
	public OffenseSegmentRulesFactory() {
		
		rulesList.add(getRule201ForUCROffenseCode());
		rulesList.add(getRule201ForOffendersSuspectedOfUsing());
		rulesList.add(getRule201ForSingleStringProperty("offenseAttemptedCompleted", "7"));
		rulesList.add(getRule201ForSingleStringProperty("locationType", "9"));
		rulesList.add(getRule201ForStringArrayProperty("biasMotivation", "8A"));
		
		//rulesList.add(getRule204(""))
		
	}
	
	Rule<OffenseSegment> getRule201ForUCROffenseCode() {
		// weird that the 2.1 spec calls this a 201 error, not 204 like the rest of the "must be a valid value" ones, but there you go...
		return new ValidValueListRule<>("ucrOffenseCode", "6", OffenseSegment.class, NIBRSErrorCode._201, OffenseCode.codeSet(), false);
	}
	
	Rule<OffenseSegment> getRule201ForOffendersSuspectedOfUsing() {
		// weird that the 2.1 spec calls this a 201 error, not 204 like the rest of the "must be a valid value" ones, but there you go...
		return new ValidValueListRule<>("offendersSuspectedOfUsing", "8", OffenseSegment.class, NIBRSErrorCode._201, OffenderSuspectedOfUsingCode.codeSet(), false);
	}
	
	Rule<OffenseSegment> getRule201ForSingleStringProperty(String propertyName, String dataElementIdentifier) {
		return new NotBlankRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._201);
	}
	
	Rule<OffenseSegment> getRule201ForStringArrayProperty(String propertyName, String dataElementIdentifier) {
		return new NotAllBlankRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._201);
	}
	
	Rule<OffenseSegment> getRule204(String propertyName, String dataElementIdentifier, Set<String> allowedValueSet) {
		return new ValidValueListRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._204, allowedValueSet);
	}
	
	/**
	 * Get the list of rules for the administrative segment.
	 * @return the list of rules
	 */
	public List<Rule<OffenseSegment>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}

}
