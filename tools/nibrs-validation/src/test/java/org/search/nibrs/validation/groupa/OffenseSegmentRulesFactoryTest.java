package org.search.nibrs.validation.groupa;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.BiasMotivationCode;
import org.search.nibrs.model.codes.LocationTypeCode;
import org.search.nibrs.model.codes.MethodOfEntryCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenderSuspectedOfUsingCode;
import org.search.nibrs.model.codes.OffenseAttemptedCompletedCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.validation.rules.Rule;

public class OffenseSegmentRulesFactoryTest {
	
	private OffenseSegmentRulesFactory rulesFactory = new OffenseSegmentRulesFactory();
	
	@Test
	public void testRule207() {
		
		Rule<OffenseSegment> rule207 = rulesFactory.getRule207("biasMotivation", "8A", BiasMotivationCode.noneOrUnknownValueCodeSet());
		OffenseSegment o = buildBaseSegment();
		o.setBiasMotivation(0, null);
		NIBRSError e = rule207.apply(o);
		assertNull(e);
		o.setBiasMotivation(0, BiasMotivationCode._11.code);
		e = rule207.apply(o);
		assertNull(e);
		o.setBiasMotivation(1, BiasMotivationCode._12.code);
		e = rule207.apply(o);
		assertNull(e);
		o.setBiasMotivation(2, BiasMotivationCode._88.code);
		e = rule207.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._207, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("8A", e.getDataElementIdentifier());

	}
	
	@Test
	public void testRule206() {
		
		Rule<OffenseSegment> rule206 = rulesFactory.getRule206("biasMotivation", "8A");
		OffenseSegment o = buildBaseSegment();
		o.setBiasMotivation(0, null);
		NIBRSError e = rule206.apply(o);
		assertNull(e);
		o.setBiasMotivation(0, BiasMotivationCode._11.code);
		e = rule206.apply(o);
		assertNull(e);
		o.setBiasMotivation(1, BiasMotivationCode._12.code);
		e = rule206.apply(o);
		assertNull(e);
		o.setBiasMotivation(2, BiasMotivationCode._12.code);
		e = rule206.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._206, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("8A", e.getDataElementIdentifier());

	}
	
	@Test
	public void testRule204() {
		
		Rule<OffenseSegment> rule204 = rulesFactory.getRule204ForValueList("biasMotivation", "8A", BiasMotivationCode.codeSet());
		OffenseSegment o = buildBaseSegment();
		o.setBiasMotivation(0, null);
		NIBRSError e = rule204.apply(o);
		assertNull(e);
		o.setBiasMotivation(0, BiasMotivationCode._11.code);
		e = rule204.apply(o);
		assertNull(e);
		o.setBiasMotivation(0, "XXX");
		e = rule204.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._204, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("8A", e.getDataElementIdentifier());
		
		rule204 = rulesFactory.getRule204ForValueList("methodOfEntry", "11", MethodOfEntryCode.codeSet());
		o = buildBaseSegment();
		o.setMethodOfEntry(null);
		e = rule204.apply(o);
		assertNull(e);
		o.setMethodOfEntry(MethodOfEntryCode.F.code);
		e = rule204.apply(o);
		assertNull(e);
		o.setMethodOfEntry("XXX");
		e = rule204.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._204, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("11", e.getDataElementIdentifier());

		rule204 = rulesFactory.getRule204ForValueList("locationType", "9", LocationTypeCode.codeSet());
		o = buildBaseSegment();
		o.setLocationType(null);
		e = rule204.apply(o);
		assertNull(e);
		o.setLocationType(LocationTypeCode._01.code);
		e = rule204.apply(o);
		assertNull(e);
		o.setLocationType("XXX");
		e = rule204.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._204, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("9", e.getDataElementIdentifier());

		rule204 = rulesFactory.getRule204ForPremisesEntered();
		o = buildBaseSegment();
		o.setNumberOfPremisesEntered(null);
		e = rule204.apply(o);
		assertNull(e);
		o.setNumberOfPremisesEntered(5);
		e = rule204.apply(o);
		assertNull(e);
		o.setNumberOfPremisesEntered(500);
		e = rule204.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._204, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("10", e.getDataElementIdentifier());
		assertEquals(500, e.getValue());

	}
	
	@Test
	public void testRule201ForOffendersSuspectedOfUsing() {
		
		Rule<OffenseSegment> rule201 = rulesFactory.getRule201ForOffendersSuspectedOfUsing();
		OffenseSegment o = buildBaseSegment();
		AbstractReport report = o.getParentReport();
		o.setOffendersSuspectedOfUsing(0, null);
		o.setOffendersSuspectedOfUsing(1, null);
		o.setOffendersSuspectedOfUsing(2, null);
		NIBRSError e = rule201.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._201, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("8", e.getDataElementIdentifier());
		String[] value = (String[]) e.getValue();
		assertEquals(3, value.length);
		assertEquals(report.getSource(), e.getContext());
		o.setOffendersSuspectedOfUsing(0, OffenderSuspectedOfUsingCode.A.code);
		o.setOffendersSuspectedOfUsing(1, null);
		o.setOffendersSuspectedOfUsing(2, null);
		e = rule201.apply(o);
		assertNull(e);
		o.setOffendersSuspectedOfUsing(0, "XXX");
		e = rule201.apply(o);
		assertNotNull(e);
		
	}

	@Test
	public void testRule201ForOffenseCode() {
		
		Rule<OffenseSegment> rule201 = rulesFactory.getRule201ForUCROffenseCode();
		OffenseSegment o = buildBaseSegment();
		AbstractReport report = o.getParentReport();
		o.setUcrOffenseCode(null);
		NIBRSError e = rule201.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._201, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("6", e.getDataElementIdentifier());
		assertEquals(null, e.getValue());
		assertEquals(report.getSource(), e.getContext());
		o.setUcrOffenseCode(OffenseCode._09A.code);
		e = rule201.apply(o);
		assertNull(e);
		o.setUcrOffenseCode("XXX");
		e = rule201.apply(o);
		assertNotNull(e);
		
	}

	@Test
	public void testRule201() {
			
		Rule<OffenseSegment> rule201 = rulesFactory.getRule201ForSingleStringProperty("offenseAttemptedCompleted", "7");
		OffenseSegment o = buildBaseSegment();
		AbstractReport report = o.getParentReport();
		o.setOffenseAttemptedCompleted(null);
		NIBRSError e = rule201.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._201, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("7", e.getDataElementIdentifier());
		assertEquals(null, e.getValue());
		assertEquals(report.getSource(), e.getContext());
		o.setOffenseAttemptedCompleted(OffenseAttemptedCompletedCode.A.code);
		e = rule201.apply(o);
		assertNull(e);
		
		rule201 = rulesFactory.getRule201ForSingleStringProperty("locationType", "9");
		o.setLocationType(null);
		e = rule201.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._201, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("9", e.getDataElementIdentifier());
		assertEquals(null, e.getValue());
		assertEquals(report.getSource(), e.getContext());
		o.setLocationType(LocationTypeCode._01.code);
		e = rule201.apply(o);
		assertNull(e);
		
		rule201 = rulesFactory.getRule201ForStringArrayProperty("offendersSuspectedOfUsing", "8");
		o.setOffendersSuspectedOfUsing(0, null);
		e = rule201.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._201, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("8", e.getDataElementIdentifier());
		assertTrue(stringArrayAllNull(e.getValue(), 3));
		assertEquals(report.getSource(), e.getContext());
		o.setOffendersSuspectedOfUsing(0, OffenderSuspectedOfUsingCode.N.code);
		e = rule201.apply(o);
		assertNull(e);
		
		rule201 = rulesFactory.getRule201ForStringArrayProperty("biasMotivation", "8A");
		o.setBiasMotivation(0, null);
		e = rule201.apply(o);
		assertNotNull(e);
		assertEquals(NIBRSErrorCode._201, e.getNIBRSErrorCode());
		assertEquals('2', e.getSegmentType());
		assertEquals("8A", e.getDataElementIdentifier());
		assertTrue(stringArrayAllNull(e.getValue(), 3));
		assertEquals(report.getSource(), e.getContext());
		o.setBiasMotivation(0, BiasMotivationCode._11.code);
		e = rule201.apply(o);
		assertNull(e);
		
	}
	
	private boolean stringArrayAllNull(Object o, int arrayLength) {
		boolean ret = true;
		if (o != null && o instanceof String[]) {
			String[] ss = (String[]) o;
			if (ss.length == arrayLength) {
				for (String s : ss) {
					if (s != null) {
						ret = false;
						break;
					}
				}
			}
		}
		return ret;
	}
	
	private OffenseSegment buildBaseSegment() {
		GroupAIncidentReport report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);
		OffenseSegment o = new OffenseSegment();
		report.addOffense(o);
		return o;
	}

}
