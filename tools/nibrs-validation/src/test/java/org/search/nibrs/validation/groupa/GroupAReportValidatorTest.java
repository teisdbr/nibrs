package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NibrsErrorCode;
import org.search.nibrs.validation.RuleViolationExemplarFactory;

public class GroupAReportValidatorTest {
		
	private GroupAIncidentReportValidator groupAValidator;
	
	RuleViolationExemplarFactory ruleFactory;// = RuleViolationExemplarFactory.getInstance();
	
	@Before
	public void init(){
		
		groupAValidator = new GroupAIncidentReportValidator();
		
		ruleFactory = RuleViolationExemplarFactory.getInstance();
	}
		
	@Test
	public void _101_adminMandatoryFieldTest(){
									
		List<GroupAIncidentReport> groupAIncidentList = ruleFactory.getGroupAIncidentsThatViolateRule(101);
		
		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
		
			NIBRSError _101Error = groupAValidator._101_adminMandatoryField(groupAIncidentReport, nibrsErrorList);
			
			Assert.assertNotNull(_101Error);
			
			Assert.assertEquals(NibrsErrorCode._101, _101Error.getNibrsErrorCode());
		}
		
		Assert.assertEquals(7, nibrsErrorList.size());
	}
	
	@Test
	public void _201_offenseRequiredFieldTest(){
		
		List<GroupAIncidentReport> groupAIncidentList = ruleFactory.getGroupAIncidentsThatViolateRule(201);
		
		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
				
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _201Error = groupAValidator._201_offenseRequiredField(groupAIncidentReport,
					nibrsErrorList);
							
			Assert.assertNotNull(_201Error);
			
			Assert.assertEquals(NibrsErrorCode._201, _201Error.getNibrsErrorCode());						
		}	
		
		Assert.assertEquals(9, nibrsErrorList.size());		
	}
	
	//TODO enable when passing. Data scenario could me more concise to construct 
	// only the four null "header" fields being tested
	@Ignore
	public void _301_propertySegmentRequiredField(){
		
		List<GroupAIncidentReport> groupAIncidentList = ruleFactory.getGroupAIncidentsThatViolateRule(301);

		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _301Error = groupAValidator._301_propertySegmentRequiredField(groupAIncidentReport,
					nibrsErrorList);
							
			Assert.assertNotNull(_301Error);
			
			Assert.assertEquals(NibrsErrorCode._301, _301Error.getNibrsErrorCode());						
		}	
		
		Assert.assertEquals(4, nibrsErrorList.size());			
	}
	
	//TODO enable when passing
	@Ignore
	public void _401_propertySegmentRequiredField(){
		
		List<GroupAIncidentReport> groupAIncidentList = ruleFactory.getGroupAIncidentsThatViolateRule(401);

		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _401Error = groupAValidator._401_victimSegmentRequiredField(groupAIncidentReport, 
					nibrsErrorList);
							
			Assert.assertNotNull(_401Error);
			
			Assert.assertEquals(NibrsErrorCode._401, _401Error.getNibrsErrorCode());						
		}	
		
		Assert.assertEquals(7, nibrsErrorList.size());			
	}	
	
	//TODO enable when passing
	@Ignore
	public void _501_propertySegmentRequiredField(){
		
		List<GroupAIncidentReport> groupAIncidentList = ruleFactory.getGroupAIncidentsThatViolateRule(501);

		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		for(GroupAIncidentReport groupAIncidentReport : groupAIncidentList){
			
			NIBRSError _501Error = groupAValidator._501_offenderSegmentRequiredField(groupAIncidentReport, 
					nibrsErrorList);
							
			Assert.assertNotNull(_501Error);
			
			Assert.assertEquals(NibrsErrorCode._051, _501Error.getNibrsErrorCode());						
		}	
		
		Assert.assertEquals(5, nibrsErrorList.size());			
	}		
	
}

