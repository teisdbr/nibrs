package org.search.nibrs.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.search.nibrs.model.Arrestee;
import org.search.nibrs.model.Incident;
import org.search.nibrs.model.Offender;
import org.search.nibrs.model.Offense;
import org.search.nibrs.model.Property;
import org.search.nibrs.model.Victim;

/**
 * Utility class that creates test incidents.
 *
 */
final class BaselineIncidentFactory {
	
	private static final BaselineIncidentFactory INSTANCE = new BaselineIncidentFactory();
	
	public static final BaselineIncidentFactory getInstance() {
		return INSTANCE;
	}

	static Incident getBaselineIncident() {
		
		Incident incident = new Incident();
		incident.setYearOfTape(2016);
		incident.setMonthOfTape(5);
		incident.setOri("WA123456789");
		incident.setIncidentNumber("54236732");
		incident.setIncidentDate(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		incident.setExceptionalClearanceCode("N");
		
		Offense o = new Offense();
		incident.addOffense(o);
		o.setUcrOffenseCode("64A");
		o.setBiasMotivation(0, "88");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setTypeOfWeaponForceInvolved(0, "11A");
		o.setOffenseAttemptedCompleted("C");
		o.setLocationType("01");
		
		o = new Offense();
		incident.addOffense(o);
		o.setUcrOffenseCode("220");
		o.setNumberOfPremisesEntered(1);
		o.setMethodOfEntry("F");
		o.setBiasMotivation(0, "21");
		o.setBiasMotivation(1, "22");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setOffenseAttemptedCompleted("A");
		o.setLocationType("14");
		
		o = new Offense();
		incident.addOffense(o);
		o.setUcrOffenseCode("720");
		o.setBiasMotivation(0, "88");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setOffenseAttemptedCompleted("C");
		o.setTypeOfCriminalActivity(0, "A");
		o.setLocationType("20");
		
		o = new Offense();
		incident.addOffense(o);
		o.setUcrOffenseCode("13A");
		o.setOffenseAttemptedCompleted("C");
		o.setLocationType("15");
		
		Property p = new Property();
		incident.addProperty(p);
		p.setTypeOfPropertyLoss("6");
		// seized 24 oz of LSD
		p.setPropertyDescription(0, "10");
		p.setEstimatedDrugQuantity(0, 24.0);
		p.setTypeDrugMeasurement(0, "OZ");
		p.setSuspectedDrugType(0, "I");
		p.setValueOfProperty(0, 500);
		// also seized some currency
		p.setPropertyDescription(1, "20");
		p.setValueOfProperty(1, 2000);
		
		p = new Property();
		incident.addProperty(p);
		// stole jewelry
		p.setTypeOfPropertyLoss("7");
		p.setPropertyDescription(0, "17");
		p.setValueOfProperty(0, 1000);
		
		p = new Property();
		incident.addProperty(p);
		// recovered some jewelry
		p.setTypeOfPropertyLoss("5");
		p.setPropertyDescription(0, "17");
		p.setValueOfProperty(0, 200);
		p.setDateRecovered(0, Date.from(LocalDate.of(2015, 5, 16).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		p = new Property();
		incident.addProperty(p);
		// stole car
		p.setTypeOfPropertyLoss("7");
		p.setPropertyDescription(0, "03");
		p.setValueOfProperty(0, 5000);
		p.setNumberOfStolenMotorVehicles(1);
		
		Victim v = new Victim();
		incident.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setVictimSequenceNumber(1);
		v.setAgeOfVictimString("2022");
		v.setEthnicityOfVictim("N");
		v.setResidentStatusOfVictim("R");
		v.setSexOfVictim("F");
		v.setRaceOfVictim("B");
		
		v = new Victim();
		incident.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setVictimSequenceNumber(2);
		v.setAgeOfVictimString("BB");
		v.setEthnicityOfVictim("N");
		v.setResidentStatusOfVictim("R");
		v.setSexOfVictim("F");
		v.setRaceOfVictim("B");
		
		v = new Victim();
		incident.addVictim(v);
		v.setVictimSequenceNumber(3);
		v.setTypeOfVictim("B");
		
		v = new Victim();
		incident.addVictim(v);
		v.setVictimSequenceNumber(4);
		v.setTypeOfVictim("L");
		v.setTypeOfInjury(0, "L");
		v.setAgeOfVictimString("30");
		v.setEthnicityOfVictim("N");
		v.setResidentStatusOfVictim("R");
		v.setSexOfVictim("F");
		v.setRaceOfVictim("B");
		v.setOfficerAssignmentType("F");
		v.setTypeOfOfficerActivityCircumstance("04");
		v.setOfficerOtherJurisdictionORI("WA987654321");
		
		Offender offender = new Offender();
		incident.addOffender(offender);
		offender.setOffenderSequenceNumber(1);
		offender.setAgeOfOffenderString("22");
		offender.setRaceOfOffender("W");
		offender.setSexOfOffender("M");
		
		Arrestee arrestee = new Arrestee();
		incident.addArrestee(arrestee);
		arrestee.setArresteeSequenceNumber(1);
		arrestee.setAgeOfArresteeString("22");
		arrestee.setRaceOfArrestee("W");
		arrestee.setSexOfArrestee("M");
		arrestee.setArrestTransactionNumber("12345");
		arrestee.setArrestDate(Date.from(LocalDate.of(2015, 5, 16).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		arrestee.setUcrArrestOffenseCode("64A");
		arrestee.setTypeOfArrest("O");
		arrestee.setMultipleArresteeSegmentsIndicator("N");
		
		return incident;
		
	}
	
}
