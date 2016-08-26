package org.search.nibrs.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupAIncidentReport;

/**
 * Class that manages a set of "edits" to baseline incidents. These edits create "exemplars" of NIBRS rules violations that can be used to unit test the validation logic in the precert tool.
 *
 */
public final class RuleViolationExemplarFactory {

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
		groupATweakerMap.putAll(AdministrativeRuleViolationExemplarFactory.getInstance().getGroupATweakerMap());
		groupATweakerMap.putAll(OffenseRuleViolationExemplarFactory.getInstance().getGroupATweakerMap());
		groupATweakerMap.putAll(PropertyRuleViolationExemplarFactory.getInstance().getGroupATweakerMap());
		groupATweakerMap.putAll(VictimRuleViolationExemplarFactory.getInstance().getGroupATweakerMap());
		groupATweakerMap.putAll(OffenderRuleViolationExemplarFactory.getInstance().getGroupATweakerMap());
		groupATweakerMap.putAll(ArresteeRuleViolationExemplarFactory.getInstance().getGroupATweakerMap());
	}
	
}
