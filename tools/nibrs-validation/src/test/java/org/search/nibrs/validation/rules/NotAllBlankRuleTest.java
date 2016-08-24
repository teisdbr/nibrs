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
