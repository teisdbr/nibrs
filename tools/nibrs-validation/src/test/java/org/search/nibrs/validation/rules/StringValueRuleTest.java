package org.search.nibrs.validation.rules;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;

public class StringValueRuleTest {
	
	public class TestSubject {
		
		private String valueToTest;

		public TestSubject(String valueToTest) {
			this.valueToTest = valueToTest;
		}
		
		public String getValueToTest() {
			return valueToTest;
		}

	}
	
	@Test
	public void test() throws Exception {
		
		StringValueRule<TestSubject> rule = new StringValueRule<TestSubject>(
				subject -> {return subject.getValueToTest();},
				(value, reportSource) -> {return "A".equals(value) ? null : new NIBRSError();});
		
		assertNull(rule.apply(new TestSubject("A"), null));
		assertNotNull(rule.apply(new TestSubject("B"), null));
		
	}
	
	@Test
	public void demonstrateNullTest() throws Exception {
		
		StringValueRule<TestSubject> rule = new StringValueRule<TestSubject>(
				subject -> {return subject.getValueToTest();},
				(value, reportSource) -> {return value != null ? null : new NIBRSError();});
		
		assertNull(rule.apply(new TestSubject("A"), null));
		assertNotNull(rule.apply(new TestSubject(null), null));

		
	}

}
