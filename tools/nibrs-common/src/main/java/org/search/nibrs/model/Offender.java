package org.search.nibrs.model;

/**
 * Representation of an Offender reported within an Incident in a NIBRS report.
 *
 */
public class Offender
{
    
    private Integer offenderSequenceNumber;
    private String ageOfOffenderString;
    private String sexOfOffender;
    private String raceOfOffender;

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
