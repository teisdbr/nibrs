
package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.NIBRSSubmission;

public class NIBRSValidator {
	
	
	public NIBRSValidator() {
	
	}
	
	public List<NIBRSError> validate(NIBRSSubmission nibrsSubmission, 
			ValidationListener validationListener){
		
		List<NIBRSError> nibrsErrorList = new ArrayList<NIBRSError>();
		
		
		validationListener.validationAvailable(nibrsErrorList);
		
		return nibrsErrorList;
	}

	
	abstract class ValidationListener{
		
		abstract void validationAvailable(List<NIBRSError> nibrsErrorList);
	}
	
}
