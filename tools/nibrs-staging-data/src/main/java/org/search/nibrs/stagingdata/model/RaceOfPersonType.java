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

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Cacheable
public class RaceOfPersonType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer raceOfPersonTypeId; 
	
	private String raceOfPersonCode; 
	private String raceOfPersonDescription; 
	
	public RaceOfPersonType() {
		super();
	}

	public RaceOfPersonType(Integer raceOfPersonTypeId) {
		super();
		this.raceOfPersonTypeId = raceOfPersonTypeId;
	}

	public RaceOfPersonType(Integer raceOfPersonTypeId, String raceOfPersonCode, String raceOfPersonDescription) {
		super();
		this.setRaceOfPersonTypeId(raceOfPersonTypeId);
		setRaceOfPersonCode(raceOfPersonCode);
		setRaceOfPersonDescription(raceOfPersonDescription);
	}


	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}


	public String getRaceOfPersonCode() {
		return raceOfPersonCode;
	}


	public void setRaceOfPersonCode(String raceOfPersonCode) {
		this.raceOfPersonCode = raceOfPersonCode;
	}


	public String getRaceOfPersonDescription() {
		return raceOfPersonDescription;
	}


	public void setRaceOfPersonDescription(String raceOfPersonDescription) {
		this.raceOfPersonDescription = raceOfPersonDescription;
	}

	public Integer getRaceOfPersonTypeId() {
		return raceOfPersonTypeId;
	}

	public void setRaceOfPersonTypeId(Integer raceOfPersonTypeId) {
		this.raceOfPersonTypeId = raceOfPersonTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((raceOfPersonCode == null) ? 0 : raceOfPersonCode.hashCode());
		result = prime * result + ((raceOfPersonDescription == null) ? 0 : raceOfPersonDescription.hashCode());
		result = prime * result + ((raceOfPersonTypeId == null) ? 0 : raceOfPersonTypeId.hashCode());
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
		RaceOfPersonType other = (RaceOfPersonType) obj;
		if (raceOfPersonCode == null) {
			if (other.raceOfPersonCode != null)
				return false;
		} else if (!raceOfPersonCode.equals(other.raceOfPersonCode))
			return false;
		if (raceOfPersonDescription == null) {
			if (other.raceOfPersonDescription != null)
				return false;
		} else if (!raceOfPersonDescription.equals(other.raceOfPersonDescription))
			return false;
		if (raceOfPersonTypeId == null) {
			if (other.raceOfPersonTypeId != null)
				return false;
		} else if (!raceOfPersonTypeId.equals(other.raceOfPersonTypeId))
			return false;
		return true;
	}

}
