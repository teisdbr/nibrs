package org.search.nibrs.validation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.ArresteeWasArmedWithCode;
import org.search.nibrs.model.codes.AutomaticWeaponIndicatorCode;
import org.search.nibrs.model.codes.MultipleArresteeSegmentsIndicator;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfArrestCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.ExclusiveCodedValueRule;
import org.search.nibrs.validation.rules.NotAllBlankRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.NullObjectRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidNIBRSIdentifierFormatRule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class ArresteeSegmentRulesFactory {
	
	private static final Logger LOG = LogManager.getLogger(ArresteeSegmentRulesFactory.class);
	
	public static final String GROUP_A_ARRESTEE_MODE = "group-a-arrestee";
	public static final String GROUP_B_ARRESTEE_MODE = "group-b-arrestee";

	private PersonSegmentRulesFactory<ArresteeSegment> personSegmentRulesFactory;
	private List<Rule<ArresteeSegment>> rulesList;
	private String mode;
	
	public static final ArresteeSegmentRulesFactory instance(String mode) {
		return new ArresteeSegmentRulesFactory(mode);
	}	
	
	public List<Rule<ArresteeSegment>> getRulesList() {
		return rulesList;
	}	
	
	private ArresteeSegmentRulesFactory(String mode) {
		personSegmentRulesFactory = new PersonSegmentRulesFactory<ArresteeSegment>(ArresteeSegment.class);
		rulesList = new ArrayList<Rule<ArresteeSegment>>();
		this.mode = mode;
		initRules(rulesList);
	}
	
	private boolean isGroupAMode() {
		return GROUP_A_ARRESTEE_MODE.equals(mode);
	}
	
	private void initRules(List<Rule<ArresteeSegment>> rulesList) {
		rulesList.add(getRuleX01ForSequenceNumber());
		rulesList.add(getRuleX01ForArrestTransactionNumber());
		rulesList.add(getRuleX15());
		rulesList.add(getRuleX17());
		rulesList.add(getRuleX01ForArrestDate());
		rulesList.add(getRule665());
		rulesList.add(getRuleX01ForTypeOfArrest());
		rulesList.add(getRuleX01ForMultipleArresteeIndicator());
		rulesList.add(getRuleX01ForUCRArrestOffenseCode());
		rulesList.add(getRule670());
		rulesList.add(getRule760());
		rulesList.add(getRuleX01ForArresteeWasArmedWith());
		rulesList.add(getRuleX06ForArresteeWasArmedWith());
		rulesList.add(getRuleX07ForArresteeWasArmedWith());
		rulesList.add(getRuleX54());
		rulesList.add(getRuleX55());
		rulesList.add(getRuleX10());
		rulesList.add(getRuleX22());
		rulesList.add(getRuleX01ForAge());
		rulesList.add(getRule761());
		rulesList.add(getRuleX01ForSex());
		rulesList.add(getRuleX01ForRace());
		rulesList.add(getRuleX04ForEthnicity());
		rulesList.add(getRuleX04ForResidentStatus());

//		rulesList.add(sexOfArresteeNotBlank601Rule());
//		rulesList.add(sexOfArresteeValidValue667Rule());
//		rulesList.add(raceOfArresteeNotBlank601Rule());
//		rulesList.add(ethnicityOfArresteeNotBlank604Rule());
//		rulesList.add(residentStatusOfArresteeNotBlank604Code());
//		rulesList.add(dispositionOfArresteeUnder18ValidValue604Rule());
	}
	
	Rule<ArresteeSegment> getRuleX01ForSequenceNumber() {
		return new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {

				Integer arresteeSequenceNumber = arresteeSegment.getArresteeSequenceNumber();
				NIBRSError e = null;

				if (arresteeSequenceNumber == null || arresteeSequenceNumber < 1 || arresteeSequenceNumber > 99) {
					e = arresteeSegment.getErrorTemplate();
					e.setNIBRSErrorCode(isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
					e.setDataElementIdentifier("40");
					e.setValue(arresteeSequenceNumber);
				}

				return e;

			}
		};
	}
	
	Rule<ArresteeSegment> getRuleX01ForArrestTransactionNumber() {
		return new NotBlankRule<ArresteeSegment>("arrestTransactionNumber", "41", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
	}
	
	Rule<ArresteeSegment> getRuleX17() {
		return new ValidNIBRSIdentifierFormatRule<>("41", isGroupAMode() ? NIBRSErrorCode._617 : NIBRSErrorCode._717);
	}
	
	Rule<ArresteeSegment> getRuleX15() {
		return new ValidNIBRSIdentifierFormatRule<>("41", isGroupAMode() ? NIBRSErrorCode._615 : NIBRSErrorCode._715);
	}
	
	Rule<ArresteeSegment> getRuleX01ForArrestDate() {
		return new NotBlankRule<ArresteeSegment>("arrestDate", "42", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
	}
	
	Rule<ArresteeSegment> getRuleX01ForArresteeWasArmedWith() {
		return new NotAllBlankRule<ArresteeSegment>("arresteeArmedWith", "46", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
	}
	
	Rule<ArresteeSegment> getRuleX06ForArresteeWasArmedWith() {
		return new DuplicateCodedValueRule<ArresteeSegment>("arresteeArmedWith", "46", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._606 : NIBRSErrorCode._706);
	}
	
	Rule<ArresteeSegment> getRuleX07ForArresteeWasArmedWith() {
		Set<String> exclusiveSet = new HashSet<>();
		exclusiveSet.add(ArresteeWasArmedWithCode._01.code);
		return new ExclusiveCodedValueRule<ArresteeSegment>("arresteeArmedWith", "46", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._607 : NIBRSErrorCode._707, exclusiveSet);
	}
	
	Rule<ArresteeSegment> getRuleX05() {
		return new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {
				NIBRSError e = null;
				AbstractReport parent = arresteeSegment.getParentReport();
				Integer yearOfTape = parent.getYearOfTape();
				Integer monthOfTape = parent.getMonthOfTape();
				Date arrestDateD = arresteeSegment.getArrestDate();
				if (monthOfTape != null && monthOfTape > 0 && monthOfTape < 13 && yearOfTape != null && arrestDateD != null) {
					Calendar c = Calendar.getInstance();
					c.set(yearOfTape, monthOfTape-1, 1);
					LocalDate compDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1);
					compDate = compDate.plusMonths(1).minusDays(1);
					c.setTime(arrestDateD);
					LocalDate arrestDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
					if (compDate.isBefore(arrestDate)) {
						e = arresteeSegment.getErrorTemplate();
						e.setNIBRSErrorCode(isGroupAMode() ? NIBRSErrorCode._605 : NIBRSErrorCode._705);
						e.setDataElementIdentifier("42");
						e.setValue(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
					}
				}
				return e;
			}
		};
	}
	
	Rule<ArresteeSegment> getRule665() {
		return new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {
				NIBRSError e = null;
				GroupAIncidentReport parent = (GroupAIncidentReport) arresteeSegment.getParentReport();
				Date incidentDateD = parent.getIncidentDate();
				Date arrestDateD = arresteeSegment.getArrestDate();
				if (incidentDateD != null && arrestDateD != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(arrestDateD);
					LocalDate arrestDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
					c.setTime(incidentDateD);
					LocalDate incidentDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
					if (arrestDate.isAfter(incidentDate)) {
						e = arresteeSegment.getErrorTemplate();
						e.setNIBRSErrorCode(NIBRSErrorCode._665);
						e.setDataElementIdentifier("42");
						e.setValue(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
					}
				}
				return e;
			}
		};
	}
	
	Rule<ArresteeSegment> getRuleX01ForTypeOfArrest() {
		return new ValidValueListRule<ArresteeSegment>("typeOfArrest", "43", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701, TypeOfArrestCode.codeSet(), false);
	}
	
	Rule<ArresteeSegment> getRuleX01ForMultipleArresteeIndicator() {
		return isGroupAMode() ?
				new ValidValueListRule<ArresteeSegment>("multipleArresteeSegmentsIndicator", "44", ArresteeSegment.class, NIBRSErrorCode._601, MultipleArresteeSegmentsIndicator.codeSet(), false) :
				new NullObjectRule<>();
	}
	
	Rule<ArresteeSegment> getRuleX01ForUCRArrestOffenseCode() {
		return new ValidValueListRule<ArresteeSegment>("ucrArrestOffenseCode", "45", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701, OffenseCode.codeSet(), false);
	}
	
	Rule<ArresteeSegment> getRule670() {
		return new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {
				NIBRSError e = null;
				String offenseCode = arresteeSegment.getUcrArrestOffenseCode();
				if (OffenseCode._09C.code.equals(offenseCode)) {
					e = arresteeSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._670);
					e.setDataElementIdentifier("45");
					e.setValue(offenseCode);
				}
				return e;
			}
		};
	}
	
	Rule<ArresteeSegment> getRule760() {
		return new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {
				NIBRSError e = null;
				String offenseCodeS = arresteeSegment.getUcrArrestOffenseCode();
				OffenseCode offenseCode = OffenseCode.forCode(offenseCodeS);
				if (arresteeSegment.isGroupB() && !offenseCode.group.equals("B")) {
					e = arresteeSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._760);
					e.setDataElementIdentifier("45");
					e.setValue(offenseCodeS);
				}
				return e;
			}
		};
	}
	
	// note: rule 604/704 is unenforceable.  there is no way to distinguish between a missing automatic weapon indicator and a valid blank (since blank is,
	//  for whatever reason, a valid value...)
	
	Rule<ArresteeSegment> getRuleX55() {
		return new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {
				NIBRSError e = null;
				String[] arresteeArmedWith = arresteeSegment.getArresteeArmedWith();
				for (int i = 0; i < arresteeArmedWith.length; i++) {
					String aaw = arresteeArmedWith[i];
					ArresteeWasArmedWithCode code = ArresteeWasArmedWithCode.forCode(aaw);
					if ((code == null || !code.isFirearm()) && AutomaticWeaponIndicatorCode.A.code.equals(arresteeSegment.getAutomaticWeaponIndicator(i))) {
						e = arresteeSegment.getErrorTemplate();
						e.setNIBRSErrorCode(isGroupAMode() ? NIBRSErrorCode._655 : NIBRSErrorCode._755);
						e.setDataElementIdentifier("46");
						e.setValue(code);
						break;
					}
				}
				return e;
			}
		};
	}
	
	Rule<ArresteeSegment> getRuleX54() {
		return new ValidValueListRule<ArresteeSegment>("automaticWeaponIndicator", "46", ArresteeSegment.class, isGroupAMode() ? NIBRSErrorCode._654 : NIBRSErrorCode._754, AutomaticWeaponIndicatorCode.codeSet());
	}
	
	Rule<ArresteeSegment> getRuleX10() {
		return personSegmentRulesFactory.getProperAgeRangeRule("47", isGroupAMode() ? NIBRSErrorCode._610 : NIBRSErrorCode._710);
	}
	
	Rule<ArresteeSegment> getRuleX22() {
		return personSegmentRulesFactory.getNonZeroAgeRangeMinimumRule("47", isGroupAMode() ? NIBRSErrorCode._622 : NIBRSErrorCode._752);
	}

	Rule<ArresteeSegment> getRuleX01ForAge() {
		return personSegmentRulesFactory.getAgeValidNonBlankRule("47", isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
	}
	
	Rule<ArresteeSegment> getRule761() {
		return isGroupAMode() ? new NullObjectRule<>() : new Rule<ArresteeSegment>() {
			@Override
			public NIBRSError apply(ArresteeSegment arresteeSegment) {
				NIBRSError e = null;
				NIBRSAge age = arresteeSegment.getAge();
				if (OffenseCode._90I.code.equals(arresteeSegment.getUcrArrestOffenseCode()) && (age == null || age.isNonNumeric() || age.getAgeMin() > 17)) {
					e = arresteeSegment.getErrorTemplate();
					e.setNIBRSErrorCode(NIBRSErrorCode._761);
					e.setDataElementIdentifier("47");
					e.setValue(age);
				}
				return e;
			}
		};
	}
	
	Rule<ArresteeSegment> getRuleX01ForSex() {
		return personSegmentRulesFactory.getSexValidNonBlankRule("48", isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
	}

	Rule<ArresteeSegment> getRuleX01ForRace() {
		return personSegmentRulesFactory.getRaceValidNonBlankRule("49", isGroupAMode() ? NIBRSErrorCode._601 : NIBRSErrorCode._701);
	}

	Rule<ArresteeSegment> getRuleX04ForEthnicity() {
		return personSegmentRulesFactory.getEthnicityValidNonBlankRule("50", isGroupAMode() ? NIBRSErrorCode._604 : NIBRSErrorCode._704, true);
	}

	Rule<ArresteeSegment> getRuleX04ForResidentStatus(){
		return personSegmentRulesFactory.getResidentStatusValidNonBlankRule("51", isGroupAMode() ? NIBRSErrorCode._604 : NIBRSErrorCode._704, true);
	}
	
}
