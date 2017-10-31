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

}
