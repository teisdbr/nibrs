package org.search.nibrs.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Report implements Serializable {

	private static final long serialVersionUID = -8095472158829269035L;

	private Integer monthOfTape;
	private Integer yearOfTape;
	private String cityIndicator;
	private String ori;
	private char adminSegmentLevel;
	private char reportActionType;
	private boolean hasUpstreamErrors;
	
	public char getReportActionType() {
		return reportActionType;
	}
	public void setReportActionType(char reportActionType) {
		this.reportActionType = reportActionType;
	}
	
	public abstract String getUniqueReportDescription();
	public abstract String getUniqueReportIdentifier();

	public char getAdminSegmentLevel() {
		return adminSegmentLevel;
	}

	public void setAdminSegmentLevel(char c) {
		this.adminSegmentLevel = c;
	}

	public String getCityIndicator() {
	    return cityIndicator;
	}

	public void setCityIndicator(String cityIndicator) {
	    this.cityIndicator = cityIndicator;
	}

	public int getMonthOfTape() {
	    return monthOfTape;
	}

	public void setMonthOfTape(Integer monthOfTape) {
		this.monthOfTape = monthOfTape;
	}

	public String getOri() {
	    return ori;
	}

	public void setOri(String ori) {
	    this.ori = ori;
	}

	public int getYearOfTape() {
	    return yearOfTape;
	}

	public void setYearOfTape(Integer yearOfTape) {
	    this.yearOfTape = yearOfTape;
	}

	public boolean getHasUpstreamErrors() {
		return hasUpstreamErrors;
	}

	public void setHasUpstreamErrors(boolean hasUpstreamErrors) {
		this.hasUpstreamErrors = hasUpstreamErrors;
	}
	protected static final Report copyWithObjectStream(Report report) {
		Report ret = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(report);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			ret = (Report) ois.readObject();
		} catch (IOException ioe) {
			// this should never really happen
			throw new RuntimeException(ioe);
		} catch (ClassNotFoundException cnfe) {
			// this should never really happen
			throw new RuntimeException(cnfe);
		}
		return ret;
	}
	
}
