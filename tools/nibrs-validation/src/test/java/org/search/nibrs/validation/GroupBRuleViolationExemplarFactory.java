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
			copy.getArrestees().get(0).setEthnicity("invalid");
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setResidentStatus("invalid");
			reports.add(copy);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setDispositionOfArresteeUnder18("invalid");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(706, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Arrest Transaction Number) Must contain a valid character combination of the following:
			//(Arrestee Was Armed With) The referenced data element in error is one that contains 
			//multiple data values. When more than one code is entered, none can be duplicate codes.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArresteeArmedWith(1, "01");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(707, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Arrestee Was Armed With) can have multiple data values and was entered 
			//with multiple values. However, the entry shown between the brackets in [value] 
			//above cannot be entered with any other data value.
			//Baseline factory has "01" an unarmed value, test adds a handgun.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArresteeArmedWith(1, "12");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(709, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Age of Arrestee) contains more than two characters indicating a 
			//possible age-range is being attempted. If so, the field must contain 
			//a numeric entry of four digits.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAgeString("123");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(710, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Age of Arrestee) was entered as an age-range. Accordingly, the 
			//first age component must be less than the second age.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAgeString("2221");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(717, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Arrest Transaction Number) Must contain a valid character combination of the following:
			//A-Z (capital letters only)
			//0-9
			//Hyphen
			//Example: 11-123-SC is valid, but 11+123*SC is not valid.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setArrestTransactionNumber("11+123*SC");
			
			return reports;
			
		});
			
		
		groupBTweakerMap.put(722, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Age of Arrestee) was entered as an age-range. Therefore, the first 
			//age component cannot be 00 (unknown).
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAgeString("0021");
			reports.add(copy);
			
			return reports;
			
		});
		
		// note: exemplars for rules 740 and 741 are not currently tested, as they are warnings not errors
		
		groupBTweakerMap.put(740, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Disposition of Arrestee Under 18) was not entered, but Data Element 47 
			//(Age of Arrestee) indicates an age-range for a juvenile. The low age 
			//is a juvenile and the high age is an adult, but the average age is a juvenile.
			//Note: When an age-range is not entered and the age is a juvenile, 
			//then the disposition must be entered. These circumstances were
			//flagged by the computer as a possible discrepancy between age and 
			//disposition and should be checked for possible correction by the participant.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAgeString("1219");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(741, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//((Age of Arrestee) was entered with a value of 99 which means the arrestee
			//is over 98 years old. The submitter should verify that 99=Over 98 Years Old 
			//is not being confused the with 00=Unknown.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAgeString("99");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(752, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Disposition of Arrestee Under 18) was not entered, but Data Element 47 
			//(Age of Arrestee) is under 18. Whenever an arrestee's age indicates a juvenile,
			//the disposition must be entered.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAgeString("12");
			reports.add(copy);
			
			return reports;
			
		});
			
		groupBTweakerMap.put(753, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Disposition of Arrestee Under 18) was entered, but Data Element 47 (Age of Arrestee) 
			//is 18 or greater. Whenever an arrestee's age indicates an adult, the 
			//juvenile disposition cannot be entered because it does not apply.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setDispositionOfArresteeUnder18("H");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(755, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//((Automatic Weapon Indicator) In Data Element 46 (Arrestee Was Armed With), 
			//A=Automatic is the third character of code. It is valid only with codes:
			//11=Firearm (Type Not Stated)
			//12=Handgun
			//13=Rifle
			//14=Shotgun
			//15=Other Firearm
			//A weapon code other than those mentioned was entered with the automatic indicator.
			//An automatic weapon is, by definition, a firearm.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setAutomaticWeaponIndicator(0, "A");
			reports.add(copy);
			
			return reports;
			
		});
		
		groupBTweakerMap.put(758, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Sex of Arrestee) does not contain a valid code of M=Male or F=Female.
			//Note: U=Unknown (if entered) is not a valid sex for an arrestee.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setSex("U");
			
			return reports;
			
		});
		
		groupBTweakerMap.put(760, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(UCR Arrest Offense Code) must contain a Group B Offense Code
			//in Data Element 45 (UCR Arrest Offense). 
			//The offense code submitted is not a Group B offense code.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setUcrArrestOffenseCode("13A");
			
			return reports;
			
		});
		
		groupBTweakerMap.put(761, arrestReport -> {
			
			List<GroupBArrestReport> reports = new ArrayList<GroupBArrestReport>();
			//(Age of Arrestee) must be 01 through 17 for offense 
			//code of 90I=Runaway on a Group B Arrest Report.
			GroupBArrestReport copy = new GroupBArrestReport(arrestReport);
			copy.getArrestees().get(0).setUcrArrestOffenseCode("90I");
			reports.add(copy);
			
			return reports;
			
		});
		
	}
	
}
