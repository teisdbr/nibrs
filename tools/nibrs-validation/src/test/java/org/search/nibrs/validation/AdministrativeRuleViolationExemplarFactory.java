/*
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.validation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.GroupAIncidentReport;

class AdministrativeRuleViolationExemplarFactory {
	
	private static final AdministrativeRuleViolationExemplarFactory INSTANCE = new AdministrativeRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AdministrativeRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private AdministrativeRuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final AdministrativeRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> getGroupATweakerMap() {
		return groupATweakerMap;
	}

	private void populateGroupAExemplarMap() {

		groupATweakerMap.put(75, incident -> {
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.removeOffenders();
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.removeOffenses();
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.removeVictims();
			incidents.add(copy);
			
			return incidents;
			
		});

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
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setMonthOfTape(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setOri(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setIncidentNumber(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(ParsedObject.getMissingParsedObject());
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setExceptionalClearanceCode(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setExceptionalClearanceCode("K"); // not valid code
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(104, incident -> {
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 1 must be valid.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(1054);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setMonthOfTape(14);
			incidents.add(copy);
			// ReportDateIndicator should be set to "R" if unknown.
			copy = new GroupAIncidentReport(incident);
			copy.setReportDateIndicator("S");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setCargoTheftIndicator("X");
			copy.setIncludesCargoTheft(true);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.setCargoTheftIndicator("X");
			copy.setIncludesCargoTheft(true);
			incidents.add(copy);
			return incidents;

		});

		groupATweakerMap.put(105, incident -> {
			// The data element in error contains a date that is not entered correctly.
			// Each component of the date must be valid; that is, months must be 01 through 12,
			// days must be 01 through 31, and year must include the century (i.e., 19xx, 20xx).
			// In addition, days cannot exceed maximum for the month (e.g., June cannot have 31days).
			// Also, the date cannot exceed the current date.
			//
			//Invalid Year of Tape
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(0120);
			copy.setMonthOfTape(5);
			//Invalid Month of Tape
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy.setYearOfTape(2016);
			copy.setMonthOfTape(13);
			copy.setIncidentDate(new ParsedObject<>(Date.from(LocalDateTime.of(2016, 6, 31, 30, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			//Invalid Exceptional Clearance date
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy.setExceptionalClearanceDate(new ParsedObject<>(Date.from(LocalDateTime.of(3016, 13, 12, 30, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
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
			ret.setIncidentNumber("89+123*SC   ");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(119, incident -> {
			// Data Element 2A (Cargo Theft) must be populated with a valid data value when
			// Data Element 6 (UCR OffenseSegment Code) contains a Cargo Theft-related offense.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setCargoTheftIndicator(null);
			copy.getOffenses().get(0).setUcrOffenseCode("120");
			incidents.add(copy);
			return incidents;
		});

		//TO-DO Rule 122
		//Data Element 2A (Cargo Theft) can be Y=Yes only when Data Element 6 
		//(UCR Offense Code) includes at least one of the following:
		//120=Robbery
		//		210=Extortion/Blackmail
		//		220=Burglary/Breaking & Entering
		//		23D=Theft From Building
		//		23F=Theft From Motor Vehicle
		//		24H=All Other Larceny
		//		240=Motor Vehicle Theft
		//		26A=False Pretenses/Swindle/Confidence Game
		//		26B=Credit Card/Automated Teller Machine Fraud
		//		26C=Impersonation
		//		26E=Wire Fraud
		//		270=Embezzlement
		//		510=Bribery
		
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
			copy = new GroupAIncidentReport(incident);
			copy.setIncidentHour(new ParsedObject<Integer>(24));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(153, incident -> {
			// Data Element 4 ((Cleared Exceptionally) Cannot be N=Not Applicable if Data Element 5 (Exceptional Clearance Date) is entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setExceptionalClearanceCode("N");
			copy.setExceptionalClearanceDate(new ParsedObject<>(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(155, incident -> {
			// Data Element 5 (Exceptional Clearance Date) is earlier than Data Element 3 (Incident Date/Hour).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(new ParsedObject<>(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			copy.setExceptionalClearanceDate(new ParsedObject<>(Date.from(LocalDateTime.of(2015, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(156, incident -> {
			// Data Element 5 (Exceptional Clearance Date) must be present if the case was cleared exceptionally.
			// Data Element 4 (Cleared Exceptionally) has an entry of A through E; therefore, the date must also be entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setExceptionalClearanceCode("A");
			copy.setExceptionalClearanceDate(ParsedObject.getMissingParsedObject());
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(170, incident -> {
			// Data Element 3 (Incident Date) The date cannot be later than the year and month the electronic submission represents.
			// For example, the May 1999 electronic submission cannot contain incidents happening after this date.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(new ParsedObject<>(Date.from(LocalDateTime.of(3016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(171, incident -> {
			// A Group A Incident AbstractReport was submitted with a date entered into Data Element 3 (Incident Date/Hour)
			// that is earlier than January 1 of the previous year, using the Month of Tape and Year of Tape as a reference point,
			// e.g., if the Month of Tape and Year of Tape contain a value of 01/1999, but the incident date is 12/25/1997, the incident will be rejected.
			// Volume 2, section I, provides specifications concerning the FBIs 2-year database.
			// For example, the May 1999 electronic submission cannot contain incidents happening after this date.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(new ParsedObject<>(Date.from(LocalDateTime.of(2000, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(172, incident -> {
			// (Incident Date) cannot be earlier than 01/01/1991. This edit will preclude dates that are obviously
			// incorrect since the FBI began accepting NIBRS data on this date.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(new ParsedObject<>(Date.from(LocalDateTime.of(1990, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(173, incident -> {
			// Data Element 5 (Exceptional Clearance Date) cannot contain a date earlier than the date the LEA began submitting data via the NIBRS.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setIncidentDate(new ParsedObject<>(Date.from(LocalDateTime.of(1990, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			GroupAIncidentReport copy1 = new GroupAIncidentReport(incident);
			copy1.setExceptionalClearanceDate(new ParsedObject<>(Date.from(LocalDateTime.of(1016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			incidents.add(copy);
			incidents.add(copy1);
			return incidents;
		});

	}

}
