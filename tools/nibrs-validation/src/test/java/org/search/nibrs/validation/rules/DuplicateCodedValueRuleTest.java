package org.search.nibrs.validation.rules;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class DuplicateCodedValueRuleTest {
	
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
		
		DuplicateCodedValueRule<TestSubject> rule = new DuplicateCodedValueRule<>("s", "s", TestSubject.class, NIBRSErrorCode._101);
		
		TestSubject subject = new TestSubject();
		subject.setS(null);
		assertNull(rule.apply(subject));
		subject.setS(new String[0]);
		assertNull(rule.apply(subject));
		subject.setS(new String[3]);
		assertNull(rule.apply(subject));
		subject.setS(new String[] {"A"});
		assertNull(rule.apply(subject));
		subject.setS(new String[] {"A", "B"});
		assertNull(rule.apply(subject));
		subject.setS(new String[] {"A", "B", "A"});
		NIBRSError e = rule.apply(subject);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._101, e.getNIBRSErrorCode());
		assertEquals("s", e.getDataElementIdentifier());

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
