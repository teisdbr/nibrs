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
package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.EthnicityCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.RaceCode;
import org.search.nibrs.model.codes.RelationshipOfVictimToOffenderCode;
import org.search.nibrs.model.codes.SexCode;

final class OffenderRuleViolationExemplarFactory {

	private static final OffenderRuleViolationExemplarFactory INSTANCE = new OffenderRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(OffenderRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private OffenderRuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final OffenderRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> getGroupATweakerMap() {
		return groupATweakerMap;
	}

	private void populateGroupAExemplarMap() {
		
		groupATweakerMap.put(560, incident -> {
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setSex(SexCode.F.code);
			copy.getOffenses().get(0).setUcrOffenseCode(OffenseCode._11A.code);
			copy.getVictims().get(0).setSex(SexCode.F.code);
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(501, incident -> {
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 5 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(ParsedObject.getMissingParsedObject());
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(new ParsedObject<>(-1));
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(new ParsedObject<>(100));
			incidents.add(copy);
			return incidents;
		});
		
		groupATweakerMap.put(504, incident -> {
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//(Sex of Offender) The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setSex(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setSex("invalid");
			incidents.add(copy);
			//(Race of Offender) The referenced data element in a Group A Incident 
			//Report must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setRace(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setRace("invalid");
			incidents.add(copy);
			//(Ethnicity of Offender) The referenced data element in a Group A Incident 
			//Report must be populated with a valid data value and cannot be blank.
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setEthnicity("invalid");
			incidents.add(copy);
			
			return incidents;
			
		});	
			
		groupATweakerMap.put(510,incident -> {
			//(Age of Offender) was entered as an age-range. Accordingly, the 
			//rst age component must be less than the second age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("3020");
			incidents.add(copy);
			return incidents;
		});		
		
		groupATweakerMap.put(509,incident -> {
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("321");
			incidents.add(copy);
			return incidents;
		});		
		
		groupATweakerMap.put(522,incident -> {
			//(Age of Offender) was entered as an age-range. Therefore, 
			//the first age component cannot be 00 (unknown).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("0020");
			incidents.add(copy);
			return incidents;
		});		
		
		// note: regarding rule 549...we do not currently support "warnings"
		
		groupATweakerMap.put(550,incident -> {
			//(Age of Offender) cannot be less than 10 years old when 
			//Data Element 35 (Relationship of Victim to Offender) 
			//contains a relationship of SE=Spouse.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("09");
			incidents.add(copy);
			return incidents;
		});		
			
		
		groupATweakerMap.put(551,incident -> {
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenderSegment offender = new OffenderSegment();
			copy.addOffender(offender);
			offender.setOffenderSequenceNumber(new ParsedObject<>(1));
			offender.setAgeString("23");
			offender.setRace("W");
			offender.setSex("M");
			offender.setEthnicity("H");
			return incidents;
		});
		
		
		groupATweakerMap.put(552,incident -> {
			//(Age of Offender Data Element 38 (Sex of Offender), and Data Element 39 
			//(Race of Offender) cannot be entered when Data Element 36 
			//(Offender Sequence Number) is 00=Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(new ParsedObject<>(00));
			//(Sex of Offender) Data Element 38 (Sex of Offender), and Data Element 39 
			//(Race of Offender) cannot be entered when Data Element 36 
			//(Offender Sequence Number) is 00=Unknown.						
			copy.getOffenders().get(0).setRace(RaceCode.A.code);
			incidents.add(copy);
			//(Race of Offender) Data Element 38 (Sex of Offender), and 
			//Data Element 39 (Race of Offender) cannot be entered when Data Element 36 
			//(Offender Sequence Number) is 00=unknown.
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(new ParsedObject<>(00));
			copy.getOffenders().get(0).setSex(SexCode.F.code);
			incidents.add(copy);
			//(Ethnicity of Offender) Data Element 38 (Sex of Offender), and 
			//Data Element 39 (Race of Offender) cannot be entered when 
			//Data Element 36 (Offender Sequence Number) is 00=Unknown.	
			copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(new ParsedObject<>(00));
			copy.getOffenders().get(0).setEthnicity(EthnicityCode.H.code);
			incidents.add(copy);
			return incidents;
		});		
		
		groupATweakerMap.put(553,incident -> {
			//(Sex of Offender) has a relationship that is inconsistent with the 
			//offenders sex. The sex of the victim and/or offender must reflect 
			//the implied relationship. For example, if the relationship of the 
			//to offender is Homosexual Relationship, then the victims sex must be the same 
			//as the offenders sex. The following relationships must reflect either the Same 
			//or Different sex codes depending upon this relationship:
			//Relationship Sex Code
			//
			//BG=Victim was Boyfriend/Girlfriend Different
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "BG");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//XS=Victim was Ex-Spouse Different
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "XS");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//SE=Victim was Spouse Different
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "SE");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//CS=Victim was Common-Law Spouse Different
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "CS");
			copy.getVictims().get(0).setSex("F");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			//HR=Homosexual Relationship Same
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "HR");
			copy.getVictims().get(0).setSex("M");
			copy.getOffenders().get(0).setSex("F");
			incidents.add(copy);
			
			return incidents;
			
		});		
			
		
		
		groupATweakerMap.put(554,incident -> {
			//(Age of Offender) has a relationship that is inconsistent with the 
			//offenders age. The age of the victim and/or offender must reflect 
			//the implied relationship. For example, if the relationship of the victim 
			//to offender is PA=Parent, then the victims age must be greater than 
			//the offenders age. The following relationships must be consistent with 
			//the victims age in relation to the offenders age:
			//Relationship Victims Age Is:
			//CH=Victim was Child Younger
			//PA=Victim was Parent Older
			//GP=Victim was Grandparent Older
			//GC=Victim was Grandchild Younger
			//
			//Victim is parent - Offender Age must be younger than victim age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "PA");
			copy.getVictims().get(0).setAgeString("10");
			copy.getOffenders().get(0).setAgeString("30");
			incidents.add(copy);
			//Victim is child - offender Age must be older.
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "CH");
			copy.getVictims().get(0).setAgeString("30");
			copy.getOffenders().get(0).setAgeString("10");
			incidents.add(copy);
			//Victim is grandparent - offender must be younger
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "GP");
			copy.getVictims().get(0).setAgeString("50");
			copy.getOffenders().get(0).setAgeString("60");
			incidents.add(copy);
			//Victim is grandchild - offender must be older
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "GC");
			copy.getVictims().get(0).setAgeString("50");
			copy.getOffenders().get(0).setAgeString("40");
			incidents.add(copy);
					
			return incidents;
			
		});		
		
		groupATweakerMap.put(555,incident -> {
			//When multiple Offender Segments are submitted, none can contain a 
			//00=Unknown value because the presence of 00 indicates that the number of 
			//offenders is unknown. In this case, multiple offenders were submitted,
			//but one of the segments contains the 00=Unknown value.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenderSegment os = new OffenderSegment();
			copy.addOffender(os);
			os.setOffenderSequenceNumber(new ParsedObject<>(0));
			incidents.add(copy);
			return incidents;
		});		
		
		groupATweakerMap.put(556,incident -> {
			//(Age of Offender) must contain numeric entry of 00 through 99.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("BB");
			incidents.add(copy);
			return incidents;
		});		
		
		groupATweakerMap.put(557, incident -> {
			//(Offender Sequence Number) contains 00 indicating that nothing is 
			//known about the offender(s) regarding number and any identifying information. 
			//In order to exceptionally clear the incident, the value cannot be 00. 
			//The incident was submitted with Data Element 4 (Cleared Exceptionally) having a value of A through E.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setOffenderSequenceNumber(new ParsedObject<>(0));
			copy.setExceptionalClearanceCode(ClearedExceptionallyCode.A.code);
			incidents.add(copy);			
			return incidents;
		});
		
		
		groupATweakerMap.put(558, incident -> {
			
			//None of the Offender Segments contain all known values for Age, Sex, 
			//and Race. When an Incident is cleared exceptionally (Data Element 4 
			//contains an A through E), one offender must have all known values.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport base = new GroupAIncidentReport(incident);
			base.setExceptionalClearanceCode(ClearedExceptionallyCode.A.code);
			
			GroupAIncidentReport copy = new GroupAIncidentReport(base);
			OffenderSegment offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setSex(SexCode.U.code);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setSex(null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setRace(RaceCode.U.code);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setRace(null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setAgeString("00  ");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setAgeString(null);
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(559, incident -> {
			
			//The incident was submitted with Data Element 6 (UCR Offense Code)
			//value of 09C=Justifiable Homicide, but unknown information was submitted
			//for all the offender(s). At least one of the offenders must have known
			//information for Age, Sex, and Race.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport base = new GroupAIncidentReport(incident);
			base.getOffenses().get(0).setUcrOffenseCode(OffenseCode._09C.code);
			
			GroupAIncidentReport copy = new GroupAIncidentReport(base);
			OffenderSegment offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setSex(SexCode.U.code);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setSex(null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setRace(RaceCode.U.code);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setRace(null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setAgeString("00  ");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(base);
			offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setAgeString(null);
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(572,incident -> {
			//Data Element 37 (Age of Offender) If Data Element 37 (Age of Offender) is 
			//00=Unknown, Data Element 38 (Sex of Offender) is U=Unknown, and 
			//Data Element 39 (Race of Offender) is U=Unknown then Data Element 35 
			//(Relationship of Victim to Offender) must be RU=Relationship Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenderSegment offenderSegment = copy.getOffenders().get(0);
			offenderSegment.setOffenderSequenceNumber(new ParsedObject<>(1));
			offenderSegment.setAgeString("00");
			offenderSegment.setSex("U");
			offenderSegment.setRace("U");
			VictimSegment victimSegment = copy.getVictims().get(0);
			victimSegment.setOffenderNumberRelated(0, new ParsedObject<>(1));
			victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
			incidents.add(copy);
			return incidents;
		});		
		
	}
	
}
