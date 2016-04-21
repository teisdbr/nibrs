package org.search.nibrs.model;

/**
 * Representation of a Victim reported within an Incident in a NIBRS report.
 *
 */
public class Victim
{
    
    private Integer victimSequenceNumber;
    private String[] ucrOffenseCodeConnection;
    private String typeOfVictim;
    private String ageOfVictimString;
    private String sexOfVictim;
    private String raceOfVictim;
    private String ethnicityOfVictim;
    private String residentStatusOfVictim;
    private String[] aggravatedAssaultHomicideCircumstances;
    private String additionalJustifiableHomicideCircumstances;
    private String[] typeOfInjury;
    private Integer[] offenderNumberRelated;
    private String[] victimOffenderRelationship;

    public Victim()
    {
        ucrOffenseCodeConnection = new String[10];
        aggravatedAssaultHomicideCircumstances = new String[2];
        typeOfInjury = new String[5];
        offenderNumberRelated = new Integer[10];
        victimOffenderRelationship = new String[10];
    }
    
    public String getVictimOffenderRelationship(int position)
    {
        return victimOffenderRelationship[position];
    }
    
    public void setVictimOffenderRelationship(int position, String value)
    {
        victimOffenderRelationship[position] = value;
    }

    public Integer getOffenderNumberRelated(int position)
    {
        return offenderNumberRelated[position];
    }
    
    public void setOffenderNumberRelated(int position, Integer value)
    {
        offenderNumberRelated[position] = value;
    }

    public String getTypeOfInjury(int position)
    {
        return typeOfInjury[position];
    }
    
    public void setTypeOfInjury(int position, String value)
    {
        typeOfInjury[position] = value;
    }

    public String getAggravatedAssaultHomicideCircumstances(int position)
    {
        return aggravatedAssaultHomicideCircumstances[position];
    }
    
    public void setAggravatedAssaultHomicideCircumstances(int position, String value)
    {
        aggravatedAssaultHomicideCircumstances[position] = value;
    }

    public String getUcrOffenseCodeConnection(int position)
    {
        return ucrOffenseCodeConnection[position];
    }
    
    public void setUcrOffenseCodeConnection(int position, String value)
    {
        ucrOffenseCodeConnection[position] = value;
    }

    public String getEthnicityOfVictim()
    {
        return ethnicityOfVictim;
    }
    public void setEthnicityOfVictim(String ethnicityOfVictim)
    {
        this.ethnicityOfVictim = ethnicityOfVictim;
    }
    public String getAdditionalJustifiableHomicideCircumstances()
    {
        return additionalJustifiableHomicideCircumstances;
    }
    public void setAdditionalJustifiableHomicideCircumstances(String additionalJustifiableHomicideCircumstances)
    {
        this.additionalJustifiableHomicideCircumstances = additionalJustifiableHomicideCircumstances;
    }
    public String getAgeOfVictimString()
    {
        return ageOfVictimString;
    }
    public void setAgeOfVictimString(String ageOfVictimString)
    {
        this.ageOfVictimString = ageOfVictimString;
    }
    public String getRaceOfVictim()
    {
        return raceOfVictim;
    }
    public void setRaceOfVictim(String raceOfVictim)
    {
        this.raceOfVictim = raceOfVictim;
    }
    public String getResidentStatusOfVictim()
    {
        return residentStatusOfVictim;
    }
    public void setResidentStatusOfVictim(String residenceStatusOfVictim)
    {
        this.residentStatusOfVictim = residenceStatusOfVictim;
    }
    public String getSexOfVictim()
    {
        return sexOfVictim;
    }
    public void setSexOfVictim(String sexOfVictim)
    {
        this.sexOfVictim = sexOfVictim;
    }
    public String getTypeOfVictim()
    {
        return typeOfVictim;
    }
    public void setTypeOfVictim(String typeOfVictim)
    {
        this.typeOfVictim = typeOfVictim;
    }
    public Integer getVictimSequenceNumber()
    {
        return victimSequenceNumber;
    }
    public void setVictimSequenceNumber(Integer victimSequenceNumber)
    {
        this.victimSequenceNumber = victimSequenceNumber;
    }
    
    public boolean isVictimPerson()
    {
        return NIBRSRules.victimTypeCodeIsPerson(getTypeOfVictim());
    }

}
