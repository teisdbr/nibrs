package org.search.nibrs.validation.rules;

import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * A rule implementation that tests whether an array-bound bean property is either null, or has elements that are all null
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class NotAllBlankRule<T extends ValidationTarget> extends AbstractBeanPropertyRule<T> {

	public NotAllBlankRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode) {
		super(propertyName, dataElementIdentifier, subjectClass, errorCode);
	}

	@Override
	protected boolean propertyViolatesRule(Object value, T subject) {
		boolean ret = true;
		if (value != null) {
			String[] s = (String[]) value;
			if (s.length > 0) {
				boolean allBlank = true;
				for (String ss : s) {
					if (ss != null) {
						allBlank = false;
						break;
					}
				}
				ret = allBlank;
			}
		}
		return ret;
	}

}
