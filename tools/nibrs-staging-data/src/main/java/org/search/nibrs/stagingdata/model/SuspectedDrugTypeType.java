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

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable
public class SuspectedDrugTypeType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer suspectedDrugTypeTypeId; 
	
	private String suspectedDrugTypeCode; 
	private String suspectedDrugTypeDescription; 
	
	@OneToMany(mappedBy = "suspectedDrugTypeType")
	@JsonIgnore
	private Set<SuspectedDrugType> suspectedDrugTypes;

	
	public SuspectedDrugTypeType() {
		super();
	}

	public SuspectedDrugTypeType(Integer suspectedDrugTypeTypeId) {
		super();
		this.suspectedDrugTypeTypeId = suspectedDrugTypeTypeId;
	}

	public SuspectedDrugTypeType(Integer suspectedDrugTypeTypeId, String suspectedDrugTypeCode,
			String suspectedDrugTypeDescription) {
		super();
		this.suspectedDrugTypeTypeId = suspectedDrugTypeTypeId;
		this.suspectedDrugTypeCode = suspectedDrugTypeCode;
		this.suspectedDrugTypeDescription = suspectedDrugTypeDescription;
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "suspectedDrugTypes");		
	}

	public String getSuspectedDrugTypeCode() {
		return suspectedDrugTypeCode;
	}

	public void setSuspectedDrugTypeCode(String suspectedDrugTypeCode) {
		this.suspectedDrugTypeCode = suspectedDrugTypeCode;
	}

	public Integer getSuspectedDrugTypeTypeId() {
		return suspectedDrugTypeTypeId;
	}

	public void setSuspectedDrugTypeTypeId(Integer suspectedDrugTypeTypeId) {
		this.suspectedDrugTypeTypeId = suspectedDrugTypeTypeId;
	}

	public String getSuspectedDrugTypeDescription() {
		return suspectedDrugTypeDescription;
	}

	public void setSuspectedDrugTypeDescription(String suspectedDrugTypeDescription) {
		this.suspectedDrugTypeDescription = suspectedDrugTypeDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suspectedDrugTypeCode == null) ? 0 : suspectedDrugTypeCode.hashCode());
		result = prime * result
				+ ((suspectedDrugTypeDescription == null) ? 0 : suspectedDrugTypeDescription.hashCode());
		result = prime * result + ((suspectedDrugTypeTypeId == null) ? 0 : suspectedDrugTypeTypeId.hashCode());
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
		SuspectedDrugTypeType other = (SuspectedDrugTypeType) obj;
		if (suspectedDrugTypeCode == null) {
			if (other.suspectedDrugTypeCode != null)
				return false;
		} else if (!suspectedDrugTypeCode.equals(other.suspectedDrugTypeCode))
			return false;
		if (suspectedDrugTypeDescription == null) {
			if (other.suspectedDrugTypeDescription != null)
				return false;
		} else if (!suspectedDrugTypeDescription.equals(other.suspectedDrugTypeDescription))
			return false;
		if (suspectedDrugTypeTypeId == null) {
			if (other.suspectedDrugTypeTypeId != null)
				return false;
		} else if (!suspectedDrugTypeTypeId.equals(other.suspectedDrugTypeTypeId))
			return false;
		return true;
	}

	public Set<SuspectedDrugType> getSuspectedDrugTypes() {
		return suspectedDrugTypes;
	}

	public void setSuspectedDrugTypes(Set<SuspectedDrugType> suspectedDrugTypes) {
		this.suspectedDrugTypes = suspectedDrugTypes;
	}

}
