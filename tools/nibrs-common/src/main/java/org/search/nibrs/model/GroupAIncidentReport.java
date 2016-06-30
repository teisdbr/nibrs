package org.search.nibrs.model;

import java.io.Serializable;
import java.util.*;

/**
 * Representation of an individual Group A incident in a NIBRS submission.
 *
 */
public class GroupAIncidentReport extends Report implements Serializable
{
    
	private static final long serialVersionUID = 3136534413958035709L;
	
	private String incidentNumber;
    private Date incidentDate;
    private String reportDateIndicatorS;
    private Integer incidentHour;
    private String exceptionalClearanceCode;
    private Date exceptionalClearanceDate;
    private Boolean cargoTheftIndicator;
    private List<Offense> offenseSegmentList;
    private List<Property> propertySegmentList;
    private List<Victim> victimSegmentList;
    private List<Offender> offenderSegmentList;
    private List<Arrestee> arresteeSegmentList;

    public GroupAIncidentReport()
    {
        removeOffenses();
        removeProperties();
        removeVictims();
        removeOffenders();
        removeArrestees();
    }
    
    public String getUniqueReportDescription() {
    	return "Group A Incident: Incident # " + incidentNumber;
    }
    
    public String getUniqueReportIdentifier() {
    	return getOri() + "." + incidentNumber;
    }
    
    public GroupAIncidentReport deepCopy() {
    	return (GroupAIncidentReport) copyWithObjectStream(this);
    }

	public void removeArrestee(int index) {
		arresteeSegmentList.remove(index);
	}

	public void removeArrestees() {
		arresteeSegmentList = new ArrayList<Arrestee>();
	}

	public void removeOffender(int index) {
		offenderSegmentList.remove(index);
	}

	public void removeOffenders() {
		offenderSegmentList = new ArrayList<Offender>();
	}

	public void removeVictim(int index) {
		victimSegmentList.remove(index);
	}

	public void removeVictims() {
		victimSegmentList = new ArrayList<Victim>();
	}

	public void removeProperty(int index) {
		propertySegmentList.remove(index);
	}

	public void removeProperties() {
		propertySegmentList = new ArrayList<Property>();
	}

	public void removeOffense(int index) {
		offenseSegmentList.remove(index);
	}
    
	public void removeOffenses() {
		offenseSegmentList = new ArrayList<Offense>();
	}
    
    public Boolean getCargoTheftIndicator() {
		return cargoTheftIndicator;
	}

	public void setCargoTheftIndicator(Boolean cargoTheftIndicator) {
		this.cargoTheftIndicator = cargoTheftIndicator;
	}

	public void addOffense(Offense offense)
    {
        offenseSegmentList.add(offense);
    }
    
    public int getOffenseCount()
    {
        return offenseSegmentList.size();
    }
    
    public Iterator<Offense> offenseIterator()
    {
        return getOffenses().iterator();
    }
    
    public List<Offense> getOffenses() {
    	return Collections.unmodifiableList(offenseSegmentList);
    }

    public void addProperty(Property property)
    {
        propertySegmentList.add(property);
    }
    
    public int getPropertyCount()
    {
        return propertySegmentList.size();
    }
    
    public Iterator<Property> propertyIterator()
    {
        return getProperties().iterator();
    }
    
    public List<Property> getProperties() {
		return Collections.unmodifiableList(propertySegmentList);
	}

	public void addVictim(Victim victim)
    {
        victimSegmentList.add(victim);
    }
    
    public int getVictimCount()
    {
        return victimSegmentList.size();
    }
    
    public Iterator<Victim> victimIterator()
    {
        return getVictims().iterator();
    }

	public List<Victim> getVictims() {
		return Collections.unmodifiableList(victimSegmentList);
	}

    public void addOffender(Offender offender)
    {
        offenderSegmentList.add(offender);
    }
    
    public int getOffenderCount()
    {
        return offenderSegmentList.size();
    }
    
    public Iterator<Offender> offenderIterator()
    {
        return getOffenders().iterator();
    }

    public List<Offender> getOffenders() {
		return Collections.unmodifiableList(offenderSegmentList);
	}

    public void addArrestee(Arrestee arrestee)
    {
        arresteeSegmentList.add(arrestee);
    }
    
    public int getArresteeCount()
    {
        return arresteeSegmentList.size();
    }
    
    public Iterator<Arrestee> arresteeIterator()
    {
        return getArrestees().iterator();
    }

    public List<Arrestee> getArrestees() {
		return Collections.unmodifiableList(arresteeSegmentList);
	}

    public String getExceptionalClearanceCode()
    {
        return exceptionalClearanceCode;
    }
    public void setExceptionalClearanceCode(String exceptionalClearanceCode)
    {
        this.exceptionalClearanceCode = exceptionalClearanceCode;
    }
    public Date getExceptionalClearanceDate()
    {
        return exceptionalClearanceDate;
    }
    public void setExceptionalClearanceDate(Date exceptionalClearanceDate)
    {
        this.exceptionalClearanceDate = exceptionalClearanceDate;
    }
    public Date getIncidentDate()
    {
        return incidentDate;
    }
    public void setIncidentDate(Date incidentDate)
    {
        this.incidentDate = incidentDate;
    }
    public Integer getIncidentHour()
    {
        return incidentHour;
    }
    public void setIncidentHour(Integer incidentHour)
    {
        this.incidentHour = incidentHour;
    }
    public String getIncidentNumber()
    {
        return incidentNumber;
    }
    public void setIncidentNumber(String incidentNumber)
    {
        this.incidentNumber = incidentNumber;
    }
    public boolean getReportDateIndicator()
    {
        return "R".equals(reportDateIndicatorS);
    }
    public void setReportDateIndicator(String reportDateIndicator)
    {
        this.reportDateIndicatorS = reportDateIndicator;
    }
}
