package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;

final class VictimRuleViolationExemplarFactory {

	private static final VictimRuleViolationExemplarFactory INSTANCE = new VictimRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(VictimRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private VictimRuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final VictimRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> getGroupATweakerMap() {
		return groupATweakerMap;
	}

	private void populateGroupAExemplarMap() {
		
		groupATweakerMap.put(401, incident -> {
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 4 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.setYearOfTape(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(copy);
			copy2.setMonthOfTape(null);
			GroupAIncidentReport copy3 = new GroupAIncidentReport(copy);
			copy3.setOri(null);
			GroupAIncidentReport copy4 = new GroupAIncidentReport(copy);
			copy4.setIncidentNumber(null);
			GroupAIncidentReport copy5 = new GroupAIncidentReport(copy);
			copy5.getVictims().get(0).setVictimSequenceNumber(null);
			GroupAIncidentReport copy6 = new GroupAIncidentReport(copy);
			copy6.getVictims().get(0).setUcrOffenseCodeConnection(0, null);
			GroupAIncidentReport copy7 = new GroupAIncidentReport(copy);
			copy7.getVictims().get(0).setTypeOfVictim(null);
			GroupAIncidentReport copy8 = new GroupAIncidentReport(copy);
			copy8.getVictims().get(0).setVictimSequenceNumber(000);
			GroupAIncidentReport copy9 = new GroupAIncidentReport(copy);
			copy9.getVictims().get(0).setUcrOffenseCodeConnection(0, "999");
			GroupAIncidentReport copy10 = new GroupAIncidentReport(copy);
			copy10.getVictims().get(0).setTypeOfVictim("Z");
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			incidents.add(copy9);
			incidents.add(copy10);
			
				
			return incidents;
		});

		groupATweakerMap.put(404, incident -> {
			//To-do, waiting on response from Becki
			//The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAgeString(null);
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		
		groupATweakerMap.put(406, incident -> {
			//(Victim Connected to UCR Offense Code) The referenced data element in 
			//error is one that contains multiple data values. When more than one code is 
			//entered, none can be duplicate codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense = new OffenseSegment();
			offense.setUcrOffenseCode("120");
			offense.setOffenseAttemptedCompleted("C");
			offense.setBiasMotivation(0,"88");
			offense.setLocationType("20");
			offense.setNumberOfPremisesEntered(null);
			offense.setMethodOfEntry("N");
			offense.setTypeOfWeaponForceInvolved(0, "99");
			offense.setOffendersSuspectedOfUsing(0, "N");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(1, "13A");
			//(Aggravated Assault/Homicide Circumstances The referenced data element 
			//in error is one that contains multiple data values. When more than one 
			//code is entered, none can be duplicate codes.
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "02");
			copy2.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(1, "02");
			//(Type Injury) The referenced data element in error is one that 
			//contains multiple data values. When more than one code is entered, none can be duplicate codes.			
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getVictims().get(0).setTypeOfInjury(0, "B");
			copy3.getVictims().get(0).setTypeOfInjury(1, "B");
			//(Offender Number to be Related) The referenced data element in error 
			//is one that contains multiple data values. When more than one code 
			//is entered, none can be duplicate codes.
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getVictims().get(0).setOffenderNumberRelated(1, 1);
			
			
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			copy.addOffense(offense);
			
			
			
			return incidents;
				
			
		});
		
		groupATweakerMap.put(409, incident -> {
			//(Age of Victim) contains more than two characters indicating a possible 
			//age-range was being attempted. If so, the field must contain numeric entry of four digits.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAgeString("253");
				
			incidents.add(copy);
				
			return incidents;
				
			});
		
		groupATweakerMap.put(410, incident -> {
			//(Age of Victim) was entered as an age-range. Accordingly, the first age 
			//component must be less than the second age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAgeString("3025");
				
			incidents.add(copy);
				
			return incidents;
				
			});
		
		groupATweakerMap.put(422, incident -> {
			//(Age of Victim) was entered as an age-range. Accordingly, the first age 
			//component must be less than the second age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAgeString("0025");
				
			incidents.add(copy);
				
			return incidents;
				
			});
		
		groupATweakerMap.put(450, incident -> {
			//(Age of Victim) contains a relationship of SE=Spouse. When this is so, the //
			//age of the victim cannot be less than 10 years.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAgeString("09");
				
			incidents.add(copy);
				
			return incidents;
				
			});
		
		groupATweakerMap.put(453, incident -> {
			//(Age of Victim) The Data Element associated with this error must be 
			//present when Data Element 25 (Type of Victim) is I=Individual.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAgeString(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getVictims().get(0).setSex(null);			
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getVictims().get(0).setRace(null);	
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			
			return incidents;
		
		});		
		
		groupATweakerMap.put(454, incident -> {
			//(Type of Officer Activity/Circumstance), Data Element 25B (Officer Assignment Type), 
			//Data Element 26 (Age of Victim), Data Element 27 (Sex of Victim), and 
			//Data Element 28 (Race of Victim) must be entered when 
			//Data Element 25 (Type of Victim) is L=Law Enforcement Officer.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//Officer Assignment Type is null
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfOfficerActivityCircumstance(null);
			copy.getVictims().get(0).setOfficerAssignmentType("K");
			copy.getVictims().get(0).setTypeOfVictim("L");
			//Officer Assignment Type is null
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy2.getVictims().get(0).setOfficerAssignmentType(null);
			copy2.getVictims().get(0).setTypeOfVictim("L");
			//Age is null
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy3.getVictims().get(0).setOfficerAssignmentType("K");
			copy3.getVictims().get(0).setAgeString(null);
			copy3.getVictims().get(0).setTypeOfVictim("L");
			//Sex is null
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy4.getVictims().get(0).setOfficerAssignmentType("K");
			copy4.getVictims().get(0).setSex(null);
			copy4.getVictims().get(0).setTypeOfVictim("L");
			//Race is null
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy5.getVictims().get(0).setOfficerAssignmentType("K");
			copy5.getVictims().get(0).setRace(null);
			copy5.getVictims().get(0).setTypeOfVictim("L");
				
			
					
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			
			return incidents;
			
			
		});
		
		groupATweakerMap.put(455, incident -> {
			//Aggravated Assault Homicide Circumstances contains: 20=Criminal Killed by Private Citizen
			//Or 21=Criminal Killed by Police Officer, but Data Element 32 (Additional Justifiable Homicide Circumstances) was not entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "20");
			copy.getVictims().get(0).setAdditionalJustifiableHomicideCircumstances(null);
						
			incidents.add(copy);
			
			
			return incidents;
			
		});
		
		groupATweakerMap.put(457, incident -> {
			//Aggravated Assault Homicide Circumstances was entered, but Data Element 31 
			//Aggravated Assault/Homicide Circumstances) does not reflect a justifiable homicide circumstance.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "34");
			copy.getVictims().get(0).setAdditionalJustifiableHomicideCircumstances("C");
						
			incidents.add(copy);
			
			
			return incidents;
			
		});
		
		groupATweakerMap.put(456, incident -> {
			//(Aggravated Assault/Homicide Circumstances) was entered with two entries, 
			//but was rejected for one of the following reasons:
			//1) Value 10=Unknown Circumstances is mutually exclusive with any other value. 
			//2) More than one category (i.e., Aggravated Assault, Negligent Manslaughter, etc.) was entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "01");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "10");
			
			incidents.add(copy);
			
				
			return incidents;
			
			
		});
		
		groupATweakerMap.put(458, incident -> {
			//The Data Element associated with this error cannot be entered 
			//when Data Element 25 (Type of Victim) is not I=Individual or 
			//L=Law Enforcement Officer when Data Element 24 (Victim Connected to 
			//UCR Offense Code) contains a Crime Against Person.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setTypeOfInjury(0, "B");
			
			incidents.add(copy);
			
			
			return incidents;
			
		});
		
		groupATweakerMap.put(459, incident -> {
			//The Data Element associated with this error cannot be entered 
			//when Data Element 25 (Type of Victim) is not I=Individual or 
			//L=Law Enforcement Officer when Data Element 24 (Victim Connected to 
			//UCR Offense Code) contains a Crime Against Person.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			
			
			incidents.add(copy);
			
			
			return incidents;
			
		});
		
		groupATweakerMap.put(460, incident -> {
			//Corresponding Data Element 35 (Relationship of Victim to Offenders) 
			//data must be entered when Data Element 34 (Offender Numbers To Be Related) 
			//is entered with a value greater than 00.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, null);
			
			
			incidents.add(copy);
			
			
			return incidents;
			
		});
		
		
		
		
		groupATweakerMap.put(461, incident -> {
			//(Type of Victim) cannot have a value of S=Society/Public when the 
			//offense is 220=Burglary/Breaking and Entering.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getVictims().get(0).setTypeOfVictim("S");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(462, incident -> {
			//(Aggravated Assault/Homicide Circumstances) An Offense Segment (Level 2) 
			//was submitted for 13A=Aggravated Assault. Accordingly, Data Element 31 
			//(Aggravated Assault/Homicide Circumstances) can only have codes of 01 through 06 and 08 through 10. 
			//All other codes, including 07=Mercy Killing, are not valid because they do not relate to an aggravated assault
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "30");
			
			
			incidents.add(copy);
			
			
			return incidents;
			
		});
		
		groupATweakerMap.put(463, incident -> {
			//(Aggravated Assault/Homicide Circumstances) When a Justifiable Homicide 
			//is reported, Data Element 31 (Aggravated Assault/Homicide Circumstances) 
			//can only have codes of 20=Criminal Killed by Private Citizen or 
			//21=Criminal Killed by Police Officer. In this case, a code other than the two mentioned was entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "30");
			
			incidents.add(copy);
			
			
			return incidents;
			
		});
		
		
		groupATweakerMap.put(464, incident -> {
			//UCR Code contains a Crime Against Person, but Data Element 25 
			//(Type of Victim) is not I=Individual or L=Law Enforcement Officer when Data Element 24 
			//(Victim Connected to UCR Offense Code) contains a Crime Against Person.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("B");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(465, incident -> {
			//UCR Code contains a Crime Against Society, but Data Element 25 
			//(Type of Victim) is not S=Society.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("720");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "720");
			copy.getVictims().get(0).setTypeOfVictim("B");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(467, incident -> {
			//UCR code contains a Crime Against Property, but Data Element 25 
			//(Type of Victim) is S=Society. This is not an allowable code for Crime Against Property offenses.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "200");
			copy.getVictims().get(0).setTypeOfVictim("S");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(468, incident -> {
			//Relationship of Victim to Offender) cannot be entered when Data Element 34 
			//(Offender Number to be Related) is zero. Zero means that the number of 
			//offenders is unknown; therefore, the relationship cannot be entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setOffenderNumberRelated(0, 0);
			
					
			incidents.add(copy);
						
			return incidents;
			
		});
		
		groupATweakerMap.put(469, incident -> {
			//Data Element 26 (Age of Victim) should be under 18 when Data Element 24
			//(Victim Connected to UCR Offense Code) is 36B=Statutory Rape.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("36B");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "36B");
			copy.getVictims().get(0).setSex("U");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("11A");
			copy2.getVictims().get(0).setUcrOffenseCodeConnection(0, "11A");
			copy2.getVictims().get(0).setSex("U");
			
					
			incidents.add(copy);
			incidents.add(copy2);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(472, incident -> {
			//(Relationship of Victim to Offender) has a relationship to the offender
			//that is not logical. In this case, the offender was entered with unknown 
			//values for age, sex, and race. Under these circumstances, the relationship 
			//must be entered as RU=Relationship Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAgeString("00");
			copy.getOffenders().get(0).setSex("U");
			copy.getOffenders().get(0).setRace("U");
						
					
			incidents.add(copy);
						
			return incidents;
			
		});
		
		
		groupATweakerMap.put(477, incident -> {
			//(Aggravated Assault/Homicide Circumstances) A victim segment was 
			//submitted with Data Element 24 (Victim Connected to UCR Offense Code) 
			//having an offense that does not have a permitted code for 
			//Data Element 31 (Aggravated Assault/Homicide Circumstances). 
			//Only those circumstances listed in Volume 1, section VI, are valid for the particular offense.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "01");
			
			incidents.add(copy);
						
			return incidents;
			
		});
		
		groupATweakerMap.put(479, incident -> {
			//A Simple Assault (13B) was committed against a victim, but the 
			//victim had major injuries/trauma entered for Data Element 33 (Type Injury). 
			//Either the offense should have been classified as an Aggravated Assault (13A) 
			//or the victim�s injury should not have been entered as major.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			copy.getVictims().get(0).setTypeOfInjury(0, "O");
			
			incidents.add(copy);
			
			
			return incidents;
			
		});
		groupATweakerMap.put(481, incident -> {
			//Data Element 26 (Age of Victim) should be under 18 when Data Element 24
			//(Victim Connected to UCR Offense Code) is 36B=Statutory Rape.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("36B");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "36B");
			copy.getVictims().get(0).setAgeString("09");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(482, incident -> {
			//(Type of Victim) cannot be L=Law Enforcement Officer unless Data Element 24 
			//(Victim Connected to UCR Offense Code) is one of the following:
			//      09A=Murder & Non-negligent Manslaughter
			//		13A=Aggravated Assault
			//		13B=Simple Assault
			//		13C=Intimidation
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "200");
			copy.getVictims().get(0).setTypeOfVictim("L");
			
			incidents.add(copy);
			
			return incidents;
					
		});
	
		groupATweakerMap.put(483, incident -> {
			//(Type of Officer Activity/Circumstance) Data Element 25B (Officer Assignment Type), 
			//Data Element 25C (Officer�ORI Other Jurisdiction), Data Element 26 (Age of Victim), 
			//Data Element 27 (Sex of Victim), Data Element 28 (Race of Victim), 
			//Data Element 29 (Ethnicity of Victim), Data Element 30 (Resident Status of Victim), and 
			//Data Element 34 (Offender Number to be Related) can only be entered when 
			//Data Element 25 (Type of Victim) is I=Individual or L=Law Enforcement Officer.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy.getVictims().get(0).setOfficerAssignmentType("G");
			copy.getVictims().get(0).setOfficerOtherJurisdictionORI("321456789");
						
			incidents.add(copy);
						
			
			return incidents;
					
		});
		
	}
}
