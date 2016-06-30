package org.search.nibrs.common;

/**
 * A class for objects that represent an error encountered in processing NIBRS data.
 *
 */
public class NIBRSError {
	
	private Object context;
	private Integer ruleNumber;
	private String ruleDescription;
	private String segmentUniqueIdentifier;
	private Object value;
	private char segmentType;
	private String withinSegmentIdentifier;
	
	public String getRuleDescription() {
		return ruleDescription;
	}
	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}
	public Object getContext() {
		return context;
	}
	public void setContext(Object context) {
		this.context = context;
	}
	public Integer getRuleNumber() {
		return ruleNumber;
	}
	public void setRuleNumber(Integer ruleNumber) {
		this.ruleNumber = ruleNumber;
	}
	public String getSegmentUniqueIdentifier() {
		return segmentUniqueIdentifier;
	}
	public void setSegmentUniqueIdentifier(String identifier) {
		this.segmentUniqueIdentifier = identifier;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public char getSegmentType() {
		return segmentType;
	}
	public void setSegmentType(char segmentType) {
		this.segmentType = segmentType;
	}
	public String getWithinSegmentIdentifier() {
		return withinSegmentIdentifier;
	}
	public void setWithinSegmentIdentifier(String withinSegmentIdentifier) {
		this.withinSegmentIdentifier = withinSegmentIdentifier;
	}
	
}
