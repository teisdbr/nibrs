package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.NibrsErrorCode;


public class GroupAReportValidator {

	
	public List<NIBRSError> validate(GroupAIncidentReport groupAIncidentReport){
				
		List<NIBRSError> errorsList = new ArrayList<NIBRSError>();
		
		_101_adminMandatoryField(groupAIncidentReport, errorsList);
		
		return errorsList;
	}

	
	
	
	NIBRSError _201_offenseRequiredField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
		
		NIBRSError rNibrsError = null;
		
		List<OffenseSegment> offenseSegmentList = groupAIncidentReport.getOffenses();
		
		for(OffenseSegment offenseSegment : offenseSegmentList){
		
			// TODO enum
			String ucrOffenseCode =  offenseSegment.getUcrOffenseCode();
			boolean hasUcrOffenseCode = StringUtils.isNotEmpty(ucrOffenseCode);

			// TODO enum
			String sOffenseAttemptedCompletedCode = offenseSegment.getOffenseAttemptedCompleted();			
			boolean hasOffenseAttemptedCompleted = StringUtils.isNotEmpty(sOffenseAttemptedCompletedCode);
						
			//note: not possible to know which index should be null checked
			String offenderSuspectedOfUsing = offenseSegment.getOffendersSuspectedOfUsing(-1);
			
			boolean hasOffenderSuspsectedOfUsing = StringUtils.isNotEmpty(offenderSuspectedOfUsing);						
		}
				
		return null;
	}
	
	
	NIBRSError _101_adminMandatoryField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
		
		NIBRSError rNibrsError = null;
		
		String ori = groupAIncidentReport.getOri();				
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		
		Date incidentDate = groupAIncidentReport.getIncidentDate();
		
		String clearedExceptionally = groupAIncidentReport.getExceptionalClearanceCode();
		
		//TODO change method to return Integer so it can be null checked
//		int monthOfSubmision = groupAIncidentReport.getMonthOfTape();
		
		//TODO change method to return Integer so it can be null checked
//		int yearOfSubmission = groupAIncidentReport.getYearOfTape();
						
		boolean missingOri = StringUtils.isEmpty(ori);
		
		boolean missingIncidentNumber = StringUtils.isEmpty(incidentNumber);								
		
		boolean missingIncidentDate = incidentDate == null;
		
		boolean missingClearedExceptionallyCode = 
				StringUtils.isEmpty(clearedExceptionally);
				
		boolean missingRequiredField = missingOri || missingIncidentNumber 
				|| missingIncidentDate || missingClearedExceptionallyCode;
													
		if(missingRequiredField){
			
			rNibrsError = new NIBRSError();			
			rNibrsError.setNibrsErrorCode(NibrsErrorCode._101);			
			rNibrsError.setSegmentType('1');			
			rNibrsError.setContext(groupAIncidentReport.getSource());	
			
			nibrsErrorList.add(rNibrsError);
		}		

		return rNibrsError;
	}
	
	
}
