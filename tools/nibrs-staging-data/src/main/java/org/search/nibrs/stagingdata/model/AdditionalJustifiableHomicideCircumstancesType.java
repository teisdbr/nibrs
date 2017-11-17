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
public class AdditionalJustifiableHomicideCircumstancesType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer additionalJustifiableHomicideCircumstancesTypeId; 
	private String additionalJustifiableHomicideCircumstancesCode; 
	private String additionalJustifiableHomicideCircumstancesDescription; 
	
	public AdditionalJustifiableHomicideCircumstancesType() {
		super();
	}

	public AdditionalJustifiableHomicideCircumstancesType(Integer additionalJustifiableHomicideCircumstancesTypeId) {
		super();
		this.additionalJustifiableHomicideCircumstancesTypeId = additionalJustifiableHomicideCircumstancesTypeId;
	}

	public AdditionalJustifiableHomicideCircumstancesType(Integer additionalJustifiableHomicideCircumstancesTypeId,
			String additionalJustifiableHomicideCircumstancesCode,
			String additionalJustifiableHomicideCircumstancesDescription) {
		super();
		this.additionalJustifiableHomicideCircumstancesTypeId = additionalJustifiableHomicideCircumstancesTypeId;
		this.additionalJustifiableHomicideCircumstancesCode = additionalJustifiableHomicideCircumstancesCode;
		this.additionalJustifiableHomicideCircumstancesDescription = additionalJustifiableHomicideCircumstancesDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getAdditionalJustifiableHomicideCircumstancesTypeId() {
		return additionalJustifiableHomicideCircumstancesTypeId;
	}

	public void setAdditionalJustifiableHomicideCircumstancesTypeId(
			Integer additionalJustifiableHomicideCircumstancesTypeId) {
		this.additionalJustifiableHomicideCircumstancesTypeId = additionalJustifiableHomicideCircumstancesTypeId;
	}

	public String getAdditionalJustifiableHomicideCircumstancesCode() {
		return additionalJustifiableHomicideCircumstancesCode;
	}

	public void setAdditionalJustifiableHomicideCircumstancesCode(
			String additionalJustifiableHomicideCircumstancesCode) {
		this.additionalJustifiableHomicideCircumstancesCode = additionalJustifiableHomicideCircumstancesCode;
	}

	public String getAdditionalJustifiableHomicideCircumstancesDescription() {
		return additionalJustifiableHomicideCircumstancesDescription;
	}

	public void setAdditionalJustifiableHomicideCircumstancesDescription(
			String additionalJustifiableHomicideCircumstancesDescription) {
		this.additionalJustifiableHomicideCircumstancesDescription = additionalJustifiableHomicideCircumstancesDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalJustifiableHomicideCircumstancesCode == null) ? 0
				: additionalJustifiableHomicideCircumstancesCode.hashCode());
		result = prime * result + ((additionalJustifiableHomicideCircumstancesDescription == null) ? 0
				: additionalJustifiableHomicideCircumstancesDescription.hashCode());
		result = prime * result + ((additionalJustifiableHomicideCircumstancesTypeId == null) ? 0
				: additionalJustifiableHomicideCircumstancesTypeId.hashCode());
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
		AdditionalJustifiableHomicideCircumstancesType other = (AdditionalJustifiableHomicideCircumstancesType) obj;
		if (additionalJustifiableHomicideCircumstancesCode == null) {
			if (other.additionalJustifiableHomicideCircumstancesCode != null)
				return false;
		} else if (!additionalJustifiableHomicideCircumstancesCode
				.equals(other.additionalJustifiableHomicideCircumstancesCode))
			return false;
		if (additionalJustifiableHomicideCircumstancesDescription == null) {
			if (other.additionalJustifiableHomicideCircumstancesDescription != null)
				return false;
		} else if (!additionalJustifiableHomicideCircumstancesDescription
				.equals(other.additionalJustifiableHomicideCircumstancesDescription))
			return false;
		if (additionalJustifiableHomicideCircumstancesTypeId == null) {
			if (other.additionalJustifiableHomicideCircumstancesTypeId != null)
				return false;
		} else if (!additionalJustifiableHomicideCircumstancesTypeId
				.equals(other.additionalJustifiableHomicideCircumstancesTypeId))
			return false;
		return true;
	}
}
