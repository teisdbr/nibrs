package org.search.nibrs.validation.groupb;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.NumericValueRule;
import org.search.nibrs.validation.rules.Rule;

/**
 * Factory class that provides Rule implementations to validate the elements contained on the Group B report administrative segment.
 */
public class GroupBArrestReportRulesFactory {

	private List<Rule<GroupBArrestReport>> rulesList;

	public GroupBArrestReportRulesFactory() {
	
		rulesList = new ArrayList<Rule<GroupBArrestReport>>();
	}
	
	/**
	 * Get the list of rules for the GroupB Arrest Report segment factory.
	 * 
	 * @return the list of rules
	 */
	public List<Rule<GroupBArrestReport>> getRulesList() {
		rulesList.add(getRule101("ori", "1"));
		rulesList.add(getRule101("yearOfTape", "Year of Tape"));
		rulesList.add(getRule101("monthOfTape", "Month of Tape"));
		rulesList.add(getRule104("yearOfTape"));
		rulesList.add(getRule104("monthOfTape"));
		return rulesList;
	}
	
	Rule<GroupBArrestReport> getRule101(String propertyName, String dataElementIdentifier) {
		return new NotBlankRule<GroupBArrestReport>(propertyName, dataElementIdentifier, GroupBArrestReport.class, NIBRSErrorCode._701);
	}
	
	Rule<GroupBArrestReport> getRule104(String propertyName) {
		Rule<GroupBArrestReport> ret = null;
		if ("yearOfTape".equals(propertyName)) {
			ret = new NumericValueRule<>(
					subject -> {
						return subject.getYearOfTape();
					},
					(value, target) -> {
						NIBRSError e = null;
						if (value != null && 1991 > value.intValue()) {
							e = target.getErrorTemplate();
							e.setNIBRSErrorCode(NIBRSErrorCode._104);
							e.setDataElementIdentifier("Year of Tape");
							e.setValue(value);
						}
						return e;
					});
		} else if ("monthOfTape".equals(propertyName)) {
			ret = new NumericValueRule<>(
					subject -> {
						return subject.getMonthOfTape();
					},
					(value, target) -> {
						NIBRSError e = null;
						if (value != null && (1 > value.intValue() || 12 < value.intValue())) {
							e = target.getErrorTemplate();
							e.setNIBRSErrorCode(NIBRSErrorCode._104);
							e.setDataElementIdentifier("Month of Tape");
							e.setValue(value);
						}
						return e;
					});
		}
		return ret;
	}
	
}
