/*******************************************************************************
 * Copyright 2016 Research Triangle Institute
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
package org.search.nibrs.flatfile.errorexport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;

/**
 * A singleton class that handles creation of an error report file in the standard FBI format.
 */
public final class ErrorExporter {
	
	private static final Logger LOG = LogManager.getLogger(ErrorExporter.class);
	
	static final int ERROR_REPORT_LINE_LENGTH = 146;
	private static final ErrorExporter INSTANCE = new ErrorExporter();
	
	private String blankLineTemplate;
	
	private ErrorExporter() {
		char[] chars = new char[ERROR_REPORT_LINE_LENGTH];
		for (int i=0;i < ERROR_REPORT_LINE_LENGTH;i++) {
			chars[i] = ' ';
		}
		blankLineTemplate = new String(chars);
	}
	
	public static final ErrorExporter getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Export the specified list of errors in FBI format to the specified writer
	 * @param errorList
	 * @param writer
	 * @throws IOException 
	 */
	public void createErrorReport(List<NIBRSError> errorList, Writer writer) throws IOException {
		BufferedWriter bw = null;
		if (!(writer instanceof BufferedWriter)) {
			bw = new BufferedWriter(writer);
		} else {
			bw = (BufferedWriter) writer;
		}
		for (NIBRSError error : errorList) {
			
		}
		String line = modifyLine(blankLineTemplate, 15-1, 23, "999999999");
		line = modifyLine(line, 62-1, 140, "IncidentBuilder processed submission on " + new SimpleDateFormat("MM/dd/yy").format(new Date()));
		bw.write(line);
		bw.newLine();
		bw.close();
	}

	String modifyLine(String line, int beginPosition, int endPosition, String string) {
		int pad = endPosition - beginPosition;
		string = StringUtils.rightPad(string, pad, ' ');
		line = StringUtils.overlay(line, string, beginPosition, endPosition);
		return line;
	}

}
