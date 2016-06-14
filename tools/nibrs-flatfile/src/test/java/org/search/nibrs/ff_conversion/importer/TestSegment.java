package org.search.nibrs.ff_conversion.importer;

import static org.junit.Assert.*;

import org.junit.*;

public class TestSegment
{

    @Test
	public void testIncidentSegment()
    {
        String segment = "00871I022003    TN0390500111502      20021115 19N        ";
        Segment s = new Segment(segment);
        assertEquals('1', s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getIncidentNumber());
        assertEquals("TN0390500", s.getOri());
    }
    
    @Test
    public void testOffenseSegment()
    {
        String segment = "00632I022003    TN0390500111502      240CN  05               88";
        Segment s = new Segment(segment);
        assertEquals('2', s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getIncidentNumber());
        assertEquals("TN0390500", s.getOri());
    }

    @Test
    public void testPropertySegment()
    {
        String segment = "03073I022003    TN0390500111502      703000000001                                                                                                                                                                                   01                                                                             ";
        Segment s = new Segment(segment);
        assertEquals('3', s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getIncidentNumber());
        assertEquals("TN0390500", s.getOri());
    }

    @Test
    public void testVictimSegment()
    {
        String segment = "01294I022003    TN0390500111502      001240                           B                                                          ";
        Segment s = new Segment(segment);
        assertEquals('4', s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getIncidentNumber());
        assertEquals("TN0390500", s.getOri());
    }

    @Test
    public void testOffenderSegment()
    {
        String segment = "00455I022003    TN0390500111502      0100  UU";
        Segment s = new Segment(segment);
        assertEquals('5', s.getSegmentType());
        assertEquals('I', s.getActionType());
        assertEquals("111502", s.getIncidentNumber());
        assertEquals("TN0390500", s.getOri());
    }

}
