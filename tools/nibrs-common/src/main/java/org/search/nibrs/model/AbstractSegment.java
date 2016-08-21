package org.search.nibrs.model;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ValidationTarget;

/**
 * Base class for NIBRS segments
 */
public abstract class AbstractSegment implements ValidationTarget {
	
	private AbstractReport parentReport;
	protected char segmentType;
	
	public AbstractSegment() {
	}
	
	public AbstractSegment(AbstractSegment s) {
		this();
		segmentType = s.segmentType;
	}

	public NIBRSError getErrorTemplate() {
		NIBRSError ret = new NIBRSError();
		ret.setContext(getParentReport().getSource());
		ret.setReportUniqueIdentifier(getParentReport().getUniqueReportIdentifier());
		ret.setWithinSegmentIdentifier(getWithinSegmentIdentifier());
		ret.setSegmentType(getSegmentType());
		return ret;
	}

	protected abstract Object getWithinSegmentIdentifier();
	
	protected void setParentReport(AbstractReport parentReport) {
		this.parentReport = parentReport;
	}

	public AbstractReport getParentReport() {
		return parentReport;
	}

	public final char getSegmentType() {
		return segmentType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1; //super.hashCode();
		result = prime * result + segmentType;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

}
