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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.AdditionalJustifiableHomicideCircumstancesType;
import org.search.nibrs.stagingdata.model.AggravatedAssaultHomicideCircumstancesType;
import org.search.nibrs.stagingdata.model.EthnicityOfPersonType;
import org.search.nibrs.stagingdata.model.OfficerActivityCircumstanceType;
import org.search.nibrs.stagingdata.model.OfficerAssignmentTypeType;
import org.search.nibrs.stagingdata.model.RaceOfPersonType;
import org.search.nibrs.stagingdata.model.ResidentStatusOfPersonType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SexOfPersonType;
import org.search.nibrs.stagingdata.model.TypeInjuryType;
import org.search.nibrs.stagingdata.model.TypeOfVictimType;

@Entity
public class VictimSegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer victimSegmentId;
	
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeId") 
	private SegmentActionTypeType segmentActionType; 
	@ManyToOne
	@JoinColumn(name="administrativeSegmentId") 
	private AdministrativeSegment administrativeSegment; 
	private Integer victimSequenceNumber; 
	
	@ManyToOne
	@JoinColumn(name="typeOfVictimTypeId") 
	private TypeOfVictimType typeOfVictimType;
	
	@ManyToOne
	@JoinColumn(name="officerActivityCircumstanceTypeId") 
	private OfficerActivityCircumstanceType officerActivityCircumstanceType;
	
	@ManyToOne
	@JoinColumn(name="officerAssignmentTypeTypeId") 
	private OfficerAssignmentTypeType officerAssignmentTypeType;
	
	private Integer ageOfVictimMin; 
	private Integer ageOfVictimMax; 
	private Integer ageNeonateIndicator;
	private Integer ageFirstWeekIndicator;
	private Integer ageFirstYearIndicator;
	
	@ManyToOne
	@JoinColumn(name="sexOfPersonTypeId") 
	private SexOfPersonType sexOfPersonType; 
	@ManyToOne
	@JoinColumn(name="raceOfPersonTypeId") 
	private RaceOfPersonType raceOfPersonType;
	@ManyToOne
	@JoinColumn(name="ethnicityOfPersonTypeId") 
	private EthnicityOfPersonType ethnicityOfPersonType;
	@ManyToOne
	@JoinColumn(name="residentStatusOfPersonTypeId") 
	private ResidentStatusOfPersonType residentStatusOfPersonType;
	@ManyToOne
	@JoinColumn(name="additionalJustifiableHomicideCircumstancesTypeId")
	private AdditionalJustifiableHomicideCircumstancesType additionalJustifiableHomicideCircumstancesType;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinTable(name = "typeInjury", 
	joinColumns = @JoinColumn(name = "victimSegmentId", referencedColumnName = "victimSegmentId"), 
	inverseJoinColumns = @JoinColumn(name = "typeInjuryTypeId", referencedColumnName = "typeInjuryTypeId"))
	private Set<TypeInjuryType> typeInjuryTypes;     
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinTable(name = "victimOffenseAssociation", 
	joinColumns = @JoinColumn(name = "victimSegmentId", referencedColumnName = "victimSegmentId"), 
	inverseJoinColumns = @JoinColumn(name = "offenseSegmentId", referencedColumnName = "offenseSegmentId"))
	private Set<OffenseSegment> offenseSegments;     
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinTable(name = "aggravatedAssaultHomicideCircumstances", 
	joinColumns = @JoinColumn(name = "victimSegmentId", referencedColumnName = "victimSegmentId"), 
	inverseJoinColumns = @JoinColumn(name = "aggravatedAssaultHomicideCircumstancesTypeId", 
				referencedColumnName = "aggravatedAssaultHomicideCircumstancesTypeId"))
	private Set<AggravatedAssaultHomicideCircumstancesType> aggravatedAssaultHomicideCircumstancesTypes;     
	
	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		String resultWithoutParentSegment = ReflectionToStringBuilder.toStringExclude(this, "administrativeSegment", "victimSegments");
		int index = StringUtils.indexOf(resultWithoutParentSegment, ",");
		
		StringBuilder sb = new StringBuilder(resultWithoutParentSegment);
		sb.insert(index + 1, "administrativeSegmentId=" + administrativeSegment.getAdministrativeSegmentId() + ",");
		return sb.toString();
	}
	public EthnicityOfPersonType getEthnicityOfPersonType() {
		return ethnicityOfPersonType;
	}
	public void setEthnicityOfPersonType(EthnicityOfPersonType ethnicityOfPersonType) {
		this.ethnicityOfPersonType = ethnicityOfPersonType;
	}
	public RaceOfPersonType getRaceOfPersonType() {
		return raceOfPersonType;
	}
	public void setRaceOfPersonType(RaceOfPersonType raceOfPersonType) {
		this.raceOfPersonType = raceOfPersonType;
	}
	public SexOfPersonType getSexOfPersonType() {
		return sexOfPersonType;
	}
	public void setSexOfPersonType(SexOfPersonType sexOfPersonType) {
		this.sexOfPersonType = sexOfPersonType;
	}
	public SegmentActionTypeType getSegmentActionType() {
		return segmentActionType;
	}
	public void setSegmentActionType(SegmentActionTypeType segmentActionType) {
		this.segmentActionType = segmentActionType;
	}
	public ResidentStatusOfPersonType getResidentStatusOfPersonType() {
		return residentStatusOfPersonType;
	}
	public void setResidentStatusOfPersonType(ResidentStatusOfPersonType residentStatusOfPersonType) {
		this.residentStatusOfPersonType = residentStatusOfPersonType;
	}
	public AdministrativeSegment getAdministrativeSegment() {
		return administrativeSegment;
	}
	public void setAdministrativeSegment(AdministrativeSegment administrativeSegment) {
		this.administrativeSegment = administrativeSegment;
	}
	public Integer getVictimSequenceNumber() {
		return victimSequenceNumber;
	}
	public void setVictimSequenceNumber(Integer victimSequenceNumber) {
		this.victimSequenceNumber = victimSequenceNumber;
	}
	public TypeOfVictimType getTypeOfVictimType() {
		return typeOfVictimType;
	}
	public void setTypeOfVictimType(TypeOfVictimType typeOfVictimType) {
		this.typeOfVictimType = typeOfVictimType;
	}
	public OfficerActivityCircumstanceType getOfficerActivityCircumstanceType() {
		return officerActivityCircumstanceType;
	}
	public void setOfficerActivityCircumstanceType(OfficerActivityCircumstanceType officerActivityCircumstanceType) {
		this.officerActivityCircumstanceType = officerActivityCircumstanceType;
	}
	public OfficerAssignmentTypeType getOfficerAssignmentTypeType() {
		return officerAssignmentTypeType;
	}
	public void setOfficerAssignmentTypeType(OfficerAssignmentTypeType officerAssignmentTypeType) {
		this.officerAssignmentTypeType = officerAssignmentTypeType;
	}
	public Integer getAgeOfVictimMin() {
		return ageOfVictimMin;
	}
	public void setAgeOfVictimMin(Integer ageOfVictimMin) {
		this.ageOfVictimMin = ageOfVictimMin;
	}
	public Integer getAgeOfVictimMax() {
		return ageOfVictimMax;
	}
	public void setAgeOfVictimMax(Integer ageOfVictimMax) {
		this.ageOfVictimMax = ageOfVictimMax;
	}
	public Integer getAgeNeonateIndicator() {
		return ageNeonateIndicator;
	}
	public void setAgeNeonateIndicator(Integer ageNeonateIndicator) {
		this.ageNeonateIndicator = ageNeonateIndicator;
	}
	public Integer getAgeFirstWeekIndicator() {
		return ageFirstWeekIndicator;
	}
	public void setAgeFirstWeekIndicator(Integer ageFirstWeekIndicator) {
		this.ageFirstWeekIndicator = ageFirstWeekIndicator;
	}
	public Integer getAgeFirstYearIndicator() {
		return ageFirstYearIndicator;
	}
	public void setAgeFirstYearIndicator(Integer ageFirstYearIndicator) {
		this.ageFirstYearIndicator = ageFirstYearIndicator;
	}
	public AdditionalJustifiableHomicideCircumstancesType getAdditionalJustifiableHomicideCircumstancesType() {
		return additionalJustifiableHomicideCircumstancesType;
	}
	public void setAdditionalJustifiableHomicideCircumstancesType(
			AdditionalJustifiableHomicideCircumstancesType additionalJustifiableHomicideCircumstancesType) {
		this.additionalJustifiableHomicideCircumstancesType = additionalJustifiableHomicideCircumstancesType;
	}
	public Set<TypeInjuryType> getTypeInjuryTypes() {
		return typeInjuryTypes;
	}
	public void setTypeInjuryTypes(Set<TypeInjuryType> typeInjuryTypes) {
		this.typeInjuryTypes = typeInjuryTypes;
	}
	public Set<OffenseSegment> getOffenseSegments() {
		return offenseSegments;
	}
	public void setOffenseSegments(Set<OffenseSegment> offenseSegments) {
		this.offenseSegments = offenseSegments;
	}
	public Set<AggravatedAssaultHomicideCircumstancesType> getAggravatedAssaultHomicideCircumstancesTypes() {
		return aggravatedAssaultHomicideCircumstancesTypes;
	}
	public void setAggravatedAssaultHomicideCircumstancesTypes(Set<AggravatedAssaultHomicideCircumstancesType> aggravatedAssaultHomicideCircumstancesTypes) {
		this.aggravatedAssaultHomicideCircumstancesTypes = aggravatedAssaultHomicideCircumstancesTypes;
	}
}
