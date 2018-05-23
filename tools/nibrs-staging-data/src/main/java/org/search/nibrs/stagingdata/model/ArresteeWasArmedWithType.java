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

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Entity
@Cacheable
public class ArresteeWasArmedWithType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer arresteeWasArmedWithTypeId; 
	
	private String stateCode; 
	private String stateDescription; 
	private String fbiCode; 
	private String fbiDescription; 
	
	@OneToMany(mappedBy = "arresteeWasArmedWithType")
	private Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWith;
	
	@OneToMany(mappedBy = "arresteeWasArmedWithType")
	private Set<ArrestReportSegmentWasArmedWith> arrestReportSegmentWasArmedWith;
	
	public ArresteeWasArmedWithType() {
		super();
	}

	public ArresteeWasArmedWithType(Integer arresteeWasArmedWithTypeId) {
		super();
		this.arresteeWasArmedWithTypeId = arresteeWasArmedWithTypeId;
	}

	public ArresteeWasArmedWithType(Integer arresteeWasArmedWithTypeId, String stateCode, String stateDescription,
			String fbiCode, String fbiDescription, Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWith,
			Set<ArrestReportSegmentWasArmedWith> arrestReportSegmentWasArmedWith) {
		super();
		this.arresteeWasArmedWithTypeId = arresteeWasArmedWithTypeId;
		this.stateCode = stateCode;
		this.stateDescription = stateDescription;
		this.fbiCode = fbiCode;
		this.fbiDescription = fbiDescription;
		this.arresteeSegmentWasArmedWith = arresteeSegmentWasArmedWith;
		this.arrestReportSegmentWasArmedWith = arrestReportSegmentWasArmedWith;
	}

	public String toString(){
		return ReflectionToStringBuilder.toStringExclude(this, "arresteeSegmentWasArmedWith", "arrestReportSegmentWasArmedWith");
	}

	public Integer getArresteeWasArmedWithTypeId() {
		return arresteeWasArmedWithTypeId;
	}

	public void setArresteeWasArmedWithTypeId(Integer arresteeWasArmedWithTypeId) {
		this.arresteeWasArmedWithTypeId = arresteeWasArmedWithTypeId;
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
		result = prime * result + ((arresteeWasArmedWithTypeId == null) ? 0 : arresteeWasArmedWithTypeId.hashCode());
		result = prime * result + ((fbiCode == null) ? 0 : fbiCode.hashCode());
		result = prime * result + ((fbiDescription == null) ? 0 : fbiDescription.hashCode());
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
		ArresteeWasArmedWithType other = (ArresteeWasArmedWithType) obj;
		if (arresteeWasArmedWithTypeId == null) {
			if (other.arresteeWasArmedWithTypeId != null)
				return false;
		} else if (!arresteeWasArmedWithTypeId.equals(other.arresteeWasArmedWithTypeId))
			return false;
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
