package org.search.nibrs.ff_conversion.importer;

import java.util.*;

import org.search.nibrs.model.*;

/**
 * A default implementation of the IncidentListener interface, that simply stores up the incidents and makes them available
 * when building is done.
 *
 */
public final class DefaultIncidentListener implements IncidentListener
{
    
    private List<Incident> incidents = new ArrayList<Incident>();
    private Map<String, Throwable> throwables = new HashMap<String, Throwable>();

    public void newIncident(Incident newIncident)
    {
        incidents.add(newIncident);
    }
    
    public List<Incident> getIncidentList()
    {
        return Collections.unmodifiableList(incidents);
    }

    public void handleThrowable(Throwable t, String currentIncidentNumber)
    {
        throwables.put(currentIncidentNumber, t);
    }
    
    public Map<String, Throwable> getThrowablesMap()
    {
        return Collections.unmodifiableMap(throwables);
    }
    
}
