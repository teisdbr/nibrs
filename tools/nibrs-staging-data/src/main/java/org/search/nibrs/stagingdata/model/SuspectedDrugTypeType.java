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
	
	private String stateCode; 
	private String stateDescription; 
	private String fbiCode; 
	private String fbiDescription; 
	
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

	public SuspectedDrugTypeType(Integer suspectedDrugTypeTypeId, String stateCode, String stateDescription,
			String fbiCode, String fbiDescription) {
		super();
		this.suspectedDrugTypeTypeId = suspectedDrugTypeTypeId;
		this.stateCode = stateCode;
		this.stateDescription = stateDescription;
		this.fbiCode = fbiCode;
		this.fbiDescription = fbiDescription;
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "suspectedDrugTypes");		
	}

	public Integer getSuspectedDrugTypeTypeId() {
		return suspectedDrugTypeTypeId;
	}

	public void setSuspectedDrugTypeTypeId(Integer suspectedDrugTypeTypeId) {
		this.suspectedDrugTypeTypeId = suspectedDrugTypeTypeId;
	}

	public Set<SuspectedDrugType> getSuspectedDrugTypes() {
		return suspectedDrugTypes;
	}

	public void setSuspectedDrugTypes(Set<SuspectedDrugType> suspectedDrugTypes) {
		this.suspectedDrugTypes = suspectedDrugTypes;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	public String getFbiCode() {
		return fbiCode;
	}

	public void setFbiCode(String fbiCode) {
		this.fbiCode = fbiCode;
	}

	public String getFbiDescription() {
		return fbiDescription;
	}

	public void setFbiDescription(String fbiDescription) {
		this.fbiDescription = fbiDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fbiCode == null) ? 0 : fbiCode.hashCode());
		result = prime * result + ((fbiDescription == null) ? 0 : fbiDescription.hashCode());
		result = prime * result + ((stateCode == null) ? 0 : stateCode.hashCode());
		result = prime * result + ((stateDescription == null) ? 0 : stateDescription.hashCode());
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
		if (fbiCode == null) {
			if (other.fbiCode != null)
				return false;
		} else if (!fbiCode.equals(other.fbiCode))
			return false;
		if (fbiDescription == null) {
			if (other.fbiDescription != null)
				return false;
		} else if (!fbiDescription.equals(other.fbiDescription))
			return false;
		if (stateCode == null) {
			if (other.stateCode != null)
				return false;
		} else if (!stateCode.equals(other.stateCode))
			return false;
		if (stateDescription == null) {
			if (other.stateDescription != null)
				return false;
		} else if (!stateDescription.equals(other.stateDescription))
			return false;
		if (suspectedDrugTypeTypeId == null) {
			if (other.suspectedDrugTypeTypeId != null)
				return false;
		} else if (!suspectedDrugTypeTypeId.equals(other.suspectedDrugTypeTypeId))
			return false;
		return true;
	}

}
