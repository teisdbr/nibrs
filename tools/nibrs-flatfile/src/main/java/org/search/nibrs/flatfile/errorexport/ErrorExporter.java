/*
 * Copyright 2016 Research Triangle Institute
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
package org.search.nibrs.flatfile.errorexport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;

/**
 * A singleton class that handles creation of an error report file in the standard FBI format.
 */
public final class ErrorExporter {
	
	private final Log log = LogFactory.getLog(ErrorExporter.class);
	
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
	@SuppressWarnings("unchecked")
	public void createErrorReport(List<NIBRSError> errorList, Writer writer) throws IOException {
		BufferedWriter bw = null;
		if (!(writer instanceof BufferedWriter)) {
			bw = new BufferedWriter(writer);
		} else {
			bw = (BufferedWriter) writer;
		}
		String line = null;
		for (NIBRSError error : errorList) {
			AbstractReport report = error.getReport();
			line = blankLineTemplate;
			if (report == null) continue;
			line = modifyLine(line, 1-1, 4, String.valueOf(report.getYearOfTape()));
			line = modifyLine(line, 5-1, 6, StringUtils.leftPad(String.valueOf(report.getMonthOfTape()), 2, '0'));
			line = modifyLine(line, 7-1, 13, StringUtils.leftPad(String.valueOf(error.getContext().getSourceLocation()), 7, '0'));
			line = modifyLine(line, 14-1, 14, String.valueOf(error.getReport().getReportActionType()));
			line = modifyLine(line, 15-1, 23, error.getReport().getOri());
			line = modifyLine(line, 24-1, 35, StringUtils.rightPad(error.getReportUniqueIdentifier(), 12));
			char segmentType = error.getSegmentType();
			if (!error.isCrossSegment()) {
				line = modifyLine(line, 36-1, 36, String.valueOf(segmentType));
			}
			Object withinSegmentIdentifier = error.getWithinSegmentIdentifier();
			if (withinSegmentIdentifier != null) {
				if (segmentType == OffenseSegment.OFFENSE_SEGMENT_TYPE_IDENTIFIER) {
					line = modifyLine(line, 37-1, 39, withinSegmentIdentifier.toString());
				} else if (segmentType == OffenderSegment.OFFENDER_SEGMENT_TYPE_IDENTIFIER || segmentType == VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER ||
						segmentType == ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER || segmentType == ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER) {
					line = modifyLine(line, 40-1, 42, StringUtils.leftPad(withinSegmentIdentifier.toString(), 3, '0'));
				} else if (segmentType == PropertySegment.PROPERTY_SEGMENT_TYPE_IDENTIFIER) {
					line = modifyLine(line, 43-1, 43, withinSegmentIdentifier.toString());
				}
			}
			String dataElementIdentifier = error.getDataElementIdentifier();
			if (dataElementIdentifier != null) {
				if (dataElementIdentifier.matches("[0-9]")) {
					dataElementIdentifier = StringUtils.leftPad(dataElementIdentifier, 2, '0');
				}
				line = modifyLine(line, 44 - 1, 46, StringUtils.rightPad(dataElementIdentifier, 3));
			}
			line = modifyLine(line, 47-1, 49, error.getNIBRSErrorCode().getCode());
			line = modifyLine(line, 62 - 1, 140, StringUtils.rightPad(error.getErrorMessage(), 79));
			
			log.debug("error.getReportUniqueIdentifier():" + error.getReportUniqueIdentifier());
			String offendingValues = error.getOffendingValues();
			
			if (error.getRuleNumber().equals("404") && error.getDataElementIdentifier().equals("35")) {
				for (String invalidValue : (List<String>)error.getValue()){
					line = modifyLine(line, 50 - 1, 61, StringUtils.rightPad(StringUtils.trimToEmpty(invalidValue), 12));
					bw.write(line);
					bw.newLine();
				}
			}
			else if (error.getRuleNumber().equals("342")){
				for (String invalidValue : (List<String>)error.getValue()){
					line = modifyLine(line, 50 - 1, 61, invalidValue);
					bw.write(line);
					bw.newLine();
				}
			}
			else { 
				if (offendingValues != null){
					line = modifyLine(line, 50 - 1, 61, StringUtils.rightPad(offendingValues, 12));
				}
				bw.write(line);
				bw.newLine();
			}
		}
		line = blankLineTemplate;
		line = modifyLine(line, 15-1, 23, "999999999");
		line = modifyLine(line, 62-1, 140, "IncidentBuilder processed submission on " + new SimpleDateFormat("MM/dd/yy").format(new Date()));
		bw.write(line);
		bw.newLine();
		bw.flush();
		bw.close();
	}

	String modifyLine(String line, int beginPosition, int endPosition, String string) {
		int pad = endPosition - beginPosition;
		string = StringUtils.rightPad(string, pad, ' ');
		line = StringUtils.overlay(line, string, beginPosition, endPosition);
		return line;
	}

}
