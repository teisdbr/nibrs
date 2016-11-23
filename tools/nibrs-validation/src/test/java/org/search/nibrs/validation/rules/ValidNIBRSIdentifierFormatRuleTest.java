/*******************************************************************************
 * Copyright 2016 Research Triangle Institute
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
