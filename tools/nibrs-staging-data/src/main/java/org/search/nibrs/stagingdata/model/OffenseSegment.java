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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class OffenseSegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer offenseSegmentId;
	
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeID") 
	private SegmentActionTypeType segmentActionType; 
	@ManyToOne
	@JoinColumn(name="administrativeSegmentId") 
	private AdministrativeSegment administrativeSegment; 
	@ManyToOne
	@JoinColumn(name="ucrOffenseCodeTypeId")
	private UcrOffenseCodeType ucrOffenseCodeType;
	
	private String offenseAttemptedCompleted;
	
	@ManyToOne
	@JoinColumn(name="locationTypeTypeId")
	private LocationType locationType;
	
	private Integer numberOfPremisesEntered;
	
	@ManyToOne
	@JoinColumn(name="methodOfEntryTypeID")
	private MethodOfEntryType methodOfEntryType;
	
	@ManyToOne
	@JoinColumn(name="biasMotivationTypeId")
	private BiasMotivationType biasMotivationType;
	
	@OneToMany(mappedBy = "offenseSegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinTable(name = "offenderSuspectedOfUsing", 
	joinColumns = @JoinColumn(name = "offenseSegmentId", referencedColumnName = "offenseSegmentId"), 
	inverseJoinColumns = @JoinColumn(name = "offenderSuspectedOfUsingTypeId", referencedColumnName = "offenderSuspectedOfUsingTypeId"))
	private Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes;     
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinTable(name = "typeCriminalActivity", 
	joinColumns = @JoinColumn(name = "offenseSegmentId", referencedColumnName = "offenseSegmentId"), 
	inverseJoinColumns = @JoinColumn(name = "typeOfCriminalActivityTypeId", referencedColumnName = "typeOfCriminalActivityTypeId"))
	private Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes;     
	
	public OffenseSegment() {
		super();
		offenderSuspectedOfUsingTypes = new HashSet<>();
//		typeOfWeaponForceInvolvedTypes = new HashSet<>();
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
	public String getOffenseAttemptedCompleted() {
		return offenseAttemptedCompleted;
	}
	public void setOffenseAttemptedCompleted(String offenseAttemptedCompleted) {
		this.offenseAttemptedCompleted = offenseAttemptedCompleted;
	}
	public Integer getOffenseSegmentId() {
		return offenseSegmentId;
	}
	public void setOffenseSegmentId(Integer offenseSegmentId) {
		this.offenseSegmentId = offenseSegmentId;
	}
	public AdministrativeSegment getAdministrativeSegment() {
		return administrativeSegment;
	}
	public void setAdministrativeSegment(AdministrativeSegment administrativeSegment) {
		this.administrativeSegment = administrativeSegment;
	}
	public UcrOffenseCodeType getUcrOffenseCodeType() {
		return ucrOffenseCodeType;
	}
	public void setUcrOffenseCodeType(UcrOffenseCodeType ucrOffenseCodeType) {
		this.ucrOffenseCodeType = ucrOffenseCodeType;
	}
	public LocationType getLocationType() {
		return locationType;
	}
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}
	public Integer getNumberOfPremisesEntered() {
		return numberOfPremisesEntered;
	}
	public void setNumberOfPremisesEntered(Integer numberOfPremisesEntered) {
		this.numberOfPremisesEntered = numberOfPremisesEntered;
	}
	public MethodOfEntryType getMethodOfEntryType() {
		return methodOfEntryType;
	}
	public void setMethodOfEntryType(MethodOfEntryType methodOfEntryType) {
		this.methodOfEntryType = methodOfEntryType;
	}
	public BiasMotivationType getBiasMotivationType() {
		return biasMotivationType;
	}
	public void setBiasMotivationType(BiasMotivationType biasMotivationType) {
		this.biasMotivationType = biasMotivationType;
	}
	public Set<OffenderSuspectedOfUsingType> getOffenderSuspectedOfUsingTypes() {
		return offenderSuspectedOfUsingTypes;
	}
	public void setOffenderSuspectedOfUsingTypes(Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes) {
		this.offenderSuspectedOfUsingTypes = offenderSuspectedOfUsingTypes;
	}
	public Set<TypeOfCriminalActivityType> getTypeOfCriminalActivityTypes() {
		return typeOfCriminalActivityTypes;
	}
	public void setTypeOfCriminalActivityTypes(Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes) {
		this.typeOfCriminalActivityTypes = typeOfCriminalActivityTypes;
	}
	public Set<TypeOfWeaponForceInvolved> getTypeOfWeaponForceInvolveds() {
		return typeOfWeaponForceInvolveds;
	}
	public void setTypeOfWeaponForceInvolveds(Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds) {
		this.typeOfWeaponForceInvolveds = typeOfWeaponForceInvolveds;
	}
}
