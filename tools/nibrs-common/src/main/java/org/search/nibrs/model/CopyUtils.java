/*******************************************************************************
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

final class CopyUtils {
	
	@SuppressWarnings("unchecked")
	public static final <T> T[] copyArray(T[] t) {
		if (t == null) {
			return null;
		}
		T[] ret = (T[]) Array.newInstance(t.getClass().getComponentType(), t.length);
		for (int i=0;i < ret.length;i++) {
			ret[i] = t[i];
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static final <T> List<T> copyList(List<T> list) {
		if (list == null) {
			return null;
		}
		List<T> ret = new ArrayList<>();
		for (T c : list) {
			T copy = null;
			try {
				copy = (T) c.getClass().getDeclaredConstructor(c.getClass()).newInstance(c);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			ret.add(copy);
		}
		return ret;
	}
	
}
