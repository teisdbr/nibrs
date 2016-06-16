package org.search.nibrs.flatfile.importer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;

public class TestIncidentBuilderErrors {
	
    @Test
    public void testBadSegmentValue() throws IOException {
    	
    	String testData = 
            "00871I022003    TN006000002-000895   20020102 10N                                      \n" +
            "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
            "00465I022003    TN006000002-000895   0124  MW \n" +
            "01109I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                \n";
    	
    	Reader testdataReader = new BufferedReader(new StringReader(testData));
        IncidentListener incidentListener = new DefaultIncidentListener();
        IncidentBuilder incidentBuilder = new IncidentBuilder();
        incidentBuilder.addIncidentListener(incidentListener);
        List<NIBRSError> errorList = incidentBuilder.buildIncidents(testdataReader);

        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(new Integer(51), e.getRuleNumber());
        assertEquals('9', e.getValue());
        assertEquals(6, e.getContext());
    	
    }


}
