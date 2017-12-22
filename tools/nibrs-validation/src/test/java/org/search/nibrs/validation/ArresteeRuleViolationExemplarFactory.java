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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;

final class ArresteeRuleViolationExemplarFactory {

	private static final ArresteeRuleViolationExemplarFactory INSTANCE = new ArresteeRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ArresteeRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private ArresteeRuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final ArresteeRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> getGroupATweakerMap() {
		return groupATweakerMap;
	}

	private void populateGroupAExemplarMap() {
		
		groupATweakerMap.put(656, incident -> {
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenderSegment offender = new OffenderSegment();
			offender.setOffenderSequenceNumber(new ParsedObject<>(0));
			ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
			arrestee.setArresteeSequenceNumber(new ParsedObject<>(1));
			copy.addArrestee(arrestee);
			incidents.add(copy);
			copy = new GroupAIncidentReport(copy);
			copy.getOffenders().get(0).setOffenderSequenceNumber(new ParsedObject<>(1));
			arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
			arrestee.setArresteeSequenceNumber(new ParsedObject<>(2));
			copy.addArrestee(arrestee);
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(669, incident -> {
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense = new OffenseSegment();
			offense.setUcrOffenseCode(OffenseCode._09C.code);
			copy.addOffense(offense);
			ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
			arrestee.setArresteeSequenceNumber(new ParsedObject<>(1));
			copy.addArrestee(arrestee);
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(661, incident -> {
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
			copy.addArrestee(arrestee);
			arrestee.setArresteeSequenceNumber(new ParsedObject<>(1));
			arrestee.setAge(NIBRSAge.getAge(23, null));
			arrestee.setRace("W");
			arrestee.setSex("M");
			arrestee.setArrestTransactionNumber("67890");
			arrestee.setArrestDate(new ParsedObject<>(Date.from(LocalDate.of(2015, 5, 16).atStartOfDay(ZoneId.systemDefault()).toInstant())));
			arrestee.setUcrArrestOffenseCode("13A");
			arrestee.setTypeOfArrest("O");
			arrestee.setMultipleArresteeSegmentsIndicator("N");
			arrestee.setArresteeArmedWith(0,"01");
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(71, incident -> {
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setReportActionType('I');
			
			copy.setExceptionalClearanceCode(ClearedExceptionallyCode.A.code);
			Calendar c = Calendar.getInstance();
			c.set(2016, Calendar.JANUARY, 1);
			LocalDate d = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
			copy.setExceptionalClearanceDate(new ParsedObject<>(Date.from(d.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
			c.set(2016, Calendar.JANUARY, 1);
			d = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
			copy.getArrestees().get(0).setArrestDate(new ParsedObject<>(Date.from(d.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(601, incident -> {
			
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 6 is mandatory & must be present.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArresteeSequenceNumber(ParsedObject.getMissingParsedObject());
			incidents.add(copy);
			
			copy.getArrestees().get(0).setArrestTransactionNumber(null);
			incidents.add(copy);
			//(Type of Arrest) The referenced data element in a Group A Incident Report
			//must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setTypeOfArrest("A");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setTypeOfArrest(null);
			incidents.add(copy);
			//(UCR Arrest Offense Code) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setUcrArrestOffenseCode("A");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setUcrArrestOffenseCode(null);
			incidents.add(copy);
			//(Arrestee Was Armed With) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArresteeArmedWith(0, null);
			incidents.add(copy);
			//Arrest Date cannot be blank
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArrestDate(ParsedObject.getMissingParsedObject());
			incidents.add(copy);
			//(Multiple Arrestee Segments Indicator) The referenced data element in a 
			//A Incident Report must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setMultipleArresteeSegmentsIndicator(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setMultipleArresteeSegmentsIndicator("A");
			incidents.add(copy);
			//(Age of Arrestee) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAge(null);
			incidents.add(copy);
			//(Sex of Arrestee) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setSex(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setSex("A");
			incidents.add(copy);
			//(Race of Arrestee) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setRace(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setRace("X");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(604, incident -> {
			
			//(Automatic Weapon Indicator) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//(Ethnicity of Arrestee) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setEthnicity("X");
			incidents.add(copy);
			//(Resident Status of Arrestee) The referenced data element in a 
			//Group A Incident Report must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setResidentStatus("X");
			incidents.add(copy);
			//Disposition of Arrestee under 18 must be valid
			copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAge(NIBRSAge.getAge(16, null));
			copy.getArrestees().get(0).setDispositionOfArresteeUnder18("A");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(605, incident -> {
			//The date cannot be later than that entered within the Month of Electronic submission 
			//and Year of Electronic submission fields on the data record. For example, 
			//if Month of Electronic submission and Year of Electronic submission are 06/1999, 
			//the arrest date cannot contain any date 07/01/1999 or later.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArrestDate(new ParsedObject<>(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(606, incident -> {
			//(Arrestee Was Armed With) The referenced data element in error is one
			//that contains multiple data values. When more than one code is entered, none can be duplicate codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArresteeArmedWith(0, "12");
			copy.getArrestees().get(0).setArresteeArmedWith(1, "12");
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(607, incident -> {
			//(Arrestee Was Armed With) The referenced data element in error is one
			//that contains multiple data values. When more than one code is entered, none can be duplicate codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArresteeArmedWith(0, "01");
			copy.getArrestees().get(0).setArresteeArmedWith(1, "12");
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(610, incident -> {
			//(Age of Arrestee) was entered as an age-range. Accordingly, 
			//the first age component must be less than the second age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAge(NIBRSAge.getAge(30, 25));
			incidents.add(copy);
					
			return incidents;
					
		});
				
		groupATweakerMap.put(617, incident -> {
			//(Arrest Transaction Number) Must contain a valid character combination of the following:
			//AZ (capital letters only)
			//09
			//Hyphen
			//Example: 11-123-SC is valid, but 11+123*SC is not valid
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArrestTransactionNumber("11+123*SC");
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		//TO-DO groupATweakerMap.put(623, incident -> {
			//Clearance Indicator and Clearance Offense Code must be 
			//blank when Segment Action Type on Level 6 (Arrestee Segment) is I=Incident.
		
		// note: rules 640 and 641 are just warnings, so we don't implement for now...
		
		groupATweakerMap.put(640, incident -> {
			//(Disposition of Arrestee Under 18) was not entered, but Data Element 47 
			//(Age of Arrestee) indicates an age-range for a juvenile. The low age is a 
			//juvenile and the high age is an adult, but the average age is a juvenile.
			//Note: When an age-range is not entered and the age is a juvenile, then the 
			//disposition must be entered. These circumstances were flagged by the computer 
			//as a possible discrepancy between age and disposition and should be checked for 
			//possible correction by the participant.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAge(NIBRSAge.getAge(8, 26));
			
			incidents.add(copy);
			
			return incidents;
			
		});	
		
		groupATweakerMap.put(641, incident -> {
			//(Age of Arrestee) was entered with a value of 99 which means the 
			//arrestee is over 98 years old. The submitter should verify that 
			//99=Over 98 Years Old is not being confused the with 00=Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAge(NIBRSAge.getAge(99, null));
			incidents.add(copy);
					
			return incidents;
					
		});
		
		groupATweakerMap.put(652, incident -> {
			//(Disposition of Arrestee Under 18) was not entered, but Data Element 47 
			//(Age of Arrestee) is under 18. Whenever an arrestees age indicates a juvenile, 
			//the disposition must be entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAge(NIBRSAge.getAge(16, null));
			
			incidents.add(copy);
			
			return incidents;
			
		});	
		
		groupATweakerMap.put(653, incident -> {
			//(Disposition of Arrestee Under 18) was entered, but Data Element 47 
			//(Age of Arrestee) is 18 or greater. Whenever an arrestees age indicates an adult, 
			//the juvenile disposition cannot be entered because it does not apply.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAge(NIBRSAge.getAge(18, null));
			copy.getArrestees().get(0).setDispositionOfArresteeUnder18("H");
			
			incidents.add(copy);
			
			return incidents;
			
		});	
		
		groupATweakerMap.put(654, incident -> {
			//This case may be duplicative of the same element in Rule 604.
			//(Automatic Weapon Indicator) does not have A=Automatic or a blank in the third position of field.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArresteeArmedWith(0, "11");
			copy.getArrestees().get(0).setAutomaticWeaponIndicator(0, "X");
			incidents.add(copy);
			
			return incidents;
		});
		
		
		groupATweakerMap.put(655, incident -> {
			//(Automatic Weapon Indicator) In Data Element 46 (Arrestee Was Armed With), 
			//A=Automatic is the third character of code. It is valid only with codes:
			//11=Firearm (Type Not Stated)
			//12=Handgun
			//13=Rifle
			//14=Shotgun
			//15=Other Firearm
			//A weapon code other than those mentioned was entered with the automatic indicator. 
			//An automatic weapon is, by definition, a firearm.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArresteeArmedWith(0, "01");
			copy.getArrestees().get(0).setAutomaticWeaponIndicator(0, "A");
			incidents.add(copy);
			copy = new GroupAIncidentReport(copy);
			copy.getArrestees().get(0).setArresteeArmedWith(0, "16");
			incidents.add(copy);
			copy = new GroupAIncidentReport(copy);
			copy.getArrestees().get(0).setArresteeArmedWith(0, "17");
			incidents.add(copy);
			
			return incidents;
		});
		
		groupATweakerMap.put(665, incident -> {
			//(Arrest Date) cannot be earlier than Data Element 3 (Incident Date/Hour). 
			//A person cannot be arrested before the incident occurred.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArrestDate(new ParsedObject<>(Date.from(LocalDateTime.of(2016, 4, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(667, incident -> {
			//(Sex of Arrestee) does not contain a valid code of M=Male or F=Female. 
			//Note: U=Unknown (if entered) is not a valid sex for an arrestee.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setSex("U");
			incidents.add(copy);
			return incidents;
		});
			
		//TO-DO groupATweakerMap.put(668, incident -> {
			//Clearance Indicator cannot contain Y=Yes for more than one 
			//Segment Level 6 (Arrestee Segment) submitted for an incident.
			
		groupATweakerMap.put(670, incident -> {
			//(UCR Arrest Offense Code) was entered with 09C=Justifiable Homicide. This is not a valid arrest offense
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setUcrArrestOffenseCode("09C");
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		//TO-DO groupATweakerMap.put(671, incident -> {
		//The referenced data element in a Group A Incident Report must 
		//be populated with a valid data value and cannot be blank.
		
	}
	
}
