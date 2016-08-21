package org.search.nibrs.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * A class for objects that represent an error encountered in processing NIBRS data.
 *
 */
public class NIBRSError {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(NIBRSError.class);
	
	private Object context;
	private Integer ruleNumber;
	private String ruleDescription;
	private String reportUniqueIdentifier;
	private Object value;
	private char segmentType;
	private String withinSegmentIdentifier;	
	private NIBRSErrorCode nibrsErrorCode;
	
	public NIBRSError() {
	}
	
	public NIBRSError(NIBRSError e) {
		this.context = e.context;
		this.ruleNumber = e.ruleNumber;
		this.ruleDescription = e.ruleDescription;
		this.reportUniqueIdentifier = e.reportUniqueIdentifier;
		this.value = e.value;
		this.segmentType = e.segmentType;
		this.withinSegmentIdentifier = e.withinSegmentIdentifier;
		this.nibrsErrorCode = e.nibrsErrorCode;
	}
	
	/**
	 * The text of the rule, as provided by the FBI.
	 * @return the rule text
	 */
	public String getRuleDescription() {
		return ruleDescription;
	}
	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}
	/**
	 * An object representing the "context" of where the error occurred.  The intent of this property is to help a human user find
	 * the offending record/data in question so they can pursue a correction.
	 * @return the context object
	 */
	public Object getContext() {
		return context;
	}
	public void setContext(Object context) {
		this.context = context;
	}
	/**
	 * The rule number from the FBI NIBRS specification.
	 * @return the rule number
	 */
	public Integer getRuleNumber() {
		return ruleNumber;
	}
	public void setRuleNumber(Integer ruleNumber) {
		this.ruleNumber = ruleNumber;
	}
	/**
	 * A value that uniquely identifies the Report in which the error occurred.  For example, this will be the incident number
	 * for Group As and the ATN for Group Bs.
	 * @return the unique ID
	 */
	public String getReportUniqueIdentifier() {
		return reportUniqueIdentifier;
	}
	public void setReportUniqueIdentifier(String identifier) {
		this.reportUniqueIdentifier = identifier;
	}
	/**
	 * The specific data value that caused the error.  For example, if element X can only have a value of "A" or "B", and a
	 * report has a value of "Z" for element X, then getValue() should return "Z".
	 * @return
	 */
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * Get the segment type of the segment that is in error.
	 * @return the segment type code
	 */
	public char getSegmentType() {
		return segmentType;
	}
	public void setSegmentType(char segmentType) {
		this.segmentType = segmentType;
	}
	/**
	 * The value that uniquely identifies the erroneous segment within all the segments of that type in the report.  For example, if a 
	 * Group A report has three offense segments, with UCR Offense Codes = ('13A', '200', '24C'), then getWithinSegmentIdentifier() should
	 * return '200' to identify the second of these offense segments.
	 * @return
	 */
	public String getWithinSegmentIdentifier() {
		return withinSegmentIdentifier;
	}
	public void setWithinSegmentIdentifier(String withinSegmentIdentifier) {
		this.withinSegmentIdentifier = withinSegmentIdentifier;
	}
	
	
	public NIBRSErrorCode getNibrsErrorCode() {
		return nibrsErrorCode;
	}

	public void setNibrsErrorCode(NIBRSErrorCode nibrsErrorCode) {
		this.nibrsErrorCode = nibrsErrorCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((ruleDescription == null) ? 0 : ruleDescription.hashCode());
		result = prime * result + ((ruleNumber == null) ? 0 : ruleNumber.hashCode());
		result = prime * result + segmentType;
		result = prime * result + ((reportUniqueIdentifier == null) ? 0 : reportUniqueIdentifier.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((withinSegmentIdentifier == null) ? 0 : withinSegmentIdentifier.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}
	
}
