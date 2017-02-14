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
package org.search.nibrs.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.text.StringRandomizer;

public class EqualsTests {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(EqualsTests.class);
	
	private static final StringRandomizer BASE_STRING_RANDOMIZER = new StringRandomizer();
	
	@Test
	public void test() throws Exception {
		
		EnhancedRandomBuilder builder = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().overrideDefaultInitialization(true).maxCollectionSize(4).maxStringLength(6);
		
		builder.randomize(FieldDefinitionBuilder.field().named("ageString").ofType(String.class).inClass(NIBRSAge.class).get(), new Randomizer<String>() {
			@Override
			public String getRandomValue() {
				return Arrays.asList("NB", "2022", "39").get(new Random().nextInt(2));
			}
		});
		
		builder.randomize(FieldDefinitionBuilder.field().named("parentReport").ofType(AbstractReport.class).get(), new Randomizer<AbstractReport>() {
			@Override
			public AbstractReport getRandomValue() {
				return null;
			}
		});
		
		builder.randomize(FieldDefinitionBuilder.field().named("report").ofType(AbstractReport.class).get(), new Randomizer<AbstractReport>() {
			@Override
			public AbstractReport getRandomValue() {
				return null;
			}
		});
		
		builder.randomize(FieldDefinitionBuilder.field().ofType(new String[0].getClass()).get(), new Randomizer<String[]>() {

			@Override
			public String[] getRandomValue() {
				String[] ret = new String[new Random().nextInt(4)];
				for (int i=0;i < ret.length;i++) {
					ret[i] = BASE_STRING_RANDOMIZER.getRandomValue();
				}
				return ret;
			}
			
		});
		
		EnhancedRandom enhancedRandom = builder.build();

		NIBRSError e1 = enhancedRandom.nextObject(NIBRSError.class);
		NIBRSError e2 = new NIBRSError(e1);
		assertTrue(e1.equals(e2));
		
		NIBRSAge a1 = enhancedRandom.nextObject(NIBRSAge.class);
		NIBRSAge a2 = new NIBRSAge(a1);
		assertTrue(a1.equals(a2));
		
		ArresteeSegment arrestee1 = enhancedRandom.nextObject(ArresteeSegment.class);
		ArresteeSegment arrestee2 = new ArresteeSegment(arrestee1);
		assertTrue(arrestee1.equals(arrestee2));
		
		for (int i = 0; i < 1000; i++) {

			GroupAIncidentReport report = enhancedRandom.nextObject(GroupAIncidentReport.class);
			assertTrue(report.equals(new GroupAIncidentReport(report)));

			ZeroReport zeroReport = enhancedRandom.nextObject(ZeroReport.class);
			assertTrue(zeroReport.equals(new ZeroReport(zeroReport)));

			GroupBArrestReport groupBReport = enhancedRandom.nextObject(GroupBArrestReport.class);
			assertTrue(groupBReport.equals(new GroupBArrestReport(groupBReport)));

		}
		
	}

}
