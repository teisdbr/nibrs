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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.codes.CargoTheftIndicatorCode;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.PropertyDescriptionCode;
import org.search.nibrs.validation.rules.BlankRightFillStringRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.NumericValueRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.StringValueRule;
import org.search.nibrs.validation.rules.ValidNIBRSIdentifierFormatRule;
import org.search.nibrs.validation.rules.ValidValueListRule;

/**
 * Factory class that provides Rule implementations to validate the elements contained on the Group A report administrative segment.
 */
public class GroupAIncidentReportRulesFactory {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(GroupAIncidentReportRulesFactory.class);
	
	private static abstract class IncidentDateRule implements Rule<GroupAIncidentReport> {
		@Override
		public NIBRSError apply(GroupAIncidentReport subject) {
			NIBRSError ret = null;
			Integer month = subject.getMonthOfTape();
			Integer year = subject.getYearOfTape();
			if (month != null && month > 0 && month < 13 && year != null) {
				if (month == 12) {
					month = 1;
					year++;
				} else {
					month++;
				}
				Date incidentDate = subject.getIncidentDate();
				if (incidentDate != null) {
					ret = compareIncidentDateToTape(month, year, incidentDate, subject.getErrorTemplate());
				}
			}
			return ret;
		}

		protected abstract NIBRSError compareIncidentDateToTape(Integer month, Integer year, Date incidentDate, NIBRSError errorTemplate);
		
	}

	private List<Rule<GroupAIncidentReport>> rulesList = new ArrayList<>();
	private Set<String> cargoTheftOffenses = new HashSet<>();
	private Set<String> trueExceptionalClearanceCodes = new HashSet<>();
	
	public GroupAIncidentReportRulesFactory() {
		
		cargoTheftOffenses.add(OffenseCode._120.code);
		cargoTheftOffenses.add(OffenseCode._210.code);
		cargoTheftOffenses.add(OffenseCode._220.code);
		cargoTheftOffenses.add(OffenseCode._23D.code);
		cargoTheftOffenses.add(OffenseCode._23F.code);
		cargoTheftOffenses.add(OffenseCode._23H.code);
		cargoTheftOffenses.add(OffenseCode._240.code);
		cargoTheftOffenses.add(OffenseCode._26A.code);
		cargoTheftOffenses.add(OffenseCode._26B.code);
		cargoTheftOffenses.add(OffenseCode._26C.code);
		cargoTheftOffenses.add(OffenseCode._26E.code);
		cargoTheftOffenses.add(OffenseCode._510.code);
		cargoTheftOffenses.add(OffenseCode._270.code);
		
		trueExceptionalClearanceCodes = ClearedExceptionallyCode.codeSet();
		trueExceptionalClearanceCodes.remove(ClearedExceptionallyCode.N.code);
		
		rulesList.add(getRule101("ori", "1"));
		rulesList.add(getRule101("incidentNumber", "2"));
		rulesList.add(getRule101("yearOfTape", "Year of Tape"));
		rulesList.add(getRule101("monthOfTape", "Month of Tape"));
		rulesList.add(getRule101("incidentDate", "3"));
		rulesList.add(getRule101("exceptionalClearanceCode", "4"));
		rulesList.add(getRule104("reportDateIndicator"));
		rulesList.add(getRule104("yearOfTape"));
		rulesList.add(getRule104("monthOfTape"));
		rulesList.add(getRule104("cargoTheftIndicator"));
		rulesList.add(getRule115());
		rulesList.add(getRule117());
		rulesList.add(getRule119());
		rulesList.add(getRule152());
		rulesList.add(getRule153());
		rulesList.add(getRule155());
		rulesList.add(getRule156());
		rulesList.add(getRule170());
		rulesList.add(getRule171());
		rulesList.add(getRule172());
		rulesList.add(getRule072());
		rulesList.add(getRule073());
		
	}
	
	Rule<GroupAIncidentReport> getRule073() {
		return new Rule<GroupAIncidentReport>() {
			@Override
			public NIBRSError apply(GroupAIncidentReport subject) {
				NIBRSError ret = null;
				PropertySegment recoveredSegment = subject.getRecoveredPropertySegment();
				PropertySegment stolenSegment = subject.getStolenPropertySegment();
				if (recoveredSegment != null && recoveredSegment.getNumberOfRecoveredMotorVehicles() != null &&
						(stolenSegment == null || stolenSegment.getNumberOfStolenMotorVehicles() < recoveredSegment.getNumberOfRecoveredMotorVehicles())) {
					ret = subject.getErrorTemplate();
					ret.setValue(recoveredSegment.getNumberOfRecoveredMotorVehicles());
					ret.setDataElementIdentifier("19");
					ret.setNIBRSErrorCode(NIBRSErrorCode._073);
				}
				return ret;
			}
		};
	}
	
	Rule<GroupAIncidentReport> getRule072() {
		return new Rule<GroupAIncidentReport>() {
			@Override
			public NIBRSError apply(GroupAIncidentReport subject) {
				NIBRSError ret = null;
				PropertySegment recoveredSegment = subject.getRecoveredPropertySegment();
				PropertySegment stolenSegment = subject.getStolenPropertySegment();
				if (recoveredSegment != null && subject.getOffenseForOffenseCode(OffenseCode._250.code) == null && subject.getOffenseForOffenseCode(OffenseCode._280.code) == null) {
					List<String> recoveredPropertyTypes = new ArrayList<>();
					recoveredPropertyTypes.addAll(Arrays.asList(recoveredSegment.getPropertyDescription()));
					recoveredPropertyTypes.removeIf(element -> element == null);
					List<String> stolenPropertyDescriptionList = stolenSegment == null ? new ArrayList<String>() : Arrays.asList(stolenSegment.getPropertyDescription());
					recoveredPropertyTypes.removeAll(stolenPropertyDescriptionList);
					if (stolenPropertyDescriptionList.contains(PropertyDescriptionCode._03.code) || stolenPropertyDescriptionList.contains(PropertyDescriptionCode._05.code)) {
						recoveredPropertyTypes.remove(PropertyDescriptionCode._38.code);
					}
					if (!recoveredPropertyTypes.isEmpty()) {
						ret = subject.getErrorTemplate();
						ret.setValue(recoveredPropertyTypes);
						ret.setDataElementIdentifier("15");
						ret.setNIBRSErrorCode(NIBRSErrorCode._072);
					}
				}
				return ret;
			}
		};
	}
	
	Rule<GroupAIncidentReport> getRule155() {
		return new Rule<GroupAIncidentReport>() {
			@Override
			public NIBRSError apply(GroupAIncidentReport subject) {
				NIBRSError ret = null;
				Date exceptionalClearanceDate = subject.getExceptionalClearanceDate();
				Date incidentDate = subject.getIncidentDate();
				if (exceptionalClearanceDate != null && incidentDate != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(incidentDate);
					LocalDate incidentLocalDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
					c.setTime(exceptionalClearanceDate);
					LocalDate exceptionalClearanceLocalDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
					if (exceptionalClearanceLocalDate.isBefore(incidentLocalDate)) {
						ret = subject.getErrorTemplate();
						ret.setValue(subject.getExceptionalClearanceDate());
						ret.setDataElementIdentifier("5");
						ret.setNIBRSErrorCode(NIBRSErrorCode._155);
					}
				}
				return ret;
			}
		};
	}
	
	Rule<GroupAIncidentReport> getRule156() {
		return new Rule<GroupAIncidentReport>() {
			@Override
			public NIBRSError apply(GroupAIncidentReport subject) {
				NIBRSError ret = null;
				if (subject.getExceptionalClearanceDate() == null && trueExceptionalClearanceCodes.contains(subject.getExceptionalClearanceCode())) {
					ret = subject.getErrorTemplate();
					ret.setValue(subject.getExceptionalClearanceCode());
					ret.setDataElementIdentifier("5");
					ret.setNIBRSErrorCode(NIBRSErrorCode._156);
				}
				return ret;
			}
		};
	}
	
	Rule<GroupAIncidentReport> getRule153() {
		return new Rule<GroupAIncidentReport>() {
			@Override
			public NIBRSError apply(GroupAIncidentReport subject) {
				NIBRSError ret = null;
				if (subject.getExceptionalClearanceDate() != null && "N".equals(subject.getExceptionalClearanceCode())) {
					ret = subject.getErrorTemplate();
					ret.setValue(subject.getExceptionalClearanceCode());
					ret.setDataElementIdentifier("4");
					ret.setNIBRSErrorCode(NIBRSErrorCode._153);
				}
				return ret;
			}
		};
	}
	
	Rule<GroupAIncidentReport> getRule172() {
		
		return new IncidentDateRule() {
			protected NIBRSError compareIncidentDateToTape(Integer month, Integer year, Date incidentDate, NIBRSError errorTemplate) {
				LocalDate fbiNIBRSStartDate = LocalDate.of(1991, 1, 1);
				Calendar c = Calendar.getInstance();
				c.setTime(incidentDate);
				LocalDate incidentLocalDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
				NIBRSError e = null;
				if (incidentLocalDate.isBefore(fbiNIBRSStartDate)) {
					e = errorTemplate;
					e.setDataElementIdentifier("3");
					e.setNIBRSErrorCode(NIBRSErrorCode._172);
					e.setValue(incidentDate);
				}
				return e;
			}
		};
		
	}
	
	Rule<GroupAIncidentReport> getRule171() {
		
		return new IncidentDateRule() {
			protected NIBRSError compareIncidentDateToTape(Integer month, Integer year, Date incidentDate, NIBRSError errorTemplate) {
				LocalDate priorYearStartDate = LocalDate.of(year-1, 1, 1);
				Calendar c = Calendar.getInstance();
				c.setTime(incidentDate);
				LocalDate incidentLocalDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
				NIBRSError e = null;
				if (incidentLocalDate.isBefore(priorYearStartDate)) {
					e = errorTemplate;
					e.setDataElementIdentifier("3");
					e.setNIBRSErrorCode(NIBRSErrorCode._171);
					e.setValue(incidentDate);
				}
				return e;
			}
		};
		
	}
	
	Rule<GroupAIncidentReport> getRule170() {
		
		return new IncidentDateRule() {
			protected NIBRSError compareIncidentDateToTape(Integer month, Integer year, Date incidentDate, NIBRSError errorTemplate) {
				LocalDate submissionDate = LocalDate.of(year, month, 1).minusDays(1);
				Calendar c = Calendar.getInstance();
				c.setTime(incidentDate);
				LocalDate incidentLocalDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
				NIBRSError e = null;
				if (incidentLocalDate.isAfter(submissionDate)) {
					e = errorTemplate;
					e.setDataElementIdentifier("3");
					e.setNIBRSErrorCode(NIBRSErrorCode._170);
					e.setValue(incidentDate);
				}
				return e;
			}
		};
		
	}
	
	Rule<GroupAIncidentReport> getRule119() {
		
		Rule<GroupAIncidentReport> ret = new Rule<GroupAIncidentReport>() {
			@Override
			public NIBRSError apply(GroupAIncidentReport subject) {
				List<OffenseSegment> offenses = subject.getOffenses();
				boolean cargoTheftIncident = false;
				for (OffenseSegment o : offenses) {
					if (cargoTheftOffenses.contains(o.getUcrOffenseCode())) {
						cargoTheftIncident = true;
						break;
					}
				}
				NIBRSError ret = null;
				if (cargoTheftIncident && subject.getCargoTheftIndicator() == null) {
					ret = subject.getErrorTemplate();
					ret.setValue(null);
					ret.setDataElementIdentifier("2A");
					ret.setNIBRSErrorCode(NIBRSErrorCode._119);
				}
				return ret;
			}
		};
		
		return ret;
		
	}
	
	Rule<GroupAIncidentReport> getRule117() {
		return new ValidNIBRSIdentifierFormatRule<>("2", NIBRSErrorCode._117);
	}

	Rule<GroupAIncidentReport> getRule104(String propertyName) {
		Rule<GroupAIncidentReport> ret = null;
		if ("reportDateIndicator".equals(propertyName)) {
			ret = new StringValueRule<>(
					subject -> {
						return subject.getReportDateIndicator();
					},
					(value, target) -> {
						NIBRSError e = null;
						if (value != null && (!value.equals("R"))) {
							e = target.getErrorTemplate();
							e.setNIBRSErrorCode(NIBRSErrorCode._104);
							e.setDataElementIdentifier("3");
							e.setValue(value);
						}
						return e;
					});
		} else if ("yearOfTape".equals(propertyName)) {
			ret = new NumericValueRule<>(
					subject -> {
						return subject.getYearOfTape();
					},
					(value, target) -> {
						NIBRSError e = null;
						if (value != null && 1991 > value.intValue()) {
							e = target.getErrorTemplate();
							e.setNIBRSErrorCode(NIBRSErrorCode._104);
							e.setDataElementIdentifier("Year of Tape");
							e.setValue(value);
						}
						return e;
					});
		} else if ("monthOfTape".equals(propertyName)) {
			ret = new NumericValueRule<>(
					subject -> {
						return subject.getMonthOfTape();
					},
					(value, target) -> {
						NIBRSError e = null;
						if (value != null && (1 > value.intValue() || 12 < value.intValue())) {
							e = target.getErrorTemplate();
							e.setNIBRSErrorCode(NIBRSErrorCode._104);
							e.setDataElementIdentifier("Month of Tape");
							e.setValue(value);
						}
						return e;
					});
		} else if ("cargoTheftIndicator".equals(propertyName)) {
			ret = new ValidValueListRule<GroupAIncidentReport>(propertyName, "2A", GroupAIncidentReport.class, NIBRSErrorCode._104, CargoTheftIndicatorCode.codeSet(), false) {
				protected boolean ignore(GroupAIncidentReport r) {
					return !r.includesCargoTheft();
				}
			};
		}
		return ret;
	}
	
	Rule<GroupAIncidentReport> getRule152() {
		return new NumericValueRule<>(subject -> {
			return subject.getIncidentHour();
		} , (value, target) -> {
			NIBRSError e = null;
			if (value != null && (0 > value.intValue() || 23 < value.intValue())) {
				e = target.getErrorTemplate();
				e.setNIBRSErrorCode(NIBRSErrorCode._152);
				e.setDataElementIdentifier("3");
				e.setValue(value);
			}
			return e;
		});
	}
	
	Rule<GroupAIncidentReport> getRule101(String propertyName, String dataElementIdentifier) {
		if ("exceptionalClearanceCode".equals(propertyName)) {
			return new ValidValueListRule<GroupAIncidentReport>(propertyName, dataElementIdentifier, GroupAIncidentReport.class,
					NIBRSErrorCode._101, ClearedExceptionallyCode.codeSet(), false);
		}
		return new NotBlankRule<>(propertyName, dataElementIdentifier, GroupAIncidentReport.class, NIBRSErrorCode._101);
	}

	Rule<GroupAIncidentReport> getRule115() {
		return new BlankRightFillStringRule<>("2", NIBRSErrorCode._115);
	}

	/**
	 * Get the list of rules for the administrative segment.
	 * @return the list of rules
	 */
	public List<Rule<GroupAIncidentReport>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}

}
