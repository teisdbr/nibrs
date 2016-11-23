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

import org.junit.Test;

public class NullArrayTests {
	
	@Test
	public void testVictimSegment() {
		
		VictimSegment v = new VictimSegment();
		
		Object[] a = v.getTypeOfInjury();
		assertNotNull(a);
		assertEquals(VictimSegment.TYPE_OF_INJURY_COUNT, a.length);
		
		v.setTypeOfInjury(null);
		a = v.getTypeOfInjury();
		assertNotNull(a);
		assertEquals(VictimSegment.TYPE_OF_INJURY_COUNT, a.length);
		
		a = v.getAggravatedAssaultHomicideCircumstances();
		assertNotNull(a);
		assertEquals(VictimSegment.AGGRAVATED_ASSAULT_HOMICIDE_CIRCUMSTANCES_COUNT, a.length);
		
		v.setAggravatedAssaultHomicideCircumstances(null);
		a = v.getAggravatedAssaultHomicideCircumstances();
		assertNotNull(a);
		assertEquals(VictimSegment.AGGRAVATED_ASSAULT_HOMICIDE_CIRCUMSTANCES_COUNT, a.length);
		
		a = v.getOffenderNumberRelated();
		assertNotNull(a);
		assertEquals(VictimSegment.OFFENDER_NUMBER_RELATED_COUNT, a.length);
		
		v.setOffenderNumberRelated(null);
		a = v.getOffenderNumberRelated();
		assertNotNull(a);
		assertEquals(VictimSegment.OFFENDER_NUMBER_RELATED_COUNT, a.length);
		
		a = v.getVictimOffenderRelationship();
		assertNotNull(a);
		assertEquals(VictimSegment.OFFENDER_NUMBER_RELATED_COUNT, a.length);
		
		v.setVictimOffenderRelationship(null);
		a = v.getVictimOffenderRelationship();
		assertNotNull(a);
		assertEquals(VictimSegment.OFFENDER_NUMBER_RELATED_COUNT, a.length);
		
		a = v.getUcrOffenseCodeConnection();
		assertNotNull(a);
		assertEquals(VictimSegment.UCR_OFFENSE_CODE_CONNECTION_COUNT, a.length);
		
		v.setUcrOffenseCodeConnection(null);
		a = v.getUcrOffenseCodeConnection();
		assertNotNull(a);
		assertEquals(VictimSegment.UCR_OFFENSE_CODE_CONNECTION_COUNT, a.length);
		
	}

}
