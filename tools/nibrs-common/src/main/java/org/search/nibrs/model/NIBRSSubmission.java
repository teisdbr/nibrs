package org.search.nibrs.model;

import java.util.*;

/**
 * Representation of a full NIBRS report file.
 *
 */
public class NIBRSSubmission
{
    
    private List<AbstractReport> reportList;
    
    public NIBRSSubmission()
    {
        reportList = new ArrayList<AbstractReport>();
    }
    
    public void addReports(Collection<AbstractReport> reports) {
    	reportList.addAll(reports);
    }
    
    public void addReport(AbstractReport incident)
    {
        reportList.add(incident);
    }
    
    public List<AbstractReport> getReports() {
    	return Collections.unmodifiableList(reportList);
    }
    
    public int getReportCount()
    {
        return reportList.size();
    }

}
