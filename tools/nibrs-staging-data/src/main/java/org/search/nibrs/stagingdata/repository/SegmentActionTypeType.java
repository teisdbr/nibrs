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
public class SegmentActionTypeType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer segmentActionTypeTypeId; 
	
	private String segmentActionTypeCode; 
	private String segmentActionTypeDescription; 
	
	public SegmentActionTypeType() {
		super();
	}

	public SegmentActionTypeType(Integer segmentActionTypeTypeId, String segmentActionTypeCode,
			String segmentActionTypeDescription) {
		super();
		this.segmentActionTypeTypeId = segmentActionTypeTypeId;
		this.segmentActionTypeCode = segmentActionTypeCode;
		this.segmentActionTypeDescription = segmentActionTypeDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
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

}
