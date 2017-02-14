/*
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.validation.rules;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class ValidValueListRuleTest {
	
	public static final class TestSubject implements ValidationTarget {
		private String s;
		private String[] ss;
		public String[] getSs() {
			return ss;
		}
		public void setSs(String[] ss) {
			this.ss = ss;
		}
		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}
		@Override
		public NIBRSError getErrorTemplate() {
			return new NIBRSError();
		}
	}
	
	private Set<String> allowedValueSet = new HashSet<>();
	
	@Before
	public void setUp() {
		allowedValueSet.add("A");
		allowedValueSet.add("B");
	}
	
	
	@Test
	public void testSingleValue() {
		ValidValueListRule<TestSubject> rule = new ValidValueListRule<TestSubject>("s", "", TestSubject.class, NIBRSErrorCode._101, allowedValueSet);
		TestSubject subject = new TestSubject();
		subject.setS("A");
		assertNull(rule.apply(subject));
		subject.setS("X");
		assertNotNull(rule.apply(subject));
		subject.setS(null);
		assertNull(rule.apply(subject));
		rule = new ValidValueListRule<TestSubject>("s", "", TestSubject.class, NIBRSErrorCode._101, allowedValueSet, false);
		subject.setS(null);
		assertNotNull(rule.apply(subject));
	}
	
	@Test
	public void testArrayValue() {
		ValidValueListRule<TestSubject> rule = new ValidValueListRule<TestSubject>("ss", "", TestSubject.class, NIBRSErrorCode._101, allowedValueSet);
		TestSubject subject = new TestSubject();
		subject.setSs(new String[] {"A"});
		assertNull(rule.apply(subject));
		subject.setSs(new String[] {"A","B"});
		assertNull(rule.apply(subject));
		subject.setSs(new String[] {"A",null});
		assertNull(rule.apply(subject));
		subject.setSs(new String[] {null,null});
		assertNull(rule.apply(subject));
		subject.setSs(new String[0]);
		assertNull(rule.apply(subject));
		subject.setSs(null);
		assertNull(rule.apply(subject));
		subject.setSs(new String[] {"A","X"});
		assertNotNull(rule.apply(subject));
		rule = new ValidValueListRule<TestSubject>("ss", "", TestSubject.class, NIBRSErrorCode._101, allowedValueSet, false);
		subject.setSs(new String[] {"A",null});
		assertNull(rule.apply(subject));
		subject.setSs(new String[] {null,null});
		assertNotNull(rule.apply(subject));
		subject.setSs(null);
		assertNotNull(rule.apply(subject));
	}
	
}
