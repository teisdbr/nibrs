package org.search.nibrs.validation;
//The Offender Segment is used to describe the offenders in the incident 
//(e.g., their age, sex, race, and ethnicity). An Offender Segment should be 
//submitted for each of the offenders (up to 99) involved in the incident. 
//There must be at least one Offender Segment in each incident report.
//When nothing is known about the offender, then 00=Unknown Offender should be 
//entered in Data Element 36 (Offender Sequence Number) and 
//Data Elements 37 through 39 should be left blank. For example, when a corpse 
//is found in a ditch and there were no eyewitnesses or other information 
//that would provide data about possible offenders, 00=Unknown Offender should be entered. 
//However, when witnesses report five offenders were running from the scene, 
//and their age, sex, or race are not known, five Offender Segments should be submitted 
//	indicating the appropriate data elements are unknown.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupAIncidentReport;

final class OffenderRuleViolationExemplarFactory {

	private static final OffenderRuleViolationExemplarFactory INSTANCE = new OffenderRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(OffenderRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private OffenderRuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final OffenderRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> getGroupATweakerMap() {
		return groupATweakerMap;
	}

	private void populateGroupAExemplarMap() {
		
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
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			
			return incidents;
		});

		groupATweakerMap.put(504, incident -> {
			//(Age of Offender) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString(null);
			incidents.add(copy);
			//(Sex of Offender) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenders().get(0).setSex(null);
			incidents.add(copy2);
			//(Race of Offender) The referenced data element in a Group A Incident 
			//Report must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenders().get(0).setRace(null);
			incidents.add(copy3);
			//(Ethnicity of Offender) The referenced data element in a Group A Incident 
			//Report must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenders().get(0).setEthnicity(null);
			incidents.add(copy4);
			
			
			return incidents;
			
		});	
			
		groupATweakerMap.put(509, incident -> {
			//(Age of Offender) contains more than two characters indicating a 
			//possible age-range is being attempted. If so, the field must contain a 
			//numeric entry of four digits.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("132");
			
			
			incidents.add(copy);
			
			return incidents;
			
		});		
		
		groupATweakerMap.put(510,incident -> {
			//(Age of Offender) was entered as an age-range. Accordingly, the 
			//rst age component must be less than the second age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("3020");
			
			
			incidents.add(copy);
			
			return incidents;
			
		});		
		
		groupATweakerMap.put(522,incident -> {
			//(Age of Offender) was entered as an age-range. Therefore, 
			//the first age component cannot be 00 (unknown).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("0020");
			
			
			incidents.add(copy);
			
			return incidents;
			
		});		
		
		groupATweakerMap.put(550,incident -> {
			//(Age of Offender) cannot be less than 10 years old when 
			//Data Element 35 (Relationship of Victim to Offender) 
			//contains a relationship of SE=Spouse.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("09");
						
			incidents.add(copy);
			
			return incidents;
			
		});		
			
		groupATweakerMap.put(552,incident -> {
			//(Age of Offender Data Element 38 (Sex of Offender), and Data Element 39 
			//(Race of Offender) cannot be entered when Data Element 36 
			//(Offender Sequence Number) is 00=Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(00);
			incidents.add(copy);
			//(Sex of Offender) Data Element 38 (Sex of Offender), and Data Element 39 
			//(Race of Offender) cannot be entered when Data Element 36 
			//(Offender Sequence Number) is 00=Unknown.						
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setRace(null);
			incidents.add(copy);
			//(Race of Offender) Data Element 38 (Sex of Offender), and 
			//Data Element 39 (Race of Offender) cannot be entered when Data Element 36 
			//(Offender Sequence Number) is 00=unknown.
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setSex(null);
			copy.getOffenders().get(0).setRace("W");
			incidents.add(copy);
			//(Ethnicity of Offender) Data Element 38 (Sex of Offender), and 
			//Data Element 39 (Race of Offender) cannot be entered when 
			//Data Element 36 (Offender Sequence Number) is 00=Unknown.	
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setSex("F");
			copy.getOffenders().get(0).setEthnicity(null);
			incidents.add(copy);
			
			
			
			return incidents;
			
		});		
		
		groupATweakerMap.put(554,incident -> {
			//(Sex of Offender) has a relationship that is inconsistent with the 
			//offender’s sex. The sex of the victim and/or offender must reflect 
			//the implied relationship. For example, if the relationship of the 
			//to offender is Homosexual Relationship, then the victim’s sex must be the same 
			//as the offender’s sex. The following relationships must reflect either the Same 
			//or Different sex codes depending upon this relationship:
			//Relationship Sex Code
			//
			//BG=Victim was Boyfriend/Girlfriend Different
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "BG");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//XS=Victim was Ex-Spouse Different
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "XS");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//SE=Victim was Spouse Different
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "SE");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//CS=Victim was Common-Law Spouse Different
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "CS");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//HR=Homosexual Relationship Same
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "HR");
			copy.getVictims().get(0).setSex("M");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			
			return incidents;
			
		});		
			
		
		
		groupATweakerMap.put(554,incident -> {
			//(Age of Offender) has a relationship that is inconsistent with the 
			//offender’s age. The age of the victim and/or offender must reflect 
			//the implied relationship. For example, if the relationship of the victim 
			//to offender is PA=Parent, then the victim’s age must be greater than 
			//the offender’s age. The following relationships must be consistent with 
			//the victim’s age in relation to the offender’s age:
			//Relationship Victim’s Age Is:
			//CH=Victim was Child Younger
			//PA=Victim was Parent Older
			//GP=Victim was Grandparent Older
			//GC=Victim was Grandchild Younger
			//
			//Victim is parent - Offender Age must be younger than victim age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "PA");
			copy.getVictims().get(0).setAgeString("10");
			copy.getOffenders().get(0).setAgeString("30");
			incidents.add(copy);
			//Victim is child - offender Age must be older.
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "CH");
			copy.getVictims().get(0).setAgeString("30");
			copy.getOffenders().get(0).setAgeString("10");
			incidents.add(copy);
			//Victim is grandparent - offender must be younger
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "GP");
			copy.getVictims().get(0).setAgeString("50");
			copy.getOffenders().get(0).setAgeString("60");
			incidents.add(copy);
			//Victim is grandchild - offender must be older
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "GC");
			copy.getVictims().get(0).setAgeString("50");
			copy.getOffenders().get(0).setAgeString("40");
			incidents.add(copy);
									
					
			return incidents;
			
		});		
		
		groupATweakerMap.put(556,incident -> {
			//(Age of Offender) must contain numeric entry of 00 through 99.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(100);
									
			incidents.add(copy);
			
			return incidents;
			
		});		
		
		groupATweakerMap.put(557, incident -> {
			//(Offender Sequence Number) contains 00 indicating that nothing is 
			//known about the offender(s) regarding number and any identifying information. 
			//In order to exceptionally clear the incident, the value cannot be 00. 
			//The incident was submitted with Data Element 4 (Cleared Exceptionally) having a value of A through E.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(00);
			
			incidents.add(copy);
			
			return incidents;
		
		});
		
		groupATweakerMap.put(572,incident -> {
			//Data Element 37 (Age of Offender) If Data Element 37 (Age of Offender) is 
			//00=Unknown, Data Element 38 (Sex of Offender) is U=Unknown, and 
			//Data Element 39 (Race of Offender) is U=Unknown then Data Element 35 
			//(Relationship of Victim to Offender) must be RU=Relationship Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("00");
			copy.getOffenders().get(0).setSex("U");
			copy.getOffenders().get(0).setRace("U");
			incidents.add(copy);
			
			
			return incidents;
			
		});		
	}	
}
