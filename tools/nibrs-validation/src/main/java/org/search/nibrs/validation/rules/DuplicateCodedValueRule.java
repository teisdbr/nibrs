package org.search.nibrs.validation.rules;

import java.util.HashSet;
import java.util.Set;

import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * A rule implementation that tests whether an array-bound bean property has duplicate values.
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class DuplicateCodedValueRule<T extends ValidationTarget> extends AbstractBeanPropertyRule<T> {

	public DuplicateCodedValueRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode) {
		super(propertyName, dataElementIdentifier, subjectClass, errorCode);
	}

	@Override
	protected boolean propertyViolatesRule(Object value, T subject) {
		if (value == null) {
			return false;
		}
		if (!value.getClass().isArray()) {
			throw new IllegalStateException(getClass().getName() + " must operate on an array bean property");
		}
		Object[] array = (Object[]) value;
		Set<Object> valueSet = new HashSet<>();
		boolean ret = false;
		for (Object o : array) {
			if (o != null) {
				if (valueSet.contains(o)) {
					ret = true;
					break;
				}
				valueSet.add(o);
			}
		}
		return ret;
	}

}
