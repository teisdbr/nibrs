package org.search.nibrs.model;

import java.util.*;

/**
 * Representation of a full NIBRS report file.
 *
 */
public class NIBRSSubmission
{
    
    private List<Incident> incidentList;
    
    public NIBRSSubmission()
    {
        incidentList = new ArrayList<Incident>();
    }
    
    public void addIncident(Incident incident)
    {
        incidentList.add(incident);
    }
    
    public List<Incident> getIncidents() {
    	return Collections.unmodifiableList(incidentList);
    }
    
    public int getIncidentCount()
    {
        return incidentList.size();
    }

}
