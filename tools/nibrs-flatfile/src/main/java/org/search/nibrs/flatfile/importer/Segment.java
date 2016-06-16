package org.search.nibrs.flatfile.importer;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.util.*;

/**
 * Representation of a single line in a NIBRS report file.
 *
 */
public class Segment
{
	
	private static final Logger LOG = LogManager.getLogger(Segment.class);
    
    private String incidentNumber;
    private String ori;
    private char segmentType;
    private char actionType;
    private String data;
    private int segmentLength;
    private int lineNumber;
    
    public List<NIBRSError> setData(int lineNumber, String data)
    {
    	List<NIBRSError> ret = new ArrayList<NIBRSError>();
        this.data = data;
        this.lineNumber = lineNumber;
        NIBRSError e = null;
        if (data == null || data.length() < 37) {
        	e = new NIBRSError();
        	e.setContext(lineNumber);
        	e.setRuleDescription("Segment less than 37 characters.  Cannot read header information");
        	if (data != null) {
        		e.setValue(data.length());
        	}
			ret.add(e);
        }
		if (data.length() >= 5) {
			segmentType = StringUtils.getStringBetween(5, 5, data).charAt(0);
			if (e != null) {
				e.setSegmentType(segmentType);
			}
		}
		if (e == null) {
			String sv = StringUtils.getStringBetween(1, 4, data);
	        Integer i = null;
	        try {
				i = Integer.parseInt(sv);
	        } catch (NumberFormatException nfe) {
	        	e = new NIBRSError();
	        	e.setContext(lineNumber);
				e.setRuleDescription("Non-numeric segment length field");
	        	e.setValue(sv);
	        	ret.add(e);
	        }
	        if (i != data.length()) {
	        	e = new NIBRSError();
	        	e.setContext(lineNumber);
	        	e.setRuleDescription("Specified segment length of " + i + " does not equal actual length");
	        	e.setValue(data.length());
	        	e.setSegmentType(segmentType);
	        	ret.add(e);
	        	LOG.debug("i=" + i + ", data.length()=" + data.length());
	        }
	        if (e == null) {
	        	this.incidentNumber = StringUtils.getStringBetween(26, 37, data);
	        	this.ori = StringUtils.getStringBetween(17, 25, data);
	        	this.actionType = StringUtils.getStringBetween(6, 6, data).charAt(0);
	        }
		}
        return ret;
    }

    public int getLineNumber() {
    	return lineNumber;
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
	public int getSegmentLength() {
		return segmentLength;
	}

}
