package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.NibrsErrorCode;
import org.search.nibrs.validation.rules.Rule;

public class GroupAReportValidator {
	
	// todo: rename this class to GroupAIncidentReportValidator
	
	private List<Rule<GroupAIncidentReport>> incidentReportRules = new ArrayList<>();
	
	public GroupAReportValidator() {
		
		incidentReportRules = new GroupAIncidentReportRulesFactory().getRulesList();
		
	}

	public List<NIBRSError> validate(GroupAIncidentReport groupAIncidentReport) {
		
		List<NIBRSError> ret = new ArrayList<NIBRSError>();
		
		for (Rule<GroupAIncidentReport> r : incidentReportRules) {
			NIBRSError e = r.apply(groupAIncidentReport, groupAIncidentReport.getSource());
			if (e != null) {
				ret.add(e);
			}
		}
		
		return ret;
		
	}
	
	public List<NIBRSError> xvalidate(GroupAIncidentReport groupAIncidentReport){
				
		List<NIBRSError> errorsList = new ArrayList<NIBRSError>();
		
		_101_adminMandatoryField(groupAIncidentReport, errorsList);
		
		_201_offenseRequiredField(groupAIncidentReport, errorsList);
		
		_301_propertySegmentRequiredField(groupAIncidentReport, errorsList);
		
		return errorsList;
	}

	
	NIBRSError _501_offenderSegmentRequiredField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
		
		Integer monthOfSubmision = groupAIncidentReport.getMonthOfTape();
		
		boolean missingMonthOfSubmision = monthOfSubmision == null;
										
		Integer yearOfSubmission = groupAIncidentReport.getYearOfTape();
		
		boolean missingYearOfSubmission = yearOfSubmission == null;				
						
		String sOri = groupAIncidentReport.getOri();
		
		boolean missingOri = StringUtils.isEmpty(sOri);
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		
		boolean missingIncidentNumber = StringUtils.isEmpty(incidentNumber);		
		
		boolean missingOffenderSeqNumber = false;
		
		List<OffenderSegment> offenderSegmentList = groupAIncidentReport.getOffenders();
		
		for(OffenderSegment iOffenderSegment : offenderSegmentList){
			
			Integer offenderSeqNumber = iOffenderSegment.getOffenderSequenceNumber();
			
			if(offenderSeqNumber == null){
				
				missingOffenderSeqNumber = true;
			}
		}
		
		boolean missingRequiredField = 
				 missingMonthOfSubmision  
				|| missingYearOfSubmission
				|| missingOri
				|| missingIncidentNumber
				|| missingOffenderSeqNumber;
				
		NIBRSError rNibrsError = null;
				
		if(missingRequiredField){
			
			rNibrsError = new NIBRSError();			
			rNibrsError.setNibrsErrorCode(NibrsErrorCode._501);			
			rNibrsError.setSegmentType('5');			
			rNibrsError.setContext(groupAIncidentReport.getSource());	
			
			nibrsErrorList.add(rNibrsError);			
		}		
		
		return rNibrsError;
	}
	
	
	NIBRSError _401_victimSegmentRequiredField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
				
		Integer monthOfSubmision = groupAIncidentReport.getMonthOfTape();
		
		boolean missingMonthOfSubmision = monthOfSubmision == null;
										
		Integer yearOfSubmission = groupAIncidentReport.getYearOfTape();
		
		boolean missingYearOfSubmission = yearOfSubmission == null;				
						
		String sOri = groupAIncidentReport.getOri();
		
		boolean missingOri = StringUtils.isEmpty(sOri);
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		
		boolean missingIncidentNumber = StringUtils.isEmpty(incidentNumber);			
		
		boolean missingUrcOffenseCodeConnection = false;
		
		List<VictimSegment> victimSegmentList = groupAIncidentReport.getVictims();
		
		boolean missingVictimSequenceNumber = false;
		
		boolean missingTypeOfVictim = false;
		
		for(VictimSegment iVictimSegment : victimSegmentList){
			
			Integer victimSequenceNumber = iVictimSegment.getVictimSequenceNumber();
			
			if(victimSequenceNumber == null){
				
				missingVictimSequenceNumber = true;
			}	
				
			// depends on declared length of array used by getUcrOffenseCodeConnection(i)
			for(int i=0; i < 10; i++){
							
				String ucrOffenseCodeConnection = iVictimSegment.getUcrOffenseCodeConnection(i);
				
				if(StringUtils.isEmpty(ucrOffenseCodeConnection)){
					
					missingUrcOffenseCodeConnection = true;
				}
				
			}
									
			String typeOfVictim = iVictimSegment.getTypeOfVictim();
			
			if(StringUtils.isEmpty(typeOfVictim)){
				
				missingTypeOfVictim = true;
			}
		}
		
		boolean missingRequiredField = 
				 missingMonthOfSubmision  
				|| missingYearOfSubmission
				|| missingOri
				|| missingIncidentNumber
				|| missingVictimSequenceNumber 
				|| missingUrcOffenseCodeConnection
				|| missingTypeOfVictim;
				
		NIBRSError rNibrsError = null;
				
		if(missingRequiredField){
			
			rNibrsError = new NIBRSError();			
			rNibrsError.setNibrsErrorCode(NibrsErrorCode._401);			
			rNibrsError.setSegmentType('4');			
			rNibrsError.setContext(groupAIncidentReport.getSource());	
			
			nibrsErrorList.add(rNibrsError);			
		}		
		
		return rNibrsError;
	}
	
	
	
	NIBRSError _301_propertySegmentRequiredField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
	
		Integer monthOfSubmision = groupAIncidentReport.getMonthOfTape();
		
		boolean hasMonthOfSubmision = monthOfSubmision != null;
										
		Integer yearOfSubmission = groupAIncidentReport.getYearOfTape();
		
		boolean hasYearOfSubmission = yearOfSubmission != null;				
						
		String sOri = groupAIncidentReport.getOri();
		
		boolean hasOri = StringUtils.isNotEmpty(sOri);
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		
		boolean hasIncidentNumber = StringUtils.isNotEmpty(incidentNumber);		
				
		boolean missingRequiredField = 
				!hasMonthOfSubmision
				|| !hasYearOfSubmission
				|| !hasOri
				|| !hasIncidentNumber;
				
		NIBRSError rNibrsError = null;
				
		if(missingRequiredField){
			
			rNibrsError = new NIBRSError();			
			rNibrsError.setNibrsErrorCode(NibrsErrorCode._301);			
			rNibrsError.setSegmentType('3');			
			rNibrsError.setContext(groupAIncidentReport.getSource());	
			
			nibrsErrorList.add(rNibrsError);			
		}
				
		return rNibrsError;
	}
	
	
	
	
	NIBRSError _201_offenseRequiredField(GroupAIncidentReport groupAIncidentReport,
			List<NIBRSError> nibrsErrorList){
		
		NIBRSError rNibrsError = null;
		
		Integer monthOfSubmision = groupAIncidentReport.getMonthOfTape();
		
		Integer yearOfSubmission = groupAIncidentReport.getYearOfTape();
		
		boolean hasMonthOfSubmision = monthOfSubmision != null;
		
		boolean hasYearOfSubmission = yearOfSubmission != null;
				
		String sOri = groupAIncidentReport.getOri();
		
		boolean hasOri = StringUtils.isNotEmpty(sOri);
		
		String incidentNumber = groupAIncidentReport.getIncidentNumber();
		
		boolean hasIncidentNumber = StringUtils.isNotEmpty(incidentNumber);
							
		boolean missingAUcrOffenseCode = false;
		
		boolean missingAnOffenderSuspsectedOfUsing = false;		
		
		boolean missingABiasMotivation = false;
		
		boolean missingALocationType = false;				
				
		boolean missingAnOffenseAttemptedCompleted = false;
		
		List<OffenseSegment> offenseSegmentList = groupAIncidentReport.getOffenses();
								
		for(OffenseSegment offenseSegment : offenseSegmentList){
					
			String sUcrOffneseCode = offenseSegment.getUcrOffenseCode();
			
			if(StringUtils.isEmpty(sUcrOffneseCode)){
				
				missingAUcrOffenseCode = true;
			}
				
			String sOffenseAttemptedCompletedCode = offenseSegment.getOffenseAttemptedCompleted();
			
			if(StringUtils.isEmpty(sOffenseAttemptedCompletedCode)){
			
				missingAnOffenseAttemptedCompleted = true;
			}
														
			// 3 depends on knowing size of array used by getOffendersSuspectedOfUsing(i)
			for(int i=0; i < 3; i++){
			
				String iOffenderSuspectedOfUsing = offenseSegment.getOffendersSuspectedOfUsing(i);	
				
				if(StringUtils.isEmpty(iOffenderSuspectedOfUsing)){
					
					missingAnOffenderSuspsectedOfUsing = true;
					
					break;
				}				
			}
									
			// 5 depends on knowing array declaration length used by offenseSegment.getBiasMotivation(i)
			for(int i=0; i < 5; i++){
				
				String biasMotivation = offenseSegment.getBiasMotivation(i);
				
				if(StringUtils.isEmpty(biasMotivation)){
					
					missingABiasMotivation = true;
					
					break;
				}
			}
			
			String locationType = offenseSegment.getLocationType();
			
			if(StringUtils.isEmpty(locationType)){
				
				missingALocationType = true;
			}
																	
		}
		
		boolean missingRequiredField = 
				!hasOri 
				|| !hasIncidentNumber
				|| !hasMonthOfSubmision
				|| !hasYearOfSubmission
				|| missingAUcrOffenseCode
				|| missingAnOffenseAttemptedCompleted
				|| missingAnOffenderSuspsectedOfUsing
				|| missingABiasMotivation
				|| missingALocationType;
		
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
