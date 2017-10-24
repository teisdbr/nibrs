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
public class UcrOffenseCodeType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer ucrOffenseCodeTypeID; 
	private String ucrOffenseCode; 
	private String ucrOffenseCodeDescription; 
	private String offenseCategory1; 
	private String offenseCategory2; 
	private String offenseCategory3; 
	private String offenseCategory4; 
	
	public UcrOffenseCodeType() {
		super();
	}
	
	public UcrOffenseCodeType(Integer ucrOffenseCodeTypeID) {
		this();
		this.ucrOffenseCodeTypeID = ucrOffenseCodeTypeID;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getUcrOffenseCode() {
		return ucrOffenseCode;
	}

	public void setUcrOffenseCode(String ucrOffenseCode) {
		this.ucrOffenseCode = ucrOffenseCode;
	}

	public String getUcrOffenseCodeDescription() {
		return ucrOffenseCodeDescription;
	}

	public void setUcrOffenseCodeDescription(String ucrOffenseCodeDescription) {
		this.ucrOffenseCodeDescription = ucrOffenseCodeDescription;
	}

	public String getOffenseCategory1() {
		return offenseCategory1;
	}

	public void setOffenseCategory1(String offenseCategory1) {
		this.offenseCategory1 = offenseCategory1;
	}

	public String getOffenseCategory2() {
		return offenseCategory2;
	}

	public void setOffenseCategory2(String offenseCategory2) {
		this.offenseCategory2 = offenseCategory2;
	}

	public String getOffenseCategory3() {
		return offenseCategory3;
	}

	public void setOffenseCategory3(String offenseCategory3) {
		this.offenseCategory3 = offenseCategory3;
	}

	public String getOffenseCategory4() {
		return offenseCategory4;
	}

	public void setOffenseCategory4(String offenseCategory4) {
		this.offenseCategory4 = offenseCategory4;
	}

	public Integer getUcrOffenseCodeTypeID() {
		return ucrOffenseCodeTypeID;
	}

	public void setUcrOffenseCodeTypeID(Integer ucrOffenseCodeTypeID) {
		this.ucrOffenseCodeTypeID = ucrOffenseCodeTypeID;
	}

}
