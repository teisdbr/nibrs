package org.search.nibrs.model;

import java.io.Serializable;

/**
 * Representation of an individual Group B incident in a NIBRS submission.
 *
 */
public class GroupBIncidentReport extends Report implements Serializable {
	
	private static final long serialVersionUID = -6738808121869747274L;
	
	private Arrestee arrestee;

	@Override
	public String getUniqueReportDescription() {
    	return "Group B Incident: ATN # " + arrestee.getArrestTransactionNumber();
	}

	@Override
	public String getUniqueReportIdentifier() {
		return getOri() + "." + arrestee.getArrestTransactionNumber();
	}

	public void setArrestee(Arrestee arrestee) {
		this.arrestee = arrestee;
	}
	
	public Arrestee getArrestee() {
		return arrestee;
	}

}
