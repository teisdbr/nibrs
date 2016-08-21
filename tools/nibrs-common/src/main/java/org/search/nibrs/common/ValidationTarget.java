package org.search.nibrs.common;

/**
 * Interface for objects that are subject to validation.  The purpose of this interface is to allow such objects to provide
 * a generic set of information to validators without necessarily exposing their entire interface.
 */
public interface ValidationTarget {
	
	/**
	 * Get an error object suitable for reporting errors on this target, with as many properties populated as possible
	 * @return A template (partially-populated) error object that the validator can finish populating and then use to report errors
	 */
	public NIBRSError getErrorTemplate();

}
