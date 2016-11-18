package org.search.nibrs.model;

/**
 * Representation of an OffenderSegment reported within an Incident in a NIBRS report.
 *
 */
public class OffenderSegment extends AbstractPersonSegment
{
    
	public static final char OFFENDER_SEGMENT_TYPE_IDENTIFIER = '5';
	private Integer offenderSequenceNumber;
    private boolean reportedUnknown;
    
    public OffenderSegment() {
    	super();
    	segmentType = OFFENDER_SEGMENT_TYPE_IDENTIFIER;
    }
    
    public OffenderSegment(OffenderSegment o) {
    	super(o);
    	offenderSequenceNumber = o.offenderSequenceNumber;
    	reportedUnknown = o.reportedUnknown;
    	segmentType = OFFENDER_SEGMENT_TYPE_IDENTIFIER;
    }
    
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
        if (offenderSequenceNumber != null && offenderSequenceNumber == 0) {
        	reportedUnknown = true;
        }
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((offenderSequenceNumber == null) ? 0 : offenderSequenceNumber.hashCode());
		result = prime * result + (reportedUnknown ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return "OffenderSegment [" + super.toString() + ", offenderSequenceNumber=" + offenderSequenceNumber + ", reportedUnknown=" + reportedUnknown + "]";
	}

	@Override
	protected Object getWithinSegmentIdentifier() {
		return offenderSequenceNumber;
	}
    
}
