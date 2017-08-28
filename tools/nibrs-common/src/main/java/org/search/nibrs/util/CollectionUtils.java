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
package org.search.nibrs.util;

import java.util.Collection;
import java.util.Objects;

import org.search.nibrs.common.ParsedObject;

/**
 * Utilities class for handling common XML tasks.
 *
 */
public class CollectionUtils {
	
	public static final boolean notAllNull(Collection<?> collection) {
		return !allNull(collection);
	}
	
	public static final boolean allNull(Collection<?> collection) {
		
		long nonNullCount = collection.stream()
				.map(item -> {
					if (item instanceof ParsedObject<?>) 
						return ((ParsedObject<?>) item).getValue(); 
					return item;
				})
				.filter(Objects::nonNull)
				.count();
		
		return nonNullCount == 0;
	}
	public static final boolean allMissing(Collection<?> collection) {
		
		long nonNullCount = collection.stream()
				.map(item -> {
					if (item instanceof ParsedObject<?> &&  
						((ParsedObject<?>) item).isMissing())
						return null; 
					return item;
				})
				.filter(Objects::nonNull)
				.count();
		
		return nonNullCount == 0;
	}
	
	
}

