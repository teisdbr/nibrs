package org.search.nibrs.validation.rules;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;

/**
 * Interface for classes of objects that validate a subject.
 *
 * @param <T> The class of subjects to which the rule applies
 */
public interface Rule<T> {
	
	/**
	 * Apply the rule to the subject object.
	 * @param subject the object under test/validation
	 * @param reportSource the context to be included in the returned error, if any
	 * @return the error object representing the result of validation (or null if the subject passes the test - that is, if it's valid according to the rule)
	 */
	public NIBRSError apply(T subject, ReportSource reportSource);

}
