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
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class SegmentActionTypeType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer segmentActionTypeTypeId; 
	
	private String segmentActionTypeCode; 
	private String segmentActionTypeDescription; 
	
	public SegmentActionTypeType() {
		super();
	}

	public SegmentActionTypeType(Integer segmentActionTypeTypeId) {
		super();
		this.segmentActionTypeTypeId = segmentActionTypeTypeId;
	}

	public SegmentActionTypeType(Integer segmentActionTypeTypeId, String segmentActionTypeCode,
			String segmentActionTypeDescription) {
		super();
		this.segmentActionTypeTypeId = segmentActionTypeTypeId;
		this.segmentActionTypeCode = segmentActionTypeCode;
		this.segmentActionTypeDescription = segmentActionTypeDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getSegmentActionTypeCode() {
		return segmentActionTypeCode;
	}

	public void setSegmentActionTypeCode(String segmentActionTypeCode) {
		this.segmentActionTypeCode = segmentActionTypeCode;
	}

	public String getSegmentActionTypeDescription() {
		return segmentActionTypeDescription;
	}

	public void setSegmentActionTypeDescription(String segmentActionTypeDescription) {
		this.segmentActionTypeDescription = segmentActionTypeDescription;
	}

	public Integer getSegmentActionTypeTypeId() {
		return segmentActionTypeTypeId;
	}

	public void setSegmentActionTypeTypeId(Integer segmentActionTypeTypeId) {
		this.segmentActionTypeTypeId = segmentActionTypeTypeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((segmentActionTypeCode == null) ? 0 : segmentActionTypeCode.hashCode());
		result = prime * result
				+ ((segmentActionTypeDescription == null) ? 0 : segmentActionTypeDescription.hashCode());
		result = prime * result + ((segmentActionTypeTypeId == null) ? 0 : segmentActionTypeTypeId.hashCode());
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
		SegmentActionTypeType other = (SegmentActionTypeType) obj;
		if (segmentActionTypeCode == null) {
			if (other.segmentActionTypeCode != null)
				return false;
		} else if (!segmentActionTypeCode.equals(other.segmentActionTypeCode))
			return false;
		if (segmentActionTypeDescription == null) {
			if (other.segmentActionTypeDescription != null)
				return false;
		} else if (!segmentActionTypeDescription.equals(other.segmentActionTypeDescription))
			return false;
		if (segmentActionTypeTypeId == null) {
			if (other.segmentActionTypeTypeId != null)
				return false;
		} else if (!segmentActionTypeTypeId.equals(other.segmentActionTypeTypeId))
			return false;
		return true;
	}

}
