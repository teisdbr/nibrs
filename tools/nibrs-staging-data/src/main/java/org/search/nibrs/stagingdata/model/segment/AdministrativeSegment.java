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
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.CargoTheftIndicatorType;
import org.search.nibrs.stagingdata.model.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class, 
	property = "administrativeSegmentId")
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
	
    @OneToMany(mappedBy = "administrativeSegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PropertySegment> propertySegments;
    
    @OneToMany(mappedBy = "administrativeSegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ArresteeSegment> arresteeSegments;
    
    @OneToMany(mappedBy = "administrativeSegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OffenderSegment> offenderSegments;
    
    @OneToMany(mappedBy = "administrativeSegment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<VictimSegment> victimSegments;
    
	@ManyToOne
	@JoinColumn(name="agencyId")
	private Agency agency; 
	private String incidentNumber; 
	private Date incidentDate;
	
	@ManyToOne
	@JoinColumn(name="incidentDateId")
	private DateType incidentDateType; 

	private String reportDateIndicator; 
	private String incidentHour;
	@ManyToOne
	@JoinColumn(name="clearedExceptionallyTypeId") 
	private ClearedExceptionallyType clearedExceptionallyType; 
	@ManyToOne
	@JoinColumn(name="cargoTheftIndicatorTypeId") 
	private CargoTheftIndicatorType cargoTheftIndicatorType; 
	
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
	public String getIncidentHour() {
		return incidentHour;
	}
	public void setIncidentHour(String incidentHour) {
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

	public Set<PropertySegment> getPropertySegments() {
		return propertySegments;
	}

	public void setPropertySegments(Set<PropertySegment> propertySegments) {
		this.propertySegments = propertySegments;
	}

	public Set<ArresteeSegment> getArresteeSegments() {
		return arresteeSegments;
	}

	public void setArresteeSegments(Set<ArresteeSegment> arresteeSegments) {
		this.arresteeSegments = arresteeSegments;
	}

	public Set<OffenderSegment> getOffenderSegments() {
		return offenderSegments;
	}

	public void setOffenderSegments(Set<OffenderSegment> offenderSegments) {
		this.offenderSegments = offenderSegments;
	}

	public Set<VictimSegment> getVictimSegments() {
		return victimSegments;
	}

	public void setVictimSegments(Set<VictimSegment> victimSegments) {
		this.victimSegments = victimSegments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((administrativeSegmentId == null) ? 0 : administrativeSegmentId.hashCode());
		result = prime * result + ((agency == null) ? 0 : agency.hashCode());
		result = prime * result + ((cityIndicator == null) ? 0 : cityIndicator.hashCode());
		result = prime * result + ((clearedExceptionallyType == null) ? 0 : clearedExceptionallyType.hashCode());
		result = prime * result + ((incidentDate == null) ? 0 : incidentDate.hashCode());
		result = prime * result + ((incidentDateType == null) ? 0 : incidentDateType.hashCode());
		result = prime * result + ((incidentHour == null) ? 0 : incidentHour.hashCode());
		result = prime * result + ((incidentNumber == null) ? 0 : incidentNumber.hashCode());
		result = prime * result + ((monthOfTape == null) ? 0 : monthOfTape.hashCode());
		result = prime * result + ((ori == null) ? 0 : ori.hashCode());
		result = prime * result + ((reportDateIndicator == null) ? 0 : reportDateIndicator.hashCode());
		result = prime * result + ((segmentActionType == null) ? 0 : segmentActionType.hashCode());
		result = prime * result + ((yearOfTape == null) ? 0 : yearOfTape.hashCode());
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
		AdministrativeSegment other = (AdministrativeSegment) obj;
		if (administrativeSegmentId == null) {
			if (other.administrativeSegmentId != null)
				return false;
		} else if (!administrativeSegmentId.equals(other.administrativeSegmentId))
			return false;
		if (agency == null) {
			if (other.agency != null)
				return false;
		} else if (!agency.equals(other.agency))
			return false;
		if (cityIndicator == null) {
			if (other.cityIndicator != null)
				return false;
		} else if (!cityIndicator.equals(other.cityIndicator))
			return false;
		if (clearedExceptionallyType == null) {
			if (other.clearedExceptionallyType != null)
				return false;
		} else if (!clearedExceptionallyType.equals(other.clearedExceptionallyType))
			return false;
		if (incidentDate == null) {
			if (other.incidentDate != null)
				return false;
		} else if (!incidentDate.equals(other.incidentDate))
			return false;
		if (incidentDateType == null) {
			if (other.incidentDateType != null)
				return false;
		} else if (!incidentDateType.equals(other.incidentDateType))
			return false;
		if (incidentHour == null) {
			if (other.incidentHour != null)
				return false;
		} else if (!incidentHour.equals(other.incidentHour))
			return false;
		if (incidentNumber == null) {
			if (other.incidentNumber != null)
				return false;
		} else if (!incidentNumber.equals(other.incidentNumber))
			return false;
		if (monthOfTape == null) {
			if (other.monthOfTape != null)
				return false;
		} else if (!monthOfTape.equals(other.monthOfTape))
			return false;
		if (ori == null) {
			if (other.ori != null)
				return false;
		} else if (!ori.equals(other.ori))
			return false;
		if (reportDateIndicator == null) {
			if (other.reportDateIndicator != null)
				return false;
		} else if (!reportDateIndicator.equals(other.reportDateIndicator))
			return false;
		if (segmentActionType == null) {
			if (other.segmentActionType != null)
				return false;
		} else if (!segmentActionType.equals(other.segmentActionType))
			return false;
		if (yearOfTape == null) {
			if (other.yearOfTape != null)
				return false;
		} else if (!yearOfTape.equals(other.yearOfTape))
			return false;
		return true;
	}

	public CargoTheftIndicatorType getCargoTheftIndicatorType() {
		return cargoTheftIndicatorType;
	}

	public void setCargoTheftIndicatorType(CargoTheftIndicatorType cargoTheftIndicatorType) {
		this.cargoTheftIndicatorType = cargoTheftIndicatorType;
	}
}
