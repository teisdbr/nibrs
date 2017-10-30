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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.search.nibrs.stagingdata.model.EthnicityOfPersonType;
import org.search.nibrs.stagingdata.model.RaceOfPersonType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SexOfPersonType;

@Entity
public class OffenderSegment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer offenderSegmentId;
	
	@ManyToOne
	@JoinColumn(name="administrativeSegmentId") 
	private AdministrativeSegment administrativeSegment; 
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeID") 
	private SegmentActionTypeType segmentActionType; 
	private Integer offenderSequenceNumber; 

	private Integer ageOfOffenderMin; 
	private Integer ageOfOffenderMax; 
	@ManyToOne
	@JoinColumn(name="sexOfPersonTypeId") 
	private SexOfPersonType sexOfPersonType; 
	@ManyToOne
	@JoinColumn(name="raceOfPersonTypeId") 
	private RaceOfPersonType raceOfPersonType;
	@ManyToOne
	@JoinColumn(name="ethnicityOfPersonTypeId")
	
	private EthnicityOfPersonType ethnicityOfPersonType;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
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
	public AdministrativeSegment getAdministrativeSegment() {
		return administrativeSegment;
	}
	public void setAdministrativeSegment(AdministrativeSegment administrativeSegment) {
		this.administrativeSegment = administrativeSegment;
	}
	public Integer getOffenderSequenceNumber() {
		return offenderSequenceNumber;
	}
	public void setOffenderSequenceNumber(Integer offenderSequenceNumber) {
		this.offenderSequenceNumber = offenderSequenceNumber;
	}
	public Integer getAgeOfOffenderMin() {
		return ageOfOffenderMin;
	}
	public void setAgeOfOffenderMin(Integer ageOfOffenderMin) {
		this.ageOfOffenderMin = ageOfOffenderMin;
	}
	public Integer getAgeOfOffenderMax() {
		return ageOfOffenderMax;
	}
	public void setAgeOfOffenderMax(Integer ageOfOffenderMax) {
		this.ageOfOffenderMax = ageOfOffenderMax;
	}
	public Integer getOffenderSegmentId() {
		return offenderSegmentId;
	}
	public void setOffenderSegmentId(Integer offenderSegmentId) {
		this.offenderSegmentId = offenderSegmentId;
	}
}
