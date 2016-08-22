package org.search.nibrs.validation.rules;

import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * A rule implementation that tests whether a subject property is non-null. 
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class NotBlankRule<T extends ValidationTarget> extends AbstractBeanPropertyRule<T> {
	
	public NotBlankRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode) {
		super(propertyName, dataElementIdentifier, subjectClass, errorCode);
	}

	protected boolean propertyViolatesRule(Object value) {
		return isBlank(value);
	}
	
	private boolean isBlank(Object o) {
		return o == null || (o instanceof String && ((String) o).trim().length() == 0);
	}

}
