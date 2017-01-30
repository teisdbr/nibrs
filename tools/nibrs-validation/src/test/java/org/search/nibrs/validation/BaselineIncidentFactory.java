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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.search.nibrs.common.ParsedObject;
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
		incident.setIncidentDate(new ParsedObject<>(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		incident.setExceptionalClearanceCode("A");
		incident.setExceptionalClearanceDate(new ParsedObject<>(Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		incident.setCityIndicator("GAA7");
		incident.setReportDateIndicator(null);	
		
		OffenseSegment o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode("13A");
		o.setTypeOfCriminalActivity(0, "J");
		o.setOffenseAttemptedCompleted("C");
		o.setTypeOfWeaponForceInvolved(0, "99");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setBiasMotivation(0, "15");
		o.setLocationType("15");
		o.setNumberOfPremisesEntered(ParsedObject.getMissingParsedObject());
		o.setAutomaticWeaponIndicator(0, " ");
		
		VictimSegment v = new VictimSegment();
		incident.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setVictimSequenceNumber(new ParsedObject<>(1));
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
		offender.setOffenderSequenceNumber(new ParsedObject<>(1));
		offender.setAgeString("22");
		offender.setRace("W");
		offender.setSex("M");
		offender.setEthnicity("H");
		
		ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		incident.addArrestee(arrestee);
		arrestee.setArresteeSequenceNumber(new ParsedObject<>(1));
		arrestee.setArrestTransactionNumber("12345");
		arrestee.setArrestDate(new ParsedObject<>(Date.from(LocalDate.of(2015, 5, 16).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		arrestee.setTypeOfArrest("O");
		arrestee.setMultipleArresteeSegmentsIndicator("N");
		arrestee.setUcrArrestOffenseCode("13A");
		arrestee.setArresteeArmedWith(0,"01");
		arrestee.setAgeString("22");
		arrestee.setSex("M");
		arrestee.setRace("W");
		arrestee.setEthnicity("U");
		arrestee.setResidentStatus("R");
			
				
		return incident;
		
	}
	
	static GroupBArrestReport getBaselineGroupBArrestReport() {
		
		GroupBArrestReport report = new GroupBArrestReport();
		
		ReportSource source = new ReportSource();
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		source.setSourceLocation(trace[1].toString());
		source.setSourceName(BaselineIncidentFactory.class.getName());
		report.setSource(source);
		
		ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		report.addArrestee(arrestee);
		arrestee.setArresteeSequenceNumber(new ParsedObject<>(1));
		arrestee.setArrestTransactionNumber("12345");
		arrestee.setArrestDate(new ParsedObject<>(Date.from(LocalDate.of(2015, 5, 16).atStartOfDay(ZoneId.systemDefault()).toInstant())));
		arrestee.setTypeOfArrest("O");
		arrestee.setUcrArrestOffenseCode("90A");
		arrestee.setArresteeArmedWith(0, "01");
		arrestee.setAgeString("22");
		arrestee.setSex("M");
		arrestee.setRace("W");
		arrestee.setEthnicity("U");
		arrestee.setResidentStatus("R");
		
		return report;
		
	}
	
}
