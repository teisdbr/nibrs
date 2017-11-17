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
public class TypeOfArrestType{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typeOfArrestTypeId; 
	
	private String typeOfArrestCode; 
	private String typeOfArrestDescription; 
	
	public TypeOfArrestType() {
		super();
	}

	public TypeOfArrestType(Integer typeOfArrestTypeId) {
		super();
		this.setTypeOfArrestTypeId(typeOfArrestTypeId);
	}

	public TypeOfArrestType(Integer typeOfArrestTypeId, String typeOfArrestCode, String typeOfArrestDescription) {
		super();
		this.setTypeOfArrestTypeId(typeOfArrestTypeId);
		this.typeOfArrestCode = typeOfArrestCode;
		this.typeOfArrestDescription = typeOfArrestDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getTypeOfArrestCode() {
		return typeOfArrestCode;
	}

	public void setTypeOfArrestCode(String typeOfArrestCode) {
		this.typeOfArrestCode = typeOfArrestCode;
	}

	public String getTypeOfArrestDescription() {
		return typeOfArrestDescription;
	}

	public void setTypeOfArrestDescription(String typeOfArrestDescription) {
		this.typeOfArrestDescription = typeOfArrestDescription;
	}

	public Integer getTypeOfArrestTypeId() {
		return typeOfArrestTypeId;
	}

	public void setTypeOfArrestTypeId(Integer typeOfArrestTypeId) {
		this.typeOfArrestTypeId = typeOfArrestTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeOfArrestCode == null) ? 0 : typeOfArrestCode.hashCode());
		result = prime * result + ((typeOfArrestDescription == null) ? 0 : typeOfArrestDescription.hashCode());
		result = prime * result + ((typeOfArrestTypeId == null) ? 0 : typeOfArrestTypeId.hashCode());
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
		TypeOfArrestType other = (TypeOfArrestType) obj;
		if (typeOfArrestCode == null) {
			if (other.typeOfArrestCode != null)
				return false;
		} else if (!typeOfArrestCode.equals(other.typeOfArrestCode))
			return false;
		if (typeOfArrestDescription == null) {
			if (other.typeOfArrestDescription != null)
				return false;
		} else if (!typeOfArrestDescription.equals(other.typeOfArrestDescription))
			return false;
		if (typeOfArrestTypeId == null) {
			if (other.typeOfArrestTypeId != null)
				return false;
		} else if (!typeOfArrestTypeId.equals(other.typeOfArrestTypeId))
			return false;
		return true;
	}

}
