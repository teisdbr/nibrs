package org.search.nibrs.model;

import java.util.*;

/**
 * Representation of a full NIBRS report file.
 *
 */
public class NIBRSSubmission
{
    
    private List<Report> reportList;
    
    public NIBRSSubmission()
    {
        reportList = new ArrayList<Report>();
    }
    
    public void addReports(Collection<Report> reports) {
    	reportList.addAll(reports);
    }
    
    public void addReport(Report incident)
    {
        reportList.add(incident);
    }
    
    public List<Report> getReports() {
    	return Collections.unmodifiableList(reportList);
    }
    
    public int getReportCount()
    {
        return reportList.size();
    }

}
