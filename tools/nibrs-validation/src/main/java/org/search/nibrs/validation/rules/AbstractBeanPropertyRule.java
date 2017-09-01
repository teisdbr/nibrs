/*
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.validation.rules;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * Abstract base class for rules that obtain values to test from a named bean property.
 *
 * @param <T> The class of subjects to which this rule applies
 */
public abstract class AbstractBeanPropertyRule<T extends ValidationTarget> implements Rule<T> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AbstractBeanPropertyRule.class);
	
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
		if (!ignore(subject)) {
			try {
				Object value = property.getReadMethod().invoke(subject, new Object[0]);
				if (propertyViolatesRule(value, subject)) {
					ret = subject.getErrorTemplate();
					ret.setNIBRSErrorCode(errorCode);
					ret.setValue(value);
					ret.setDataElementIdentifier(dataElementIdentifier);
				}
			} catch (ReflectiveOperationException e) {
				// this really should never happen...
				throw new RuntimeException(e);
			}
		}
		return ret;
	}
	
	/**
	 * Templated method that derived types can override to determine if the rule should be ignored for a particular incident.
	 */
	protected boolean ignore(T subject) {
		if (subject instanceof GroupBArrestReport){
			if (((GroupBArrestReport)subject).getReportActionType() == 'D'){
				return true; 
			}
		}
		
		if (subject instanceof ArresteeSegment){
			if (((ArresteeSegment) subject).getReportActionType() == 'D'){
				return true; 
			}
		}
		return false;
	}
	
	/**
	 * Templated method that derived types should implement to perform the test on the property value.
	 * @param value the bean property value
	 * @param subject the validation target object (implementers can use this for additional context needed to implement the logic in the method)
	 * @return whether the value passes the test or not
	 */
	protected abstract boolean propertyViolatesRule(Object value, T subject);

}
