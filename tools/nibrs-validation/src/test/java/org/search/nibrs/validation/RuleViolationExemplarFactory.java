package org.search.nibrs.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.Offense;
import org.search.nibrs.model.codes.LocationTypeCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.Property;

/**
 * Class that manages a set of "edits" to baseline incidents. These edits create "exemplars" of NIBRS rules violations that can be used to unit test the validation logic in the precert tool.
 *
 */
public class RuleViolationExemplarFactory {

	private static final RuleViolationExemplarFactory INSTANCE = new RuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(RuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private RuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final RuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * Get an Incident that violates the specified rule. For rules and their numbers, reference the NIBRS Technical Specification, Section 5.
	 * 
	 * @param ruleNumber
	 *            the rule number
	 * @return an incident that exemplifies violation of the rule
	 */
	public List<GroupAIncidentReport> getGroupAIncidentsThatViolateRule(Integer ruleNumber) {
		return groupATweakerMap.get(ruleNumber).apply(BaselineIncidentFactory.getBaselineIncident());
	}

	private void populateGroupAExemplarMap() {

		groupATweakerMap.put(59, incident -> {
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			// First two positions must be the code of the state (e.g., SC, MD) in which the incident occurred.
			// non-federal participants, every record must have the same code.
			ret.setOri("ZZ123456789");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(101, incident -> {
			// The referenced data element in a Group A Incident Report
			// Segment 1 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.setExceptionalClearanceCode(null);
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			copy7.setExceptionalClearanceDate(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			return incidents;

		});

		groupATweakerMap.put(104, incident -> {
			// The referenced data element in a Group A Incident Report
			// Segment 1 must be valid.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(1054);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(14);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri("WA1234");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber("12345");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentDate(Date.from(LocalDateTime.of(1054, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.setExceptionalClearanceCode("X");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			copy7.setCityIndicator("ZZ12");
			// ReportDateIndicator should be set to "R" if unknown.
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			copy8.setIncidentDate(null);
			copy8.setReportDateIndicator("S");
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			// (Incident Hour) The referenced data element must contain a valid data value when it is entered.
			copy9.setIncidentDate(Date.from(LocalDateTime.of(2016, 13, 12, 30, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			// (Incident Hour)) The referenced data element must contain a valid data value when it is entered.
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			copy10.setExceptionalClearanceDate(Date.from(LocalDateTime.of(2016, 13, 12, 30, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			return incidents;

		});

		groupATweakerMap.put(105, incident -> {
			// The data element in error contains a date that is not entered correctly.
			// Each component of the date must be valid; that is, months must be 01 through 12,
			// days must be 01 through 31, and year must include the century (i.e., 19xx, 20xx).
			// In addition, days cannot exceed maximum for the month (e.g., June cannot have 31days).
			// Also, the date cannot exceed the current date.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(0120);
			copy.setMonthOfTape(5);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy.setYearOfTape(2016);
			copy.setMonthOfTape(13);
			copy.setIncidentDate(Date.from(LocalDateTime.of(2016, 6, 31, 30, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy.setExceptionalClearanceDate(Date.from(LocalDateTime.of(3016, 13, 12, 30, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy4);
			return incidents;
		});

		groupATweakerMap.put(115, incident -> {
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			// (Incident Number) Must be blank right-fill if under 12 characters in length.
			// Cannot have embedded blanks between the first and last characters entered.
			ret.setIncidentNumber("1234 5678");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(116, incident -> {
			// (Incident Number) must be left-justified with blank right-fill.
			// Since the number is less than 12 characters, it must begin in position 1.
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			ret.setIncidentNumber(" 12345678");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(117, incident -> {

			// (Incident Number) can only have character combinations of A through Z, 0 through 9,
			// hyphens, and/or blanks. For example, 89-123-SC is valid, but 89+123*SC is invalid.
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			ret.setIncidentNumber("89+123*SC");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(119, incident -> {
			// Data Element 2A (Cargo Theft) must be populated with a valid data value when
			// Data Element 6 (UCR Offense Code) contains a Cargo Theft-related offense.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setCargoTheftIndicator(true);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});

		groupATweakerMap.put(151, incident -> {
			// This field must be blank if the incident date is known. If the incident date is unknown,
			// then the report date would be entered instead and must be indicated with an "R" in the Report
			// Indicator field within the Administrative Segment. The "R" in this case is invalid.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setReportDateIndicator("R");
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(152, incident -> {
			// If Hour is entered within Data Element 3 (Incident Date/Hour), it must be 00 through 23.
			// If 00=Midnight is entered, be careful that the Incident Date is entered as if the time was
			// 1 minute past midnight.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(Date.from(LocalDateTime.of(2016, 5, 12, 00, 0, 00).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(153, incident -> {
			// Data Element 4 ((Cleared Exceptionally) Cannot be N=Not Applicable if Data Element 5 (Exceptional Clearance Date) is entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setExceptionalClearanceCode("N");
			copy.setExceptionalClearanceDate(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(155, incident -> {
			// Data Element 5 (Exceptional Clearance Date) is earlier than Data Element 3 (Incident Date/Hour).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			copy.setExceptionalClearanceDate(Date.from(LocalDateTime.of(2015, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(156, incident -> {
			// Data Element 5 (Exceptional Clearance Date) must be present if the case was cleared exceptionally.
			// Data Element 4 (Cleared Exceptionally) has an entry of A through E; therefore, the date must also be entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setExceptionalClearanceCode("A");
			copy.setExceptionalClearanceDate(null);
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(170, incident -> {
			// Data Element 3 (Incident Date) The date cannot be later than the year and month the electronic submission represents.
			// For example, the May 1999 electronic submission cannot contain incidents happening after this date.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(Date.from(LocalDateTime.of(3016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(171, incident -> {
			// A Group ï¿½Aï¿½ Incident Report was submitted with a date entered into Data Element 3 (Incident Date/Hour)
			// that is earlier than January 1 of the previous year, using the Month of Tape and Year of Tape as a reference point,
			// e.g., if the Month of Tape and Year of Tape contain a value of 01/1999, but the incident date is 12/25/1997, the incident will be rejected.
			// Volume 2, section I, provides specifications concerning the FBIï¿½s 2-year database.
			// For example, the May 1999 electronic submission cannot contain incidents happening after this date.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(Date.from(LocalDateTime.of(2000, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(172, incident -> {
			// (Incident Date) cannot be earlier than 01/01/1991. This edit will preclude dates that are obviously
			// incorrect since the FBI began accepting NIBRS data on this date.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(Date.from(LocalDateTime.of(1990, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(173, incident -> {
			// Data Element 5 (Exceptional Clearance Date) cannot contain a date earlier than the date the LEA began submitting data via the NIBRS.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(Date.from(LocalDateTime.of(1990, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			GroupAIncidentReport copy1 = new GroupAIncidentReport(incident);
			copy1.setExceptionalClearanceDate(Date.from(LocalDateTime.of(1016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			incidents.add(copy);
			incidents.add(copy1);
			return incidents;
		});

		groupATweakerMap.put(201, incident -> {
			// The referenced data element in a Group A Incident Report
			// Segment 2 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.setExceptionalClearanceCode(null);

			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			Offense ucrOffense = new Offense();
			ucrOffense.setUcrOffenseCode(null);
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			Offense attemptedOffense = new Offense();
			attemptedOffense.setOffenseAttemptedCompleted(null);
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			Offense biasOffense = new Offense();
			biasOffense.setBiasMotivation(0, null);
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			Offense offenderSuspectedOfUsing = new Offense();
			offenderSuspectedOfUsing.setOffendersSuspectedOfUsing(0, null);
			GroupAIncidentReport copy11 = new GroupAIncidentReport(copy);
			Offense offenseLocation = new Offense();
			offenseLocation.setLocationType(null);
			GroupAIncidentReport copy12 = new GroupAIncidentReport(copy);
			Offense weaponForceInvolved = new Offense();
			weaponForceInvolved.setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy13 = new GroupAIncidentReport(copy);
			Offense automaticWeaponIndicator = new Offense();
			automaticWeaponIndicator.setAutomaticWeaponIndicator(0, null);

			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			incidents.add(copy11);
			incidents.add(copy12);
			incidents.add(copy13);
			return incidents;
		});

		groupATweakerMap.put(204, incident -> {
			// The referenced data element in a Group A Incident Report Segment 2 must
			// be populated with a valid data value.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setOri("1234567890123");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setOri("ZZ123456789");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setCityIndicator("ZZ12");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("XXX");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			Offense biasOffense = new Offense();
			biasOffense.setBiasMotivation(0, "10");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			Offense offenseLocation = new Offense();
			offenseLocation.setLocationType("99");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			Offense numberOfPremisesEntered = new Offense();
			numberOfPremisesEntered.setNumberOfPremisesEntered(100);
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			Offense methodOfEntry = new Offense();
			methodOfEntry.setMethodOfEntry(null);
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			Offense criminalActivity = new Offense();
			criminalActivity.setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			Offense weaponForceInvolved = new Offense();
			weaponForceInvolved.setTypeOfWeaponForceInvolved(0, "10");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(copy);
			Offense automaticWeaponIndicator = new Offense();
			automaticWeaponIndicator.setAutomaticWeaponIndicator(0, "B");

			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			incidents.add(copy11);
			return incidents;
		});

		groupATweakerMap.put(206, incident -> {
			// The referenced data element in error is one that contains multiple
			// data values. When more than one code is entered, none can be duplicate codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstOffendersSuspectedOfUsing = new Offense();
			firstOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "A");
			Offense secondOffendersSuspectedOfUsing = new Offense();
			secondOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "C");
			Offense thirdOffendersSuspectedOfUsing = new Offense();
			thirdOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "C");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense firstBiasMotivationOffense = new Offense();
			firstBiasMotivationOffense.setBiasMotivation(0, "15");
			Offense secondBiasMotivationOffense = new Offense();
			secondBiasMotivationOffense.setBiasMotivation(0, "26");
			Offense thirdBiasMotivationOffense = new Offense();
			thirdBiasMotivationOffense.setBiasMotivation(0, "26");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Offense firstTypeOfCriminalActivity = new Offense();
			firstTypeOfCriminalActivity.setTypeOfCriminalActivity(0, "J");
			Offense secondTypeOfCriminalActivity = new Offense();
			secondTypeOfCriminalActivity.setTypeOfCriminalActivity(0, "J");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Offense typeOfWeaponForceInvolved = new Offense();
			typeOfWeaponForceInvolved.setTypeOfCriminalActivity(0, "11");
			secondTypeOfCriminalActivity.setTypeOfCriminalActivity(0, "11");

			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			return incidents;

		});

		groupATweakerMap.put(207, incident -> {
			// The referenced data element in error is one that contains multiple
			// data values. However "N" is mutually exclusive with other codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstOffendersSuspectedOfUsing = new Offense();
			firstOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "A");
			Offense secondOffendersSuspectedOfUsing = new Offense();
			secondOffendersSuspectedOfUsing.setOffendersSuspectedOfUsing(0, "N");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense firstBiasMotivationOffense = new Offense();
			firstBiasMotivationOffense.setBiasMotivation(0, "15");
			Offense secondBiasMotivationOffense = new Offense();
			secondBiasMotivationOffense.setBiasMotivation(0, "88");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Offense firstTypeOfCriminalActivity = new Offense();
			firstTypeOfCriminalActivity.setTypeOfCriminalActivity(0, "N");
			Offense secondTypeOfCriminalActivity = new Offense();
			secondTypeOfCriminalActivity.setTypeOfCriminalActivity(0, "J");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Offense firstTypeOfWeaponForceInvolve = new Offense();
			firstTypeOfWeaponForceInvolve.setTypeOfWeaponForceInvolved(0, "11");
			Offense secondTypeOfWeaponForceInvolved = new Offense();
			secondTypeOfWeaponForceInvolved.setTypeOfWeaponForceInvolved(0, "99");
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			return incidents;

		});

		groupATweakerMap.put(220, incident -> {
			// Data Element 12 (Type Criminal Activity/Gang Information) Must be populated with a valid data value and cannot be blank when Data Element 6 (UCR Offense Code) is:
			// 250=Counterfeiting/Forgery
			// 280=Stolen Property Offenses
			// 35A=Drug/Narcotic Violations
			// 35B=Drug Equipment Violations
			// 39C=Gambling Equipment Violations
			// 370=Pornography/Obscene Material
			// 520=Weapon Law Violations
			// 720=Animal Cruelty
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstCriminalActivity = new Offense();
			firstCriminalActivity.setTypeOfCriminalActivity(0, null);
			firstCriminalActivity.setUcrOffenseCode("250");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense secondCriminalActivity = new Offense();
			secondCriminalActivity.setTypeOfCriminalActivity(0, null);
			secondCriminalActivity.setUcrOffenseCode("280");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Offense thirdCriminalActivity = new Offense();
			thirdCriminalActivity.setTypeOfCriminalActivity(0, null);
			thirdCriminalActivity.setUcrOffenseCode("35A");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Offense fourthCriminalActivity = new Offense();
			fourthCriminalActivity.setTypeOfCriminalActivity(0, null);
			fourthCriminalActivity.setUcrOffenseCode("35B");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Offense fifthCriminalActivity = new Offense();
			fifthCriminalActivity.setTypeOfCriminalActivity(0, null);
			fifthCriminalActivity.setUcrOffenseCode("39C");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			Offense sixthCriminalActivity = new Offense();
			sixthCriminalActivity.setTypeOfCriminalActivity(0, null);
			sixthCriminalActivity.setUcrOffenseCode("370");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			Offense seventhCriminalActivity = new Offense();
			seventhCriminalActivity.setTypeOfCriminalActivity(0, null);
			seventhCriminalActivity.setUcrOffenseCode("520");
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			Offense eighthCriminalActivity = new Offense();
			eighthCriminalActivity.setTypeOfCriminalActivity(0, null);
			eighthCriminalActivity.setUcrOffenseCode("720");
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			return incidents;

		});

		groupATweakerMap.put(221, incident -> {
			// Data Element 13 (Type Weapon/Force Involved) Must be populated with a valid data value and cannot be blank
			//when Data Element 6 (UCR Offense Code) is:
			//09A=Murder and Non-negligent Manslaughter
			//09B=Negligent Manslaughter
			//09C=Justifiable Homicide
			//100=Kidnapping/Abduction
			//11A=Rape
			//11B=Sodomy
			//11C=Sexual Assault With An Object
			//11D=Fondling
			//120=Robbery
			//13A=Aggravated Assault
			//13B=Simple Assault
			//210=Extortion/Blackmail
			//520=Weapon Law Violations
			//64A=Human Trafficking, Commercial Sex Acts
			//64B=Human Trafficking, Involuntary Servitude
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstUCROffense = new Offense();
			firstUCROffense.setTypeOfWeaponForceInvolved(0, null);
			firstUCROffense.setUcrOffenseCode("09A");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense secondUCROffense = new Offense();
			secondUCROffense.setTypeOfWeaponForceInvolved(0, null);
			secondUCROffense.setUcrOffenseCode("09B");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Offense thirdUCROffense = new Offense();
			thirdUCROffense.setTypeOfWeaponForceInvolved(0, null);
			thirdUCROffense.setUcrOffenseCode("09C");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Offense fourthUCROffense = new Offense();
			fourthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			fourthUCROffense.setUcrOffenseCode("100");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Offense fifthUCROffense = new Offense();
			fifthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			fifthUCROffense.setUcrOffenseCode("11A");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			Offense sixthUCROffense = new Offense();
			sixthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			sixthUCROffense.setUcrOffenseCode("11B");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			Offense seventhUCROffense = new Offense();
			seventhUCROffense.setTypeOfWeaponForceInvolved(0, null);
			seventhUCROffense.setUcrOffenseCode("11C");
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			Offense eighthUCROffense = new Offense();
			eighthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			eighthUCROffense.setUcrOffenseCode("11D");
			GroupAIncidentReport copy9 = new GroupAIncidentReport(incident);
			Offense ninthUCROffense = new Offense();
			ninthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			ninthUCROffense.setUcrOffenseCode("120");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(incident);
			Offense tenthUCROffense = new Offense();
			tenthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			tenthUCROffense.setUcrOffenseCode("13A");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(incident);
			Offense eleventhUCROffense = new Offense();
			eleventhUCROffense.setTypeOfWeaponForceInvolved(0, null);
			eleventhUCROffense.setUcrOffenseCode("13B");
			GroupAIncidentReport copy12 = new GroupAIncidentReport(incident);
			Offense twelvthUCROffense = new Offense();
			twelvthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			twelvthUCROffense.setUcrOffenseCode("210");
			GroupAIncidentReport copy13 = new GroupAIncidentReport(incident);
			Offense thirteenthUCROffense = new Offense();
			thirteenthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			thirteenthUCROffense.setUcrOffenseCode("520");
			GroupAIncidentReport copy14 = new GroupAIncidentReport(incident);
			Offense fourteenthUCROffense = new Offense();
			fourteenthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			fourteenthUCROffense.setUcrOffenseCode("64A");
			GroupAIncidentReport copy15 = new GroupAIncidentReport(incident);
			Offense fifteenthUCROffense = new Offense();
			fifteenthUCROffense.setTypeOfWeaponForceInvolved(0, null);
			fifteenthUCROffense.setUcrOffenseCode("64B");
			
			//todo: do we need another set with an invalid value for TypeOfWeaponForceInvolved?	
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			incidents.add(copy11);
			incidents.add(copy12);
			incidents.add(copy13);
			incidents.add(copy14);
			incidents.add(copy15);
			return incidents;

		});
		
		
		
		
		groupATweakerMap.put(251, incident -> {
			// (Offense Attempted/Completed) Must be a valid code of A=Attempted or C=Completed.
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			ret.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(252, incident -> {

			// When number of premises is entered location type must be 14 or 19
			// and UCR Offense Code must be Burglary.

			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();

			Set<OffenseCode> offenseCodeSet = EnumSet.allOf(OffenseCode.class);
			offenseCodeSet.remove(OffenseCode._220); // Burglary

			Set<LocationTypeCode> locationTypeCodeSet = LocationTypeCode.asSet();
			locationTypeCodeSet.remove(LocationTypeCode._14);
			locationTypeCodeSet.remove(LocationTypeCode._19);

			for (OffenseCode oc : offenseCodeSet) {
				for (LocationTypeCode ltc : locationTypeCodeSet) {
					GroupAIncidentReport copy = new GroupAIncidentReport(incident);
					Offense offense = new Offense();
					offense.setNumberOfPremisesEntered(2);
					offense.setLocationType(ltc.code);
					offense.setUcrOffenseCode(oc.code);
					incident.addOffense(offense);
					incidents.add(copy);
				}
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				Offense offense = new Offense();
				offense.setNumberOfPremisesEntered(2);
				offense.setLocationType(LocationTypeCode._14.code);
				offense.setUcrOffenseCode(oc.code);
				incident.addOffense(offense);
				incidents.add(copy);
			}

			for (LocationTypeCode ltc : locationTypeCodeSet) {
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				Offense offense = new Offense();
				offense.setNumberOfPremisesEntered(2);
				offense.setLocationType(ltc.code);
				offense.setUcrOffenseCode(OffenseCode._220.code);
				incident.addOffense(offense);
				incidents.add(copy);
			}

			return incidents;

		});

		groupATweakerMap.put(253, incident -> {
			// (Method of Entry) Data Element was not entered; it must be entered
			// when UCR Offense Code of 220=Burglary has been entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense offense = new Offense();
			offense.setMethodOfEntry(null);
			offense.setUcrOffenseCode("220");
			incident.addOffense(offense);
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(254, incident -> {
			// (Method of Entry) Data Element only applies to UCR Offense Code of 220=Burglary.
			// Since a burglary offense was not entered, the Method of Entry should not have been entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense offense = new Offense();
			offense.setMethodOfEntry("F");
			offense.setUcrOffenseCode("13A");
			incident.addOffense(offense);
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(255, incident -> {
			// ((Automatic Weapon Indicator) Must be A=Automatic or blank=Not Automatic
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense automaticWeaponIndicator = new Offense();
			automaticWeaponIndicator.setAutomaticWeaponIndicator(0, "F");
			incident.addOffense(automaticWeaponIndicator);
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(256, incident -> {
			// Offense Attempted/Completed, Data Element 7, must be a valid code of A=Attempted or C=Completed if UCR code is Homicide
			// Assault.
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			ret.getOffenses().get(0).setUcrOffenseCode("09A");
			ret.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(257, incident -> {
			// (Number of Premises Entered) Must be entered if offense code is 220 (Burglary)
			// and if Data Element 9 (Location Type) contains 14=Hotel/Motel/Etc. or 19=Rental Storage Facility.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense offense = new Offense();
			offense.setLocationType("14");
			offense.setUcrOffenseCode("220");
			GroupAIncidentReport copy1 = new GroupAIncidentReport(incident);
			Offense offense1 = new Offense();
			offense1.setLocationType("19");
			offense1.setUcrOffenseCode("220");
			incident.addOffense(offense);
			incidents.add(copy);
			incidents.add(copy1);
			return incidents;

		});

		groupATweakerMap.put(258, incident -> {
			// (Automatic Weapon Indicator) In Data Element 13 (Type of Weapon/Force Involved), A=Automatic is the third character of code. It is valid only with the following codes:
			// 11=Firearm (Type Not Stated)
			// 12=Handgun
			// 13=Rifle
			// 15=Other Firearm
			// A weapon code other than those mentioned was entered with the automatic indicator. An automatic weapon is, by definition, a firearm.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense automaticWeaponIndicator = new Offense();
			automaticWeaponIndicator.setAutomaticWeaponIndicator(0, "A");
			automaticWeaponIndicator.setTypeOfWeaponForceInvolved(0, "20");
			incident.addOffense(automaticWeaponIndicator);
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(262, incident -> {
			// When a Group A Incident Report is submitted, the individual segments
			// comprising the incident cannot contain duplicates.
			// In this case, two Offense Segments were submitted having the same
			// offense in Data Element 6 (UCR Offense Code).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			copy.addOffense(secondOffense);
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(263, incident -> {
			// Can be submitted only 10 times for each Group A Incident Report;
			// 10 offense codes are allowed for each incident.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("13A");
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			Offense thirdOffense = new Offense();
			thirdOffense.setUcrOffenseCode("13C");
			Offense fourthOffense = new Offense();
			fourthOffense.setUcrOffenseCode("13D");
			Offense fifthOffense = new Offense();
			fifthOffense.setUcrOffenseCode("13E");
			Offense sixthOffense = new Offense();
			sixthOffense.setUcrOffenseCode("13F");
			Offense seventhOffense = new Offense();
			seventhOffense.setUcrOffenseCode("13G");
			Offense eighthOffense = new Offense();
			eighthOffense.setUcrOffenseCode("13H");
			Offense ninthOffense = new Offense();
			ninthOffense.setUcrOffenseCode("13I");
			Offense tenthOffense = new Offense();
			tenthOffense.setUcrOffenseCode("13J");
			Offense eleventhOffense = new Offense();
			eleventhOffense.setUcrOffenseCode("13K");
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(264, incident -> {
			// Group A Offense code cannot contain a Group B Offense
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("90A");
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(265, incident -> {
			// (Type Weapon/Force Involved) If an Offense Segment (Level 2) was submitted for 13B=Simple Assault,
			// Data Element 13 (Type Weapon/Force Involved) can only have codes of 40=Personal Weapons,
			// 90=Other, 95=Unknown, and 99=None. All other codes are not valid because they do not relate to a simple assault.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("13B");
			firstOffense.setTypeOfWeaponForceInvolved(0, "11");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			secondOffense.setTypeOfWeaponForceInvolved(0, "12");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Offense thirdOffense = new Offense();
			thirdOffense.setUcrOffenseCode("13B");
			thirdOffense.setTypeOfWeaponForceInvolved(0, "13");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Offense fourthOffense = new Offense();
			fourthOffense.setUcrOffenseCode("13B");
			fourthOffense.setTypeOfWeaponForceInvolved(0, "14");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Offense fifthOffense = new Offense();
			fifthOffense.setUcrOffenseCode("13B");
			fifthOffense.setTypeOfWeaponForceInvolved(0, "15");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			Offense sixthOffense = new Offense();
			sixthOffense.setUcrOffenseCode("13B");
			sixthOffense.setTypeOfWeaponForceInvolved(0, "20");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			Offense seventhOffense = new Offense();
			seventhOffense.setUcrOffenseCode("13B");
			seventhOffense.setTypeOfWeaponForceInvolved(0, "30");
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			Offense eighthOffense = new Offense();
			eighthOffense.setUcrOffenseCode("13B");
			eighthOffense.setTypeOfWeaponForceInvolved(0, "50");
			GroupAIncidentReport copy9 = new GroupAIncidentReport(incident);
			Offense ninthOffense = new Offense();
			ninthOffense.setUcrOffenseCode("13B");
			ninthOffense.setTypeOfWeaponForceInvolved(0, "60");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(incident);
			Offense tenthOffense = new Offense();
			tenthOffense.setUcrOffenseCode("13B");
			tenthOffense.setTypeOfWeaponForceInvolved(0, "65");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(incident);
			Offense eleventhOffense = new Offense();
			eleventhOffense.setUcrOffenseCode("13B");
			eleventhOffense.setTypeOfWeaponForceInvolved(0, "70");
			GroupAIncidentReport copy12 = new GroupAIncidentReport(incident);
			Offense twelfthOffense = new Offense();
			twelfthOffense.setUcrOffenseCode("13B");
			twelfthOffense.setTypeOfWeaponForceInvolved(0, "85");
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			incidents.add(copy11);
			incidents.add(copy12);
			return incidents;
		});

		groupATweakerMap.put(266, incident -> {
			// When a Justifiable Homicide is reported, no other offense may be
			// reported in the Group A Incident Report. These should be submitted on another
			// Group A Incident Report.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense JustifiableHomicideOffense = new Offense();
			JustifiableHomicideOffense.setUcrOffenseCode("09C");
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(267, incident -> {
			// If a homicide offense is submitted, Data Element 13 (Type Weapon/Force Involved)
			// cannot have 99=None. Some type of weapon/force must be used in a homicide offense.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense homicideOffense = new Offense();
			homicideOffense.setUcrOffenseCode("09A");
			homicideOffense.setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense homicideOffense2 = new Offense();
			homicideOffense2.setUcrOffenseCode("09A");
			homicideOffense2.setTypeOfWeaponForceInvolved(0, "99");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;

		});

		groupATweakerMap.put(269, incident -> {
			// (Type Weapon/Force Involved) If Data Element 6 (UCR Offense Code) is 13B=Simple Assault and the
			// weapon involved is 11=Firearm, 12=Handgun, 13=Rifle, 14=Shotgun, or 15=Other Firearm, then the offense
			// should instead be classified as 13A=Aggravated Assault.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("13B");
			firstOffense.setTypeOfWeaponForceInvolved(0, "11");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense secondOffense = new Offense();
			secondOffense.setUcrOffenseCode("13B");
			secondOffense.setTypeOfWeaponForceInvolved(0, "12");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Offense thirdOffense = new Offense();
			thirdOffense.setUcrOffenseCode("13B");
			thirdOffense.setTypeOfWeaponForceInvolved(0, "13");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Offense fourthOffense = new Offense();
			fourthOffense.setUcrOffenseCode("13B");
			fourthOffense.setTypeOfWeaponForceInvolved(0, "14");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Offense fifthOffense = new Offense();
			fifthOffense.setUcrOffenseCode("13B");
			fifthOffense.setTypeOfWeaponForceInvolved(0, "15");
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			return incidents;

		});

		groupATweakerMap.put(270, incident -> {
			// If a justifiable homicide offense is submitted, Data Element 8A (Bias motivation) must be 88.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense JustifiableHomicideOffense = new Offense();
			JustifiableHomicideOffense.setUcrOffenseCode("09C");
			JustifiableHomicideOffense.setBiasMotivation(0, "11");
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(301, incident -> {
			// The referenced data element in a Group A Incident Report
			// Segment 3 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.setExceptionalClearanceCode(null);
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			Property typeOfPropertyLoss = new Property();
			typeOfPropertyLoss.setTypeOfPropertyLoss(null);
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, null);

			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			return incidents;
		});

		groupATweakerMap.put(304, incident -> {
			// The referenced data element in a Group A Incident Report Segment 3 must
			// be populated with a valid data value.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setOri("1234567890123");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setOri("ZZ123456789");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setCityIndicator("ZZ12");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			Offense firstOffense = new Offense();
			firstOffense.setUcrOffenseCode("XXX");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			Offense biasOffense = new Offense();
			biasOffense.setBiasMotivation(0, "10");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			Offense offenseLocation = new Offense();
			offenseLocation.setLocationType("99");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			Property typeOfPropertyLoss = new Property();
			typeOfPropertyLoss.setTypeOfPropertyLoss("9");
			//Data Element 16 Value of Property does not have separate rules for blank and valid.
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0, 000000500);
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			Property valueOfProperty2 = new Property();
			valueOfProperty2.setValueOfProperty(0, null);
			
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			return incidents;
		});

		
		groupATweakerMap.put(342, incident -> {
			//(Value of Property) When referenced data element contains a value that exceeds an FBI-assigned 
			//threshold amount, a warning message will be created. The participant is asked to check to 
			//see if the value entered was a data entry error, or if it was intended to be entered. 
			//A warning message is always produced when the value is $1,000,000 or greater. 
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0, 001000000);
			
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(351, incident -> {
			// Value of Property) cannot be zero unless Data Element 15 (Property Description) is:
			//Mandatory zero
			//09=Credit/Debit Cards
			//22=Non-negotiable Instruments
			//48=Documents–Personal or Business
			//65=Identity Documents
			//66=Identity–Intangible
			//Optional zero
			//77=Other
			//99=(blank)–this data value is not currently used by the FBI
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0, 000000500);
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, "09");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Property valueOfProperty2 = new Property();
			valueOfProperty2.setValueOfProperty(0, 000000500);
			Property propertyDescription2 = new Property();
			propertyDescription2.setPropertyDescription(0, "22");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Property valueOfProperty3 = new Property();
			valueOfProperty3.setValueOfProperty(0, 000000500);
			Property propertyDescription3 = new Property();
			propertyDescription3.setPropertyDescription(0, "48");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Property valueOfProperty4 = new Property();
			valueOfProperty4.setValueOfProperty(0, 000000500);
			Property propertyDescription4 = new Property();
			propertyDescription.setPropertyDescription(0, "65");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Property valueOfProperty5 = new Property();
			valueOfProperty5.setValueOfProperty(0, 000000500);
			Property propertyDescription5 = new Property();
			propertyDescription5.setPropertyDescription(0, "66");

			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			
			
			return incidents;
		});

		groupATweakerMap.put(353, incident -> {
			//(Value of Property) is 88=Pending Inventory, but Data Element 16 (Value of Property) is not $1. 
			//Determine which of the data elements was entered incorrectly.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, "88");
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0,000000001);
			
			incidents.add(copy);
			return incidents;
		});

			
		groupATweakerMap.put(354, incident -> {
			// (Property Description) Data Element 16 (Value of Property) contains a value,
			// but Data Element 15 (Property Description) was not entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0, 000000500);
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, null);

			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(356, incident -> {
			// (Property Description) was entered, but Data Elements 15 (Property Description)
			// and/or 16 (Property Value) were not entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0, null);
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, "01");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Property valueOfProperty2 = new Property();
			valueOfProperty2.setValueOfProperty(0, 000000500);
			Property propertyDescription2 = new Property();
			propertyDescription2.setPropertyDescription(0, null);

			incidents.add(copy);
			incidents.add(copy2);
			
			return incidents;
		});

		groupATweakerMap.put(359, incident -> {
			// (Property Description) Must be one of the following
			//03=Automobiles
			//05=Buses
			//24=Other Motor Vehicles
			//28=Recreational Vehicles
			//37=Trucks
			//when Data Element 18 (Number of Stolen Motor Vehicles) or Data Element 19 (Number of Recovered Motor Vehicles) 
			//contain a data value other than 00=Unknown:
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, "01");
			Property numberOfStolenMotorVehicles = new Property();
			numberOfStolenMotorVehicles.setNumberOfStolenMotorVehicles(2);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Property propertyDescription2 = new Property();
			propertyDescription2.setPropertyDescription(0, "01");
			Property numberOfRecoveredMotorVehicles = new Property();
			numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(2);
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		groupATweakerMap.put(372, incident -> {
			//(Type of Property Loss) is 
			//2=Burned
			//3=Counterfeited/Forged
			//4=Destroyed/Damaged/Vandalized
			//5=Recovered
			//6=Seized
			//7=Stolen/Etc.
			//Data Elements 15 through 22 must have applicable entries in the segment.
			
			//2=Burned
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property typeOfPropertyLoss = new Property();
			typeOfPropertyLoss.setTypeOfPropertyLoss("2");
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, null);
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0, null);
			Property dateRecovered = new Property();
			dateRecovered.setDateRecovered(0, null);
			Property numberOfStolenMotorVehicles = new Property();
			numberOfStolenMotorVehicles.setNumberOfStolenMotorVehicles(2);
			Property numberOfRecoveredMotorVehicles = new Property();
			numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(1);
			Property suspectedDrugType = new Property();
			suspectedDrugType.setSuspectedDrugType(0, null);
			Property estimatedDrugQuantity = new Property();
			estimatedDrugQuantity.setEstimatedDrugQuantity(0, null);
			Property typeDrugMeasurement = new Property();
			typeDrugMeasurement.setTypeDrugMeasurement(0, null);
			//3=Counterfeited/Forged
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Property typeOfPropertyLoss2 = new Property();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("3");
			Property propertyDescription2 = new Property();
			propertyDescription2.setPropertyDescription(0, null);
			Property valueOfProperty2 = new Property();
			valueOfProperty2.setValueOfProperty(0, null);
			Property dateRecovered2 = new Property();
			dateRecovered2.setDateRecovered(0, null);
			Property numberOfStolenMotorVehicles2 = new Property();
			numberOfStolenMotorVehicles2.setNumberOfStolenMotorVehicles(2);
			Property numberOfRecoveredMotorVehicles2 = new Property();
			numberOfRecoveredMotorVehicles2.setNumberOfRecoveredMotorVehicles(1);
			Property suspectedDrugType2 = new Property();
			suspectedDrugType2.setSuspectedDrugType(0, null);
			Property estimatedDrugQuantity2 = new Property();
			estimatedDrugQuantity2.setEstimatedDrugQuantity(0, null);
			Property typeDrugMeasurement2 = new Property();
			typeDrugMeasurement2.setTypeDrugMeasurement(0, null);
			//4=Destroyed/Damaged/Vandalized
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Property typeOfPropertyLoss3 = new Property();
			typeOfPropertyLoss3.setTypeOfPropertyLoss("4");
			Property propertyDescription3 = new Property();
			propertyDescription3.setPropertyDescription(0, null);
			Property valueOfProperty3 = new Property();
			valueOfProperty3.setValueOfProperty(0, null);
			Property dateRecovered3 = new Property();
			dateRecovered3.setDateRecovered(0, null);
			Property numberOfStolenMotorVehicles3 = new Property();
			numberOfStolenMotorVehicles3.setNumberOfStolenMotorVehicles(2);
			Property numberOfRecoveredMotorVehicles3 = new Property();
			numberOfRecoveredMotorVehicles3.setNumberOfRecoveredMotorVehicles(1);
			Property suspectedDrugType3 = new Property();
			suspectedDrugType3.setSuspectedDrugType(0, null);
			Property estimatedDrugQuantity3 = new Property();
			estimatedDrugQuantity3.setEstimatedDrugQuantity(0, null);
			Property typeDrugMeasurement3 = new Property();
			typeDrugMeasurement3.setTypeDrugMeasurement(0, null);
			//5=Recovered
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Property typeOfPropertyLoss4 = new Property();
			typeOfPropertyLoss4.setTypeOfPropertyLoss("5");
			Property propertyDescription4 = new Property();
			propertyDescription4.setPropertyDescription(0, null);
			Property valueOfProperty4 = new Property();
			valueOfProperty4.setValueOfProperty(0, null);
			Property dateRecovered4 = new Property();
			dateRecovered4.setDateRecovered(0, null);
			Property numberOfStolenMotorVehicles4 = new Property();
			numberOfStolenMotorVehicles4.setNumberOfStolenMotorVehicles(null);
			Property numberOfRecoveredMotorVehicles4 = new Property();
			numberOfRecoveredMotorVehicles4.setNumberOfRecoveredMotorVehicles(null);
			Property suspectedDrugType4 = new Property();
			suspectedDrugType4.setSuspectedDrugType(0, null);
			Property estimatedDrugQuantity4 = new Property();
			estimatedDrugQuantity4.setEstimatedDrugQuantity(0, null);
			Property typeDrugMeasurement4 = new Property();
			typeDrugMeasurement4.setTypeDrugMeasurement(0, null);
			//6=Seized
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Property typeOfPropertyLoss5 = new Property();
			typeOfPropertyLoss5.setTypeOfPropertyLoss("5");
			Property propertyDescription5 = new Property();
			propertyDescription5.setPropertyDescription(0, null);
			Property valueOfProperty5 = new Property();
			valueOfProperty5.setValueOfProperty(0, null);
			Property dateRecovered5 = new Property();
			dateRecovered5.setDateRecovered(0, null);
			Property numberOfStolenMotorVehicles5 = new Property();
			numberOfStolenMotorVehicles5.setNumberOfStolenMotorVehicles(null);
			Property numberOfRecoveredMotorVehicles5 = new Property();
			numberOfRecoveredMotorVehicles5.setNumberOfRecoveredMotorVehicles(null);
			Property suspectedDrugType5 = new Property();
			suspectedDrugType5.setSuspectedDrugType(0, null);
			Property estimatedDrugQuantity5 = new Property();
			estimatedDrugQuantity5.setEstimatedDrugQuantity(0, null);
			Property typeDrugMeasurement5 = new Property();
			typeDrugMeasurement5.setTypeDrugMeasurement(0, null);
			//6=Stolen, Etc.
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			Property typeOfPropertyLoss6 = new Property();
			typeOfPropertyLoss6.setTypeOfPropertyLoss("5");
			Property propertyDescription6 = new Property();
			propertyDescription6.setPropertyDescription(0, null);
			Property valueOfProperty6 = new Property();
			valueOfProperty6.setValueOfProperty(0, null);
			Property dateRecovered6 = new Property();
			dateRecovered6.setDateRecovered(0, null);
			Property numberOfStolenMotorVehicles6 = new Property();
			numberOfStolenMotorVehicles6.setNumberOfStolenMotorVehicles(null);
			Property numberOfRecoveredMotorVehicles6 = new Property();
			numberOfRecoveredMotorVehicles6.setNumberOfRecoveredMotorVehicles(null);
			Property suspectedDrugType6 = new Property();
			suspectedDrugType6.setSuspectedDrugType(0, null);
			Property estimatedDrugQuantity6 = new Property();
			estimatedDrugQuantity6.setEstimatedDrugQuantity(0, null);
			Property typeDrugMeasurement6 = new Property();
			typeDrugMeasurement6.setTypeDrugMeasurement(0, null);
			
			
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			
			return incidents;
		});
		
		
		groupATweakerMap.put(375, incident -> {
			// (Property Description) At least one Data Element 15 (Property Description) code must
			//be entered when Data Element 14 (Type Property Loss/Etc.) contains Property Segment(s) for:
			//		2=Burned
			//		3=Counterfeited/Forged
			//		4=Destroyed/Damaged/Vandalized
			//		5=Recovered
			//		6=Seized
			//		7=Stolen/Etc.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, null);
			Property typeOfPropertyLoss = new Property();
			typeOfPropertyLoss.setTypeOfPropertyLoss("2");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Property propertyDescription2 = new Property();
			propertyDescription2.setPropertyDescription(0, null);
			Property typeOfPropertyLoss2 = new Property();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("3");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Property propertyDescription3 = new Property();
			propertyDescription3.setPropertyDescription(0, null);
			Property typeOfPropertyLoss3 = new Property();
			typeOfPropertyLoss3.setTypeOfPropertyLoss("4");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Property propertyDescription4 = new Property();
			propertyDescription4.setPropertyDescription(0, null);
			Property typeOfPropertyLoss4 = new Property();
			typeOfPropertyLoss4.setTypeOfPropertyLoss("5");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Property propertyDescription5 = new Property();
			propertyDescription5.setPropertyDescription(0, null);
			Property typeOfPropertyLoss5 = new Property();
			typeOfPropertyLoss5.setTypeOfPropertyLoss("6");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			Property propertyDescription6 = new Property();
			propertyDescription6.setPropertyDescription(0, null);
			Property typeOfPropertyLoss6 = new Property();
			typeOfPropertyLoss6.setTypeOfPropertyLoss("7");
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			
			return incidents;
		});
	
		groupATweakerMap.put(383, incident -> {
			//(Value of Property) has a value other than zero entered. 
			//Since Data Element 15 (Property Description) code is 10=Drugs/Narcotics and the only Crime Against Property 
			//offense submitted is a 35A=Drug/Narcotic Violations, Data Element 16 (Value of Property) must be blank.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense offense = new Offense();
			offense.setUcrOffenseCode("35A");
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, "10");
			Property valueOfProperty = new Property();
			valueOfProperty.setValueOfProperty(0, 000000500);
			
			incidents.add(copy);
						
			return incidents;
		});
			groupATweakerMap.put(387, incident -> {
			//(Property Description) To ensure that 35A-35B Drug/Narcotic Offenses-Drug Equipment 
			//Violations are properly reported, Data Element 15 (Property Description) of 11=Drug/Narcotic Equipment 
			//is not allowed with only a 35A Drug/Narcotic Violation. Similarly, 10=Drugs/Narcotics is not 
			//allowed with only a 35B Drug Equipment Violation. And Data Element 14 (Type Property Loss/Etc.) is 6=Seized.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense offense = new Offense();
			offense.setUcrOffenseCode("35A");
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, "11");
			Property typeOfPropertyLoss = new Property();
			typeOfPropertyLoss.setTypeOfPropertyLoss("6");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense offense2 = new Offense();
			offense2.setUcrOffenseCode("35B");
			Property propertyDescription2 = new Property();
			propertyDescription2.setPropertyDescription(0, "11");
			Property typeOfPropertyLoss2 = new Property();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("6");
			
			incidents.add(copy);
			incidents.add(copy2);
			
			return incidents;
		});
		
		groupATweakerMap.put(390, incident -> {
			// (Property Description) must contain a data value that is logical for one or more of the offenses 
			//entered in Data Element 6 (UCR Offense Code).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//Property descriptions for structures are illogical with 220=Burglary/Breaking 
			//& Entering or 240=Motor Vehicle Theft
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			Offense offense1 = new Offense();
			offense1.setUcrOffenseCode("220");
			Property propertyDescription = new Property();
			propertyDescription.setPropertyDescription(0, "29");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			Offense offense2 = new Offense();
			offense2.setUcrOffenseCode("240");
			Property propertyDescription2 = new Property();
			propertyDescription2.setPropertyDescription(0, "29");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			Offense offense3 = new Offense();
			offense3.setUcrOffenseCode("220");
			Property propertyDescription3 = new Property();
			propertyDescription3.setPropertyDescription(0, "30");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			Offense offense4 = new Offense();
			offense4.setUcrOffenseCode("240");
			Property propertyDescription4 = new Property();
			propertyDescription4.setPropertyDescription(0, "30");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			Offense offense5 = new Offense();
			offense5.setUcrOffenseCode("220");
			Property propertyDescription5 = new Property();
			propertyDescription5.setPropertyDescription(0, "31");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			Offense offense6 = new Offense();
			offense6.setUcrOffenseCode("240");
			Property propertyDescription6 = new Property();
			propertyDescription6.setPropertyDescription(0, "31");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			Offense offense7 = new Offense();
			offense7.setUcrOffenseCode("220");
			Property propertyDescription7 = new Property();
			propertyDescription7.setPropertyDescription(0, "32");
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			Offense offense8 = new Offense();
			offense8.setUcrOffenseCode("240");
			Property propertyDescription8 = new Property();
			propertyDescription8.setPropertyDescription(0, "32");
			GroupAIncidentReport copy9 = new GroupAIncidentReport(incident);
			Offense offense9 = new Offense();
			offense9.setUcrOffenseCode("220");
			Property propertyDescription9 = new Property();
			propertyDescription9.setPropertyDescription(0, "33");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(incident);
			Offense offense10 = new Offense();
			offense10.setUcrOffenseCode("240");
			Property propertyDescription10 = new Property();
			propertyDescription10.setPropertyDescription(0, "33");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(incident);
			Offense offense11 = new Offense();
			offense11.setUcrOffenseCode("220");
			Property propertyDescription11 = new Property();
			propertyDescription11.setPropertyDescription(0, "34");
			GroupAIncidentReport copy12 = new GroupAIncidentReport(incident);
			Offense offense12 = new Offense();
			offense12.setUcrOffenseCode("240");
			Property propertyDescription12 = new Property();
			propertyDescription12.setPropertyDescription(0, "34");
			GroupAIncidentReport copy13 = new GroupAIncidentReport(incident);
			Offense offense13 = new Offense();
			offense13.setUcrOffenseCode("220");
			Property propertyDescription13 = new Property();
			propertyDescription13.setPropertyDescription(0, "35");
			GroupAIncidentReport copy14 = new GroupAIncidentReport(incident);
			Offense offense14 = new Offense();
			offense14.setUcrOffenseCode("240");
			Property propertyDescription14 = new Property();
			propertyDescription14.setPropertyDescription(0, "35");
			//Property descriptions for items that would not fit in a purse or pocket (aircraft, vehicles, structures, 
			//a person’s identity, watercraft, etc.) are illogical with 23A=Pocket-picking or 23B=Purse-snatching
			GroupAIncidentReport copy15 = new GroupAIncidentReport(incident);
			Offense offense15 = new Offense();
			offense15.setUcrOffenseCode("23A");
			Property propertyDescription15 = new Property();
			propertyDescription15.setPropertyDescription(0, "01");
			GroupAIncidentReport copy16 = new GroupAIncidentReport(incident);
			Offense offense16 = new Offense();
			offense16.setUcrOffenseCode("23B");
			Property propertyDescription16 = new Property();
			propertyDescription16.setPropertyDescription(0, "01");
			//Automobiles
			GroupAIncidentReport copy17 = new GroupAIncidentReport(incident);
			Offense offense17 = new Offense();
			offense17.setUcrOffenseCode("23A");
			Property propertyDescription17 = new Property();
			propertyDescription17.setPropertyDescription(0, "03");
			GroupAIncidentReport copy18 = new GroupAIncidentReport(incident);
			Offense offense18 = new Offense();
			offense18.setUcrOffenseCode("23B");
			Property propertyDescription18 = new Property();
			propertyDescription18.setPropertyDescription(0, "03");
			//Bicycles
			GroupAIncidentReport copy19 = new GroupAIncidentReport(incident);
			Offense offense19 = new Offense();
			offense19.setUcrOffenseCode("23A");
			Property propertyDescription19 = new Property();
			propertyDescription19.setPropertyDescription(0, "04");
			GroupAIncidentReport copy20 = new GroupAIncidentReport(incident);
			Offense offense20 = new Offense();
			offense20.setUcrOffenseCode("23B");
			Property propertyDescription20 = new Property();
			propertyDescription20.setPropertyDescription(0, "04");
			//Buses
			GroupAIncidentReport copy21 = new GroupAIncidentReport(incident);
			Offense offense21 = new Offense();
			offense21.setUcrOffenseCode("23A");
			Property propertyDescription21 = new Property();
			propertyDescription21.setPropertyDescription(0, "05");
			GroupAIncidentReport copy22 = new GroupAIncidentReport(incident);
			Offense offense22 = new Offense();
			offense22.setUcrOffenseCode("23B");
			Property propertyDescription22 = new Property();
			propertyDescription22.setPropertyDescription(0, "05");
			//Farm Equipment
			GroupAIncidentReport copy23 = new GroupAIncidentReport(incident);
			Offense offense23 = new Offense();
			offense23.setUcrOffenseCode("23A");
			Property propertyDescription23 = new Property();
			propertyDescription23.setPropertyDescription(0, "12");
			GroupAIncidentReport copy24 = new GroupAIncidentReport(incident);
			Offense offense24 = new Offense();
			offense24.setUcrOffenseCode("23B");
			Property propertyDescription24 = new Property();
			propertyDescription24.setPropertyDescription(0, "12");
			//Heavy Construction/Industrial Equipment
			GroupAIncidentReport copy25 = new GroupAIncidentReport(incident);
			Offense offense25 = new Offense();
			offense25.setUcrOffenseCode("23A");
			Property propertyDescription25 = new Property();
			propertyDescription25.setPropertyDescription(0, "15");
			GroupAIncidentReport copy26 = new GroupAIncidentReport(incident);
			Offense offense26 = new Offense();
			offense26.setUcrOffenseCode("23B");
			Property propertyDescription26 = new Property();
			propertyDescription26.setPropertyDescription(0, "15");
			//Livestock
			GroupAIncidentReport copy27 = new GroupAIncidentReport(incident);
			Offense offense27 = new Offense();
			offense27.setUcrOffenseCode("23A");
			Property propertyDescription27 = new Property();
			propertyDescription27.setPropertyDescription(0, "18");
			GroupAIncidentReport copy28 = new GroupAIncidentReport(incident);
			Offense offense28 = new Offense();
			offense28.setUcrOffenseCode("23B");
			Property propertyDescription28 = new Property();
			propertyDescription28.setPropertyDescription(0, "18");
			//Other Motor Vehicles
			GroupAIncidentReport copy29 = new GroupAIncidentReport(incident);
			Offense offense29 = new Offense();
			offense29.setUcrOffenseCode("23A");
			Property propertyDescription29 = new Property();
			propertyDescription29.setPropertyDescription(0, "24");
			GroupAIncidentReport copy30 = new GroupAIncidentReport(incident);
			Offense offense30 = new Offense();
			offense30.setUcrOffenseCode("23B");
			Property propertyDescription30 = new Property();
			propertyDescription30.setPropertyDescription(0, "24");
			//Recreational Vehicles
			GroupAIncidentReport copy31 = new GroupAIncidentReport(incident);
			Offense offense31 = new Offense();
			offense31.setUcrOffenseCode("23A");
			Property propertyDescription31 = new Property();
			propertyDescription31.setPropertyDescription(0, "28");
			GroupAIncidentReport copy32 = new GroupAIncidentReport(incident);
			Offense offense32 = new Offense();
			offense32.setUcrOffenseCode("23B");
			Property propertyDescription32 = new Property();
			propertyDescription32.setPropertyDescription(0, "28");
			//Structures - Single Occupancy Dwelling
			GroupAIncidentReport copy33 = new GroupAIncidentReport(incident);
			Offense offense33 = new Offense();
			offense33.setUcrOffenseCode("23A");
			Property propertyDescription33 = new Property();
			propertyDescription33.setPropertyDescription(0, "29");
			GroupAIncidentReport copy34 = new GroupAIncidentReport(incident);
			Offense offense34 = new Offense();
			offense34.setUcrOffenseCode("23B");
			Property propertyDescription34 = new Property();
			propertyDescription34.setPropertyDescription(0, "29");
			//Structures - Other Dwellings
			GroupAIncidentReport copy35 = new GroupAIncidentReport(incident);
			Offense offense35 = new Offense();
			offense35.setUcrOffenseCode("23A");
			Property propertyDescription35 = new Property();
			propertyDescription35.setPropertyDescription(0, "30");
			GroupAIncidentReport copy36 = new GroupAIncidentReport(incident);
			Offense offense36 = new Offense();
			offense36.setUcrOffenseCode("23B");
			Property propertyDescription36 = new Property();
			propertyDescription36.setPropertyDescription(0, "30");
			//Structures - Commercial Business
			GroupAIncidentReport copy37 = new GroupAIncidentReport(incident);
			Offense offense37 = new Offense();
			offense37.setUcrOffenseCode("23A");
			Property propertyDescription37 = new Property();
			propertyDescription37.setPropertyDescription(0, "31");
			GroupAIncidentReport copy38 = new GroupAIncidentReport(incident);
			Offense offense38 = new Offense();
			offense38.setUcrOffenseCode("23B");
			Property propertyDescription38 = new Property();
			propertyDescription38.setPropertyDescription(0, "31");
			//Structures - Industrial/Manufacturing
			GroupAIncidentReport copy39 = new GroupAIncidentReport(incident);
			Offense offense39 = new Offense();
			offense39.setUcrOffenseCode("23A");
			Property propertyDescription39 = new Property();
			propertyDescription39.setPropertyDescription(0, "32");
			GroupAIncidentReport copy40 = new GroupAIncidentReport(incident);
			Offense offense40 = new Offense();
			offense40.setUcrOffenseCode("23B");
			Property propertyDescription40 = new Property();
			propertyDescription40.setPropertyDescription(0, "32");
			//Structures - Public/Community
			GroupAIncidentReport copy41 = new GroupAIncidentReport(incident);
			Offense offense41 = new Offense();
			offense41.setUcrOffenseCode("23A");
			Property propertyDescription41 = new Property();
			propertyDescription41.setPropertyDescription(0, "33");
			GroupAIncidentReport copy42 = new GroupAIncidentReport(incident);
			Offense offense42 = new Offense();
			offense42.setUcrOffenseCode("23B");
			Property propertyDescription42 = new Property();
			propertyDescription42.setPropertyDescription(0, "33");
			//Structures - Storage
			GroupAIncidentReport copy43 = new GroupAIncidentReport(incident);
			Offense offense43 = new Offense();
			offense43.setUcrOffenseCode("23A");
			Property propertyDescription43 = new Property();
			propertyDescription43.setPropertyDescription(0, "34");
			GroupAIncidentReport copy44 = new GroupAIncidentReport(incident);
			Offense offense44 = new Offense();
			offense44.setUcrOffenseCode("23B");
			Property propertyDescription44 = new Property();
			propertyDescription44.setPropertyDescription(0, "34");
			//Structures - Other
			GroupAIncidentReport copy45 = new GroupAIncidentReport(incident);
			Offense offense45 = new Offense();
			offense45.setUcrOffenseCode("23A");
			Property propertyDescription45 = new Property();
			propertyDescription45.setPropertyDescription(0, "35");
			GroupAIncidentReport copy46 = new GroupAIncidentReport(incident);
			Offense offense46 = new Offense();
			offense46.setUcrOffenseCode("23B");
			Property propertyDescription46 = new Property();
			propertyDescription46.setPropertyDescription(0, "35");
			//Trucks
			GroupAIncidentReport copy47 = new GroupAIncidentReport(incident);
			Offense offense47 = new Offense();
			offense47.setUcrOffenseCode("23A");
			Property propertyDescription47 = new Property();
			propertyDescription47.setPropertyDescription(0, "37");
			GroupAIncidentReport copy48 = new GroupAIncidentReport(incident);
			Offense offense48 = new Offense();
			offense48.setUcrOffenseCode("23B");
			Property propertyDescription48 = new Property();
			propertyDescription48.setPropertyDescription(0, "37");
			//Watercraft
			GroupAIncidentReport copy49 = new GroupAIncidentReport(incident);
			Offense offense49 = new Offense();
			offense49.setUcrOffenseCode("23A");
			Property propertyDescription49 = new Property();
			propertyDescription49.setPropertyDescription(0, "39");
			GroupAIncidentReport copy50 = new GroupAIncidentReport(incident);
			Offense offense50 = new Offense();
			offense50.setUcrOffenseCode("23B");
			Property propertyDescription50 = new Property();
			propertyDescription50.setPropertyDescription(0, "39");
			//Property descriptions that cannot be shoplifted due to other UCR definitions 
			//(aircraft, vehicles, structures, a person’s identity, watercraft, etc.) are illogical with 23C=Shoplifting
			GroupAIncidentReport copy51 = new GroupAIncidentReport(incident);
			Offense offense51 = new Offense();
			offense51.setUcrOffenseCode("23C");
			Property propertyDescription51 = new Property();
			propertyDescription51.setPropertyDescription(0, "01");
			//Automobiles
			GroupAIncidentReport copy52 = new GroupAIncidentReport(incident);
			Offense offense52 = new Offense();
			offense52.setUcrOffenseCode("23C");
			Property propertyDescription52 = new Property();
			propertyDescription52.setPropertyDescription(0, "03");
			//Buses
			GroupAIncidentReport copy53 = new GroupAIncidentReport(incident);
			Offense offense53 = new Offense();
			offense53.setUcrOffenseCode("23C");
			Property propertyDescription53 = new Property();
			propertyDescription53.setPropertyDescription(0, "05");
			//Farm Equipment
			GroupAIncidentReport copy54 = new GroupAIncidentReport(incident);
			Offense offense54 = new Offense();
			offense54.setUcrOffenseCode("23C");
			Property propertyDescription54 = new Property();
			propertyDescription54.setPropertyDescription(0, "12");
			//Heavy Contruction/Industrial Equipment
			GroupAIncidentReport copy55 = new GroupAIncidentReport(incident);
			Offense offense55 = new Offense();
			offense55.setUcrOffenseCode("23C");
			Property propertyDescription55 = new Property();
			propertyDescription55.setPropertyDescription(0, "15");
			//Livestock
			GroupAIncidentReport copy56 = new GroupAIncidentReport(incident);
			Offense offense56 = new Offense();
			offense56.setUcrOffenseCode("23C");
			Property propertyDescription56 = new Property();
			propertyDescription56.setPropertyDescription(0, "18");
			//Other Motor Vehicles
			GroupAIncidentReport copy57 = new GroupAIncidentReport(incident);
			Offense offense57 = new Offense();
			offense57.setUcrOffenseCode("23C");
			Property propertyDescription57 = new Property();
			propertyDescription57.setPropertyDescription(0, "24");
			//Recreational Vehicles
			GroupAIncidentReport copy58 = new GroupAIncidentReport(incident);
			Offense offense58 = new Offense();
			offense58.setUcrOffenseCode("23C");
			Property propertyDescription58 = new Property();
			propertyDescription58.setPropertyDescription(0, "28");
			//Structures - Single Occupancy Dwellings
			GroupAIncidentReport copy59 = new GroupAIncidentReport(incident);
			Offense offense59 = new Offense();
			offense59.setUcrOffenseCode("23C");
			Property propertyDescription59 = new Property();
			propertyDescription59.setPropertyDescription(0, "29");
			//Structures - Other Dwellings
			GroupAIncidentReport copy60 = new GroupAIncidentReport(incident);
			Offense offense60 = new Offense();
			offense60.setUcrOffenseCode("23C");
			Property propertyDescription60 = new Property();
			propertyDescription60.setPropertyDescription(0, "30");
			//Structures - Commercial/Business
			GroupAIncidentReport copy61 = new GroupAIncidentReport(incident);
			Offense offense61 = new Offense();
			offense61.setUcrOffenseCode("23C");
			Property propertyDescription61 = new Property();
			propertyDescription61.setPropertyDescription(0, "31");
			//Structures - Industrial/Manufacturing
			GroupAIncidentReport copy62 = new GroupAIncidentReport(incident);
			Offense offense62 = new Offense();
			offense62.setUcrOffenseCode("23C");
			Property propertyDescription62 = new Property();
			propertyDescription62.setPropertyDescription(0, "32");
			//Structures - Public/Community
			GroupAIncidentReport copy63 = new GroupAIncidentReport(incident);
			Offense offense63 = new Offense();
			offense63.setUcrOffenseCode("23C");
			Property propertyDescription63 = new Property();
			propertyDescription63.setPropertyDescription(0, "33");
			//Structures - Storage
			GroupAIncidentReport copy64 = new GroupAIncidentReport(incident);
			Offense offense64 = new Offense();
			offense64.setUcrOffenseCode("23C");
			Property propertyDescription64 = new Property();
			propertyDescription64.setPropertyDescription(0, "34");
			//Structures - Other
			GroupAIncidentReport copy65 = new GroupAIncidentReport(incident);
			Offense offense65 = new Offense();
			offense65.setUcrOffenseCode("23C");
			Property propertyDescription65 = new Property();
			propertyDescription65.setPropertyDescription(0, "35");
			//Trucks
			GroupAIncidentReport copy66 = new GroupAIncidentReport(incident);
			Offense offense66 = new Offense();
			offense66.setUcrOffenseCode("23C");
			Property propertyDescription66 = new Property();
			propertyDescription66.setPropertyDescription(0, "37");
			//Watercraft
			GroupAIncidentReport copy67 = new GroupAIncidentReport(incident);
			Offense offense67 = new Offense();
			offense67.setUcrOffenseCode("23C");
			Property propertyDescription67 = new Property();
			propertyDescription67.setPropertyDescription(0, "39");
			//Property descriptions for vehicles and structures are illogical with 23D=Theft from Building, 
			//23E=Theft from Coin-Operated Machine or Device, 23F=Theft from Motor Vehicle, and 
			//23G=Theft of Motor Vehicle Parts or Accessories
			//Automobiles
			GroupAIncidentReport copy68 = new GroupAIncidentReport(incident);
			Offense offense68 = new Offense();
			offense68.setUcrOffenseCode("23D");
			Property propertyDescription68 = new Property();
			propertyDescription68.setPropertyDescription(0, "03");
			GroupAIncidentReport copy69 = new GroupAIncidentReport(incident);
			Offense offense69 = new Offense();
			offense69.setUcrOffenseCode("23E");
			Property propertyDescription69 = new Property();
			propertyDescription69.setPropertyDescription(0, "03");
			GroupAIncidentReport copy70 = new GroupAIncidentReport(incident);
			Offense offense70 = new Offense();
			offense70.setUcrOffenseCode("23F");
			Property propertyDescription70 = new Property();
			propertyDescription70.setPropertyDescription(0, "03");
			GroupAIncidentReport copy71 = new GroupAIncidentReport(incident);
			Offense offense71 = new Offense();
			offense71.setUcrOffenseCode("23G");
			Property propertyDescription71 = new Property();
			propertyDescription71.setPropertyDescription(0, "03");
			//Buses
			GroupAIncidentReport copy72 = new GroupAIncidentReport(incident);
			Offense offense72 = new Offense();
			offense72.setUcrOffenseCode("23D");
			Property propertyDescription72 = new Property();
			propertyDescription72.setPropertyDescription(0, "05");
			GroupAIncidentReport copy73 = new GroupAIncidentReport(incident);
			Offense offense73 = new Offense();
			offense73.setUcrOffenseCode("23E");
			Property propertyDescription73 = new Property();
			propertyDescription73.setPropertyDescription(0, "05");
			GroupAIncidentReport copy74 = new GroupAIncidentReport(incident);
			Offense offense74 = new Offense();
			offense74.setUcrOffenseCode("23F");
			Property propertyDescription74 = new Property();
			propertyDescription74.setPropertyDescription(0, "05");
			GroupAIncidentReport copy75 = new GroupAIncidentReport(incident);
			Offense offense75 = new Offense();
			offense75.setUcrOffenseCode("23G");
			Property propertyDescription75 = new Property();
			propertyDescription75.setPropertyDescription(0, "05");
			//Other Motor Vehicles
			GroupAIncidentReport copy76 = new GroupAIncidentReport(incident);
			Offense offense76 = new Offense();
			offense76.setUcrOffenseCode("23D");
			Property propertyDescription76 = new Property();
			propertyDescription76.setPropertyDescription(0, "24");
			GroupAIncidentReport copy77 = new GroupAIncidentReport(incident);
			Offense offense77 = new Offense();
			offense77.setUcrOffenseCode("23E");
			Property propertyDescription77 = new Property();
			propertyDescription77.setPropertyDescription(0, "24");
			GroupAIncidentReport copy78 = new GroupAIncidentReport(incident);
			Offense offense78 = new Offense();
			offense78.setUcrOffenseCode("23F");
			Property propertyDescription78 = new Property();
			propertyDescription78.setPropertyDescription(0, "24");
			GroupAIncidentReport copy79 = new GroupAIncidentReport(incident);
			Offense offense79 = new Offense();
			offense79.setUcrOffenseCode("23G");
			Property propertyDescription79 = new Property();
			propertyDescription79.setPropertyDescription(0, "24");
			//Recreational Vehicles
			GroupAIncidentReport copy80 = new GroupAIncidentReport(incident);
			Offense offense80 = new Offense();
			offense80.setUcrOffenseCode("23D");
			Property propertyDescription80 = new Property();
			propertyDescription80.setPropertyDescription(0, "28");
			GroupAIncidentReport copy81 = new GroupAIncidentReport(incident);
			Offense offense81 = new Offense();
			offense81.setUcrOffenseCode("23E");
			Property propertyDescription81 = new Property();
			propertyDescription81.setPropertyDescription(0, "28");
			GroupAIncidentReport copy82 = new GroupAIncidentReport(incident);
			Offense offense82 = new Offense();
			offense82.setUcrOffenseCode("23F");
			Property propertyDescription82 = new Property();
			propertyDescription82.setPropertyDescription(0, "28");
			GroupAIncidentReport copy83 = new GroupAIncidentReport(incident);
			Offense offense83 = new Offense();
			offense83.setUcrOffenseCode("23G");
			Property propertyDescription83 = new Property();
			propertyDescription83.setPropertyDescription(0, "28");
			//Structures - Single Occupancy Dwellings
			GroupAIncidentReport copy84 = new GroupAIncidentReport(incident);
			Offense offense84 = new Offense();
			offense84.setUcrOffenseCode("23D");
			Property propertyDescription84 = new Property();
			propertyDescription84.setPropertyDescription(0, "29");
			GroupAIncidentReport copy85 = new GroupAIncidentReport(incident);
			Offense offense85 = new Offense();
			offense85.setUcrOffenseCode("23E");
			Property propertyDescription85 = new Property();
			propertyDescription85.setPropertyDescription(0, "29");
			GroupAIncidentReport copy86 = new GroupAIncidentReport(incident);
			Offense offense86 = new Offense();
			offense86.setUcrOffenseCode("23F");
			Property propertyDescription86 = new Property();
			propertyDescription86.setPropertyDescription(0, "29");
			GroupAIncidentReport copy87 = new GroupAIncidentReport(incident);
			Offense offense87 = new Offense();
			offense87.setUcrOffenseCode("23G");
			Property propertyDescription87 = new Property();
			propertyDescription87.setPropertyDescription(0, "29");
			//Structures - Other Dwellings
			GroupAIncidentReport copy88 = new GroupAIncidentReport(incident);
			Offense offense88 = new Offense();
			offense88.setUcrOffenseCode("23D");
			Property propertyDescription88 = new Property();
			propertyDescription88.setPropertyDescription(0, "30");
			GroupAIncidentReport copy89 = new GroupAIncidentReport(incident);
			Offense offense89 = new Offense();
			offense89.setUcrOffenseCode("23E");
			Property propertyDescription89 = new Property();
			propertyDescription89.setPropertyDescription(0, "30");
			GroupAIncidentReport copy90 = new GroupAIncidentReport(incident);
			Offense offense90 = new Offense();
			offense90.setUcrOffenseCode("23F");
			Property propertyDescription90 = new Property();
			propertyDescription90.setPropertyDescription(0, "30");
			GroupAIncidentReport copy91 = new GroupAIncidentReport(incident);
			Offense offense91 = new Offense();
			offense91.setUcrOffenseCode("23G");
			Property propertyDescription91 = new Property();
			propertyDescription91.setPropertyDescription(0, "30");
			//Structures - Commercial/Business
			GroupAIncidentReport copy92 = new GroupAIncidentReport(incident);
			Offense offense92 = new Offense();
			offense92.setUcrOffenseCode("23D");
			Property propertyDescription92 = new Property();
			propertyDescription92.setPropertyDescription(0, "31");
			GroupAIncidentReport copy93 = new GroupAIncidentReport(incident);
			Offense offense93 = new Offense();
			offense93.setUcrOffenseCode("23E");
			Property propertyDescription93 = new Property();
			propertyDescription93.setPropertyDescription(0, "31");
			GroupAIncidentReport copy94 = new GroupAIncidentReport(incident);
			Offense offense94 = new Offense();
			offense94.setUcrOffenseCode("23F");
			Property propertyDescription94 = new Property();
			propertyDescription94.setPropertyDescription(0, "31");
			GroupAIncidentReport copy95 = new GroupAIncidentReport(incident);
			Offense offense95 = new Offense();
			offense95.setUcrOffenseCode("23G");
			Property propertyDescription95 = new Property();
			propertyDescription95.setPropertyDescription(0, "31");
			//Structures - Industrial/Manufacturing
			GroupAIncidentReport copy96 = new GroupAIncidentReport(incident);
			Offense offense96 = new Offense();
			offense96.setUcrOffenseCode("23D");
			Property propertyDescription96 = new Property();
			propertyDescription96.setPropertyDescription(0, "32");
			GroupAIncidentReport copy97 = new GroupAIncidentReport(incident);
			Offense offense97 = new Offense();
			offense97.setUcrOffenseCode("23E");
			Property propertyDescription97 = new Property();
			propertyDescription97.setPropertyDescription(0, "32");
			GroupAIncidentReport copy98 = new GroupAIncidentReport(incident);
			Offense offense98 = new Offense();
			offense98.setUcrOffenseCode("23F");
			Property propertyDescription98 = new Property();
			propertyDescription98.setPropertyDescription(0, "32");
			GroupAIncidentReport copy99 = new GroupAIncidentReport(incident);
			Offense offense99 = new Offense();
			offense99.setUcrOffenseCode("23G");
			Property propertyDescription99 = new Property();
			propertyDescription99.setPropertyDescription(0, "32");
			//Structures - Public/Community
			GroupAIncidentReport copy100 = new GroupAIncidentReport(incident);
			Offense offense100 = new Offense();
			offense100.setUcrOffenseCode("23D");
			Property propertyDescription100 = new Property();
			propertyDescription100.setPropertyDescription(0, "33");
			GroupAIncidentReport copy101 = new GroupAIncidentReport(incident);
			Offense offense101 = new Offense();
			offense101.setUcrOffenseCode("23E");
			Property propertyDescription101 = new Property();
			propertyDescription101.setPropertyDescription(0, "33");
			GroupAIncidentReport copy102 = new GroupAIncidentReport(incident);
			Offense offense102 = new Offense();
			offense102.setUcrOffenseCode("23F");
			Property propertyDescription102 = new Property();
			propertyDescription102.setPropertyDescription(0, "33");
			GroupAIncidentReport copy103 = new GroupAIncidentReport(incident);
			Offense offense103 = new Offense();
			offense103.setUcrOffenseCode("23G");
			Property propertyDescription103 = new Property();
			propertyDescription103.setPropertyDescription(0, "33");
			//Structures - Storage
			GroupAIncidentReport copy104 = new GroupAIncidentReport(incident);
			Offense offense104 = new Offense();
			offense104.setUcrOffenseCode("23D");
			Property propertyDescription104 = new Property();
			propertyDescription104.setPropertyDescription(0, "34");
			GroupAIncidentReport copy105 = new GroupAIncidentReport(incident);
			Offense offense105 = new Offense();
			offense105.setUcrOffenseCode("23E");
			Property propertyDescription105 = new Property();
			propertyDescription105.setPropertyDescription(0, "34");
			GroupAIncidentReport copy106 = new GroupAIncidentReport(incident);
			Offense offense106 = new Offense();
			offense106.setUcrOffenseCode("23F");
			Property propertyDescription106 = new Property();
			propertyDescription106.setPropertyDescription(0, "34");
			GroupAIncidentReport copy107 = new GroupAIncidentReport(incident);
			Offense offense107 = new Offense();
			offense107.setUcrOffenseCode("23G");
			Property propertyDescription107 = new Property();
			propertyDescription107.setPropertyDescription(0, "34");
			//Structures - Other
			GroupAIncidentReport copy108 = new GroupAIncidentReport(incident);
			Offense offense108 = new Offense();
			offense108.setUcrOffenseCode("23D");
			Property propertyDescription108 = new Property();
			propertyDescription108.setPropertyDescription(0, "35");
			GroupAIncidentReport copy109 = new GroupAIncidentReport(incident);
			Offense offense109 = new Offense();
			offense109.setUcrOffenseCode("23E");
			Property propertyDescription109 = new Property();
			propertyDescription109.setPropertyDescription(0, "35");
			GroupAIncidentReport copy110 = new GroupAIncidentReport(incident);
			Offense offense110 = new Offense();
			offense110.setUcrOffenseCode("23F");
			Property propertyDescription110 = new Property();
			propertyDescription110.setPropertyDescription(0, "35");
			GroupAIncidentReport copy111 = new GroupAIncidentReport(incident);
			Offense offense111 = new Offense();
			offense111.setUcrOffenseCode("23G");
			Property propertyDescription111 = new Property();
			propertyDescription111.setPropertyDescription(0, "35");
			//Trucks
			GroupAIncidentReport copy112 = new GroupAIncidentReport(incident);
			Offense offense112 = new Offense();
			offense112.setUcrOffenseCode("23D");
			Property propertyDescription112 = new Property();
			propertyDescription112.setPropertyDescription(0, "37");
			GroupAIncidentReport copy113 = new GroupAIncidentReport(incident);
			Offense offense113 = new Offense();
			offense113.setUcrOffenseCode("23E");
			Property propertyDescription113 = new Property();
			propertyDescription113.setPropertyDescription(0, "37");
			GroupAIncidentReport copy114 = new GroupAIncidentReport(incident);
			Offense offense114 = new Offense();
			offense114.setUcrOffenseCode("23F");
			Property propertyDescription114 = new Property();
			propertyDescription114.setPropertyDescription(0, "37");
			GroupAIncidentReport copy115 = new GroupAIncidentReport(incident);
			Offense offense115 = new Offense();
			offense115.setUcrOffenseCode("23G");
			Property propertyDescription115 = new Property();
			propertyDescription115.setPropertyDescription(0, "37");
			//Property descriptions for vehicles are illogical with 23H=All Other Larceny
			GroupAIncidentReport copy116 = new GroupAIncidentReport(incident);
			Offense offense116 = new Offense();
			offense116.setUcrOffenseCode("23H");
			Property propertyDescription116 = new Property();
			propertyDescription116.setPropertyDescription(0, "03");
			GroupAIncidentReport copy117 = new GroupAIncidentReport(incident);
			Offense offense117 = new Offense();
			offense117.setUcrOffenseCode("23H");
			Property propertyDescription117 = new Property();
			propertyDescription117.setPropertyDescription(0, "05");
			GroupAIncidentReport copy118 = new GroupAIncidentReport(incident);
			Offense offense118 = new Offense();
			offense118.setUcrOffenseCode("23H");
			Property propertyDescription118 = new Property();
			propertyDescription118.setPropertyDescription(0, "24");
			GroupAIncidentReport copy119 = new GroupAIncidentReport(incident);
			Offense offense119 = new Offense();
			offense119.setUcrOffenseCode("23H");
			Property propertyDescription119 = new Property();
			propertyDescription119.setPropertyDescription(0, "28");
			GroupAIncidentReport copy120 = new GroupAIncidentReport(incident);
			Offense offense120 = new Offense();
			offense120.setUcrOffenseCode("23H");
			Property propertyDescription120 = new Property();
			propertyDescription120.setPropertyDescription(0, "37");

			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			incidents.add(copy11);
			incidents.add(copy12);
			incidents.add(copy13);
			incidents.add(copy14);
			incidents.add(copy15);
			incidents.add(copy16);
			incidents.add(copy17);
			incidents.add(copy18);
			incidents.add(copy19);
			incidents.add(copy20);
			incidents.add(copy21);
			incidents.add(copy22);
			incidents.add(copy23);
			incidents.add(copy24);
			incidents.add(copy25);
			incidents.add(copy26);
			incidents.add(copy27);
			incidents.add(copy28);
			incidents.add(copy29);
			incidents.add(copy30);
			incidents.add(copy31);
			incidents.add(copy32);
			incidents.add(copy33);
			incidents.add(copy34);
			incidents.add(copy35);
			incidents.add(copy36);
			incidents.add(copy37);
			incidents.add(copy38);
			incidents.add(copy39);
			incidents.add(copy40);
			incidents.add(copy41);
			incidents.add(copy42);
			incidents.add(copy43);
			incidents.add(copy44);
			incidents.add(copy45);
			incidents.add(copy46);
			incidents.add(copy47);
			incidents.add(copy48);
			incidents.add(copy49);
			incidents.add(copy50);
			incidents.add(copy51);
			incidents.add(copy52);
			incidents.add(copy53);
			incidents.add(copy54);
			incidents.add(copy55);
			incidents.add(copy56);
			incidents.add(copy57);
			incidents.add(copy58);
			incidents.add(copy59);
			incidents.add(copy60);
			incidents.add(copy61);
			incidents.add(copy62);
			incidents.add(copy63);
			incidents.add(copy64);
			incidents.add(copy65);
			incidents.add(copy66);
			incidents.add(copy67);
			incidents.add(copy68);
			incidents.add(copy69);
			incidents.add(copy70);
			incidents.add(copy71);
			incidents.add(copy72);
			incidents.add(copy73);
			incidents.add(copy74);
			incidents.add(copy75);
			incidents.add(copy76);
			incidents.add(copy77);
			incidents.add(copy78);
			incidents.add(copy79);
			incidents.add(copy80);
			incidents.add(copy81);
			incidents.add(copy82);
			incidents.add(copy83);
			incidents.add(copy84);
			incidents.add(copy85);
			incidents.add(copy86);
			incidents.add(copy87);
			incidents.add(copy88);
			incidents.add(copy89);
			incidents.add(copy90);
			incidents.add(copy91);
			incidents.add(copy92);
			incidents.add(copy93);
			incidents.add(copy94);
			incidents.add(copy95);
			incidents.add(copy96);
			incidents.add(copy97);
			incidents.add(copy98);
			incidents.add(copy99);
			incidents.add(copy100);
			incidents.add(copy101);
			incidents.add(copy102);
			incidents.add(copy103);
			incidents.add(copy104);
			incidents.add(copy105);
			incidents.add(copy106);
			incidents.add(copy107);
			incidents.add(copy108);
			incidents.add(copy109);
			incidents.add(copy110);
			incidents.add(copy111);
			incidents.add(copy112);
			incidents.add(copy113);
			incidents.add(copy114);
			incidents.add(copy115);
			incidents.add(copy116);
			incidents.add(copy117);
			incidents.add(copy118);
			incidents.add(copy119);
			incidents.add(copy120);
						
			return incidents;
		});
		
		
		
		
		groupATweakerMap.put(401, incident -> {
			// The referenced data element in a Group A Incident Report
			// Segment 4 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
		});

		groupATweakerMap.put(501, incident -> {
			// The referenced data element in a Group A Incident Report
			// Segment 5 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
		});

		groupATweakerMap.put(601, incident -> {
			// The referenced data element in a Group A Incident Report
			// Segment 6 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentDate(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.setExceptionalClearanceCode(null);
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			return incidents;
		});

	}

}
