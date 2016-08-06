package org.search.nibrs.flatfile.importer;

import java.util.*;

import org.search.nibrs.model.*;

/**
 * A default implementation of the IncidentListener interface, that simply stores up the incidents and makes them available
 * when building is done.
 *
 */
public final class DefaultReportListener implements ReportListener
{
    
    private List<AbstractReport> reports = new ArrayList<AbstractReport>();
    private Map<String, Throwable> throwables = new HashMap<String, Throwable>();

    public void newReport(AbstractReport newReport)
    {
        reports.add(newReport);
    }
    
    public List<AbstractReport> getReportList()
    {
        return Collections.unmodifiableList(reports);
    }

    public Map<String, Throwable> getThrowablesMap()
    {
        return Collections.unmodifiableMap(throwables);
    }
    
    public List<GroupAIncidentReport> getGroupAIncidentList() {
    	List<GroupAIncidentReport> ret = new ArrayList<GroupAIncidentReport>();
    	for (AbstractReport r : reports) {
    		if (r instanceof GroupAIncidentReport) {
    			ret.add((GroupAIncidentReport) r);
    		}
    	}
    	return ret;
    }
    
    public List<GroupBIncidentReport> getGroupBIncidentList() {
    	List<GroupBIncidentReport> ret = new ArrayList<GroupBIncidentReport>();
    	for (AbstractReport r : reports) {
    		if (r instanceof GroupBIncidentReport) {
    			ret.add((GroupBIncidentReport) r);
    		}
    	}
    	return ret;
    }
    
    public List<ZeroReport> getZeroReportList() {
    	List<ZeroReport> ret = new ArrayList<ZeroReport>();
    	for (AbstractReport r : reports) {
    		if (r instanceof ZeroReport) {
    			ret.add((ZeroReport) r);
    		}
    	}
    	return ret;
    }
    
}
