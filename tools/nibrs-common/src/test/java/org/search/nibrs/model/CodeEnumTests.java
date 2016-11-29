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

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.search.nibrs.model.codes.ArresteeWasArmedWithCode;
import org.search.nibrs.model.codes.OffenseCode;

public class CodeEnumTests {
	
	@Test
	public void testFirearmCode() {
		assertTrue(ArresteeWasArmedWithCode._11.isFirearm());
		assertFalse(ArresteeWasArmedWithCode._01.isFirearm());
	}
	
	@Test
	public void testGamblingOffenseCode() {
		assertTrue(OffenseCode.isGamblingOffenseCode(OffenseCode._39A.code));
		assertFalse(OffenseCode.isGamblingOffenseCode(OffenseCode._200.code));
		Set<String> codes = new HashSet<>();
		codes.add(OffenseCode._39A.code);
		codes.add(OffenseCode._200.code);
		codes.add(OffenseCode._13A.code);
		assertTrue(OffenseCode.containsGamblingOffenseCode(codes));
		codes.remove(OffenseCode._39A.code);
		assertFalse(OffenseCode.containsGamblingOffenseCode(codes));
	}

}
