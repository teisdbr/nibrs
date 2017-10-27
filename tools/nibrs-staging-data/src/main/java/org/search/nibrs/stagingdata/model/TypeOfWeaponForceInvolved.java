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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class TypeOfWeaponForceInvolved implements Serializable{
	private static final long serialVersionUID = 8597871552401839697L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typeOfWeaponForceInvolvedId; 
	
    @ManyToOne
    @JoinColumn(name = "offenseSegmentId")
	private OffenseSegment offenseSegment; 
    
    @ManyToOne
    @JoinColumn(name = "typeOfWeaponForceInvolvedTypeId")
	private TypeOfWeaponForceInvolvedType typeOfWeaponForceInvolvedType;
    
    @Column(name = "automaticWeaponIndicator")
	private String automaticWeaponIndicator; 
	
	public TypeOfWeaponForceInvolved() {
		super();
	}

	public String toString(){
		//TODO  need to add the offenseSegment's Id into the returned string. 
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "offenseSegment");		
	}
	public Integer getTypeOfWeaponForceInvolvedId() {
		return typeOfWeaponForceInvolvedId;
	}

	public void setTypeOfWeaponForceInvolvedId(Integer typeOfWeaponForceInvolvedId) {
		this.typeOfWeaponForceInvolvedId = typeOfWeaponForceInvolvedId;
	}

	public OffenseSegment getOffenseSegment() {
		return offenseSegment;
	}

	public void setOffenseSegment(OffenseSegment offenseSegment) {
		this.offenseSegment = offenseSegment;
	}

	public TypeOfWeaponForceInvolvedType getTypeOfWeaponForceInvolvedType() {
		return typeOfWeaponForceInvolvedType;
	}

	public void setTypeOfWeaponForceInvolvedType(TypeOfWeaponForceInvolvedType typeOfWeaponForceInvolvedType) {
		this.typeOfWeaponForceInvolvedType = typeOfWeaponForceInvolvedType;
	}

	public String getAutomaticWeaponIndicator() {
		return automaticWeaponIndicator;
	}

	public void setAutomaticWeaponIndicator(String automaticWeaponIndicator) {
		this.automaticWeaponIndicator = automaticWeaponIndicator;
	}

}
