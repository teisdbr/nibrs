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
			
		groupATweakerMap.put(550,incident -> {
			//(Age of Offender Data Element 38 (Sex of Offender), and Data Element 39 
			//(Race of Offender) cannot be entered when Data Element 36 
			//(Offender Sequence Number) is 00=Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(00);
									
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
		
		
	}	
}
