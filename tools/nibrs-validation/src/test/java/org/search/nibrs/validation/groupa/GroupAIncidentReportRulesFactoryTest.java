package org.search.nibrs.validation.groupa;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.codes.NibrsErrorCode;
import org.search.nibrs.validation.rules.Rule;

public class GroupAIncidentReportRulesFactoryTest {
	
	private GroupAIncidentReportRulesFactory rulesFactory = new GroupAIncidentReportRulesFactory();
	
	@Test
	public void testRule115() {
		Rule<GroupAIncidentReport> rule115 = rulesFactory.getRule115();
		GroupAIncidentReport report = buildBaseReport();
		report.setIncidentNumber("");
		NIBRSError e = rule115.apply(report, report.getSource());
		assertNotNull(e);
		assertEquals(NibrsErrorCode._115, e.getNibrsErrorCode());
		assertEquals('1', e.getSegmentType());
		assertEquals(report.getIncidentNumber(), e.getValue());
		assertEquals(report.getSource(), e.getContext());
		report.setIncidentNumber("A");
		assertNotNull(rule115.apply(report, report.getSource()));
		report.setIncidentNumber("A B         ");
		assertNotNull(rule115.apply(report, report.getSource()));
		report.setIncidentNumber(" AB         ");
		assertNotNull(rule115.apply(report, report.getSource()));
		report.setIncidentNumber("AB         ");
		assertNotNull(rule115.apply(report, report.getSource()));
		report.setIncidentNumber("AB           ");
		assertNotNull(rule115.apply(report, report.getSource()));
		report.setIncidentNumber("AB          ");
		assertNull(rule115.apply(report, report.getSource()));
		report.setIncidentNumber("ABBBBBBBBBBB");
		assertNull(rule115.apply(report, report.getSource()));
	}

	@Test
	public void testRule115Regex() {
		Pattern p = GroupAIncidentReportRulesFactory.getRule115Regex();
		assertTrue(p.matcher("A           ").matches());
		assertTrue(p.matcher("AB          ").matches());
		assertTrue(p.matcher("ABCCCCCCCCCC").matches());
		assertFalse(p.matcher("").matches());
		assertFalse(p.matcher("           ").matches());
	}

	private GroupAIncidentReport buildBaseReport() {
		GroupAIncidentReport report;
		report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);
		return report;
	}
	
}
