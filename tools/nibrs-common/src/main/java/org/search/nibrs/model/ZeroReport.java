package org.search.nibrs.model;

/**
 * Representation of an individual "Zero AbstractReport" in a NIBRS submission.  A zero report is submitted by an agency to signify that no crime occurred in that month in that agency.
 *
 */
public class ZeroReport extends AbstractReport {

	public ZeroReport() {
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
    	return "Zero AbstractReport for ORI " + getOri();
	}

	@Override
	public String getUniqueReportIdentifier() {
		return getOri() + "." + "000000000000";
	}

}
