package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NibrsErrorCode;
import org.search.nibrs.validation.RuleViolationExemplarFactory;

public class GroupAReportValidatorTest {
		
	private GroupAReportValidator groupAValidator;
	
	RuleViolationExemplarFactory ruleFactory;// = RuleViolationExemplarFactory.getInstance();
	
	@Before
	public void init(){
		
		groupAValidator = new GroupAReportValidator();
		
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

}

