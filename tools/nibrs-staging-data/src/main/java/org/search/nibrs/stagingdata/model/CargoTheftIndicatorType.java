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

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Cacheable
public class CargoTheftIndicatorType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer cargoTheftIndicatorTypeId; 
	
	private String cargoTheftIndicatorCode; 
	private String cargoTheftIndicatorDescription; 
	
	public CargoTheftIndicatorType() {
		super();
	}

	public CargoTheftIndicatorType(Integer cargoTheftIndicatorTypeId) {
		super();
		this.setCargoTheftIndicatorTypeId(cargoTheftIndicatorTypeId);
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getCargoTheftIndicatorTypeId() {
		return cargoTheftIndicatorTypeId;
	}

	public void setCargoTheftIndicatorTypeId(Integer cargoTheftIndicatorTypeId) {
		this.cargoTheftIndicatorTypeId = cargoTheftIndicatorTypeId;
	}

	public String getCargoTheftIndicatorCode() {
		return cargoTheftIndicatorCode;
	}

	public void setCargoTheftIndicatorCode(String cargoTheftIndicatorCode) {
		this.cargoTheftIndicatorCode = cargoTheftIndicatorCode;
	}

	public String getCargoTheftIndicatorDescription() {
		return cargoTheftIndicatorDescription;
	}

	public void setCargoTheftIndicatorDescription(String cargoTheftIndicatorDescription) {
		this.cargoTheftIndicatorDescription = cargoTheftIndicatorDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cargoTheftIndicatorCode == null) ? 0 : cargoTheftIndicatorCode.hashCode());
		result = prime * result
				+ ((cargoTheftIndicatorDescription == null) ? 0 : cargoTheftIndicatorDescription.hashCode());
		result = prime * result + ((cargoTheftIndicatorTypeId == null) ? 0 : cargoTheftIndicatorTypeId.hashCode());
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
		CargoTheftIndicatorType other = (CargoTheftIndicatorType) obj;
		if (cargoTheftIndicatorCode == null) {
			if (other.cargoTheftIndicatorCode != null)
				return false;
		} else if (!cargoTheftIndicatorCode.equals(other.cargoTheftIndicatorCode))
			return false;
		if (cargoTheftIndicatorDescription == null) {
			if (other.cargoTheftIndicatorDescription != null)
				return false;
		} else if (!cargoTheftIndicatorDescription.equals(other.cargoTheftIndicatorDescription))
			return false;
		if (cargoTheftIndicatorTypeId == null) {
			if (other.cargoTheftIndicatorTypeId != null)
				return false;
		} else if (!cargoTheftIndicatorTypeId.equals(other.cargoTheftIndicatorTypeId))
			return false;
		return true;
	}

}
