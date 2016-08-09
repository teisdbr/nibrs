package org.search.nibrs.validation;

import java.util.List;

import org.search.nibrs.common.NIBRSError;

/**
 * Should be implemented by classes interested in handling validation events
 */
public abstract class ValidationListener {
	
	/**
	 * Notifies implementer to the availability of NEW validation error(s) 
	 * 
	 * @param nibrsErrorList
	 * 		Contains NEW validation errors.  Does not contain errors that were 
	 * 		found in previous validationAvailable executions 
	 */
	public abstract void validationAvailable(List<NIBRSError> nibrsErrorList);

}
