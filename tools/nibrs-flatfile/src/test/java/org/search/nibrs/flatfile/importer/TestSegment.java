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

import java.util.List;

import org.junit.*;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.flatfile.importer.Segment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;

public class TestSegment
{
	
	@Test
	public void testSegmentTooShort() {
        String segment = "00871";
        Segment s = new Segment();
        List<NIBRSError> errorList = s.setData(makeReportSource(1), segment);
        assertEquals(1, errorList.size());
        NIBRSError error = errorList.get(0);
        assertEquals(5, error.getValue());
        assertEquals("1", error.getContext().getSourceLocation());
        assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, error.getSegmentType());
	}

    @Test
	public void testIncidentSegment()
    {
        String segment = "00871I022003    TN0390500111502      20021115 19N                                      ";
        Segment s = new Segment();
        assertEquals(0, s.setData(makeReportSource(1), segment).size());
        assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getSegmentUniqueIdentifier());
        assertEquals("TN0390500", s.getOri());
        assertEquals("1", s.getReportSource().getSourceLocation());
        assertEquals(getClass().getName(), s.getReportSource().getSourceName());
        assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, s.getSegmentLevel());
    }
    
    @Test
    public void testOffenseSegment()
    {
        String segment = "00632I022003    TN0390500111502      240CN  05               88";
        Segment s = new Segment();
        assertEquals(0, s.setData(makeReportSource(1), segment).size());
        assertEquals(OffenseSegment.OFFENSE_SEGMENT_TYPE_IDENTIFIER, s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getSegmentUniqueIdentifier());
        assertEquals("TN0390500", s.getOri());
        assertEquals('2', s.getSegmentLevel());
    }

    @Test
    public void testPropertySegment()
    {
        String segment = "03073I022003    TN0390500111502      703000000001                                                                                                                                                                                   01                                                                             ";
        Segment s = new Segment();
        assertEquals(0, s.setData(makeReportSource(1), segment).size());
        assertEquals(PropertySegment.PROPERTY_SEGMENT_TYPE_IDENTIFIER, s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getSegmentUniqueIdentifier());
        assertEquals("TN0390500", s.getOri());
    }

    @Test
    public void testVictimSegment()
    {
        String segment = "01294I022003    TN0390500111502      001240                           B                                                          ";
        Segment s = new Segment();
        assertEquals(0, s.setData(makeReportSource(1), segment).size());
        assertEquals(VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER, s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getSegmentUniqueIdentifier());
        assertEquals("TN0390500", s.getOri());
    }

    @Test
    public void testOffenderSegment()
    {
        String segment = "00455I022003    TN0390500111502      0100  UU";
        Segment s = new Segment();
        assertEquals(0, s.setData(makeReportSource(1), segment).size());
        assertEquals(OffenderSegment.OFFENDER_SEGMENT_TYPE_IDENTIFIER, s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getSegmentUniqueIdentifier());
        assertEquals("TN0390500", s.getOri());
    }
    
    private ReportSource makeReportSource(int lineNumber) {
		ReportSource ret = new ReportSource();
		ret.setSourceName(getClass().getName());
		ret.setSourceLocation(String.valueOf(lineNumber));
		return ret;
	}

}
