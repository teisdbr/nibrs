/*******************************************************************************
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
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
package org.search.nibrs.common;

/**
 * A class whose objects wrap primitives and provide, for null values, whether the null was a consequence of a missing value in original input, or an invalid value.
 *
 * @param <T> the specific type of Object to which an instance applies
 */
public class ParsedObject<T> {
	
	private T value;
	private boolean missing;
	private boolean invalid;
	private NIBRSError validationError;
	
	public ParsedObject() {
		this.missing = true;
	}
	
	public ParsedObject(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	
	public boolean isMissing() {
		return missing;
	}
	public void setMissing(boolean missing) {
		this.missing = missing;
	}
	
	public boolean isInvalid() {
		return invalid;
	}
	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
	
	public NIBRSError getValidationError() {
		return validationError;
	}
	public void setValidationError(NIBRSError validationError) {
		this.validationError = validationError;
	}
	
	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (invalid ? 1231 : 1237);
		result = prime * result + (missing ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((validationError == null) ? 0 : validationError.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean ret = false;
		
		if (obj instanceof ParsedObject && obj != null) {
			
			ParsedObject<?> compNumber = (ParsedObject<?>) obj;
			
			if (value != null) {
				ret = value.equals(compNumber.value);
			} else {
				ret = missing == compNumber.missing && invalid == compNumber.invalid;
				if (validationError != null) {
					ret &= validationError.equals(compNumber.validationError);
				}
			}
			
		}
		
		return ret;
		
	}
	
	/**
	 * Factory method for getting a missing parsed object.
	 * @return the object
	 */
	public static final <T> ParsedObject<T> getMissingParsedObject() {
		ParsedObject<T> po = new ParsedObject<>();
		po.setValue(null);
		po.setMissing(true);
		po.setInvalid(false);
		return po;
	}
	
	/**
	 * Factory method for getting a missing parsed object.
	 * @return the object
	 */
	public static final <T> ParsedObject<T> getInvalidParsedObject() {
		ParsedObject<T> po = new ParsedObject<>();
		po.setValue(null);
		po.setMissing(false);
		po.setInvalid(true);
		return po;
	}
	
}
