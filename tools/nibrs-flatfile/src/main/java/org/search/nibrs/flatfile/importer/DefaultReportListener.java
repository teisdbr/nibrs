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
package org.search.nibrs.flatfile.importer;

import java.util.*;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.*;

/**
 * A default implementation of the IncidentListener interface, that simply stores up the incidents and makes them available
 * when building is done.
 *
 */
public final class DefaultReportListener implements ReportListener {

	private List<AbstractReport> reports = new ArrayList<>();
	List<NIBRSError> errorList = new ArrayList<>();
	private Map<String, Throwable> throwables = new HashMap<String, Throwable>();

	public void newReport(AbstractReport newReport, List<NIBRSError> errorList) {
		reports.add(newReport);
		this.errorList.addAll(errorList);
	}

	public List<AbstractReport> getReportList() {
		return Collections.unmodifiableList(reports);
	}

	public Map<String, Throwable> getThrowablesMap() {
		return Collections.unmodifiableMap(throwables);
	}
	
	public List<NIBRSError> getErrorList() {
		return Collections.unmodifiableList(errorList);
	}

	public List<GroupAIncidentReport> getGroupAIncidentList() {
		List<GroupAIncidentReport> ret = new ArrayList<GroupAIncidentReport>();
		for (AbstractReport r : reports) {
			if (r instanceof GroupAIncidentReport) {
				ret.add((GroupAIncidentReport) r);
			}
		}
		return ret;
	}

	public List<GroupBArrestReport> getGroupBIncidentList() {
		List<GroupBArrestReport> ret = new ArrayList<GroupBArrestReport>();
		for (AbstractReport r : reports) {
			if (r instanceof GroupBArrestReport) {
				ret.add((GroupBArrestReport) r);
			}
		}
		return ret;
	}

	public List<ZeroReport> getZeroReportList() {
		List<ZeroReport> ret = new ArrayList<ZeroReport>();
		for (AbstractReport r : reports) {
			if (r instanceof ZeroReport) {
				ret.add((ZeroReport) r);
			}
		}
		return ret;
	}

}
