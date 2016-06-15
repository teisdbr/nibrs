package org.search.nibrs.model;

import java.io.Serializable;
import java.util.*;

/**
 * Representation of an Arrestee reported within an Incident in a NIBRS report.
 *
 */
public class Arrestee implements Serializable
{
    
	private static final long serialVersionUID = 7160255436781736744L;
	
	private Integer arresteeSequenceNumber;
    private String arrestTransactionNumber;
    private Date arrestDate;
    private String typeOfArrest;
    private String multipleArresteeSegmentsIndicator;
    private String ucrArrestOffenseCode;
    private String[] arresteeArmedWith;
    private String[] automaticWeaponIndicator;
    private String ageOfArresteeString;
    private String sexOfArrestee;
    private String raceOfArrestee;
    private String ethnicityOfArrestee;
    private String residentStatusOfArrestee;
    private String dispositionOfArresteeUnder18;

    public Arrestee()
    {
        arresteeArmedWith = new String[2];
        automaticWeaponIndicator = new String[2];
    }
    
    public String getAutomaticWeaponIndicator(int position)
    {
        return automaticWeaponIndicator[position];
    }
    
    public void setAutomaticWeaponIndicator(int position, String value)
    {
        automaticWeaponIndicator[position] = value;
    }
    
    public String getArresteeArmedWith(int position)
    {
        return arresteeArmedWith[position];
    }
    
    public void setArresteeArmedWith(int position, String value)
    {
        arresteeArmedWith[position] = value;
    }
    
    public String getAgeOfArresteeString()
    {
        return ageOfArresteeString;
    }
    public void setAgeOfArresteeString(String ageOfArresteeString)
    {
        this.ageOfArresteeString = ageOfArresteeString;
    }
    public Date getArrestDate()
    {
        return arrestDate;
    }
    public void setArrestDate(Date arrestDate)
    {
        this.arrestDate = arrestDate;
    }
    public Integer getArresteeSequenceNumber()
    {
        return arresteeSequenceNumber;
    }
    public void setArresteeSequenceNumber(Integer arresteeSequenceNumber)
    {
        this.arresteeSequenceNumber = arresteeSequenceNumber;
    }
    public String getArrestTransactionNumber()
    {
        return arrestTransactionNumber;
    }
    public void setArrestTransactionNumber(String arrestTransactionNumber)
    {
        this.arrestTransactionNumber = arrestTransactionNumber;
    }
    public String getDispositionOfArresteeUnder18()
    {
        return dispositionOfArresteeUnder18;
    }
    public void setDispositionOfArresteeUnder18(String dispositionOfArresteeUnder18)
    {
        this.dispositionOfArresteeUnder18 = dispositionOfArresteeUnder18;
    }
    public String getEthnicityOfArrestee()
    {
        return ethnicityOfArrestee;
    }
    public void setEthnicityOfArrestee(String ethnicityOfArrestee)
    {
        this.ethnicityOfArrestee = ethnicityOfArrestee;
    }
    public String getMultipleArresteeSegmentsIndicator()
    {
        return multipleArresteeSegmentsIndicator;
    }
    public void setMultipleArresteeSegmentsIndicator(String multipleArresteeSegmentsIndicator)
    {
        this.multipleArresteeSegmentsIndicator = multipleArresteeSegmentsIndicator;
    }
    public String getRaceOfArrestee()
    {
        return raceOfArrestee;
    }
    public void setRaceOfArrestee(String raceOfArrestee)
    {
        this.raceOfArrestee = raceOfArrestee;
    }
    public String getResidentStatusOfArrestee()
    {
        return residentStatusOfArrestee;
    }
    public void setResidentStatusOfArrestee(String residentStatusOfArrestee)
    {
        this.residentStatusOfArrestee = residentStatusOfArrestee;
    }
    public String getSexOfArrestee()
    {
        return sexOfArrestee;
    }
    public void setSexOfArrestee(String sexOfArrestee)
    {
        this.sexOfArrestee = sexOfArrestee;
    }
    public String getTypeOfArrest()
    {
        return typeOfArrest;
    }
    public void setTypeOfArrest(String typeOfArrest)
    {
        this.typeOfArrest = typeOfArrest;
    }
    public String getUcrArrestOffenseCode()
    {
        return ucrArrestOffenseCode;
    }
    public void setUcrArrestOffenseCode(String ucrArrestOffenseCode)
    {
        this.ucrArrestOffenseCode = ucrArrestOffenseCode;
    }

}
