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
	public void test() {
		ValidValueListRule<TestSubject> rule = new ValidValueListRule<TestSubject>("s", "", TestSubject.class, NIBRSErrorCode._101, allowedValueSet);
		TestSubject subject = new TestSubject();
		subject.setS("A");
		assertNull(rule.apply(subject));
		subject.setS("X");
		assertNotNull(rule.apply(subject));
		subject.setS(null);
		assertNull(rule.apply(subject));
	}

}
