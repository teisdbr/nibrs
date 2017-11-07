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
package org.search.nibrs.stagingdata.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to process Group B Arrest Report.  
 *
 */
@Service
public class CodeTableService {
	SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");

	@Autowired
	public DateTypeRepository dateTypeRepository; 

	public DateType getDateType(Date date) {
		
		Optional<Date> optionalDate = Optional.ofNullable(date); 
		
		DateType dateType = getCodeTableType( 
				optionalDate.map(d -> formatter.format(d)).orElse(""), 
				dateTypeRepository::findFirstByDateMMDDYYYY, 
				DateType::new);  
		return dateType;
	}
	
	public <R> R getCodeTableType(String code,  Function<String, R> findByCodeFunction, Function<Integer, R> constructorFunction ) {
		R r = null;
		if (StringUtils.isNotBlank(code)){
			r = findByCodeFunction.apply(code);
		}
		
		if (r == null && constructorFunction != null){
			r = constructorFunction.apply(99998);
		}
		return r;
	}
	
	
}
