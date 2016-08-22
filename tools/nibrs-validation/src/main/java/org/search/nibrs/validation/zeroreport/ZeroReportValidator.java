package org.search.nibrs.validation.zeroreport;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;


public class ZeroReportValidator {

	
	public List<NIBRSError> validate(ZeroReport zeroReport){
				
		List<NIBRSError> errorsList = new ArrayList<NIBRSError>();
		
		_015_validate12Zeros(zeroReport, errorsList);
				
		return errorsList;
	}
	
	
	NIBRSError _015_validate12Zeros(ZeroReport zeroReport, List<NIBRSError> nibrsErrorList){
				
		NIBRSError rNibrsError = null;
		
		String incidentNumber = zeroReport.getIncidentNumber();
		
		if(!"000000000000".equals(incidentNumber)){
			
			rNibrsError = new NIBRSError();
			
			rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._015);
			
			nibrsErrorList.add(rNibrsError);
		}
		
		return rNibrsError;
	}	
	
	
}
