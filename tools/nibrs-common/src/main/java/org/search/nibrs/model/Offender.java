package org.search.nibrs.model;

import java.io.Serializable;

/**
 * Representation of an Offender reported within an Incident in a NIBRS report.
 *
 */
public class Offender implements Serializable
{
    
	private static final long serialVersionUID = 871781322681376238L;
	
	private Integer offenderSequenceNumber;
    private String ageOfOffenderString;
    private String sexOfOffender;
    private String raceOfOffender;
    private String ethnicityOfOffender;

    public String getEthnicityOfOffender() {
		return ethnicityOfOffender;
	}
	public void setEthnicityOfOffender(String ethnicityOfOffender) {
		this.ethnicityOfOffender = ethnicityOfOffender;
	}
	public String getAgeOfOffenderString()
    {
        return ageOfOffenderString;
    }
    public void setAgeOfOffenderString(String ageOfOffenderString)
    {
        this.ageOfOffenderString = ageOfOffenderString;
    }
    public Integer getOffenderSequenceNumber()
    {
        return offenderSequenceNumber;
    }
    public void setOffenderSequenceNumber(Integer offenderSequenceNumber)
    {
        this.offenderSequenceNumber = offenderSequenceNumber;
    }
    public String getRaceOfOffender()
    {
        return raceOfOffender;
    }
    public void setRaceOfOffender(String raceOfOffender)
    {
        this.raceOfOffender = raceOfOffender;
    }
    public String getSexOfOffender()
    {
        return sexOfOffender;
    }
    public void setSexOfOffender(String sexOfOffender)
    {
        this.sexOfOffender = sexOfOffender;
    }
    
}
