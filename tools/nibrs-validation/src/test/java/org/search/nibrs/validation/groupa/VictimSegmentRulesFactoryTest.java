package org.search.nibrs.validation.groupa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
	public void testRule410ForAgeOfVictim(){
		
		Rule<VictimSegment> ageRule410 = victimRulesFactory.getRule410ForAgeOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		//invalid age
		victimSegment.setAgeString("3020");
		
		NIBRSError nibrsError = ageRule410.apply(victimSegment);
		
		assertNotNull(nibrsError);
				
		assertEquals(NIBRSErrorCode._410, nibrsError.getNIBRSErrorCode());
		
		// valid age
		victimSegment.setAgeString("2030");
		
		nibrsError = ageRule410.apply(victimSegment);
		
		assertNull(nibrsError);				
	}
	
	
	@Test
	public void testRule422ForAgeOfVictim(){
	
		Rule<VictimSegment> ageRule422 = victimRulesFactory.getRule422ForAgeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		// test invalid 00 as first range value
		victimSegment.setAgeString("0020");
		
		NIBRSError nibrsError = ageRule422.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._422, nibrsError.getNIBRSErrorCode());
		
		// test valid range
		victimSegment.setAgeString("2030");
		
		nibrsError = ageRule422.apply(victimSegment);
		
		assertNull(nibrsError);
	}
	
	
	@Test
	public void testRule450ForAgeOfVictim(){
		
		Rule<VictimSegment> rule450ageLess10 = victimRulesFactory.getRule450ForAgeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		// test can't be less than 10, for spouse relationship
		victimSegment.setAgeString("0909");
		
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.SE.code);
		
		NIBRSError nibrsError = rule450ageLess10.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._450, nibrsError.getNIBRSErrorCode());
	
		// test non-spouse relationship, younger than 10 = valid test
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
	
		nibrsError = rule450ageLess10.apply(victimSegment);
		
		assertNull(nibrsError);
		
		//test spouse relationship, 10 years old is old enough - valid 

		victimSegment.setAgeString("1010");
		
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.SE.code);
		
		nibrsError = rule450ageLess10.apply(victimSegment);
		
		assertNull(nibrsError);
	}
	
	
	
	@Test
	public void testRule453ForAgeOfVictim(){
		
		Rule<VictimSegment> age453Rule = victimRulesFactory.getRule453ForAgeOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		
		// test missing age
		victimSegment.setAgeString(null);
		
		NIBRSError nibrsError = age453Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
	
		// test valid age
		victimSegment.setAgeString("3033");
		
		nibrsError = age453Rule.apply(victimSegment);
	
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule404ForEthnicityOfVictim(){
		
		Rule<VictimSegment> ethnicity404Rule = victimRulesFactory.getRule404ForEthnicityOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setEthnicity(null);
		
		NIBRSError nibrsError = ethnicity404Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule404ForResidentStatus(){
		
		Rule<VictimSegment> residentStatus404Rule = victimRulesFactory.getRule404ForResidentStatusOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setResidentStatusOfVictim(null);
		
		NIBRSError nibrsError = residentStatus404Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
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
	public void testRule454ForTypeOfOfficerActivityCircumstance(){
		
		Rule<VictimSegment> activity454Rule = victimRulesFactory.getRule454ForTypeOfOfficerActivityCircumstance();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test invalid circumstance for law officer
		victimSegment.setTypeOfOfficerActivityCircumstance(null);
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		
		NIBRSError nibrsError = activity454Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());		
		assertEquals("25A", nibrsError.getDataElementIdentifier());
		
		// test a valid circumstance for law officer
		victimSegment.setTypeOfOfficerActivityCircumstance(TypeOfOfficerActivityCircumstance._01.code);
		victimSegment.setOfficerAssignmentType(OfficerAssignmentType.F.code);
		victimSegment.setAgeString("3336");
		victimSegment.setSex(SexOfVictimCode.M.code);
		victimSegment.setRace(RaceOfVictimCode.A.code);
		
		nibrsError = activity454Rule.apply(victimSegment);
		
		assertNull(nibrsError);				
	}
	
	
	@Test
	public void testRule454ForOfficerAssignmentType(){
		
		Rule<VictimSegment> assignment454Rule = victimRulesFactory.getRule454ForOfficerAssignmentType();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setOfficerAssignmentType(null);
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
	
		NIBRSError nibrsError = assignment454Rule.apply(victimSegment);
		
		assertNotNull(victimSegment);
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());
		
		assertEquals("25B", nibrsError.getDataElementIdentifier());
				
		// test a valid circumstance for law officer
		victimSegment.setTypeOfOfficerActivityCircumstance(TypeOfOfficerActivityCircumstance._01.code);
		victimSegment.setOfficerAssignmentType(OfficerAssignmentType.F.code);
		victimSegment.setAgeString("3336");
		victimSegment.setSex(SexOfVictimCode.M.code);
		victimSegment.setRace(RaceOfVictimCode.A.code);
		
		nibrsError = assignment454Rule.apply(victimSegment);
		
		assertNull(nibrsError);				
	}
	
	
	@Test
	public void testRule454ForSexOfVictim(){
		
		Rule<VictimSegment> sexOfVictim454Rule = victimRulesFactory.getRule454ForSexOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		
		victimSegment.setSex(null);
		
		NIBRSError nibrsError = sexOfVictim454Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());		
		assertEquals("27", nibrsError.getDataElementIdentifier());
		
		// test a valid circumstance for law officer
		victimSegment.setTypeOfOfficerActivityCircumstance(TypeOfOfficerActivityCircumstance._01.code);
		victimSegment.setOfficerAssignmentType(OfficerAssignmentType.F.code);
		victimSegment.setAgeString("3336");
		victimSegment.setSex(SexOfVictimCode.M.code);
		victimSegment.setRace(RaceOfVictimCode.A.code);
		
		nibrsError = sexOfVictim454Rule.apply(victimSegment);
		
		assertNull(nibrsError);
	}
	
	
	
	
	@Test
	public void testRule454ForRaceOfVictim(){
	
		Rule<VictimSegment> raceOfVictim454Rule = victimRulesFactory.getRule454ForRaceOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.L.code);
		
		victimSegment.setRace(null);
		
		NIBRSError nibrsError = raceOfVictim454Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._454, nibrsError.getNIBRSErrorCode());
		
		assertEquals("28", nibrsError.getDataElementIdentifier());
		
		// test a valid circumstance for law officer
		victimSegment.setTypeOfOfficerActivityCircumstance(TypeOfOfficerActivityCircumstance._01.code);
		victimSegment.setOfficerAssignmentType(OfficerAssignmentType.F.code);
		victimSegment.setAgeString("3336");
		victimSegment.setSex(SexOfVictimCode.M.code);
		victimSegment.setRace(RaceOfVictimCode.A.code);
		
		nibrsError = raceOfVictim454Rule.apply(victimSegment);
		
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
		
		// test null value
		victimSegment.setSex(null);
		
		NIBRSError nibrsError = sexOfVictim404Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		
		// test invalid value
		victimSegment.setSex("Male");
		
		nibrsError = sexOfVictim404Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule453ForSexOfVictim(){
		
		Rule<VictimSegment> rule453SexCode = victimRulesFactory.getRule453ForSexOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		
		// test required sex code missing
		victimSegment.setSex(null);
		
		NIBRSError nibrsError = rule453SexCode.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
		
		// test sex code valid
		victimSegment.setSex(SexOfVictimCode.M.code);
		
		nibrsError = rule453SexCode.apply(victimSegment);
		
		assertNull(nibrsError);
	}
		
	
	
	@Test
	public void testRule404ForRaceOfVictim(){
		
		Rule<VictimSegment> raceOfVictim404Rule = victimRulesFactory.getRule404ForRaceOfVictim();
	
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setRace(null);
		
		NIBRSError nibrsError = raceOfVictim404Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);	
		
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		
		
		victimSegment.setRace("Purple");
		
		nibrsError = raceOfVictim404Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule453ForRaceOfVictim(){
		
		Rule<VictimSegment> race453Rule = victimRulesFactory.getRule453ForRaceOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		// race is required for Individuals
		victimSegment.setTypeOfVictim(RaceOfVictimCode.I.code);
		
		victimSegment.setRace(null);
		
		NIBRSError nibrsError = race453Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
	
		// race should not be required in this case
		victimSegment.setTypeOfVictim(RaceOfVictimCode.P.code);
		
		nibrsError = race453Rule.apply(victimSegment);
						
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
	public void testRule455ForAdditionalJustifiableHomicideCircsumstances(){
		
		Rule<VictimSegment> addnlCir455Rule = victimRulesFactory
				.getRule455ForAdditionalJustifiableHomicideCircsumstances();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, 
				AggravatedAssaultHomicideCircumstancesCode._20.code);
		
		victimSegment.setAdditionalJustifiableHomicideCircumstances(null);
		
		NIBRSError nibrsError = addnlCir455Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._455, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule457ForAdditionalJustifiableHomicideCircsumstances(){
	
		Rule<VictimSegment> addnlInfoWithoutJustHomicideRule = victimRulesFactory
				.getRule457ForAdditionalJustifiableHomicideCircsumstances();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, 
				AggravatedAssaultHomicideCircumstancesCode._10.code);
		
		victimSegment.setAdditionalJustifiableHomicideCircumstances(
				AdditionalJustifiableHomicideCircumstancesCode.A.code);
		
		NIBRSError nibrsError = addnlInfoWithoutJustHomicideRule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._457, nibrsError.getNIBRSErrorCode());
	}
	
		
	@Test
	public void testRule458ForTypeOfInjury(){
		
		Rule<VictimSegment>  typeInjury458Rule = victimRulesFactory.getRule458ForTypeOfInjury();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		
		// type victim not individual or law officer
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		
		// test populated type of injury
		victimSegment.setTypeOfInjury(0, TypeInjuryCode.B.code);
		
		NIBRSError nibrsError = typeInjury458Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		
		//TODO enable when set(null) supported
		// test empty type of injury
//		victimSegment.setTypeOfInjury(null);		
//		nibrsError = typeInjury458Rule.apply(victimSegment);		
//		assertNull(nibrsError);
	}
	
	
	@Test
	public void testRule458ForEthnicityOfVictim(){
		
		Rule<VictimSegment> ethnicity458Rule = victimRulesFactory.getRule458ForEthnicityOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		// crime against person
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		
		// type victim not individual or law officer
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		
		// not allowed for above conditions
		victimSegment.setEthnicity(EthnicityOfVictim.H.code);
		
		NIBRSError nibrsError = ethnicity458Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule458ForResidentStatusOfVictim(){
		
		Rule<VictimSegment> residentStatus458Rule = victimRulesFactory.getRule458ForResidentStatusOfVictim(); 	

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// crime against person
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		
		// type victim not individual or law officer
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);		
		
		// not allowed for above conditions
		victimSegment.setResidentStatusOfVictim(ResidentStatusCode.N.code);
		
		NIBRSError nibrsError = residentStatus458Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule458ForOffenderNumberToBeRelated(){
	
		Rule<VictimSegment> offenderNum458Rule = victimRulesFactory.getRule458ForOffenderNumberToBeRelated();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		// crime against person
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
		
		// type victim not individual or law officer
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		
		victimSegment.setOffenderNumberRelated(0, 1);
		
		NIBRSError nibrsError = offenderNum458Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule458ForSexOfVictim(){
		
		Rule<VictimSegment> rule458SexOfVictim = victimRulesFactory.getRule458ForSexOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// crime against person
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);		
	
		// type victim not individual or law officer
		victimSegment.setTypeOfVictim(TypeOfVictimCode.R.code);
		
		// not allowed to be populated when above conditions exist
		victimSegment.setSex(SexOfVictimCode.M.code);
		
		NIBRSError nibrsError = rule458SexOfVictim.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._458, nibrsError.getNIBRSErrorCode());
		assertEquals("27", nibrsError.getDataElementIdentifier());
	}
	
	@Test
	public void testRule404ForTypeOfInjury() {

		Rule<VictimSegment> typeOfInjuryRule = victimRulesFactory.getRule404ForTypeOfInjury();

		VictimSegment victimSegment = getBasicVictimSegment();

		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._200.code);

		NIBRSError nibrsError = typeOfInjuryRule.apply(victimSegment);
		assertNull(nibrsError);

		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._100.code);
		nibrsError = typeOfInjuryRule.apply(victimSegment);
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
	public void testRule406ForVictimConnectedToUcrOffenseCode(){
		
		Rule<VictimSegment> offenseCode401Rule = victimRulesFactory.getRule406ForVictimConnectedToUcrOffenseCode();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test duplicate codes
		String[] aOffenseCodes = {OffenseCode._09A.code, OffenseCode._09A.code};
		
		victimSegment.setUcrOffenseCodeConnection(aOffenseCodes);
		
		NIBRSError nibrsError = offenseCode401Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		String[] aOffenseCodesDifferent = {OffenseCode._09A.code, OffenseCode._09B.code};
		
		victimSegment.setUcrOffenseCodeConnection(aOffenseCodesDifferent);
		
		nibrsError = offenseCode401Rule.apply(victimSegment);
		
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule406ForAggravatedAssaultHomicideCircumstances(){
		
		Rule<VictimSegment> assault406Rule = victimRulesFactory.getRule406ForAggravatedAssaultHomicideCircumstances();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test duplicate codes
		String[] aAssaultCodes = {AggravatedAssaultHomicideCircumstancesCode._01.code, 
				AggravatedAssaultHomicideCircumstancesCode._01.code};
		
		victimSegment.setAggravatedAssaultHomicideCircumstances(aAssaultCodes);
		
		NIBRSError nibrsError = assault406Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		String[] aAssaultCodesDifferent = {AggravatedAssaultHomicideCircumstancesCode._01.code, 
				AggravatedAssaultHomicideCircumstancesCode._02.code};
		
		victimSegment.setAggravatedAssaultHomicideCircumstances(aAssaultCodesDifferent);
		
		nibrsError = assault406Rule.apply(victimSegment);
		
		assertNull(nibrsError);
	}
	
	@Test
	public void testRule406OffenderNumberToBeRelated(){
		
		Rule<VictimSegment> offender406Rule = victimRulesFactory.getRule406OffenderNumberToBeRelated();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test duplicate codes
		victimSegment.setOffenderNumberRelated(new Integer[]{1,1});
		
		NIBRSError nibrsError = offender406Rule.apply(victimSegment);
	
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		victimSegment.setOffenderNumberRelated(new Integer[]{1,2});
		
		nibrsError = offender406Rule.apply(victimSegment);
		
		assertNull(nibrsError);		
	}
	
	
	@Test
	public void testRule406ForTypeOfInjury(){
	
		Rule<VictimSegment> typeInjury406Rule = victimRulesFactory.getRule406ForTypeOfInjury();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test duplicate codes
		String[] aDuplicateInjuries = {TypeInjuryCode.B.code, TypeInjuryCode.B.code};
		
		victimSegment.setTypeOfInjury(aDuplicateInjuries);
		
		NIBRSError nibrsError = typeInjury406Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		String[] aDifferentInjuries = {TypeInjuryCode.B.code, TypeInjuryCode.I.code};
		
		victimSegment.setTypeOfInjury(aDifferentInjuries);
		
		nibrsError = typeInjury406Rule.apply(victimSegment);
		
		assertNull(nibrsError);
	}
	
	
	@Test
	public void testRule419ForAggravatedAssaultHomicideCircumstances(){
		
		Rule<VictimSegment> assault419Rule = victimRulesFactory.getRule419ForAggravatedAssaultHomicideCircumstances();
	
		VictimSegment victimSegment = getBasicVictimSegment();		
				
		victimSegment.setAggravatedAssaultHomicideCircumstances(0, 
				AggravatedAssaultHomicideCircumstancesCode._01.code);		
						
		// test an unrelated offense code(shouldn't cause error)
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._11A.code);
		
		NIBRSError nibrsError = assault419Rule.apply(victimSegment);
		
		assertNotNull(nibrsError);
		
		// test a valid offense code for the assault circumstances
		assertEquals(NIBRSErrorCode._419, nibrsError.getNIBRSErrorCode());
		
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
				
		nibrsError = assault419Rule.apply(victimSegment);
		
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
