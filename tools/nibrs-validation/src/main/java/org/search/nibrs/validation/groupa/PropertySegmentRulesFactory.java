package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.GroupAIncidentReport;
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
	private Set<String> zeroValuePropertyDescriptions = new HashSet<>();
	
	public PropertySegmentRulesFactory() {
		
		zeroValuePropertyDescriptions.add(PropertyDescriptionCode._09.code);
		zeroValuePropertyDescriptions.add(PropertyDescriptionCode._22.code);
		zeroValuePropertyDescriptions.add(PropertyDescriptionCode._48.code);
		zeroValuePropertyDescriptions.add(PropertyDescriptionCode._65.code);
		zeroValuePropertyDescriptions.add(PropertyDescriptionCode._66.code);
		
		rulesList.add(getRule304ForTypePropertyLoss());
		rulesList.add(getRule304ForListBoundElement("propertyDescription", "15", PropertyDescriptionCode.codeSet()));
		rulesList.add(getRule304ForListBoundElement("suspectedDrugType", "20", SuspectedDrugTypeCode.codeSet()));
		rulesList.add(getRule304ForListBoundElement("typeDrugMeasurement", "22", TypeOfDrugMeasurementCode.codeSet()));
		rulesList.add(getRule304ForStolenMotorVehicleCount());
		rulesList.add(getRule304ForRecoveredMotorVehicleCount());
		rulesList.add(getRule304ForDrugQuantity());
		rulesList.add(getRule304ForPropertyValue());
		rulesList.add(getRule305());
		rulesList.add(getRule306());
		rulesList.add(getRule320());
		rulesList.add(getRule342());
		rulesList.add(getRule351());
		rulesList.add(getRule391());
		
	}
	
	Rule<PropertySegment> getRule391() {
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				for (int i=0;i < 10;i++) {
					Integer valueOfProperty = subject.getValueOfProperty(i);
					String propertyDescription = subject.getPropertyDescription(i);
					if (valueOfProperty != null && valueOfProperty != 0 && zeroValuePropertyDescriptions.contains(propertyDescription)) {
						ret = subject.getErrorTemplate();
						ret.setNIBRSErrorCode(NIBRSErrorCode._391);
						ret.setValue(valueOfProperty);
						ret.setDataElementIdentifier("16");
					}
				}
				return ret;
			}
		};
	}
	
	Rule<PropertySegment> getRule351() {
		Set<String> allowedZeroValue = new HashSet<>();
		allowedZeroValue.addAll(zeroValuePropertyDescriptions);
		allowedZeroValue.add(PropertyDescriptionCode._77.code);
		allowedZeroValue.add(PropertyDescriptionCode._99.code);
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				for (int i=0;i < 10;i++) {
					Integer valueOfProperty = subject.getValueOfProperty(i);
					String propertyDescription = subject.getPropertyDescription(i);
					if (valueOfProperty != null && valueOfProperty == 0 && !allowedZeroValue.contains(propertyDescription)) {
						ret = subject.getErrorTemplate();
						ret.setNIBRSErrorCode(NIBRSErrorCode._351);
						ret.setValue(valueOfProperty);
						ret.setDataElementIdentifier("16");
					}
				}
				return ret;
			}
		};
	}
	
	Rule<PropertySegment> getRule342() {
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				Integer[] value = subject.getValueOfProperty();
				if (value != null) {
					for (int i=0;i < value.length;i++) {
						// since we don't have the "FBI assigned threshold", we just compare it to $1000000
						if (value[i] != null && 1000000 < value[i].intValue()) {
							ret = subject.getErrorTemplate();
							ret.setWarning(true);
							ret.setNIBRSErrorCode(NIBRSErrorCode._342);
							ret.setValue(value[i]);
							ret.setDataElementIdentifier("16");
						}
					}
				}
				return ret;
			}
		};
	}
	
	Rule<PropertySegment> getRule306() {
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				String[] suspectedDrugType = subject.getSuspectedDrugType();
				String[] typeDrugMeasurement = subject.getTypeDrugMeasurement();
				if (suspectedDrugType != null && typeDrugMeasurement != null && suspectedDrugType.length == typeDrugMeasurement.length) {
					Map<String, String> typeToMeasureMap = new HashMap<>();
					for (int i=0;i < suspectedDrugType.length;i++) {
						String t = suspectedDrugType[i];
						String m = typeDrugMeasurement[i];
						if (t != null && m != null) {
							if (typeToMeasureMap.keySet().contains(t) && m.equals(typeToMeasureMap.get(t))) {
								ret = subject.getErrorTemplate();
								ret.setDataElementIdentifier("20");
								ret.setValue(t);
								ret.setNIBRSErrorCode(NIBRSErrorCode._306);
								break;
							}
						}
						typeToMeasureMap.put(t, m);
						if (typeToMeasureMap.size() > 1 && typeToMeasureMap.keySet().contains(SuspectedDrugTypeCode._U.code)) {
							ret = subject.getErrorTemplate();
							ret.setDataElementIdentifier("20");
							ret.setValue(t);
							ret.setNIBRSErrorCode(NIBRSErrorCode._306);
							break;
						}
					}
				}
				return ret;
			}
		};
	}
	
	Rule<PropertySegment> getRule320() {
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				GroupAIncidentReport parentIncident = (GroupAIncidentReport) subject.getParentReport();
				Date incidentDate = parentIncident.getIncidentDate();
				for (int i=0;i < 10;i++) {
					Date recoveredDate = subject.getDateRecovered(i);
					if (recoveredDate != null) {
						if (incidentDate != null && recoveredDate.before(incidentDate)) {
							ret = subject.getErrorTemplate();
							ret.setDataElementIdentifier("17");
							ret.setValue(recoveredDate);
							ret.setNIBRSErrorCode(NIBRSErrorCode._320);
						}
					}
				}
				return ret;
			}
		};
	}
	
	Rule<PropertySegment> getRule305() {
		return new Rule<PropertySegment>() {
			@Override
			public NIBRSError apply(PropertySegment subject) {
				NIBRSError ret = null;
				GroupAIncidentReport parentIncident = (GroupAIncidentReport) subject.getParentReport();
				Date incidentDate = parentIncident.getIncidentDate();
				Integer monthOfTape = parentIncident.getMonthOfTape();
				Integer yearOfTape = parentIncident.getYearOfTape();
				Calendar c = Calendar.getInstance();
				for (int i=0;i < 10;i++) {
					Date recoveredDate = subject.getDateRecovered(i);
					if (recoveredDate != null) {
						if (incidentDate != null && recoveredDate.before(incidentDate)) {
							ret = subject.getErrorTemplate();
							ret.setDataElementIdentifier("17");
							ret.setValue(recoveredDate);
							ret.setNIBRSErrorCode(NIBRSErrorCode._305);
						} else if (monthOfTape != null && yearOfTape != null) {
							int y = yearOfTape.intValue();
							int m = monthOfTape.intValue();
							if (m == 12) {
								y++;
								m = 1;
							} else {
								m++;
							}
							c.set(y, m-1, 1);
							c.add(Calendar.DAY_OF_MONTH, -1);
							Date submissionDate = c.getTime();
							if (recoveredDate.after(submissionDate)) {
								ret = subject.getErrorTemplate();
								ret.setDataElementIdentifier("17");
								ret.setValue(recoveredDate);
								ret.setNIBRSErrorCode(NIBRSErrorCode._305);
							}
						}
					}
				}
				return ret;
			}
		};
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
