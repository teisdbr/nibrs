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

import java.util.function.BiFunction;
import java.util.function.Function;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

/**
 * A rule implementation suitable for testing the value of a number produced as a function of the subject.  Typically, the number will be a numeric
 * property of the subject (but need not be).
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class NumericValueRule<T extends ValidationTarget> implements Rule<T> {
	
	private Function<T, Number> numberProvider;
	private BiFunction<Number, ValidationTarget, NIBRSError> test;

	/**
	 * Create an instance of the rule
	 * @param numberProvider lambda function that takes a subject and returns a number
	 * @param test lambda function that takes a number and validation target and returns a NIBRSError (or null if the string passes the test)
	 */
	public NumericValueRule(Function<T, Number> numberProvider, BiFunction<Number, ValidationTarget, NIBRSError> test) {
		this.numberProvider = numberProvider;
		this.test = test;
	}

	@Override
	public NIBRSError apply(T subject) {
		Number valueToTest = numberProvider.apply(subject);
		NIBRSError error = test.apply(valueToTest, subject);
		if (error != null) {
			error.setValue(valueToTest);
		}
		return error;
	}

}
