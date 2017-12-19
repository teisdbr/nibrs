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
public class VictimOffenderRelationshipType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer victimOffenderRelationshipTypeId; 
	private String victimOffenderRelationshipCode; 
	private String victimOffenderRelationshipDescription; 
	
	public VictimOffenderRelationshipType() {
		super();
	}

	public VictimOffenderRelationshipType(Integer victimOffenderRelationshipTypeId) {
		super();
		this.victimOffenderRelationshipTypeId = victimOffenderRelationshipTypeId;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getVictimOffenderRelationshipTypeId() {
		return victimOffenderRelationshipTypeId;
	}

	public void setVictimOffenderRelationshipTypeId(Integer victimOffenderRelationshipTypeId) {
		this.victimOffenderRelationshipTypeId = victimOffenderRelationshipTypeId;
	}

	public String getVictimOffenderRelationshipCode() {
		return victimOffenderRelationshipCode;
	}

	public void setVictimOffenderRelationshipCode(String victimOffenderRelationshipCode) {
		this.victimOffenderRelationshipCode = victimOffenderRelationshipCode;
	}

	public String getVictimOffenderRelationshipDescription() {
		return victimOffenderRelationshipDescription;
	}

	public void setVictimOffenderRelationshipDescription(String victimOffenderRelationshipDescription) {
		this.victimOffenderRelationshipDescription = victimOffenderRelationshipDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((victimOffenderRelationshipCode == null) ? 0 : victimOffenderRelationshipCode.hashCode());
		result = prime * result + ((victimOffenderRelationshipDescription == null) ? 0
				: victimOffenderRelationshipDescription.hashCode());
		result = prime * result
				+ ((victimOffenderRelationshipTypeId == null) ? 0 : victimOffenderRelationshipTypeId.hashCode());
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
		VictimOffenderRelationshipType other = (VictimOffenderRelationshipType) obj;
		if (victimOffenderRelationshipCode == null) {
			if (other.victimOffenderRelationshipCode != null)
				return false;
		} else if (!victimOffenderRelationshipCode.equals(other.victimOffenderRelationshipCode))
			return false;
		if (victimOffenderRelationshipDescription == null) {
			if (other.victimOffenderRelationshipDescription != null)
				return false;
		} else if (!victimOffenderRelationshipDescription.equals(other.victimOffenderRelationshipDescription))
			return false;
		if (victimOffenderRelationshipTypeId == null) {
			if (other.victimOffenderRelationshipTypeId != null)
				return false;
		} else if (!victimOffenderRelationshipTypeId.equals(other.victimOffenderRelationshipTypeId))
			return false;
		return true;
	}

}
