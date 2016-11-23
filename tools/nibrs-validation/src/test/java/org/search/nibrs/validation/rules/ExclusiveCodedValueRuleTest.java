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

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class ExclusiveCodedValueRuleTest {
	
	@Rule
	public final ExpectedException expectedException = ExpectedException.none();
	
	public static final class TestSubject implements ValidationTarget {
		private String nonArrayProperty;
		private String[] s;
		public String getNonArrayProperty() {
			return nonArrayProperty;
		}
		public void setNonArrayProperty(String nonArrayProperty) {
			this.nonArrayProperty = nonArrayProperty;
		}
		public String[] getS() {
			return s;
		}
		public void setS(String[] s) {
			this.s = s;
		}
		@Override
		public NIBRSError getErrorTemplate() {
			return new NIBRSError();
		}
	}
	
	@Test
	public void test() {
		
		Set<String> evs = new HashSet<String>();
		evs.add("X");
		ExclusiveCodedValueRule<TestSubject> rule = new ExclusiveCodedValueRule<>("s", "s", TestSubject.class, NIBRSErrorCode._001, evs);
		
		TestSubject subject = new TestSubject();
		subject.setS(null);
		assertNull(rule.apply(subject));
		subject.setS(new String[0]);
		assertNull(rule.apply(subject));
		subject.setS(new String[3]);
		assertNull(rule.apply(subject));
		subject.setS(new String[] {"A"});
		assertNull(rule.apply(subject));
		subject.setS(new String[] {"A","B"});
		assertNull(rule.apply(subject));
		subject.setS(new String[] {"X"});
		assertNull(rule.apply(subject));
		subject.setS(new String[] {"X","A"});
		NIBRSError e = rule.apply(subject);
		assertNotNull(e);
		subject.setS(new String[] {"A","X"});
		e = rule.apply(subject);
		assertNotNull(e);
		
	}
	
	@Test
	public void testNonExistentProperty() {
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage(DuplicateCodedValueRule.class.getName() + " must operate on an array bean property");
		TestSubject subject = new TestSubject();
		subject.setNonArrayProperty("x");
		new DuplicateCodedValueRule<>("nonArrayProperty", "nonArrayProperty", TestSubject.class, NIBRSErrorCode._101).apply(subject);
	}
	


}
