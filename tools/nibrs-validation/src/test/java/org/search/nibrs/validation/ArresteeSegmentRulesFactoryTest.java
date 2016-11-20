package org.search.nibrs.validation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.rules.Rule;

public class ArresteeSegmentRulesFactoryTest {
	
	private ArresteeSegmentRulesFactory groupARulesFactory = ArresteeSegmentRulesFactory.instance(ArresteeSegmentRulesFactory.GROUP_A_ARRESTEE_MODE);
	private ArresteeSegmentRulesFactory groupBRulesFactory = ArresteeSegmentRulesFactory.instance(ArresteeSegmentRulesFactory.GROUP_B_ARRESTEE_MODE);
	
	@Test
	public void testRule601ForSequenceNumber() {
		Rule<ArresteeSegment> rule = groupARulesFactory.getRuleX01ForSequenceNumber();
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		arresteeSegment.setArresteeSequenceNumber(null);
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._601, nibrsError.getNIBRSErrorCode());
		assertEquals("40", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		arresteeSegment.setArresteeSequenceNumber(0);
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(0, nibrsError.getValue());
		arresteeSegment.setArresteeSequenceNumber(100);
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		arresteeSegment.setArresteeSequenceNumber(1);
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule701ForSequenceNumber() {
		Rule<ArresteeSegment> rule = groupBRulesFactory.getRuleX01ForSequenceNumber();
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		arresteeSegment.setArresteeSequenceNumber(null);
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._701, nibrsError.getNIBRSErrorCode());
		// don't need to test the rest...it's tested in the Group A version
	}
	
	@Test
	public void testRule601ForArrestTransactionNumber() {
		Rule<ArresteeSegment> rule = groupARulesFactory.getRuleX01ForArrestTransactionNumber();
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		arresteeSegment.setArrestTransactionNumber(null);
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._601, nibrsError.getNIBRSErrorCode());
		assertEquals("41", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		arresteeSegment.setArrestTransactionNumber("AB123456789");
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule615() {
		atnFormatRuleTest(groupARulesFactory.getRuleX15(), NIBRSErrorCode._615);
	}

	@Test
	public void testRule617() {
		atnFormatRuleTest(groupARulesFactory.getRuleX17(), NIBRSErrorCode._617);
	}

	private void atnFormatRuleTest(Rule<ArresteeSegment> rule, NIBRSErrorCode errorCode) {
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		arresteeSegment.setArrestTransactionNumber(null);
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arresteeSegment.setArrestTransactionNumber("11-123-SC   ");
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arresteeSegment.setArrestTransactionNumber("11-123*SC   ");
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(errorCode, nibrsError.getNIBRSErrorCode());
		assertEquals("41", nibrsError.getDataElementIdentifier());
		arresteeSegment.setArrestTransactionNumber("11-123-SC");
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
	}
	
	private ArresteeSegment buildBaseGroupASegment() {
		GroupAIncidentReport report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);
		ArresteeSegment s = new ArresteeSegment();
		report.addArrestee(s);
		return s;
	}

}
