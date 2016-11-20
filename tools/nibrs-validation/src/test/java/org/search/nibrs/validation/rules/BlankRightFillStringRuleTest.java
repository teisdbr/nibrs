package org.search.nibrs.validation.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

public class BlankRightFillStringRuleTest {
	
	@Test
	public void testRegex() {
		Pattern p = BlankRightFillStringRule.REGEX_PATTERN;
		assertTrue(p.matcher("A           ").matches());
		assertTrue(p.matcher("AB          ").matches());
		assertTrue(p.matcher("ABCCCCCCCCCC").matches());
		assertFalse(p.matcher("").matches());
		assertFalse(p.matcher("           ").matches());
	}

}
