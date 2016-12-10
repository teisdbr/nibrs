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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;

public class ErrorExporterTests {
	
	private static final Logger LOG = LogManager.getLogger(ErrorExporterTests.class);
	
	private ErrorExporter errorExporter;
	
	@Before
	public void setUp() throws Exception {
		errorExporter = ErrorExporter.getInstance();
	}
	
	@Test
	public void testModifyLine() {
		String line = "         ";
		assertEquals("   ABC   ", errorExporter.modifyLine(line, 3, 6, "ABC"));
	}
	
	@Test
	public void testEmptyErrorReport() throws IOException {
		List<NIBRSError> errorList = new ArrayList<>();
		StringWriter writer = new StringWriter();
		errorExporter.createErrorReport(errorList, writer);
		String contents = writer.toString();
		String[] lines = StringUtils.split(contents, System.lineSeparator());
		assertEquals(1, lines.length);
		String line = lines[0];
		assertEquals(ErrorExporter.ERROR_REPORT_LINE_LENGTH, line.length());
		assertEquals(StringUtils.repeat(' ', 14) + "999999999" + StringUtils.repeat(' ', 38) + "IncidentBuilder processed submission on " +
				new SimpleDateFormat("MM/dd/yy").format(new Date()) + StringUtils.repeat(' ', 31) + StringUtils.repeat(' ', 6), line);
	}
	
}
