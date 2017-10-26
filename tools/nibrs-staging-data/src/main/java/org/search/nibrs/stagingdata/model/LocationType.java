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

}
