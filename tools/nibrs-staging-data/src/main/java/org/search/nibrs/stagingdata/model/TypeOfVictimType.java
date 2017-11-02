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
public class TypeOfVictimType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typeOfVictimTypeId; 
	
	private String typeOfVictimCode; 
	private String typeOfVictimDescription; 
	
	public TypeOfVictimType() {
		super();
	}

	public TypeOfVictimType(Integer typeOfVictimTypeId, String typeOfVictimCode, String typeOfVictimDescription) {
		super();
		this.typeOfVictimTypeId = typeOfVictimTypeId;
		this.typeOfVictimCode = typeOfVictimCode;
		this.typeOfVictimDescription = typeOfVictimDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getTypeOfVictimTypeId() {
		return typeOfVictimTypeId;
	}

	public void setTypeOfVictimTypeId(Integer typeOfVictimTypeId) {
		this.typeOfVictimTypeId = typeOfVictimTypeId;
	}

	public String getTypeOfVictimCode() {
		return typeOfVictimCode;
	}

	public void setTypeOfVictimCode(String typeOfVictimCode) {
		this.typeOfVictimCode = typeOfVictimCode;
	}

	public String getTypeOfVictimDescription() {
		return typeOfVictimDescription;
	}

	public void setTypeOfVictimDescription(String typeOfVictimDescription) {
		this.typeOfVictimDescription = typeOfVictimDescription;
	}

}