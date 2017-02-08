/*******************************************************************************
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.search.nibrs.flatfile.importer;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.search.nibrs.model.*;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.importer.DefaultReportListener;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.flatfile.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;

/**
 * Unit test suite for the IncidentBuilder class.
 *
 */
public class TestIncidentBuilderOldFormat {
	
	private static final Logger LOG = LogManager.getLogger(TestIncidentBuilderOldFormat.class);
	
	private static final String TESTDATA_OLDFORMAT =
            "00871I022003    TN006000002-000895   20020102 10N                                      \n" +
            "00632I022003    TN006000002-000895   220CN  20  N            88\n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01294I022003    TN006000002-000895   001220                           I46  FWNR                                                  \n" +
            "00455I022003    TN006000002-000895   0124  MW\n" +
            "01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                \n" +
            "00871I022003    TN006000002-003178   20020116 12N                                      \n" +
            "00632I022003    TN006000002-003178   220CN  20  N            88\n" +
            "03073I022003    TN006000002-003178   777000000005                                                                                                                                                                                                                                                                  \n" +
            "01294I022003    TN006000002-003178   001220                           I28  MWNR                                                  \n" +
            "00455I022003    TN006000002-003178   0124  MW\n" +
            "01106I022003    TN006000002-003178   0102-003178   20021230TM22001    24  MWNR                                \n" +
            "00871I022003    TN006000002-018065   20020408 10N                                      \n" +
            "00632I022003    TN006000002-018065   23DCN  20               88\n" +
            "03073I022003    TN006000002-018065   717000002600                                                                                                                                                                                                                                                                  \n" +
            "01294I022003    TN006000002-018065   00123D                           I54  FWNR                                                  \n" +
            "00455I022003    TN006000002-018065   0100  FW\n" +
            "00871I022003    TN006000002-023736   20020517 06N                                      \n" +
            "00632I022003    TN006000002-023736   220CN  20  F            88\n" +
            "03073I022003    TN006000002-023736   720000000115        22000000000                                                                                                                                                                                                                                               \n" +
            "01294I022003    TN006000002-023736   001220                           I43  MWNR                                                  \n" +
            "00455I022003    TN006000002-023736   0121  MW\n" +
            "01106I022003    TN006000002-023736   0102-023736   20021224TM22001    21  MWNN                                \n" +
            "00871I022003    TN006000002-033709   20020624 12N                                      \n" +
            "00632I022003    TN006000002-033709   23HCN  20               88\n" +
            "03073I022003    TN006000002-033709   539000001000200301037700000150020030103                                                                                                                                                                                                                                       \n" +
            "03073I022003    TN006000002-033709   704000000065        36000001400        39000001000        77000002964                                                                                                                                                                                                         \n" +
            "01294I022003    TN006000002-033709   00123H                           I46  MWNR                                                  \n" +
            "00455I022003    TN006000002-033709   00      \n" +
            "00871I022003    TN006000002-064481   20021216 07N                                      \n" +
            "00632I022003    TN006000002-064481   35ACN  22   P           88\n" +
            "03073I022003    TN006000002-064481   610                                                                                                                                                                                                E000000000100GMH000000002000DU                                             \n" +
            "01294I022003    TN006000002-064481   00135A                           S                                                          \n" +
            "00455I022003    TN006000002-064481   0115  MW\n" +
            "00455I022003    TN006000002-064481   0214  MW\n" +
            "01106I022003    TN006000002-064481   0202-064481A  20021216SN35A01    14  MWNRR                               \n" +
            "00871I022003    TN0390500111502      20021115 19N                                      \n" +
            "00632I022003    TN0390500111502      240CN  05               88\n" +
            "03073I022003    TN0390500111502      703000000001                                                                                                                                                                                   01                                                                             \n" +
            "03073I022003    TN0390500111502      50300000000120021115                                                                                                                                                                             01                                                                           \n" +
            "01294I022003    TN0390500111502      001240                           B                                                          \n" +
            "00455I022003    TN0390500111502      0100  UU\n" +
            "00871I022003    TN038010003000037    20030111 22N                                      \n" +
            "00632I022003    TN038010003000037    13ACN  20      12       88\n" +
            "00632I022003    TN038010003000037    23HCN  20               88\n" +
            "00632I022003    TN038010003000037    290CN  20               88\n" +
            "03073I022003    TN038010003000037    720000000150                                                                                                                                                                                                                                                                  \n" +
            "03073I022003    TN038010003000037    432000000100                                                                                                                                                                                                                                                                  \n" +
            "01294I022003    TN038010003000037    00113A23H290                     I20  FBNR09   N    01BG                                    \n" +
            "00455I022003    TN038010003000037    0133  MB\n" +
            "01106I022003    TN038010003000037    0100002655    20030112TN13A01    33  MBNR                                \n" +
    		"00667I022003    TN03801000000265601    20030112T90A01    33  MBNR \n" +
            "00430I022003    TN0380100000000000000012003\n";

     
	private Reader testdataReader;
	private DefaultReportListener incidentListener;

	@Before
	public void setUp() throws Exception {
		testdataReader = new BufferedReader(new StringReader(TESTDATA_OLDFORMAT));
		incidentListener = new DefaultReportListener();
		IncidentBuilder incidentBuilder = new IncidentBuilder();
		incidentBuilder.addIncidentListener(incidentListener);
		incidentBuilder.buildIncidents(testdataReader, getClass().getName());
		List<NIBRSError> errorList = incidentListener.getErrorList();
		for (NIBRSError e : errorList) {
			LOG.info(e.getRuleDescription());
		}
		assertEquals(0, errorList.size());
	}

	@Test
	public void testZeroReport() {
		ZeroReport report = incidentListener.getZeroReportList().get(0);
		assertEquals("TN0380100", report.getOri());
		assertEquals(ZeroReport.ZERO_REPORT_TYPE_IDENTIFIER, report.getAdminSegmentLevel());
		assertEquals('I', report.getReportActionType());
		assertEquals(getClass().getName(), report.getSource().getSourceName());
	}

	@Test
	public void testGroupBIncident() {
		GroupBArrestReport report = incidentListener.getGroupBIncidentList().get(0);
		assertEquals("TN0380100", report.getOri());
		assertEquals(ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER, report.getAdminSegmentLevel());
		assertEquals('I', report.getReportActionType());
		ArresteeSegment arrestee = report.getArrestees().get(0);
		assertNotNull(arrestee);
		assertEquals("0000265601", arrestee.getArrestTransactionNumber());
		assertTrue(arrestee.getArresteeSequenceNumber().isMissing());
		assertNull(arrestee.getMultipleArresteeSegmentsIndicator());
		assertEquals(DateUtils.makeDate(2003, Calendar.JANUARY, 12), arrestee.getArrestDate().getValue());
		assertEquals("T", arrestee.getTypeOfArrest());
		assertEquals("90A", arrestee.getUcrArrestOffenseCode());
		assertEquals("01", arrestee.getArresteeArmedWith(0));
		assertNull(arrestee.getAutomaticWeaponIndicator(0));
		assertNull(arrestee.getArresteeArmedWith(1));
		assertNull(arrestee.getAutomaticWeaponIndicator(1));
		assertEquals(new Integer(33), arrestee.getAge().getAgeMin());
		assertEquals("M", arrestee.getSex());
		assertEquals("B", arrestee.getRace());
		assertEquals("R", arrestee.getResidentStatus());
		assertNull(arrestee.getDispositionOfArresteeUnder18());
	}

	@Test
	public void testFirstIncident() {
		List<GroupAIncidentReport> incidentList = incidentListener.getGroupAIncidentList();
		GroupAIncidentReport incident = (GroupAIncidentReport) incidentList.get(0);
		assertNotNull(incident);
		assertEquals("02-000895", incident.getIncidentNumber());
		assertEquals(DateUtils.makeDate(2002, Calendar.JANUARY, 2), incident.getIncidentDate().getValue());
		assertEquals(new Integer(10), incident.getIncidentHour().getValue());
		assertNull(incident.getReportDateIndicator());
		assertNull(incident.getCargoTheftIndicator());
		assertEquals("N", incident.getExceptionalClearanceCode());
		assertTrue(incident.getExceptionalClearanceDate().isMissing());
		assertEquals(1, incident.getOffenseCount());
		assertEquals(1, incident.getPropertyCount());
		assertEquals(1, incident.getVictimCount());
		assertEquals(1, incident.getOffenderCount());
		assertEquals(1, incident.getArresteeCount());
		assertEquals('I', incident.getReportActionType());
		assertFalse(incident.includesLeoka());
	}

	@Test
	public void testFirstIncidentArrestee() {
		ArresteeSegment arrestee = (ArresteeSegment) ((AbstractReport) incidentListener.getGroupAIncidentList().get(0)).arresteeIterator().next();
		assertEquals(new Integer(1), arrestee.getArresteeSequenceNumber().getValue());
		assertEquals("02-000895", arrestee.getArrestTransactionNumber());
		assertEquals(DateUtils.makeDate(2002, Calendar.DECEMBER, 30), arrestee.getArrestDate().getValue());
		assertEquals("T", arrestee.getTypeOfArrest());
		assertEquals("M", arrestee.getMultipleArresteeSegmentsIndicator());
		assertEquals("220", arrestee.getUcrArrestOffenseCode());
		assertEquals("01", arrestee.getArresteeArmedWith(0));
		assertNull(arrestee.getAutomaticWeaponIndicator(0));
		assertNull(arrestee.getArresteeArmedWith(1));
		assertNull(arrestee.getAutomaticWeaponIndicator(1));
		assertEquals(new Integer(24), arrestee.getAge().getAgeMin());
		assertEquals("M", arrestee.getSex());
		assertEquals("W", arrestee.getRace());
		assertEquals("R", arrestee.getResidentStatus());
		assertNull(arrestee.getDispositionOfArresteeUnder18());

	}

	@Test
	public void testFirstIncidentOffender() {
		OffenderSegment offender = (OffenderSegment) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).offenderIterator().next();
		assertEquals(new Integer(1), offender.getOffenderSequenceNumber().getValue());
		assertEquals(new Integer(24), offender.getAge().getAgeMin());
		assertEquals("M", offender.getSex());
		assertEquals("W", offender.getRace());
	}

	@Test
	public void testFirstIncidentVictim() {
		VictimSegment victim = (VictimSegment) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).victimIterator().next();
		assertEquals(new Integer(1), victim.getVictimSequenceNumber().getValue());
		assertEquals("220", victim.getUcrOffenseCodeConnection(0));
		for (int i = 1; i < 10; i++) {
			assertNull(victim.getUcrOffenseCodeConnection(i));
		}
		assertEquals("I", victim.getTypeOfVictim());
		assertEquals(new Integer(46), victim.getAge().getAgeMin());
		assertEquals("W", victim.getRace());
		assertEquals("N", victim.getEthnicity());
		assertEquals("R", victim.getResidentStatus());
		assertNull(victim.getAggravatedAssaultHomicideCircumstances(0));
		assertNull(victim.getAggravatedAssaultHomicideCircumstances(1));
		assertNull(victim.getAdditionalJustifiableHomicideCircumstances());
		for (int i = 0; i < 5; i++) {
			assertNull(victim.getTypeOfInjury(i));
		}
		for (int i = 0; i < 10; i++) {
			assertTrue(victim.getOffenderNumberRelated(i).isMissing());
			assertNull(victim.getVictimOffenderRelationship(i));
		}
	}

	@Test
	public void testComplexIncidentVictim() {
		VictimSegment victim = (VictimSegment) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(7)).victimIterator().next();
		assertEquals("13A", victim.getUcrOffenseCodeConnection(0));
		assertEquals("23H", victim.getUcrOffenseCodeConnection(1));
		assertEquals("290", victim.getUcrOffenseCodeConnection(2));
		assertEquals("09", victim.getAggravatedAssaultHomicideCircumstances(0));
		assertEquals("N", victim.getTypeOfInjury(0));
		assertEquals(new Integer(1), victim.getOffenderNumberRelated(0).getValue());
		assertEquals("BG", victim.getVictimOffenderRelationship(0));
	}

	@Test
	public void testFirstIncidentProperty() {
		PropertySegment property = (PropertySegment) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).propertyIterator().next();
		assertEquals("7", property.getTypeOfPropertyLoss());
		assertEquals("13", property.getPropertyDescription(0));
		assertEquals(new Integer(20), property.getValueOfProperty(0).getValue());
		for (int i = 1; i < 10; i++) {
			assertNull(property.getPropertyDescription(i));
			assertTrue(property.getValueOfProperty(i).isMissing());
		}
		assertNull(property.getNumberOfStolenMotorVehicles().getValue());
		assertNull(property.getNumberOfRecoveredMotorVehicles().getValue());
		for (int i = 0; i < 3; i++) {
			assertNull(property.getSuspectedDrugType(i));
			assertNull(property.getEstimatedDrugQuantity(i));
			assertNull(property.getTypeDrugMeasurement(i));
		}
	}

	@Test
	public void testDrugIncidentProperty() {
		PropertySegment property = (PropertySegment) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(5)).propertyIterator().next();
		assertEquals("E", property.getSuspectedDrugType(0));
		assertEquals(new Double(0.1), property.getEstimatedDrugQuantity(0));
		assertEquals("GM", property.getTypeDrugMeasurement(0));
		assertEquals("H", property.getSuspectedDrugType(1));
		assertEquals(new Double(2.0), property.getEstimatedDrugQuantity(1));
		assertEquals("DU", property.getTypeDrugMeasurement(1));
	}

	@Test
	public void testMotorVehicleIncidentProperty() {
		Iterator<PropertySegment> propertyIterator = ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(6)).propertyIterator();
		PropertySegment stolenProperty = (PropertySegment) propertyIterator.next();
		PropertySegment recoveredProperty = (PropertySegment) propertyIterator.next();
		assertEquals("7", stolenProperty.getTypeOfPropertyLoss());
		assertEquals(new Integer(1), stolenProperty.getNumberOfStolenMotorVehicles().getValue());
		assertNull(stolenProperty.getNumberOfRecoveredMotorVehicles().getValue());
		assertEquals("5", recoveredProperty.getTypeOfPropertyLoss());
		assertEquals(DateUtils.makeDate(2002, Calendar.NOVEMBER, 15), recoveredProperty.getDateRecovered(0));
		assertEquals(new Integer(1), recoveredProperty.getNumberOfRecoveredMotorVehicles().getValue());
		assertNull(recoveredProperty.getNumberOfStolenMotorVehicles().getValue());
	}

	@Test
	public void testFirstIncidentOffense() {
		OffenseSegment offense = (OffenseSegment) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).offenseIterator().next();
		assertEquals("220", offense.getUcrOffenseCode());
		assertEquals("C", offense.getOffenseAttemptedCompleted());
		assertEquals("N", offense.getOffendersSuspectedOfUsing(0));
		assertNull(offense.getOffendersSuspectedOfUsing(1));
		assertNull(offense.getOffendersSuspectedOfUsing(2));
		assertEquals("20", offense.getLocationType());
		assertTrue(offense.getNumberOfPremisesEntered().isMissing());
		assertEquals("N", offense.getMethodOfEntry());
		for (int i = 0; i < 3; i++) {
			assertNull(offense.getTypeOfCriminalActivity(i));
			assertNull(offense.getTypeOfWeaponForceInvolved(i));
			assertNull(offense.getAutomaticWeaponIndicator(i));
		}
		assertEquals("88", offense.getBiasMotivation(0));
	}

	@Test
	public void testIncidentNumbers() {
		String[] numbers = new String[] { "02-000895", "02-003178", "02-018065", "02-023736", "02-033709", "02-064481", "111502", "03000037" };
		List<GroupAIncidentReport> incidentList = incidentListener.getGroupAIncidentList();
		for (int i = 0; i < numbers.length; i++) {
			assertEquals(numbers[i], ((GroupAIncidentReport) incidentList.get(i)).getIncidentNumber());
		}
	}

	@Test
	public void testCorrectGroupAIncidentCount() {
		List<GroupAIncidentReport> incidentList = incidentListener.getGroupAIncidentList();
		assertEquals(8, incidentList.size());
	}

	@Test
	public void testCorrectGroupBIncidentCount() {
		List<GroupBArrestReport> incidentList = incidentListener.getGroupBIncidentList();
		assertEquals(1, incidentList.size());
	}

	@Test
	public void testCorrectZeroReportCount() {
		List<ZeroReport> incidentList = incidentListener.getZeroReportList();
		assertEquals(1, incidentList.size());
	}

	@Test
	public void testCorrectReportCount() {
		assertEquals(10, incidentListener.getReportList().size());
	}

}
