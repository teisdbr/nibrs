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
public class DispositionOfArresteeUnder18Type {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer dispositionOfArresteeUnder18TypeId; 
	
	private String dispositionOfArresteeUnder18Code; 
	private String dispositionOfArresteeUnder18Description; 
	
	public DispositionOfArresteeUnder18Type() {
		super();
	}

	public DispositionOfArresteeUnder18Type(Integer dispositionOfArresteeUnder18TypeId) {
		super();
		this.dispositionOfArresteeUnder18TypeId = dispositionOfArresteeUnder18TypeId;
	}

	public DispositionOfArresteeUnder18Type(Integer dispositionOfArresteeUnder18TypeId,
			String dispositionOfArresteeUnder18Code, String dispositionOfArresteeUnder18Description) {
		super();
		this.dispositionOfArresteeUnder18TypeId = dispositionOfArresteeUnder18TypeId;
		this.dispositionOfArresteeUnder18Code = dispositionOfArresteeUnder18Code;
		this.dispositionOfArresteeUnder18Description = dispositionOfArresteeUnder18Description;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getDispositionOfArresteeUnder18TypeId() {
		return dispositionOfArresteeUnder18TypeId;
	}

	public void setDispositionOfArresteeUnder18TypeId(Integer dispositionOfArresteeUnder18TypeId) {
		this.dispositionOfArresteeUnder18TypeId = dispositionOfArresteeUnder18TypeId;
	}

	public String getDispositionOfArresteeUnder18Code() {
		return dispositionOfArresteeUnder18Code;
	}

	public void setDispositionOfArresteeUnder18Code(String dispositionOfArresteeUnder18Code) {
		this.dispositionOfArresteeUnder18Code = dispositionOfArresteeUnder18Code;
	}

	public String getDispositionOfArresteeUnder18Description() {
		return dispositionOfArresteeUnder18Description;
	}

	public void setDispositionOfArresteeUnder18Description(String dispositionOfArresteeUnder18Description) {
		this.dispositionOfArresteeUnder18Description = dispositionOfArresteeUnder18Description;
	}
}
