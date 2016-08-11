package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NibrsErrorCode;


public class GroupAReportValidator {

	
	public List<NIBRSError> validate(GroupAIncidentReport groupAIncidentReport){
				
		List<NIBRSError> errorsList = new ArrayList<NIBRSError>();
		
		_101_adminMandatoryField(groupAIncidentReport, errorsList);
		
		return errorsList;
	}

	
	NIBRSError _101_adminMandatoryField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
		
		NIBRSError rNibrsError = null;
		
		String ori = groupAIncidentReport.getOri();				
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		
		Date incidentDate = groupAIncidentReport.getIncidentDate();
		
		String clearedExceptionally = groupAIncidentReport.getExceptionalClearanceCode();
		
		
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
