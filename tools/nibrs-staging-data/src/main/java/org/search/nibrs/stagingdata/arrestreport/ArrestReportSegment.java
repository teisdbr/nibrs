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
package org.search.nibrs.stagingdata.arrestreport;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;

@Entity
public class ArrestReportSegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer arrestReportSegmentId; 
	private Integer segmentActionTypeTypeID; 
	private String monthOfTape; 
	private String yearOfTape; 
	private String cityIndicator; 
	private Integer agencyId; 
	private String ori; 
	private String arrestTransactionNumber; 
	private Integer arresteeSequenceNumber; 
	private Date arrestDate;
	
//	private Integer arrestDateId; 
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="arrestDateId")
	private DateType arrestDateType; 
	private Integer typeOfArrestTypeId; 
	private Integer ageOfArresteeMin; 
	private Integer ageOfArresteeMax; 
	private Integer sexOfPersonTypeId; 
	private Integer raceOfPersonTypeId;
	private Integer ethnicityOfPersonTypeId;
	private Integer residentStatusOfPersonTypeId;
	private Integer dispositionOfArresteeUnder18TypeId;
	
//	private Integer ucrOffenseCodeTypeId;
	@ManyToOne
	@JoinColumn(name="ucrOffenseCodeTypeId")
	private UcrOffenseCodeType ucrOffenseCodeType;
	
	public Integer getSegmentActionTypeTypeID() {
		return segmentActionTypeTypeID;
	}
	public void setSegmentActionTypeTypeID(Integer segmentActionTypeTypeID) {
		this.segmentActionTypeTypeID = segmentActionTypeTypeID;
	}
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
	public Integer getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
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
	public Integer getTypeOfArrestTypeId() {
		return typeOfArrestTypeId;
	}
	public void setTypeOfArrestTypeId(Integer typeOfArrestTypeId) {
		this.typeOfArrestTypeId = typeOfArrestTypeId;
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
	public Integer getSexOfPersonTypeId() {
		return sexOfPersonTypeId;
	}
	public void setSexOfPersonTypeId(Integer sexOfPersonTypeId) {
		this.sexOfPersonTypeId = sexOfPersonTypeId;
	}
	public Integer getRaceOfPersonTypeId() {
		return raceOfPersonTypeId;
	}
	public void setRaceOfPersonTypeId(Integer raceOfPersonTypeId) {
		this.raceOfPersonTypeId = raceOfPersonTypeId;
	}
	public Integer getEthnicityOfPersonTypeId() {
		return ethnicityOfPersonTypeId;
	}
	public void setEthnicityOfPersonTypeId(Integer ethnicityOfPersonTypeId) {
		this.ethnicityOfPersonTypeId = ethnicityOfPersonTypeId;
	}
	public Integer getResidentStatusOfPersonTypeId() {
		return residentStatusOfPersonTypeId;
	}
	public void setResidentStatusOfPersonTypeId(Integer residentStatusOfPersonTypeId) {
		this.residentStatusOfPersonTypeId = residentStatusOfPersonTypeId;
	}
	public Integer getDispositionOfArresteeUnder18TypeId() {
		return dispositionOfArresteeUnder18TypeId;
	}
	public void setDispositionOfArresteeUnder18TypeId(Integer dispositionOfArresteeUnder18TypeId) {
		this.dispositionOfArresteeUnder18TypeId = dispositionOfArresteeUnder18TypeId;
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
//	public Integer getArrestDateId() {
//		return arrestDateId;
//	}
//	public void setArrestDateId(Integer arrestDateId) {
//		this.arrestDateId = arrestDateId;
//	}
//	public Integer getUcrOffenseCodeTypeId() {
//		return ucrOffenseCodeTypeId;
//	}
//	public void setUcrOffenseCodeTypeId(Integer ucrOffenseCodeTypeId) {
//		this.ucrOffenseCodeTypeId = ucrOffenseCodeTypeId;
//	}
}
