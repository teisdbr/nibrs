package org.search.nibrs.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private String segmentUniqueIdentifier;
	private Object value;
	private char segmentType;
	private String withinSegmentIdentifier;
	
	public NIBRSError() {
	}
	
	public NIBRSError(NIBRSError e) {
		this.context = e.context;
		this.ruleNumber = e.ruleNumber;
		this.ruleDescription = e.ruleDescription;
		this.segmentUniqueIdentifier = e.segmentUniqueIdentifier;
		this.value = e.value;
		this.segmentType = e.segmentType;
		this.withinSegmentIdentifier = e.withinSegmentIdentifier;
	}
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((ruleDescription == null) ? 0 : ruleDescription.hashCode());
		result = prime * result + ((ruleNumber == null) ? 0 : ruleNumber.hashCode());
		result = prime * result + segmentType;
		result = prime * result + ((segmentUniqueIdentifier == null) ? 0 : segmentUniqueIdentifier.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((withinSegmentIdentifier == null) ? 0 : withinSegmentIdentifier.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}
	
}
