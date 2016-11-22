package org.search.nibrs.xml.exporter;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.AbstractPersonSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestXMLExporter {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TestXMLExporter.class);
	
	@Test
	public void testDocumentConversion() throws Exception {
		
		NIBRSSubmission report = new NIBRSSubmission();
		report.addReport(buildBaseGroupAIncident());
		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		Document d = new XMLExporter().convertNIBRSSubmissionToDocument(report, errorList);
		assertTrue(errorList.isEmpty());
		assertNotNull(d);
		//XmlUtils.printNode(d, System.out);
	}
	
	@Test
	public void testStreamConversion() throws Exception {
		NIBRSSubmission report = new NIBRSSubmission();
		report.addReport(buildBaseGroupAIncident());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		new XMLExporter().convertNIBRSSubmissionToStream(report, baos, errorList);
		assertTrue(errorList.isEmpty());
		String xml = baos.toString();
		Document d = XmlUtils.toDocument(xml);
		assertNotNull(d);
		//XmlUtils.printNode(d);
	}
	
	@Test
	public void testGroupBExport() throws Exception {
		NIBRSSubmission report = new NIBRSSubmission();
		GroupBIncidentReport baseIncident = buildBaseGroupBIncident();
		report.addReport(baseIncident);
		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		Document d = new XMLExporter().convertNIBRSSubmissionToDocument(report, errorList);
		//XmlUtils.printNode(d);
		assertEquals(baseIncident.getYearOfTape() + "-" + new DecimalFormat("00").format(baseIncident.getMonthOfTape()),
				XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportDate/nc:YearMonthDate"));
		assertEquals(baseIncident.getOri(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID"));
		assertEquals(baseIncident.getReportActionType(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportActionCategoryCode").charAt(0));
		assertEquals("GROUP B ARREST REPORT", XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode"));

		assertArrestees(d, baseIncident, 1);
	}
	
	private void assertArrestees(Document d, AbstractReport baseIncident, int expectedArresteeCount) throws Exception {
		List<ArresteeSegment> arresteeList = baseIncident.getArrestees();
		assertEquals(expectedArresteeCount, arresteeList.size());
		NodeList arresteeNodes = XmlUtils.xPathNodeListSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:Arrestee");
		assertEquals(expectedArresteeCount, arresteeNodes.getLength());
		for (int i=0; i < arresteeNodes.getLength();i++) {
			Node arresteeNode = arresteeNodes.item(i);
			ArresteeSegment arrestee = arresteeList.get(i);
			assertEquals(arrestee.getArresteeSequenceNumber(), Integer.valueOf(XmlUtils.xPathStringSearch(arresteeNode, "j:ArrestSequenceID")));
			String personId = XmlUtils.xPathStringSearch(arresteeNode, "nc:RoleOfPerson/@s:ref");
			Node personNode = XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:AbstractPersonSegment[@s:id='" + personId + "']");
			assertEquals(arrestee.getSex(), XmlUtils.xPathStringSearch(personNode, "j:PersonSexCode"));
			assertEquals(arrestee.getRace(), XmlUtils.xPathStringSearch(personNode, "j:PersonRaceNDExCode"));
			assertPersonAge(arrestee.getAge(), personNode);
			Node assnNode = XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:ArrestSubjectAssociation[j:Subject/@s:ref='" + personId + "']");
			assertNotNull(assnNode);
			String arrestId = XmlUtils.xPathStringSearch(assnNode, "nc:Activity/@s:ref");
			Node arrestNode = XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:Arrest[@s:id='" + arrestId + "']");
			assertEquals(arrestee.getArrestTransactionNumber(), XmlUtils.xPathStringSearch(arrestNode, "nc:ActivityIdentification/nc:IdentificationID"));
			assertEquals(arrestee.getUcrArrestOffenseCode(), XmlUtils.xPathStringSearch(arrestNode, "j:ArrestCharge/nibrs:ChargeUCRCode"));
			assertEquals(arrestee.getTypeOfArrest(), XmlUtils.xPathStringSearch(arrestNode, "j:ArrestCategoryCode"));
		}
	}

	@Test
	public void testZeroReportExport() throws Exception {
		NIBRSSubmission report = new NIBRSSubmission();
		ZeroReport baseIncident = buildZeroReport();
		report.addReport(baseIncident);
		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		Document d = new XMLExporter().convertNIBRSSubmissionToDocument(report, errorList);
		//XmlUtils.printNode(d);
		assertEquals(baseIncident.getYearOfTape() + "-" + new DecimalFormat("00").format(baseIncident.getMonthOfTape()),
				XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportDate/nc:YearMonthDate"));
		assertEquals(baseIncident.getOri(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID"));
		assertEquals(baseIncident.getReportActionType(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportActionCategoryCode").charAt(0));
		assertEquals("ZERO REPORT", XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode"));

	}
	
	@Test
	public void testGroupAExport() throws Exception {
		
		NIBRSSubmission report = new NIBRSSubmission();
		GroupAIncidentReport baseIncident = buildBaseGroupAIncident();
		report.addReport(baseIncident);
		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		Document d = new XMLExporter().convertNIBRSSubmissionToDocument(report, errorList);
		
		assertEquals(baseIncident.getYearOfTape() + "-" + new DecimalFormat("00").format(baseIncident.getMonthOfTape()),
				XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportDate/nc:YearMonthDate"));
		assertEquals(baseIncident.getOri(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID"));
		assertEquals(baseIncident.getReportActionType(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:ReportActionCategoryCode").charAt(0));
		assertEquals("GROUP A INCIDENT REPORT", XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode"));
		assertEquals(baseIncident.getIncidentNumber(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:Incident/nc:ActivityIdentification/nc:IdentificationID"));
		assertEquals(baseIncident.getExceptionalClearanceCode(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:Incident/j:IncidentAugmentation/j:IncidentExceptionalClearanceCode"));
		assertNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:Incident/cjis:IncidentAugmentation/cjis:IncidentReportDateIndicator"));
		assertEquals(baseIncident.getCargoTheftIndicator(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:Incident/cjis:IncidentAugmentation/j:OffenseCargoTheftIndicator"));
		
		for (OffenseSegment o : baseIncident.getOffenses()) {
			String ucrCode = o.getUcrOffenseCode();
			Element offenseElement = (Element) XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:OffenseSegment[@s:id='OffenseSegment-" + ucrCode + "']");
			assertNotNull(offenseElement);
			assertEquals(ucrCode, XmlUtils.xPathStringSearch(offenseElement, "nibrs:OffenseUCRCode"));
			for (int i=0; i < o.getPopulatedBiasMotivationCount(); i++) {
				assertNotNull(XmlUtils.xPathNodeSearch(offenseElement, "j:OffenseFactorBiasMotivationCode[text()='" + XMLExporter.BIAS_MAP.get(o.getBiasMotivation(i)) + "']"));
			}
			for (int i=0; i < o.getPopulatedOffendersSuspectedOfUsingCount(); i++) {
				assertNotNull(XmlUtils.xPathNodeSearch(offenseElement, "j:OffenseFactor/j:OffenseFactorCode[text()='" + o.getOffendersSuspectedOfUsing(i) + "']"));
			}
			for (int i=0; i < o.getPopulatedTypeOfWeaponForceInvolvedCount(); i++) {
				assertNotNull(XmlUtils.xPathNodeSearch(offenseElement, "j:OffenseForce/j:ForceCategoryCode[text()='" + o.getTypeOfWeaponForceInvolved(i) + "']"));
			}
			assertEquals(o.getLocationType(), XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport/nc:Location[@s:id='Location-" + ucrCode + "']/j:LocationCategoryCode"));
			assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport/j:OffenseLocationAssociation[nc:Location/@s:ref='Location-" + ucrCode + "']"));
			assertEquals(o.getOffenseAttemptedIndicator(), new Boolean(XmlUtils.xPathStringSearch(offenseElement, "j:OffenseAttemptedIndicator")));
		}
		
		for (PropertySegment p : baseIncident.getProperties()) {
			String typePropertyLoss = p.getTypeOfPropertyLoss();
			for (int i = 0; i < p.getPopulatedPropertyDescriptionCount(); i++) {
				String propertyDescription = p.getPropertyDescription(i);
				if (!"10".equals(propertyDescription)) {
					Element itemElement = (Element) XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:Item[j:ItemCategoryNIBRSPropertyCategoryCode='" + propertyDescription +
							"' and nc:ItemStatus/cjis:ItemStatusCode='" + XMLExporter.ITEM_STATUS_MAP.get(typePropertyLoss) + "']");
					assertNotNull(itemElement);
					assertEquals(p.getValueOfProperty(i), new Integer(XmlUtils.xPathStringSearch(itemElement, "nc:ItemValue/nc:ItemValueAmount/nc:Amount")));
					Integer recoveredMotorVehicles = p.getNumberOfRecoveredMotorVehicles();
					Integer stolenMotorVehicles = p.getNumberOfStolenMotorVehicles();
					if (recoveredMotorVehicles != null || stolenMotorVehicles != null) {
						assertNotNull(XmlUtils.xPathNodeSearch(itemElement, "nc:ItemQuantity[text()='" +
								(recoveredMotorVehicles != null ? recoveredMotorVehicles : stolenMotorVehicles) + "']"));
					} else {
						assertNull(XmlUtils.xPathNodeSearch(itemElement, "nc:ItemQuantity"));
					}
				}
			}
			for (int i=0; i < p.getPopulatedSuspectedDrugTypeCount(); i++) {
				String drugType = p.getSuspectedDrugType(i);
				Double drugQuantity = p.getEstimatedDrugQuantity(i);
				String measurement = p.getTypeDrugMeasurement(i);
				assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:Substance[" +
						"j:DrugCategoryCode='" + drugType + "' and " +
						"nc:SubstanceQuantityMeasure/nc:MeasureDecimalValue='" + drugQuantity + "' and " +
						"nc:SubstanceQuantityMeasure/j:SubstanceUnitCode='" + measurement + "']"));
			}
		}
		
		for (OffenderSegment o : baseIncident.getOffenders()) {
			Integer offenderSequenceNumber = o.getOffenderSequenceNumber();
			Element offenderPersonElement = (Element) XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:AbstractPersonSegment[@s:id='OffenderSegment-" + offenderSequenceNumber + "']");
			assertNotNull(offenderPersonElement);
			assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:Subject[nc:RoleOfPerson/@s:ref='OffenderSegment-" + offenderSequenceNumber + "' and " +
					"j:OffenderSequenceNumberText/text()='" + offenderSequenceNumber + "']"));
			if (o.getReportedUnknown()) {
				assertEquals(0, offenderPersonElement.getChildNodes().getLength());
			} else {
				assertDemographics(o, offenderPersonElement);
			}
		}
		
		for (VictimSegment v : baseIncident.getVictims()) {
			Integer victimSequenceNumber = v.getVictimSequenceNumber();
			assertEquals(XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:VictimSegment[nc:RoleOfPerson/@s:ref='VictimSegment-" + victimSequenceNumber + "']/j:VictimCategoryCode"),
					v.getTypeOfVictim());
			if (v.isPerson()) {
				Element victimPersonElement = (Element) XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:AbstractPersonSegment[@s:id='VictimSegment-" + victimSequenceNumber + "']");
				assertNotNull(victimPersonElement);
				assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:VictimSegment[nc:RoleOfPerson/@s:ref='VictimSegment-" + victimSequenceNumber + "' and "
						+ "j:VictimSequenceNumberText/text()='" + victimSequenceNumber + "']"));
				assertDemographics(v, victimPersonElement);
				for (int i=0; i < v.getPopulatedAggravatedAssaultHomicideCircumstancesCount(); i++) {
					String circ = v.getAggravatedAssaultHomicideCircumstances(i);
					assertEquals(circ, XmlUtils.xPathStringSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:VictimSegment[j:VictimSequenceNumberText='" + victimSequenceNumber + "']/j:VictimAggravatedAssaultHomicideFactorCode"));
				}
				for (int i=0; i < v.getPopulatedTypeOfInjuryCount(); i++) {
					String injury = v.getTypeOfInjury(i);
					assertEquals(injury, XmlUtils.xPathStringSearch(victimPersonElement, "nc:PersonInjury/j:InjuryCategoryCode"));
				}
				if (v.isLawEnforcementOfficer() && baseIncident.includesLeoka()) {
					Element enforcementOfficialElement = (Element) XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:EnforcementOfficial/nc:RoleOfPerson[@s:ref='VictimSegment-" + 
							victimSequenceNumber + "']");
					assertNotNull(enforcementOfficialElement);
					assertEquals(v.getOfficerAssignmentType(), XmlUtils.xPathStringSearch(enforcementOfficialElement, "j:EnforcementOfficialAssignmentCategoryCode"));
					assertEquals(v.getTypeOfOfficerActivityCircumstance(), XmlUtils.xPathStringSearch(enforcementOfficialElement, "j:EnforcementOfficialActivityCategoryCode"));
					assertEquals(v.getOfficerOtherJurisdictionORI(), XmlUtils.xPathStringSearch(enforcementOfficialElement,
							"j:EnforcementOfficialUnit/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID"));
				}
			} else {
				assertNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/nc:AbstractPersonSegment[@s:id='VictimSegment-" + victimSequenceNumber + "']"));
			}
			for (int i=0; i < v.getPopulatedUcrOffenseCodeConnectionCount(); i++) {
				String offenseCode = v.getUcrOffenseCodeConnection(i);
				assertNotNull(XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:OffenseVictimAssociation[j:OffenseSegment/@s:ref='OffenseSegment-" + offenseCode + 
						"' and j:VictimSegment/@s:ref='VictimSegment-" + victimSequenceNumber + "']"));
			}
			for (int i=0; i < v.getPopulatedOffenderNumberRelatedCount(); i++) {
				Integer offender = v.getOffenderNumberRelated(i);
				Node assnNode = XmlUtils.xPathNodeSearch(d, "/nibrs:Submission/nibrs:AbstractReport[1]/j:SubjectVictimAssociation[j:Subject/@s:ref='OffenderSegment-" + offender +
						"' and j:VictimSegment/@s:ref='VictimSegment-" + victimSequenceNumber + "']");
				assertNotNull(assnNode);
				assertEquals(XMLExporter.RELATIONSHIP_MAP.get(v.getVictimOffenderRelationship(i)), XmlUtils.xPathStringSearch(assnNode, "j:VictimToSubjectRelationshipCode"));
			}
		}
		
		assertArrestees(d, baseIncident, baseIncident.getArresteeCount());
		
	}

	private void assertDemographics(AbstractPersonSegment person, Element personElement) throws Exception {
		String ethnicity = person.getEthnicity();
		if (ethnicity == null) {
			assertNull(XmlUtils.xPathNodeSearch(personElement, "nc:PersonEthnicityCode"));
		} else {
			assertEquals(ethnicity, XmlUtils.xPathStringSearch(personElement, "nc:PersonEthnicityCode"));
		}
		assertEquals(person.getRace(), XmlUtils.xPathStringSearch(personElement, "j:PersonRaceNDExCode"));
		assertEquals(person.getSex(), XmlUtils.xPathStringSearch(personElement, "j:PersonSexCode"));
		assertPersonAge(person.getAge(), personElement);
	}

	private void assertPersonAge(NIBRSAge age, Node personElement) throws Exception {
		if (age.isAgeRange()) {
			assertEquals(age.getAgeMin(), new Integer(XmlUtils.xPathStringSearch(personElement, "nc:PersonAgeMeasure/nc:MeasureRangeValue/nc:RangeMinimumIntegerValue")));
			assertEquals(age.getAgeMax(), new Integer(XmlUtils.xPathStringSearch(personElement, "nc:PersonAgeMeasure/nc:MeasureRangeValue/nc:RangeMaximumIntegerValue")));
		} else if (age.isNonNumeric()) {
			assertEquals(age.getNonNumericAge(), XmlUtils.xPathStringSearch(personElement, "j:PersonAugmentation/nibrs:PersonAgeCode"));
		} else {
			assertEquals(age.getAgeMin(), new Integer(XmlUtils.xPathStringSearch(personElement, "nc:PersonAgeMeasure/nc:MeasureIntegerValue")));
		}
	}
	
	private ZeroReport buildZeroReport() {
		ZeroReport incident = new ZeroReport();
		incident.setYearOfTape(2016);
		incident.setMonthOfTape(5);
		incident.setOri("WA1234567");
		incident.setReportActionType('I');
		return incident;
	}

	private GroupBIncidentReport buildBaseGroupBIncident() throws ParseException {
		
		GroupBIncidentReport incident = new GroupBIncidentReport();
		
		incident.setYearOfTape(2016);
		incident.setMonthOfTape(5);
		incident.setOri("WA123456789");
		
		incident.setReportActionType('I');
		
		ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		incident.addArrestee(arrestee);
		arrestee.setArresteeSequenceNumber(1);
		arrestee.setAgeString("22");
		arrestee.setRace("W");
		arrestee.setSex("M");
		arrestee.setArrestTransactionNumber("12345");
		arrestee.setArrestDate(XMLExporter.DATE_FORMAT.parse("2016-05-16"));
		arrestee.setUcrArrestOffenseCode("90A");
		arrestee.setTypeOfArrest("O");
		
		return incident;
		
	}

	private GroupAIncidentReport buildBaseGroupAIncident() throws ParseException {
		
		GroupAIncidentReport incident = new GroupAIncidentReport();
		incident.setYearOfTape(2016);
		incident.setMonthOfTape(5);
		incident.setOri("WA123456789");
		incident.setIncidentNumber("54236732");
		incident.setIncidentDate(XMLExporter.DATETIME_FORMAT.parse("2016-05-12T10:07:46.342-0500"));
		incident.setExceptionalClearanceCode("N");
		incident.setReportActionType('I');
		incident.setCargoTheftIndicator("N");
		
		OffenseSegment o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode("64A");
		o.setBiasMotivation(0, "88");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setTypeOfWeaponForceInvolved(0, "11A");
		o.setOffenseAttemptedCompleted("C");
		o.setLocationType("01");
		
		o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode("220");
		o.setNumberOfPremisesEntered(1);
		o.setMethodOfEntry("F");
		o.setBiasMotivation(0, "21");
		o.setBiasMotivation(1, "22");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setOffenseAttemptedCompleted("A");
		o.setLocationType("14");
		
		o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode("720");
		o.setBiasMotivation(0, "88");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setOffenseAttemptedCompleted("C");
		o.setTypeOfCriminalActivity(0, "A");
		o.setLocationType("20");
		
		o = new OffenseSegment();
		incident.addOffense(o);
		o.setUcrOffenseCode("13A");
		o.setOffenseAttemptedCompleted("C");
		o.setLocationType("15");
		
		PropertySegment p = new PropertySegment();
		incident.addProperty(p);
		p.setTypeOfPropertyLoss("6");
		// seized 24 oz of LSD
		p.setPropertyDescription(0, "10");
		p.setEstimatedDrugQuantity(0, 24.0);
		p.setTypeDrugMeasurement(0, "OZ");
		p.setSuspectedDrugType(0, "I");
		p.setValueOfProperty(0, 500);
		// also seized some currency
		p.setPropertyDescription(1, "20");
		p.setValueOfProperty(1, 2000);
		
		p = new PropertySegment();
		incident.addProperty(p);
		// stole jewelry
		p.setTypeOfPropertyLoss("7");
		p.setPropertyDescription(0, "17");
		p.setValueOfProperty(0, 1000);
		
		p = new PropertySegment();
		incident.addProperty(p);
		// recovered some jewelry
		p.setTypeOfPropertyLoss("5");
		p.setPropertyDescription(0, "17");
		p.setValueOfProperty(0, 200);
		p.setDateRecovered(0, XMLExporter.DATE_FORMAT.parse("2016-05-16"));
		
		p = new PropertySegment();
		incident.addProperty(p);
		// stole car
		p.setTypeOfPropertyLoss("7");
		p.setPropertyDescription(0, "03");
		p.setValueOfProperty(0, 5000);
		p.setNumberOfStolenMotorVehicles(1);
		
		VictimSegment v = new VictimSegment();
		incident.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setVictimSequenceNumber(1);
		v.setAgeString("2022");
		v.setEthnicity("N");
		v.setResidentStatusOfVictim("R");
		v.setSex("F");
		v.setRace("B");
		v.setUcrOffenseCodeConnection(0, "64A");
		v.setUcrOffenseCodeConnection(1, "13A");
		v.setOffenderNumberRelated(0, 1);
		v.setVictimOffenderRelationship(0, "ER");
		
		v = new VictimSegment();
		incident.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setVictimSequenceNumber(2);
		v.setAgeString("BB");
		v.setEthnicity("N");
		v.setResidentStatusOfVictim("R");
		v.setSex("F");
		v.setRace("B");
		v.setUcrOffenseCodeConnection(0, "13A");
		v.setOffenderNumberRelated(0, 1);
		v.setVictimOffenderRelationship(0, "FR");
		v.setAggravatedAssaultHomicideCircumstances(0, "32");
		
		v = new VictimSegment();
		incident.addVictim(v);
		v.setVictimSequenceNumber(3);
		v.setTypeOfVictim("B");
		v.setUcrOffenseCodeConnection(0, "220");
		v.setOffenderNumberRelated(0, 1);
		v.setVictimOffenderRelationship(0, "RU");
		
		v = new VictimSegment();
		incident.addVictim(v);
		v.setVictimSequenceNumber(4);
		v.setTypeOfVictim("L");
		v.setTypeOfInjury(0, "L");
		v.setAgeString("30");
		v.setEthnicity("N");
		v.setResidentStatusOfVictim("R");
		v.setSex("F");
		v.setRace("B");
		v.setOfficerAssignmentType("F");
		v.setTypeOfOfficerActivityCircumstance("04");
		v.setOfficerOtherJurisdictionORI("WA987654321");
		v.setUcrOffenseCodeConnection(0, "13A");
		v.setOffenderNumberRelated(0, 1);
		v.setVictimOffenderRelationship(0, "ST");
		
		OffenderSegment offender = new OffenderSegment();
		incident.addOffender(offender);
		offender.setOffenderSequenceNumber(1);
		offender.setAgeString("22");
		offender.setRace("W");
		offender.setSex("M");
		
		ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		incident.addArrestee(arrestee);
		arrestee.setArresteeSequenceNumber(1);
		arrestee.setAgeString("22");
		arrestee.setRace("W");
		arrestee.setSex("M");
		arrestee.setArrestTransactionNumber("12345");
		arrestee.setArrestDate(XMLExporter.DATE_FORMAT.parse("2016-05-16"));
		arrestee.setUcrArrestOffenseCode("64A");
		arrestee.setTypeOfArrest("O");
		arrestee.setMultipleArresteeSegmentsIndicator("N");
		
		offender = new OffenderSegment();
		incident.addOffender(offender);
		offender.setOffenderSequenceNumber(2);
		offender.setAgeString("32");
		offender.setRace("B");
		offender.setSex("F");
		offender.setEthnicity("N");
		
		offender = new OffenderSegment();
		incident.addOffender(offender);
		offender.setOffenderSequenceNumber(0);
		
		return incident;
		
	}
	
}
