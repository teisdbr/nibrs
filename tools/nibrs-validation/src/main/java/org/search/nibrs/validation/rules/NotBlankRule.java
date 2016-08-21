package org.search.nibrs.validation.rules;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

/**
 * A rule implementation that tests whether a subject property is non-null. 
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class NotBlankRule<T extends ValidationTarget> implements Rule<T> {
	
	private PropertyDescriptor property;
	
	public NotBlankRule(String propertyName, Class<T> subjectClass) {
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
	}

	@Override
	public NIBRSError apply(T subject) {
		NIBRSError ret = null;
		try {
			Object value = property.getReadMethod().invoke(subject, new Object[0]);
			 if (isBlank(value)) {
				 ret = subject.getErrorTemplate();
				 ret.setValue(value);
			 }
		} catch (ReflectiveOperationException e) {
			// this really should never happen...
			throw new RuntimeException(e);
		}
		return ret;
	}
	
	private boolean isBlank(Object o) {
		return o == null || (o instanceof String && ((String) o).trim().length() == 0);
	}

}
