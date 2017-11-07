/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
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
package org.search.nibrs.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interface for objects that are subject to validation.  The purpose of this interface is to allow such objects to provide
 * a generic set of information to validators without necessarily exposing their entire interface.
 */
public interface ValidationTarget {
	
	/**
	 * Get an error object suitable for reporting errors on this target, with as many properties populated as possible
	 * @return A template (partially-populated) error object that the validator can finish populating and then use to report errors
	 */
	@JsonIgnore
	public NIBRSError getErrorTemplate();

}
