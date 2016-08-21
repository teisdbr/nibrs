package org.search.nibrs.validation.rules;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class NotBlankRuleTest {
	
	@Rule
	public final ExpectedException expectedException = ExpectedException.none();
	
	public static final class TestSubject implements ValidationTarget {
		private Integer v1;
		private int v2;
		private String v3;
		public Integer getV1() {
			return v1;
		}
		public void setV1(Integer v1) {
			this.v1 = v1;
		}
		public int getV2() {
			return v2;
		}
		public void setV2(int v2) {
			this.v2 = v2;
		}
		public String getV3() {
			return v3;
		}
		public void setV3(String v3) {
			this.v3 = v3;
		}
		@Override
		public NIBRSError getErrorTemplate() {
			return new NIBRSError();
		}
	}
	
	@Test
	public void test() {
		
		TestSubject subject = new TestSubject();
		
		NotBlankRule<TestSubject> rule = new NotBlankRule<>("v1", "1", TestSubject.class, NIBRSErrorCode._101);
		assertNotNull(rule.apply(subject));
		rule = new NotBlankRule<>("v3", "3", TestSubject.class, NIBRSErrorCode._101);
		assertNotNull(rule.apply(subject));
		
		rule = new NotBlankRule<>("v2", "2", TestSubject.class, NIBRSErrorCode._101);
		assertNull(rule.apply(subject));
		rule = new NotBlankRule<>("v1", "1", TestSubject.class, NIBRSErrorCode._101);
		subject.v1 = 5;
		assertNull(rule.apply(subject));
		subject.v3 = "foo";
		rule = new NotBlankRule<>("v3", "3", TestSubject.class, NIBRSErrorCode._101);
		assertNull(rule.apply(subject));
		
	}
	
	@Test
	public void testNonExistentProperty() {
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No property named v4 found");
		new NotBlankRule<>("v4", "4", TestSubject.class, NIBRSErrorCode._101);
	}

}
