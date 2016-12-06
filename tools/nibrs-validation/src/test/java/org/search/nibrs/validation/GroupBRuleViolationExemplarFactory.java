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
package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupBArrestReport;

final class GroupBRuleViolationExemplarFactory {
	
	private static final GroupBRuleViolationExemplarFactory INSTANCE = new GroupBRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(GroupBRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupBArrestReport, List<GroupBArrestReport>>> groupBTweakerMap;

	private GroupBRuleViolationExemplarFactory() {
		groupBTweakerMap = new HashMap<Integer, Function<GroupBArrestReport, List<GroupBArrestReport>>>();
		populateExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final GroupBRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupBArrestReport, List<GroupBArrestReport>>> getGroupBTweakerMap() {
		return groupBTweakerMap;
	}
	
	private void populateExemplarMap() {
		
		groupBTweakerMap.put(701, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.setYearOfTape(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.setMonthOfTape(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.setOri(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArresteeSequenceNumber(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArrestTransactionNumber(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArrestDate(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setTypeOfArrest(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setUcrArrestOffenseCode(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArresteeArmedWith(0, null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAgeString(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setSex(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setRace(null);
			reports.add(copy);
			
							
			return reports;
			
		});
		
		groupBTweakerMap.put(704, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAutomaticWeaponIndicator(0, "B");
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setEthnicity(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setResidentStatus(null);
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setDispositionOfArresteeUnder18(null);
			reports.add(copy);
			
			return reports;
			
		});
		
		
		groupBTweakerMap.put(717, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArrestTransactionNumber("11+123*SC");
			
			return reports;
			
		});
			
	}
	
}
