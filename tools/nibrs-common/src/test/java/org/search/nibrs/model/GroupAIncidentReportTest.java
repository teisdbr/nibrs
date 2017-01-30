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
package org.search.nibrs.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.search.nibrs.common.ParsedObject;

public class GroupAIncidentReportTest {
	
	private OffenderSegment os1;
	private OffenderSegment os2;
	private OffenderSegment os3;
	private VictimSegment vs1;
	private VictimSegment vs2;
	private VictimSegment vs3;
	private VictimSegment vs4;
	private GroupAIncidentReport incident;

	@Before
	public void init() {
		incident = new GroupAIncidentReport();
		os1 = new OffenderSegment();
		os1.setOffenderSequenceNumber(new ParsedObject<>(1));
		incident.addOffender(os1);
		os2 = new OffenderSegment();
		os2.setOffenderSequenceNumber(new ParsedObject<>(2));
		incident.addOffender(os2);
		os3 = new OffenderSegment();
		os3.setOffenderSequenceNumber(new ParsedObject<>(3));
		incident.addOffender(os3);
		vs1 = new VictimSegment();
		vs1.setOffenderNumberRelated(0, new ParsedObject<>(1));
		vs1.setOffenderNumberRelated(1, new ParsedObject<>(2));
		incident.addVictim(vs1);
		vs2 = new VictimSegment();
		vs2.setOffenderNumberRelated(0, new ParsedObject<>(1));
		incident.addVictim(vs2);
		vs3 = new VictimSegment();
		vs3.setOffenderNumberRelated(0, new ParsedObject<>(2));
		incident.addVictim(vs3);
		vs4 = new VictimSegment();
		incident.addVictim(vs4);
	}

	@Test
	public void testVictimsOfOffender() {
		List<VictimSegment> victims = incident.getVictimsOfOffender(os1);
		assertTrue(victims.contains(vs1));
		assertTrue(victims.contains(vs2));
		assertFalse(victims.contains(vs3));
		assertTrue(incident.getVictimsOfOffender(os3).isEmpty());
	}

	@Test
	public void testOffendersOfVictim() {
		List<OffenderSegment> offenders = incident.getOffendersOfVictim(vs1);
		assertTrue(offenders.contains(os1));
		assertTrue(offenders.contains(os2));
		assertTrue(incident.getOffendersOfVictim(vs4).isEmpty());
	}

}
