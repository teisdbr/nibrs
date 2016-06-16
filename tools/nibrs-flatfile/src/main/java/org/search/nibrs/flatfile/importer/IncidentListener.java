package org.search.nibrs.flatfile.importer;

import org.search.nibrs.model.*;

/**
 * Interface for objects that are interested in processing new Incidents as they are built from a NIBRS report stream.
 *
 */
public interface IncidentListener
{

    /**
     * Called on implementations when the builder creates a new Incident from the report stream.
     * @param newIncident the new Incident object
     */
    public void newIncident(Incident newIncident);

}
