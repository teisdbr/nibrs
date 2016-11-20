package org.search.nibrs.validation.rules;

import java.util.regex.Pattern;

import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.Identifiable;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * Rule for Incident Numbers and Arrest Transaction Numbers, requiring left-justified, blank-right-fill values.
 */
public class BlankRightFillStringRule<T extends Identifiable & ValidationTarget> extends AbstractIdentifierRegexRule<T> {
	
	static final Pattern REGEX_PATTERN = Pattern.compile("^[^ ]+[ ]*$");

	public BlankRightFillStringRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		super(REGEX_PATTERN, dataElementIdentifier, nibrsErrorCode);
	}

}
