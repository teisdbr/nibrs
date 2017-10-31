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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offenseCategory1 == null) ? 0 : offenseCategory1.hashCode());
		result = prime * result + ((offenseCategory2 == null) ? 0 : offenseCategory2.hashCode());
		result = prime * result + ((offenseCategory3 == null) ? 0 : offenseCategory3.hashCode());
		result = prime * result + ((offenseCategory4 == null) ? 0 : offenseCategory4.hashCode());
		result = prime * result + ((ucrOffenseCode == null) ? 0 : ucrOffenseCode.hashCode());
		result = prime * result + ((ucrOffenseCodeDescription == null) ? 0 : ucrOffenseCodeDescription.hashCode());
		result = prime * result + ((ucrOffenseCodeTypeID == null) ? 0 : ucrOffenseCodeTypeID.hashCode());
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
		UcrOffenseCodeType other = (UcrOffenseCodeType) obj;
		if (offenseCategory1 == null) {
			if (other.offenseCategory1 != null)
				return false;
		} else if (!offenseCategory1.equals(other.offenseCategory1))
			return false;
		if (offenseCategory2 == null) {
			if (other.offenseCategory2 != null)
				return false;
		} else if (!offenseCategory2.equals(other.offenseCategory2))
			return false;
		if (offenseCategory3 == null) {
			if (other.offenseCategory3 != null)
				return false;
		} else if (!offenseCategory3.equals(other.offenseCategory3))
			return false;
		if (offenseCategory4 == null) {
			if (other.offenseCategory4 != null)
				return false;
		} else if (!offenseCategory4.equals(other.offenseCategory4))
			return false;
		if (ucrOffenseCode == null) {
			if (other.ucrOffenseCode != null)
				return false;
		} else if (!ucrOffenseCode.equals(other.ucrOffenseCode))
			return false;
		if (ucrOffenseCodeDescription == null) {
			if (other.ucrOffenseCodeDescription != null)
				return false;
		} else if (!ucrOffenseCodeDescription.equals(other.ucrOffenseCodeDescription))
			return false;
		if (ucrOffenseCodeTypeID == null) {
			if (other.ucrOffenseCodeTypeID != null)
				return false;
		} else if (!ucrOffenseCodeTypeID.equals(other.ucrOffenseCodeTypeID))
			return false;
		return true;
	}

}
