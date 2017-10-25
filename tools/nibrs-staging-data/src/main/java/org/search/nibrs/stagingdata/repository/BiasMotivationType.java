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

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class BiasMotivationType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer biasMotivationTypeID; 
	
	private String biasMotivationCode; 
	private String biasMotivationDescription; 
	private String biasMotivationCategory; 
	
	public BiasMotivationType() {
		super();
	}

	public BiasMotivationType(Integer biasMotivationTypeID, String biasMotivationCode, String biasMotivationDescription,
			String biasMotivationCategory) {
		super();
		this.biasMotivationTypeID = biasMotivationTypeID;
		this.biasMotivationCode = biasMotivationCode;
		this.biasMotivationDescription = biasMotivationDescription;
		this.biasMotivationCategory = biasMotivationCategory;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getBiasMotivationTypeID() {
		return biasMotivationTypeID;
	}

	public void setBiasMotivationTypeID(Integer biasMotivationTypeID) {
		this.biasMotivationTypeID = biasMotivationTypeID;
	}

	public String getBiasMotivationCode() {
		return biasMotivationCode;
	}

	public void setBiasMotivationCode(String biasMotivationCode) {
		this.biasMotivationCode = biasMotivationCode;
	}

	public String getBiasMotivationDescription() {
		return biasMotivationDescription;
	}

	public void setBiasMotivationDescription(String biasMotivationDescription) {
		this.biasMotivationDescription = biasMotivationDescription;
	}

	public String getBiasMotivationCategory() {
		return biasMotivationCategory;
	}

	public void setBiasMotivationCategory(String biasMotivationCategory) {
		this.biasMotivationCategory = biasMotivationCategory;
	}

}
