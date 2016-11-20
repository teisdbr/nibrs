package org.search.nibrs.model;

/**
 * Representation of an individual Group B incident in a NIBRS submission.
 *
 */
public class GroupBIncidentReport extends AbstractReport {
	
	public GroupBIncidentReport() {
        super(ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
	}
	
	public GroupBIncidentReport(GroupBIncidentReport r) {
		super(r);
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
    	return "Group B Incident: ATN # " + getATNOrEmpty();
	}

	@Override
	public String getGloballyUniqueReportIdentifier() {
		return getOri() + "." + getATNOrEmpty();
	}
	
	@Override
	public String getIdentifier() {
		return getArresteeCount() == 0 ? null : getArrestees().get(0).getArrestTransactionNumber();
	}
	
	private String getATNOrEmpty() {
		return getArresteeCount() == 0 ? "empty" : getArrestees().get(0).getArrestTransactionNumber();
	}

}
