package org.search.nibrs.validation.rules;

import java.util.regex.Pattern;

import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.Identifiable;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class ValidNIBRSIdentifierFormatRule<T extends Identifiable & ValidationTarget> extends AbstractIdentifierRegexRule<T> {
	
	static final Pattern REGEX_PATTERN = Pattern.compile("[A-Z0-9\\-]+[ ]*");

	public ValidNIBRSIdentifierFormatRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		super(REGEX_PATTERN, dataElementIdentifier, nibrsErrorCode);
	}

}
