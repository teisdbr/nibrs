package org.search.nibrs.model;

import java.io.Serializable;

/**
 * Representation of a Victim reported within an Incident in a NIBRS report.  Note that we extend Person even though some types of victims are not people
 * (e.g., Business)...since NIBRS represents them all with the same data structure.
 *
 */
public class Victim extends Person implements Serializable
{
    
	private static final long serialVersionUID = -1133037294039133967L;
	
	private Integer victimSequenceNumber;
    private String[] ucrOffenseCodeConnection;
    private String typeOfVictim;
    private String residentStatusOfVictim;
    private String[] aggravatedAssaultHomicideCircumstances;
    private String additionalJustifiableHomicideCircumstances;
    private String[] typeOfInjury;
    private Integer[] offenderNumberRelated;
    private String[] victimOffenderRelationship;
    private String typeOfOfficerActivityCircumstance;
    private String officerAssignmentType;
    private String officerOtherJurisdictionORI;
    private int populatedAggravatedAssaultHomicideCircumstancesCount;
    private int populatedTypeOfInjuryCount;
    private int populatedUcrOffenseCodeConnectionCount;
    private int populatedOffenderNumberRelatedCount;

    public String getTypeOfOfficerActivityCircumstance() {
		return typeOfOfficerActivityCircumstance;
	}

	public void setTypeOfOfficerActivityCircumstance(String typeOfOfficerActivityCircumstance) {
		this.typeOfOfficerActivityCircumstance = typeOfOfficerActivityCircumstance;
	}

	public String getOfficerAssignmentType() {
		return officerAssignmentType;
	}

	public void setOfficerAssignmentType(String officerAssignmentType) {
		this.officerAssignmentType = officerAssignmentType;
	}

	public String getOfficerOtherJurisdictionORI() {
		return officerOtherJurisdictionORI;
	}

	public void setOfficerOtherJurisdictionORI(String officerOtherJurisdictionORI) {
		this.officerOtherJurisdictionORI = officerOtherJurisdictionORI;
	}

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
        populatedOffenderNumberRelatedCount = Math.max(populatedOffenderNumberRelatedCount, position+1);
    }

    public int getPopulatedUcrOffenseCodeConnectionCount() {
		return populatedUcrOffenseCodeConnectionCount;
	}

	public int getPopulatedOffenderNumberRelatedCount() {
		return populatedOffenderNumberRelatedCount;
	}

	public String getTypeOfInjury(int position)
    {
        return typeOfInjury[position];
    }
    
    public void setTypeOfInjury(int position, String value)
    {
        typeOfInjury[position] = value;
        populatedTypeOfInjuryCount = Math.max(populatedTypeOfInjuryCount, position+1);
    }

    public String getAggravatedAssaultHomicideCircumstances(int position)
    {
        return aggravatedAssaultHomicideCircumstances[position];
    }
    
    public void setAggravatedAssaultHomicideCircumstances(int position, String value)
    {
        aggravatedAssaultHomicideCircumstances[position] = value;
        populatedAggravatedAssaultHomicideCircumstancesCount = Math.max(populatedAggravatedAssaultHomicideCircumstancesCount,  position+1);
    }

    public int getPopulatedAggravatedAssaultHomicideCircumstancesCount() {
		return populatedAggravatedAssaultHomicideCircumstancesCount;
	}

	public int getPopulatedTypeOfInjuryCount() {
		return populatedTypeOfInjuryCount;
	}

	public String getUcrOffenseCodeConnection(int position)
    {
        return ucrOffenseCodeConnection[position];
    }
    
    public void setUcrOffenseCodeConnection(int position, String value)
    {
        ucrOffenseCodeConnection[position] = value;
        populatedUcrOffenseCodeConnectionCount = Math.max(populatedUcrOffenseCodeConnectionCount, position+1);
    }

    public String getAdditionalJustifiableHomicideCircumstances()
    {
        return additionalJustifiableHomicideCircumstances;
    }
    public void setAdditionalJustifiableHomicideCircumstances(String additionalJustifiableHomicideCircumstances)
    {
        this.additionalJustifiableHomicideCircumstances = additionalJustifiableHomicideCircumstances;
    }
    public String getResidentStatusOfVictim()
    {
        return residentStatusOfVictim;
    }
    public void setResidentStatusOfVictim(String residenceStatusOfVictim)
    {
        this.residentStatusOfVictim = residenceStatusOfVictim;
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

	public boolean isPerson() {
		return "I".equals(typeOfVictim) || "L".equals(typeOfVictim);
	}
	
	public boolean isLawEnforcementOfficer() {
		return "L".equals(typeOfVictim);
	}

}
