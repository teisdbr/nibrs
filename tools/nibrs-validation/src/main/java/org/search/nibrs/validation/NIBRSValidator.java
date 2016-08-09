
package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBIncidentReport;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.validation.zeroreport.GroupAReportValidator;
import org.search.nibrs.validation.zeroreport.GroupBReportValidator;
import org.search.nibrs.validation.zeroreport.ZeroReportValidator;

public class NIBRSValidator {
		
	private static final Logger logger = Logger.getLogger(NIBRSValidator.class.getName());
	
	private ValidationListener validationListener;
			
	public NIBRSValidator(ValidationListener validationListener) {
		
		this.validationListener = validationListener;
	}
	
	/**
	 * Returns all validation errors.  While processing, notifies listeners of errors 
	 * found for each report that's validated.
	 * 
	 * @param nibrsSubmission
	 * 		Should contain all the reports to be validated
	 * 
	 * @return
	 * 		A List of all validated errors, for all the reports
	 */
	public List<NIBRSError> validate(NIBRSSubmission nibrsSubmission){
		
		List<NIBRSError> rAllNibrsErrorList = new ArrayList<NIBRSError>();
				
		List<AbstractReport> reportList = nibrsSubmission.getReports();
				
		for(AbstractReport iReport : reportList){
						
			List<NIBRSError> singleReportErrorsList = validateReport(iReport);
			
			if(singleReportErrorsList != null && !singleReportErrorsList.isEmpty()){
				
				rAllNibrsErrorList.addAll(singleReportErrorsList);
				
				logger.info("notifying listener of single report validation");
				
				validationListener.validationAvailable(singleReportErrorsList);								
			}
		}
		
		logger.info("Completed validating all reports, returning them");
		
		return rAllNibrsErrorList;
	}

	
	public List<NIBRSError> validateReport(AbstractReport report){
		
		List<NIBRSError> nibrsErrorList = null;
		
		if(report instanceof ZeroReport){
			
			ZeroReport zeroReport = (ZeroReport)report;
			
			ZeroReportValidator zeroReportValidator = new ZeroReportValidator();
			
			nibrsErrorList = zeroReportValidator.validate(zeroReport);				
		
		}else if(report instanceof GroupAIncidentReport){
			
			GroupAIncidentReport groupAIncidentReport = (GroupAIncidentReport)report;
			
			GroupAReportValidator groupAValidator = new GroupAReportValidator();
			
			groupAValidator.validate(groupAIncidentReport);			
			
		}else if(report instanceof GroupBIncidentReport){
			
			GroupBIncidentReport groupBIncidentReport = (GroupBIncidentReport)report;
			
			GroupBReportValidator groupBValidator = new GroupBReportValidator();
			
			groupBValidator.validate(groupBIncidentReport);
		}		
		
		return nibrsErrorList;
	}
	
		
}
