package org.search.nibrs.model;

import java.io.Serializable;

/**
 * Representation of an individual "Zero Report" in a NIBRS submission.
 *
 */
public class ZeroReport extends Report implements Serializable {

	private static final long serialVersionUID = 6435774702233555489L;

	@Override
	public String getUniqueReportDescription() {
    	return "Zero Report for ORI " + getOri();
	}

	@Override
	public String getUniqueReportIdentifier() {
		return getOri() + "." + "000000000000";
	}

}
