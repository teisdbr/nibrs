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
public class ResidentStatusOfPersonType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer residentStatusOfPersonTypeId; 
	
	private String residentStatusOfPersonCode; 
	private String residentStatusOfPersonDescription; 
	
	public ResidentStatusOfPersonType() {
		super();
	}

	public ResidentStatusOfPersonType(Integer residentStatusOfPersonTypeID, String residentStatusOfPersonCode,
			String residentStatusOfPersonDescription) {
		super();
		this.residentStatusOfPersonTypeId = residentStatusOfPersonTypeID;
		this.residentStatusOfPersonCode = residentStatusOfPersonCode;
		this.residentStatusOfPersonDescription = residentStatusOfPersonDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getResidentStatusOfPersonCode() {
		return residentStatusOfPersonCode;
	}

	public void setResidentStatusOfPersonCode(String residentStatusOfPersonCode) {
		this.residentStatusOfPersonCode = residentStatusOfPersonCode;
	}

	public String getResidentStatusOfPersonDescription() {
		return residentStatusOfPersonDescription;
	}

	public void setResidentStatusOfPersonDescription(String residentStatusOfPersonDescription) {
		this.residentStatusOfPersonDescription = residentStatusOfPersonDescription;
	}

	public Integer getResidentStatusOfPersonTypeId() {
		return residentStatusOfPersonTypeId;
	}

	public void setResidentStatusOfPersonTypeId(Integer residentStatusOfPersonTypeID) {
		this.residentStatusOfPersonTypeId = residentStatusOfPersonTypeID;
	}


}
