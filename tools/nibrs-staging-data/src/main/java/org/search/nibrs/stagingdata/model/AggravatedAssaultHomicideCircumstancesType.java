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

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.VictimSegment;

@Entity
public class AggravatedAssaultHomicideCircumstancesType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer aggravatedAssaultHomicideCircumstancesTypeId; 
	private String aggravatedAssaultHomicideCircumstancesCode; 
	private String aggravatedAssaultHomicideCircumstancesDescription; 
	
	@ManyToMany(mappedBy = "aggravatedAssaultHomicideCircumstancesTypes", fetch=FetchType.LAZY)
	private Set<VictimSegment> victimSegments;

	public AggravatedAssaultHomicideCircumstancesType() {
		super();
	}

	public AggravatedAssaultHomicideCircumstancesType(Integer aggravatedAssaultHomicideCircumstancesTypeId) {
		super();
		this.aggravatedAssaultHomicideCircumstancesTypeId = aggravatedAssaultHomicideCircumstancesTypeId;
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
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "victimSegments");		
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggravatedAssaultHomicideCircumstancesCode == null) ? 0
				: aggravatedAssaultHomicideCircumstancesCode.hashCode());
		result = prime * result + ((aggravatedAssaultHomicideCircumstancesDescription == null) ? 0
				: aggravatedAssaultHomicideCircumstancesDescription.hashCode());
		result = prime * result + ((aggravatedAssaultHomicideCircumstancesTypeId == null) ? 0
				: aggravatedAssaultHomicideCircumstancesTypeId.hashCode());
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
		AggravatedAssaultHomicideCircumstancesType other = (AggravatedAssaultHomicideCircumstancesType) obj;
		if (aggravatedAssaultHomicideCircumstancesCode == null) {
			if (other.aggravatedAssaultHomicideCircumstancesCode != null)
				return false;
		} else if (!aggravatedAssaultHomicideCircumstancesCode.equals(other.aggravatedAssaultHomicideCircumstancesCode))
			return false;
		if (aggravatedAssaultHomicideCircumstancesDescription == null) {
			if (other.aggravatedAssaultHomicideCircumstancesDescription != null)
				return false;
		} else if (!aggravatedAssaultHomicideCircumstancesDescription
				.equals(other.aggravatedAssaultHomicideCircumstancesDescription))
			return false;
		if (aggravatedAssaultHomicideCircumstancesTypeId == null) {
			if (other.aggravatedAssaultHomicideCircumstancesTypeId != null)
				return false;
		} else if (!aggravatedAssaultHomicideCircumstancesTypeId
				.equals(other.aggravatedAssaultHomicideCircumstancesTypeId))
			return false;
		return true;
	}
}
