package org.search.nibrs.validation;

import java.util.HashMap;
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
	
	private Map<Integer, Function<Incident, Incident>> tweakerMap;
	
	private RuleViolationExemplarFactory() {
		tweakerMap = new HashMap<Integer, Function<Incident,Incident>>();
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
	public Incident getIncidentThatViolatesRule(Integer ruleNumber) {
		return tweakerMap.get(ruleNumber).apply(BaselineIncidentFactory.getBaselineIncident());
	}
	
	private void populateExemplarMap() {

		tweakerMap.put(115, incident -> {
			// do whatever you need to do in here to "tweak" the incident so that it violates the rule
			// Rule 115 says that there cannot be embedded blanks within an Incident Number
			incident.setIncidentNumber("1234 5678");
			return incident;
		});
		
	}

}
