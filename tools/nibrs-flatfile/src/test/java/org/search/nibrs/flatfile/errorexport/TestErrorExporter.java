/*
 * Copyright 2016 Research Triangle Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.flatfile.errorexport;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.flatfile.NIBRSAgeBuilder;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfPropertyLossCode;

public class TestErrorExporter {
	
	private static final Logger LOG = LogManager.getLogger(TestErrorExporter.class);
	
	private ErrorExporter errorExporter;
	private List<NIBRSError> errorList;
	
	@Before
	public void setUp() throws Exception {
		
		errorExporter = ErrorExporter.getInstance();
		errorList = new ArrayList<>();
		
		// Note: we just need this incident to include the values necessary for producing the report.  There is no
		// intent that the incident actually fail any edits...
		GroupAIncidentReport incident = getBaselineIncident();
		
		NIBRSError e = new NIBRSError();
		e.setContext(makeReportSource(123));
		e.setDataElementIdentifier("5");
		e.setNIBRSErrorCode(NIBRSErrorCode._001);
		e.setReportUniqueIdentifier(incident.getIncidentNumber());
		e.setSegmentType('1');
		e.setValue("ABC");
		e.setWithinSegmentIdentifier(null);
		e.setReport(incident);
		errorList.add(e);
		
		e = new NIBRSError();
		e.setContext(makeReportSource(124));
		e.setDataElementIdentifier("7");
		e.setNIBRSErrorCode(NIBRSErrorCode._206);
		e.setReportUniqueIdentifier(incident.getIncidentNumber());
		e.setSegmentType('2');
		e.setValue("ABC");
		e.setWithinSegmentIdentifier(OffenseCode._09A.code);
		e.setReport(incident);
		errorList.add(e);
		
		e = new NIBRSError();
		e.setContext(makeReportSource(125));
		e.setDataElementIdentifier("15");
		e.setNIBRSErrorCode(NIBRSErrorCode._320);
		e.setReportUniqueIdentifier(incident.getIncidentNumber());
		e.setSegmentType('3');
		e.setValue(14);
		e.setWithinSegmentIdentifier(TypeOfPropertyLossCode._1.code);
		e.setReport(incident);
		errorList.add(e);
		
		e = new NIBRSError();
		e.setContext(makeReportSource(126));
		e.setDataElementIdentifier("25");
		e.setNIBRSErrorCode(NIBRSErrorCode._406);
		e.setReportUniqueIdentifier(incident.getIncidentNumber());
		e.setSegmentType('4');
		e.setValue(null);
		e.setWithinSegmentIdentifier(new Integer(1));
		e.setReport(incident);
		errorList.add(e);
		
		e = new NIBRSError();
		e.setContext(makeReportSource(127));
		e.setDataElementIdentifier("37");
		e.setNIBRSErrorCode(NIBRSErrorCode._517);
		e.setReportUniqueIdentifier(incident.getIncidentNumber());
		e.setSegmentType('5');
		e.setValue(99);
		e.setWithinSegmentIdentifier(new Integer(1));
		e.setReport(incident);
		errorList.add(e);
		
		e = new NIBRSError();
		e.setContext(makeReportSource(128));
		e.setDataElementIdentifier("43");
		e.setNIBRSErrorCode(NIBRSErrorCode._606);
		e.setReportUniqueIdentifier(incident.getIncidentNumber());
		e.setSegmentType('6');
		e.setValue("ABC");
		e.setWithinSegmentIdentifier(new Integer(3));
		e.setReport(incident);
		errorList.add(e);
		
	}
	
	private ReportSource makeReportSource(int lineNumber) {
		ReportSource ret = new ReportSource();
		ret.setSourceName(getClass().getName());
		ret.setSourceLocation(String.valueOf(lineNumber));
		return ret;
	}
	
	@Test
	public void testModifyLine() {
		String line = "         ";
		assertEquals("   ABC   ", errorExporter.modifyLine(line, 3, 6, "ABC"));
	}
	
	@Test
	public void testEmptyErrorReport() throws IOException {
		List<NIBRSError> errorList = new ArrayList<>();
		StringWriter writer = new StringWriter();
		errorExporter.createErrorReport(errorList, writer);
		String contents = writer.toString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		assertEquals(1, lines.length);
		String line = lines[0];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals(StringUtils.repeat(' ', 14) + "999999999" + StringUtils.repeat(' ', 38) + "IncidentBuilder processed submission on " +
				new SimpleDateFormat("MM/dd/yy").format(new Date()) + StringUtils.repeat(' ', 31) + StringUtils.repeat(' ', 6), line);
	}
	
	@Test
	public void testErrorReport() throws IOException {
		String contents = exportErrorListToString();
		LOG.info(System.lineSeparator() + contents);
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		assertEquals(7, lines.length);
		for(int i=0;i < lines.length - 2;i++) {
			String line = lines[i];
			assertEquals("201605", line.substring(0,  6));
			assertEquals("I", line.substring(13, 14));
			assertEquals("WA1234567", line.substring(14,  23));
			assertEquals("54236732    ", line.substring(23,  35));
		}
	}

	@Test
	public void testAdminSegmentErrorReport() throws IOException {
		String contents = exportErrorListToString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		String line = lines[0];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals("0000123", line.substring(6,  13));
		assertEquals("1", line.substring(35, 36));
		assertEquals("   ", line.substring(36, 39));
		assertEquals("   ", line.substring(39, 42));
		assertEquals(" ", line.substring(42, 43));
		assertEquals("05 ", line.substring(43, 46));
		assertEquals("ABC         ", line.substring(49, 61));
		assertErrorInfo(line, NIBRSErrorCode._001);
	}

	@Test
	public void testOffenseSegmentErrorReport() throws IOException {
		String contents = exportErrorListToString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		String line = lines[1];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals("0000124", line.substring(6,  13));
		assertEquals("2", line.substring(35, 36));
		assertEquals("09A", line.substring(36, 39));
		assertEquals("   ", line.substring(39, 42));
		assertEquals(" ", line.substring(42, 43));
		assertEquals("07 ", line.substring(43, 46));
		assertEquals("            ", line.substring(49, 61));
		assertErrorInfo(line, NIBRSErrorCode._206);
	}
	
	@Test
	public void testPropertySegmentErrorReport() throws IOException {
		String contents = exportErrorListToString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		String line = lines[2];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals("0000125", line.substring(6,  13));
		assertEquals("3", line.substring(35, 36));
		assertEquals("   ", line.substring(36, 39));
		assertEquals("   ", line.substring(39, 42));
		assertEquals("1", line.substring(42, 43));
		assertEquals("15 ", line.substring(43, 46));
		assertEquals("14          ", line.substring(49, 61));
		assertErrorInfo(line, NIBRSErrorCode._320);
	}
	
	@Test
	public void testVictimSegmentErrorReport() throws IOException {
		String contents = exportErrorListToString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		String line = lines[3];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals("0000126", line.substring(6,  13));
		assertEquals("4", line.substring(35, 36));
		assertEquals("   ", line.substring(36, 39));
		assertEquals("001", line.substring(39, 42));
		assertEquals(" ", line.substring(42, 43));
		assertEquals("25 ", line.substring(43, 46));
		assertEquals("            ", line.substring(49, 61));
		assertErrorInfo(line, NIBRSErrorCode._406);
	}
	
	@Test
	public void testOffenderSegmentErrorReport() throws IOException {
		String contents = exportErrorListToString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		String line = lines[4];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals("0000127", line.substring(6,  13));
		assertEquals("5", line.substring(35, 36));
		assertEquals("   ", line.substring(36, 39));
		assertEquals("001", line.substring(39, 42));
		assertEquals(" ", line.substring(42, 43));
		assertEquals("37 ", line.substring(43, 46));
		assertEquals("99          ", line.substring(49, 61));
		assertErrorInfo(line, NIBRSErrorCode._517);
	}
	
	@Test
	public void testArresteeSegmentErrorReport() throws IOException {
		String contents = exportErrorListToString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		String line = lines[5];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals("0000128", line.substring(6,  13));
		assertEquals("6", line.substring(35, 36));
		assertEquals("   ", line.substring(36, 39));
		assertEquals("003", line.substring(39, 42));
		assertEquals(" ", line.substring(42, 43));
		assertEquals("43 ", line.substring(43, 46));
		assertEquals("            ", line.substring(49, 61));
		assertErrorInfo(line, NIBRSErrorCode._606);
	}
	
	private GroupAIncidentReport getBaselineIncident() {
		
		GroupAIncidentReport incident = new GroupAIncidentReport();
		
		ReportSource source = new ReportSource();
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		source.setSourceLocation(trace[1].toString());
		source.setSourceName(TestErrorExporter.class.getName());
		incident.setSource(source);
		
		incident.setYearOfTape(2016);
		incident.setMonthOfTape(5);
		incident.setReportActionType('I');
		incident.setOri("WA1234567");
		incident.setIncidentNumber("54236732");
		LocalDate d = LocalDate.of(2016, 5, 12);
		ParsedObject<LocalDate> incidentDate = new ParsedObject<>();
		incidentDate.setValue(d);
		incident.setIncidentDate(incidentDate);
		incident.setExceptionalClearanceCode("A");
		incident.setExceptionalClearanceDate(incidentDate);
		incident.setCityIndicator("GAA7");
		incident.setReportDateIndicator(null);
		
		OffenseSegment o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode("13A");
		o.setTypeOfCriminalActivity(0, "J");
		o.setOffenseAttemptedCompleted("C");
		o.setTypeOfWeaponForceInvolved(0, "99");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setBiasMotivation(0, "15");
		o.setLocationType("15");
		o.setNumberOfPremisesEntered(null);
		o.setAutomaticWeaponIndicator(0, " ");
		
		VictimSegment v = new VictimSegment();
		incident.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setVictimSequenceNumber(new ParsedObject<>(1));
		v.setAge(NIBRSAgeBuilder.buildAgeFromRawString("2022", v));
		v.setEthnicity("N");
		v.setResidentStatus("R");
		v.setSex("F");
		v.setRace("B");
		v.setOffenderNumberRelated(0, new ParsedObject<>(1));
		v.setVictimOffenderRelationship(0, "SE");
		v.setUcrOffenseCodeConnection(0, "13A");
		
		OffenderSegment offender = new OffenderSegment();
		incident.addOffender(offender);
		offender.setOffenderSequenceNumber(new ParsedObject<>(1));
		offender.setAge(NIBRSAgeBuilder.buildAgeFromRawString("22", v));
		offender.setRace("W");
		offender.setSex("M");
		offender.setEthnicity("H");
		
		ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		incident.addArrestee(arrestee);
		arrestee.setArresteeSequenceNumber(new ParsedObject<>(1));
		arrestee.setArrestTransactionNumber("12345");
		arrestee.setArrestDate(new ParsedObject<>(LocalDate.of(2015, 5, 16)));
		arrestee.setTypeOfArrest("O");
		arrestee.setMultipleArresteeSegmentsIndicator("N");
		arrestee.setUcrArrestOffenseCode("13A");
		arrestee.setArresteeArmedWith(0,"01");
		arrestee.setAge(NIBRSAgeBuilder.buildAgeFromRawString("22", v));
		arrestee.setSex("M");
		arrestee.setRace("W");
		arrestee.setEthnicity("U");
		arrestee.setResidentStatus("R");
		
		PropertySegment property = new PropertySegment();
		incident.addProperty(property);
		property.setTypeOfPropertyLoss(TypeOfPropertyLossCode._1.code);
				
		return incident;
		
	}
	
	private String exportErrorListToString() throws IOException {
		StringWriter writer = new StringWriter();
		errorExporter.createErrorReport(errorList, writer);
		String ret = writer.toString();
		writer.close();
		return ret;
	}
	
	private void assertErrorInfo(String line, NIBRSErrorCode expectedErrorCode) {
		assertEquals(expectedErrorCode.getCode(), line.substring(46, 49));
		assertEquals(expectedErrorCode.message + StringUtils.repeat(" ", 79 - expectedErrorCode.message.length()), line.substring(61, 140));
	}
	
}
