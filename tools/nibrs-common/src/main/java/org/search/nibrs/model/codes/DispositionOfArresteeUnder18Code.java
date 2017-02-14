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
package org.search.nibrs.model.codes;

import java.util.HashSet;
import java.util.Set;

/**
 * Code list enum for Disposition of Arrestee Under 18 (data element 52)
 */
public enum DispositionOfArresteeUnder18Code {
	
	H("H","Handled Within Department"), 
	R("R","Referred to Other Authorities");
	
	private DispositionOfArresteeUnder18Code(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;

	public static Set<String> codeSet() {
		Set<String> codeSet = new HashSet<String>();
		for (DispositionOfArresteeUnder18Code code : values()) {
			codeSet.add(code.code);
		}
		return codeSet;
	}

}
