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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class MethodOfEntryType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer methodOfEntryTypeId; 
	
	private String methodOfEntryCode; 
	private String methodOfEntryDescription; 
	
	public MethodOfEntryType() {
		super();
	}

	public MethodOfEntryType(Integer methodOfEntryTypeId, String methodOfEntryCode,
			String methodOfEntryDescription) {
		super();
		this.methodOfEntryTypeId = methodOfEntryTypeId;
		this.methodOfEntryCode = methodOfEntryCode;
		this.methodOfEntryDescription = methodOfEntryDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getMethodOfEntryTypeId() {
		return methodOfEntryTypeId;
	}

	public void setMethodOfEntryTypeId(Integer methodOfEntryTypeId) {
		this.methodOfEntryTypeId = methodOfEntryTypeId;
	}

	public String getMethodOfEntryCode() {
		return methodOfEntryCode;
	}

	public void setMethodOfEntryCode(String methodOfEntryCode) {
		this.methodOfEntryCode = methodOfEntryCode;
	}

	public String getMethodOfEntryDescription() {
		return methodOfEntryDescription;
	}

	public void setMethodOfEntryDescription(String methodOfEntryDescription) {
		this.methodOfEntryDescription = methodOfEntryDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodOfEntryCode == null) ? 0 : methodOfEntryCode.hashCode());
		result = prime * result + ((methodOfEntryDescription == null) ? 0 : methodOfEntryDescription.hashCode());
		result = prime * result + ((methodOfEntryTypeId == null) ? 0 : methodOfEntryTypeId.hashCode());
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
		MethodOfEntryType other = (MethodOfEntryType) obj;
		if (methodOfEntryCode == null) {
			if (other.methodOfEntryCode != null)
				return false;
		} else if (!methodOfEntryCode.equals(other.methodOfEntryCode))
			return false;
		if (methodOfEntryDescription == null) {
			if (other.methodOfEntryDescription != null)
				return false;
		} else if (!methodOfEntryDescription.equals(other.methodOfEntryDescription))
			return false;
		if (methodOfEntryTypeId == null) {
			if (other.methodOfEntryTypeId != null)
				return false;
		} else if (!methodOfEntryTypeId.equals(other.methodOfEntryTypeId))
			return false;
		return true;
	}

}
