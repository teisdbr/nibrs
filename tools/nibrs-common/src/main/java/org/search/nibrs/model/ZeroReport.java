package org.search.nibrs.model;

/**
 * Representation of an individual "Zero Report" in a NIBRS submission.  A zero report is submitted by an agency to signify that no crime occurred in that month in that agency.
 *
 */
public class ZeroReport extends AbstractReport {

	public ZeroReport() {
        super('0');
	}
	
	public ZeroReport(ZeroReport z) {
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
    	return "Zero Report for ORI " + getOri();
	}

	@Override
	public String getUniqueReportIdentifier() {
		return getOri() + "." + getIncidentNumber();
	}

	/**
	 * Get the "incident number" for the zero report, which will always be twelve zeros.
	 * @return the "incident number"
	 */
	public String getIncidentNumber() {
		return "000000000000";
	}

}
