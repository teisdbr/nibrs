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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class ArrestReportSegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer arrestReportSegmentId;
	
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeID") 
	private SegmentActionTypeType segmentActionType; 
	private String monthOfTape; 
	private String yearOfTape; 
	private String cityIndicator;
	
	@ManyToOne
	@JoinColumn(name="agencyId")
	private Agency agency; 
	private String ori; 
	private String arrestTransactionNumber; 
	private Integer arresteeSequenceNumber; 
	private Date arrestDate;
	
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="arrestDateId")
	private DateType arrestDateType; 

	@ManyToOne
	@JoinColumn(name="typeOfArrestTypeId") 
	private TypeOfArrestType typeOfArrestType; 
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
	
	public Integer getArrestReportSegmentId() {
		return arrestReportSegmentId;
	}
	public void setArrestReportSegmentId(Integer arrestReportSegmentId) {
		this.arrestReportSegmentId = arrestReportSegmentId;
	}
	public String getMonthOfTape() {
		return monthOfTape;
	}
	public void setMonthOfTape(String monthOfTape) {
		this.monthOfTape = monthOfTape;
	}
	public String getYearOfTape() {
		return yearOfTape;
	}
	public void setYearOfTape(String yearOfTape) {
		this.yearOfTape = yearOfTape;
	}
	public String getCityIndicator() {
		return cityIndicator;
	}
	public void setCityIndicator(String cityIndicator) {
		this.cityIndicator = cityIndicator;
	}
	public String getOri() {
		return ori;
	}
	public void setOri(String ori) {
		this.ori = ori;
	}
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
	public DateType getDateType() {
		return arrestDateType;
	}
	public void setArrestDateType(DateType dateType) {
		this.arrestDateType = dateType;
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
	public Agency getAgency() {
		return agency;
	}
	public void setAgency(Agency agency) {
		this.agency = agency;
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
}
