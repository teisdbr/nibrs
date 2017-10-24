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
package org.search.nibrs.stagingdata.repository;

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

}
