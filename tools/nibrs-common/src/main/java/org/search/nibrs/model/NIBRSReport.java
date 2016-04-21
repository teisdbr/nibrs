package org.search.nibrs.model;

import java.util.*;

/**
 * Representation of a full NIBRS report file.
 *
 */
public class NIBRSReport
{
    
    private List<Incident> incidentList;
    
    public NIBRSReport()
    {
        incidentList = new ArrayList<Incident>();
    }
    
    public void addIncident(Incident incident)
    {
        incidentList.add(incident);
    }
    
    public Iterator<Incident> incidentIterator()
    {
        return Collections.unmodifiableList(incidentList).iterator();
    }
    
    public int getIncidentCount()
    {
        return incidentList.size();
    }

}
