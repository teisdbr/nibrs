package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.search.nibrs.model.Incident;

/**
 * Class that manages a set of "edits" to baseline incidents.  These edits create "exemplars" of NIBRS rules violations that can be used to
 * unit test the validation logic in the precert tool.
 *
 */
public class RuleViolationExemplarFactory {
	
	private static final RuleViolationExemplarFactory INSTANCE = new RuleViolationExemplarFactory();
	
	private Map<Integer, Function<Incident, List<Incident>>> tweakerMap;
	
	private RuleViolationExemplarFactory() {
		tweakerMap = new HashMap<Integer, Function<Incident,List<Incident>>>();
		populateExemplarMap();
	}
	
	/**
	 * Get an instance of the factory.
	 * @return the instance
	 */
	public static final RuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Get an Incident that violates the specified rule.  For rules and their numbers, reference the NIBRS Technical Specification, Section 5.
	 * @param ruleNumber the rule number
	 * @return an incident that exemplifies violation of the rule
	 */
	public List<Incident> getIncidentsThatViolateRule(Integer ruleNumber) {
		return tweakerMap.get(ruleNumber).apply(BaselineIncidentFactory.getBaselineIncident());
	}
	
	private void populateExemplarMap() {

		tweakerMap.put(101, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 1 must be populated with a valid data length and cannot be blank.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setOri(null);
			Incident copy2 = copy.deepCopy();
			copy2.setOri("WA1234");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		tweakerMap.put(201, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 2 must be populated with a valid data length and cannot be blank.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setOri(null);
			Incident copy2 = copy.deepCopy();
			copy2.setOri("WA1234");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		tweakerMap.put(204, incident -> {
			/*The referenced data element in a Group A Incident Report must 
			be populated with a valid data value and cannot be blank.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setOri(null);
			Incident copy2 = copy.deepCopy();
			copy2.setOri("WA1234");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		
		tweakerMap.put(301, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 3 must be populated with a valid data length and cannot be blank.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setOri(null);
			Incident copy2 = copy.deepCopy();
			copy2.setOri("WA1234");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		tweakerMap.put(401, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 3 must be populated with a valid data length and cannot be blank.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setOri(null);
			Incident copy2 = copy.deepCopy();
			copy2.setOri("WA1234");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		tweakerMap.put(501, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 3 must be populated with a valid data length and cannot be blank.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setOri(null);
			Incident copy2 = copy.deepCopy();
			copy2.setOri("WA1234");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		tweakerMap.put(601, incident -> {
			/*The referenced data element in a Group A Incident Report
			Segment 3 must be populated with a valid data length and cannot be blank.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setOri(null);
			Incident copy2 = copy.deepCopy();
			copy2.setOri("WA1234");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
			tweakerMap.put(59, incident -> {
			/*First two positions must be the code of the state (e.g., SC, MD) in which the incident occurred.
			 For nonfederal participants, every record must have the same code.
			*/
			Incident ret = incident.deepCopy();
			ret.setOri("ZZ123456789");
			return Collections.singletonList(ret);
		});
		
		tweakerMap.put(105, incident -> {
			/*The data element in error contains a date that is not entered correctly.
			Each component of the date must be valid; that is, months must be 01 through 12,
			days must be 01 through 31, and year must include the century (i.e., 19xx, 20xx).
			In addition, days cannot exceed maximum for the month (e.g., June cannot have 31days).
			Also, the date cannot exceed the current date.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setYearOfTape(0120);
			copy.setMonthOfTape(5);
			Incident copy2 = copy.deepCopy();
			copy.setYearOfTape(2016);
			copy.setMonthOfTape(13);
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
		
		tweakerMap.put(115, incident -> {
			/*(Incident Number) Must be blank right-fill if under 12 characters in length. 
			Cannot have embedded blanks between the first and last characters entered.
			*/
			Incident ret = incident.deepCopy();
			ret.setIncidentNumber("1234 5678");
			return Collections.singletonList(ret);
		});
		
		tweakerMap.put(116, incident -> {
			/*(Incident Number) must be left-justified with blank right-fill.
			Since the number is less than 12 characters, it must begin in position 1.
			 */
			Incident ret = incident.deepCopy();
			ret.setIncidentNumber(" 12345678");
			return Collections.singletonList(ret);
		});
		
		tweakerMap.put(117, incident -> {
			/*(Incident Number) can only have character combinations of A through Z, 0 through 9,
			hyphens, and/or blanks. For example, 89-123-SC is valid, but 89+123*SC is invalid.
			 */
			Incident ret = incident.deepCopy();
			ret.setIncidentNumber("89+123*SC");
			return Collections.singletonList(ret);
		});
		
		tweakerMap.put(119, incident -> {
			/*Data Element 2A (Cargo Theft) must be populated with a valid data value when 
			Data Element 6 (UCR Offense Code) contains a Cargo Theft-related offense.
			*/
			List<Incident> incidents = new ArrayList<Incident>();
			Incident copy = incident.deepCopy();
			copy.setCargoTheftIndicator(true);
			Incident copy2 = copy.deepCopy();
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			incidents.add(copy);
			incidents.add(copy2);
			return incidents;
		});
		
	}

}
