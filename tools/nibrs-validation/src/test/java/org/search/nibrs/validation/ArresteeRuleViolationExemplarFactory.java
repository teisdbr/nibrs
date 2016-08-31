package org.search.nibrs.validation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupAIncidentReport;

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
			copy5.getArrestees().get(0).setArresteeSequenceNumber(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.getArrestees().get(0).setArrestTransactionNumber(null);
			//(Type of Arrest) The referenced data element in a Group A Incident Report
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			copy7.getArrestees().get(0).setTypeOfArrest("A");
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			copy8.getArrestees().get(0).setTypeOfArrest(null);
			//(UCR Arrest Offense Code) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			copy9.getArrestees().get(0).setUcrArrestOffenseCode("A");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			copy10.getArrestees().get(0).setUcrArrestOffenseCode(null);
			//(Arrestee Was Armed With) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy11 = new GroupAIncidentReport(copy);
			copy11.getArrestees().get(0).setArresteeArmedWith(0, null);
			GroupAIncidentReport copy12 = new GroupAIncidentReport(copy);
			copy12.getArrestees().get(0).setArresteeArmedWith(0,"00");
			//Arrest Date cannot be blank
			GroupAIncidentReport copy13 = new GroupAIncidentReport(copy);
			copy13.getArrestees().get(0).setArrestDate(null);
			//(Multiple Arrestee Segments Indicator) The referenced data element in a 
			//A Incident Report must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy14 = new GroupAIncidentReport(copy);
			copy14.getArrestees().get(0).setMultipleArresteeSegmentsIndicator(null);
			GroupAIncidentReport copy15 = new GroupAIncidentReport(copy);
			copy15.getArrestees().get(0).setMultipleArresteeSegmentsIndicator("A");
			//(Age of Arrestee) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy16 = new GroupAIncidentReport(copy);
			copy16.getArrestees().get(0).setAgeString(null);
			GroupAIncidentReport copy17 = new GroupAIncidentReport(copy);
			copy17.getArrestees().get(0).setAgeString("AA");
			//(Sex of Arrestee) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy18 = new GroupAIncidentReport(copy);
			copy18.getArrestees().get(0).setSex(null);
			GroupAIncidentReport copy19 = new GroupAIncidentReport(copy);
			copy19.getArrestees().get(0).setSex("A");
			
			
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
			
			return incidents;
		});
		
		groupATweakerMap.put(604, incident -> {
			//(Automatic Weapon Indicator) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArresteeArmedWith(0, "11");
			copy.getArrestees().get(0).setAutomaticWeaponIndicator(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(copy);
			copy.getArrestees().get(0).setAutomaticWeaponIndicator(0, "X");
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
			copy.getArrestees().get(0).setArrestDate(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			
			
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
		
		groupATweakerMap.put(609, incident -> {
			//(Age of Arrestee) contains more than two characters indicating 
			//a possible age-range is being attempted. If so, the field must 
			//contain a numeric entry of four digits.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAgeString("022");
			incidents.add(copy);
					
			return incidents;
					
		});
			
		groupATweakerMap.put(610, incident -> {
			//(Age of Arrestee) was entered as an age-range. Accordingly, 
			//the first age component must be less than the second age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAgeString("3025");
			incidents.add(copy);
					
			return incidents;
					
		});
				
			
			
			
	
		
		groupATweakerMap.put(617, incident -> {
			//(Arrest Transaction Number) Must contain a valid character combination of the following:
			//A–Z (capital letters only)
			//0–9
			//Hyphen
			//Example: 11-123-SC is valid, but 11+123*SC is not valid
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArrestTransactionNumber("11+123*SC");
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(622, incident -> {
			//(Age of Arrestee) was entered as an age-range. 
			//Therefore, the first age component cannot be 00 (unknown).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAgeString("0025");
			incidents.add(copy);
					
			return incidents;
					
		});
				
		groupATweakerMap.put(641, incident -> {
			//(Age of Arrestee) was entered with a value of 99 which means the 
			//arrestee is over 98 years old. The submitter should verify that 
			//99=Over 98 Years Old is not being confused the with 00=Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setAgeString("99");
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
			copy.getArrestees().get(0).setArrestDate(Date.from(LocalDateTime.of(2016, 4, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(670, incident -> {
			//(UCR Arrest Offense Code) was entered with 09C=Justifiable Homicide. This is not a valid arrest offense
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setUcrArrestOffenseCode("09C");
			
			incidents.add(copy);
			
			return incidents;
			
		});
	}
	
}
