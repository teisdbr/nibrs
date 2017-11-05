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
public class SexOfPersonType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer sexOfPersonTypeId; 
	
	private String sexOfPersonCode; 
	private String sexOfPersonDescription; 
	
	public SexOfPersonType() {
		super();
	}

	public SexOfPersonType(Integer sexOfPersonTypeId) {
		super();
		this.sexOfPersonTypeId = sexOfPersonTypeId;
	}

	public SexOfPersonType(Integer sexOfPersonTypeId, String sexOfPersonCode, String sexOfPersonDescription) {
		super();
		setSexOfPersonTypeId(sexOfPersonTypeId);
		setSexOfPersonCode(sexOfPersonCode);
		setSexOfPersonDescription(sexOfPersonDescription);
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}


	public String getSexOfPersonCode() {
		return sexOfPersonCode;
	}


	public void setSexOfPersonCode(String sexOfPersonCode) {
		this.sexOfPersonCode = sexOfPersonCode;
	}


	public String getSexOfPersonDescription() {
		return sexOfPersonDescription;
	}


	public void setSexOfPersonDescription(String sexOfPersonDescription) {
		this.sexOfPersonDescription = sexOfPersonDescription;
	}

	public Integer getSexOfPersonTypeId() {
		return sexOfPersonTypeId;
	}

	public void setSexOfPersonTypeId(Integer sexOfPersonTypeId) {
		this.sexOfPersonTypeId = sexOfPersonTypeId;
	}

}
