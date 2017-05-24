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

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
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
	protected boolean propertyViolatesRule(Object value, T subject) {
		boolean ret = false;
		
		char reportActionType = ' '; 
		if (subject instanceof GroupAIncidentReport){
			reportActionType =((GroupAIncidentReport)subject).getReportActionType() ; 
		}
		else if (subject instanceof GroupBArrestReport){
			reportActionType =((GroupBArrestReport)subject).getReportActionType() ;
		}
		else if (subject instanceof ArresteeSegment){
			reportActionType = ((ArresteeSegment) subject).getReportActionType();
		}
		
		if (reportActionType == 'D') return ret; 
		
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
