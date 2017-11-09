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

import java.util.Date;
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.search.nibrs.stagingdata.model.ArresteeSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.DispositionOfArresteeUnder18Type;
import org.search.nibrs.stagingdata.model.EthnicityOfPersonType;
import org.search.nibrs.stagingdata.model.MultipleArresteeSegmentsIndicatorType;
import org.search.nibrs.stagingdata.model.RaceOfPersonType;
import org.search.nibrs.stagingdata.model.ResidentStatusOfPersonType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SexOfPersonType;
import org.search.nibrs.stagingdata.model.TypeOfArrestType;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class, 
	property = "arresteeSegmentId")
public class ArresteeSegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer arresteeSegmentId;
	
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeID") 
	private SegmentActionTypeType segmentActionType; 
	@ManyToOne
	@JoinColumn(name="administrativeSegmentId") 
	private AdministrativeSegment administrativeSegment; 
	private Integer arresteeSequenceNumber; 
	private String arrestTransactionNumber; 
	private Date arrestDate;
	
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="arrestDateId")
	private DateType arrestDateType; 
	
	@ManyToOne
	@JoinColumn(name="typeOfArrestTypeId") 
	private TypeOfArrestType typeOfArrestType;
	
	@ManyToOne
	@JoinColumn(name="multipleArresteeSegmentsIndicatorTypeId") 
	private MultipleArresteeSegmentsIndicatorType multipleArresteeSegmentsIndicatorType;
	
	private Integer ageOfArresteeMin; 
	private Integer ageOfArresteeMax; 
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
	@JoinColumn(name="dispositionOfArresteeUnder18TypeId") 
	private DispositionOfArresteeUnder18Type dispositionOfArresteeUnder18Type;
	
	@ManyToOne
	@JoinColumn(name="ucrOffenseCodeTypeId")
	private UcrOffenseCodeType ucrOffenseCodeType;
	
	@OneToMany(mappedBy = "arresteeSegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths;
	
	public String getArrestTransactionNumber() {
		return arrestTransactionNumber;
	}
	public void setArrestTransactionNumber(String arrestTransactionNumber) {
		this.arrestTransactionNumber = arrestTransactionNumber;
	}
	public Integer getArresteeSequenceNumber() {
		return arresteeSequenceNumber;
	}
	public void setArresteeSequenceNumber(Integer arresteeSequenceNumber) {
		this.arresteeSequenceNumber = arresteeSequenceNumber;
	}
	public Date getArrestDate() {
		return arrestDate;
	}
	public void setArrestDate(Date arrestDate) {
		this.arrestDate = arrestDate;
	}
	public Integer getAgeOfArresteeMin() {
		return ageOfArresteeMin;
	}
	public void setAgeOfArresteeMin(Integer ageOfArresteeMin) {
		this.ageOfArresteeMin = ageOfArresteeMin;
	}
	public Integer getAgeOfArresteeMax() {
		return ageOfArresteeMax;
	}
	public void setAgeOfArresteeMax(Integer ageOfArresteeMax) {
		this.ageOfArresteeMax = ageOfArresteeMax;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	public UcrOffenseCodeType getUcrOffenseCodeType() {
		return ucrOffenseCodeType;
	}
	public void setUcrOffenseCodeType(UcrOffenseCodeType ucrOffenseCodeType) {
		this.ucrOffenseCodeType = ucrOffenseCodeType;
	}
	public DateType getArrestDateType() {
		return arrestDateType;
	}
	public void setArrestDateType(DateType arrestDateType) {
		this.arrestDateType = arrestDateType;
	}
	public DispositionOfArresteeUnder18Type getDispositionOfArresteeUnder18Type() {
		return dispositionOfArresteeUnder18Type;
	}
	public void setDispositionOfArresteeUnder18Type(DispositionOfArresteeUnder18Type dispositionOfArresteeUnder18Type) {
		this.dispositionOfArresteeUnder18Type = dispositionOfArresteeUnder18Type;
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
	public TypeOfArrestType getTypeOfArrestType() {
		return typeOfArrestType;
	}
	public void setTypeOfArrestType(TypeOfArrestType typeOfArrestType) {
		this.typeOfArrestType = typeOfArrestType;
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
	public MultipleArresteeSegmentsIndicatorType getMultipleArresteeSegmentsIndicatorType() {
		return multipleArresteeSegmentsIndicatorType;
	}
	public void setMultipleArresteeSegmentsIndicatorType(MultipleArresteeSegmentsIndicatorType multipleArresteeSegmentsIndicatorType) {
		this.multipleArresteeSegmentsIndicatorType = multipleArresteeSegmentsIndicatorType;
	}
	public Integer getArresteeSegmentId() {
		return arresteeSegmentId;
	}
	public void setArresteeSegmentId(Integer arresteeSegmentId) {
		this.arresteeSegmentId = arresteeSegmentId;
	}
	public Set<ArresteeSegmentWasArmedWith> getArresteeSegmentWasArmedWiths() {
		return arresteeSegmentWasArmedWiths;
	}
	public void setArresteeSegmentWasArmedWiths(Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths) {
		this.arresteeSegmentWasArmedWiths = arresteeSegmentWasArmedWiths;
	}
}
