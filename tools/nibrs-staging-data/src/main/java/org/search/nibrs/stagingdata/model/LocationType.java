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

@Entity(name="locationTypeType")
public class LocationType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer locationTypeTypeId; 
	
	private String locationTypeCode; 
	private String locationTypeDescription; 
	
	public LocationType() {
		super();
	}

	public LocationType(Integer locationTypeTypeId, String locationTypeCode, String locationTypeDescription) {
		super();
		this.locationTypeTypeId = locationTypeTypeId;
		this.locationTypeCode = locationTypeCode;
		this.locationTypeDescription = locationTypeDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getLocationTypeTypeId() {
		return locationTypeTypeId;
	}

	public void setLocationTypeTypeId(Integer locationTypeTypeId) {
		this.locationTypeTypeId = locationTypeTypeId;
	}

	public String getLocationTypeCode() {
		return locationTypeCode;
	}

	public void setLocationTypeCode(String locationTypeCode) {
		this.locationTypeCode = locationTypeCode;
	}

	public String getLocationTypeDescription() {
		return locationTypeDescription;
	}

	public void setLocationTypeDescription(String locationTypeDescription) {
		this.locationTypeDescription = locationTypeDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locationTypeCode == null) ? 0 : locationTypeCode.hashCode());
		result = prime * result + ((locationTypeDescription == null) ? 0 : locationTypeDescription.hashCode());
		result = prime * result + ((locationTypeTypeId == null) ? 0 : locationTypeTypeId.hashCode());
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
		LocationType other = (LocationType) obj;
		if (locationTypeCode == null) {
			if (other.locationTypeCode != null)
				return false;
		} else if (!locationTypeCode.equals(other.locationTypeCode))
			return false;
		if (locationTypeDescription == null) {
			if (other.locationTypeDescription != null)
				return false;
		} else if (!locationTypeDescription.equals(other.locationTypeDescription))
			return false;
		if (locationTypeTypeId == null) {
			if (other.locationTypeTypeId != null)
				return false;
		} else if (!locationTypeTypeId.equals(other.locationTypeTypeId))
			return false;
		return true;
	}

}
