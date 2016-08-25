package org.search.nibrs.validation.rules;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * A rule implementation that tests a named property against a valid list of values.
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class ValidValueListRule<T extends ValidationTarget> extends AbstractBeanPropertyRule<T> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ValidValueListRule.class);
	
	private Set<String> allowedValueSet;
	private boolean nullAllowed;

	public ValidValueListRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode, Set<String> allowedValueSet, boolean nullAllowed) {
		super(propertyName, dataElementIdentifier, subjectClass, errorCode);
		this.allowedValueSet = allowedValueSet;
		this.nullAllowed = nullAllowed;
	}
	public ValidValueListRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode, Set<String> allowedValueSet) {
		this(propertyName, dataElementIdentifier, subjectClass,  errorCode, allowedValueSet, true);
	}

	@Override
	protected boolean propertyViolatesRule(Object value) {
		boolean ret = false;
		if (!nullAllowed && value == null) {
			ret = true;
		} else if (value != null) {
			if (value.getClass().isArray()) {
				boolean allNull = true;
				for (Object o : (Object[]) value) {
					if (o != null) {
						ret |= !allowedValueSet.contains(o);
						allNull = false;
					}
				}
				ret |= (!nullAllowed && allNull);
			} else {
				ret = !allowedValueSet.contains(value);
			}
		}
		return ret;
	}
	
}
