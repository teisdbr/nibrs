/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.model;

import java.util.Date;

import org.search.nibrs.common.ParsedObject;

/**
 * Representation of an individual Bad Segment Level report in a NIBRS submission. 
 *
 */
public class BadSegmentLevelReport extends AbstractReport {

	public static final char BAD_SEGMENT_REPORT_TYPE_IDENTIFIER = 'X';
	private String incidentNumber;

	public BadSegmentLevelReport() {
        super(BAD_SEGMENT_REPORT_TYPE_IDENTIFIER);

	}
	
	public BadSegmentLevelReport(BadSegmentLevelReport z) {
		super(z);
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o.hashCode() == hashCode();
	}
	
	@Override
	public int hashCode() {
		return 31*super.hashCode() + getClass().getName().hashCode();
	}

	@Override
	public String getUniqueReportDescription() {
    	return "Bad Segment Report Report for ORI " + getOri();
	}

	@Override
	public String getGloballyUniqueReportIdentifier() {
		return getOri() + "." + getIncidentNumber();
	}
	
	@Override
	public String getIdentifier() {
		return getIncidentNumber();
	}

    public void setIncidentNumber(String incidentNumber)
    {
        this.incidentNumber = incidentNumber;
    }

    public String getIncidentNumber() {
		return incidentNumber;
	}

}
