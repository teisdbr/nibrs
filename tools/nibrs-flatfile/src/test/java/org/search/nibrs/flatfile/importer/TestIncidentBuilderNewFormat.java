package org.search.nibrs.flatfile.importer;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.search.nibrs.model.*;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.importer.DefaultReportListener;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.flatfile.util.*;
import org.junit.*;

/**
 * Unit test suite for the IncidentBuilder class.
 *
 */
public class TestIncidentBuilderNewFormat
{
	
    private static final String TESTDATA_NEWFORMAT =
        "00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
        "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
        "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
        "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
        "00465I022003    TN006000002-000895   0124  MW \n" +
        "01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                \n" +
        "00881I022003    TN006000002-003178   20020116 12N                                      Y\n" +
        "00712I022003    TN006000002-003178   220CN  20  N            88        \n" +
        "03073I022003    TN006000002-003178   777000000005                                                                                                                                                                                                                                                                  \n" +
        "01414I022003    TN006000002-003178   001220                           I28  MWNR                                                              \n" +
        "00465I022003    TN006000002-003178   0124  MW \n" +
        "01106I022003    TN006000002-003178   0102-003178   20021230TM22001    24  MWNR                                \n" +
        "00871I022003    TN006000002-018065   20020408 10N                                      \n" +
        "00712I022003    TN006000002-018065   23DCN  20               88        \n" +
        "03073I022003    TN006000002-018065   717000002600                                                                                                                                                                                                                                                                  \n" +
        "01414I022003    TN006000002-018065   00123D                           I54  FWNR                                                              \n" +
        "00465I022003    TN006000002-018065   0100  FWN\n" +
        "00871I022003    TN006000002-023736   20020517 06N                                      \n" +
        "00712I022003    TN006000002-023736   220CN  20  F            88        \n" +
        "03073I022003    TN006000002-023736   720000000115        22000000000                                                                                                                                                                                                                                               \n" +
        "01414I022003    TN006000002-023736   001220                           I43  MWNR                                                              \n" +
        "00465I022003    TN006000002-023736   0121  MWH\n" +
        "01106I022003    TN006000002-023736   0102-023736   20021224TM22001    21  MWNN                                \n" +
        "00871I022003    TN006000002-033709   20020624 12N                                      \n" +
        "00712I022003    TN006000002-033709   23HCN  20               88        \n" +
        "03073I022003    TN006000002-033709   539000001000200301037700000150020030103                                                                                                                                                                                                                                       \n" +
        "03073I022003    TN006000002-033709   704000000065        36000001400        39000001000        77000002964                                                                                                                                                                                                         \n" +
        "01414I022003    TN006000002-033709   00123H                           I46  MWNR                                                              \n" +
        "00465I022003    TN006000002-033709   00       \n" +
        "00871I022003    TN006000002-064481   20021216 07N                                      \n" +
        "00712I022003    TN006000002-064481   35ACN  22   P           88        \n" +
        "03073I022003    TN006000002-064481   610                                                                                                                                                                                                E000000000100GMH000000002000DU                                             \n" +
        "01414I022003    TN006000002-064481   00135A                           S                                                                      \n" +
        "00465I022003    TN006000002-064481   0115  MW \n" +
        "00465I022003    TN006000002-064481   0214  MW \n" +
        "01106I022003    TN006000002-064481   0202-064481A  20021216SN35A01    14  MWNRR                               \n" +
        "00871I022003    TN0390500111502      20021115 19N                                      \n" +
        "00712I022003    TN0390500111502      240CN  05               88        \n" +
        "03073I022003    TN0390500111502      703000000001                                                                                                                                                                                   01                                                                             \n" +
        "03073I022003    TN0390500111502      50300000000120021115                                                                                                                                                                             01                                                                           \n" +
        "01414I022003    TN0390500111502      001240                           B                                                                      \n" +
        "00465I022003    TN0390500111502      0100  UU \n" +
        "00871I022003    TN038010003000037    20030111 22N                                      \n" +
        "00712I022003    TN038010003000037    13ACN  20      12       88        \n" +
        "00712I022003    TN038010003000037    23HCN  20               88        \n" +
        "00712I022003    TN038010003000037    290CN  20               6162      \n" +
        "03073I022003    TN038010003000037    720000000150                                                                                                                                                                                                                                                                  \n" +
        "03073I022003    TN038010003000037    432000000100                                                                                                                                                                                                                                                                  \n" +
        "01414I022003    TN038010003000037    00113A23H290                     L20  FBNR09   N    01BG                                    01G         \n" +
        "00465I022003    TN038010003000037    0133  MB \n" +
        "01106I022003    TN038010003000037    0100002655    20030112TN13A01    33  MBNR                                \n";

    private Reader testdataReader;
    private DefaultReportListener incidentListener;
    
    @Before
    public void setUp() throws Exception
    {
        testdataReader = new BufferedReader(new StringReader(TESTDATA_NEWFORMAT));
        incidentListener = new DefaultReportListener();
        IncidentBuilder incidentBuilder = new IncidentBuilder();
        incidentBuilder.addIncidentListener(incidentListener);
        List<NIBRSError> errorList = incidentBuilder.buildIncidents(testdataReader);
        assertEquals(0, errorList.size());
    }
    
    @Test
    public void testFirstIncident()
    {
        List<GroupAIncidentReport> incidentList = incidentListener.getGroupAIncidentList();
        GroupAIncidentReport incident = (GroupAIncidentReport) incidentList.get(0);
        assertNotNull(incident);
        assertEquals("02-000895", incident.getIncidentNumber());
        assertEquals(DateUtils.makeDate(2002, Calendar.JANUARY, 2), incident.getIncidentDate());
        assertEquals(new Integer(10), incident.getIncidentHour());
        assertFalse(incident.getReportDateIndicator());
        assertEquals("N", incident.getExceptionalClearanceCode());
        assertNull(incident.getExceptionalClearanceDate());
        assertEquals(1, incident.getOffenseCount());
        assertEquals(1, incident.getPropertyCount());
        assertEquals(1, incident.getVictimCount());
        assertEquals(1, incident.getOffenderCount());
        assertEquals(1, incident.getArresteeCount());
        assertFalse(incident.getHasUpstreamErrors());
        assertTrue(incident.includesLeoka());
    }
    
    @Test
    public void testCargoTheftIndicator() {
    	List<GroupAIncidentReport> incidentList = incidentListener.getGroupAIncidentList();
        GroupAIncidentReport incident = (GroupAIncidentReport) incidentList.get(0);
        assertFalse(incident.getCargoTheftIndicator());
        incident = (GroupAIncidentReport) incidentList.get(1);
        assertTrue(incident.getCargoTheftIndicator());
        incident = (GroupAIncidentReport) incidentList.get(2);
        assertNull(incident.getCargoTheftIndicator());
    }
    
    @Test
    public void testFirstIncidentArrestee()
    {
        Arrestee arrestee = (Arrestee) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).arresteeIterator().next();
        assertEquals(new Integer(1), arrestee.getArresteeSequenceNumber());
        assertEquals("02-000895", arrestee.getArrestTransactionNumber());
        assertEquals(DateUtils.makeDate(2002, Calendar.DECEMBER, 30), arrestee.getArrestDate());
        assertEquals("T", arrestee.getTypeOfArrest());
        assertEquals("M", arrestee.getMultipleArresteeSegmentsIndicator());
        assertEquals("220", arrestee.getUcrArrestOffenseCode());
        assertEquals("01", arrestee.getArresteeArmedWith(0));
        assertNull(arrestee.getAutomaticWeaponIndicator(0));
        assertNull(arrestee.getArresteeArmedWith(1));
        assertNull(arrestee.getAutomaticWeaponIndicator(1));
        assertEquals("24", arrestee.getAgeOfArresteeString());
        assertEquals("M", arrestee.getSexOfArrestee());
        assertEquals("W", arrestee.getRaceOfArrestee());
        assertEquals("R", arrestee.getResidentStatusOfArrestee());
        assertNull(arrestee.getDispositionOfArresteeUnder18());
        
    }
    
    @Test
    public void testFirstIncidentOffender()
    {
        Offender offender = (Offender) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).offenderIterator().next();
        assertEquals(new Integer(1), offender.getOffenderSequenceNumber());
        assertEquals("24", offender.getAgeOfOffenderString());
        assertEquals("M", offender.getSexOfOffender());
        assertEquals("W", offender.getRaceOfOffender());
    }
    
    @Test
    public void testFirstIncidentVictim()
    {
        Victim victim = (Victim) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).victimIterator().next();
        assertEquals(new Integer(1), victim.getVictimSequenceNumber());
        assertEquals("220", victim.getUcrOffenseCodeConnection(0));
        for (int i=1;i < 10;i++)
        {
            assertNull(victim.getUcrOffenseCodeConnection(i));
        }
        assertEquals("I", victim.getTypeOfVictim());
        assertEquals("46", victim.getAgeOfVictimString());
        assertEquals("W", victim.getRaceOfVictim());
        assertEquals("N", victim.getEthnicityOfVictim());
        assertEquals("R", victim.getResidentStatusOfVictim());
        assertNull(victim.getAggravatedAssaultHomicideCircumstances(0));
        assertNull(victim.getAggravatedAssaultHomicideCircumstances(1));
        assertNull(victim.getAdditionalJustifiableHomicideCircumstances());
        for (int i=0;i < 5;i++)
        {
            assertNull(victim.getTypeOfInjury(i));
        }
        for (int i=0;i < 10;i++)
        {
            assertNull(victim.getOffenderNumberRelated(i));
            assertNull(victim.getVictimOffenderRelationship(i));
        }
    }
    
    @Test
    public void testComplexIncidentVictim()
    {
        Victim victim = (Victim) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(7)).victimIterator().next();
        assertEquals("13A", victim.getUcrOffenseCodeConnection(0));
        assertEquals("23H", victim.getUcrOffenseCodeConnection(1));
        assertEquals("290", victim.getUcrOffenseCodeConnection(2));
        assertEquals("09", victim.getAggravatedAssaultHomicideCircumstances(0));
        assertEquals("N", victim.getTypeOfInjury(0));
        assertEquals(new Integer(1), victim.getOffenderNumberRelated(0));
        assertEquals("BG", victim.getVictimOffenderRelationship(0));
    }
    
    @Test
    public void testFirstIncidentProperty()
    {
        Property property = (Property) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).propertyIterator().next();
        assertEquals("7", property.getTypeOfPropertyLoss());
        assertEquals("13", property.getPropertyDescription(0));
        assertEquals(new Integer(20), property.getValueOfProperty(0));
        for (int i=1;i < 10;i++)
        {
            assertNull(property.getPropertyDescription(i));
            assertNull(property.getValueOfProperty(i));
        }
        assertNull(property.getNumberOfStolenMotorVehicles());
        assertNull(property.getNumberOfRecoveredMotorVehicles());
        for (int i=0;i < 3;i++)
        {
            assertNull(property.getSuspectedDrugType(i));
            assertNull(property.getEstimatedDrugQuantity(i));
            assertNull(property.getTypeDrugMeasurement(i));
        }
    }
    
    @Test
    public void testDrugIncidentProperty()
    {
        Property property = (Property) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(5)).propertyIterator().next();
        assertEquals("E", property.getSuspectedDrugType(0));
        assertEquals(new Double(0.1), property.getEstimatedDrugQuantity(0));
        assertEquals("GM", property.getTypeDrugMeasurement(0));
        assertEquals("H", property.getSuspectedDrugType(1));
        assertEquals(new Double(2.0), property.getEstimatedDrugQuantity(1));
        assertEquals("DU", property.getTypeDrugMeasurement(1));
    }
    
    @Test
    public void testMotorVehicleIncidentProperty()
    {
        Iterator<Property> propertyIterator = ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(6)).propertyIterator();
        Property stolenProperty = (Property) propertyIterator.next();
        Property recoveredProperty = (Property) propertyIterator.next();
        assertEquals("7", stolenProperty.getTypeOfPropertyLoss());
        assertEquals(new Integer(1), stolenProperty.getNumberOfStolenMotorVehicles());
        assertNull(stolenProperty.getNumberOfRecoveredMotorVehicles());
        assertEquals("5", recoveredProperty.getTypeOfPropertyLoss());
        assertEquals(DateUtils.makeDate(2002, Calendar.NOVEMBER, 15), recoveredProperty.getDateRecovered(0));
        assertEquals(new Integer(1), recoveredProperty.getNumberOfRecoveredMotorVehicles());
        assertNull(recoveredProperty.getNumberOfStolenMotorVehicles());
    }
    
    @Test
    public void testFirstIncidentOffense()
    {
        Offense offense = (Offense) ((GroupAIncidentReport) incidentListener.getGroupAIncidentList().get(0)).offenseIterator().next();
        assertEquals("220", offense.getUcrOffenseCode());
        assertEquals("C", offense.getOffenseAttemptedCompleted());
        assertEquals("N", offense.getOffendersSuspectedOfUsing(0));
        assertNull(offense.getOffendersSuspectedOfUsing(1));
        assertNull(offense.getOffendersSuspectedOfUsing(2));
        assertEquals("20", offense.getLocationType());
        assertNull(offense.getNumberOfPremisesEntered());
        assertEquals("N", offense.getMethodOfEntry());
        for (int i=0;i < 3;i++)
        {
            assertNull(offense.getTypeOfCriminalActivity(i));
            assertNull(offense.getTypeOfWeaponForceInvolved(i));
            assertNull(offense.getAutomaticWeaponIndicator(i));
        }
        assertEquals("88", offense.getBiasMotivation(0));
    }
    
    @Test
    public void testIncidentNumbers()
    {
        String[] numbers = new String[]
        {
                "02-000895",
                "02-003178",
                "02-018065",
                "02-023736",
                "02-033709",
                "02-064481",
                "111502",
                "03000037"
        };
        List<GroupAIncidentReport> incidentList = incidentListener.getGroupAIncidentList();
        for (int i=0;i < numbers.length;i++)
        {
            assertEquals(numbers[i], ((GroupAIncidentReport) incidentList.get(i)).getIncidentNumber());
        }
    }
    
    @Test
    public void testCorrectIncidentCount()
    {
        List<GroupAIncidentReport> incidentList = incidentListener.getGroupAIncidentList();
        assertEquals(8, incidentList.size());
    }

}
