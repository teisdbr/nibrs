package org.search.nibrs.validation.rules;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * Abstract base class for rules that obtain values to test from a named bean property.
 *
 * @param <T> The class of subjects to which this rule applies
 */
public abstract class AbstractBeanPropertyRule<T extends ValidationTarget> implements Rule<T> {
	
	protected PropertyDescriptor property;
	protected String dataElementIdentifier;
	protected NIBRSErrorCode errorCode;
	
	protected AbstractBeanPropertyRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(subjectClass);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (propertyName.equals(pd.getName())) {
					property = pd;
					break;
				}
			}
			if (property == null) {
				throw new RuntimeException("No property named " + propertyName + " found on subject class " + subjectClass);
			}
		} catch (IntrospectionException e) {
			// this really should never happen...
			throw new RuntimeException(e);
		}
		this.dataElementIdentifier = dataElementIdentifier;
		this.errorCode = errorCode;
	}

	@Override
	public final NIBRSError apply(T subject) {
		NIBRSError ret = null;
		try {
			Object value = property.getReadMethod().invoke(subject, new Object[0]);
			if (propertyViolatesRule(value)) {
				ret = subject.getErrorTemplate();
				ret.setValue(value);
				ret.setDataElementIdentifier(dataElementIdentifier);
				ret.setNibrsErrorCode(errorCode);
			}
		} catch (ReflectiveOperationException e) {
			// this really should never happen...
			throw new RuntimeException(e);
		}
		return ret;
	}
	
	/**
	 * Templated method that derived types should implement to perform the test on the property value.
	 * @param value the bean property value
	 * @return whether the value passes the test or not
	 */
	protected abstract boolean propertyViolatesRule(Object value);

}
