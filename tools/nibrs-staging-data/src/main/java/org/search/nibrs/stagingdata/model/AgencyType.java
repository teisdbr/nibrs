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
public class AgencyType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer agencyTypeId; 
	
	private String agencyTypeCode; 
	private String agencyTypeDescription; 
	
	public AgencyType() {
		super();
	}

	public AgencyType(Integer agencyTypeId, String agencyTypeCode, String agencyTypeDescription) {
		super();
		this.agencyTypeId = agencyTypeId;
		this.agencyTypeCode = agencyTypeCode;
		this.agencyTypeDescription = agencyTypeDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getAgencyTypeCode() {
		return agencyTypeCode;
	}

	public void setAgencyTypeCode(String agencyTypeCode) {
		this.agencyTypeCode = agencyTypeCode;
	}

	public String getAgencyTypeDescription() {
		return agencyTypeDescription;
	}

	public void setAgencyTypeDescription(String agencyTypeDescription) {
		this.agencyTypeDescription = agencyTypeDescription;
	}

	public Integer getAgencyTypeId() {
		return agencyTypeId;
	}

	public void setAgencyTypeId(Integer agencyTypeId) {
		this.agencyTypeId = agencyTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agencyTypeCode == null) ? 0 : agencyTypeCode.hashCode());
		result = prime * result + ((agencyTypeDescription == null) ? 0 : agencyTypeDescription.hashCode());
		result = prime * result + ((agencyTypeId == null) ? 0 : agencyTypeId.hashCode());
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
		AgencyType other = (AgencyType) obj;
		if (agencyTypeCode == null) {
			if (other.agencyTypeCode != null)
				return false;
		} else if (!agencyTypeCode.equals(other.agencyTypeCode))
			return false;
		if (agencyTypeDescription == null) {
			if (other.agencyTypeDescription != null)
				return false;
		} else if (!agencyTypeDescription.equals(other.agencyTypeDescription))
			return false;
		if (agencyTypeId == null) {
			if (other.agencyTypeId != null)
				return false;
		} else if (!agencyTypeId.equals(other.agencyTypeId))
			return false;
		return true;
	}

}
