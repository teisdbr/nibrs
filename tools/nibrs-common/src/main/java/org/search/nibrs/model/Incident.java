package org.search.nibrs.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Representation of an individual incident in a NIBRS report.
 *
 */
public class Incident implements Serializable
{
    
	private static final long serialVersionUID = 3136534413958035709L;
	
	private int monthOfTape;
    private int yearOfTape;
    private String cityIndicator;
    private String ori;
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

    public Incident()
    {
        removeOffenses();
        removeProperties();
        removeVictims();
        removeOffenders();
        removeArrestees();
    }
    
    public Incident deepCopy() {
    	
    	Incident ret = null;
    	
    	try {
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			ret = (Incident) ois.readObject();
    	} catch (IOException ioe) {
    		// this should never really happen
    		throw new RuntimeException(ioe);
    	} catch (ClassNotFoundException cnfe) {
    		// this should never really happen
    		throw new RuntimeException(cnfe);
		}
    	
    	return ret;
    	
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

    public String getCityIndicator()
    {
        return cityIndicator;
    }
    public void setCityIndicator(String cityIndicator)
    {
        this.cityIndicator = cityIndicator;
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
    public int getMonthOfTape()
    {
        return monthOfTape;
    }
    public void setMonthOfTape(int monthOfTape)
    {
        this.monthOfTape = monthOfTape;
    }
    public String getOri()
    {
        return ori;
    }
    public void setOri(String ori)
    {
        this.ori = ori;
    }
    public boolean getReportDateIndicator()
    {
        return "R".equals(reportDateIndicatorS);
    }
    public void setReportDateIndicator(String reportDateIndicator)
    {
        this.reportDateIndicatorS = reportDateIndicator;
    }
    public int getYearOfTape()
    {
        return yearOfTape;
    }
    public void setYearOfTape(int yearOfTape)
    {
        this.yearOfTape = yearOfTape;
    }
}
