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
package org.search.nibrs.validation.groupb;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.validation.RuleViolationExemplarFactory;

public class GroupBArrestReportValidatorTest {
	
	private static final Logger LOG = LogManager.getLogger(GroupBArrestReportValidatorTest.class);
	
	private GroupBArrestReportValidator validator;
	private RuleViolationExemplarFactory exemplarFactory;

	@Before
	public void init() {
		validator = new GroupBArrestReportValidator();
		exemplarFactory = RuleViolationExemplarFactory.getInstance();
	}
	
	@Test
	public void testRule701() {
		testRule(NIBRSErrorCode._701, 701);
	}
	
	@Test
	public void testRule704() {
		testRule(NIBRSErrorCode._704, 704);
	}
	
	@Test
	public void testRule706() {
		testRule(NIBRSErrorCode._706, 706);
	}
	
	@Test
	public void testRule707() {
		testRule(NIBRSErrorCode._707, 707);
	}
	
	@Test
	public void testRule709() {
		testRule(NIBRSErrorCode._709, 709);
	}
	
	@Test
	public void testRule710() {
		testRule(NIBRSErrorCode._710, 710);
	}
	
	@Test
	public void testRule717() {
		testRule(NIBRSErrorCode._717, 717);
	}
	
	@Test
	public void testRule722() {
		testRule(NIBRSErrorCode._722, 722);
	}
	
	@Test
	public void testRule752() {
		testRule(NIBRSErrorCode._752, 752);
	}
	
	@Test
	public void testRule753() {
		testRule(NIBRSErrorCode._753, 753);
	}
	
	@Test
	public void testRule755() {
		testRule(NIBRSErrorCode._755, 755);
	}
	
	@Test
	public void testRule758() {
		testRule(NIBRSErrorCode._758, 758);
	}
	
	@Test
	public void testRule760() {
		testRule(NIBRSErrorCode._760, 760);
	}
	
	@Test
	public void testRule761() {
		testRule(NIBRSErrorCode._761, 761);
	}
	
	private void testRule(NIBRSErrorCode ruleCode, int ruleNumber) {
		List<GroupBArrestReport> exemplars = exemplarFactory.getGroupBArrestsThatViolateRule(ruleNumber);
		for (GroupBArrestReport r : exemplars) {
			List<NIBRSError> errorList = validator.validate(r);
			boolean found = false;
			for (NIBRSError e : errorList) {
				if (ruleCode == e.getNIBRSErrorCode()) {
					found = true;
					break;
				}
			}
			if (!found) {
				LOG.error("Expected rule violdation not found for rule #" + ruleNumber + ".  Incident: " + r);
			}
			assertTrue(found);
		}
	}

}
