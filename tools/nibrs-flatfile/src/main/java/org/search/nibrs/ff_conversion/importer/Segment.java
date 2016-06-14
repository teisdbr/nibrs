package org.search.nibrs.ff_conversion.importer;

import org.search.nibrs.ff_conversion.util.*;

/**
 * Representation of a single line in a NIBRS report file.
 *
 */
public class Segment
{
    
    private String incidentNumber;
    private String ori;
    private char segmentType;
    private char actionType;
    private String data;
    
    public Segment(String data)
    {
        this.data = data;
        this.incidentNumber = StringUtils.getStringBetween(26, 37, data);
        this.ori = StringUtils.getStringBetween(17, 25, data);
        this.segmentType = StringUtils.getStringBetween(5, 5, data).charAt(0);
        this.actionType = StringUtils.getStringBetween(6, 6, data).charAt(0);
    }

    public String getData()
    {
        return data;
    }
    public String getIncidentNumber()
    {
        return incidentNumber;
    }
    public String getOri()
    {
        return ori;
    }
    public char getSegmentType()
    {
        return segmentType;
    }
    public char getActionType()
    {
        return actionType;
    }
}
