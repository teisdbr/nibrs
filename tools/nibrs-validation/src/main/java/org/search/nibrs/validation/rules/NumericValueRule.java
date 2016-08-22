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
