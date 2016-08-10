package org.search.nibrs.common;

/**
 * The interface for objects that serve as a source of NIBRS reports (e.g., files, databases, etc.)  Importers or other sources of NIBRS reports use these
 * objects to report information about where each report came from.
 */
public class ReportSource {
	
	private String sourceName;
	private String sourceLocation;
	
	public ReportSource() {
	}
	
	public ReportSource(ReportSource rs) {
		this();
		sourceName = rs.sourceName;
		sourceLocation = rs.sourceLocation;
	}
	
	/**
	 * The name of the source.  This could be used to hold the path to a file, the name of a database, etc.
	 * @return the name of the source
	 */
	public String getSourceName() {
		return sourceName;
	}
	
	/**
	 * The location within a source at which the report can be found.  For instance, this could be used for the line number of a file, a primary key value
	 * for a database, etc.
	 * @return the location within the source
	 */
	public String getSourceLocation() {
		return sourceLocation;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public void setSourceLocation(String sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

}
