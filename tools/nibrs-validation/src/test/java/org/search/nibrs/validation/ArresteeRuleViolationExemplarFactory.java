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
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			
			
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
		
		groupATweakerMap.put(665, incident -> {
			//(Arrest Date) cannot be earlier than Data Element 3 (Incident Date/Hour). 
			//A person cannot be arrested before the incident occurred.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getArrestees().get(0).setArrestDate(Date.from(LocalDateTime.of(2016, 4, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
			
			
			incidents.add(copy);
			
			return incidents;
			
		});
	}
	
}
