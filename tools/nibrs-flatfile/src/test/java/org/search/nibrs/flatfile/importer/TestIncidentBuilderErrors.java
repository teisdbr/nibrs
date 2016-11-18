package org.search.nibrs.flatfile.importer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class TestIncidentBuilderErrors {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TestIncidentBuilderErrors.class);
	
    @Test
    public void testBadSegmentValue() throws IOException {
    	
    	String testData = 
            "00871I022003    TN006000002-000895   20020102 10N                                      \n" +
            "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
            "00468I022003    TN006000002-000895   0124  MW \n";
    	
    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);

        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(NIBRSErrorCode._051, e.getNIBRSErrorCode());
        assertEquals('8', e.getValue());
        assertEquals(5, e.getContext());
        
        assertTrue(incidentListener.getGroupAIncidentList().get(0).getHasUpstreamErrors());
    	
    }

    @Test
    public void testInvalidInts() throws IOException {
    	
    	String testData = 
            "00871IABCDEF    TN006000002-000895   20020102 10N                                      \n" +
            "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
            "00465I022003    TN006000002-000895   0124  MW \n";
    	
    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(2, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals("AB", e.getValue());
    	
        assertTrue(incidentListener.getGroupAIncidentList().get(0).getHasUpstreamErrors());

    }
    
    @Test
    public void testInvalidAdministrativeSegmentLength() throws IOException {
    	
    	String testData = 
                "00801I022003    TN006000002-000895   20020102 10N                               \n" +
                "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
                "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
                "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
                "00465I022003    TN006000002-000895   0124  MW \n";
        	
    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        assertEquals(1, e.getContext());
        assertEquals(80, e.getValue());
    	
        assertTrue(incidentListener.getGroupAIncidentList().get(0).getHasUpstreamErrors());

    }
    
    @Test
    public void testInvalidArresteeSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01116I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                 ";

    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        assertEquals(6, e.getContext());
        assertEquals(111, e.getValue());
        
        assertTrue(incidentListener.getGroupAIncidentList().get(0).getHasUpstreamErrors());
        
    }

    @Test
    public void testInvalidOffenseSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00702I022003    TN006000002-000895   220CN  20  N            88       \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

        ReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(OffenseSegment.OFFENSE_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        assertEquals(2, e.getContext());
        assertEquals(70, e.getValue());
        
    }

    @Test
    public void testInvalidPropertySegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03063I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                 \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

        ReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(PropertySegment.PROPERTY_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        assertEquals(3, e.getContext());
        assertEquals(306, e.getValue());
        
    }

    @Test
    public void testInvalidVictimSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01404I022003    TN006000002-000895   001220                           I46  FWNR                                                             \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

        ReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        assertEquals(4, e.getContext());
        assertEquals(140, e.getValue());
        
    }

    @Test
    public void testInvalidOffenderSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00475I022003    TN006000002-000895   0124   MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

        ReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(OffenderSegment.OFFENDER_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        assertEquals(5, e.getContext());
        assertEquals(47, e.getValue());
        
    }

	private List<NIBRSError> getErrorsForTestData(String testData, ReportListener incidentListener) throws IOException {
		Reader testdataReader = new BufferedReader(new StringReader(testData));
        IncidentBuilder incidentBuilder = new IncidentBuilder();
        incidentBuilder.addIncidentListener(incidentListener);
        List<NIBRSError> errorList = incidentBuilder.buildIncidents(testdataReader, getClass().getName());
		return errorList;
	}
    
}
