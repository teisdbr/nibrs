package org.search.nibrs.validation.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

public class ValidNIBRSIdentifierFormatRuleTest {
	
	@Test
	public void testRegex() {
		Pattern p = ValidNIBRSIdentifierFormatRule.REGEX_PATTERN;
		assertTrue(p.matcher("A").matches());
		assertTrue(p.matcher("AB").matches());
		assertTrue(p.matcher("AB1").matches());
		assertTrue(p.matcher("AB12").matches());
		assertTrue(p.matcher("AB12-").matches());
		assertTrue(p.matcher("11-123-SC").matches());
		assertTrue(p.matcher("11-123-SC   ").matches());
		assertFalse(p.matcher("a").matches());
		assertFalse(p.matcher("A B").matches());
		assertFalse(p.matcher("11+123*SC").matches());
	}

}
