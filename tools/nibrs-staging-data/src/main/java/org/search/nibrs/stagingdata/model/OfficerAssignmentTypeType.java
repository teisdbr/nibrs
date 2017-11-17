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
public class OfficerAssignmentTypeType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer officerAssignmentTypeTypeId; 
	private String officerAssignmentTypeCode; 
	private String officerAssignmentTypeDescription; 
	
	public OfficerAssignmentTypeType() {
		super();
	}

	public OfficerAssignmentTypeType(Integer officerAssignmentTypeTypeId) {
		super();
		this.officerAssignmentTypeTypeId = officerAssignmentTypeTypeId;
	}

	public OfficerAssignmentTypeType(Integer officerAssignmentTypeTypeId, String officerAssignmentTypeCode,
			String officerAssignmentTypeDescription) {
		super();
		this.officerAssignmentTypeTypeId = officerAssignmentTypeTypeId;
		this.officerAssignmentTypeCode = officerAssignmentTypeCode;
		this.officerAssignmentTypeDescription = officerAssignmentTypeDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getOfficerAssignmentTypeTypeId() {
		return officerAssignmentTypeTypeId;
	}

	public void setOfficerAssignmentTypeTypeId(Integer officerAssignmentTypeTypeId) {
		this.officerAssignmentTypeTypeId = officerAssignmentTypeTypeId;
	}

	public String getOfficerAssignmentTypeCode() {
		return officerAssignmentTypeCode;
	}

	public void setOfficerAssignmentTypeCode(String officerAssignmentTypeCode) {
		this.officerAssignmentTypeCode = officerAssignmentTypeCode;
	}

	public String getOfficerAssignmentTypeDescription() {
		return officerAssignmentTypeDescription;
	}

	public void setOfficerAssignmentTypeDescription(String officerAssignmentTypeDescription) {
		this.officerAssignmentTypeDescription = officerAssignmentTypeDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((officerAssignmentTypeCode == null) ? 0 : officerAssignmentTypeCode.hashCode());
		result = prime * result
				+ ((officerAssignmentTypeDescription == null) ? 0 : officerAssignmentTypeDescription.hashCode());
		result = prime * result + ((officerAssignmentTypeTypeId == null) ? 0 : officerAssignmentTypeTypeId.hashCode());
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
		OfficerAssignmentTypeType other = (OfficerAssignmentTypeType) obj;
		if (officerAssignmentTypeCode == null) {
			if (other.officerAssignmentTypeCode != null)
				return false;
		} else if (!officerAssignmentTypeCode.equals(other.officerAssignmentTypeCode))
			return false;
		if (officerAssignmentTypeDescription == null) {
			if (other.officerAssignmentTypeDescription != null)
				return false;
		} else if (!officerAssignmentTypeDescription.equals(other.officerAssignmentTypeDescription))
			return false;
		if (officerAssignmentTypeTypeId == null) {
			if (other.officerAssignmentTypeTypeId != null)
				return false;
		} else if (!officerAssignmentTypeTypeId.equals(other.officerAssignmentTypeTypeId))
			return false;
		return true;
	}

}
