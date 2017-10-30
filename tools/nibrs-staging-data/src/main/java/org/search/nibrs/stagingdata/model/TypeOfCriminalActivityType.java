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

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;

@Entity
public class TypeOfCriminalActivityType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typeOfCriminalActivityTypeId; 
	
	private String typeOfCriminalActivityCode; 
	private String typeOfCriminalActivityDescription; 
	
	@ManyToMany(mappedBy = "typeOfCriminalActivityTypes", fetch=FetchType.LAZY)
	private Set<OffenseSegment> offenseSegments;
	
	public TypeOfCriminalActivityType() {
		super();
	}

	public TypeOfCriminalActivityType(Integer typeOfCriminalActivityTypeId, String typeOfCriminalActivityCode,
			String typeOfCriminalActivityDescription) {
		super();
		this.typeOfCriminalActivityTypeId = typeOfCriminalActivityTypeId;
		this.typeOfCriminalActivityCode = typeOfCriminalActivityCode;
		this.typeOfCriminalActivityDescription = typeOfCriminalActivityDescription;
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "offenseSegments");		
	}

	public Integer getTypeOfCriminalActivityTypeId() {
		return typeOfCriminalActivityTypeId;
	}

	public void setTypeOfCriminalActivityTypeId(Integer typeOfCriminalActivityTypeId) {
		this.typeOfCriminalActivityTypeId = typeOfCriminalActivityTypeId;
	}

	public String getTypeOfCriminalActivityCode() {
		return typeOfCriminalActivityCode;
	}

	public void setTypeOfCriminalActivityCode(String typeOfCriminalActivityCode) {
		this.typeOfCriminalActivityCode = typeOfCriminalActivityCode;
	}

	public String getTypeOfCriminalActivityDescription() {
		return typeOfCriminalActivityDescription;
	}

	public void setTypeOfCriminalActivityDescription(String typeOfCriminalActivityDescription) {
		this.typeOfCriminalActivityDescription = typeOfCriminalActivityDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeOfCriminalActivityCode == null) ? 0 : typeOfCriminalActivityCode.hashCode());
		result = prime * result
				+ ((typeOfCriminalActivityDescription == null) ? 0 : typeOfCriminalActivityDescription.hashCode());
		result = prime * result
				+ ((typeOfCriminalActivityTypeId == null) ? 0 : typeOfCriminalActivityTypeId.hashCode());
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
		TypeOfCriminalActivityType other = (TypeOfCriminalActivityType) obj;
		if (typeOfCriminalActivityCode == null) {
			if (other.typeOfCriminalActivityCode != null)
				return false;
		} else if (!typeOfCriminalActivityCode.equals(other.typeOfCriminalActivityCode))
			return false;
		if (typeOfCriminalActivityDescription == null) {
			if (other.typeOfCriminalActivityDescription != null)
				return false;
		} else if (!typeOfCriminalActivityDescription.equals(other.typeOfCriminalActivityDescription))
			return false;
		if (typeOfCriminalActivityTypeId == null) {
			if (other.typeOfCriminalActivityTypeId != null)
				return false;
		} else if (!typeOfCriminalActivityTypeId.equals(other.typeOfCriminalActivityTypeId))
			return false;
		return true;
	}

}
