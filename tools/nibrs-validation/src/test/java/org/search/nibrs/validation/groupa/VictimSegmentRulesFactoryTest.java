package org.search.nibrs.validation.groupa;

import junit.framework.Assert;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.AggravatedAssaultHomicideCircumstancesCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.RaceOfVictimCode;
import org.search.nibrs.model.codes.RelationshipOfVictimToOffenderCode;
import org.search.nibrs.model.codes.SexOfVictimCode;
import org.search.nibrs.model.codes.TypeInjuryCode;
import org.search.nibrs.model.codes.TypeOfVictimCode;
import org.search.nibrs.validation.rules.Rule;

public class VictimSegmentRulesFactoryTest {
	
	private VictimSegmentRulesFactory victimRulesFactory = VictimSegmentRulesFactory.instance();
	
	@Test
	public void testRule401ForSequenceNumber(){
				
		Rule<VictimSegment> rule401 = victimRulesFactory.getRule401ForSequenceNumber();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test value 0
		victimSegment.setVictimSequenceNumber(0);
		
		NIBRSError nibrsError = rule401.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		Assert.assertEquals("23", nibrsError.getDataElementIdentifier());
		
		// test value 1000
		victimSegment.setVictimSequenceNumber(1000);
		
		nibrsError = rule401.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		Assert.assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		
		//test null value
		victimSegment.setVictimSequenceNumber(1000);
		
		nibrsError = rule401.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		Assert.assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());				
	}
	
	
	@Test
	public void testRule401ForVictimConnectedToUcrOffenseCode(){
		
		Rule<VictimSegment> rule401 = victimRulesFactory.getRule401ForVictimConnectedToUcrOffenseCode();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setUcrOffenseCodeConnection(null);
		
		NIBRSError nibrsError = rule401.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		Assert.assertEquals("24", nibrsError.getDataElementIdentifier());		
	}
	
	@Test
	public void testRule401ForTypeOfVictim(){
		
		Rule<VictimSegment> rule401 = victimRulesFactory.getRule401ForTypeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test null value
		victimSegment.setTypeOfVictim(null);
		
		NIBRSError nibrsError = rule401.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
		
		// test invalid code
		victimSegment.setTypeOfVictim("Z");
		
		nibrsError = rule401.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._401, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule404ForAgeOfVictim(){
		
		Rule<VictimSegment> ageRule404 = victimRulesFactory.getRule404ForAgeOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setAgeString(null);
		
		NIBRSError nibrsError = ageRule404.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());	
	}
	
	
	@Test
	public void testRule410ForAgeOfVictim(){
		
		Rule<VictimSegment> ageRule410 = victimRulesFactory.getRule410ForAgeOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		//invalid age
		victimSegment.setAgeString("3020");
		
		NIBRSError nibrsError = ageRule410.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
				
		Assert.assertEquals(NIBRSErrorCode._410, nibrsError.getNIBRSErrorCode());
		
		// valid age
		victimSegment.setAgeString("2030");
		
		nibrsError = ageRule410.apply(victimSegment);
		
		Assert.assertNull(nibrsError);				
	}
	
	
	@Test
	public void testRule422ForAgeOfVictim(){
	
		Rule<VictimSegment> ageRule422 = victimRulesFactory.getRule422ForAgeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		// test invalid 00 as first range value
		victimSegment.setAgeString("0020");
		
		NIBRSError nibrsError = ageRule422.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._422, nibrsError.getNIBRSErrorCode());
		
		// test valid range
		victimSegment.setAgeString("2030");
		
		nibrsError = ageRule422.apply(victimSegment);
		
		Assert.assertNull(nibrsError);
	}
	
	
	@Test
	public void testRule450ForAgeOfVictim(){
		
		Rule<VictimSegment> rule450ageLess10 = victimRulesFactory.getRule450ForAgeOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		// test can't be less than 10, for spouse relationship
		victimSegment.setAgeString("0909");
		
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.SE.code);
		
		NIBRSError nibrsError = rule450ageLess10.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._450, nibrsError.getNIBRSErrorCode());
	
		// test non-spouse relationship, younger than 10 = valid test
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.AQ.code);
	
		nibrsError = rule450ageLess10.apply(victimSegment);
		
		Assert.assertNull(nibrsError);
		
		//test spouse relationship, 10 years old is old enough - valid 

		victimSegment.setAgeString("1010");
		
		victimSegment.setVictimOffenderRelationship(0, RelationshipOfVictimToOffenderCode.SE.code);
		
		nibrsError = rule450ageLess10.apply(victimSegment);
		
		Assert.assertNull(nibrsError);
	}
	
	
	
	@Test
	public void testRule453ForAgeOfVictim(){
		
		Rule<VictimSegment> age453Rule = victimRulesFactory.getRule453ForAgeOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		
		// test missing age
		victimSegment.setAgeString(null);
		
		NIBRSError nibrsError = age453Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
	
		// test valid age
		victimSegment.setAgeString("3033");
		
		nibrsError = age453Rule.apply(victimSegment);
	
		Assert.assertNull(nibrsError);
	}
	
	
	
	
	
	@Test
	public void testRule404ForEthnicityOfVictim(){
		
		Rule<VictimSegment> ethnicity404Rule = victimRulesFactory.getRule404ForEthnicityOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setEthnicity(null);
		
		NIBRSError nibrsError = ethnicity404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule404ForResidentStatus(){
		
		Rule<VictimSegment> residentStatus404Rule = victimRulesFactory.getRule404ForResidentStatusOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setResidentStatusOfVictim(null);
		
		NIBRSError nibrsError = residentStatus404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule404ForAggravatedAssaultHomicideCircumstances(){
		
		Rule<VictimSegment> assault404Rule = victimRulesFactory.getRule404ForAggravatedAssaultHomicideCircumstances(); 

		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setAggravatedAssaultHomicideCircumstances(null);
		
		NIBRSError nibrsError = assault404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());		
	}
	
	@Test
	public void testRule404OffenderNumberToBeRelated(){
		
		Rule<VictimSegment> offender404Rule = victimRulesFactory.getRule404OffenderNumberToBeRelated();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test null array
		victimSegment.setOffenderNumberRelated(null);
		
		NIBRSError nibrsError = offender404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		
		// test empty array
		victimSegment.setOffenderNumberRelated(new Integer[]{});
		
		nibrsError = offender404Rule.apply(victimSegment);
				
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule404ForTypeOfficerActivityCircumstance(){
		
		Rule<VictimSegment> officerActivity404Rule = victimRulesFactory.getRule404ForTypeOfOfficerActivityCircumstance();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setTypeOfOfficerActivityCircumstance(null);
		
		NIBRSError nibrsError = officerActivity404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule404ForOfficerAssignmentType(){
		
		Rule<VictimSegment> officerAssignment404Rule = victimRulesFactory.getRule404ForOfficerAssignmentType();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setOfficerAssignmentType(null);
		
		NIBRSError nibrsError = officerAssignment404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule404ForOfficerOriOtherJurisdiction(){
		
		Rule<VictimSegment> jurisdiction404Rule = victimRulesFactory.getRule404ForOfficerOriOtherJurisdiction();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setOfficerOtherJurisdictionORI(null);
	
		NIBRSError nibrsError = jurisdiction404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule404ForSexOfVictim(){
		
		Rule<VictimSegment> sexOfVictim404Rule = victimRulesFactory.getRule404ForSexOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test null value
		victimSegment.setSex(null);
		
		NIBRSError nibrsError = sexOfVictim404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		
		// test invalid value
		victimSegment.setSex("Male");
		
		nibrsError = sexOfVictim404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule453ForSexOfVictim(){
		
		Rule<VictimSegment> rule453SexCode = victimRulesFactory.getRule453ForSexOfVictim();

		VictimSegment victimSegment = getBasicVictimSegment();
	
		victimSegment.setTypeOfVictim(TypeOfVictimCode.I.code);
		
		// test required sex code missing
		victimSegment.setSex(null);
		
		NIBRSError nibrsError = rule453SexCode.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
		
		// test sex code valid
		victimSegment.setSex(SexOfVictimCode.M.code);
		
		nibrsError = rule453SexCode.apply(victimSegment);
		
		Assert.assertNull(nibrsError);
	}
		
	
	
	@Test
	public void testRule404ForRaceOfVictim(){
		
		Rule<VictimSegment> raceOfVictim404Rule = victimRulesFactory.getRule404ForRaceOfVictim();
	
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setRace(null);
		
		NIBRSError nibrsError = raceOfVictim404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);	
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		
		
		victimSegment.setRace("Purple");
		
		nibrsError = raceOfVictim404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule453ForRaceOfVictim(){
		
		Rule<VictimSegment> race453Rule = victimRulesFactory.getRule453ForRaceOfVictim();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		// race is required for Individuals
		victimSegment.setTypeOfVictim(RaceOfVictimCode.I.code);
		
		victimSegment.setRace(null);
		
		NIBRSError nibrsError = race453Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._453, nibrsError.getNIBRSErrorCode());
	
		// race should not be required in this case
		victimSegment.setTypeOfVictim(RaceOfVictimCode.P.code);
		
		nibrsError = race453Rule.apply(victimSegment);
						
		Assert.assertNull(nibrsError);
	}
	
		
	
	@Test
	public void testRule404ForAdditionalJustifiableHomicideCircsumstances(){
		
		Rule<VictimSegment> justifyHomicideRule = victimRulesFactory.getRule404ForAdditionalJustifiableHomicideCircsumstances();
	
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setAdditionalJustifiableHomicideCircumstances(null);

		NIBRSError nibrsError = justifyHomicideRule.apply(victimSegment);
	
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule404ForTypeOfInjury(){
		
		Rule<VictimSegment> typeOfInjuryRule = victimRulesFactory.getRule404ForTypeOfInjury();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setTypeOfInjury(null);
		
		NIBRSError nibrsError = typeOfInjuryRule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
		
	@Test
	public void testRule404ForRelationshipOfVictimToOffender(){
		
		Rule<VictimSegment> relationshipToOffender404Rule = victimRulesFactory.getRule404ForRelationshipOfVictimToOffender();
	
		VictimSegment victimSegment = getBasicVictimSegment();
		
		victimSegment.setVictimOffenderRelationship(null);
		
		NIBRSError nibrsError = relationshipToOffender404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
		
		
		
		victimSegment.setVictimOffenderRelationship(new String[]{});
		
		nibrsError = relationshipToOffender404Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._404, nibrsError.getNIBRSErrorCode());
	}
	
	
	@Test
	public void testRule406ForVictimConnectedToUcrOffenseCode(){
		
		Rule<VictimSegment> offenseCode401Rule = victimRulesFactory.getRule406ForVictimConnectedToUcrOffenseCode();
		
		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test duplicate codes
		String[] aOffenseCodes = {OffenseCode._09A.code, OffenseCode._09A.code};
		
		victimSegment.setUcrOffenseCodeConnection(aOffenseCodes);
		
		NIBRSError nibrsError = offenseCode401Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		String[] aOffenseCodesDifferent = {OffenseCode._09A.code, OffenseCode._09B.code};
		
		victimSegment.setUcrOffenseCodeConnection(aOffenseCodesDifferent);
		
		nibrsError = offenseCode401Rule.apply(victimSegment);
		
		Assert.assertNull(nibrsError);
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
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		String[] aAssaultCodesDifferent = {AggravatedAssaultHomicideCircumstancesCode._01.code, 
				AggravatedAssaultHomicideCircumstancesCode._02.code};
		
		victimSegment.setAggravatedAssaultHomicideCircumstances(aAssaultCodesDifferent);
		
		nibrsError = assault406Rule.apply(victimSegment);
		
		Assert.assertNull(nibrsError);
	}
	
	@Test
	public void testRule406OffenderNumberToBeRelated(){
		
		Rule<VictimSegment> offender406Rule = victimRulesFactory.getRule406OffenderNumberToBeRelated();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test duplicate codes
		victimSegment.setOffenderNumberRelated(new Integer[]{1,1});
		
		NIBRSError nibrsError = offender406Rule.apply(victimSegment);
	
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		victimSegment.setOffenderNumberRelated(new Integer[]{1,2});
		
		nibrsError = offender406Rule.apply(victimSegment);
		
		Assert.assertNull(nibrsError);		
	}
	
	
	@Test
	public void testRule406ForTypeOfInjury(){
	
		Rule<VictimSegment> typeInjury406Rule = victimRulesFactory.getRule406ForTypeOfInjury();

		VictimSegment victimSegment = getBasicVictimSegment();
		
		// test duplicate codes
		String[] aDuplicateInjuries = {TypeInjuryCode.B.code, TypeInjuryCode.B.code};
		
		victimSegment.setTypeOfInjury(aDuplicateInjuries);
		
		NIBRSError nibrsError = typeInjury406Rule.apply(victimSegment);
		
		Assert.assertNotNull(nibrsError);
		
		Assert.assertEquals(NIBRSErrorCode._406, nibrsError.getNIBRSErrorCode());
		
		// test different codes
		String[] aDifferentInjuries = {TypeInjuryCode.B.code, TypeInjuryCode.I.code};
		
		victimSegment.setTypeOfInjury(aDifferentInjuries);
		
		nibrsError = typeInjury406Rule.apply(victimSegment);
		
		Assert.assertNull(nibrsError);
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
		
		Assert.assertNotNull(nibrsError);
		
		// test a valid offense code for the assault circumstances
		Assert.assertEquals(NIBRSErrorCode._419, nibrsError.getNIBRSErrorCode());
		
		victimSegment.setUcrOffenseCodeConnection(0, OffenseCode._09A.code);
				
		nibrsError = assault419Rule.apply(victimSegment);
		
		Assert.assertNull(nibrsError);		
	}
	
	
	
	private VictimSegment getBasicVictimSegment(){
		
		GroupAIncidentReport report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);		
	
		VictimSegment victimSegment = new VictimSegment();
		report.addVictim(victimSegment);
		
		return victimSegment;
	}

}
