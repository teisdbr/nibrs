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
package org.search.nibrs.validation;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractPersonSegment;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.codes.EthnicityCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.RaceCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexCode;
import org.search.nibrs.validation.rules.AbstractBeanPropertyRule;
import org.search.nibrs.validation.rules.Rule;

public class PersonSegmentRulesFactory<T extends AbstractPersonSegment> {
	
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());
	
	private Class<T> clazz;
	
	public <S extends AbstractPersonSegment> PersonSegmentRulesFactory(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	private final class PersonValidValueRule<S extends T> extends AbstractBeanPropertyRule<T> {
		
		private Set<String> allowedValueSet;
		private boolean allowNull;

		public PersonValidValueRule(String propertyName, String dataElementIdentifier, NIBRSErrorCode errorCode, Set<String> allowedValueSet, boolean allowNull) {
			super(propertyName, dataElementIdentifier, clazz, errorCode);
			this.allowedValueSet = allowedValueSet;
			this.allowNull = allowNull;
		}

		@Override
		protected boolean propertyViolatesRule(Object value, AbstractPersonSegment subject) {
			return (subject.isPerson() && !subject.isUnknown() && ((!allowNull && value == null) || (value != null && !allowedValueSet.contains(value))));
		}

	}

	public Rule<T> getSexValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new PersonValidValueRule<T>("sex", dataElementIdentifier, nibrsErrorCode, SexCode.codeSet(), false);
	}

	public Rule<T> getRaceValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new PersonValidValueRule<T>("race", dataElementIdentifier, nibrsErrorCode, RaceCode.codeSet(), false);
	}

	public Rule<T> getResidentStatusValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode, boolean allowNull) {
		return new PersonValidValueRule<T>("residentStatus", dataElementIdentifier, nibrsErrorCode, ResidentStatusCode.codeSet(), allowNull);
	}

	public Rule<T> getEthnicityValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode, boolean allowNull) {
		return new PersonValidValueRule<T>("ethnicity", dataElementIdentifier, nibrsErrorCode, EthnicityCode.codeSet(), allowNull);
	}
	
	public Rule<T> getProperAgeRangeRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new Rule<T>() {
			@Override
			public NIBRSError apply(T segment) {
				NIBRSError e = null;
				NIBRSAge nibrsAge = segment.getAge();
				if (nibrsAge != null && nibrsAge.isAgeRange()) {

					Integer ageMin = nibrsAge.getAgeMin();
					Integer ageMax = nibrsAge.getAgeMax();

					if (ageMin > ageMax) {
						e = segment.getErrorTemplate();
						e.setDataElementIdentifier(dataElementIdentifier);
						e.setNIBRSErrorCode(nibrsErrorCode);
						e.setValue(nibrsAge);
					}
				}
				return e;
			}
		};
	}
	
	public Rule<T> getAgeRangeLengthRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new Rule<T>() {
			@Override
			public NIBRSError apply(T segment) {
				NIBRSError e = null;
				NIBRSAge nibrsAge = segment.getAge();
				if (nibrsAge != null && nibrsAge.getError() != null 
						 && nibrsAge.getNonNumericAge().length()>2) {
					e = new NIBRSError(nibrsAge.getError());
					e.setDataElementIdentifier(dataElementIdentifier);
					e.setNIBRSErrorCode(nibrsErrorCode);
					e.setContext(segment.getParentReport().getSource());
					e.setValue(nibrsAge.getError().getValue());
					if (segment.isPerson() && !segment.isUnknown()){
						e.setContext(segment.getParentReport().getSource());
						e.setReportUniqueIdentifier(segment.getParentReport().getIdentifier());
						e.setSegmentType(segment.getSegmentType());
						e.setReport(segment.getParentReport());
					}
				}
				return e;
			}
		};
	}
	
	public Rule<T> getNonZeroAgeRangeMinimumRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new Rule<T>() {
			@Override
			public NIBRSError apply(T segment) {
				NIBRSError e = null;
				NIBRSAge nibrsAge = segment.getAge();
				if (nibrsAge != null && nibrsAge.isAgeRange() && nibrsAge.getAgeMin() != null && nibrsAge.getAgeMin() == 0) {
					e = segment.getErrorTemplate();
					e.setDataElementIdentifier(dataElementIdentifier);
					e.setNIBRSErrorCode(nibrsErrorCode);
					e.setContext(segment.getParentReport().getSource());
					e.setValue(nibrsAge);
				}
				return e;
			}
		};
	}
	
	public Rule<T> getAgeValidRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return getAgeValidRule(dataElementIdentifier, nibrsErrorCode, true);
	}

	public Rule<T> getAgeValidRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode, boolean allowNull) {
		return new Rule<T>() {
			@Override
			public NIBRSError apply(T segment) {
				NIBRSError e = null;
				NIBRSAge nibrsAge = segment.getAge();
				if (nibrsAge != null) {
					e = nibrsAge.getError();
					if (e != null) {
						e = new NIBRSError(nibrsAge.getError());
						e.setDataElementIdentifier(dataElementIdentifier);
						e.setNIBRSErrorCode(nibrsErrorCode);
						e.setContext(segment.getParentReport().getSource());
						e.setValue(nibrsAge.getError().getValue());
						
						if (segment.isPerson() && !segment.isUnknown()){
							e.setContext(segment.getParentReport().getSource());
							e.setReportUniqueIdentifier(segment.getParentReport().getIdentifier());
							e.setWithinSegmentIdentifier(segment.getWithinSegmentIdentifier());
							e.setSegmentType(segment.getSegmentType());
							e.setReport(segment.getParentReport());
						}
					}
				} else if (!allowNull && segment.isPerson() && !segment.isUnknown()) {
					e = segment.getErrorTemplate();
					e.setDataElementIdentifier(dataElementIdentifier);
					e.setNIBRSErrorCode(nibrsErrorCode);
					e.setValue(nibrsAge);
				}
				return e;
			}
		};
	}
	
}
