package org.search.nibrs.validation.groupa;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.PropertyDescriptionCode;
import org.search.nibrs.model.codes.SuspectedDrugTypeCode;
import org.search.nibrs.model.codes.TypeOfDrugMeasurementCode;
import org.search.nibrs.model.codes.TypeOfPropertyLossCode;
import org.search.nibrs.validation.rules.Rule;

public class PropertySegmentRulesFactoryTest {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(PropertySegmentRulesFactoryTest.class);
	
	private PropertySegmentRulesFactory rulesFactory = new PropertySegmentRulesFactory();
	
	private static final String[] getStringArrayWithValues(int length, String... values) {
		String[] ret = new String[length];
		for (int i=0; i < values.length; i++) {
			ret[i] = values[i];
		}
		return ret;
	}
	
	@Test
	public void testRule305_forIncidentDate() {
		Rule<PropertySegment> rule = rulesFactory.getRule305();
		PropertySegment p = buildBaseSegment();
		GroupAIncidentReport parent = (GroupAIncidentReport) p.getParentReport();
		parent.setIncidentDate(null);
		for (int i=0;i < 10;i++) {
			p.setDateRecovered(i, null);
		}
		NIBRSError e = rule.apply(p);
		assertNull(e);
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.JANUARY, 2);
		parent.setIncidentDate(c.getTime());
		c.set(2016, Calendar.JANUARY, 2);
		p.setDateRecovered(0, c.getTime());
		e = rule.apply(p);
		assertNull(e);
		c.set(2016, Calendar.JANUARY, 1);
		p.setDateRecovered(0, c.getTime());
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("17", e.getDataElementIdentifier());
		assertEquals(c.getTime(), e.getValue());
		c.set(2016, Calendar.JANUARY, 10);
		p.setDateRecovered(1, c.getTime());
		e = rule.apply(p);
		assertNotNull(e);
	}
	
	@Test
	public void testRule305_forSubmissionDate() {
		Rule<PropertySegment> rule = rulesFactory.getRule305();
		PropertySegment p = buildBaseSegment();
		GroupAIncidentReport parent = (GroupAIncidentReport) p.getParentReport();
		parent.setYearOfTape(null);
		parent.setMonthOfTape(null);
		for (int i=0;i < 10;i++) {
			p.setDateRecovered(i, null);
		}
		NIBRSError e = rule.apply(p);
		assertNull(e);
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.JANUARY, 1);
		p.setDateRecovered(0, c.getTime());
		parent.setYearOfTape(2016);
		parent.setMonthOfTape(1);
		e = rule.apply(p);
		assertNull(e);
		c.set(2016, Calendar.FEBRUARY, 1);
		p.setDateRecovered(0, c.getTime());
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("17", e.getDataElementIdentifier());
		assertEquals(c.getTime(), e.getValue());
		c.set(2016, Calendar.JANUARY, 1);
		p.setDateRecovered(1, c.getTime());
		e = rule.apply(p);
		assertNotNull(e);
	}
	
	@Test
	public void testRule304_valueOfProperty() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForPropertyValue();
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 10);
		e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, -10);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("16", e.getDataElementIdentifier());
		assertEquals(-10, e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		p.setValueOfProperty(0, 1000000000);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals(1000000000, e.getValue());
		p.setValueOfProperty(1, 10);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals(1000000000, e.getValue());
	}
	
	@Test
	public void testRule304_estimatedDrugQuantity() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForDrugQuantity();
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setEstimatedDrugQuantity(0, 10.0);
		e = rule.apply(p);
		assertNull(e);
		p.setEstimatedDrugQuantity(0, -10.0);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("21", e.getDataElementIdentifier());
		assertEquals(-10.0, e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		p.setEstimatedDrugQuantity(0, 10000000000.0);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals(10000000000.0, e.getValue());
		p.setEstimatedDrugQuantity(1, 10.0);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals(10000000000.0, e.getValue());
	}
	
	@Test
	public void testRule304_stolenMotorVehicles() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForStolenMotorVehicleCount();
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setNumberOfStolenMotorVehicles(2);
		e = rule.apply(p);
		assertNull(e);
		p.setNumberOfStolenMotorVehicles(-2);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("18", e.getDataElementIdentifier());
		assertEquals(-2, e.getValue());
		p.setNumberOfStolenMotorVehicles(200);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("18", e.getDataElementIdentifier());
		assertEquals(200, e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule304_recoveredMotorVehicles() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForRecoveredMotorVehicleCount();
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setNumberOfRecoveredMotorVehicles(2);
		e = rule.apply(p);
		assertNull(e);
		p.setNumberOfRecoveredMotorVehicles(-2);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("19", e.getDataElementIdentifier());
		assertEquals(-2, e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		p.setNumberOfRecoveredMotorVehicles(200);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("19", e.getDataElementIdentifier());
		assertEquals(200, e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule304_typeDrugMeasurement() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForListBoundElement("typeDrugMeasurement", "22", PropertyDescriptionCode.codeSet());
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		for (int i=0;i < 3;i++) {
			p.setTypeDrugMeasurement(i, null);
		}
		e = rule.apply(p);
		assertNull(e);
		p.setTypeDrugMeasurement(0, "XXX");
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("22", e.getDataElementIdentifier());
		assertArrayEquals(getStringArrayWithValues(3, "XXX"), (String[]) e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		p.setTypeDrugMeasurement(1, TypeOfDrugMeasurementCode._OZ.code);
		e = rule.apply(p);
		assertNotNull(e);
		assertArrayEquals(getStringArrayWithValues(3, "XXX", TypeOfDrugMeasurementCode._OZ.code), (String[]) e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule304_suspectedDrugType() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForListBoundElement("suspectedDrugType", "20", PropertyDescriptionCode.codeSet());
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		for (int i=0;i < 3;i++) {
			p.setSuspectedDrugType(i, null);
		}
		e = rule.apply(p);
		assertNull(e);
		p.setSuspectedDrugType(0, "XXX");
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("20", e.getDataElementIdentifier());
		assertArrayEquals(getStringArrayWithValues(3, "XXX"), (String[]) e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		p.setSuspectedDrugType(1, SuspectedDrugTypeCode._A.code);
		e = rule.apply(p);
		assertNotNull(e);
		assertArrayEquals(getStringArrayWithValues(3, "XXX", SuspectedDrugTypeCode._A.code), (String[]) e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule304_propertyDescription() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForListBoundElement("propertyDescription", "15", PropertyDescriptionCode.codeSet());
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		for (int i=0;i < 10;i++) {
			p.setPropertyDescription(i, null);
		}
		e = rule.apply(p);
		assertNull(e);
		p.setPropertyDescription(0, "XXX");
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("15", e.getDataElementIdentifier());
		assertArrayEquals(getStringArrayWithValues(10, "XXX"), (String[]) e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		p.setPropertyDescription(1, PropertyDescriptionCode._01.code);
		e = rule.apply(p);
		assertNotNull(e);
		assertArrayEquals(getStringArrayWithValues(10, "XXX", PropertyDescriptionCode._01.code), (String[]) e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
	}
	
	@Test
	public void testRule304_typeOfPropertyLoss() {
		Rule<PropertySegment> rule = rulesFactory.getRule304ForTypePropertyLoss();
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setTypeOfPropertyLoss(null);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("14", e.getDataElementIdentifier());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		assertNull(e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
		p.setTypeOfPropertyLoss("XXX");
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals("XXX", e.getValue());
		assertEquals(NIBRSErrorCode._304, e.getNIBRSErrorCode());
	}
	
	private PropertySegment buildBaseSegment() {
		GroupAIncidentReport report = new GroupAIncidentReport();
		ReportSource source = new ReportSource();
		source.setSourceLocation(getClass().getName());
		source.setSourceName(getClass().getName());
		report.setSource(source);
		PropertySegment p = new PropertySegment();
		p.setTypeOfPropertyLoss(TypeOfPropertyLossCode._1.code);
		report.addProperty(p);
		return p;
	}

}
