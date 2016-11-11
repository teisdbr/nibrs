package org.search.nibrs.validation.groupa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.EthnicityCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.RaceCode;
import org.search.nibrs.model.codes.SexCode;
import org.search.nibrs.model.codes.TypeOfVictimCode;
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
	
	@Test
	public void testRule504ForSexOfOffender() {
		
		Rule<OffenderSegment> rule = rulesFactory.getRule504ForSexOfOffender();

		OffenderSegment offenderSegment = buildBaseSegment();
		offenderSegment.setSex(null);
		NIBRSError nibrsError = rule.apply(offenderSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._504, nibrsError.getNIBRSErrorCode());
		assertEquals("38", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		
		offenderSegment.setSex("invalid");
		nibrsError = rule.apply(offenderSegment);
		assertNotNull(nibrsError);
		assertEquals("invalid", nibrsError.getValue());
		
		offenderSegment.setSex(SexCode.F.code);
		nibrsError = rule.apply(offenderSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule404ForRaceOfOffender() {
		
		Rule<OffenderSegment> rule = rulesFactory.getRule504ForRaceOfOffender();
	
		OffenderSegment offenderSegment = buildBaseSegment();
		offenderSegment.setRace(null);
		NIBRSError nibrsError = rule.apply(offenderSegment);
		assertNotNull(nibrsError);	
		assertEquals(NIBRSErrorCode._504, nibrsError.getNIBRSErrorCode());
		assertEquals("39", nibrsError.getDataElementIdentifier());
		assertEquals(null, nibrsError.getValue());
		
		offenderSegment.setRace("invalid");
		nibrsError = rule.apply(offenderSegment);
		assertNotNull(nibrsError);
		assertEquals("invalid", nibrsError.getValue());
		
		offenderSegment.setRace(RaceCode.A.code);
		nibrsError = rule.apply(offenderSegment);
		assertNull(nibrsError);
		
	}
	
	@Test
	public void testRule504ForEthnicityOfOffender() {
		
		Rule<OffenderSegment> rule = rulesFactory.getRule504ForEthnicityOfOffender();
	
		OffenderSegment offenderSegment = buildBaseSegment();
		offenderSegment.setEthnicity(null);
		NIBRSError nibrsError = rule.apply(offenderSegment);
		assertNull(nibrsError);

		offenderSegment.setEthnicity("invalid");
		nibrsError = rule.apply(offenderSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._504, nibrsError.getNIBRSErrorCode());
		assertEquals("39A", nibrsError.getDataElementIdentifier());
		assertEquals("invalid", nibrsError.getValue());
		
		offenderSegment.setEthnicity(EthnicityCode.H.code);
		nibrsError = rule.apply(offenderSegment);
		assertNull(nibrsError);
		
	}
	
	@Test
	public void testRule510() {

		Rule<OffenderSegment> rule = rulesFactory.getRule510();

		OffenderSegment offenderSegment = buildBaseSegment();
		offenderSegment.setAgeString("3020");
		NIBRSError nibrsError = rule.apply(offenderSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._510, nibrsError.getNIBRSErrorCode());
		assertEquals("37", nibrsError.getDataElementIdentifier());
		assertEquals(offenderSegment.getAge(), nibrsError.getValue());

		offenderSegment.setAgeString("2030");
		nibrsError = rule.apply(offenderSegment);
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
