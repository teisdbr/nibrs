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
public class OfficerActivityCircumstanceType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer officerActivityCircumstanceTypeId; 
	private String officerActivityCircumstanceCode; 
	private String officerActivityCircumstanceDescription; 
	
	public OfficerActivityCircumstanceType() {
		super();
	}

	public OfficerActivityCircumstanceType(Integer officerActivityCircumstanceTypeId) {
		super();
		this.officerActivityCircumstanceTypeId = officerActivityCircumstanceTypeId;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getOfficerActivityCircumstanceTypeId() {
		return officerActivityCircumstanceTypeId;
	}

	public void setOfficerActivityCircumstanceTypeId(Integer officerActivityCircumstanceTypeId) {
		this.officerActivityCircumstanceTypeId = officerActivityCircumstanceTypeId;
	}

	public String getOfficerActivityCircumstanceCode() {
		return officerActivityCircumstanceCode;
	}

	public void setOfficerActivityCircumstanceCode(String officerActivityCircumstanceCode) {
		this.officerActivityCircumstanceCode = officerActivityCircumstanceCode;
	}

	public String getOfficerActivityCircumstanceDescription() {
		return officerActivityCircumstanceDescription;
	}

	public void setOfficerActivityCircumstanceDescription(String officerActivityCircumstanceDescription) {
		this.officerActivityCircumstanceDescription = officerActivityCircumstanceDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((officerActivityCircumstanceCode == null) ? 0 : officerActivityCircumstanceCode.hashCode());
		result = prime * result + ((officerActivityCircumstanceDescription == null) ? 0
				: officerActivityCircumstanceDescription.hashCode());
		result = prime * result
				+ ((officerActivityCircumstanceTypeId == null) ? 0 : officerActivityCircumstanceTypeId.hashCode());
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
		OfficerActivityCircumstanceType other = (OfficerActivityCircumstanceType) obj;
		if (officerActivityCircumstanceCode == null) {
			if (other.officerActivityCircumstanceCode != null)
				return false;
		} else if (!officerActivityCircumstanceCode.equals(other.officerActivityCircumstanceCode))
			return false;
		if (officerActivityCircumstanceDescription == null) {
			if (other.officerActivityCircumstanceDescription != null)
				return false;
		} else if (!officerActivityCircumstanceDescription.equals(other.officerActivityCircumstanceDescription))
			return false;
		if (officerActivityCircumstanceTypeId == null) {
			if (other.officerActivityCircumstanceTypeId != null)
				return false;
		} else if (!officerActivityCircumstanceTypeId.equals(other.officerActivityCircumstanceTypeId))
			return false;
		return true;
	}

}
