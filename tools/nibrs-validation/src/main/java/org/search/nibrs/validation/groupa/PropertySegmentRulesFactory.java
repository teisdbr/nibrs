package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.PropertyDescriptionCode;
import org.search.nibrs.model.codes.SuspectedDrugTypeCode;
import org.search.nibrs.model.codes.TypeOfDrugMeasurementCode;
import org.search.nibrs.model.codes.TypeOfPropertyLossCode;
import org.search.nibrs.validation.rules.NumericValueRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class PropertySegmentRulesFactory {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(PropertySegmentRulesFactory.class);
	
	private List<Rule<PropertySegment>> rulesList = new ArrayList<>();
	
	public PropertySegmentRulesFactory() {
		
		rulesList.add(getRule304ForTypePropertyLoss());
		rulesList.add(getRule304ForListBoundElement("propertyDescription", "15", PropertyDescriptionCode.codeSet()));
		rulesList.add(getRule304ForListBoundElement("suspectedDrugType", "20", SuspectedDrugTypeCode.codeSet()));
		rulesList.add(getRule304ForListBoundElement("typeDrugMeasurement", "22", TypeOfDrugMeasurementCode.codeSet()));
		rulesList.add(getRule304ForStolenMotorVehicleCount());
		rulesList.add(getRule304ForRecoveredMotorVehicleCount());
		rulesList.add(getRule304ForDrugQuantity());
		rulesList.add(getRule304ForPropertyValue());
		
	}
	
	Rule<PropertySegment> getRule304ForPropertyValue() {
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				for (int i=0;i < 10;i++) {
					Integer propertyValue = subject.getValueOfProperty(i);
					if (propertyValue != null) {
						int v = propertyValue.intValue();
						if (v < 0 || v > 999999999) {
							ret = subject.getErrorTemplate();
							ret.setDataElementIdentifier("16");
							ret.setValue(v);
							ret.setNIBRSErrorCode(NIBRSErrorCode._304);
						}
					}
				}
				return ret;
			}
		};
	}
	
	Rule<PropertySegment> getRule304ForDrugQuantity() {
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				for (int i=0;i < 3;i++) {
					Double drugQuantity = subject.getEstimatedDrugQuantity(i);
					if (drugQuantity != null) {
						double d = drugQuantity.doubleValue();
						if (d < 0 || d > 100000000) {
							ret = subject.getErrorTemplate();
							ret.setDataElementIdentifier("21");
							ret.setValue(drugQuantity);
							ret.setNIBRSErrorCode(NIBRSErrorCode._304);
						}
					}
				}
				return ret;
			}
		};
	}
	
	Rule<PropertySegment> getRule304ForRecoveredMotorVehicleCount() {
		return new NumericValueRule<>(
				subject -> {
					return subject.getNumberOfRecoveredMotorVehicles();
				},
				(value, target) -> {
					return getErrorForMotorVehicleCountValue(value, target, "19");
				});
	}

	Rule<PropertySegment> getRule304ForStolenMotorVehicleCount() {
		return new NumericValueRule<>(
				subject -> {
					return subject.getNumberOfStolenMotorVehicles();
				},
				(value, target) -> {
					return getErrorForMotorVehicleCountValue(value, target, "18");
				});
	}

	Rule<PropertySegment> getRule304ForTypePropertyLoss() {
		return new ValidValueListRule<PropertySegment>("typeOfPropertyLoss", "14", PropertySegment.class, NIBRSErrorCode._304, TypeOfPropertyLossCode.codeSet(), false);
	}
	
	Rule<PropertySegment> getRule304ForListBoundElement(String propertyName, String dataElementIdentifier, Set<String> allowedCodeSet) {
		return new ValidValueListRule<PropertySegment>(propertyName, dataElementIdentifier, PropertySegment.class, NIBRSErrorCode._304, allowedCodeSet);
	}
	
	/**
	 * Get the list of rules for the property segment.
	 * @return the list of rules
	 */
	public List<Rule<PropertySegment>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}

	private static final NIBRSError getErrorForMotorVehicleCountValue(Number value, ValidationTarget target, String dataElementIdentifier) {
		NIBRSError ret = null;
		if (value != null && (value.intValue() < 0 || value.intValue() > 99)) {
			ret = target.getErrorTemplate();
			ret.setNIBRSErrorCode(NIBRSErrorCode._304);
			ret.setDataElementIdentifier(dataElementIdentifier);
			ret.setValue(value);
		}
		return ret;
	}
	
}
