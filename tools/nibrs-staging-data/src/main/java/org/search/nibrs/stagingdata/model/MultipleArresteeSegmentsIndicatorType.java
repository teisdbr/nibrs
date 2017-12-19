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
package org.search.nibrs.stagingdata.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Cacheable
public class MultipleArresteeSegmentsIndicatorType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer multipleArresteeSegmentsIndicatorTypeId; 
	
	private String multipleArresteeSegmentsIndicatorCode; 
	private String multipleArresteeSegmentsIndicatorDescription; 
	
	public MultipleArresteeSegmentsIndicatorType() {
		super();
	}

	public MultipleArresteeSegmentsIndicatorType(Integer multipleArresteeSegmentsIndicatorTypeId) {
		super();
		this.multipleArresteeSegmentsIndicatorTypeId = multipleArresteeSegmentsIndicatorTypeId;
	}

	public MultipleArresteeSegmentsIndicatorType(Integer multipleArresteeSegmentsIndicatorTypeId,
			String multipleArresteeSegmentsIndicatorCode, String multipleArresteeSegmentsIndicatorDescription) {
		super();
		this.multipleArresteeSegmentsIndicatorTypeId = multipleArresteeSegmentsIndicatorTypeId;
		this.multipleArresteeSegmentsIndicatorCode = multipleArresteeSegmentsIndicatorCode;
		this.multipleArresteeSegmentsIndicatorDescription = multipleArresteeSegmentsIndicatorDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getMultipleArresteeSegmentsIndicatorTypeId() {
		return multipleArresteeSegmentsIndicatorTypeId;
	}

	public void setMultipleArresteeSegmentsIndicatorTypeId(Integer multipleArresteeSegmentsIndicatorTypeId) {
		this.multipleArresteeSegmentsIndicatorTypeId = multipleArresteeSegmentsIndicatorTypeId;
	}

	public String getMultipleArresteeSegmentsIndicatorCode() {
		return multipleArresteeSegmentsIndicatorCode;
	}

	public void setMultipleArresteeSegmentsIndicatorCode(String multipleArresteeSegmentsIndicatorCode) {
		this.multipleArresteeSegmentsIndicatorCode = multipleArresteeSegmentsIndicatorCode;
	}

	public String getMultipleArresteeSegmentsIndicatorDescription() {
		return multipleArresteeSegmentsIndicatorDescription;
	}

	public void setMultipleArresteeSegmentsIndicatorDescription(String multipleArresteeSegmentsIndicatorDescription) {
		this.multipleArresteeSegmentsIndicatorDescription = multipleArresteeSegmentsIndicatorDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((multipleArresteeSegmentsIndicatorCode == null) ? 0
				: multipleArresteeSegmentsIndicatorCode.hashCode());
		result = prime * result + ((multipleArresteeSegmentsIndicatorDescription == null) ? 0
				: multipleArresteeSegmentsIndicatorDescription.hashCode());
		result = prime * result + ((multipleArresteeSegmentsIndicatorTypeId == null) ? 0
				: multipleArresteeSegmentsIndicatorTypeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultipleArresteeSegmentsIndicatorType other = (MultipleArresteeSegmentsIndicatorType) obj;
		if (multipleArresteeSegmentsIndicatorCode == null) {
			if (other.multipleArresteeSegmentsIndicatorCode != null)
				return false;
		} else if (!multipleArresteeSegmentsIndicatorCode.equals(other.multipleArresteeSegmentsIndicatorCode))
			return false;
		if (multipleArresteeSegmentsIndicatorDescription == null) {
			if (other.multipleArresteeSegmentsIndicatorDescription != null)
				return false;
		} else if (!multipleArresteeSegmentsIndicatorDescription
				.equals(other.multipleArresteeSegmentsIndicatorDescription))
			return false;
		if (multipleArresteeSegmentsIndicatorTypeId == null) {
			if (other.multipleArresteeSegmentsIndicatorTypeId != null)
				return false;
		} else if (!multipleArresteeSegmentsIndicatorTypeId.equals(other.multipleArresteeSegmentsIndicatorTypeId))
			return false;
		return true;
	}

}
