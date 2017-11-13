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
package org.search.nibrs.stagingdata.model.segment;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SuspectedDrugType;
import org.search.nibrs.stagingdata.model.TypePropertyLossEtcType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class, 
	property = "propertySegmentId")
public class PropertySegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer propertySegmentId;
	
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeID") 
	private SegmentActionTypeType segmentActionType; 
	@ManyToOne
	@JoinColumn(name="administrativeSegmentId") 
	private AdministrativeSegment administrativeSegment; 
	@ManyToOne
	@JoinColumn(name="typePropertyLossEtcTypeId")
	private TypePropertyLossEtcType typePropertyLossEtcType;
	
	@OneToMany(mappedBy = "propertySegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<SuspectedDrugType> suspectedDrugTypes;

	@OneToMany(mappedBy = "propertySegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<PropertyType> propertyTypes;
	
	private Integer numberOfStolenMotorVehicles;
	private Integer numberOfRecoveredMotorVehicles;
	
	public PropertySegment() {
		super();
	}
	
	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		String resultWithoutOffenseSegment = ReflectionToStringBuilder.toStringExclude(this, "administrativeSegment");
		int index = StringUtils.indexOf(resultWithoutOffenseSegment, ",");
		
		StringBuilder sb = new StringBuilder(resultWithoutOffenseSegment);
		sb.insert(index + 1, "administrativeSegmentId=" + administrativeSegment.getAdministrativeSegmentId() + ",");
		return sb.toString();
	}
	public SegmentActionTypeType getSegmentActionType() {
		return segmentActionType;
	}
	public void setSegmentActionType(SegmentActionTypeType segmentActionType) {
		this.segmentActionType = segmentActionType;
	}
	public AdministrativeSegment getAdministrativeSegment() {
		return administrativeSegment;
	}
	public void setAdministrativeSegment(AdministrativeSegment administrativeSegment) {
		this.administrativeSegment = administrativeSegment;
	}
	public Integer getPropertySegmentId() {
		return propertySegmentId;
	}

	public void setPropertySegmentId(Integer propertySegmentId) {
		this.propertySegmentId = propertySegmentId;
	}

	public TypePropertyLossEtcType getTypePropertyLossEtcType() {
		return typePropertyLossEtcType;
	}

	public void setTypePropertyLossEtcType(TypePropertyLossEtcType typePropertyLossEtcType) {
		this.typePropertyLossEtcType = typePropertyLossEtcType;
	}

	public Integer getNumberOfStolenMotorVehicles() {
		return numberOfStolenMotorVehicles;
	}

	public void setNumberOfStolenMotorVehicles(Integer numberOfStolenMotorVehicles) {
		this.numberOfStolenMotorVehicles = numberOfStolenMotorVehicles;
	}

	public Integer getNumberOfRecoveredMotorVehicles() {
		return numberOfRecoveredMotorVehicles;
	}

	public void setNumberOfRecoveredMotorVehicles(Integer numberOfRecoveredMotorVehicles) {
		this.numberOfRecoveredMotorVehicles = numberOfRecoveredMotorVehicles;
	}

	public Set<SuspectedDrugType> getSuspectedDrugTypes() {
		return suspectedDrugTypes;
	}

	public void setSuspectedDrugTypes(Set<SuspectedDrugType> suspectedDrugTypes) {
		this.suspectedDrugTypes = suspectedDrugTypes;
	}

	public Set<PropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(Set<PropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}
}
