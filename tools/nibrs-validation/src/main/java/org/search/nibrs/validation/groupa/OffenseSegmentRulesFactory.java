package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.AutomaticWeaponIndicatorCode;
import org.search.nibrs.model.codes.BiasMotivationCode;
import org.search.nibrs.model.codes.LocationTypeCode;
import org.search.nibrs.model.codes.MethodOfEntryCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenderSuspectedOfUsingCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfCriminalActivityCode;
import org.search.nibrs.model.codes.TypeOfWeaponForceCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.ExclusiveCodedValueRule;
import org.search.nibrs.validation.rules.NotAllBlankRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.NumericValueRule;
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
		
		rulesList.add(getRule204ForValueList("biasMotivation", "8A", BiasMotivationCode.codeSet()));
		rulesList.add(getRule204ForValueList("locationType", "9", LocationTypeCode.codeSet()));
		rulesList.add(getRule204ForValueList("methodOfEntry", "11", MethodOfEntryCode.codeSet()));
		rulesList.add(getRule204ForValueList("typeOfCriminalActivity", "12", TypeOfCriminalActivityCode.codeSet()));
		rulesList.add(getRule204ForValueList("typeOfWeaponForceInvolved", "13", TypeOfWeaponForceCode.codeSet()));
		rulesList.add(getRule204ForValueList("automaticWeaponIndicator", "13", AutomaticWeaponIndicatorCode.codeSet()));
		rulesList.add(getRule204ForPremisesEntered());
		
		rulesList.add(getRule206("typeOfCriminalActivity", "12"));
		rulesList.add(getRule206("typeOfWeaponForceInvolved", "13"));
		rulesList.add(getRule206("offendersSuspectedOfUsing", "8"));
		rulesList.add(getRule206("biasMotivation", "8A"));
		
		rulesList.add(getRule207("typeOfCriminalActivity", "12", TypeOfCriminalActivityCode.noneOrUnknownValueCodeSet()));
		rulesList.add(getRule207("typeOfWeaponForceInvolved", "13", TypeOfWeaponForceCode.noneOrUnknownValueCodeSet()));
		rulesList.add(getRule207("offendersSuspectedOfUsing", "8", OffenderSuspectedOfUsingCode.noneOrUnknownValueCodeSet()));
		rulesList.add(getRule207("biasMotivation", "8A", BiasMotivationCode.noneOrUnknownValueCodeSet()));
		
	}
	
	Rule<OffenseSegment> getRule207(String propertyName, String dataElementIdentifier, Set<String> exclusiveValueSet) {
		return new ExclusiveCodedValueRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._207, exclusiveValueSet);
	}
	
	Rule<OffenseSegment> getRule206(String propertyName, String dataElementIdentifier) {
		return new DuplicateCodedValueRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._206);
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
	
	Rule<OffenseSegment> getRule204ForValueList(String propertyName, String dataElementIdentifier, Set<String> allowedValueSet) {
		return new ValidValueListRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._204, allowedValueSet);
	}
	
	Rule<OffenseSegment> getRule204ForPremisesEntered() {
		Rule<OffenseSegment> ret = new NumericValueRule<>(
				subject -> {
					return subject.getNumberOfPremisesEntered();
				},
				(value, target) -> {
					NIBRSError e = null;
					if (value != null && (0 > value.intValue() || 99 < value.intValue())) {
						e = target.getErrorTemplate();
						e.setNIBRSErrorCode(NIBRSErrorCode._204);
						e.setDataElementIdentifier("10");
						e.setValue(value);
					}
					return e;
				});
		return ret;
	}
	
	/**
	 * Get the list of rules for the administrative segment.
	 * @return the list of rules
	 */
	public List<Rule<OffenseSegment>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}

}
