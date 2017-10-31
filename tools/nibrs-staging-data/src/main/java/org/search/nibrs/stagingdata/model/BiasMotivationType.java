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
public class BiasMotivationType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer biasMotivationTypeID; 
	
	private String biasMotivationCode; 
	private String biasMotivationDescription; 
	private String biasMotivationCategory; 
	
	public BiasMotivationType() {
		super();
	}

	public BiasMotivationType(Integer biasMotivationTypeID, String biasMotivationCode, String biasMotivationDescription,
			String biasMotivationCategory) {
		super();
		this.biasMotivationTypeID = biasMotivationTypeID;
		this.biasMotivationCode = biasMotivationCode;
		this.biasMotivationDescription = biasMotivationDescription;
		this.biasMotivationCategory = biasMotivationCategory;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getBiasMotivationTypeID() {
		return biasMotivationTypeID;
	}

	public void setBiasMotivationTypeID(Integer biasMotivationTypeID) {
		this.biasMotivationTypeID = biasMotivationTypeID;
	}

	public String getBiasMotivationCode() {
		return biasMotivationCode;
	}

	public void setBiasMotivationCode(String biasMotivationCode) {
		this.biasMotivationCode = biasMotivationCode;
	}

	public String getBiasMotivationDescription() {
		return biasMotivationDescription;
	}

	public void setBiasMotivationDescription(String biasMotivationDescription) {
		this.biasMotivationDescription = biasMotivationDescription;
	}

	public String getBiasMotivationCategory() {
		return biasMotivationCategory;
	}

	public void setBiasMotivationCategory(String biasMotivationCategory) {
		this.biasMotivationCategory = biasMotivationCategory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((biasMotivationCategory == null) ? 0 : biasMotivationCategory.hashCode());
		result = prime * result + ((biasMotivationCode == null) ? 0 : biasMotivationCode.hashCode());
		result = prime * result + ((biasMotivationDescription == null) ? 0 : biasMotivationDescription.hashCode());
		result = prime * result + ((biasMotivationTypeID == null) ? 0 : biasMotivationTypeID.hashCode());
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
		BiasMotivationType other = (BiasMotivationType) obj;
		if (biasMotivationCategory == null) {
			if (other.biasMotivationCategory != null)
				return false;
		} else if (!biasMotivationCategory.equals(other.biasMotivationCategory))
			return false;
		if (biasMotivationCode == null) {
			if (other.biasMotivationCode != null)
				return false;
		} else if (!biasMotivationCode.equals(other.biasMotivationCode))
			return false;
		if (biasMotivationDescription == null) {
			if (other.biasMotivationDescription != null)
				return false;
		} else if (!biasMotivationDescription.equals(other.biasMotivationDescription))
			return false;
		if (biasMotivationTypeID == null) {
			if (other.biasMotivationTypeID != null)
				return false;
		} else if (!biasMotivationTypeID.equals(other.biasMotivationTypeID))
			return false;
		return true;
	}

}
