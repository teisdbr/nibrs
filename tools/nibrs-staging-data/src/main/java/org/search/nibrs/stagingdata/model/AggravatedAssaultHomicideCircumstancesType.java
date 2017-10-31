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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class AggravatedAssaultHomicideCircumstancesType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer aggravatedAssaultHomicideCircumstancesTypeId; 
	private String aggravatedAssaultHomicideCircumstancesCode; 
	private String aggravatedAssaultHomicideCircumstancesDescription; 
	
	public AggravatedAssaultHomicideCircumstancesType() {
		super();
	}

	public AggravatedAssaultHomicideCircumstancesType(Integer aggravatedAssaultHomicideCircumstancesTypeId,
			String aggravatedAssaultHomicideCircumstancesCode,
			String aggravatedAssaultHomicideCircumstancesDescription) {
		super();
		this.aggravatedAssaultHomicideCircumstancesTypeId = aggravatedAssaultHomicideCircumstancesTypeId;
		this.aggravatedAssaultHomicideCircumstancesCode = aggravatedAssaultHomicideCircumstancesCode;
		this.aggravatedAssaultHomicideCircumstancesDescription = aggravatedAssaultHomicideCircumstancesDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getAggravatedAssaultHomicideCircumstancesTypeId() {
		return aggravatedAssaultHomicideCircumstancesTypeId;
	}

	public void setAggravatedAssaultHomicideCircumstancesTypeId(Integer aggravatedAssaultHomicideCircumstancesTypeId) {
		this.aggravatedAssaultHomicideCircumstancesTypeId = aggravatedAssaultHomicideCircumstancesTypeId;
	}

	public String getAggravatedAssaultHomicideCircumstancesCode() {
		return aggravatedAssaultHomicideCircumstancesCode;
	}

	public void setAggravatedAssaultHomicideCircumstancesCode(String aggravatedAssaultHomicideCircumstancesCode) {
		this.aggravatedAssaultHomicideCircumstancesCode = aggravatedAssaultHomicideCircumstancesCode;
	}

	public String getAggravatedAssaultHomicideCircumstancesDescription() {
		return aggravatedAssaultHomicideCircumstancesDescription;
	}

	public void setAggravatedAssaultHomicideCircumstancesDescription(
			String aggravatedAssaultHomicideCircumstancesDescription) {
		this.aggravatedAssaultHomicideCircumstancesDescription = aggravatedAssaultHomicideCircumstancesDescription;
	}
}
