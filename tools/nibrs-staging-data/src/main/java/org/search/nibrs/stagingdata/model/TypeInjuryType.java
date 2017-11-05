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
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.VictimSegment;

@Entity
public class TypeInjuryType {
	public TypeInjuryType(Integer typeInjuryTypeId) {
		super();
		this.typeInjuryTypeId = typeInjuryTypeId;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typeInjuryTypeId; 
	private String typeInjuryCode; 
	private String typeInjuryDescription; 
	
	@ManyToMany(mappedBy = "typeInjuryTypes", fetch=FetchType.LAZY)
	private Set<VictimSegment> victimSegments;
	
	public TypeInjuryType() {
		super();
	}

	public TypeInjuryType(Integer typeInjuryTypeId, String typeInjuryCode, String typeInjuryDescription) {
		super();
		this.typeInjuryTypeId = typeInjuryTypeId;
		this.typeInjuryCode = typeInjuryCode;
		this.typeInjuryDescription = typeInjuryDescription;
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "victimSegments");		
	}

	public String getTypeInjuryCode() {
		return typeInjuryCode;
	}

	public void setTypeInjuryCode(String typeInjuryCode) {
		this.typeInjuryCode = typeInjuryCode;
	}

	public String getTypeInjuryDescription() {
		return typeInjuryDescription;
	}

	public void setTypeInjuryDescription(String typeInjuryDescription) {
		this.typeInjuryDescription = typeInjuryDescription;
	}

	public Set<VictimSegment> getVictimSegments() {
		return victimSegments;
	}

	public void setVictimSegments(Set<VictimSegment> victimSegments) {
		this.victimSegments = victimSegments;
	}

	public Integer getTypeInjuryTypeId() {
		return typeInjuryTypeId;
	}

	public void setTypeInjuryTypeId(Integer typeInjuryTypeId) {
		this.typeInjuryTypeId = typeInjuryTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeInjuryCode == null) ? 0 : typeInjuryCode.hashCode());
		result = prime * result + ((typeInjuryDescription == null) ? 0 : typeInjuryDescription.hashCode());
		result = prime * result + ((typeInjuryTypeId == null) ? 0 : typeInjuryTypeId.hashCode());
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
		TypeInjuryType other = (TypeInjuryType) obj;
		if (typeInjuryCode == null) {
			if (other.typeInjuryCode != null)
				return false;
		} else if (!typeInjuryCode.equals(other.typeInjuryCode))
			return false;
		if (typeInjuryDescription == null) {
			if (other.typeInjuryDescription != null)
				return false;
		} else if (!typeInjuryDescription.equals(other.typeInjuryDescription))
			return false;
		if (typeInjuryTypeId == null) {
			if (other.typeInjuryTypeId != null)
				return false;
		} else if (!typeInjuryTypeId.equals(other.typeInjuryTypeId))
			return false;
		return true;
	}
}
