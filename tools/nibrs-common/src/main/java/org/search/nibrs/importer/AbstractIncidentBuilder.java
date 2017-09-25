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
package org.search.nibrs.importer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;

/**
 * Builder class that constructs incidents from a stream of NIBRS report data.
 * Incidents are broadcast to listeners as events; this keeps the class as
 * memory-unintensive as possible (NIBRS report streams can be rather large).
 * <br/>
 * At some point, if other report elements than Incidents are desired, this will
 * need to be modified. Currently, it only broadcasts Incident "add" records.
 * 
 */
public class AbstractIncidentBuilder {
	
	public static final class LogListener implements ReportListener {
		public int reportCount = 0;
		public int errorCount = 0;
		public void newReport(AbstractReport newReport, List<NIBRSError> errorList) {
			log.info("Created " + newReport.getUniqueReportDescription());
			reportCount++;
			errorCount += errorList.size();
		}
	}

	private static final Log log = LogFactory.getLog(AbstractIncidentBuilder.class);;

	private List<ReportListener> listeners;
	private LogListener logListener = new LogListener();
	private DateFormat dateFormat;

	public AbstractIncidentBuilder() {
		setListeners(new ArrayList<ReportListener>());
		getListeners().add(getLogListener());
		setDateFormat(new SimpleDateFormat("yyyyMMdd"));
		getDateFormat().setLenient(false);
	}

	public void addIncidentListener(ReportListener listener) {
		getListeners().add(listener);
	}

	public void removeIncidentListener(ReportListener listener) {
		getListeners().remove(listener);
	}

	/**
	 * Read NIBRS incidents from the flatfile format exposed by the specified Reader
	 * @param reader the source of the data
	 * @throws IOException exception encountered in addressing the Reader
	 */
	public void buildIncidents(Reader reader, String readerLocationName) throws IOException {
		throw new NotImplementedException("The method buildIncidents(Reader, String) is not implemented"); 
	}

	public void buildIncidents(InputStream inputStream, String readerLocationName) {
		throw new NotImplementedException("The method buildIncidents(InputStream, String) is not implemented");
	}

	public List<ReportListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<ReportListener> listeners) {
		this.listeners = listeners;
	}

	public LogListener getLogListener() {
		return logListener;
	}

	public void setLogListener(LogListener logListener) {
		this.logListener = logListener;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

}
