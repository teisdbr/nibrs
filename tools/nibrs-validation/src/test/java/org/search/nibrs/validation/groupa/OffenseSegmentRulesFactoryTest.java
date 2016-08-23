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
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenderSuspectedOfUsingCode;
import org.search.nibrs.model.codes.OffenseAttemptedCompletedCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.validation.rules.Rule;

public class OffenseSegmentRulesFactoryTest {
	
	private OffenseSegmentRulesFactory rulesFactory = new OffenseSegmentRulesFactory();
	
	@Test
	public void testRule201() {
		
		Rule<OffenseSegment> rule201 = rulesFactory.getRule201ForSingleStringProperty("ucrOffenseCode", "6");
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

		rule201 = rulesFactory.getRule201ForSingleStringProperty("offenseAttemptedCompleted", "7");
		o.setOffenseAttemptedCompleted(null);
		e = rule201.apply(o);
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
