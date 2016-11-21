package org.search.nibrs.validation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.DispositionOfArresteeUnder18Code;
import org.search.nibrs.model.codes.EthnicityOfArrestee;
import org.search.nibrs.model.codes.MultipleArresteeSegmentsIndicator;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.RaceOfArresteeCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexOfArresteeCode;
import org.search.nibrs.model.codes.TypeOfArrestCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
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

	private List<Rule<ArresteeSegment>> rulesList;
	private String mode;
	
	public static final ArresteeSegmentRulesFactory instance(String mode) {
		return new ArresteeSegmentRulesFactory(mode);
	}	
	
	public List<Rule<ArresteeSegment>> getRulesList() {
		return rulesList;
	}	
	
	private ArresteeSegmentRulesFactory(String mode) {
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
		
//		rulesList.add(usrArrestOffenseCode());
//		rulesList.add(arresteeWasArmedWithNotAllEmpty601Rule());
//		rulesList.add(arresteeWasArmedWithNoDuplicates606Rule());
//		rulesList.add(ageOfArresteeNotBlank601Rule());
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
	
	private Rule<ArresteeSegment> arresteeWasArmedWithNotAllEmpty601Rule(){
		
		NotAllBlankRule<ArresteeSegment> notAllBlankRule = new NotAllBlankRule<ArresteeSegment>("arresteeArmedWith", 
				"46", ArresteeSegment.class, NIBRSErrorCode._601);
						
		return notAllBlankRule;
	}
	
	
	private Rule<ArresteeSegment> arresteeWasArmedWithNoDuplicates606Rule(){
		
		DuplicateCodedValueRule<ArresteeSegment> duplicateValueRule = new DuplicateCodedValueRule<ArresteeSegment>("arresteeArmedWith", 
				"46", ArresteeSegment.class, NIBRSErrorCode._606);
		
		return duplicateValueRule;
	}
	
	public Rule<ArresteeSegment> autoWeaponIndicatorNotBlank604Rule(){
		//TODO depends on 3rd element
		return null;
	}
	
	public Rule<ArresteeSegment> ageOfArresteeNotBlank601Rule(){
		
		//TODO validate range
		
		NotBlankRule<ArresteeSegment> notBlankRule = new NotBlankRule<ArresteeSegment>("age", 
				"47", ArresteeSegment.class, NIBRSErrorCode._601);
		
		return notBlankRule;
	}
	
	public Rule<ArresteeSegment> sexOfArresteeNotBlank601Rule(){
		
		NotBlankRule<ArresteeSegment> notBlankRule = new NotBlankRule<ArresteeSegment>("sex", 
				"49", ArresteeSegment.class, NIBRSErrorCode._601);
		
		return notBlankRule;
	}
	
	public Rule<ArresteeSegment> sexOfArresteeValidValue667Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("sex", 
		"49", ArresteeSegment.class, NIBRSErrorCode._667, SexOfArresteeCode.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<ArresteeSegment> raceOfArresteeNotBlank601Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("race", 
				"49", ArresteeSegment.class, NIBRSErrorCode._601, RaceOfArresteeCode.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<ArresteeSegment> ethnicityOfArresteeNotBlank604Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("ethnicity", 
				"50", ArresteeSegment.class, NIBRSErrorCode._604, EthnicityOfArrestee.codeSet());
		
		return validValueListRule;
	}
	
	public Rule<ArresteeSegment> residentStatusOfArresteeNotBlank604Code(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("residentStatusOfArrestee", 
				"51", ArresteeSegment.class, NIBRSErrorCode._604, ResidentStatusCode.codeSet());
		
		return validValueListRule;		
	}
		
	
	public Rule<ArresteeSegment> dispositionOfArresteeUnder18ValidValue604Rule(){
		
		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("dispositionOfArresteeUnder18", 
				"52", ArresteeSegment.class, NIBRSErrorCode._604, DispositionOfArresteeUnder18Code.codeSet());
		
		return validValueListRule;
	}
	
// TODO see why not in pojo	
//	public Rule<ArresteeSegment> clearanceCodeValidValue604Rule(){
//		
//		ValidValueListRule<ArresteeSegment> validValueListRule = new ValidValueListRule<ArresteeSegment>("TODO (not in pojo)", 
//			/* unnumbered */ "", ArresteeSegment.class, NIBRSErrorCode._604, ClearanceCode.codeSet());
//		
//		return validValueListRule;
//	}
	
}
