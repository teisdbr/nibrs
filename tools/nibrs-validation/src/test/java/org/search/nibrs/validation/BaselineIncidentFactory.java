package org.search.nibrs.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.search.nibrs.model.Arrestee;
import org.search.nibrs.model.Incident;
import org.search.nibrs.model.Offender;
import org.search.nibrs.model.Offense;
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
		incident.setExceptionalClearanceCode("A");
		incident.setCityIndicator("GAA7");
		incident.setReportDateIndicator("R");		
		Offense o = new Offense();
		
		o = new Offense();
		incident.addOffense(o);
		o.setUcrOffenseCode("13A");
		o.setOffenseAttemptedCompleted("C");
		o.setLocationType("15");
		
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
		v.setOffenderNumberRelated(0, 1);
		v.setVictimOffenderRelationship(0, "SE");
		v.setUcrOffenseCodeConnection(0, "13A");
		
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
		arrestee.setUcrArrestOffenseCode("13A");
		arrestee.setTypeOfArrest("O");
		arrestee.setMultipleArresteeSegmentsIndicator("N");
		
		return incident;
		
	}
	
}
