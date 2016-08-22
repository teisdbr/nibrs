package org.search.nibrs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representation of an individual Group A incident in a NIBRS submission.
 *
 */
public class GroupAIncidentReport extends AbstractReport
{
    
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(GroupAIncidentReport.class);
	
	private String incidentNumber;
    private Date incidentDate;
    private String reportDateIndicator;
    private Integer incidentHour;
    private String exceptionalClearanceCode;
    private Date exceptionalClearanceDate;
    private String cargoTheftIndicator;
    private List<OffenseSegment> offenseSegmentList;
    private List<PropertySegment> propertySegmentList;
    private List<VictimSegment> victimSegmentList;
    private List<OffenderSegment> offenderSegmentList;
    private boolean includesLeoka = false;
    private boolean includesCargoTheft = false;

	public GroupAIncidentReport()
    {
		super('1');
        removeOffenses();
        removeProperties();
        removeVictims();
        removeOffenders();
    }
	
	public GroupAIncidentReport(GroupAIncidentReport r) {
		super(r);
		this.incidentNumber = r.incidentNumber;
		this.incidentDate = r.incidentDate;
		this.reportDateIndicator = r.reportDateIndicator;
		this.incidentHour = r.incidentHour;
		this.exceptionalClearanceCode = r.exceptionalClearanceCode;
		this.exceptionalClearanceDate = r.exceptionalClearanceDate;
		this.cargoTheftIndicator = r.cargoTheftIndicator;
		this.includesLeoka = r.includesLeoka;
		offenseSegmentList = CopyUtils.copyList(r.offenseSegmentList);
		setParentReportOnChildSegments(offenseSegmentList);
		propertySegmentList = CopyUtils.copyList(r.propertySegmentList);
		setParentReportOnChildSegments(propertySegmentList);
		victimSegmentList = CopyUtils.copyList(r.victimSegmentList);
		setParentReportOnChildSegments(victimSegmentList);
		offenderSegmentList = CopyUtils.copyList(r.offenderSegmentList);
		setParentReportOnChildSegments(offenderSegmentList);
	}
	
	private final void setParentReportOnChildSegments(List<? extends AbstractSegment> segments) {
		for (AbstractSegment s : segments) {
			s.setParentReport(this);
		}
	}
	
	public OffenderSegment getOffenderForSequenceNumber(Integer sequenceNumber) {
		OffenderSegment ret = null;
		if (sequenceNumber == null) {
			return null;
		}
		for (OffenderSegment o : offenderSegmentList) {
			if (sequenceNumber.equals(o.getOffenderSequenceNumber())) {
				ret = o;
				break;
			}
		}
		return ret;
	}
    
	public boolean includesCargoTheft() {
		return includesCargoTheft;
	}

	public void setIncludesCargoTheft(boolean includesCargoTheft) {
		this.includesCargoTheft = includesCargoTheft;
	}

    public boolean includesLeoka() {
		return includesLeoka;
	}

	public void setIncludesLeoka(boolean includesLeoka) {
		this.includesLeoka = includesLeoka;
	}

    public String getUniqueReportDescription() {
    	return "Group A Incident: Incident # " + incidentNumber;
    }
    
    public String getUniqueReportIdentifier() {
    	return getOri() + "." + incidentNumber;
    }
    
    public void removeOffender(int index) {
		offenderSegmentList.remove(index);
	}

	public void removeOffenders() {
		offenderSegmentList = new ArrayList<OffenderSegment>();
	}

	public void removeVictim(int index) {
		victimSegmentList.remove(index);
	}

	public void removeVictims() {
		victimSegmentList = new ArrayList<VictimSegment>();
	}

	public void removeProperty(int index) {
		propertySegmentList.remove(index);
	}

	public void removeProperties() {
		propertySegmentList = new ArrayList<PropertySegment>();
	}

	public void removeOffense(int index) {
		offenseSegmentList.remove(index);
	}
    
	public void removeOffenses() {
		offenseSegmentList = new ArrayList<OffenseSegment>();
	}
    
    public String getCargoTheftIndicator() {
		return cargoTheftIndicator;
	}

	public void setCargoTheftIndicator(String cargoTheftIndicator) {
		this.cargoTheftIndicator = cargoTheftIndicator;
	}

	public void addOffense(OffenseSegment offense)
    {
        offenseSegmentList.add(offense);
    }
    
    public int getOffenseCount()
    {
        return offenseSegmentList.size();
    }
    
    public Iterator<OffenseSegment> offenseIterator()
    {
        return getOffenses().iterator();
    }
    
    public List<OffenseSegment> getOffenses() {
    	return Collections.unmodifiableList(offenseSegmentList);
    }
    
    public void addProperty(PropertySegment property)
    {
        propertySegmentList.add(property);
    }
    
    public int getPropertyCount()
    {
        return propertySegmentList.size();
    }
    
    public Iterator<PropertySegment> propertyIterator()
    {
        return getProperties().iterator();
    }
    
    public List<PropertySegment> getProperties() {
		return Collections.unmodifiableList(propertySegmentList);
	}

	public void addVictim(VictimSegment victim)
    {
        victimSegmentList.add(victim);
    }
    
    public int getVictimCount()
    {
        return victimSegmentList.size();
    }
    
    public Iterator<VictimSegment> victimIterator()
    {
        return getVictims().iterator();
    }

	public List<VictimSegment> getVictims() {
		return Collections.unmodifiableList(victimSegmentList);
	}

    public void addOffender(OffenderSegment offender)
    {
        offenderSegmentList.add(offender);
    }
    
    public int getOffenderCount()
    {
        return offenderSegmentList.size();
    }
    
    public Iterator<OffenderSegment> offenderIterator()
    {
        return getOffenders().iterator();
    }

    public List<OffenderSegment> getOffenders() {
		return Collections.unmodifiableList(offenderSegmentList);
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
    public String getReportDateIndicator()
    {
        return reportDateIndicator;
    }
    public void setReportDateIndicator(String reportDateIndicator)
    {
        this.reportDateIndicator = reportDateIndicator;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cargoTheftIndicator == null) ? 0 : cargoTheftIndicator.hashCode());
		result = prime * result + ((exceptionalClearanceCode == null) ? 0 : exceptionalClearanceCode.hashCode());
		result = prime * result + ((exceptionalClearanceDate == null) ? 0 : exceptionalClearanceDate.hashCode());
		result = prime * result + ((incidentDate == null) ? 0 : incidentDate.hashCode());
		result = prime * result + ((incidentHour == null) ? 0 : incidentHour.hashCode());
		result = prime * result + ((incidentNumber == null) ? 0 : incidentNumber.hashCode());
		result = prime * result + (includesLeoka ? 1231 : 1237);
		result = prime * result + ((offenderSegmentList == null) ? 0 : offenderSegmentList.hashCode());
		result = prime * result + ((offenseSegmentList == null) ? 0 : offenseSegmentList.hashCode());
		result = prime * result + ((propertySegmentList == null) ? 0 : propertySegmentList.hashCode());
		result = prime * result + ((reportDateIndicator == null) ? 0 : reportDateIndicator.hashCode());
		result = prime * result + ((victimSegmentList == null) ? 0 : victimSegmentList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(1024);
		sb.append("GroupAIncidentReport:\n");
		sb.append(super.toString());
		sb.append("[incidentNumber=" + incidentNumber + ", incidentDate=" + incidentDate + ", reportDateIndicator=" + reportDateIndicator + ", incidentHour=" + incidentHour
				+ ", exceptionalClearanceCode=" + exceptionalClearanceCode + ", exceptionalClearanceDate=" + exceptionalClearanceDate + ", cargoTheftIndicator=" + cargoTheftIndicator + ", includesLeoka=" + includesLeoka);
		sb.append("\n").append(offenseSegmentList.size() + " OffenseSegment Segments:\n");
		for (OffenseSegment o : offenseSegmentList) {
			sb.append("\t").append(o.toString()).append("\n");
		}
		sb.append("\n").append(offenderSegmentList.size() + " OffenderSegment Segments:\n");
		for (OffenderSegment o : offenderSegmentList) {
			sb.append("\t").append(o.toString()).append("\n");
		}
		sb.append("\n").append(propertySegmentList.size() + " PropertySegment Segments:\n");
		for (PropertySegment p : propertySegmentList) {
			sb.append("\t").append(p.toString()).append("\n");
		}
		sb.append("\n").append(victimSegmentList.size() + " VictimSegment Segments:\n");
		for (VictimSegment v : victimSegmentList) {
			sb.append("\t").append(v.toString()).append("\n");
		}
		
		return sb.toString();
	}
}
