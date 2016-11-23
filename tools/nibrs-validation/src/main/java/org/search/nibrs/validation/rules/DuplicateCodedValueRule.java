/*******************************************************************************
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
