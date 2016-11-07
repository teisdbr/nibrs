package org.search.nibrs.validation.groupa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.AdditionalJustifiableHomicideCircumstancesCode;
import org.search.nibrs.model.codes.AggravatedAssaultHomicideCircumstancesCode;
import org.search.nibrs.model.codes.EthnicityOfVictim;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.OfficerAssignmentType;
import org.search.nibrs.model.codes.RaceOfVictimCode;
import org.search.nibrs.model.codes.RelationshipOfVictimToOffenderCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexOfVictimCode;
import org.search.nibrs.model.codes.TypeInjuryCode;
import org.search.nibrs.model.codes.TypeOfOfficerActivityCircumstance;
import org.search.nibrs.model.codes.TypeOfVictimCode;
import org.search.nibrs.validation.rules.Rule;

public class VictimSegmentRulesFactoryTest {
	
	private VictimSegmentRulesFactory victimRulesFactory = VictimSegmentRulesFactory.instance();
	
	@Test
	public void testRule401ForSequenceNumber(){
				
		Rule<VictimSegment> rule401 = victimRulesFactory.getRule401ForSequenceNumber();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setVictimSequenceNumber(0);
		NIBRSError nibrsError = rule401.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		assertEquals("23", nibrsError.getDataElementIdentifier());
		assertEquals(0, nibrsError.getValue());
		
		victimSegment.setVictimSequenceNumber(1000);
		nibrsError = rule401.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		assertEquals(1000, nibrsError.getValue());
		
		victimSegment.setVictimSequenceNumber(null);
		nibrsError = rule401.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		assertNull(nibrsError.getValue());
		
	}
	
	@Test
	public void testRule401ForVictimConnectedToUcrOffenseCode() {
		
		Rule<VictimSegment> rule401 = victimRulesFactory.getRule401ForVictimConnectedToUcrOffenseCode();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		OffenseSegment offenseSegment = ((GroupAIncidentReport) victimSegment.getParentReport()).getOffenses().get(0);
		offenseSegment.setUcrOffenseCode(OffenseCode._09A.code);
		
		NIBRSError nibrsError = rule401.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		assertEquals("24", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09B.code);
		nibrsError = rule401.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		assertEquals("24", nibrsError.getDataElementIdentifier());
		
		Set<String> compSet = new HashSet<>();
		compSet.add(OffenseCode._09B.code);
		
		assertEquals(compSet, nibrsError.getValue());
		
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule401.apply(victimSegment);
		assertNull(nibrsError);

	}
	
	@Test
	public void testRule401ForTypeOfVictim(){
		
		Rule<VictimSegment> rule401 = victimRulesFactory.getRule401ForTypeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test null value
		victimSegment.setTypeOfVictim(null);
		
		NIBRSError nibrsError = rule401.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		
		// test invalid code
		victimSegment.setTypeOfVictim("Z");
		
		nibrsError = rule401.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule404ForAgeOfVictim(){
		Rule<VictimSegment> ageRule404 = victimRulesFactory.getRule404ForAgeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		NIBRSError nibrsError = ageRule404.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule410ForAgeOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule410ForAgeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAgeString("3020");
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._410, nibrsError.getNIBRSErrorCode());
		assertEquals("26", nibrsError.getDataElementIdentifier());
		assertEquals(victimSegment.getAge(), nibrsError.getValue());

		victimSegment.setAgeString("2030");
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
	}

	@Test
	public void testRule422ForAgeOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule422ForAgeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAgeString("0020");
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._422, nibrsError.getNIBRSErrorCode());
		assertEquals("26", nibrsError.getDataElementIdentifier());
		assertEquals(victimSegment.getAge(), nibrsError.getValue());

		victimSegment.setAgeString("2030");
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
	}

	@Test
	public void testRule450ForAgeOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule450ForAgeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAgeString("0909");
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.SE.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._450, nibrsError.getNIBRSErrorCode());
		assertEquals("26", nibrsError.getDataElementIdentifier());
		assertEquals(victimSegment.getAge(), nibrsError.getValue());

		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

		victimSegment.setAgeString("1010");
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.SE.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
	}

	@Test
	public void testRule453ForAgeOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule453ForAgeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setAgeString(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
		assertEquals("26", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());

		victimSegment.setAgeString("3033");
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
	}

	@Test
	public void testRule453ForSexOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule453ForSexOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setSex(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
		assertEquals("27", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		
		victimSegment.setSex(SexOfVictimCode.M.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule453ForRaceOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule453ForRaceOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(RaceOfVictimCode.I.code);
		victimSegment.setRace(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
		assertEquals("28", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());

		victimSegment.setTypeOfVictim(RaceOfVictimCode.P.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule404ForEthnicityOfVictim() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForEthnicityOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setEthnicity(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setEthnicity("invalid");
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("29", nibrsError.getDataElementIdentifier());
		assertEquals("invalid", nibrsError.getValue());
		victimSegment.setEthnicity(EthnicityOfVictim.N.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule404ForResidentStatus() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForResidentStatusOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setResidentStatusOfVictim(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setResidentStatusOfVictim("invalid");
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("30", nibrsError.getDataElementIdentifier());
		assertEquals("invalid", nibrsError.getValue());
		victimSegment.setResidentStatusOfVictim(ResidentStatusCode.N.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule404ForAggravatedAssaultHomicideCircumstances() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForAggravatedAssaultHomicideCircumstances();

		VictimSegment victimSegment = getBasicVictimSegment();

		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09C.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("31", nibrsError.getDataElementIdentifier());

	}

	@Test
	public void testRule404OffenderNumberToBeRelated(){
		
		Rule<VictimSegment> offender404Rule = victimRulesFactory.getRule404OffenderNumberToBeRelated();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		NIBRSError nibrsError = offender404Rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertNull(nibrsError.getValue());
		
		OffenderSegment offenderSegment = ((GroupAIncidentReport) victimSegment.getParentReport()).getOffenders().get(0);
		offenderSegment.setOffenderSequenceNumber(1);
		
		victimSegment.setOffenderNumberRelated(0, 2);
		
		nibrsError = offender404Rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		Set<Integer> compSet = new HashSet<>();
		compSet.add(2);
		assertEquals(compSet, nibrsError.getValue());
		
		victimSegment.setOffenderNumberRelated(0, 1);
		nibrsError = offender404Rule.apply(victimSegment);
		assertNull(nibrsError);
		
	}
	
	@Test
	public void testRule404ForTypeOfficerActivityCircumstance() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForTypeOfOfficerActivityCircumstance();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfOfficerActivityCircumstance(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setTypeOfOfficerActivityCircumstance(TypeOfOfficerActivityCircumstance._01.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setTypeOfOfficerActivityCircumstance("invalid");
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("25A", nibrsError.getDataElementIdentifier());
		assertEquals("invalid", nibrsError.getValue());
	}

	@Test
	public void testRule454ForTypeOfOfficerActivityCircumstance() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule454ForTypeOfOfficerActivityCircumstance();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfOfficerActivityCircumstance(null);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());
		assertEquals("25A", nibrsError.getDataElementIdentifier());

		victimSegment.setTypeOfOfficerActivityCircumstance(TypeOfOfficerActivityCircumstance._01.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setTypeOfOfficerActivityCircumstance(null);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule454ForOfficerAssignmentType() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule454ForOfficerAssignmentType();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOfficerAssignmentType(null);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(victimSegment);
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());
		assertEquals("25B", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());

		victimSegment.setOfficerAssignmentType(OfficerAssignmentType.F.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setOfficerAssignmentType(null);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule454ForSexOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule454ForSexOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		victimSegment.setSex(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());
		assertEquals("27", nibrsError.getDataElementIdentifier());

		victimSegment.setSex(SexOfVictimCode.M.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setSex(null);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule454ForRaceOfVictim() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule454ForRaceOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		victimSegment.setRace(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());
		assertEquals("28", nibrsError.getDataElementIdentifier());

		victimSegment.setRace(RaceOfVictimCode.A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setRace(null);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule404ForOfficerAssignmentType() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForOfficerAssignmentType();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOfficerAssignmentType(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setOfficerAssignmentType(OfficerAssignmentType.F.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setOfficerAssignmentType("invalid");
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("25B", nibrsError.getDataElementIdentifier());
		assertEquals("invalid", nibrsError.getValue());
	}
	
	@Test
	public void testRule404ForOfficerOriOtherJurisdiction() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForOfficerOriOtherJurisdiction();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOfficerOtherJurisdictionORI(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setOfficerOtherJurisdictionORI("AB12345");
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	
	@Test
	public void testRule404ForSexOfVictim(){
		
		Rule<VictimSegment> sexOfVictim404Rule = victimRulesFactory.getRule404ForSexOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setSex(null);
		NIBRSError nibrsError = sexOfVictim404Rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("27", nibrsError.getDataElementIdentifier());
		assertNull(nibrsError.getValue());
		
		victimSegment.setSex("invalid");
		nibrsError = sexOfVictim404Rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals("invalid", nibrsError.getValue());
		
		victimSegment.setSex(SexOfVictimCode.F.code);
		nibrsError = sexOfVictim404Rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule404ForRaceOfVictim() {
		
		Rule<VictimSegment> raceOfVictim404Rule = victimRulesFactory.getRule404ForRaceOfVictim();
	
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setRace(null);
		NIBRSError nibrsError = raceOfVictim404Rule.apply(victimSegment);
		assertNotNull(nibrsError);	
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("28", nibrsError.getDataElementIdentifier());
		assertEquals(null, nibrsError.getValue());
		
		victimSegment.setRace("invalid");
		nibrsError = raceOfVictim404Rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals("invalid", nibrsError.getValue());
		
		victimSegment.setRace(RaceOfVictimCode.A.code);
		nibrsError = raceOfVictim404Rule.apply(victimSegment);
		assertNull(nibrsError);
		
	}
	
	
	@Test
	public void testRule404ForAdditionalJustifiableHomicideCircsumstances() {
		
		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForAdditionalJustifiableHomicideCircsumstances();

		VictimSegment victimSegment = getBasicVictimSegment();

		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

		victimSegment.setUcrOffenseCodeConnection(1, OffenseCode._09C.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("32", nibrsError.getDataElementIdentifier());
		
		victimSegment.setAdditionalJustifiableHomicideCircumstances(AdditionalJustifiableHomicideCircumstancesCode.A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule455ForAdditionalJustifiableHomicideCircsumstances() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule455ForAdditionalJustifiableHomicideCircsumstances();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._20.code);
		victimSegment.setAdditionalJustifiableHomicideCircumstances(null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._455, nibrsError.getNIBRSErrorCode());
		assertEquals("32", nibrsError.getDataElementIdentifier());
		assertEquals(Arrays.asList(new String[] {AggravatedAssaultHomicideCircumstancesCode._20.code}), nibrsError.getValue());
		victimSegment.setAdditionalJustifiableHomicideCircumstances(AdditionalJustifiableHomicideCircumstancesCode.A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule457ForAdditionalJustifiableHomicideCircsumstances() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule457ForAdditionalJustifiableHomicideCircsumstances();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._10.code);
		victimSegment.setAdditionalJustifiableHomicideCircumstances(AdditionalJustifiableHomicideCircumstancesCode.A.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._457, nibrsError.getNIBRSErrorCode());
		assertEquals("32", nibrsError.getDataElementIdentifier());
		assertEquals(AdditionalJustifiableHomicideCircumstancesCode.A.code, nibrsError.getValue());
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._20.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule419ForTypeOfInjury() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule419ForTypeOfInjury();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfInjury(0, TypeInjuryCode.B.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._419, nibrsError.getNIBRSErrorCode());
		assertEquals("33", nibrsError.getDataElementIdentifier());
		assertEquals(Arrays.asList(new String[] {TypeInjuryCode.B.code}), nibrsError.getValue());
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setTypeOfInjury(0, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setTypeOfInjury(0, TypeInjuryCode.B.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._100.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule419ForAggravatedAssaultHomicideCircumstances() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule419ForAggravatedAssaultHomicideCircumstances();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._01.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._11A.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._419, nibrsError.getNIBRSErrorCode());
		assertEquals("31", nibrsError.getDataElementIdentifier());
		assertEquals(Arrays.asList(new String[] {AggravatedAssaultHomicideCircumstancesCode._01.code}), nibrsError.getValue());

		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
	}

	@Test
	public void testRule458ForTypeOfInjury() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule458ForTypeOfInjury();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		victimSegment.setTypeOfInjury(0, TypeInjuryCode.B.code);

		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("33", nibrsError.getDataElementIdentifier());
		assertEquals(Arrays.asList(new String[] {TypeInjuryCode.B.code}), nibrsError.getValue());

		victimSegment.setTypeOfInjury(0, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setTypeOfInjury(0, TypeInjuryCode.B.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setTypeOfInjury(0, TypeInjuryCode.B.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		
	}

	@Test
	public void testRule458ForEthnicityOfVictim() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule458ForEthnicityOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		victimSegment.setEthnicity(EthnicityOfVictim.H.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("29", nibrsError.getDataElementIdentifier());
		assertEquals(EthnicityOfVictim.H.code, nibrsError.getValue());
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule458ForResidentStatusOfVictim() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule458ForResidentStatusOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		victimSegment.setResidentStatusOfVictim(ResidentStatusCode.N.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("30", nibrsError.getDataElementIdentifier());
		assertEquals(ResidentStatusCode.N.code, nibrsError.getValue());
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule458ForOffenderNumberToBeRelated() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule458ForOffenderNumberToBeRelated();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		victimSegment.setOffenderNumberRelated(0, 1);

		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("34", nibrsError.getDataElementIdentifier());
		assertEquals(Arrays.asList(new Integer[] { 1 }), nibrsError.getValue());

		victimSegment.setOffenderNumberRelated(0, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setOffenderNumberRelated(0, 1);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setOffenderNumberRelated(0, 1);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);

	}

	@Test
	public void testRule458ForSexOfVictim() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule458ForSexOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		victimSegment.setSex(SexOfVictimCode.F.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("27", nibrsError.getDataElementIdentifier());
		assertEquals(SexOfVictimCode.F.code, nibrsError.getValue());
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule458ForRaceOfVictim() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule458ForRaceOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		victimSegment.setRace(RaceOfVictimCode.A.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("28", nibrsError.getDataElementIdentifier());
		assertEquals(RaceOfVictimCode.A.code, nibrsError.getValue());
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule458ForAgeOfVictim() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule458ForAgeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		victimSegment.setAgeString("25  ");
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("26", nibrsError.getDataElementIdentifier());
		assertEquals(victimSegment.getAge(), nibrsError.getValue());
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);	
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);		
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule459ForOffenderNumberToBeRelated() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule459ForOffenderNumberToBeRelated();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOffenderNumberRelated(0, 1);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._459, nibrsError.getNIBRSErrorCode());
		assertEquals("34", nibrsError.getDataElementIdentifier());
		assertEquals(Arrays.asList(new Integer[] {1}), nibrsError.getValue());
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._120.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);
		victimSegment.setOffenderNumberRelated(0, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule460() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule460ForRelationshipOfVictimToOffender();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOffenderNumberRelated(0, 1);
		victimSegment.setVictimOffenderRelationship(0, null);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._460, nibrsError.getNIBRSErrorCode());
		assertEquals("35", nibrsError.getDataElementIdentifier());
		assertEquals(1, nibrsError.getValue());
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule468() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule468ForRelationshipOfVictimToOffender();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOffenderNumberRelated(0, 0);
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._468, nibrsError.getNIBRSErrorCode());
		assertEquals("35", nibrsError.getDataElementIdentifier());
		assertEquals(RelationshipOfVictimToOffenderCode.AQ.code, nibrsError.getValue());
		victimSegment.setVictimOffenderRelationship(0, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule469() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule469ForSexOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setSex(SexOfVictimCode.U.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._11A.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._469, nibrsError.getNIBRSErrorCode());
		assertEquals("27", nibrsError.getDataElementIdentifier());
		assertEquals(SexOfVictimCode.U.code, nibrsError.getValue());
		victimSegment.setSex(SexOfVictimCode.M.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule481() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule481ForAgeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAgeString("19  ");
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._36B.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._481, nibrsError.getNIBRSErrorCode());
		assertEquals("26", nibrsError.getDataElementIdentifier());
		assertEquals(victimSegment.getAge(), nibrsError.getValue());
		victimSegment.setAgeString("17  ");
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setAgeString("19  ");
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule482() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule482ForTypeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._120.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._482, nibrsError.getNIBRSErrorCode());
		assertEquals("25", nibrsError.getDataElementIdentifier());
		assertEquals(TypeOfVictimCode.L.code, nibrsError.getValue());
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._13A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule483() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule483ForTypeOfOfficerActivity();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		victimSegment.setOfficerAssignmentType(OfficerAssignmentType.F.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._484, nibrsError.getNIBRSErrorCode());
		assertEquals("25B", nibrsError.getDataElementIdentifier());
		assertEquals(OfficerAssignmentType.F.code, nibrsError.getValue());
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule462() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule462();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._13A.code);
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._07.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._462, nibrsError.getNIBRSErrorCode());
		assertEquals("31", nibrsError.getDataElementIdentifier());
		victimSegment.setUcrOffenseCodeConnection(1, OffenseCode._120.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setAggravatedAssaultHomicideCircumstances(1, AggravatedAssaultHomicideCircumstancesCode._01.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._120.code);
		victimSegment.setUcrOffenseCodeConnection(1, null);
		victimSegment.setAggravatedAssaultHomicideCircumstances(1, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule463() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule463();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09C.code);
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._01.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._463, nibrsError.getNIBRSErrorCode());
		assertEquals("31", nibrsError.getDataElementIdentifier());
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._20.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule461() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule461ForTypeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.S.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._220.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._461, nibrsError.getNIBRSErrorCode());
		assertEquals("25", nibrsError.getDataElementIdentifier());
		assertEquals(TypeOfVictimCode.S.code, nibrsError.getValue());
		victimSegment.setUcrOffenseCodeConnection(1, OffenseCode._120.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._120.code);
		victimSegment.setUcrOffenseCodeConnection(1, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.B.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._220.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule464() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule464ForTypeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.S.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._464, nibrsError.getNIBRSErrorCode());
		assertEquals("25", nibrsError.getDataElementIdentifier());
		assertEquals(TypeOfVictimCode.S.code, nibrsError.getValue());
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._120.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule467() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule467ForTypeOfVictim();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfVictim(TypeOfVictimCode.S.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._510.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._467, nibrsError.getNIBRSErrorCode());
		assertEquals("25", nibrsError.getDataElementIdentifier());
		assertEquals(TypeOfVictimCode.S.code, nibrsError.getValue());
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._510.code);
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule404ForTypeOfInjury() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForTypeOfInjury();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);

		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._100.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals("33", nibrsError.getDataElementIdentifier());

	}

	@Test
	public void testRule404ForRelationshipOfVictimToOffender(){
		
		Rule<VictimSegment> rule = victimRulesFactory.getRule404ForRelationshipOfVictimToOffender();
	
		VictimSegment victimSegment = getBasicVictimSegment();
		
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setOffenderNumberRelated(0, 0);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals(RelationshipOfVictimToOffenderCode.AQ.code, nibrsError.getValue());
		assertEquals("35", nibrsError.getDataElementIdentifier());
		
		victimSegment.setOffenderNumberRelated(0, 1);
		victimSegment.setVictimOffenderRelationship(0, null);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		assertEquals(null, nibrsError.getValue());
		assertEquals("35", nibrsError.getDataElementIdentifier());
		
	}
	
	@Test
	public void testRule406ForVictimConnectedToUcrOffenseCode() {

		Rule<VictimSegment> offenseCode401Rule = victimRulesFactory.getRule406ForVictimConnectedToUcrOffenseCode();

		VictimSegment victimSegment = getBasicVictimSegment();

		victimSegment.setUcrOffenseCodeConnection(new String[] { OffenseCode._09A.code, OffenseCode._09A.code });
		NIBRSError nibrsError = offenseCode401Rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());

		victimSegment.setUcrOffenseCodeConnection(new String[] { OffenseCode._09A.code, OffenseCode._09B.code });
		nibrsError = offenseCode401Rule.apply(victimSegment);
		assertNull(nibrsError);

	}

	@Test
	public void testRule406ForAggravatedAssaultHomicideCircumstances() {

		Rule<VictimSegment> assault406Rule = victimRulesFactory.getRule406ForAggravatedAssaultHomicideCircumstances();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAggravatedAssaultHomicideCircumstances(new String[] {
				AggravatedAssaultHomicideCircumstancesCode._01.code, AggravatedAssaultHomicideCircumstancesCode._01.code });

		NIBRSError nibrsError = assault406Rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());

		victimSegment.setAggravatedAssaultHomicideCircumstances(new String[] {
				AggravatedAssaultHomicideCircumstancesCode._01.code, AggravatedAssaultHomicideCircumstancesCode._02.code });

		nibrsError = assault406Rule.apply(victimSegment);
		assertNull(nibrsError);
	}

	@Test
	public void testRule406OffenderNumberToBeRelated() {

		Rule<VictimSegment> rule = victimRulesFactory.getRule406OffenderNumberToBeRelated();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOffenderNumberRelated(new Integer[] { 1, 1 });
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		assertEquals("34", nibrsError.getDataElementIdentifier());

		victimSegment.setOffenderNumberRelated(new Integer[] { 1, 2 });
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);

	}
	
	@Test
	public void testRule406ForTypeOfInjury() {
	
		Rule<VictimSegment> rule = victimRulesFactory.getRule406ForTypeOfInjury();

		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setTypeOfInjury(new String[] {TypeInjuryCode.B.code, TypeInjuryCode.B.code});
		
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		assertEquals("33", nibrsError.getDataElementIdentifier());
		
		victimSegment.setTypeOfInjury(new String[] {TypeInjuryCode.B.code, TypeInjuryCode.I.code});
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule471() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule471();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOffenderNumberRelated(0, 1);
		victimSegment.setOffenderNumberRelated(1, 2);
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.VO.code);
		victimSegment.setVictimOffenderRelationship(1, RelationshipOfVictimToOffenderCode.VO.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals("34", nibrsError.getDataElementIdentifier());
		assertEquals(RelationshipOfVictimToOffenderCode.VO.code, nibrsError.getValue());
		victimSegment.setOffenderNumberRelated(1, null);
		victimSegment.setVictimOffenderRelationship(1, null);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule472() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule472();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setOffenderNumberRelated(0, 1);
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
		OffenderSegment os = ((GroupAIncidentReport) victimSegment.getParentReport()).getOffenders().get(0);
		os.setSex(null);
		os.setOffenderSequenceNumber(1);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals("35", nibrsError.getDataElementIdentifier());
		assertEquals(NIBRSErrorCode._472, nibrsError.getNIBRSErrorCode());
		assertEquals(RelationshipOfVictimToOffenderCode.AQ.code, nibrsError.getValue());
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.RU.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule475() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule475();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.SE.code);
		victimSegment.setVictimOffenderRelationship(1, RelationshipOfVictimToOffenderCode.SE.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals("34", nibrsError.getDataElementIdentifier());
		assertEquals(NIBRSErrorCode._475, nibrsError.getNIBRSErrorCode());
		assertEquals(RelationshipOfVictimToOffenderCode.SE.code, nibrsError.getValue());
		victimSegment.setVictimOffenderRelationship(1, RelationshipOfVictimToOffenderCode.AQ.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule477() {
		Rule<VictimSegment> rule = victimRulesFactory.getRule477();
		VictimSegment victimSegment = getBasicVictimSegment();
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._20.code);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		NIBRSError nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		assertEquals("31", nibrsError.getDataElementIdentifier());
		assertEquals(NIBRSErrorCode._477, nibrsError.getNIBRSErrorCode());
		assertEquals(Arrays.asList(new String[] {AggravatedAssaultHomicideCircumstancesCode._20.code}), nibrsError.getValue());
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._01.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09B.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._30.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
		victimSegment.setAggravatedAssaultHomicideCircumstances(1, AggravatedAssaultHomicideCircumstancesCode._31.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setAggravatedAssaultHomicideCircumstances(1, null);
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09C.code);
		nibrsError = rule.apply(victimSegment);
		assertNotNull(nibrsError);
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, AggravatedAssaultHomicideCircumstancesCode._20.code);
		nibrsError = rule.apply(victimSegment);
		assertNull(nibrsError);
	}
	
	private VictimSegment getBasicVictimSegment(){
		
		GroupAIncidentReport report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);		
	
		VictimSegment victimSegment = new VictimSegment();
		report.addVictim(victimSegment);
		
		OffenseSegment offenseSegment = new OffenseSegment();
		report.addOffense(offenseSegment);

		OffenderSegment offenderSegment = new OffenderSegment();
		report.addOffender(offenderSegment);
		
		return victimSegment;
	}

}
