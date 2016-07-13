package org.search.nibrs.model;

import java.io.Serializable;

/**
 * Representation of an Offender reported within an Incident in a NIBRS report.
 *
 */
public class Offender extends Person implements Serializable
{
    
	private static final long serialVersionUID = 871781322681376238L;
	
	private Integer offenderSequenceNumber;
    private boolean reportedUnknown;
    
    public boolean getReportedUnknown() {
    	return reportedUnknown;
    }

    public Integer getOffenderSequenceNumber()
    {
        return offenderSequenceNumber;
    }
    public void setOffenderSequenceNumber(Integer offenderSequenceNumber)
    {
        this.offenderSequenceNumber = offenderSequenceNumber;
        if (offenderSequenceNumber == 0) {
        	reportedUnknown = true;
        }
    }
    
}
