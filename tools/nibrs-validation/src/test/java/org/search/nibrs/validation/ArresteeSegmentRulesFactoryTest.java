package org.search.nibrs.validation;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBIncidentReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.TypeOfArrestCode;
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
		ArresteeSegment arresteeSegment = buildBaseGroupBSegment();
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
	public void testRule601ForArrestDate() {
		Rule<ArresteeSegment> rule = groupARulesFactory.getRuleX01ForArrestDate();
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		arresteeSegment.setArrestDate(null);
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._601, nibrsError.getNIBRSErrorCode());
		assertEquals("42", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.JANUARY, 1);
		arresteeSegment.setArrestDate(c.getTime());
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRuleX05() {
		Rule<ArresteeSegment> rule = groupARulesFactory.getRuleX05();
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		arresteeSegment.setArrestDate(null);
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		LocalDate arrestDate = LocalDate.of(2016, 2, 15);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		GroupAIncidentReport incident = (GroupAIncidentReport) arresteeSegment.getParentReport();
		incident.setYearOfTape(2016);
		incident.setMonthOfTape(2);
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		incident.setMonthOfTape(1);
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._605, nibrsError.getNIBRSErrorCode());
		assertEquals("42", nibrsError.getDataElementIdentifier());
		assertEquals(arresteeSegment.getArrestDate(), nibrsError.getValue());
		arrestDate = LocalDate.of(2016, 1, 31);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arrestDate = LocalDate.of(2016, 1, 1);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arrestDate = LocalDate.of(2015, 12, 31);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		rule = groupBRulesFactory.getRuleX05();
		arresteeSegment = buildBaseGroupBSegment();
		GroupBIncidentReport groupBArrestReport = (GroupBIncidentReport) arresteeSegment.getParentReport();
		groupBArrestReport.setYearOfTape(2016);
		groupBArrestReport.setMonthOfTape(1);
		arrestDate = LocalDate.of(2016, 1, 31);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arrestDate = LocalDate.of(2016, 2, 1);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		arresteeSegment.setArrestDate(null);
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule665() {
		Rule<ArresteeSegment> rule = groupARulesFactory.getRule665();
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		GroupAIncidentReport incident = (GroupAIncidentReport) arresteeSegment.getParentReport();
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		LocalDate incidentDate = LocalDate.of(2016, 1, 31);
		incident.setIncidentDate(Date.from(incidentDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		incident.setIncidentDate(null);
		LocalDate arrestDate = LocalDate.of(2016, 1, 31);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arrestDate = LocalDate.of(2016, 1, 21);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		incident.setIncidentDate(Date.from(incidentDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arrestDate = LocalDate.of(2016, 2, 1);
		arresteeSegment.setArrestDate(Date.from(arrestDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._665, nibrsError.getNIBRSErrorCode());
		assertEquals("42", nibrsError.getDataElementIdentifier());
		assertEquals(arresteeSegment.getArrestDate(), nibrsError.getValue());
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
	
	@Test
	public void testRuleX01ForTypeOfArrest() {
		Rule<ArresteeSegment> rule = groupARulesFactory.getRuleX01ForTypeOfArrest();
		ArresteeSegment arresteeSegment = buildBaseGroupASegment();
		arresteeSegment.setTypeOfArrest(TypeOfArrestCode.O.code);
		NIBRSError nibrsError = rule.apply(arresteeSegment);
		assertNull(nibrsError);
		arresteeSegment.setTypeOfArrest(null);
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._601, nibrsError.getNIBRSErrorCode());
		assertEquals("43", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		arresteeSegment.setTypeOfArrest("invalid");
		nibrsError = rule.apply(arresteeSegment);
		assertNotNull(nibrsError);
		assertEquals("invalid", nibrsError.getValue());
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

	private ArresteeSegment buildBaseGroupBSegment() {
		GroupBIncidentReport report = new GroupBIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);
		ArresteeSegment s = new ArresteeSegment();
		report.addArrestee(s);
		return s;
	}

}
