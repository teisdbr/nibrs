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
package org.search.nibrs.stagingdata.groupa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.repository.BiasMotivationType;
import org.search.nibrs.stagingdata.repository.LocationType;
import org.search.nibrs.stagingdata.repository.MethodOfEntryType;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeType;
import org.search.nibrs.stagingdata.repository.UcrOffenseCodeType;

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
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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
}
