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
    	
    	Reader testdataReader = new BufferedReader(new StringReader(testData));
        IncidentListener incidentListener = new DefaultIncidentListener();
        IncidentBuilder incidentBuilder = new IncidentBuilder();
        incidentBuilder.addIncidentListener(incidentListener);
        List<NIBRSError> errorList = incidentBuilder.buildIncidents(testdataReader);

        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(new Integer(51), e.getRuleNumber());
        assertEquals('8', e.getValue());
        assertEquals(5, e.getContext());
    	
    }
    
    @Test
    public void testInvalidInts() throws IOException {
    	
    	String testData = 
            "00871IABCDEF    TN006000002-000895   20020102 10N                                      \n" +
            "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
            "00465I022003    TN006000002-000895   0124  MW \n";
    	
    	Reader testdataReader = new BufferedReader(new StringReader(testData));
        IncidentListener incidentListener = new DefaultIncidentListener();
        IncidentBuilder incidentBuilder = new IncidentBuilder();
        incidentBuilder.addIncidentListener(incidentListener);
        List<NIBRSError> errorList = incidentBuilder.buildIncidents(testdataReader);
        
        assertEquals(2, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals("AB", e.getValue());
    	
    }


}
