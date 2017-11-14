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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BiasMotivationType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer biasMotivationTypeId; 
	
	private String biasMotivationCode; 
	private String biasMotivationDescription; 
	private String biasMotivationCategory;
	
	@ManyToMany(mappedBy = "biasMotivationTypes", fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<OffenseSegment> offenseSegments;

	public BiasMotivationType() {
		super();
	}

	public BiasMotivationType(Integer biasMotivationTypeId) {
		super();
		this.biasMotivationTypeId = biasMotivationTypeId;
	}

	public BiasMotivationType(Integer biasMotivationTypeId, String biasMotivationCode, String biasMotivationDescription,
			String biasMotivationCategory) {
		super();
		this.biasMotivationTypeId = biasMotivationTypeId;
		this.biasMotivationCode = biasMotivationCode;
		this.biasMotivationDescription = biasMotivationDescription;
		this.biasMotivationCategory = biasMotivationCategory;
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "offenseSegments");		
	}

	public Integer getBiasMotivationTypeId() {
		return biasMotivationTypeId;
	}

	public void setBiasMotivationTypeId(Integer biasMotivationTypeId) {
		this.biasMotivationTypeId = biasMotivationTypeId;
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
		result = prime * result + ((biasMotivationTypeId == null) ? 0 : biasMotivationTypeId.hashCode());
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
		if (biasMotivationTypeId == null) {
			if (other.biasMotivationTypeId != null)
				return false;
		} else if (!biasMotivationTypeId.equals(other.biasMotivationTypeId))
			return false;
		return true;
	}

}
