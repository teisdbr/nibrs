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
public class OfficerActivityCircumstanceType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer officerActivityCircumstanceTypeId; 
	private String officerActivityCircumstanceCode; 
	private String officerActivityCircumstanceDescription; 
	
	public OfficerActivityCircumstanceType() {
		super();
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

}
