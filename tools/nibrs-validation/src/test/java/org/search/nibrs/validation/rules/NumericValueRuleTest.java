package org.search.nibrs.validation.rules;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

public class NumericValueRuleTest {
	
	public static final class TestSubject implements ValidationTarget {
		
		private Number valueToTest;

		public TestSubject(Number valueToTest) {
			this.valueToTest = valueToTest;
		}
		
		public Number getValueToTest() {
			return valueToTest;
		}

		@Override
		public NIBRSError getErrorTemplate() {
			return new NIBRSError();
		}

	}
	
	@Test
	public void equalityTest() throws Exception {
		
		NumericValueRule<TestSubject> equalityRule = new NumericValueRule<TestSubject>(
				subject -> {return subject.getValueToTest();},
				(value, subject) -> {return value.equals(10) ? null : new NIBRSError();});
		
		assertNull(equalityRule.apply(new TestSubject(10)));
		assertNotNull(equalityRule.apply(new TestSubject(20)));
		
	}

	@Test
	public void rangeTest() throws Exception {
		
		NumericValueRule<TestSubject> rangeRule = new NumericValueRule<TestSubject>(
				subject -> {return subject.getValueToTest();},
				(value, subject) -> {return value == null || (value.intValue() > 8 && value.intValue() < 12) ? null : new NIBRSError();});
		
		assertNull(rangeRule.apply(new TestSubject(10)));
		assertNull(rangeRule.apply(new TestSubject(null)));
		assertNotNull(rangeRule.apply(new TestSubject(20)));
		
	}

}
