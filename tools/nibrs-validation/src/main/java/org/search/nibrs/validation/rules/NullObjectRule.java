package org.search.nibrs.validation.rules;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

/**
 * A null object (default) implementation of a rule that never returns an error.
 *
 * @param <T>
 */
public class NullObjectRule<T extends ValidationTarget> implements Rule<T> {

	@Override
	public NIBRSError apply(T subject) {
		return null;
	}

}
