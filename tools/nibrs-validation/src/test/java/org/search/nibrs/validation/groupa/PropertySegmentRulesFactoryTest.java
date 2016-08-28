package org.search.nibrs.validation.groupa;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenseCode;
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
	public void testRule354() {
		Rule<PropertySegment> rule = rulesFactory.getRule354();
		PropertySegment p = buildBaseSegment();
		setAllNull(p.getPropertyDescription());
		setAllNull(p.getValueOfProperty());
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 10);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals(10, e.getValue());
		assertEquals(NIBRSErrorCode._354, e.getNIBRSErrorCode());
		assertEquals("15", e.getDataElementIdentifier());
	}
	
	@Test
	public void testRule353() {
		Rule<PropertySegment> rule = rulesFactory.getRule353();
		PropertySegment p = buildBaseSegment();
		setAllNull(p.getPropertyDescription());
		setAllNull(p.getValueOfProperty());
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setPropertyDescription(0, PropertyDescriptionCode._88.code);
		p.setValueOfProperty(0, 1);
		e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 10);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals(10, e.getValue());
		assertEquals(NIBRSErrorCode._353, e.getNIBRSErrorCode());
		assertEquals("16", e.getDataElementIdentifier());
	}
	
	@Test
	public void testRule352() {
		
		PropertySegment p = buildBaseSegment();
		GroupAIncidentReport incident = (GroupAIncidentReport) p.getParentReport();
		OffenseSegment o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode(OffenseCode._09A.code);
		p.setTypeOfPropertyLoss(TypeOfPropertyLossCode._8.code);
		
		setAllNull(p.getPropertyDescription());
		setAllNull(p.getValueOfProperty());
		setAllNull(p.getDateRecovered());
		p.setNumberOfRecoveredMotorVehicles(null);
		p.setNumberOfStolenMotorVehicles(null);
		setAllNull(p.getSuspectedDrugType());
		setAllNull(p.getEstimatedDrugQuantity());
		setAllNull(p.getTypeDrugMeasurement());

		testRule252Common(p, TypeOfPropertyLossCode._8.code);
		testRule352DrugElements(p);
		
		p.setTypeOfPropertyLoss(TypeOfPropertyLossCode._1.code);
		
		setAllNull(p.getPropertyDescription());
		setAllNull(p.getValueOfProperty());
		setAllNull(p.getDateRecovered());
		p.setNumberOfRecoveredMotorVehicles(null);
		p.setNumberOfStolenMotorVehicles(null);
		setAllNull(p.getSuspectedDrugType());
		setAllNull(p.getEstimatedDrugQuantity());
		setAllNull(p.getTypeDrugMeasurement());

		
		testRule252Common(p, TypeOfPropertyLossCode._1.code);
		testRule352DrugElements(p);
		
		setAllNull(p.getPropertyDescription());
		setAllNull(p.getValueOfProperty());
		setAllNull(p.getDateRecovered());
		p.setNumberOfRecoveredMotorVehicles(null);
		p.setNumberOfStolenMotorVehicles(null);
		setAllNull(p.getSuspectedDrugType());
		setAllNull(p.getEstimatedDrugQuantity());
		setAllNull(p.getTypeDrugMeasurement());

		o.setUcrOffenseCode(OffenseCode._35A.code);
		p.setSuspectedDrugType(0, SuspectedDrugTypeCode._A.code);
		testRule252Common(p, TypeOfPropertyLossCode._1.code);
		
		p.setSuspectedDrugType(0, null);
		Rule<PropertySegment> rule = rulesFactory.getRule352();
		NIBRSError e = rule.apply(p);
		assertNotNull(e);
		
	}

	private void testRule352DrugElements(PropertySegment p) {
		
		Rule<PropertySegment> rule = rulesFactory.getRule352();
		
		p.setSuspectedDrugType(0, SuspectedDrugTypeCode._A.code);
		NIBRSError e = rule.apply(p);
		assertNotNull(e);
		
		p.setSuspectedDrugType(0, null);
		p.setEstimatedDrugQuantity(0, 10.0);
		e = rule.apply(p);
		assertNotNull(e);
		
		p.setEstimatedDrugQuantity(0, null);
		p.setTypeDrugMeasurement(0, TypeOfDrugMeasurementCode._DU.code);
		e = rule.apply(p);
		assertNotNull(e);
		
		p.setTypeDrugMeasurement(0, null);
		
	}

	private void testRule252Common(PropertySegment p, String expectedErrorValue) {
		
		Rule<PropertySegment> rule = rulesFactory.getRule352();
		
		NIBRSError e = rule.apply(p);
		assertNull(e);
		
		p.setPropertyDescription(0, PropertyDescriptionCode._01.code);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals(expectedErrorValue, e.getValue());
		assertEquals(NIBRSErrorCode._352, e.getNIBRSErrorCode());
		assertEquals("14", e.getDataElementIdentifier());
		
		p.setPropertyDescription(0, null);
		p.setValueOfProperty(0, 10);
		e = rule.apply(p);
		assertNotNull(e);
		
		p.setValueOfProperty(0, null);
		p.setDateRecovered(0, new Date());
		e = rule.apply(p);
		assertNotNull(e);
		p.setDateRecovered(0, null);
		
	}
	
	private static final void setAllNull(Object[] array) {
		for (int i=0;i < array.length;i++) {
			array[i] = null;
		}
	}
	
	@Test
	public void testRule391() {
		Rule<PropertySegment> rule = rulesFactory.getRule391();
		PropertySegment p = buildBaseSegment();
		for (int i=0;i < 10;i++) {
			p.setValueOfProperty(i, null);
		}
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 10);
		p.setPropertyDescription(0, PropertyDescriptionCode._01.code);
		e = rule.apply(p);
		assertNull(e);
		p.setPropertyDescription(0, PropertyDescriptionCode._09.code);
		p.setValueOfProperty(0, 0);
		e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 10);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("16", e.getDataElementIdentifier());
		assertEquals(10, e.getValue());
		assertEquals(NIBRSErrorCode._391, e.getNIBRSErrorCode());
		p.setPropertyDescription(1, PropertyDescriptionCode._99.code);
		p.setValueOfProperty(1, 0);
		e = rule.apply(p);
		assertNotNull(e);
	}
	
	@Test
	public void testRule351() {
		Rule<PropertySegment> rule = rulesFactory.getRule351();
		PropertySegment p = buildBaseSegment();
		for (int i=0;i < 10;i++) {
			p.setValueOfProperty(i, null);
		}
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 0);
		p.setPropertyDescription(0, PropertyDescriptionCode._09.code);
		e = rule.apply(p);
		assertNull(e);
		p.setPropertyDescription(0, PropertyDescriptionCode._01.code);
		p.setValueOfProperty(0, 10);
		e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 0);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("16", e.getDataElementIdentifier());
		assertEquals(0, e.getValue());
		assertEquals(NIBRSErrorCode._351, e.getNIBRSErrorCode());
		p.setPropertyDescription(1, PropertyDescriptionCode._99.code);
		p.setValueOfProperty(1, 0);
		e = rule.apply(p);
		assertNotNull(e);
	}
	
	@Test
	public void testRule342() {
		Rule<PropertySegment> rule = rulesFactory.getRule342();
		PropertySegment p = buildBaseSegment();
		for (int i=0;i < 10;i++) {
			p.setValueOfProperty(i, null);
		}
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(0, 10);
		e = rule.apply(p);
		assertNull(e);
		p.setValueOfProperty(1, 10000000);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("16", e.getDataElementIdentifier());
		assertEquals(10000000, e.getValue());
		assertEquals(NIBRSErrorCode._342, e.getNIBRSErrorCode());
		assertTrue(e.isWarning());
	}
	
	@Test
	public void testRule306() {
		Rule<PropertySegment> rule = rulesFactory.getRule306();
		PropertySegment p = buildBaseSegment();
		NIBRSError e = rule.apply(p);
		assertNull(e);
		p.setSuspectedDrugType(0, SuspectedDrugTypeCode._A.code);
		p.setTypeDrugMeasurement(0, TypeOfDrugMeasurementCode._DU.code);
		p.setSuspectedDrugType(1, SuspectedDrugTypeCode._B.code);
		p.setTypeDrugMeasurement(1, TypeOfDrugMeasurementCode._DU.code);
		e = rule.apply(p);
		assertNull(e);
		p.setSuspectedDrugType(1, SuspectedDrugTypeCode._A.code);
		e = rule.apply(p);
		assertNotNull(e);
		assertEquals('3', e.getSegmentType());
		assertEquals("20", e.getDataElementIdentifier());
		assertEquals(SuspectedDrugTypeCode._A.code, e.getValue());
		assertEquals(NIBRSErrorCode._306, e.getNIBRSErrorCode());
		p.setTypeDrugMeasurement(1, TypeOfDrugMeasurementCode._FO.code);
		e = rule.apply(p);
		assertNull(e);
		p.setSuspectedDrugType(0, SuspectedDrugTypeCode._U.code);
		e = rule.apply(p);
		assertNotNull(e);
		p.setSuspectedDrugType(0, SuspectedDrugTypeCode._A.code);
		p.setSuspectedDrugType(1, SuspectedDrugTypeCode._U.code);
		e = rule.apply(p);
		assertNotNull(e);
	}
	
	@Test
	public void testRule320() {
		testThatRecoveredDateLaterThanIncidentDate(rulesFactory.getRule320(), NIBRSErrorCode._320);
	}
	
	@Test
	public void testRule305_forIncidentDate() {
		testThatRecoveredDateLaterThanIncidentDate(rulesFactory.getRule305(), NIBRSErrorCode._305);
	}

	private NIBRSError testThatRecoveredDateLaterThanIncidentDate(Rule<PropertySegment> rule, NIBRSErrorCode errorCode) {
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
		assertEquals(errorCode, e.getNIBRSErrorCode());
		c.set(2016, Calendar.JANUARY, 10);
		p.setDateRecovered(1, c.getTime());
		e = rule.apply(p);
		assertNotNull(e);
		return e;
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
		assertEquals(NIBRSErrorCode._305, e.getNIBRSErrorCode());
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
