package org.search.nibrs.flatfile.importer;

import org.search.nibrs.model.*;

/**
 * Interface for objects that are interested in processing new Incidents as they are built from a NIBRS report stream.
 *
 */
public interface ReportListener
{

    /**
     * Called on implementations when the builder creates a new Report from the report stream.
     * @param report the new Report object
     */
    public void newReport(Report report);

}
