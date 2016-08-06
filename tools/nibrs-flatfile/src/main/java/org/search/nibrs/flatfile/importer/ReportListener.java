package org.search.nibrs.flatfile.importer;

import org.search.nibrs.model.*;

/**
 * Interface for objects that are interested in processing new Incidents as they are built from a NIBRS report stream.
 *
 */
public interface ReportListener
{

    /**
     * Called on implementations when the builder creates a new AbstractReport from the report stream.
     * @param report the new AbstractReport object
     */
    public void newReport(AbstractReport report);

}
