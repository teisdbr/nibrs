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
