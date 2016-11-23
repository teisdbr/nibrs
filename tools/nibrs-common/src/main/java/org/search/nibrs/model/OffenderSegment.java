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
package org.search.nibrs.model;

import java.util.Arrays;

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

	public boolean isOffenderOfVictim(VictimSegment vs) {
		return (Arrays.asList(vs.getOffenderNumberRelated()).contains(getOffenderSequenceNumber()));
	}
    
}
