package org.search.nibrs.validation.zeroreport;

import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ZeroReport;


public class ZeroReportValidator {

	
	public List<NIBRSError> validate(ZeroReport zeroReport,
			List<NIBRSError> nibrsErrorList){
				
		validate12Zeros(zeroReport, nibrsErrorList);
				
		return nibrsErrorList;
	}
	
	
	NIBRSError validate12Zeros(ZeroReport zeroReport, List<NIBRSError> nibrsErrorList){
				
		NIBRSError rNibrsError = null;
		
		String incidentNumber = zeroReport.getIncidentNumber();
		
		if(!"000000000000".equals(incidentNumber)){
			
			rNibrsError = new NIBRSError();
			
			// TODO confirm(don't specify message?)
			rNibrsError.setRuleNumber(015);
			
			nibrsErrorList.add(rNibrsError);
		}
		
		return rNibrsError;
	}	
	
}
