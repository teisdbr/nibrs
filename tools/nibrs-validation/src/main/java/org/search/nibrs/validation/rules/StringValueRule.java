package org.search.nibrs.validation.rules;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

/**
 * A rule implementation suitable for testing the value of a string produced as a function of the subject.  Typically, the string will be a string
 * property of the subject (but need not be).
 *
 * @param <T> The class of subjects to which this rule applies
 */
public class StringValueRule<T extends ValidationTarget> implements Rule<T> {
	
	private Function<T, String> stringProvider;
	private BiFunction<String, ValidationTarget, NIBRSError> test;

	/**
	 * Create an instance of the rule
	 * @param stringProvider lambda function that takes a subject and returns a string
	 * @param test lambda function that takes a string and validation target and returns a NIBRSError (or null if the string passes the test)
	 */
	public StringValueRule(Function<T, String> stringProvider, BiFunction<String, ValidationTarget, NIBRSError> test) {
		this.stringProvider = stringProvider;
		this.test = test;
	}

	@Override
	public NIBRSError apply(T subject) {
		String valueToTest = stringProvider.apply(subject);
		NIBRSError error = test.apply(valueToTest, subject);
		if (error != null) {
			error.setValue(valueToTest);
		}
		return error;
	}

}
