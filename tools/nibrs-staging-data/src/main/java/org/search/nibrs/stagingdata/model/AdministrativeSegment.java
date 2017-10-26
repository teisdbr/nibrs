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
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class AdministrativeSegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer administrativeSegmentId;
	
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeID") 
	private SegmentActionTypeType segmentActionType; 
	private String monthOfTape; 
	private String yearOfTape; 
	private String cityIndicator;
	private String ori;
	
    @OneToMany(mappedBy = "administrativeSegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<OffenseSegment> offenseSegments;
	
	@ManyToOne
	@JoinColumn(name="agencyId")
	private Agency agency; 
	private String incidentNumber; 
	private Date incidentDate;
	
	@ManyToOne
	@JoinColumn(name="incidentDateId")
	private DateType incidentDateType; 

	private String reportDateIndicator; 
	private Integer incidentHour;
	@ManyToOne
	@JoinColumn(name="clearedExceptionallyTypeId") 
	private ClearedExceptionallyType clearedExceptionallyType; 
	
    public Set<OffenseSegment> getOffenseSegments() {
        return offenseSegments;
    }

    public void setOffenseSegments(Set<OffenseSegment> offenseSegments) {
    	this.offenseSegments = offenseSegments;
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
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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
	public String getReportDateIndicator() {
		return reportDateIndicator;
	}
	public void setReportDateIndicator(String reportDateIndicator) {
		this.reportDateIndicator = reportDateIndicator;
	}
	public Integer getIncidentHour() {
		return incidentHour;
	}
	public void setIncidentHour(Integer incidentHour) {
		this.incidentHour = incidentHour;
	}
	public Integer getAdministrativeSegmentId() {
		return administrativeSegmentId;
	}
	public void setAdministrativeSegmentId(Integer administrativeSegmentId) {
		this.administrativeSegmentId = administrativeSegmentId;
	}
	public String getIncidentNumber() {
		return incidentNumber;
	}
	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}
	public Date getIncidentDate() {
		return incidentDate;
	}
	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}
	public DateType getIncidentDateType() {
		return incidentDateType;
	}
	public void setIncidentDateType(DateType incidentDateType) {
		this.incidentDateType = incidentDateType;
	}
	public ClearedExceptionallyType getClearedExceptionallyType() {
		return clearedExceptionallyType;
	}
	public void setClearedExceptionallyType(ClearedExceptionallyType clearedExceptionallyType) {
		this.clearedExceptionallyType = clearedExceptionallyType;
	}
}
