package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.StringValueRule;

/**
 * Factory class that provides Rule implementations to validate the elements contained on the Group A report administrative segment.
 */
public class GroupAIncidentReportRulesFactory {
	
	private List<Rule<GroupAIncidentReport>> rulesList = new ArrayList<>();
	
	public GroupAIncidentReportRulesFactory() {
		rulesList.add(getRule115());
	}

	Rule<GroupAIncidentReport> getRule115() {
		Pattern p = getRule115Regex();
		Rule<GroupAIncidentReport> rule115 = new StringValueRule<>(
				subject -> {
					return subject.getIncidentNumber();
				},
				(value, reportSource) -> {
					NIBRSError ret = null;
					if (value == null || value.length() != 12 || !p.matcher(value).matches()) {
						ret = new NIBRSError();
						ret.setContext(reportSource);
						ret.setNibrsErrorCode(NIBRSErrorCode._115);
						ret.setSegmentType('1');
						ret.setValue(value);
					}
					return ret;
				});
		return rule115;
	}

	static Pattern getRule115Regex() {
		return Pattern.compile("^[^ ]+[ ]*$");
	}

	/**
	 * Get the list of rules for the administrative segment.
	 * @return the list of rules
	 */
	public List<Rule<GroupAIncidentReport>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}

}
