package org.search.nibrs.model;

import java.io.Serializable;

/**
 * Representation of an individual Group B incident in a NIBRS submission.
 *
 */
public class GroupBIncidentReport extends Report implements Serializable {
	
	private static final long serialVersionUID = -6738808121869747274L;
	
	@Override
	public String getUniqueReportDescription() {
    	return "Group B Incident: ATN # " + getATNOrEmpty();
	}

	@Override
	public String getUniqueReportIdentifier() {
		return getOri() + "." + getATNOrEmpty();
	}
	
	private String getATNOrEmpty() {
		return getArresteeCount() == 0 ? "empty" : getArrestees().get(0).getArrestTransactionNumber();
	}

}
