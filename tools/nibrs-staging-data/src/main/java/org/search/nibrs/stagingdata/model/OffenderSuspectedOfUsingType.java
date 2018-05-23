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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable
public class OffenderSuspectedOfUsingType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer offenderSuspectedOfUsingTypeId; 
	
	private String stateCode; 
	private String stateDescription; 
	private String fbiCode; 
	private String fbiDescription; 
	
	public OffenderSuspectedOfUsingType(Integer offenderSuspectedOfUsingTypeId, String stateCode,
			String stateDescription, String fbiCode, String fbiDescription) {
		super();
		this.offenderSuspectedOfUsingTypeId = offenderSuspectedOfUsingTypeId;
		this.stateCode = stateCode;
		this.stateDescription = stateDescription;
		this.fbiCode = fbiCode;
		this.fbiDescription = fbiDescription;
	}

	@ManyToMany(mappedBy = "offenderSuspectedOfUsingTypes", fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<OffenseSegment> offenseSegments;
	
	public OffenderSuspectedOfUsingType() {
		super();
		offenseSegments = new HashSet<>();
	}

	public OffenderSuspectedOfUsingType(Integer offenderSuspectedOfUsingTypeId) {
		super();
		this.offenderSuspectedOfUsingTypeId = offenderSuspectedOfUsingTypeId;
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "offenseSegments");		
	}

	public Integer getOffenderSuspectedOfUsingTypeId() {
		return offenderSuspectedOfUsingTypeId;
	}

	public void setOffenderSuspectedOfUsingTypeId(Integer offenderSuspectedOfUsingTypeId) {
		this.offenderSuspectedOfUsingTypeId = offenderSuspectedOfUsingTypeId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	public String getFbiCode() {
		return fbiCode;
	}

	public void setFbiCode(String fbiCode) {
		this.fbiCode = fbiCode;
	}

	public String getFbiDescription() {
		return fbiDescription;
	}

	public void setFbiDescription(String fbiDescription) {
		this.fbiDescription = fbiDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fbiCode == null) ? 0 : fbiCode.hashCode());
		result = prime * result + ((fbiDescription == null) ? 0 : fbiDescription.hashCode());
		result = prime * result
				+ ((offenderSuspectedOfUsingTypeId == null) ? 0 : offenderSuspectedOfUsingTypeId.hashCode());
		result = prime * result + ((stateCode == null) ? 0 : stateCode.hashCode());
		result = prime * result + ((stateDescription == null) ? 0 : stateDescription.hashCode());
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
		OffenderSuspectedOfUsingType other = (OffenderSuspectedOfUsingType) obj;
		if (fbiCode == null) {
			if (other.fbiCode != null)
				return false;
		} else if (!fbiCode.equals(other.fbiCode))
			return false;
		if (fbiDescription == null) {
			if (other.fbiDescription != null)
				return false;
		} else if (!fbiDescription.equals(other.fbiDescription))
			return false;
		if (offenderSuspectedOfUsingTypeId == null) {
			if (other.offenderSuspectedOfUsingTypeId != null)
				return false;
		} else if (!offenderSuspectedOfUsingTypeId.equals(other.offenderSuspectedOfUsingTypeId))
			return false;
		if (stateCode == null) {
			if (other.stateCode != null)
				return false;
		} else if (!stateCode.equals(other.stateCode))
			return false;
		if (stateDescription == null) {
			if (other.stateDescription != null)
				return false;
		} else if (!stateDescription.equals(other.stateDescription))
			return false;
		return true;
	}

}
