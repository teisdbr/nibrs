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
package org.search.nibrs.stagingdata.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Agency {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer agencyId; 
	
	private String agencyOri; 
	private String agencyName;
	private String stateCode;
	private String stateName;
	private Integer Population;
	
	@ManyToOne
	@JoinColumn(name = "agencyTypeId")
	private AgencyType agencyType; 
	
	public Agency() {
		super();
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getAgencyOri() {
		return agencyOri;
	}

	public void setAgencyOri(String agencyOri) {
		this.agencyOri = agencyOri;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getPopulation() {
		return Population;
	}

	public void setPopulation(Integer population) {
		Population = population;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public AgencyType getAgencyType() {
		return agencyType;
	}

	public void setAgencyType(AgencyType agencyType) {
		this.agencyType = agencyType;
	}

}
