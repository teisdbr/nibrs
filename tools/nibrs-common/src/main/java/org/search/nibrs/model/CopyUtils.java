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
