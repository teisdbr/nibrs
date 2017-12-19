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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable
public class TypeOfWeaponForceInvolvedType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typeOfWeaponForceInvolvedTypeId; 
	
	private String typeOfWeaponForceInvolvedCode; 
	private String typeOfWeaponForceInvolvedDescription; 
	
	@OneToMany(mappedBy = "typeOfWeaponForceInvolvedType")
	@JsonIgnore
	private Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds;
	
	public TypeOfWeaponForceInvolvedType() {
		super();
	}

	public TypeOfWeaponForceInvolvedType(Integer typeOfWeaponForceInvolvedTypeId, String typeOfWeaponForceInvolvedCode,
			String typeOfWeaponForceInvolvedDescription) {
		super();
		this.typeOfWeaponForceInvolvedTypeId = typeOfWeaponForceInvolvedTypeId;
		this.typeOfWeaponForceInvolvedCode = typeOfWeaponForceInvolvedCode;
		this.typeOfWeaponForceInvolvedDescription = typeOfWeaponForceInvolvedDescription;
	}

	public String toString(){
		return ReflectionToStringBuilder.toStringExclude(this, "typeOfWeaponForceInvolveds");
	}

	public Integer getTypeOfWeaponForceInvolvedTypeId() {
		return typeOfWeaponForceInvolvedTypeId;
	}

	public void setTypeOfWeaponForceInvolvedTypeId(Integer typeOfWeaponForceInvolvedTypeId) {
		this.typeOfWeaponForceInvolvedTypeId = typeOfWeaponForceInvolvedTypeId;
	}

	public String getTypeOfWeaponForceInvolvedCode() {
		return typeOfWeaponForceInvolvedCode;
	}

	public void setTypeOfWeaponForceInvolvedCode(String typeOfWeaponForceInvolvedCode) {
		this.typeOfWeaponForceInvolvedCode = typeOfWeaponForceInvolvedCode;
	}

	public String getTypeOfWeaponForceInvolvedDescription() {
		return typeOfWeaponForceInvolvedDescription;
	}

	public void setTypeOfWeaponForceInvolvedDescription(String typeOfWeaponForceInvolvedDescription) {
		this.typeOfWeaponForceInvolvedDescription = typeOfWeaponForceInvolvedDescription;
	}

	public Set<TypeOfWeaponForceInvolved> getTypeOfWeaponForceInvolveds() {
		return typeOfWeaponForceInvolveds;
	}

	public void setTypeOfWeaponForceInvolveds(Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds) {
		this.typeOfWeaponForceInvolveds = typeOfWeaponForceInvolveds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((typeOfWeaponForceInvolvedCode == null) ? 0 : typeOfWeaponForceInvolvedCode.hashCode());
		result = prime * result + ((typeOfWeaponForceInvolvedDescription == null) ? 0
				: typeOfWeaponForceInvolvedDescription.hashCode());
		result = prime * result
				+ ((typeOfWeaponForceInvolvedTypeId == null) ? 0 : typeOfWeaponForceInvolvedTypeId.hashCode());
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
		TypeOfWeaponForceInvolvedType other = (TypeOfWeaponForceInvolvedType) obj;
		if (typeOfWeaponForceInvolvedCode == null) {
			if (other.typeOfWeaponForceInvolvedCode != null)
				return false;
		} else if (!typeOfWeaponForceInvolvedCode.equals(other.typeOfWeaponForceInvolvedCode))
			return false;
		if (typeOfWeaponForceInvolvedDescription == null) {
			if (other.typeOfWeaponForceInvolvedDescription != null)
				return false;
		} else if (!typeOfWeaponForceInvolvedDescription.equals(other.typeOfWeaponForceInvolvedDescription))
			return false;
		if (typeOfWeaponForceInvolvedTypeId == null) {
			if (other.typeOfWeaponForceInvolvedTypeId != null)
				return false;
		} else if (!typeOfWeaponForceInvolvedTypeId.equals(other.typeOfWeaponForceInvolvedTypeId))
			return false;
		return true;
	}

}
