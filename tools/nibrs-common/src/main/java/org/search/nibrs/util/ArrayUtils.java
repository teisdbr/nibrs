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

import java.util.Arrays;

/**
 * Utilities class for handling common XML tasks.
 *
 */
public class ArrayUtils {
	
	public static final boolean notAllNull(Object[] array) {
		return !allNull(array);
	}
	
	public static final boolean allNull(Object[] array) {
		
		return CollectionUtils.allNull(Arrays.asList(array));
	}
	
	public static final boolean allMissing(Object[] array) {
		
		return CollectionUtils.allMissing(Arrays.asList(array));
	}
	
}

