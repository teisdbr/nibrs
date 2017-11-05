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
public class TypePropertyLossEtcType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer typePropertyLossEtcTypeId; 
	
	private String typePropertyLossEtcCode; 
	private String typePropertyLossEtcDescription; 
	
	public TypePropertyLossEtcType() {
		super();
	}

	public TypePropertyLossEtcType(Integer typePropertyLossEtcTypeId) {
		super();
		this.typePropertyLossEtcTypeId = typePropertyLossEtcTypeId;
	}

	public TypePropertyLossEtcType(Integer typePropertyLossEtcTypeId, String typePropertyLossEtcCode,
			String typePropertyLossEtcDescription) {
		super();
		this.typePropertyLossEtcTypeId = typePropertyLossEtcTypeId;
		this.typePropertyLossEtcCode = typePropertyLossEtcCode;
		this.typePropertyLossEtcDescription = typePropertyLossEtcDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getTypePropertyLossEtcTypeId() {
		return typePropertyLossEtcTypeId;
	}

	public void setTypePropertyLossEtcTypeId(Integer typePropertyLossEtcTypeId) {
		this.typePropertyLossEtcTypeId = typePropertyLossEtcTypeId;
	}

	public String getTypePropertyLossEtcCode() {
		return typePropertyLossEtcCode;
	}

	public void setTypePropertyLossEtcCode(String typePropertyLossEtcCode) {
		this.typePropertyLossEtcCode = typePropertyLossEtcCode;
	}

	public String getTypePropertyLossEtcDescription() {
		return typePropertyLossEtcDescription;
	}

	public void setTypePropertyLossEtcDescription(String typePropertyLossEtcDescription) {
		this.typePropertyLossEtcDescription = typePropertyLossEtcDescription;
	}

}
