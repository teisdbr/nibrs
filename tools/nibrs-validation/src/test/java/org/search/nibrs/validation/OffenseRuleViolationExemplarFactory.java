package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.codes.LocationTypeCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfWeaponForceCode;

final class OffenseRuleViolationExemplarFactory {
	
	private static final OffenseRuleViolationExemplarFactory INSTANCE = new OffenseRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(OffenseRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private OffenseRuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final OffenseRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> getGroupATweakerMap() {
		return groupATweakerMap;
	}

	private void populateGroupAExemplarMap() {
		
		groupATweakerMap.put(201, incident -> {
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 2 is mandatory & must be present.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("XXX");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			//Data Element 6
			copy.getOffenses().get(0).setUcrOffenseCode(null);
			incidents.add(copy);
			//Data Element 7
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setOffenseAttemptedCompleted(null);
			incidents.add(copy);
			//Data Element 8
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, null);
			incidents.add(copy);
			//Data Element 8A
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setBiasMotivation(0, null);
			incidents.add(copy);
			//Data Element 9
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setLocationType(null);
			incidents.add(copy);
						
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode(null);
			copy.getOffenses().get(0).setOffenseAttemptedCompleted(null);
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, null);
			copy.getOffenses().get(0).setBiasMotivation(0, null);
			copy.getOffenses().get(0).setLocationType(null);
			incidents.add(copy);

			return incidents;
		});

		//TO-DO Rule 202
		//Data Element 10 (Number of Premises Entered) is not a numeric entry of 01 through 99.	
		
		groupATweakerMap.put(204, incident -> {
			// The referenced data element in a Group A Incident AbstractReport Segment 2 must
			// be populated with a valid data value.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setBiasMotivation(0, "10");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setLocationType("99");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setMethodOfEntry("X");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, "XX");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "10");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setAutomaticWeaponIndicator(0, "B");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setNumberOfPremisesEntered(100);
			incidents.add(copy);
			
			return incidents;
			
		});

		//TO-DO Rule 205 
		//Data Element 9 (Location Type) Can only be entered when Data Element 6 Offense Code is one of the violations listed below:
		//	210=Extortion/Blackmail
		//	250=Counterfeiting/Forgery
		//	270=Embezzlement
		//	280=Stolen Property Offenses
		//	290=Destruction/Damage/Vandalism of Property
		//	370=Pornography/Obscene Material
		//	510=Bribery
		//	26A =False Pretenses/Swindle/Confidence Game
		//	26B =Credit Card/Automated Teller Machine Fraud
		//	26C =Impersonation
		//	26D =Welfare Fraud
		//	26E =Wire Fraud
		//	26F =Identity Theft
		//	26G =Hacking/Computer Invasion
		//	9A =Betting/Wagering
		//	39B =Operating/Promoting/Assisting Gambling
		//	39D =Gambling Equipment Violations
		//	13C =Intimidation
		//	35A =Drug/Narcotic Violations
		//	35B =Drug Equipment Violations
		//	520=Weapon Law Violations
		//	64A =Human Trafficking, Commercial Sex Acts
		//	64B =Human Trafficking, Involuntary Servitude
		//	40A =Prostitution
		//	40B =Assisting or Promoting Prostitution
		//	40C =Purchasing Prostitution
			
		groupATweakerMap.put(206, incident -> {
			// The referenced data element in error is one that contains multiple
			// data values. When more than one code is entered, none can be duplicate codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//Data Element 8			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "A");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(1, "C");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(2, "C");
			incidents.add(copy);
			//Data Element 8A
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setBiasMotivation(0, "15");
			copy.getOffenses().get(0).setBiasMotivation(1, "26");
			copy.getOffenses().get(0).setBiasMotivation(2, "26");
			incidents.add(copy);
			//Data Element 12
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setTypeOfCriminalActivity(1, "J");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(2, "P");
			incidents.add(copy);
			//Data Element 13
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "11");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(1, "11");
			copy.getOffenses().get(0).setBiasMotivation(0, "15");
			copy.getOffenses().get(0).setBiasMotivation(1, "26");
			copy.getOffenses().get(0).setBiasMotivation(2, "26");
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(207, incident -> {
			// The referenced data element in error is one that contains multiple
			// data values. However values are mutually exclusive with other codes.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//Data Element 8 Offender Suspected of Using "N" is exclusive of other values
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(0, "A");
			copy.getOffenses().get(0).setOffendersSuspectedOfUsing(1, "N");
			incidents.add(copy);
			//Data Element 8A Bias Motivation "88" is exclusive of other values
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setBiasMotivation(0, "15");
			copy.getOffenses().get(0).setBiasMotivation(1, "88");
			incidents.add(copy);
			//Data Element 12 Type of Criminal Activity "N" is exclusive of other values
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, "J");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(1, "N");
			incidents.add(copy);
			//Data Element 13 Type of Weapon Force Involved "99" is exclusive of other values
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "11");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(1, "99");
			incidents.add(copy);
			
			return incidents;

		});
		
		groupATweakerMap.put(219, incident -> {
			//TO-DO Incomplete scenarios.
			//(Type Criminal Activity/Gang Information) Type criminal activity codes of B, C, D, E, O, P, T,
			//or U can only be entered when the UCR Offense Code is:
			//250=Counterfeiting/Forgery
			//280=Stolen Property Offenses
			//35A=Drug/Narcotic Violations
			//35B=Drug Equipment Violations
			//39C=Gambling Equipment Violations
			//370=Pornography/Obscene Material
			//520=Weapon Law Violations
			//(Type Criminal Activity/Gang Information) Gang information codes of J, G, and N can only be entered
			//when the UCR Offense Code is:09A=Murder and Non-negligent Manslaughter
			//09B=Negligent Manslaughter
			//100=Kidnapping/Abduction
			//11A=Rape
			//11B=Sodomy
			//11C=Sexual Assault With An Object
			//11D=Fondling
			//120=Robbery
			//13A=Aggravated Assault
			//13B=Simple Assault
			//13C=Intimidation
			//(Type Criminal Activity/Gang Information) Criminal Activity codes of 
			//A, F, I, and S can only be entered when the UCR Offense Code is:
			//720=Animal Cruelty
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09B");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, "B");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09B");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, "C");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(1, "J");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("250");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, "J");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("720");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, "J");
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(220, incident -> {
			// Data Element 12 (Type Criminal Activity/Gang Information) Must be populated with a valid data value and cannot be 
			//blank when Data Element 6 (UCR OffenseSegment Code) is:
			// 250=Counterfeiting/Forgery
			// 280=Stolen PropertySegment Offenses
			// 35A=Drug/Narcotic Violations
			// 35B=Drug Equipment Violations
			// 39C=Gambling Equipment Violations
			// 370=Pornography/Obscene Material
			// 520=Weapon Law Violations
			// 720=Animal Cruelty
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//TypeOfCriminalActivity cannot be blank
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("250");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("280");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("35A");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("35B");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("39C");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("370");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("520");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("720");
			copy.getOffenses().get(0).setTypeOfCriminalActivity(0, null);
			incidents.add(copy);
			
			
			return incidents;

		});

		groupATweakerMap.put(221, incident -> {
			// Data Element 13 (Type Weapon/Force Involved) Must be populated with a valid data value and cannot be blank
			//when Data Element 6 (UCR OffenseSegment Code) is:
			//09A=Murder and Non-negligent Manslaughter
			//09B=Negligent Manslaughter
			//09C=Justifiable Homicide
			//100=Kidnapping/Abduction
			//11A=Rape
			//11B=Sodomy
			//11C=Sexual Assault With An Object
			//11D=Fondling
			//120=Robbery
			//13A=Aggravated Assault
			//13B=Simple Assault
			//210=Extortion/Blackmail
			//520=Weapon Law Violations
			//64A=Human Trafficking, Commercial Sex Acts
			//64B=Human Trafficking, Involuntary Servitude
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//TypeOfWeaponForceInvolved cannot be blank
			incident = new GroupAIncidentReport(incident);
			incident.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, null);
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09A");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09B");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("100");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("11A");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("11B");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("11C");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("11D");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("120");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13A");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("210");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("520");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("64A");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("64B");
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(251, incident -> {
			// (OffenseSegment Attempted/Completed) Must be a valid code of A=Attempted or C=Completed.
			GroupAIncidentReport ret = new GroupAIncidentReport(incident);
			ret.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			return Collections.singletonList(ret);
		});

		groupATweakerMap.put(252, incident -> {

			// When number of premises is entered location type must be 14 or 19
			// and UCR OffenseSegment Code must be Burglary.

			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();

			Set<OffenseCode> offenseCodeSet = EnumSet.allOf(OffenseCode.class);
			offenseCodeSet.remove(OffenseCode._220); // Burglary

			Set<LocationTypeCode> locationTypeCodeSet = LocationTypeCode.asSet();
			locationTypeCodeSet.remove(LocationTypeCode._14);
			locationTypeCodeSet.remove(LocationTypeCode._19);

			for (OffenseCode oc : offenseCodeSet) {
				for (LocationTypeCode ltc : locationTypeCodeSet) {
					GroupAIncidentReport copy = new GroupAIncidentReport(incident);
					OffenseSegment offense = new OffenseSegment();
					offense.setNumberOfPremisesEntered(2);
					offense.setLocationType(ltc.code);
					offense.setUcrOffenseCode(oc.code);
					copy.addOffense(offense);
					incidents.add(copy);
				}
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				OffenseSegment offense = new OffenseSegment();
				offense.setNumberOfPremisesEntered(2);
				offense.setLocationType(LocationTypeCode._14.code);
				offense.setUcrOffenseCode(oc.code);
				copy.addOffense(offense);
				incidents.add(copy);
			}

			for (LocationTypeCode ltc : locationTypeCodeSet) {
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				OffenseSegment offense = new OffenseSegment();
				offense.setNumberOfPremisesEntered(2);
				offense.setLocationType(ltc.code);
				offense.setUcrOffenseCode(OffenseCode._220.code);
				copy.addOffense(offense);
				incidents.add(copy);
			}

			return incidents;

		});

		
		groupATweakerMap.put(253, incident -> {
			// Data Element 11 (Method of Entry) Data Element was not entered; it must be entered
			// when UCR OffenseSegment Code of 220=Burglary has been entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getOffenses().get(0).setMethodOfEntry(null);
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(254, incident -> {
			// (Method of Entry) Data Element only applies to UCR OffenseSegment Code of 220=Burglary.
			// Since a burglary offense was not entered, the Method of Entry should not have been entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setMethodOfEntry("F");
			
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(255, incident -> {
			// Data Element 13(Automatic Weapon Indicator) Must be A=Automatic or blank=Not Automatic
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setAutomaticWeaponIndicator(0, "F");
			
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(256, incident -> {
			// OffenseSegment Attempted/Completed, Data Element 7, must be C=Completed if UCR code is Homicide or
			// Assault.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			//Homicide Offenses
			copy.getOffenses().get(0).setUcrOffenseCode("09A");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09A");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("A");
			incidents.add(copy);
			copy.getOffenses().get(0).setUcrOffenseCode("09B");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09B");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("A");
			incidents.add(copy);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("A");
			incidents.add(copy);
			//Assault Charges
			copy.getOffenses().get(0).setUcrOffenseCode("13A");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13AA");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("A");
			incidents.add(copy);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("A");
			incidents.add(copy);
			copy.getOffenses().get(0).setUcrOffenseCode("13C");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("X");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13C");
			copy.getOffenses().get(0).setOffenseAttemptedCompleted("A");
			incidents.add(copy);

		
			
			return incidents;
		});

		groupATweakerMap.put(257, incident -> {
			// (Number of Premises Entered) Must be entered if offense code is 220 (Burglary)
			// and if Data Element 9 (Location Type) contains 14=Hotel/Motel/Etc. or 19=Rental Storage Facility.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getOffenses().get(0).setLocationType("14");
			copy.getOffenses().get(0).setNumberOfPremisesEntered(null);
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getOffenses().get(0).setLocationType("19");
			copy.getOffenses().get(0).setNumberOfPremisesEntered(null);
								
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(258, incident -> {
			// (Automatic Weapon Indicator) In Data Element 13 (Type of Weapon/Force Involved), A=Automatic 
			//is the third character of code. It is valid only with the following codes:
			// 11=Firearm (Type Not Stated)
			// 12=Handgun
			// 13=Rifle
			// 15=Other Firearm
			// A weapon code other than those mentioned was entered with the automatic indicator. An automatic weapon is, by definition, a firearm.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setAutomaticWeaponIndicator(0, "A");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "20");

			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(262, incident -> {
			// When a Group A Incident Report is submitted, the individual segments
			// comprising the incident cannot contain duplicates.
			// In this case, two OffenseSegment Segments were submitted having the same
			// offense in Data Element 6 (UCR OffenseSegment Code).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("13A");
			offense2.setTypeOfCriminalActivity(0, "J");
			offense2.setOffenseAttemptedCompleted("C");
			offense2.setTypeOfWeaponForceInvolved(0, "99");
			offense2.setOffendersSuspectedOfUsing(0, "N");
			offense2.setBiasMotivation(0, "15");
			offense2.setLocationType("15");
			offense2.setNumberOfPremisesEntered(null);
			offense2.setAutomaticWeaponIndicator(0, " ");
			
					
			copy.addOffense(offense2);
			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(263, incident -> {
			// Can be submitted only 10 times for each Group A Incident AbstractReport;
			// 10 offense codes are allowed for each incident.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			//Offense Segment 2
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("13A");
			offense2.setTypeOfCriminalActivity(0, "J");
			offense2.setOffenseAttemptedCompleted("C");
			offense2.setTypeOfWeaponForceInvolved(0, "99");
			offense2.setOffendersSuspectedOfUsing(0, "N");
			offense2.setBiasMotivation(0, "15");
			offense2.setLocationType("15");
			offense2.setNumberOfPremisesEntered(null);
			offense2.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 3
			OffenseSegment offense3 = new OffenseSegment();
			offense3.setUcrOffenseCode("13B");
			offense3.setTypeOfCriminalActivity(0, "J");
			offense3.setOffenseAttemptedCompleted("C");
			offense3.setTypeOfWeaponForceInvolved(0, "99");
			offense3.setOffendersSuspectedOfUsing(0, "N");
			offense3.setBiasMotivation(0, "15");
			offense3.setLocationType("15");
			offense3.setNumberOfPremisesEntered(null);
			offense3.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 4
			OffenseSegment offense4 = new OffenseSegment();
			offense4.setUcrOffenseCode("13C");
			offense4.setTypeOfCriminalActivity(0, "J");
			offense4.setOffenseAttemptedCompleted("C");
			offense4.setTypeOfWeaponForceInvolved(0, "99");
			offense4.setOffendersSuspectedOfUsing(0, "N");
			offense4.setBiasMotivation(0, "15");
			offense4.setLocationType("15");
			offense4.setNumberOfPremisesEntered(null);
			offense4.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 5
			OffenseSegment offense5 = new OffenseSegment();
			offense5.setUcrOffenseCode("510");
			offense5.setTypeOfCriminalActivity(0, "J");
			offense5.setOffenseAttemptedCompleted("C");
			offense5.setTypeOfWeaponForceInvolved(0, "99");
			offense5.setOffendersSuspectedOfUsing(0, "N");
			offense5.setBiasMotivation(0, "15");
			offense5.setLocationType("15");
			offense5.setNumberOfPremisesEntered(null);
			offense5.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 6
			OffenseSegment offense6 = new OffenseSegment();
			offense6.setUcrOffenseCode("26A");
			offense6.setTypeOfCriminalActivity(0, "J");
			offense6.setOffenseAttemptedCompleted("C");
			offense6.setTypeOfWeaponForceInvolved(0, "99");
			offense6.setOffendersSuspectedOfUsing(0, "N");
			offense6.setBiasMotivation(0, "15");
			offense6.setLocationType("15");
			offense6.setNumberOfPremisesEntered(null);
			offense6.setAutomaticWeaponIndicator(0, " ");
			//Offense Segment 7
			OffenseSegment offense7 = new OffenseSegment();
			offense7.setUcrOffenseCode("26B");
			offense7.setTypeOfCriminalActivity(0, "J");
			offense7.setOffenseAttemptedCompleted("C");
			offense7.setTypeOfWeaponForceInvolved(0, "99");
			offense7.setOffendersSuspectedOfUsing(0, "N");
			offense7.setBiasMotivation(0, "15");
			offense7.setLocationType("15");
			offense7.setNumberOfPremisesEntered(null);
			offense7.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 8
			OffenseSegment offense8 = new OffenseSegment();
			offense8.setUcrOffenseCode("26C");
			offense8.setTypeOfCriminalActivity(0, "J");
			offense8.setOffenseAttemptedCompleted("C");
			offense8.setTypeOfWeaponForceInvolved(0, "99");
			offense8.setOffendersSuspectedOfUsing(0, "N");
			offense8.setBiasMotivation(0, "15");
			offense8.setLocationType("15");
			offense8.setNumberOfPremisesEntered(null);
			offense8.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 9
			OffenseSegment offense9 = new OffenseSegment();
			offense9.setUcrOffenseCode("26D");
			offense9.setTypeOfCriminalActivity(0, "J");
			offense9.setOffenseAttemptedCompleted("C");
			offense9.setTypeOfWeaponForceInvolved(0, "99");
			offense9.setOffendersSuspectedOfUsing(0, "N");
			offense9.setBiasMotivation(0, "15");
			offense9.setLocationType("15");
			offense9.setNumberOfPremisesEntered(null);
			offense9.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 10
			OffenseSegment offense10 = new OffenseSegment();
			offense10.setUcrOffenseCode("26E");
			offense10.setTypeOfCriminalActivity(0, "J");
			offense10.setOffenseAttemptedCompleted("C");
			offense10.setTypeOfWeaponForceInvolved(0, "99");
			offense10.setOffendersSuspectedOfUsing(0, "N");
			offense10.setBiasMotivation(0, "15");
			offense10.setLocationType("15");
			offense10.setNumberOfPremisesEntered(null);
			offense10.setAutomaticWeaponIndicator(0, " ");
			//OffenseSegment 11
			OffenseSegment offense11 = new OffenseSegment();
			offense11.setUcrOffenseCode("39A");
			offense11.setTypeOfCriminalActivity(0, "J");
			offense11.setOffenseAttemptedCompleted("C");
			offense11.setTypeOfWeaponForceInvolved(0, "99");
			offense11.setOffendersSuspectedOfUsing(0, "N");
			offense11.setBiasMotivation(0, "15");
			offense11.setLocationType("15");
			offense11.setNumberOfPremisesEntered(null);
			offense11.setAutomaticWeaponIndicator(0, " ");
			
				
			copy.addOffense(offense2);
			copy.addOffense(offense3);		
			copy.addOffense(offense4);
			copy.addOffense(offense5);
			copy.addOffense(offense6);
			copy.addOffense(offense7);
			copy.addOffense(offense8);
			copy.addOffense(offense9);
			copy.addOffense(offense10);
			copy.addOffense(offense11);

			incidents.add(copy);
			return incidents;
		});

		groupATweakerMap.put(264, incident -> {
			// Group A OffenseSegment code cannot contain a Group B OffenseSegment
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("90A");
					
			incidents.add(copy);
			
			return incidents;
		});

		groupATweakerMap.put(265, incident -> {
			
			// (Type Weapon/Force Involved) If an OffenseSegment Segment (Level 2) was submitted for 13B=Simple Assault,
			// Data Element 13 (Type Weapon/Force Involved) can only have codes of 40=Personal Weapons,
			// 90=Other, 95=Unknown, and 99=None. All other codes are not valid because they do not relate to a simple assault.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			Set<String> simpleAssaultDisallowedCodes = TypeOfWeaponForceCode.codeSet();
			simpleAssaultDisallowedCodes.remove(TypeOfWeaponForceCode._40.code);
			simpleAssaultDisallowedCodes.remove(TypeOfWeaponForceCode._90.code);
			simpleAssaultDisallowedCodes.remove(TypeOfWeaponForceCode._95.code);
			simpleAssaultDisallowedCodes.remove(TypeOfWeaponForceCode._99.code);
			
			for (String code : simpleAssaultDisallowedCodes) {
				GroupAIncidentReport copy = new GroupAIncidentReport(incident);
				copy.getOffenses().get(0).setUcrOffenseCode("13B");
				copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, code);
				incidents.add(copy);
			}
			
			return incidents;
			
		});

		groupATweakerMap.put(266, incident -> {
			// When a Justifiable Homicide is reported, no other offense may be
			// reported in the Group A Incident AbstractReport. These should be submitted on another
			// Group A Incident AbstractReport.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense2 = new OffenseSegment();
			offense2.setUcrOffenseCode("09C");
			offense2.setTypeOfCriminalActivity(0, "J");
			offense2.setOffenseAttemptedCompleted("C");
			offense2.setTypeOfWeaponForceInvolved(0, "1199");
			offense2.setOffendersSuspectedOfUsing(0, "N");
			offense2.setBiasMotivation(0, "03");
			offense2.setLocationType("15");
			offense2.setNumberOfPremisesEntered(null);
			offense2.setAutomaticWeaponIndicator(0, " ");
			
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(267, incident -> {
			// If a homicide offense is submitted, Data Element 13 (Type Weapon/Force Involved)
			// cannot have 99=None. Some type of weapon/force must be used in a homicide offense.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09A");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "99");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09B");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "99");
			incidents.add(copy);
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "99");
			incidents.add(copy);
			
			return incidents;

		});

		groupATweakerMap.put(268, incident -> {
			//(Offense Segment) Cannot be submitted with a data value for a 
			//motor vehicle in Data Element 15 (Property Description) when Data Element 6 
			//(UCR Offense Code) contains an offense of (23A23H)=Larceny/Theft Offenses; 
			//stolen vehicles cannot be reported for a larceny
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("23A");
			PropertySegment property = new PropertySegment();
			copy.addProperty(property);
			property.setTypeOfPropertyLoss("07");
			property.setPropertyDescription(0, "03");
			property.setValueOfProperty(0, 10000);
			//23B
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("23B");
			PropertySegment property2 = new PropertySegment();
			copy2.addProperty(property);
			property2.setTypeOfPropertyLoss("07");
			property2.setPropertyDescription(0, "03");
			property2.setValueOfProperty(0, 10000);
			//23C
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setUcrOffenseCode("23C");
			PropertySegment property3 = new PropertySegment();
			copy3.addProperty(property);
			property3.setTypeOfPropertyLoss("07");
			property3.setPropertyDescription(0, "03");
			property3.setValueOfProperty(0, 10000);
			//23D
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setUcrOffenseCode("23D");
			PropertySegment property4 = new PropertySegment();
			copy4.addProperty(property);
			property4.setTypeOfPropertyLoss("07");
			property4.setPropertyDescription(0, "03");
			property4.setValueOfProperty(0, 10000);
			//23E
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getOffenses().get(0).setUcrOffenseCode("23E");
			PropertySegment property5 = new PropertySegment();
			copy5.addProperty(property);
			property5.setTypeOfPropertyLoss("07");
			property5.setPropertyDescription(0, "03");
			property5.setValueOfProperty(0, 10000);
			//23F
			GroupAIncidentReport copy6 = new GroupAIncidentReport(incident);
			copy6.getOffenses().get(0).setUcrOffenseCode("23F");
			PropertySegment property6 = new PropertySegment();
			copy6.addProperty(property);
			property6.setTypeOfPropertyLoss("07");
			property6.setPropertyDescription(0, "03");
			property6.setValueOfProperty(0, 10000);
			//23G
			GroupAIncidentReport copy7 = new GroupAIncidentReport(incident);
			copy7.getOffenses().get(0).setUcrOffenseCode("23G");
			PropertySegment property7 = new PropertySegment();
			copy7.addProperty(property);
			property7.setTypeOfPropertyLoss("07");
			property7.setPropertyDescription(0, "03");
			property7.setValueOfProperty(0, 10000);
			//23H
			GroupAIncidentReport copy8 = new GroupAIncidentReport(incident);
			copy8.getOffenses().get(0).setUcrOffenseCode("23H");
			PropertySegment property8 = new PropertySegment();
			copy8.addProperty(property);
			property8.setTypeOfPropertyLoss("07");
			property8.setPropertyDescription(0, "03");
			property8.setValueOfProperty(0, 10000);
			
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			incidents.add(copy6);
			incidents.add(copy7);
			incidents.add(copy8);
			
					
			return incidents;
			
			
		});
		
		
		
		groupATweakerMap.put(269, incident -> {
			// (Type Weapon/Force Involved) If Data Element 6 (UCR OffenseSegment Code) is 13B=Simple Assault and the
			// weapon involved is 11=Firearm, 12=Handgun, 13=Rifle, 14=Shotgun, or 15=Other Firearm, then the offense
			// should instead be classified as 13A=Aggravated Assault.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			copy.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "11");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("13B");
			copy2.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "12");
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getOffenses().get(0).setUcrOffenseCode("13B");
			copy3.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "13");
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getOffenses().get(0).setUcrOffenseCode("13B");
			copy4.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "14");
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getOffenses().get(0).setUcrOffenseCode("13B");
			copy5.getOffenses().get(0).setTypeOfWeaponForceInvolved(0, "15");
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			
			return incidents;

		});

		groupATweakerMap.put(270, incident -> {
			// If a justifiable homicide offense is submitted, Data Element 8A (Bias motivation) must be 88.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getOffenses().get(0).setBiasMotivation(0, "15");
						
			incidents.add(copy);
			
			return incidents;

		});


	}

}
