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

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.Identifiable;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * Abstract base class for rules that test an identifier (Incident Number or Arrest Transaction Number) against a regular expression.
 *
 * @param <T> the class to which this applies, which must be both an Identifiable and a ValidationTarget
 */
public abstract class AbstractIdentifierRegexRule<T extends Identifiable & ValidationTarget> extends StringValueRule<T> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AbstractIdentifierRegexRule.class);
	
	public AbstractIdentifierRegexRule(Pattern regexPattern, String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		super(subject -> {
			return subject.getIdentifier();
		}, (value, target) -> {
			NIBRSError ret = null;
			if (value != null && (value.length() > 12 || !regexPattern.matcher(value).matches())) {
				ret = target.getErrorTemplate();
				ret.setNIBRSErrorCode(nibrsErrorCode);
				ret.setDataElementIdentifier(dataElementIdentifier);
				ret.setValue(value);
			}
			return ret;
		});
	}

}
