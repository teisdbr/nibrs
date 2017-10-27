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
public class TypeDrugMeasurementType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typeDrugMeasurementTypeId; 
	
	private String typeDrugMeasurementCode; 
	private String typeDrugMeasurementDescription; 
	
	public TypeDrugMeasurementType() {
		super();
	}

	public TypeDrugMeasurementType(Integer typeDrugMeasurementTypeId, String typeDrugMeasurementCode,
			String typeDrugMeasurementDescription) {
		super();
		this.typeDrugMeasurementTypeId = typeDrugMeasurementTypeId;
		this.typeDrugMeasurementCode = typeDrugMeasurementCode;
		this.typeDrugMeasurementDescription = typeDrugMeasurementDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getTypeDrugMeasurementTypeId() {
		return typeDrugMeasurementTypeId;
	}

	public void setTypeDrugMeasurementTypeId(Integer typeDrugMeasurementTypeId) {
		this.typeDrugMeasurementTypeId = typeDrugMeasurementTypeId;
	}

	public String getTypeDrugMeasurementCode() {
		return typeDrugMeasurementCode;
	}

	public void setTypeDrugMeasurementCode(String typeDrugMeasurementCode) {
		this.typeDrugMeasurementCode = typeDrugMeasurementCode;
	}

	public String getTypeDrugMeasurementDescription() {
		return typeDrugMeasurementDescription;
	}

	public void setTypeDrugMeasurementDescription(String typeDrugMeasurementDescription) {
		this.typeDrugMeasurementDescription = typeDrugMeasurementDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeDrugMeasurementCode == null) ? 0 : typeDrugMeasurementCode.hashCode());
		result = prime * result
				+ ((typeDrugMeasurementDescription == null) ? 0 : typeDrugMeasurementDescription.hashCode());
		result = prime * result + ((typeDrugMeasurementTypeId == null) ? 0 : typeDrugMeasurementTypeId.hashCode());
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
		TypeDrugMeasurementType other = (TypeDrugMeasurementType) obj;
		if (typeDrugMeasurementCode == null) {
			if (other.typeDrugMeasurementCode != null)
				return false;
		} else if (!typeDrugMeasurementCode.equals(other.typeDrugMeasurementCode))
			return false;
		if (typeDrugMeasurementDescription == null) {
			if (other.typeDrugMeasurementDescription != null)
				return false;
		} else if (!typeDrugMeasurementDescription.equals(other.typeDrugMeasurementDescription))
			return false;
		if (typeDrugMeasurementTypeId == null) {
			if (other.typeDrugMeasurementTypeId != null)
				return false;
		} else if (!typeDrugMeasurementTypeId.equals(other.typeDrugMeasurementTypeId))
			return false;
		return true;
	}

}
