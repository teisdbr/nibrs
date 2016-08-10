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
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.LocationTypeCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.PropertySegment;

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
			// The referenced data element in a Group A Incident AbstractReport
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
			// The referenced data element in a Group A Incident AbstractReport
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
			// Data Element 6 (UCR OffenseSegment Code) contains a Cargo Theft-related offense.
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
			// then the report date would be entered instead and must be indicated with an "R" in the AbstractReport
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
			// A Group A Incident AbstractReport was submitted with a date entered into Data Element 3 (Incident Date/Hour)
			// that is earlier than January 1 of the previous year, using the Month of Tape and Year of Tape as a reference point,
			// e.g., if the Month of Tape and Year of Tape contain a value of 01/1999, but the incident date is 12/25/1997, the incident will be rejected.
			// Volume 2, section I, provides specifications concerning the FBI�s 2-year database.
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
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 2 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			//Data Element 1
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			//Data  Element 2
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			//Data Element 6
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.getOffenses().get(0).setUcrOffenseCode(null);
			//Data Element 7
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.getOffenses().get(0).setOffenseAttemptedCompleted(null);
			//Data Element 8
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			copy7.getOffenses().get(0).setOffendersSuspectedOfUsing(0, null);
			//Data Element 8A
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			copy8.getOffenses().get(0).setBiasMotivation(0, null);
			//Data Element 9
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			copy9.getOffenses().get(0).setLocationType(null);
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			copy10.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			
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

		groupATweakerMap.put(204, incident -> {
			// The referenced data element in a Group A Incident AbstractReport Segment 2 must
			// be populated with a valid data value.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setOri("1234567890123");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setOri("ZZ123456789");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setCityIndicator("ZZ12");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.getOffenses().get(0).setUcrOffenseCode("XXX");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.getOffenses().get(0).setBiasMotivation(0, "10");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.getOffenses().get(0).setLocationType("99");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			copy7.getOffenses().get(0).setNumberOfPremisesEntered(100);
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			copy8.getOffenses().get(0).setMethodOfEntry(null);
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			copy9.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			copy10.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "10");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(copy);
			copy11.getOffenses().get(0).setAutomaticWeaponIndicator(0, "B");
			
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
			//Data Element 8			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "A");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(1, "C");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(2, "C");
			//Data Element 8A
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setBiasMotivation(0, "15");
			copy2.getOffenses().get(0).setBiasMotivation(0, "26");
			copy2.getOffenses().get(0).setBiasMotivation(0, "26");
			//Data Element 12
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setTypeOfCriminalActivity(1, "J");
			copy3.getOffenses().get(0).setTypeOfCriminalActivity(2, "P");
			//Data Element 13
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "11");
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(1, "11");
			
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
			//Data Element 8
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "A");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(1, "N");
			//Data Element 8A
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setBiasMotivation(0, "15");
			copy2.getOffenses().get(0).setBiasMotivation(1, "88");
			//Data Element 12
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setTypeOfCriminalActivity(0, "J");
			copy3.getOffenses().get(0).setTypeOfCriminalActivity(1, "N");
			//Data Element 13
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "11");
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(1, "99");
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			return incidents;

		});

		groupATweakerMap.put(220, incident -> {
			// Data Element 12 (Type Criminal Activity/Gang Information) Must be populated with a valid data value and cannot be 
			//blank when Data Element 6 (UCR OffenseSegment Code) is:
			// 250=Counterfeiting/Forgery
			// 280=Stolen PropertySegment Offenses
			// 35A=Drug/Narcotic Violations
			// 35B=Drug Equipment Violations
			// 39C=Gambling Equipment Violations
			// 370=Pornography/Obscene Material
			// 520=Weapon Law Violations
			// 720=Animal Cruelty
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//TypeOfCriminalActivity cannot be blank
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("250");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("280");
			copy2.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setUcrOffenseCode("35A");
			copy3.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setUcrOffenseCode("35B");
			copy4.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getOffenses().get(0).setUcrOffenseCode("39C");
			copy5.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			copy6.getOffenses().get(0).setUcrOffenseCode("370");
			copy6.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			copy7.getOffenses().get(0).setUcrOffenseCode("520");
			copy7.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("720");
			copy8.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			//TypeOfCriminalActivity must be valid
			GroupAIncidentReport copy9 = new GroupAIncidentReport(incident);
			copy9.getOffenses().get(0).setUcrOffenseCode("250");
			copy9.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(incident);
			copy10.getOffenses().get(0).setUcrOffenseCode("280");
			copy10.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(incident);
			copy11.getOffenses().get(0).setUcrOffenseCode("35A");
			copy11.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
			GroupAIncidentReport copy12 = new GroupAIncidentReport(incident);
			copy12.getOffenses().get(0).setUcrOffenseCode("35B");
			copy12.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
			GroupAIncidentReport copy13 = new GroupAIncidentReport(incident);
			copy13.getOffenses().get(0).setUcrOffenseCode("39C");
			copy13.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
			GroupAIncidentReport copy14 = new GroupAIncidentReport(incident);
			copy14.getOffenses().get(0).setUcrOffenseCode("370");
			copy14.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
			GroupAIncidentReport copy15 = new GroupAIncidentReport(incident);
			copy15.getOffenses().get(0).setUcrOffenseCode("520");
			copy15.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
			GroupAIncidentReport copy16 = new GroupAIncidentReport(incident);
			copy16.getOffenses().get(0).setUcrOffenseCode("720");
			copy16.getOffenses().get(0).setTypeOfCriminalActivity(0, "Z");
						
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
			
			return incidents;

		});

		groupATweakerMap.put(221, incident -> {
			// Data Element 13 (Type Weapon/Force Involved) Must be populated with a valid data value and cannot be blank
			//when Data Element 6 (UCR OffenseSegment Code) is:
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
			//TypeOfWeaponForceInvolved cannot be blank
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("9A");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("9B");
			copy2.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setUcrOffenseCode("9C");
			copy3.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setUcrOffenseCode("100");
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getOffenses().get(0).setUcrOffenseCode("11A");
			copy5.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			copy6.getOffenses().get(0).setUcrOffenseCode("11B");
			copy6.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			copy7.getOffenses().get(0).setUcrOffenseCode("11C");
			copy7.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("11D");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy9 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("120");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy10 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("13A");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy11 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("13B");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy12 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("210");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy13 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("520");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy14 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("64A");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy15 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("64B");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			//TypeofWeaponForceInvolved must be valid.
			GroupAIncidentReport copy16 = new GroupAIncidentReport(incident);
			copy16.getOffenses().get(0).setUcrOffenseCode("9A");
			copy16.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy17 = new GroupAIncidentReport(incident);
			copy17.getOffenses().get(0).setUcrOffenseCode("9B");
			copy17.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy18 = new GroupAIncidentReport(incident);
			copy18.getOffenses().get(0).setUcrOffenseCode("9C");
			copy18.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy19 = new GroupAIncidentReport(incident);
			copy19.getOffenses().get(0).setUcrOffenseCode("100");
			copy19.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy20 = new GroupAIncidentReport(incident);
			copy20.getOffenses().get(0).setUcrOffenseCode("11A");
			copy20.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy21 = new GroupAIncidentReport(incident);
			copy21.getOffenses().get(0).setUcrOffenseCode("11B");
			copy21.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy22 = new GroupAIncidentReport(incident);
			copy22.getOffenses().get(0).setUcrOffenseCode("11C");
			copy22.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy23 = new GroupAIncidentReport(incident);
			copy23.getOffenses().get(0).setUcrOffenseCode("11D");
			copy23.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy24 = new GroupAIncidentReport(incident);
			copy24.getOffenses().get(0).setUcrOffenseCode("120");
			copy24.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy25 = new GroupAIncidentReport(incident);
			copy25.getOffenses().get(0).setUcrOffenseCode("13A");
			copy25.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy26 = new GroupAIncidentReport(incident);
			copy26.getOffenses().get(0).setUcrOffenseCode("13B");
			copy26.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy27 = new GroupAIncidentReport(incident);
			copy27.getOffenses().get(0).setUcrOffenseCode("210");
			copy27.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy28 = new GroupAIncidentReport(incident);
			copy28.getOffenses().get(0).setUcrOffenseCode("520");
			copy28.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy29 = new GroupAIncidentReport(incident);
			copy29.getOffenses().get(0).setUcrOffenseCode("64A");
			copy29.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			GroupAIncidentReport copy30 = new GroupAIncidentReport(incident);
			copy30.getOffenses().get(0).setUcrOffenseCode("64B");
			copy30.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "Z");
			
							
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
			
					
			return incidents;

		});

	
		groupATweakerMap.put(251, incident -> {
			// (OffenseSegment Attempted/Completed) Must be a valid code of A=Attempted or C=Completed.
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			ret.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(252, incident -> {

			// When number of premises is entered location type must be 14 or 19
			// and UCR OffenseSegment Code must be Burglary.

			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();

			Set<OffenseCode> offenseCodeSet = EnumSet.allOf(OffenseCode.class);
			offenseCodeSet.remove(OffenseCode._220); // Burglary

			Set<LocationTypeCode> locationTypeCodeSet = LocationTypeCode.asSet();
			locationTypeCodeSet.remove(LocationTypeCode._14);
			locationTypeCodeSet.remove(LocationTypeCode._19);

			for (OffenseCode oc : offenseCodeSet) {
				for (LocationTypeCode ltc : locationTypeCodeSet) {
					GroupAIncidentReport copy = new GroupAIncidentReport(incident);
					OffenseSegment offense = new OffenseSegment();
					offense.setNumberOfPremisesEntered(2);
					offense.setLocationType(ltc.code);
					offense.setUcrOffenseCode(oc.code);
					incident.addOffense(offense);
					incidents.add(copy);
				}
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				OffenseSegment offense = new OffenseSegment();
				offense.setNumberOfPremisesEntered(2);
				offense.setLocationType(LocationTypeCode._14.code);
				offense.setUcrOffenseCode(oc.code);
				incident.addOffense(offense);
				incidents.add(copy);
			}

			for (LocationTypeCode ltc : locationTypeCodeSet) {
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				OffenseSegment offense = new OffenseSegment();
				offense.setNumberOfPremisesEntered(2);
				offense.setLocationType(ltc.code);
				offense.setUcrOffenseCode(OffenseCode._220.code);
				incident.addOffense(offense);
				incidents.add(copy);
			}

			return incidents;

		});

		
		groupATweakerMap.put(253, incident -> {
			// Data Element 11 (Method of Entry) Data Element was not entered; it must be entered
			// when UCR OffenseSegment Code of 220=Burglary has been entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getOffenses().get(0).setMethodOfEntry(null);
			
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(254, incident -> {
			// (Method of Entry) Data Element only applies to UCR OffenseSegment Code of 220=Burglary.
			// Since a burglary offense was not entered, the Method of Entry should not have been entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setMethodOfEntry("F");
			
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(255, incident -> {
			// Data Element 13(Automatic Weapon Indicator) Must be A=Automatic or blank=Not Automatic
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setAutomaticWeaponIndicator(0, "F");
			
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(256, incident -> {
			// OffenseSegment Attempted/Completed, Data Element 7, must be a valid code of A=Attempted or C=Completed if UCR code is Homicide
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
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getOffenses().get(0).setLocationType("14");
			copy.getOffenses().get(0).setNumberOfPremisesEntered(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("220");
			copy2.getOffenses().get(0).setLocationType("19");
			copy2.getOffenses().get(0).setNumberOfPremisesEntered(null);
			
					
			incidents.add(copy);
			incidents.add(copy2);
			
			return incidents;

		});

		groupATweakerMap.put(258, incident -> {
			// (Automatic Weapon Indicator) In Data Element 13 (Type of Weapon/Force Involved), A=Automatic 
			//is the third character of code. It is valid only with the following codes:
			// 11=Firearm (Type Not Stated)
			// 12=Handgun
			// 13=Rifle
			// 15=Other Firearm
			// A weapon code other than those mentioned was entered with the automatic indicator. An automatic weapon is, by definition, a firearm.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setAutomaticWeaponIndicator(0, "A");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "20");

			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(262, incident -> {
			// When a Group A Incident Report is submitted, the individual segments
			// comprising the incident cannot contain duplicates.
			// In this case, two OffenseSegment Segments were submitted having the same
			// offense in Data Element 6 (UCR OffenseSegment Code).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("13A");
			offense2.setTypeOfCriminalActivity(0, "J");
			offense2.setOffenseAttemptedCompleted("C");
			offense2.setTypeOfWeaponForceInvolved(0, "99");
			offense2.setOffendersSuspectedOfUsing(0, "N");
			offense2.setBiasMotivation(0, "15");
			offense2.setLocationType("15");
			offense2.setNumberOfPremisesEntered(null);
			offense2.setAutomaticWeaponIndicator(0, " ");
			
					
			copy.addOffense(offense2);
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(263, incident -> {
			// Can be submitted only 10 times for each Group A Incident AbstractReport;
			// 10 offense codes are allowed for each incident.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			//Offense Segment 2
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("13A");
			offense2.setTypeOfCriminalActivity(0, "J");
			offense2.setOffenseAttemptedCompleted("C");
			offense2.setTypeOfWeaponForceInvolved(0, "99");
			offense2.setOffendersSuspectedOfUsing(0, "N");
			offense2.setBiasMotivation(0, "15");
			offense2.setLocationType("15");
			offense2.setNumberOfPremisesEntered(null);
			offense2.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 3
			OffenseSegment offense3 = new OffenseSegment();
			offense3.setUcrOffenseCode("13B");
			offense3.setTypeOfCriminalActivity(0, "J");
			offense3.setOffenseAttemptedCompleted("C");
			offense3.setTypeOfWeaponForceInvolved(0, "99");
			offense3.setOffendersSuspectedOfUsing(0, "N");
			offense3.setBiasMotivation(0, "15");
			offense3.setLocationType("15");
			offense3.setNumberOfPremisesEntered(null);
			offense3.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 4
			OffenseSegment offense4 = new OffenseSegment();
			offense4.setUcrOffenseCode("13C");
			offense4.setTypeOfCriminalActivity(0, "J");
			offense4.setOffenseAttemptedCompleted("C");
			offense4.setTypeOfWeaponForceInvolved(0, "99");
			offense4.setOffendersSuspectedOfUsing(0, "N");
			offense4.setBiasMotivation(0, "15");
			offense4.setLocationType("15");
			offense4.setNumberOfPremisesEntered(null);
			offense4.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 5
			OffenseSegment offense5 = new OffenseSegment();
			offense5.setUcrOffenseCode("510");
			offense5.setTypeOfCriminalActivity(0, "J");
			offense5.setOffenseAttemptedCompleted("C");
			offense5.setTypeOfWeaponForceInvolved(0, "99");
			offense5.setOffendersSuspectedOfUsing(0, "N");
			offense5.setBiasMotivation(0, "15");
			offense5.setLocationType("15");
			offense5.setNumberOfPremisesEntered(null);
			offense5.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 6
			OffenseSegment offense6 = new OffenseSegment();
			offense6.setUcrOffenseCode("26A");
			offense6.setTypeOfCriminalActivity(0, "J");
			offense6.setOffenseAttemptedCompleted("C");
			offense6.setTypeOfWeaponForceInvolved(0, "99");
			offense6.setOffendersSuspectedOfUsing(0, "N");
			offense6.setBiasMotivation(0, "15");
			offense6.setLocationType("15");
			offense6.setNumberOfPremisesEntered(null);
			offense6.setAutomaticWeaponIndicator(0, " ");
			//Offense Segment 7
			OffenseSegment offense7 = new OffenseSegment();
			offense7.setUcrOffenseCode("26B");
			offense7.setTypeOfCriminalActivity(0, "J");
			offense7.setOffenseAttemptedCompleted("C");
			offense7.setTypeOfWeaponForceInvolved(0, "99");
			offense7.setOffendersSuspectedOfUsing(0, "N");
			offense7.setBiasMotivation(0, "15");
			offense7.setLocationType("15");
			offense7.setNumberOfPremisesEntered(null);
			offense7.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 8
			OffenseSegment offense8 = new OffenseSegment();
			offense8.setUcrOffenseCode("26C");
			offense8.setTypeOfCriminalActivity(0, "J");
			offense8.setOffenseAttemptedCompleted("C");
			offense8.setTypeOfWeaponForceInvolved(0, "99");
			offense8.setOffendersSuspectedOfUsing(0, "N");
			offense8.setBiasMotivation(0, "15");
			offense8.setLocationType("15");
			offense8.setNumberOfPremisesEntered(null);
			offense8.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 9
			OffenseSegment offense9 = new OffenseSegment();
			offense9.setUcrOffenseCode("26D");
			offense9.setTypeOfCriminalActivity(0, "J");
			offense9.setOffenseAttemptedCompleted("C");
			offense9.setTypeOfWeaponForceInvolved(0, "99");
			offense9.setOffendersSuspectedOfUsing(0, "N");
			offense9.setBiasMotivation(0, "15");
			offense9.setLocationType("15");
			offense9.setNumberOfPremisesEntered(null);
			offense9.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 10
			OffenseSegment offense10 = new OffenseSegment();
			offense10.setUcrOffenseCode("26E");
			offense10.setTypeOfCriminalActivity(0, "J");
			offense10.setOffenseAttemptedCompleted("C");
			offense10.setTypeOfWeaponForceInvolved(0, "99");
			offense10.setOffendersSuspectedOfUsing(0, "N");
			offense10.setBiasMotivation(0, "15");
			offense10.setLocationType("15");
			offense10.setNumberOfPremisesEntered(null);
			offense10.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 11
			OffenseSegment offense11 = new OffenseSegment();
			offense11.setUcrOffenseCode("39A");
			offense11.setTypeOfCriminalActivity(0, "J");
			offense11.setOffenseAttemptedCompleted("C");
			offense11.setTypeOfWeaponForceInvolved(0, "99");
			offense11.setOffendersSuspectedOfUsing(0, "N");
			offense11.setBiasMotivation(0, "15");
			offense11.setLocationType("15");
			offense11.setNumberOfPremisesEntered(null);
			offense11.setAutomaticWeaponIndicator(0, " ");
			
				
			copy.addOffense(offense2);
			copy.addOffense(offense3);		
			copy.addOffense(offense4);
			copy.addOffense(offense5);
			copy.addOffense(offense6);
			copy.addOffense(offense7);
			copy.addOffense(offense8);
			copy.addOffense(offense9);
			copy.addOffense(offense10);
			copy.addOffense(offense11);

			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(264, incident -> {
			// Group A OffenseSegment code cannot contain a Group B OffenseSegment
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("90A");
					
			incidents.add(copy);
			
			return incidents;
		});

		groupATweakerMap.put(265, incident -> {
			// (Type Weapon/Force Involved) If an OffenseSegment Segment (Level 2) was submitted for 13B=Simple Assault,
			// Data Element 13 (Type Weapon/Force Involved) can only have codes of 40=Personal Weapons,
			// 90=Other, 95=Unknown, and 99=None. All other codes are not valid because they do not relate to a simple assault.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "11");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("13B");
			copy2.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "12");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setUcrOffenseCode("13B");
			copy3.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "13");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setUcrOffenseCode("13B");
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "14");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getOffenses().get(0).setUcrOffenseCode("13B");
			copy5.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "15");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			copy6.getOffenses().get(0).setUcrOffenseCode("13B");
			copy6.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "20");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			copy7.getOffenses().get(0).setUcrOffenseCode("13B");
			copy7.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "30");
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("13B");
			copy8.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "50");
			GroupAIncidentReport copy9 = new GroupAIncidentReport(incident);
			copy9.getOffenses().get(0).setUcrOffenseCode("13B");
			copy9.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "60");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(incident);
			copy10.getOffenses().get(0).setUcrOffenseCode("13B");
			copy10.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "65");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(incident);
			copy11.getOffenses().get(0).setUcrOffenseCode("13B");
			copy11.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "70");
			GroupAIncidentReport copy12 = new GroupAIncidentReport(incident);
			copy12.getOffenses().get(0).setUcrOffenseCode("13B");
			copy12.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "80");
			
			
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
			// reported in the Group A Incident AbstractReport. These should be submitted on another
			// Group A Incident AbstractReport.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("09C");
			offense2.setTypeOfCriminalActivity(0, "J");
			offense2.setOffenseAttemptedCompleted("C");
			offense2.setTypeOfWeaponForceInvolved(0, "1199");
			offense2.setOffendersSuspectedOfUsing(0, "N");
			offense2.setBiasMotivation(0, "03");
			offense2.setLocationType("15");
			offense2.setNumberOfPremisesEntered(null);
			offense2.setAutomaticWeaponIndicator(0, " ");
			
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(267, incident -> {
			// If a homicide offense is submitted, Data Element 13 (Type Weapon/Force Involved)
			// cannot have 99=None. Some type of weapon/force must be used in a homicide offense.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09A");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "99");
			
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(269, incident -> {
			// (Type Weapon/Force Involved) If Data Element 6 (UCR OffenseSegment Code) is 13B=Simple Assault and the
			// weapon involved is 11=Firearm, 12=Handgun, 13=Rifle, 14=Shotgun, or 15=Other Firearm, then the offense
			// should instead be classified as 13A=Aggravated Assault.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "11");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("13B");
			copy2.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "12");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setUcrOffenseCode("13B");
			copy3.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "13");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setUcrOffenseCode("13B");
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "14");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getOffenses().get(0).setUcrOffenseCode("13B");
			copy5.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "15");
			
			
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
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getOffenses().get(0).setBiasMotivation(0, "15");
						
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(301, incident -> {
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 3 is mandatory & must be present.
			//Header Elements cannot be blank
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//Year of Tape
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy.getOffenses().get(0).setUcrOffenseCode("240");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy.getOffenses().get(0).setBiasMotivation(0, "88");
			copy.getOffenses().get(0).setLocationType("20");
			copy.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property = new PropertySegment();
			property.setTypeOfPropertyLoss("7");
			property.setPropertyDescription(0, "03");
			property.setValueOfProperty(0, 000010000);
			property.setNumberOfStolenMotorVehicles(1);
			//Month of Tape
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy2.getOffenses().get(0).setUcrOffenseCode("220");
			copy2.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy2.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy2.getOffenses().get(0).setBiasMotivation(0, "88");
			copy2.getOffenses().get(0).setLocationType("20");
			copy2.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy2.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property2 = new PropertySegment();
			property.setTypeOfPropertyLoss("7");
			property.setPropertyDescription(0, "03");
			property.setValueOfProperty(0, 000010000);
			property.setNumberOfStolenMotorVehicles(1);
			//City Indicator Blank
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setCityIndicator(null);
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy3.getOffenses().get(0).setUcrOffenseCode("220");
			copy3.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy3.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy3.getOffenses().get(0).setBiasMotivation(0, "88");
			copy3.getOffenses().get(0).setLocationType("20");
			copy3.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy3.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property3 = new PropertySegment();
			property.setTypeOfPropertyLoss("7");
			property.setPropertyDescription(0, "03");
			property.setValueOfProperty(0, 000010000);
			property.setNumberOfStolenMotorVehicles(1);
			//ORI Blank
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setOri(null);
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy4.getOffenses().get(0).setUcrOffenseCode("220");
			copy4.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy4.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy4.getOffenses().get(0).setBiasMotivation(0, "88");
			copy4.getOffenses().get(0).setLocationType("20");
			copy4.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy4.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property4 = new PropertySegment();
			property4.setTypeOfPropertyLoss("7");
			property4.setPropertyDescription(0, "03");
			property4.setValueOfProperty(0, 000010000);
			property4.setNumberOfStolenMotorVehicles(1);
			//Incident Number blank
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.setIncidentNumber(null);
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy5.getOffenses().get(0).setUcrOffenseCode("220");
			copy5.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy5.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy5.getOffenses().get(0).setBiasMotivation(0, "88");
			copy5.getOffenses().get(0).setLocationType("20");
			copy5.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy5.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property5 = new PropertySegment();
			property5.setTypeOfPropertyLoss("7");
			property5.setPropertyDescription(0, "03");
			property5.setValueOfProperty(0, 000010000);
			property5.setNumberOfStolenMotorVehicles(1);
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			copy.addProperty(property);
			copy2.addProperty(property2);
			copy3.addProperty(property3);
			copy4.addProperty(property4);
			copy5.addProperty(property5);
			
			
			return incidents;
		});

		groupATweakerMap.put(304, incident -> {
			// The referenced data element in a Group A Incident AbstractReport Segment 3 must
			// be populated with a valid data value.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//Invalid ORI
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setOri("1234567890123");
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy.getOffenses().get(0).setUcrOffenseCode("240");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy.getOffenses().get(0).setBiasMotivation(0, "88");
			copy.getOffenses().get(0).setLocationType("20");
			copy.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property = new PropertySegment();
			property.setTypeOfPropertyLoss("7");
			property.setPropertyDescription(0, "03");
			property.setValueOfProperty(0, 000010000);
			property.setNumberOfStolenMotorVehicles(1);
			//Invalid ORI State Code
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setOri("ZZ123456789");
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy2.getOffenses().get(0).setUcrOffenseCode("240");
			copy2.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy2.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy2.getOffenses().get(0).setBiasMotivation(0, "88");
			copy2.getOffenses().get(0).setLocationType("20");
			copy2.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy2.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property2 = new PropertySegment();
			property2.setTypeOfPropertyLoss("7");
			property2.setPropertyDescription(0, "03");
			property2.setValueOfProperty(0, 000010000);
			property2.setNumberOfStolenMotorVehicles(1);
			//Invalid City Indicator
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setCityIndicator("ZZ12");
			//Valid Offense Segment for an incident including Property Loss
			//Motor Vehicle Theft, Completed, OffenderSuspectedOfUsing N/A, No BiasMotivation, Residence Location, 1 Premise, No forced entry
			copy3.getOffenses().get(0).setUcrOffenseCode("240");
			copy3.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy3.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy3.getOffenses().get(0).setBiasMotivation(0, "88");
			copy3.getOffenses().get(0).setLocationType("20");
			copy3.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy3.getOffenses().get(0).setMethodOfEntry("N");
			//Valid Property Segment: Stolen, Automobiles(1) Value $10,000, 
			PropertySegment property3 = new PropertySegment();
			property3.setTypeOfPropertyLoss("7");
			property3.setPropertyDescription(0, "03");
			property3.setValueOfProperty(0, 000010000);
			property3.setNumberOfStolenMotorVehicles(1);
			//Invalid TypeOfPropertyLoss
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.getOffenses().get(0).setUcrOffenseCode("240");
			copy4.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy4.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy4.getOffenses().get(0).setBiasMotivation(0, "88");
			copy4.getOffenses().get(0).setLocationType("20");
			copy4.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy4.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property4 = new PropertySegment();
			property4.setTypeOfPropertyLoss("9");
			property4.setPropertyDescription(0, "03");
			property4.setValueOfProperty(0, 000010000);
			property4.setNumberOfStolenMotorVehicles(1);
			//Null TypeOfPropertyLoss
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.getOffenses().get(0).setUcrOffenseCode("240");
			copy5.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy5.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy5.getOffenses().get(0).setBiasMotivation(0, "88");
			copy5.getOffenses().get(0).setLocationType("20");
			copy5.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy5.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property5 = new PropertySegment();
			property5.setTypeOfPropertyLoss(null);
			property5.setPropertyDescription(0, "03");
			property5.setValueOfProperty(0, 000010000);
			property5.setNumberOfStolenMotorVehicles(1);
			//Invalid PropertyDescription
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.getOffenses().get(0).setUcrOffenseCode("240");
			copy6.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy6.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy6.getOffenses().get(0).setBiasMotivation(0, "88");
			copy6.getOffenses().get(0).setLocationType("20");
			copy6.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy6.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property6 = new PropertySegment();
			property6.setTypeOfPropertyLoss("7");
			property6.setPropertyDescription(0, "00");
			property6.setValueOfProperty(0, 000010000);
			property6.setNumberOfStolenMotorVehicles(1);
			//Null PropertyDescription
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			copy7.getOffenses().get(0).setUcrOffenseCode("240");
			copy7.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy7.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy7.getOffenses().get(0).setBiasMotivation(0, "88");
			copy7.getOffenses().get(0).setLocationType("20");
			copy7.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy7.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property7 = new PropertySegment();
			property7.setTypeOfPropertyLoss("7");
			property7.setPropertyDescription(0, null);
			property7.setValueOfProperty(0, 000010000);
			property7.setNumberOfStolenMotorVehicles(1);
			//Null ValueOfProperty
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			copy8.getOffenses().get(0).setUcrOffenseCode("240");
			copy8.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy8.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy8.getOffenses().get(0).setBiasMotivation(0, "88");
			copy8.getOffenses().get(0).setLocationType("20");
			copy8.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy8.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property8 = new PropertySegment();
			property8.setTypeOfPropertyLoss("7");
			property8.setPropertyDescription(0, "03");
			property8.setValueOfProperty(0, null);
			property8.setNumberOfStolenMotorVehicles(1);
			//Invalid ValueOfProperty
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			copy9.getOffenses().get(0).setUcrOffenseCode("240");
			copy9.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy9.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy9.getOffenses().get(0).setBiasMotivation(0, "88");
			copy9.getOffenses().get(0).setLocationType("20");
			copy9.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy9.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property9 = new PropertySegment();
			property9.setTypeOfPropertyLoss("7");
			property9.setPropertyDescription(0, "03");
			property9.setValueOfProperty(0, 0000010000);
			property9.setNumberOfStolenMotorVehicles(1);
			//Invalid NumberOfStolenMotorVehicles
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			copy10.getOffenses().get(0).setUcrOffenseCode("240");
			copy10.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy10.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy10.getOffenses().get(0).setBiasMotivation(0, "88");
			copy10.getOffenses().get(0).setLocationType("20");
			copy10.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy10.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property10 = new PropertySegment();
			property10.setTypeOfPropertyLoss("7");
			property10.setPropertyDescription(0, "03");
			property10.setValueOfProperty(0, 000010000);
			property10.setNumberOfStolenMotorVehicles(0);
			property10.setDateRecovered(0, null);
			//Null Date Recovered
			GroupAIncidentReport copy11 = new GroupAIncidentReport(copy);
			copy11.getOffenses().get(0).setUcrOffenseCode("240");
			copy11.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy11.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy11.getOffenses().get(0).setBiasMotivation(0, "88");
			copy11.getOffenses().get(0).setLocationType("20");
			copy11.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy11.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property11 = new PropertySegment();
			property11.setTypeOfPropertyLoss("5");
			property11.setPropertyDescription(0, "03");
			property11.setValueOfProperty(0, 000010000);
			property11.setNumberOfRecoveredMotorVehicles(1);
			property11.setDateRecovered(0, null);
			//Invalid Date Recovered
			GroupAIncidentReport copy12 = new GroupAIncidentReport(copy);
			copy12.getOffenses().get(0).setUcrOffenseCode("240");
			copy12.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy12.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy12.getOffenses().get(0).setBiasMotivation(0, "88");
			copy12.getOffenses().get(0).setLocationType("20");
			copy12.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy12.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property12 = new PropertySegment();
			property12.setTypeOfPropertyLoss("5");
			property12.setPropertyDescription(0, "03");
			property12.setValueOfProperty(0, 000010000);
			property12.setNumberOfRecoveredMotorVehicles(1);
			property12.setDateRecovered(0, (Date.from(LocalDateTime.of(20160, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Invalid NumberOfRecoveredMotorVehicles
			GroupAIncidentReport copy13 = new GroupAIncidentReport(copy);
			copy13.getOffenses().get(0).setUcrOffenseCode("240");
			copy13.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy13.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy13.getOffenses().get(0).setBiasMotivation(0, "88");
			copy13.getOffenses().get(0).setLocationType("20");
			copy13.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy13.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property13 = new PropertySegment();
			property13.setTypeOfPropertyLoss("5");
			property13.setPropertyDescription(0, "03");
			property13.setValueOfProperty(0, 000010000);
			property13.setNumberOfRecoveredMotorVehicles(0);
			property13.setDateRecovered(0, (Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Null NumberOfRecoveredMotorVehicles
			GroupAIncidentReport copy14 = new GroupAIncidentReport(copy);
			copy14.getOffenses().get(0).setUcrOffenseCode("240");
			copy14.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy14.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy14.getOffenses().get(0).setBiasMotivation(0, "88");
			copy14.getOffenses().get(0).setLocationType("20");
			copy14.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy14.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property14 = new PropertySegment();
			property14.setTypeOfPropertyLoss("5");
			property14.setPropertyDescription(0, "03");
			property14.setValueOfProperty(0, 000010000);
			property14.setNumberOfRecoveredMotorVehicles(null);
			property14.setDateRecovered(0, (Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Suspected Drug Type cannot be blank
			GroupAIncidentReport copy15 = new GroupAIncidentReport(copy);
			copy15.getOffenses().get(0).setUcrOffenseCode("35A");
			copy15.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy15.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy15.getOffenses().get(0).setBiasMotivation(0, "88");
			copy15.getOffenses().get(0).setLocationType("20");
			copy15.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy15.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property15 = new PropertySegment();
			property15.setTypeOfPropertyLoss("6");
			property15.setPropertyDescription(0, "10");
			property15.setSuspectedDrugType(0, null);
			property15.setEstimatedDrugQuantity(0, 1.0);
			property15.setTypeDrugMeasurement(0, "OZ");
			property15.setValueOfProperty(0, 000010000);
			property15.setNumberOfRecoveredMotorVehicles(null);
			property15.setDateRecovered(0, (Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Suspected Drug type must be valid
			GroupAIncidentReport copy16 = new GroupAIncidentReport(copy);
			copy16.getOffenses().get(0).setUcrOffenseCode("35A");
			copy16.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy16.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy16.getOffenses().get(0).setBiasMotivation(0, "88");
			copy16.getOffenses().get(0).setLocationType("20");
			copy16.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy16.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property16 = new PropertySegment();
			property16.setTypeOfPropertyLoss("6");
			property16.setPropertyDescription(0, "10");
			property16.setSuspectedDrugType(0, "Z");
			property16.setEstimatedDrugQuantity(0, 1.0);
			property16.setTypeDrugMeasurement(0, "OZ");
			property16.setValueOfProperty(0, 000010000);
			property16.setNumberOfRecoveredMotorVehicles(null);
			property16.setDateRecovered(0, (Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Estimated Drug Quantity cannot be blank
			GroupAIncidentReport copy17 = new GroupAIncidentReport(copy);
			copy17.getOffenses().get(0).setUcrOffenseCode("35A");
			copy17.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy17.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy17.getOffenses().get(0).setBiasMotivation(0, "88");
			copy17.getOffenses().get(0).setLocationType("20");
			copy17.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy17.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property17 = new PropertySegment();
			property17.setTypeOfPropertyLoss("6");
			property17.setPropertyDescription(0, "10");
			property17.setSuspectedDrugType(0, "Z");
			property17.setEstimatedDrugQuantity(0, null);
			property17.setTypeDrugMeasurement(0, "OZ");
			property17.setValueOfProperty(0, 000010000);
			property17.setNumberOfRecoveredMotorVehicles(null);
			property17.setDateRecovered(0, (Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Estimated Drug Quantity must be valid.
			GroupAIncidentReport copy18 = new GroupAIncidentReport(copy);
			copy18.getOffenses().get(0).setUcrOffenseCode("35A");
			copy18.getOffenses().get(0).setOffenseAttemptedCompleted("C");
			copy18.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "N");
			copy18.getOffenses().get(0).setBiasMotivation(0, "88");
			copy18.getOffenses().get(0).setLocationType("20");
			copy18.getOffenses().get(0).setNumberOfPremisesEntered(1);
			copy18.getOffenses().get(0).setMethodOfEntry("N");
			PropertySegment property18 = new PropertySegment();
			property18.setTypeOfPropertyLoss("6");
			property18.setPropertyDescription(0, "10");
			property18.setSuspectedDrugType(0, "Z");
			property18.setEstimatedDrugQuantity(0, 9999999999.0);
			property18.setTypeDrugMeasurement(0, "OZ");
			property18.setValueOfProperty(0, 000010000);
			property18.setNumberOfRecoveredMotorVehicles(null);
			property18.setDateRecovered(0, (Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			
					
			
			
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
			copy.addProperty(property);
			copy2.addProperty(property2);
			copy3.addProperty(property3);
			copy4.addProperty(property4);
			copy5.addProperty(property5);
			copy6.addProperty(property6);
			copy7.addProperty(property7);
			copy8.addProperty(property8);
			copy9.addProperty(property9);
			copy10.addProperty(property10);
			copy11.addProperty(property11);
			copy12.addProperty(property12);
			copy13.addProperty(property13);
			copy14.addProperty(property14);
			copy15.addProperty(property15);
			copy16.addProperty(property16);
			
			return incidents;
			
		});

		groupATweakerMap.put(305, incident -> {
			//(Date Recovered Each component of the date must be valid; that is, months must be 01 through 12, 
			//days must be 01 through 31, and year must include the century (i.e., 19xx, 20xx). 
			//In addition, days cannot exceed maximum for the month (e.g., June cannot have 31 days). 
			//The date cannot be later than that entered within the Month of Electronic submission and 
			//Year of Electronic submission fields on the data record. For example, if Month of Electronic 
			//submission and Year of Electronic submission are 06/1999, the recovered date cannot contain 
			//any date 07/01/1999 or later. Cannot be earlier than Data Element 3 (Incident Date/Hour).
			
			//Date is later than tape submission
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("05");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "03");
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0, 000000500);
			PropertySegment numberOfRecoveredMotorVehicles = new PropertySegment();
			numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment dateRecovered = new PropertySegment();
			dateRecovered.setDateRecovered(0, (Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Date Recovered is earlier than incident
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			PropertySegment typeOfPropertyLoss2 = new PropertySegment();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("05");
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "03");
			PropertySegment valueOfProperty2 = new PropertySegment();
			valueOfProperty2.setValueOfProperty(0, 000000500);
			PropertySegment numberOfRecoveredMotorVehicles2 = new PropertySegment();
			numberOfRecoveredMotorVehicles2.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment dateRecovered2 = new PropertySegment();
			dateRecovered2.setDateRecovered(0, (Date.from(LocalDate.of(2015, 4, 16).atStartOfDay(ZoneId.systemDefault()).toInstant())));
			//Invalid Date Recovered Month
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			PropertySegment typeOfPropertyLoss3 = new PropertySegment();
			typeOfPropertyLoss3.setTypeOfPropertyLoss("05");
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, "03");
			PropertySegment valueOfProperty3 = new PropertySegment();
			valueOfProperty3.setValueOfProperty(0, 000000500);
			PropertySegment numberOfRecoveredMotorVehicles3 = new PropertySegment();
			numberOfRecoveredMotorVehicles3.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment dateRecovered3 = new PropertySegment();
			dateRecovered3.setDateRecovered(0, (Date.from(LocalDate.of(2015, 13, 16).atStartOfDay(ZoneId.systemDefault()).toInstant())));
			//Invalid Date Recovered Days for month of May
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			PropertySegment typeOfPropertyLoss4 = new PropertySegment();
			typeOfPropertyLoss4.setTypeOfPropertyLoss("05");
			PropertySegment propertyDescription4 = new PropertySegment();
			propertyDescription4.setPropertyDescription(0, "03");
			PropertySegment valueOfProperty4 = new PropertySegment();
			valueOfProperty4.setValueOfProperty(0, 000000500);
			PropertySegment numberOfRecoveredMotorVehicles4 = new PropertySegment();
			numberOfRecoveredMotorVehicles4.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment dateRecovered4 = new PropertySegment();
			dateRecovered4.setDateRecovered(0, (Date.from(LocalDate.of(2015, 5, 32).atStartOfDay(ZoneId.systemDefault()).toInstant())));
			//Year Does not include century
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			PropertySegment typeOfPropertyLoss5 = new PropertySegment();
			typeOfPropertyLoss5.setTypeOfPropertyLoss("05");
			PropertySegment propertyDescription5 = new PropertySegment();
			propertyDescription5.setPropertyDescription(0, "03");
			PropertySegment valueOfProperty5 = new PropertySegment();
			valueOfProperty5.setValueOfProperty(0, 000000500);
			PropertySegment numberOfRecoveredMotorVehicles5 = new PropertySegment();
			numberOfRecoveredMotorVehicles5.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment dateRecovered5 = new PropertySegment();
			dateRecovered5.setDateRecovered(0, (Date.from(LocalDate.of(15, 5, 13).atStartOfDay(ZoneId.systemDefault()).toInstant())));
			
					
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			
			return incidents;
		});
			
		groupATweakerMap.put(306, incident -> {	
			//(Suspected Drug Type) The referenced data element in error is one that contains multiple data values. 
			//When more than one code is entered, none can be duplicate codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode = new OffenseSegment();
			ucrOffenseCode.setUcrOffenseCode("35A");
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("6");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "10");
			PropertySegment suspectedDrugType = new PropertySegment();
			suspectedDrugType.setSuspectedDrugType(0, "A");
			PropertySegment suspectedDrugType2 = new PropertySegment();
			suspectedDrugType2.setSuspectedDrugType(0, "A");
				
			//There are two exceptions to this rule:
			//When a data value is entered in both Drug Type 1 and Drug Type 2, but different measurement categories are 
			//entered in Data Element 22 (Type Drug Measurement); this is allowed. For example, when A=Crack Cocaine
			//is entered in Drug Type 1 and it is also entered in Drug Type 2, Data Element 22 
			//(Type Drug Measurement) must be two different measurement categories 
			//(i.e., grams and liters) and not grams and pounds (same weight category).
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			OffenseSegment ucrOffenseCode2 = new OffenseSegment();
			ucrOffenseCode2.setUcrOffenseCode("35A");
			PropertySegment typeOfPropertyLoss2 = new PropertySegment();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("6");
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "10");
			PropertySegment suspectedDrugType3 = new PropertySegment();
			suspectedDrugType3.setSuspectedDrugType(0, "A");
			PropertySegment suspectedDrugType4 = new PropertySegment();
			suspectedDrugType4.setSuspectedDrugType(0, "A");
			PropertySegment typeDrugMeasurement = new PropertySegment();
			//typeDrugMeasurement.setTypeDrugMeasurement(0, value); // doesn't compile
			
			incidents.add(copy);
			incidents.add(copy2);
			
		
			
			return incidents;
		});
			
			
		
		groupATweakerMap.put(342, incident -> {
			//(Value of PropertySegment) When referenced data element contains a value that exceeds an FBI-assigned 
			//threshold amount, a warning message will be created. The participant is asked to check to 
			//see if the value entered was a data entry error, or if it was intended to be entered. 
			//A warning message is always produced when the value is $1,000,000 or greater. 
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0, 001000000);
			
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(351, incident -> {
			// Value of PropertySegment) cannot be zero unless Data Element 15 (PropertySegment Description) is:
			//Mandatory zero
			//09=Credit/Debit Cards
			//22=Non-negotiable Instruments
			//48=Documents�Personal or Business
			//65=Identity Documents
			//66=Identity�Intangible
			//Optional zero
			//77=Other
			//99=(blank)�this data value is not currently used by the FBI
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0, 000000500);
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "09");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			PropertySegment valueOfProperty2 = new PropertySegment();
			valueOfProperty2.setValueOfProperty(0, 000000500);
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "22");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			PropertySegment valueOfProperty3 = new PropertySegment();
			valueOfProperty3.setValueOfProperty(0, 000000500);
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, "48");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			PropertySegment valueOfProperty4 = new PropertySegment();
			valueOfProperty4.setValueOfProperty(0, 000000500);
			PropertySegment propertyDescription4 = new PropertySegment();
			propertyDescription4.setPropertyDescription(0, "65");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			PropertySegment valueOfProperty5 = new PropertySegment();
			valueOfProperty5.setValueOfProperty(0, 000000500);
			PropertySegment propertyDescription5 = new PropertySegment();
			propertyDescription5.setPropertyDescription(0, "66");

			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			
			
			return incidents;
		});

		groupATweakerMap.put(353, incident -> {
			//(Value of PropertySegment) is 88=Pending Inventory, but Data Element 16 (Value of PropertySegment) is not $1. 
			//Determine which of the data elements was entered incorrectly.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "88");
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0,000000001);
			
			incidents.add(copy);
			return incidents;
		});

			
		groupATweakerMap.put(354, incident -> {
			// (PropertySegment Description) Data Element 16 (Value of PropertySegment) contains a value,
			// but Data Element 15 (PropertySegment Description) was not entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0, 000000500);
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, null);

			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(355, incident -> {
			// (Type Of PropertySegment Loss) must be 5=Recovered for Data Element 17 (Date Recovered) to be entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("2");
			PropertySegment dateRecovered = new PropertySegment();
			dateRecovered.setDateRecovered(0,(Date.from(LocalDate.of(2015, 5, 16).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		
			incidents.add(copy);
			return incidents;
		});
		
		//groupATweakerMap.put(356, incident -> {
			//to-do: Rule is not clear.
			// (PropertySegment Description) was entered, but Data Elements 15 (PropertySegment Description)
			// and/or 16 (PropertySegment Value) were not entered.
			//List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			//PropertySegment valueOfProperty = new PropertySegment();
			//valueOfProperty.setValueOfProperty(0, null);
			//PropertySegment propertyDescription = new PropertySegment();
			//propertyDescription.setPropertyDescription(0, "01");
			//GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			//PropertySegment valueOfProperty2 = new PropertySegment();
			//.setValueOfProperty(0, 000000500);
			//PropertySegment propertyDescription2 = new PropertySegment();
			//propertyDescription2.setPropertyDescription(0, null);

			//incidents.add(copy);
			//incidents.add(copy2);
			
			//return incidents;
		//});

		groupATweakerMap.put(357, incident -> {
			//(Number of Stolen Motor Vehicles) was entered. However, Data Element 14 (Type PropertySegment Loss/Etc.) 
			//7=Stolen/Etc. was not entered, and/or Data Element 6 (UCR OffenseSegment Code) of 240=Motor Vehicle Theft was not entered, 
			//and/or Data Element 7 (OffenseSegment Attempted/Completed) was A=Attempted.
			
			//UCR OffenseSegment Code not = 240
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode = new OffenseSegment();
			ucrOffenseCode.setUcrOffenseCode("13A");
			OffenseSegment offenseAttemptedCompleted = new OffenseSegment();
			offenseAttemptedCompleted.setOffenseAttemptedCompleted("C");
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("7");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "03");
			PropertySegment numberOfStolenMotorVehicles = new PropertySegment();
			numberOfStolenMotorVehicles.setNumberOfStolenMotorVehicles(1);
			//Type of PropertySegment Loss not stolen
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode2 = new OffenseSegment();
			ucrOffenseCode2.setUcrOffenseCode("240");
			OffenseSegment offenseAttemptedCompleted2 = new OffenseSegment();
			offenseAttemptedCompleted2.setOffenseAttemptedCompleted("C");
			PropertySegment typeOfPropertyLoss2 = new PropertySegment();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("2");
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "03");
			PropertySegment numberOfStolenMotorVehicles2 = new PropertySegment();
			numberOfStolenMotorVehicles2.setNumberOfStolenMotorVehicles(1);
			//OffenseSegment Attempted/Completed not = C
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode3 = new OffenseSegment();
			ucrOffenseCode3.setUcrOffenseCode("240");
			OffenseSegment offenseAttemptedCompleted3 = new OffenseSegment();
			offenseAttemptedCompleted3.setOffenseAttemptedCompleted("A");
			PropertySegment typeOfPropertyLoss3 = new PropertySegment();
			typeOfPropertyLoss3.setTypeOfPropertyLoss("7");
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, "03");
			PropertySegment numberOfStolenMotorVehicles3 = new PropertySegment();
			numberOfStolenMotorVehicles3.setNumberOfStolenMotorVehicles(1);
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(358, incident -> {
			//(Number of Stolen Motor Vehicles) Entry must be made for Data Element 18 
			//(Number of Stolen Motor Vehicles) when Data Element 6 (UCR OffenseSegment Code) 
			//is 240=Motor Vehicle Theft, Data Element 7 (OffenseSegment Attempted/Completed) is C=Completed, and 
			//Data Element 14 (Type PropertySegment Loss/Etc.) is 7=Stolen/Etc.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode = new OffenseSegment();
			ucrOffenseCode.setUcrOffenseCode("240");
			OffenseSegment offenseAttemptedCompleted = new OffenseSegment();
			offenseAttemptedCompleted.setOffenseAttemptedCompleted("C");
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("7");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "03");
			PropertySegment numberOfStolenMotorVehicles = new PropertySegment();
			numberOfStolenMotorVehicles.setNumberOfStolenMotorVehicles(null);
		
			incidents.add(copy);
		
			return incidents;
			
		});
		
		groupATweakerMap.put(359, incident -> {
			// (PropertySegment Description) Must be one of the following
			//03=Automobiles
			//05=Buses
			//24=Other Motor Vehicles
			//28=Recreational Vehicles
			//37=Trucks
			//when Data Element 18 (Number of Stolen Motor Vehicles) or Data Element 19 (Number of Recovered Motor Vehicles) 
			//contain a data value other than 00=Unknown:
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "01");
			PropertySegment numberOfStolenMotorVehicles = new PropertySegment();
			numberOfStolenMotorVehicles.setNumberOfStolenMotorVehicles(2);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "01");
			PropertySegment numberOfRecoveredMotorVehicles = new PropertySegment();
			numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(2);
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		groupATweakerMap.put(360, incident -> {
			//(Number of Recovered Motor Vehicles was entered. However, Data Element 14 
			//(Type PropertySegment Loss/Etc.) 5=Recovered was not entered, and/or Data Element 6 
			//(UCR OffenseSegment Code) of 240=Motor Vehicle Theft was not entered, and/or Data Element 7 
			//(OffenseSegment Attempted/Completed) was A=Attempted.
			
			//UCR OffenseSegment Code not = 240
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode = new OffenseSegment();
			ucrOffenseCode.setUcrOffenseCode("13A");
			OffenseSegment offenseAttemptedCompleted = new OffenseSegment();
			offenseAttemptedCompleted.setOffenseAttemptedCompleted("C");
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("5");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "03");
			PropertySegment numberOfRecoveredMotorVehicles = new PropertySegment();
			numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(1);
			//Type of PropertySegment Loss not Recovered
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode2 = new OffenseSegment();
			ucrOffenseCode2.setUcrOffenseCode("240");
			OffenseSegment offenseAttemptedCompleted2 = new OffenseSegment();
			offenseAttemptedCompleted2.setOffenseAttemptedCompleted("C");
			PropertySegment typeOfPropertyLoss2 = new PropertySegment();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("2");
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "03");
			PropertySegment numberOfRecoveredMotorVehicles2 = new PropertySegment();
			numberOfRecoveredMotorVehicles2.setNumberOfRecoveredMotorVehicles(1);
			//OffenseSegment Attempted/Completed not = C
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode3 = new OffenseSegment();
			ucrOffenseCode3.setUcrOffenseCode("240");
			OffenseSegment offenseAttemptedCompleted3 = new OffenseSegment();
			offenseAttemptedCompleted3.setOffenseAttemptedCompleted("A");
			PropertySegment typeOfPropertyLoss3 = new PropertySegment();
			typeOfPropertyLoss3.setTypeOfPropertyLoss("5");
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, "03");
			PropertySegment numberOfRecoveredMotorVehicles3 = new PropertySegment();
			numberOfRecoveredMotorVehicles3.setNumberOfRecoveredMotorVehicles(1);
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			
			return incidents;
						
			
		});
		
		groupATweakerMap.put(361, incident -> {
			//((Number of Recovered Motor Vehicles) Entry must be made when Data Element 6 
			//(UCR OffenseSegment Code) is 240=Motor Vehicle Theft, Data Element 14 
			//(Type PropertySegment Loss/Etc.) is 5=Recovered, and Data Element 15 
			//(PropertySegment Description) contains a vehicle code.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment ucrOffenseCode = new OffenseSegment();
			ucrOffenseCode.setUcrOffenseCode("240");
			OffenseSegment offenseAttemptedCompleted = new OffenseSegment();
			offenseAttemptedCompleted.setOffenseAttemptedCompleted("C");
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("5");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "03");
			PropertySegment numberOfRecoveredMotorVehicles = new PropertySegment();
			numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(null);
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(372, incident -> {
			//(Type of PropertySegment Loss) is 
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
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("2");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, null);
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0, null);
			PropertySegment dateRecovered = new PropertySegment();
			dateRecovered.setDateRecovered(0, null);
			PropertySegment numberOfStolenMotorVehicles = new PropertySegment();
			numberOfStolenMotorVehicles.setNumberOfStolenMotorVehicles(2);
			PropertySegment numberOfRecoveredMotorVehicles = new PropertySegment();
			numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment suspectedDrugType = new PropertySegment();
			suspectedDrugType.setSuspectedDrugType(0, null);
			PropertySegment estimatedDrugQuantity = new PropertySegment();
			estimatedDrugQuantity.setEstimatedDrugQuantity(0, null);
			PropertySegment typeDrugMeasurement = new PropertySegment();
			typeDrugMeasurement.setTypeDrugMeasurement(0, null);
			//3=Counterfeited/Forged
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			PropertySegment typeOfPropertyLoss2 = new PropertySegment();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("3");
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, null);
			PropertySegment valueOfProperty2 = new PropertySegment();
			valueOfProperty2.setValueOfProperty(0, null);
			PropertySegment dateRecovered2 = new PropertySegment();
			dateRecovered2.setDateRecovered(0, null);
			PropertySegment numberOfStolenMotorVehicles2 = new PropertySegment();
			numberOfStolenMotorVehicles2.setNumberOfStolenMotorVehicles(2);
			PropertySegment numberOfRecoveredMotorVehicles2 = new PropertySegment();
			numberOfRecoveredMotorVehicles2.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment suspectedDrugType2 = new PropertySegment();
			suspectedDrugType2.setSuspectedDrugType(0, null);
			PropertySegment estimatedDrugQuantity2 = new PropertySegment();
			estimatedDrugQuantity2.setEstimatedDrugQuantity(0, null);
			PropertySegment typeDrugMeasurement2 = new PropertySegment();
			typeDrugMeasurement2.setTypeDrugMeasurement(0, null);
			//4=Destroyed/Damaged/Vandalized
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			PropertySegment typeOfPropertyLoss3 = new PropertySegment();
			typeOfPropertyLoss3.setTypeOfPropertyLoss("4");
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, null);
			PropertySegment valueOfProperty3 = new PropertySegment();
			valueOfProperty3.setValueOfProperty(0, null);
			PropertySegment dateRecovered3 = new PropertySegment();
			dateRecovered3.setDateRecovered(0, null);
			PropertySegment numberOfStolenMotorVehicles3 = new PropertySegment();
			numberOfStolenMotorVehicles3.setNumberOfStolenMotorVehicles(2);
			PropertySegment numberOfRecoveredMotorVehicles3 = new PropertySegment();
			numberOfRecoveredMotorVehicles3.setNumberOfRecoveredMotorVehicles(1);
			PropertySegment suspectedDrugType3 = new PropertySegment();
			suspectedDrugType3.setSuspectedDrugType(0, null);
			PropertySegment estimatedDrugQuantity3 = new PropertySegment();
			estimatedDrugQuantity3.setEstimatedDrugQuantity(0, null);
			PropertySegment typeDrugMeasurement3 = new PropertySegment();
			typeDrugMeasurement3.setTypeDrugMeasurement(0, null);
			//5=Recovered
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			PropertySegment typeOfPropertyLoss4 = new PropertySegment();
			typeOfPropertyLoss4.setTypeOfPropertyLoss("5");
			PropertySegment propertyDescription4 = new PropertySegment();
			propertyDescription4.setPropertyDescription(0, null);
			PropertySegment valueOfProperty4 = new PropertySegment();
			valueOfProperty4.setValueOfProperty(0, null);
			PropertySegment dateRecovered4 = new PropertySegment();
			dateRecovered4.setDateRecovered(0, null);
			PropertySegment numberOfStolenMotorVehicles4 = new PropertySegment();
			numberOfStolenMotorVehicles4.setNumberOfStolenMotorVehicles(null);
			PropertySegment numberOfRecoveredMotorVehicles4 = new PropertySegment();
			numberOfRecoveredMotorVehicles4.setNumberOfRecoveredMotorVehicles(null);
			PropertySegment suspectedDrugType4 = new PropertySegment();
			suspectedDrugType4.setSuspectedDrugType(0, null);
			PropertySegment estimatedDrugQuantity4 = new PropertySegment();
			estimatedDrugQuantity4.setEstimatedDrugQuantity(0, null);
			PropertySegment typeDrugMeasurement4 = new PropertySegment();
			typeDrugMeasurement4.setTypeDrugMeasurement(0, null);
			//6=Seized
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			PropertySegment typeOfPropertyLoss5 = new PropertySegment();
			typeOfPropertyLoss5.setTypeOfPropertyLoss("5");
			PropertySegment propertyDescription5 = new PropertySegment();
			propertyDescription5.setPropertyDescription(0, null);
			PropertySegment valueOfProperty5 = new PropertySegment();
			valueOfProperty5.setValueOfProperty(0, null);
			PropertySegment dateRecovered5 = new PropertySegment();
			dateRecovered5.setDateRecovered(0, null);
			PropertySegment numberOfStolenMotorVehicles5 = new PropertySegment();
			numberOfStolenMotorVehicles5.setNumberOfStolenMotorVehicles(null);
			PropertySegment numberOfRecoveredMotorVehicles5 = new PropertySegment();
			numberOfRecoveredMotorVehicles5.setNumberOfRecoveredMotorVehicles(null);
			PropertySegment suspectedDrugType5 = new PropertySegment();
			suspectedDrugType5.setSuspectedDrugType(0, null);
			PropertySegment estimatedDrugQuantity5 = new PropertySegment();
			estimatedDrugQuantity5.setEstimatedDrugQuantity(0, null);
			PropertySegment typeDrugMeasurement5 = new PropertySegment();
			typeDrugMeasurement5.setTypeDrugMeasurement(0, null);
			//6=Stolen, Etc.
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			PropertySegment typeOfPropertyLoss6 = new PropertySegment();
			typeOfPropertyLoss6.setTypeOfPropertyLoss("5");
			PropertySegment propertyDescription6 = new PropertySegment();
			propertyDescription6.setPropertyDescription(0, null);
			PropertySegment valueOfProperty6 = new PropertySegment();
			valueOfProperty6.setValueOfProperty(0, null);
			PropertySegment dateRecovered6 = new PropertySegment();
			dateRecovered6.setDateRecovered(0, null);
			PropertySegment numberOfStolenMotorVehicles6 = new PropertySegment();
			numberOfStolenMotorVehicles6.setNumberOfStolenMotorVehicles(null);
			PropertySegment numberOfRecoveredMotorVehicles6 = new PropertySegment();
			numberOfRecoveredMotorVehicles6.setNumberOfRecoveredMotorVehicles(null);
			PropertySegment suspectedDrugType6 = new PropertySegment();
			suspectedDrugType6.setSuspectedDrugType(0, null);
			PropertySegment estimatedDrugQuantity6 = new PropertySegment();
			estimatedDrugQuantity6.setEstimatedDrugQuantity(0, null);
			PropertySegment typeDrugMeasurement6 = new PropertySegment();
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
			// (PropertySegment Description) At least one Data Element 15 (PropertySegment Description) code must
			//be entered when Data Element 14 (Type PropertySegment Loss/Etc.) contains PropertySegment Segment(s) for:
			//		2=Burned
			//		3=Counterfeited/Forged
			//		4=Destroyed/Damaged/Vandalized
			//		5=Recovered
			//		6=Seized
			//		7=Stolen/Etc.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, null);
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("2");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, null);
			PropertySegment typeOfPropertyLoss2 = new PropertySegment();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("3");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, null);
			PropertySegment typeOfPropertyLoss3 = new PropertySegment();
			typeOfPropertyLoss3.setTypeOfPropertyLoss("4");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription4 = new PropertySegment();
			propertyDescription4.setPropertyDescription(0, null);
			PropertySegment typeOfPropertyLoss4 = new PropertySegment();
			typeOfPropertyLoss4.setTypeOfPropertyLoss("5");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription5 = new PropertySegment();
			propertyDescription5.setPropertyDescription(0, null);
			PropertySegment typeOfPropertyLoss5 = new PropertySegment();
			typeOfPropertyLoss5.setTypeOfPropertyLoss("6");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription6 = new PropertySegment();
			propertyDescription6.setPropertyDescription(0, null);
			PropertySegment typeOfPropertyLoss6 = new PropertySegment();
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
			//(Value of PropertySegment) has a value other than zero entered. 
			//Since Data Element 15 (PropertySegment Description) code is 10=Drugs/Narcotics and the only Crime Against PropertySegment 
			//offense submitted is a 35A=Drug/Narcotic Violations, Data Element 16 (Value of PropertySegment) must be blank.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense = new OffenseSegment();
			offense.setUcrOffenseCode("35A");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "10");
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0, 000000500);
			
			incidents.add(copy);
						
			return incidents;
		});
			groupATweakerMap.put(387, incident -> {
			//(PropertySegment Description) To ensure that 35A-35B Drug/Narcotic Offenses-Drug Equipment 
			//Violations are properly reported, Data Element 15 (PropertySegment Description) of 11=Drug/Narcotic Equipment 
			//is not allowed with only a 35A Drug/Narcotic Violation. Similarly, 10=Drugs/Narcotics is not 
			//allowed with only a 35B Drug Equipment Violation. And Data Element 14 (Type PropertySegment Loss/Etc.) is 6=Seized.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense = new OffenseSegment();
			offense.setUcrOffenseCode("35A");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "11");
			PropertySegment typeOfPropertyLoss = new PropertySegment();
			typeOfPropertyLoss.setTypeOfPropertyLoss("6");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("35B");
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "11");
			PropertySegment typeOfPropertyLoss2 = new PropertySegment();
			typeOfPropertyLoss2.setTypeOfPropertyLoss("6");
			
			incidents.add(copy);
			incidents.add(copy2);
			
			return incidents;
		});
		
			groupATweakerMap.put(388, incident -> {
				//(Number of Stolen Motor Vehicles) More than one vehicle code was entered in 
				//Data Element 15 (PropertySegment Description), but the number stolen in Data Element 18 
				//(Number of Stolen Motor Vehicles) is less than this number. 
				//For example, if vehicle codes of 03=Automobiles and 05=Buses were entered as being stolen, 
				//then the number stolen must be at least 2, unless the number stolen was unknown (00).
				//The exception to this rule is when 00=Unknown is entered in Data Element 18.
				
				List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				OffenseSegment ucrOffenseCode = new OffenseSegment();
				ucrOffenseCode.setUcrOffenseCode("240");
				OffenseSegment offenseAttemptedCompleted = new OffenseSegment();
				offenseAttemptedCompleted.setOffenseAttemptedCompleted("C");
				PropertySegment typeOfPropertyLoss = new PropertySegment();
				typeOfPropertyLoss.setTypeOfPropertyLoss("7");
				PropertySegment propertyDescription = new PropertySegment();
				propertyDescription.setPropertyDescription(0, "03");
				PropertySegment propertyDescription2 = new PropertySegment();
				propertyDescription2.setPropertyDescription(0, "05");
				PropertySegment numberOfStolenMotorVehicles = new PropertySegment();
				numberOfStolenMotorVehicles.setNumberOfStolenMotorVehicles(1);
			
				incidents.add(copy);
			
				return incidents;
				
			});
			
			
			groupATweakerMap.put(389, incident -> {
				//(Number of Recovered Stolen Motor Vehicles) More than one vehicle code was entered in 
				//Data Element 15 (PropertySegment Description), but the number recovered in Data Element 18 
				//(Number of Recovered Motor Vehicles) is less than this number. 
				//For example, if vehicle codes of 03=Automobiles and 05=Buses were entered as being recovered, 
				//then the number recovered must be at least 2, unless the number recovered was unknown (00).
				//The exception to this rule is when 00=Unknown is entered in Data Element 18.
				
				List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				OffenseSegment ucrOffenseCode = new OffenseSegment();
				ucrOffenseCode.setUcrOffenseCode("240");
				OffenseSegment offenseAttemptedCompleted = new OffenseSegment();
				offenseAttemptedCompleted.setOffenseAttemptedCompleted("C");
				PropertySegment typeOfPropertyLoss = new PropertySegment();
				typeOfPropertyLoss.setTypeOfPropertyLoss("5");
				PropertySegment propertyDescription = new PropertySegment();
				propertyDescription.setPropertyDescription(0, "03");
				PropertySegment propertyDescription2 = new PropertySegment();
				propertyDescription2.setPropertyDescription(0, "05");
				PropertySegment numberOfRecoveredMotorVehicles = new PropertySegment();
				numberOfRecoveredMotorVehicles.setNumberOfRecoveredMotorVehicles(1);
			
				incidents.add(copy);
			
				return incidents;
				
			});

		groupATweakerMap.put(390, incident -> {
			// (PropertySegment Description) must contain a data value that is logical for one or more of the offenses 
			//entered in Data Element 6 (UCR OffenseSegment Code).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//PropertySegment descriptions for structures are illogical with 220=Burglary/Breaking 
			//& Entering or 240=Motor Vehicle Theft
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense1 = new OffenseSegment();
			offense1.setUcrOffenseCode("220");
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "29");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("240");
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "29");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			OffenseSegment offense3 = new OffenseSegment();
			offense3.setUcrOffenseCode("220");
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, "30");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			OffenseSegment offense4 = new OffenseSegment();
			offense4.setUcrOffenseCode("240");
			PropertySegment propertyDescription4 = new PropertySegment();
			propertyDescription4.setPropertyDescription(0, "30");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			OffenseSegment offense5 = new OffenseSegment();
			offense5.setUcrOffenseCode("220");
			PropertySegment propertyDescription5 = new PropertySegment();
			propertyDescription5.setPropertyDescription(0, "31");
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			OffenseSegment offense6 = new OffenseSegment();
			offense6.setUcrOffenseCode("240");
			PropertySegment propertyDescription6 = new PropertySegment();
			propertyDescription6.setPropertyDescription(0, "31");
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			OffenseSegment offense7 = new OffenseSegment();
			offense7.setUcrOffenseCode("220");
			PropertySegment propertyDescription7 = new PropertySegment();
			propertyDescription7.setPropertyDescription(0, "32");
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			OffenseSegment offense8 = new OffenseSegment();
			offense8.setUcrOffenseCode("240");
			PropertySegment propertyDescription8 = new PropertySegment();
			propertyDescription8.setPropertyDescription(0, "32");
			GroupAIncidentReport copy9 = new GroupAIncidentReport(incident);
			OffenseSegment offense9 = new OffenseSegment();
			offense9.setUcrOffenseCode("220");
			PropertySegment propertyDescription9 = new PropertySegment();
			propertyDescription9.setPropertyDescription(0, "33");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(incident);
			OffenseSegment offense10 = new OffenseSegment();
			offense10.setUcrOffenseCode("240");
			PropertySegment propertyDescription10 = new PropertySegment();
			propertyDescription10.setPropertyDescription(0, "33");
			GroupAIncidentReport copy11 = new GroupAIncidentReport(incident);
			OffenseSegment offense11 = new OffenseSegment();
			offense11.setUcrOffenseCode("220");
			PropertySegment propertyDescription11 = new PropertySegment();
			propertyDescription11.setPropertyDescription(0, "34");
			GroupAIncidentReport copy12 = new GroupAIncidentReport(incident);
			OffenseSegment offense12 = new OffenseSegment();
			offense12.setUcrOffenseCode("240");
			PropertySegment propertyDescription12 = new PropertySegment();
			propertyDescription12.setPropertyDescription(0, "34");
			GroupAIncidentReport copy13 = new GroupAIncidentReport(incident);
			OffenseSegment offense13 = new OffenseSegment();
			offense13.setUcrOffenseCode("220");
			PropertySegment propertyDescription13 = new PropertySegment();
			propertyDescription13.setPropertyDescription(0, "35");
			GroupAIncidentReport copy14 = new GroupAIncidentReport(incident);
			OffenseSegment offense14 = new OffenseSegment();
			offense14.setUcrOffenseCode("240");
			PropertySegment propertyDescription14 = new PropertySegment();
			propertyDescription14.setPropertyDescription(0, "35");
			//PropertySegment descriptions for items that would not fit in a purse or pocket (aircraft, vehicles, structures, 
			//a person�s identity, watercraft, etc.) are illogical with 23A=Pocket-picking or 23B=Purse-snatching
			GroupAIncidentReport copy15 = new GroupAIncidentReport(incident);
			OffenseSegment offense15 = new OffenseSegment();
			offense15.setUcrOffenseCode("23A");
			PropertySegment propertyDescription15 = new PropertySegment();
			propertyDescription15.setPropertyDescription(0, "01");
			GroupAIncidentReport copy16 = new GroupAIncidentReport(incident);
			OffenseSegment offense16 = new OffenseSegment();
			offense16.setUcrOffenseCode("23B");
			PropertySegment propertyDescription16 = new PropertySegment();
			propertyDescription16.setPropertyDescription(0, "01");
			//Automobiles
			GroupAIncidentReport copy17 = new GroupAIncidentReport(incident);
			OffenseSegment offense17 = new OffenseSegment();
			offense17.setUcrOffenseCode("23A");
			PropertySegment propertyDescription17 = new PropertySegment();
			propertyDescription17.setPropertyDescription(0, "03");
			GroupAIncidentReport copy18 = new GroupAIncidentReport(incident);
			OffenseSegment offense18 = new OffenseSegment();
			offense18.setUcrOffenseCode("23B");
			PropertySegment propertyDescription18 = new PropertySegment();
			propertyDescription18.setPropertyDescription(0, "03");
			//Bicycles
			GroupAIncidentReport copy19 = new GroupAIncidentReport(incident);
			OffenseSegment offense19 = new OffenseSegment();
			offense19.setUcrOffenseCode("23A");
			PropertySegment propertyDescription19 = new PropertySegment();
			propertyDescription19.setPropertyDescription(0, "04");
			GroupAIncidentReport copy20 = new GroupAIncidentReport(incident);
			OffenseSegment offense20 = new OffenseSegment();
			offense20.setUcrOffenseCode("23B");
			PropertySegment propertyDescription20 = new PropertySegment();
			propertyDescription20.setPropertyDescription(0, "04");
			//Buses
			GroupAIncidentReport copy21 = new GroupAIncidentReport(incident);
			OffenseSegment offense21 = new OffenseSegment();
			offense21.setUcrOffenseCode("23A");
			PropertySegment propertyDescription21 = new PropertySegment();
			propertyDescription21.setPropertyDescription(0, "05");
			GroupAIncidentReport copy22 = new GroupAIncidentReport(incident);
			OffenseSegment offense22 = new OffenseSegment();
			offense22.setUcrOffenseCode("23B");
			PropertySegment propertyDescription22 = new PropertySegment();
			propertyDescription22.setPropertyDescription(0, "05");
			//Farm Equipment
			GroupAIncidentReport copy23 = new GroupAIncidentReport(incident);
			OffenseSegment offense23 = new OffenseSegment();
			offense23.setUcrOffenseCode("23A");
			PropertySegment propertyDescription23 = new PropertySegment();
			propertyDescription23.setPropertyDescription(0, "12");
			GroupAIncidentReport copy24 = new GroupAIncidentReport(incident);
			OffenseSegment offense24 = new OffenseSegment();
			offense24.setUcrOffenseCode("23B");
			PropertySegment propertyDescription24 = new PropertySegment();
			propertyDescription24.setPropertyDescription(0, "12");
			//Heavy Construction/Industrial Equipment
			GroupAIncidentReport copy25 = new GroupAIncidentReport(incident);
			OffenseSegment offense25 = new OffenseSegment();
			offense25.setUcrOffenseCode("23A");
			PropertySegment propertyDescription25 = new PropertySegment();
			propertyDescription25.setPropertyDescription(0, "15");
			GroupAIncidentReport copy26 = new GroupAIncidentReport(incident);
			OffenseSegment offense26 = new OffenseSegment();
			offense26.setUcrOffenseCode("23B");
			PropertySegment propertyDescription26 = new PropertySegment();
			propertyDescription26.setPropertyDescription(0, "15");
			//Livestock
			GroupAIncidentReport copy27 = new GroupAIncidentReport(incident);
			OffenseSegment offense27 = new OffenseSegment();
			offense27.setUcrOffenseCode("23A");
			PropertySegment propertyDescription27 = new PropertySegment();
			propertyDescription27.setPropertyDescription(0, "18");
			GroupAIncidentReport copy28 = new GroupAIncidentReport(incident);
			OffenseSegment offense28 = new OffenseSegment();
			offense28.setUcrOffenseCode("23B");
			PropertySegment propertyDescription28 = new PropertySegment();
			propertyDescription28.setPropertyDescription(0, "18");
			//Other Motor Vehicles
			GroupAIncidentReport copy29 = new GroupAIncidentReport(incident);
			OffenseSegment offense29 = new OffenseSegment();
			offense29.setUcrOffenseCode("23A");
			PropertySegment propertyDescription29 = new PropertySegment();
			propertyDescription29.setPropertyDescription(0, "24");
			GroupAIncidentReport copy30 = new GroupAIncidentReport(incident);
			OffenseSegment offense30 = new OffenseSegment();
			offense30.setUcrOffenseCode("23B");
			PropertySegment propertyDescription30 = new PropertySegment();
			propertyDescription30.setPropertyDescription(0, "24");
			//Recreational Vehicles
			GroupAIncidentReport copy31 = new GroupAIncidentReport(incident);
			OffenseSegment offense31 = new OffenseSegment();
			offense31.setUcrOffenseCode("23A");
			PropertySegment propertyDescription31 = new PropertySegment();
			propertyDescription31.setPropertyDescription(0, "28");
			GroupAIncidentReport copy32 = new GroupAIncidentReport(incident);
			OffenseSegment offense32 = new OffenseSegment();
			offense32.setUcrOffenseCode("23B");
			PropertySegment propertyDescription32 = new PropertySegment();
			propertyDescription32.setPropertyDescription(0, "28");
			//Structures - Single Occupancy Dwelling
			GroupAIncidentReport copy33 = new GroupAIncidentReport(incident);
			OffenseSegment offense33 = new OffenseSegment();
			offense33.setUcrOffenseCode("23A");
			PropertySegment propertyDescription33 = new PropertySegment();
			propertyDescription33.setPropertyDescription(0, "29");
			GroupAIncidentReport copy34 = new GroupAIncidentReport(incident);
			OffenseSegment offense34 = new OffenseSegment();
			offense34.setUcrOffenseCode("23B");
			PropertySegment propertyDescription34 = new PropertySegment();
			propertyDescription34.setPropertyDescription(0, "29");
			//Structures - Other Dwellings
			GroupAIncidentReport copy35 = new GroupAIncidentReport(incident);
			OffenseSegment offense35 = new OffenseSegment();
			offense35.setUcrOffenseCode("23A");
			PropertySegment propertyDescription35 = new PropertySegment();
			propertyDescription35.setPropertyDescription(0, "30");
			GroupAIncidentReport copy36 = new GroupAIncidentReport(incident);
			OffenseSegment offense36 = new OffenseSegment();
			offense36.setUcrOffenseCode("23B");
			PropertySegment propertyDescription36 = new PropertySegment();
			propertyDescription36.setPropertyDescription(0, "30");
			//Structures - Commercial Business
			GroupAIncidentReport copy37 = new GroupAIncidentReport(incident);
			OffenseSegment offense37 = new OffenseSegment();
			offense37.setUcrOffenseCode("23A");
			PropertySegment propertyDescription37 = new PropertySegment();
			propertyDescription37.setPropertyDescription(0, "31");
			GroupAIncidentReport copy38 = new GroupAIncidentReport(incident);
			OffenseSegment offense38 = new OffenseSegment();
			offense38.setUcrOffenseCode("23B");
			PropertySegment propertyDescription38 = new PropertySegment();
			propertyDescription38.setPropertyDescription(0, "31");
			//Structures - Industrial/Manufacturing
			GroupAIncidentReport copy39 = new GroupAIncidentReport(incident);
			OffenseSegment offense39 = new OffenseSegment();
			offense39.setUcrOffenseCode("23A");
			PropertySegment propertyDescription39 = new PropertySegment();
			propertyDescription39.setPropertyDescription(0, "32");
			GroupAIncidentReport copy40 = new GroupAIncidentReport(incident);
			OffenseSegment offense40 = new OffenseSegment();
			offense40.setUcrOffenseCode("23B");
			PropertySegment propertyDescription40 = new PropertySegment();
			propertyDescription40.setPropertyDescription(0, "32");
			//Structures - Public/Community
			GroupAIncidentReport copy41 = new GroupAIncidentReport(incident);
			OffenseSegment offense41 = new OffenseSegment();
			offense41.setUcrOffenseCode("23A");
			PropertySegment propertyDescription41 = new PropertySegment();
			propertyDescription41.setPropertyDescription(0, "33");
			GroupAIncidentReport copy42 = new GroupAIncidentReport(incident);
			OffenseSegment offense42 = new OffenseSegment();
			offense42.setUcrOffenseCode("23B");
			PropertySegment propertyDescription42 = new PropertySegment();
			propertyDescription42.setPropertyDescription(0, "33");
			//Structures - Storage
			GroupAIncidentReport copy43 = new GroupAIncidentReport(incident);
			OffenseSegment offense43 = new OffenseSegment();
			offense43.setUcrOffenseCode("23A");
			PropertySegment propertyDescription43 = new PropertySegment();
			propertyDescription43.setPropertyDescription(0, "34");
			GroupAIncidentReport copy44 = new GroupAIncidentReport(incident);
			OffenseSegment offense44 = new OffenseSegment();
			offense44.setUcrOffenseCode("23B");
			PropertySegment propertyDescription44 = new PropertySegment();
			propertyDescription44.setPropertyDescription(0, "34");
			//Structures - Other
			GroupAIncidentReport copy45 = new GroupAIncidentReport(incident);
			OffenseSegment offense45 = new OffenseSegment();
			offense45.setUcrOffenseCode("23A");
			PropertySegment propertyDescription45 = new PropertySegment();
			propertyDescription45.setPropertyDescription(0, "35");
			GroupAIncidentReport copy46 = new GroupAIncidentReport(incident);
			OffenseSegment offense46 = new OffenseSegment();
			offense46.setUcrOffenseCode("23B");
			PropertySegment propertyDescription46 = new PropertySegment();
			propertyDescription46.setPropertyDescription(0, "35");
			//Trucks
			GroupAIncidentReport copy47 = new GroupAIncidentReport(incident);
			OffenseSegment offense47 = new OffenseSegment();
			offense47.setUcrOffenseCode("23A");
			PropertySegment propertyDescription47 = new PropertySegment();
			propertyDescription47.setPropertyDescription(0, "37");
			GroupAIncidentReport copy48 = new GroupAIncidentReport(incident);
			OffenseSegment offense48 = new OffenseSegment();
			offense48.setUcrOffenseCode("23B");
			PropertySegment propertyDescription48 = new PropertySegment();
			propertyDescription48.setPropertyDescription(0, "37");
			//Watercraft
			GroupAIncidentReport copy49 = new GroupAIncidentReport(incident);
			OffenseSegment offense49 = new OffenseSegment();
			offense49.setUcrOffenseCode("23A");
			PropertySegment propertyDescription49 = new PropertySegment();
			propertyDescription49.setPropertyDescription(0, "39");
			GroupAIncidentReport copy50 = new GroupAIncidentReport(incident);
			OffenseSegment offense50 = new OffenseSegment();
			offense50.setUcrOffenseCode("23B");
			PropertySegment propertyDescription50 = new PropertySegment();
			propertyDescription50.setPropertyDescription(0, "39");
			//PropertySegment descriptions that cannot be shoplifted due to other UCR definitions 
			//(aircraft, vehicles, structures, a person�s identity, watercraft, etc.) are illogical with 23C=Shoplifting
			GroupAIncidentReport copy51 = new GroupAIncidentReport(incident);
			OffenseSegment offense51 = new OffenseSegment();
			offense51.setUcrOffenseCode("23C");
			PropertySegment propertyDescription51 = new PropertySegment();
			propertyDescription51.setPropertyDescription(0, "01");
			//Automobiles
			GroupAIncidentReport copy52 = new GroupAIncidentReport(incident);
			OffenseSegment offense52 = new OffenseSegment();
			offense52.setUcrOffenseCode("23C");
			PropertySegment propertyDescription52 = new PropertySegment();
			propertyDescription52.setPropertyDescription(0, "03");
			//Buses
			GroupAIncidentReport copy53 = new GroupAIncidentReport(incident);
			OffenseSegment offense53 = new OffenseSegment();
			offense53.setUcrOffenseCode("23C");
			PropertySegment propertyDescription53 = new PropertySegment();
			propertyDescription53.setPropertyDescription(0, "05");
			//Farm Equipment
			GroupAIncidentReport copy54 = new GroupAIncidentReport(incident);
			OffenseSegment offense54 = new OffenseSegment();
			offense54.setUcrOffenseCode("23C");
			PropertySegment propertyDescription54 = new PropertySegment();
			propertyDescription54.setPropertyDescription(0, "12");
			//Heavy Contruction/Industrial Equipment
			GroupAIncidentReport copy55 = new GroupAIncidentReport(incident);
			OffenseSegment offense55 = new OffenseSegment();
			offense55.setUcrOffenseCode("23C");
			PropertySegment propertyDescription55 = new PropertySegment();
			propertyDescription55.setPropertyDescription(0, "15");
			//Livestock
			GroupAIncidentReport copy56 = new GroupAIncidentReport(incident);
			OffenseSegment offense56 = new OffenseSegment();
			offense56.setUcrOffenseCode("23C");
			PropertySegment propertyDescription56 = new PropertySegment();
			propertyDescription56.setPropertyDescription(0, "18");
			//Other Motor Vehicles
			GroupAIncidentReport copy57 = new GroupAIncidentReport(incident);
			OffenseSegment offense57 = new OffenseSegment();
			offense57.setUcrOffenseCode("23C");
			PropertySegment propertyDescription57 = new PropertySegment();
			propertyDescription57.setPropertyDescription(0, "24");
			//Recreational Vehicles
			GroupAIncidentReport copy58 = new GroupAIncidentReport(incident);
			OffenseSegment offense58 = new OffenseSegment();
			offense58.setUcrOffenseCode("23C");
			PropertySegment propertyDescription58 = new PropertySegment();
			propertyDescription58.setPropertyDescription(0, "28");
			//Structures - Single Occupancy Dwellings
			GroupAIncidentReport copy59 = new GroupAIncidentReport(incident);
			OffenseSegment offense59 = new OffenseSegment();
			offense59.setUcrOffenseCode("23C");
			PropertySegment propertyDescription59 = new PropertySegment();
			propertyDescription59.setPropertyDescription(0, "29");
			//Structures - Other Dwellings
			GroupAIncidentReport copy60 = new GroupAIncidentReport(incident);
			OffenseSegment offense60 = new OffenseSegment();
			offense60.setUcrOffenseCode("23C");
			PropertySegment propertyDescription60 = new PropertySegment();
			propertyDescription60.setPropertyDescription(0, "30");
			//Structures - Commercial/Business
			GroupAIncidentReport copy61 = new GroupAIncidentReport(incident);
			OffenseSegment offense61 = new OffenseSegment();
			offense61.setUcrOffenseCode("23C");
			PropertySegment propertyDescription61 = new PropertySegment();
			propertyDescription61.setPropertyDescription(0, "31");
			//Structures - Industrial/Manufacturing
			GroupAIncidentReport copy62 = new GroupAIncidentReport(incident);
			OffenseSegment offense62 = new OffenseSegment();
			offense62.setUcrOffenseCode("23C");
			PropertySegment propertyDescription62 = new PropertySegment();
			propertyDescription62.setPropertyDescription(0, "32");
			//Structures - Public/Community
			GroupAIncidentReport copy63 = new GroupAIncidentReport(incident);
			OffenseSegment offense63 = new OffenseSegment();
			offense63.setUcrOffenseCode("23C");
			PropertySegment propertyDescription63 = new PropertySegment();
			propertyDescription63.setPropertyDescription(0, "33");
			//Structures - Storage
			GroupAIncidentReport copy64 = new GroupAIncidentReport(incident);
			OffenseSegment offense64 = new OffenseSegment();
			offense64.setUcrOffenseCode("23C");
			PropertySegment propertyDescription64 = new PropertySegment();
			propertyDescription64.setPropertyDescription(0, "34");
			//Structures - Other
			GroupAIncidentReport copy65 = new GroupAIncidentReport(incident);
			OffenseSegment offense65 = new OffenseSegment();
			offense65.setUcrOffenseCode("23C");
			PropertySegment propertyDescription65 = new PropertySegment();
			propertyDescription65.setPropertyDescription(0, "35");
			//Trucks
			GroupAIncidentReport copy66 = new GroupAIncidentReport(incident);
			OffenseSegment offense66 = new OffenseSegment();
			offense66.setUcrOffenseCode("23C");
			PropertySegment propertyDescription66 = new PropertySegment();
			propertyDescription66.setPropertyDescription(0, "37");
			//Watercraft
			GroupAIncidentReport copy67 = new GroupAIncidentReport(incident);
			OffenseSegment offense67 = new OffenseSegment();
			offense67.setUcrOffenseCode("23C");
			PropertySegment propertyDescription67 = new PropertySegment();
			propertyDescription67.setPropertyDescription(0, "39");
			//PropertySegment descriptions for vehicles and structures are illogical with 23D=Theft from Building, 
			//23E=Theft from Coin-Operated Machine or Device, 23F=Theft from Motor Vehicle, and 
			//23G=Theft of Motor Vehicle Parts or Accessories
			//Automobiles
			GroupAIncidentReport copy68 = new GroupAIncidentReport(incident);
			OffenseSegment offense68 = new OffenseSegment();
			offense68.setUcrOffenseCode("23D");
			PropertySegment propertyDescription68 = new PropertySegment();
			propertyDescription68.setPropertyDescription(0, "03");
			GroupAIncidentReport copy69 = new GroupAIncidentReport(incident);
			OffenseSegment offense69 = new OffenseSegment();
			offense69.setUcrOffenseCode("23E");
			PropertySegment propertyDescription69 = new PropertySegment();
			propertyDescription69.setPropertyDescription(0, "03");
			GroupAIncidentReport copy70 = new GroupAIncidentReport(incident);
			OffenseSegment offense70 = new OffenseSegment();
			offense70.setUcrOffenseCode("23F");
			PropertySegment propertyDescription70 = new PropertySegment();
			propertyDescription70.setPropertyDescription(0, "03");
			GroupAIncidentReport copy71 = new GroupAIncidentReport(incident);
			OffenseSegment offense71 = new OffenseSegment();
			offense71.setUcrOffenseCode("23G");
			PropertySegment propertyDescription71 = new PropertySegment();
			propertyDescription71.setPropertyDescription(0, "03");
			//Buses
			GroupAIncidentReport copy72 = new GroupAIncidentReport(incident);
			OffenseSegment offense72 = new OffenseSegment();
			offense72.setUcrOffenseCode("23D");
			PropertySegment propertyDescription72 = new PropertySegment();
			propertyDescription72.setPropertyDescription(0, "05");
			GroupAIncidentReport copy73 = new GroupAIncidentReport(incident);
			OffenseSegment offense73 = new OffenseSegment();
			offense73.setUcrOffenseCode("23E");
			PropertySegment propertyDescription73 = new PropertySegment();
			propertyDescription73.setPropertyDescription(0, "05");
			GroupAIncidentReport copy74 = new GroupAIncidentReport(incident);
			OffenseSegment offense74 = new OffenseSegment();
			offense74.setUcrOffenseCode("23F");
			PropertySegment propertyDescription74 = new PropertySegment();
			propertyDescription74.setPropertyDescription(0, "05");
			GroupAIncidentReport copy75 = new GroupAIncidentReport(incident);
			OffenseSegment offense75 = new OffenseSegment();
			offense75.setUcrOffenseCode("23G");
			PropertySegment propertyDescription75 = new PropertySegment();
			propertyDescription75.setPropertyDescription(0, "05");
			//Other Motor Vehicles
			GroupAIncidentReport copy76 = new GroupAIncidentReport(incident);
			OffenseSegment offense76 = new OffenseSegment();
			offense76.setUcrOffenseCode("23D");
			PropertySegment propertyDescription76 = new PropertySegment();
			propertyDescription76.setPropertyDescription(0, "24");
			GroupAIncidentReport copy77 = new GroupAIncidentReport(incident);
			OffenseSegment offense77 = new OffenseSegment();
			offense77.setUcrOffenseCode("23E");
			PropertySegment propertyDescription77 = new PropertySegment();
			propertyDescription77.setPropertyDescription(0, "24");
			GroupAIncidentReport copy78 = new GroupAIncidentReport(incident);
			OffenseSegment offense78 = new OffenseSegment();
			offense78.setUcrOffenseCode("23F");
			PropertySegment propertyDescription78 = new PropertySegment();
			propertyDescription78.setPropertyDescription(0, "24");
			GroupAIncidentReport copy79 = new GroupAIncidentReport(incident);
			OffenseSegment offense79 = new OffenseSegment();
			offense79.setUcrOffenseCode("23G");
			PropertySegment propertyDescription79 = new PropertySegment();
			propertyDescription79.setPropertyDescription(0, "24");
			//Recreational Vehicles
			GroupAIncidentReport copy80 = new GroupAIncidentReport(incident);
			OffenseSegment offense80 = new OffenseSegment();
			offense80.setUcrOffenseCode("23D");
			PropertySegment propertyDescription80 = new PropertySegment();
			propertyDescription80.setPropertyDescription(0, "28");
			GroupAIncidentReport copy81 = new GroupAIncidentReport(incident);
			OffenseSegment offense81 = new OffenseSegment();
			offense81.setUcrOffenseCode("23E");
			PropertySegment propertyDescription81 = new PropertySegment();
			propertyDescription81.setPropertyDescription(0, "28");
			GroupAIncidentReport copy82 = new GroupAIncidentReport(incident);
			OffenseSegment offense82 = new OffenseSegment();
			offense82.setUcrOffenseCode("23F");
			PropertySegment propertyDescription82 = new PropertySegment();
			propertyDescription82.setPropertyDescription(0, "28");
			GroupAIncidentReport copy83 = new GroupAIncidentReport(incident);
			OffenseSegment offense83 = new OffenseSegment();
			offense83.setUcrOffenseCode("23G");
			PropertySegment propertyDescription83 = new PropertySegment();
			propertyDescription83.setPropertyDescription(0, "28");
			//Structures - Single Occupancy Dwellings
			GroupAIncidentReport copy84 = new GroupAIncidentReport(incident);
			OffenseSegment offense84 = new OffenseSegment();
			offense84.setUcrOffenseCode("23D");
			PropertySegment propertyDescription84 = new PropertySegment();
			propertyDescription84.setPropertyDescription(0, "29");
			GroupAIncidentReport copy85 = new GroupAIncidentReport(incident);
			OffenseSegment offense85 = new OffenseSegment();
			offense85.setUcrOffenseCode("23E");
			PropertySegment propertyDescription85 = new PropertySegment();
			propertyDescription85.setPropertyDescription(0, "29");
			GroupAIncidentReport copy86 = new GroupAIncidentReport(incident);
			OffenseSegment offense86 = new OffenseSegment();
			offense86.setUcrOffenseCode("23F");
			PropertySegment propertyDescription86 = new PropertySegment();
			propertyDescription86.setPropertyDescription(0, "29");
			GroupAIncidentReport copy87 = new GroupAIncidentReport(incident);
			OffenseSegment offense87 = new OffenseSegment();
			offense87.setUcrOffenseCode("23G");
			PropertySegment propertyDescription87 = new PropertySegment();
			propertyDescription87.setPropertyDescription(0, "29");
			//Structures - Other Dwellings
			GroupAIncidentReport copy88 = new GroupAIncidentReport(incident);
			OffenseSegment offense88 = new OffenseSegment();
			offense88.setUcrOffenseCode("23D");
			PropertySegment propertyDescription88 = new PropertySegment();
			propertyDescription88.setPropertyDescription(0, "30");
			GroupAIncidentReport copy89 = new GroupAIncidentReport(incident);
			OffenseSegment offense89 = new OffenseSegment();
			offense89.setUcrOffenseCode("23E");
			PropertySegment propertyDescription89 = new PropertySegment();
			propertyDescription89.setPropertyDescription(0, "30");
			GroupAIncidentReport copy90 = new GroupAIncidentReport(incident);
			OffenseSegment offense90 = new OffenseSegment();
			offense90.setUcrOffenseCode("23F");
			PropertySegment propertyDescription90 = new PropertySegment();
			propertyDescription90.setPropertyDescription(0, "30");
			GroupAIncidentReport copy91 = new GroupAIncidentReport(incident);
			OffenseSegment offense91 = new OffenseSegment();
			offense91.setUcrOffenseCode("23G");
			PropertySegment propertyDescription91 = new PropertySegment();
			propertyDescription91.setPropertyDescription(0, "30");
			//Structures - Commercial/Business
			GroupAIncidentReport copy92 = new GroupAIncidentReport(incident);
			OffenseSegment offense92 = new OffenseSegment();
			offense92.setUcrOffenseCode("23D");
			PropertySegment propertyDescription92 = new PropertySegment();
			propertyDescription92.setPropertyDescription(0, "31");
			GroupAIncidentReport copy93 = new GroupAIncidentReport(incident);
			OffenseSegment offense93 = new OffenseSegment();
			offense93.setUcrOffenseCode("23E");
			PropertySegment propertyDescription93 = new PropertySegment();
			propertyDescription93.setPropertyDescription(0, "31");
			GroupAIncidentReport copy94 = new GroupAIncidentReport(incident);
			OffenseSegment offense94 = new OffenseSegment();
			offense94.setUcrOffenseCode("23F");
			PropertySegment propertyDescription94 = new PropertySegment();
			propertyDescription94.setPropertyDescription(0, "31");
			GroupAIncidentReport copy95 = new GroupAIncidentReport(incident);
			OffenseSegment offense95 = new OffenseSegment();
			offense95.setUcrOffenseCode("23G");
			PropertySegment propertyDescription95 = new PropertySegment();
			propertyDescription95.setPropertyDescription(0, "31");
			//Structures - Industrial/Manufacturing
			GroupAIncidentReport copy96 = new GroupAIncidentReport(incident);
			OffenseSegment offense96 = new OffenseSegment();
			offense96.setUcrOffenseCode("23D");
			PropertySegment propertyDescription96 = new PropertySegment();
			propertyDescription96.setPropertyDescription(0, "32");
			GroupAIncidentReport copy97 = new GroupAIncidentReport(incident);
			OffenseSegment offense97 = new OffenseSegment();
			offense97.setUcrOffenseCode("23E");
			PropertySegment propertyDescription97 = new PropertySegment();
			propertyDescription97.setPropertyDescription(0, "32");
			GroupAIncidentReport copy98 = new GroupAIncidentReport(incident);
			OffenseSegment offense98 = new OffenseSegment();
			offense98.setUcrOffenseCode("23F");
			PropertySegment propertyDescription98 = new PropertySegment();
			propertyDescription98.setPropertyDescription(0, "32");
			GroupAIncidentReport copy99 = new GroupAIncidentReport(incident);
			OffenseSegment offense99 = new OffenseSegment();
			offense99.setUcrOffenseCode("23G");
			PropertySegment propertyDescription99 = new PropertySegment();
			propertyDescription99.setPropertyDescription(0, "32");
			//Structures - Public/Community
			GroupAIncidentReport copy100 = new GroupAIncidentReport(incident);
			OffenseSegment offense100 = new OffenseSegment();
			offense100.setUcrOffenseCode("23D");
			PropertySegment propertyDescription100 = new PropertySegment();
			propertyDescription100.setPropertyDescription(0, "33");
			GroupAIncidentReport copy101 = new GroupAIncidentReport(incident);
			OffenseSegment offense101 = new OffenseSegment();
			offense101.setUcrOffenseCode("23E");
			PropertySegment propertyDescription101 = new PropertySegment();
			propertyDescription101.setPropertyDescription(0, "33");
			GroupAIncidentReport copy102 = new GroupAIncidentReport(incident);
			OffenseSegment offense102 = new OffenseSegment();
			offense102.setUcrOffenseCode("23F");
			PropertySegment propertyDescription102 = new PropertySegment();
			propertyDescription102.setPropertyDescription(0, "33");
			GroupAIncidentReport copy103 = new GroupAIncidentReport(incident);
			OffenseSegment offense103 = new OffenseSegment();
			offense103.setUcrOffenseCode("23G");
			PropertySegment propertyDescription103 = new PropertySegment();
			propertyDescription103.setPropertyDescription(0, "33");
			//Structures - Storage
			GroupAIncidentReport copy104 = new GroupAIncidentReport(incident);
			OffenseSegment offense104 = new OffenseSegment();
			offense104.setUcrOffenseCode("23D");
			PropertySegment propertyDescription104 = new PropertySegment();
			propertyDescription104.setPropertyDescription(0, "34");
			GroupAIncidentReport copy105 = new GroupAIncidentReport(incident);
			OffenseSegment offense105 = new OffenseSegment();
			offense105.setUcrOffenseCode("23E");
			PropertySegment propertyDescription105 = new PropertySegment();
			propertyDescription105.setPropertyDescription(0, "34");
			GroupAIncidentReport copy106 = new GroupAIncidentReport(incident);
			OffenseSegment offense106 = new OffenseSegment();
			offense106.setUcrOffenseCode("23F");
			PropertySegment propertyDescription106 = new PropertySegment();
			propertyDescription106.setPropertyDescription(0, "34");
			GroupAIncidentReport copy107 = new GroupAIncidentReport(incident);
			OffenseSegment offense107 = new OffenseSegment();
			offense107.setUcrOffenseCode("23G");
			PropertySegment propertyDescription107 = new PropertySegment();
			propertyDescription107.setPropertyDescription(0, "34");
			//Structures - Other
			GroupAIncidentReport copy108 = new GroupAIncidentReport(incident);
			OffenseSegment offense108 = new OffenseSegment();
			offense108.setUcrOffenseCode("23D");
			PropertySegment propertyDescription108 = new PropertySegment();
			propertyDescription108.setPropertyDescription(0, "35");
			GroupAIncidentReport copy109 = new GroupAIncidentReport(incident);
			OffenseSegment offense109 = new OffenseSegment();
			offense109.setUcrOffenseCode("23E");
			PropertySegment propertyDescription109 = new PropertySegment();
			propertyDescription109.setPropertyDescription(0, "35");
			GroupAIncidentReport copy110 = new GroupAIncidentReport(incident);
			OffenseSegment offense110 = new OffenseSegment();
			offense110.setUcrOffenseCode("23F");
			PropertySegment propertyDescription110 = new PropertySegment();
			propertyDescription110.setPropertyDescription(0, "35");
			GroupAIncidentReport copy111 = new GroupAIncidentReport(incident);
			OffenseSegment offense111 = new OffenseSegment();
			offense111.setUcrOffenseCode("23G");
			PropertySegment propertyDescription111 = new PropertySegment();
			propertyDescription111.setPropertyDescription(0, "35");
			//Trucks
			GroupAIncidentReport copy112 = new GroupAIncidentReport(incident);
			OffenseSegment offense112 = new OffenseSegment();
			offense112.setUcrOffenseCode("23D");
			PropertySegment propertyDescription112 = new PropertySegment();
			propertyDescription112.setPropertyDescription(0, "37");
			GroupAIncidentReport copy113 = new GroupAIncidentReport(incident);
			OffenseSegment offense113 = new OffenseSegment();
			offense113.setUcrOffenseCode("23E");
			PropertySegment propertyDescription113 = new PropertySegment();
			propertyDescription113.setPropertyDescription(0, "37");
			GroupAIncidentReport copy114 = new GroupAIncidentReport(incident);
			OffenseSegment offense114 = new OffenseSegment();
			offense114.setUcrOffenseCode("23F");
			PropertySegment propertyDescription114 = new PropertySegment();
			propertyDescription114.setPropertyDescription(0, "37");
			GroupAIncidentReport copy115 = new GroupAIncidentReport(incident);
			OffenseSegment offense115 = new OffenseSegment();
			offense115.setUcrOffenseCode("23G");
			PropertySegment propertyDescription115 = new PropertySegment();
			propertyDescription115.setPropertyDescription(0, "37");
			//PropertySegment descriptions for vehicles are illogical with 23H=All Other Larceny
			GroupAIncidentReport copy116 = new GroupAIncidentReport(incident);
			OffenseSegment offense116 = new OffenseSegment();
			offense116.setUcrOffenseCode("23H");
			PropertySegment propertyDescription116 = new PropertySegment();
			propertyDescription116.setPropertyDescription(0, "03");
			GroupAIncidentReport copy117 = new GroupAIncidentReport(incident);
			OffenseSegment offense117 = new OffenseSegment();
			offense117.setUcrOffenseCode("23H");
			PropertySegment propertyDescription117 = new PropertySegment();
			propertyDescription117.setPropertyDescription(0, "05");
			GroupAIncidentReport copy118 = new GroupAIncidentReport(incident);
			OffenseSegment offense118 = new OffenseSegment();
			offense118.setUcrOffenseCode("23H");
			PropertySegment propertyDescription118 = new PropertySegment();
			propertyDescription118.setPropertyDescription(0, "24");
			GroupAIncidentReport copy119 = new GroupAIncidentReport(incident);
			OffenseSegment offense119 = new OffenseSegment();
			offense119.setUcrOffenseCode("23H");
			PropertySegment propertyDescription119 = new PropertySegment();
			propertyDescription119.setPropertyDescription(0, "28");
			GroupAIncidentReport copy120 = new GroupAIncidentReport(incident);
			OffenseSegment offense120 = new OffenseSegment();
			offense120.setUcrOffenseCode("23H");
			PropertySegment propertyDescription120 = new PropertySegment();
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
		
		groupATweakerMap.put(391, incident -> {
			//(Value of PropertySegment) has a code that requires a zero value in Data Element 16 (Value of PropertySegment). 
			//Either the wrong property description code was entered or the property value was not entered.
			//(This error was formerly error number 340, a warning message.) Data Element 16 
			//(Value of PropertySegment) must be zero when Data Element 15 (PropertySegment Description) is:
			//09=Credit/Debit Cards
			//22=Non-negotiable Instruments
			//48=Documents�Personal or Business
			//65=Identity Documents
			//66=Identity�Intangible
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription = new PropertySegment();
			propertyDescription.setPropertyDescription(0, "09");
			PropertySegment valueOfProperty = new PropertySegment();
			valueOfProperty.setValueOfProperty(0,000000500);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription2 = new PropertySegment();
			propertyDescription2.setPropertyDescription(0, "22");
			PropertySegment valueOfProperty2 = new PropertySegment();
			valueOfProperty2.setValueOfProperty(0,000000500);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription3 = new PropertySegment();
			propertyDescription3.setPropertyDescription(0, "48");
			PropertySegment valueOfProperty3 = new PropertySegment();
			valueOfProperty3.setValueOfProperty(0,000000500);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription4 = new PropertySegment();
			propertyDescription4.setPropertyDescription(0, "65");
			PropertySegment valueOfProperty4 = new PropertySegment();
			valueOfProperty4.setValueOfProperty(0,000000500);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			PropertySegment propertyDescription5 = new PropertySegment();
			propertyDescription5.setPropertyDescription(0, "66");
			PropertySegment valueOfProperty5 = new PropertySegment();
			valueOfProperty5.setValueOfProperty(0,000000500);
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			
			return incidents;
		});
		
		
		groupATweakerMap.put(401, incident -> {
			// The referenced data element in a Group A Incident AbstractReport
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
			// The referenced data element in a Group A Incident AbstractReport
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
			// The referenced data element in a Group A Incident AbstractReport
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
