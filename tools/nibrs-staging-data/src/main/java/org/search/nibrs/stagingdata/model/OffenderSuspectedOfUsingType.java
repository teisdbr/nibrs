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
public class OffenderSuspectedOfUsingType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer offenderSuspectedOfUsingTypeId; 
	
	private String offenderSuspectedOfUsingCode; 
	private String offenderSuspectedOfUsingDescription; 
	
	@ManyToMany(mappedBy = "offenderSuspectedOfUsingTypes", fetch=FetchType.LAZY)
	private Set<OffenseSegment> offenseSegments;
	
	public OffenderSuspectedOfUsingType() {
		super();
		offenseSegments = new HashSet<>();
	}

	public OffenderSuspectedOfUsingType(Integer offenderSuspectedOfUsingTypeId, String offenderSuspectedOfUsingCode,
			String offenderSuspectedOfUsingDescription) {
		super();
		this.offenderSuspectedOfUsingTypeId = offenderSuspectedOfUsingTypeId;
		this.offenderSuspectedOfUsingCode = offenderSuspectedOfUsingCode;
		this.offenderSuspectedOfUsingDescription = offenderSuspectedOfUsingDescription;
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

	public String getOffenderSuspectedOfUsingCode() {
		return offenderSuspectedOfUsingCode;
	}

	public void setOffenderSuspectedOfUsingCode(String offenderSuspectedOfUsingCode) {
		this.offenderSuspectedOfUsingCode = offenderSuspectedOfUsingCode;
	}

	public String getOffenderSuspectedOfUsingDescription() {
		return offenderSuspectedOfUsingDescription;
	}

	public void setOffenderSuspectedOfUsingDescription(String offenderSuspectedOfUsingDescription) {
		this.offenderSuspectedOfUsingDescription = offenderSuspectedOfUsingDescription;
	}

	public Set<OffenseSegment> getOffenseSegments() {
		return offenseSegments;
	}

	public void setOffenseSegments(Set<OffenseSegment> offenseSegments) {
		this.offenseSegments = offenseSegments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((offenderSuspectedOfUsingCode == null) ? 0 : offenderSuspectedOfUsingCode.hashCode());
		result = prime * result
				+ ((offenderSuspectedOfUsingDescription == null) ? 0 : offenderSuspectedOfUsingDescription.hashCode());
		result = prime * result
				+ ((offenderSuspectedOfUsingTypeId == null) ? 0 : offenderSuspectedOfUsingTypeId.hashCode());
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
		if (offenderSuspectedOfUsingCode == null) {
			if (other.offenderSuspectedOfUsingCode != null)
				return false;
		} else if (!offenderSuspectedOfUsingCode.equals(other.offenderSuspectedOfUsingCode))
			return false;
		if (offenderSuspectedOfUsingDescription == null) {
			if (other.offenderSuspectedOfUsingDescription != null)
				return false;
		} else if (!offenderSuspectedOfUsingDescription.equals(other.offenderSuspectedOfUsingDescription))
			return false;
		if (offenderSuspectedOfUsingTypeId == null) {
			if (other.offenderSuspectedOfUsingTypeId != null)
				return false;
		} else if (!offenderSuspectedOfUsingTypeId.equals(other.offenderSuspectedOfUsingTypeId))
			return false;
		return true;
	}

}
