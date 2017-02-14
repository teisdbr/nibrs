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
