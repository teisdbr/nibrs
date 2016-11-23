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

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

public class StringValueRuleTest {
	
	public static final class TestSubject implements ValidationTarget {
		
		private String valueToTest;

		public TestSubject(String valueToTest) {
			this.valueToTest = valueToTest;
		}
		
		public String getValueToTest() {
			return valueToTest;
		}

		@Override
		public NIBRSError getErrorTemplate() {
			return new NIBRSError();
		}

	}
	
	@Test
	public void test() throws Exception {
		
		StringValueRule<TestSubject> rule = new StringValueRule<TestSubject>(
				subject -> {return subject.getValueToTest();},
				(value, subject) -> {return "A".equals(value) ? null : new NIBRSError();});
		
		assertNull(rule.apply(new TestSubject("A")));
		assertNotNull(rule.apply(new TestSubject("B")));
		
	}
	
	@Test
	public void demonstrateNullTest() throws Exception {
		
		StringValueRule<TestSubject> rule = new StringValueRule<TestSubject>(
				subject -> {return subject.getValueToTest();},
				(value, subject) -> {return value != null ? null : new NIBRSError();});
		
		assertNull(rule.apply(new TestSubject("A")));
		assertNotNull(rule.apply(new TestSubject(null)));

		
	}

}
