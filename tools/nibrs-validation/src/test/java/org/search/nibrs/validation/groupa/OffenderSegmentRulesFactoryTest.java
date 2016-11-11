package org.search.nibrs.validation.groupa;

import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.rules.Rule;

public class OffenderSegmentRulesFactoryTest {
	
	private static final Logger LOG = LogManager.getLogger(OffenderSegmentRulesFactoryTest.class);
	
	private OffenderSegmentRulesFactory rulesFactory = new OffenderSegmentRulesFactory();
	
	@Test
	public void testRule501() {
		Rule<OffenderSegment> rule = rulesFactory.getRule501();
		OffenderSegment os = buildBaseSegment();
		os.setOffenderSequenceNumber(null);
		NIBRSError nibrsError = rule.apply(os);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._501, nibrsError.getNIBRSErrorCode());
		assertEquals("36", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		os.setOffenderSequenceNumber(1000);
		nibrsError = rule.apply(os);
		assertNotNull(nibrsError);
	}

	@Test
	public void testRule557() {
		Rule<OffenderSegment> rule = rulesFactory.getRule557();
		OffenderSegment os = buildBaseSegment();
		os.setOffenderSequenceNumber(0);
		GroupAIncidentReport incident = (GroupAIncidentReport) os.getParentReport();
		incident.setExceptionalClearanceCode(ClearedExceptionallyCode.A.code);
		NIBRSError nibrsError = rule.apply(os);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._557, nibrsError.getNIBRSErrorCode());
		assertEquals("36", nibrsError.getDataElementIdentifier());
		assertEquals(0, nibrsError.getValue());
	}
	
	
	@Test
	public void testRule504ForAgeOfOffender() {
		Rule<OffenderSegment> rule = rulesFactory.getRule504ForAgeOfOffender();
		OffenderSegment os = buildBaseSegment();
		NIBRSError nibrsError = rule.apply(os);
		assertNull(nibrsError);
	}

	private OffenderSegment buildBaseSegment() {
		GroupAIncidentReport report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);
		OffenderSegment o = new OffenderSegment();
		report.addOffender(o);
		return o;
	}

}
