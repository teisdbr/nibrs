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

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * A Rule implementation that tests whether an array-bound property has multiple values when one is an exclusive value.
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class ExclusiveCodedValueRule<T extends ValidationTarget> extends AbstractBeanPropertyRule<T> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ExclusiveCodedValueRule.class);
	
	private Set<String> exclusiveValueSet;

	public ExclusiveCodedValueRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode, Set<String> exclusiveValueSet) {
		super(propertyName, dataElementIdentifier, subjectClass, errorCode);
		this.exclusiveValueSet = exclusiveValueSet;
	}

	/**
	 * In NIBRS, it is common for the value "N" to be disallowed with any other values for an array-bound property.  This constructor builds a rule that covers this case.
	 */
	public ExclusiveCodedValueRule(String propertyName, String dataElementIdentifier, Class<T> subjectClass, NIBRSErrorCode errorCode) {
		this(propertyName, dataElementIdentifier, subjectClass, errorCode, new HashSet<String>());
		exclusiveValueSet.add("N");
	}

	@Override
	protected boolean propertyViolatesRule(Object value, T subject) {
		boolean ret = false;
		if (value != null) {
			if (!value.getClass().isArray()) {
				throw new IllegalStateException(getClass().getName() + " must operate on an array bean property");
			}
			Object[] array = (Object[]) value;
			boolean exclusiveValueSeen = false;
			boolean multipleElements = false;
			for (Object o : array) {
				if (exclusiveValueSet.contains(o)) {
					exclusiveValueSeen = true;
					if (multipleElements) {
						ret = true;
						break;
					}
				} else if (o != null) {
					multipleElements = true;
					if (exclusiveValueSeen) {
						ret = true;
						break;
					}
				}
			}
		}
		return ret;
	}

}
