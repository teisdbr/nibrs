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

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

/**
 * Interface for classes of objects that validate a subject.
 *
 * @param <T> The class of subjects to which the rule applies
 */
public interface Rule<T extends ValidationTarget> {
	
	/**
	 * Apply the rule to the subject object.
	 * @param subject the object under test/validation
	 * @return the error object representing the result of validation (or null if the subject passes the test - that is, if it's valid according to the rule)
	 */
	public NIBRSError apply(T subject);

}
