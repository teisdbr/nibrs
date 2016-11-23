package org.search.nibrs.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;

/**
 * Utility class that creates test incidents.
 *
 */
final class BaselineIncidentFactory {
	
	private static final BaselineIncidentFactory INSTANCE = new BaselineIncidentFactory();
	
	public static final BaselineIncidentFactory getInstance() {
		return INSTANCE;
	}

	static GroupAIncidentReport getBaselineIncident() {
		
		GroupAIncidentReport incident = new GroupAIncidentReport();
		
		ReportSource source = new ReportSource();
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		source.setSourceLocation(trace[1].toString());
		source.setSourceName(BaselineIncidentFactory.class.getName());
		incident.setSource(source);
		
		incident.setYearOfTape(2016);
		incident.setMonthOfTape(5);
		incident.setOri("WA123456789");
		incident.setIncidentNumber("54236732");
		incident.setIncidentDate(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		incident.setExceptionalClearanceCode("A");
		incident.setExceptionalClearanceDate(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		incident.setCityIndicator("GAA7");
		incident.setReportDateIndicator(null);		
		OffenseSegment o = new OffenseSegment();
		
		o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode("13A");
		o.setTypeOfCriminalActivity(0, "J");
		o.setOffenseAttemptedCompleted("C");
		o.setTypeOfWeaponForceInvolved(0, "99");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setBiasMotivation(0, "15");
		o.setLocationType("15");
		o.setNumberOfPremisesEntered(null);
		o.setAutomaticWeaponIndicator(0, " ");
		
		VictimSegment v = new VictimSegment();
		incident.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setVictimSequenceNumber(1);
		v.setAgeString("2022");
		v.setEthnicity("N");
		v.setResidentStatus("R");
		v.setSex("F");
		v.setRace("B");
		v.setOffenderNumberRelated(0, 1);
		v.setVictimOffenderRelationship(0, "SE");
		v.setUcrOffenseCodeConnection(0, "13A");
		
		OffenderSegment offender = new OffenderSegment();
		incident.addOffender(offender);
		offender.setOffenderSequenceNumber(1);
		offender.setAgeString("22");
		offender.setRace("W");
		offender.setSex("M");
		offender.setEthnicity("H");
		
		ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		incident.addArrestee(arrestee);
		arrestee.setArresteeSequenceNumber(1);
		arrestee.setAgeString("22");
		arrestee.setRace("W");
		arrestee.setSex("M");
		arrestee.setArrestTransactionNumber("12345");
		arrestee.setArrestDate(Date.from(LocalDate.of(2015, 5, 16).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		arrestee.setUcrArrestOffenseCode("13A");
		arrestee.setTypeOfArrest("O");
		arrestee.setMultipleArresteeSegmentsIndicator("N");
		arrestee.setArresteeArmedWith(0,"01");
		
		return incident;
		
	}
	
	static GroupBArrestReport getBaselineGroupBArrestReport() {
		
		GroupBArrestReport report = new GroupBArrestReport();
		
		// TODO:  need to set up the baseline properties
		
		return report;
		
	}
	
}
