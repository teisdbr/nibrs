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

		tweakerMap.put(115, incident -> {
			// do whatever you need to do in here to "tweak" the incident so that it violates the rule
			// Rule 115 says that there cannot be embedded blanks within an Incident Number
			Incident ret = incident.deepCopy();
			ret.setIncidentNumber("1234 5678");
			return Collections.singletonList(ret);
		});
		
		tweakerMap.put(117, incident -> {
			Incident ret = incident.deepCopy();
			ret.setIncidentNumber("89+123*SC");
			return Collections.singletonList(ret);
		});
		
		tweakerMap.put(119, incident -> {
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
