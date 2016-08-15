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
		
		_201_offenseRequiredField(groupAIncidentReport, errorsList);
		
		return errorsList;
	}

	
	NIBRSError _201_offenseRequiredField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
		
		NIBRSError rNibrsError = null;
		
		Integer monthOfSubmision = groupAIncidentReport.getMonthOfTape();
		
		Integer yearOfSubmission = groupAIncidentReport.getYearOfTape();
		
		boolean hasmonthOfSubmision = monthOfSubmision != null;
		boolean hasyearOfSubmission = yearOfSubmission != null;
		
		
		String sOri = groupAIncidentReport.getOri();
		boolean hasOri = StringUtils.isNotEmpty(sOri);
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		boolean hasIncidentNumber = StringUtils.isNotEmpty(incidentNumber);
		
		boolean hasUcrOffenseCode = false;		
		boolean hasOffenseAttemptedCompleted = false;
		boolean hasOffenderSuspsectedOfUsing = false;		
		boolean hasBiasMotivation = false;
		boolean hasLocationType = false;				
		
		List<OffenseSegment> offenseSegmentList = groupAIncidentReport.getOffenses();
		
		for(OffenseSegment offenseSegment : offenseSegmentList){
		
			String ucrOffenseCode =  offenseSegment.getUcrOffenseCode();
			hasUcrOffenseCode = StringUtils.isNotEmpty(ucrOffenseCode);

			String sOffenseAttemptedCompletedCode = offenseSegment.getOffenseAttemptedCompleted();			
			hasOffenseAttemptedCompleted = StringUtils.isNotEmpty(sOffenseAttemptedCompletedCode);
								
			hasOffenderSuspsectedOfUsing = false;
			
			// 3 depends on knowing size of array used by getOffendersSuspectedOfUsing(i)
			for(int i=0; i < 3; i++){
			
				String iOffenderSuspectedOfUsing = offenseSegment.getOffendersSuspectedOfUsing(i);	
				
				if(StringUtils.isNotEmpty(iOffenderSuspectedOfUsing)){
					hasOffenderSuspsectedOfUsing = true;
					
					break;
				}				
			}
						
			hasBiasMotivation = false;
			
			// 5 depends on knowing array declaration length used by offenseSegment.getBiasMotivation(i)
			for(int i=0; i < 5; i++){
				
				String biasMotivation = offenseSegment.getBiasMotivation(i);
				
				if(StringUtils.isNotEmpty(biasMotivation)){
					
					hasBiasMotivation = true;
					
					break;
				}
			}
			
			String locationType = offenseSegment.getLocationType();
			
			hasLocationType = StringUtils.isNotEmpty(locationType);		
			
			// 3 depends on knowing array declaration length used by offenseSegment.getTypeOfCriminalActivity(i)
			for(int i=0; i < 3; i++){
			
				String crimActivityType = offenseSegment.getTypeOfCriminalActivity(i);
				
				StringUtils.isNotEmpty(crimActivityType);
			}
									
		}
		
		boolean missingRequiredField = 
				!hasOri 
				|| !hasIncidentNumber
				|| !hasmonthOfSubmision
				|| !hasyearOfSubmission
				|| !hasUcrOffenseCode
				|| !hasOffenseAttemptedCompleted
				|| !hasOffenderSuspsectedOfUsing
				|| !hasBiasMotivation
				|| !hasLocationType;
		
		if(missingRequiredField){
			
			rNibrsError = new NIBRSError();			
			rNibrsError.setNibrsErrorCode(NibrsErrorCode._201);			
			rNibrsError.setSegmentType('2');			
			rNibrsError.setContext(groupAIncidentReport.getSource());	
			
			nibrsErrorList.add(rNibrsError);			
		}
				
		return rNibrsError;
	}
	
	
	NIBRSError _101_adminMandatoryField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
		
		NIBRSError rNibrsError = null;
		
		String ori = groupAIncidentReport.getOri();				
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		
		Date incidentDate = groupAIncidentReport.getIncidentDate();
		
		String clearedExceptionally = groupAIncidentReport.getExceptionalClearanceCode();
		
		Date exceptionalClearanceDate = groupAIncidentReport.getExceptionalClearanceDate();
		
		Integer monthOfSubmision = groupAIncidentReport.getMonthOfTape();
		
		Integer yearOfSubmission = groupAIncidentReport.getYearOfTape();
						
		
		boolean missingOri = StringUtils.isEmpty(ori);
		
		boolean missingIncidentNumber = StringUtils.isEmpty(incidentNumber);								
		
		boolean missingIncidentDate = incidentDate == null;
		
		boolean missingClearedExceptionallyCode = 
				StringUtils.isEmpty(clearedExceptionally);
				
		boolean missingMonthOfSubmission = monthOfSubmision == null;
		
		boolean missingYearOfSubmission = yearOfSubmission == null;
		
		boolean missingExceptionalClearanceDate = exceptionalClearanceDate == null;
		
		boolean missingRequiredField = missingOri || missingIncidentNumber 
				|| missingIncidentDate || missingClearedExceptionallyCode
				|| missingMonthOfSubmission || missingYearOfSubmission 
				|| missingExceptionalClearanceDate;
													
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
