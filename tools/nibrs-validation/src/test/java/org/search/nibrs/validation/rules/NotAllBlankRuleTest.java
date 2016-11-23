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
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class NotAllBlankRuleTest {
	
	public static final class TestSubject implements ValidationTarget {
		private String[] s;
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
		
		TestSubject subject = new TestSubject();
		NotAllBlankRule<TestSubject> rule = new NotAllBlankRule<>("s", "dei", TestSubject.class, NIBRSErrorCode._001);
		
		NIBRSError e = rule.apply(subject);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._001, e.getNIBRSErrorCode());
		assertEquals("dei", e.getDataElementIdentifier());
		assertNull(e.getValue());
		
		subject.setS(new String[3]);
		e = rule.apply(subject);
		assertNotNull(e);
		
		subject.setS(new String[] {"a", null, null});
		e = rule.apply(subject);
		assertNull(e);
		
		subject.setS(new String[] {null, "a", null});
		e = rule.apply(subject);
		assertNull(e);
		
		subject.setS(new String[] {"x", "a", null});
		e = rule.apply(subject);
		assertNull(e);
		
	}
	
}
