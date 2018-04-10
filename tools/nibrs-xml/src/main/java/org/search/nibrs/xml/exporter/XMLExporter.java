/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.xml.exporter;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXResult;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.xml.NibrsNamespaceContext;
import org.search.nibrs.xml.NibrsNamespaceContext.Namespace;
import org.search.nibrs.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * 
 * Class supporting export of NIBRS data to XML that conforms to the FBI NIBRS IEPD.
 *  TODO fix the junit tests. 
 */
public class XMLExporter {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(XMLExporter.class);

	static final NumberFormat MONTH_NUMBER_FORMAT = new DecimalFormat("00");
	static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	static final BidiMap<String, String> BIAS_MAP = new DualHashBidiMap<String, String>();
	static final BidiMap<String, String> ITEM_STATUS_MAP = new DualHashBidiMap<String, String>();
	static final BidiMap<String, String> RELATIONSHIP_MAP = new DualHashBidiMap<String, String>();

	static {
		
		RELATIONSHIP_MAP.put("AQ", "Acquaintance");
		RELATIONSHIP_MAP.put("BE", "Babysitter");
		RELATIONSHIP_MAP.put("CF", "Child of Boyfriend_Girlfriend");
		RELATIONSHIP_MAP.put("EE", "Employee");
		RELATIONSHIP_MAP.put("ER", "Employer");
		RELATIONSHIP_MAP.put("XS", "Ex_Spouse");
		RELATIONSHIP_MAP.put("OF", "Family Member");
		RELATIONSHIP_MAP.put("CH", "Family Member_Child");
		RELATIONSHIP_MAP.put("GC", "Family Member_Grandchild");
		RELATIONSHIP_MAP.put("GP", "Family Member_Grandparent");
		RELATIONSHIP_MAP.put("IL", "Family Member_In-Law");
		RELATIONSHIP_MAP.put("PA", "Family Member_Parent");
		RELATIONSHIP_MAP.put("SB", "Family Member_Sibling");
		RELATIONSHIP_MAP.put("SE", "Family Member_Spouse");
		RELATIONSHIP_MAP.put("CS", "Family Member_Spouse_Common Law");
		RELATIONSHIP_MAP.put("SC", "Family Member_Stepchild");
		RELATIONSHIP_MAP.put("SP", "Family Member_Stepparent");
		RELATIONSHIP_MAP.put("SS", "Family Member_Stepsibling");
		RELATIONSHIP_MAP.put("FR", "Friend");
		RELATIONSHIP_MAP.put("HR", "Homosexual relationship");
		RELATIONSHIP_MAP.put("NE", "Neighbor");
		RELATIONSHIP_MAP.put("OK", "NonFamily_Otherwise Known");
		RELATIONSHIP_MAP.put("RU", "Relationship Unknown");
		RELATIONSHIP_MAP.put("ST", "Stranger");
		RELATIONSHIP_MAP.put("VO", "VictimSegment Was OffenderSegment");
		
		BIAS_MAP.put("13", "ANTIAMERICAN INDIAN_ ALASKAN NATIVE");
		BIAS_MAP.put("31", "ANTIARAB");
		BIAS_MAP.put("14", "ANTIASIAN");
		BIAS_MAP.put("27", "ANTIATHEIST_AGNOSTIC");
		BIAS_MAP.put("45", "ANTIBISEXUAL");
		BIAS_MAP.put("12", "ANTIBLACK_AFRICAN AMERICAN");
		BIAS_MAP.put("83", "ANTIBUDDHIST");
		BIAS_MAP.put("22", "ANTICATHOLIC");
		BIAS_MAP.put("81", "ANTIEASTERNORTHODOX");
		BIAS_MAP.put("62", "ANTIFEMALE");
		BIAS_MAP.put("42", "ANTIFEMALE HOMOSEXUAL");
		BIAS_MAP.put("72", "ANTIGENDER_NONCONFORMING");
		BIAS_MAP.put("44", "ANTIHETEROSEXUAL");
		BIAS_MAP.put("84", "ANTIHINDU");
		BIAS_MAP.put("32", "ANTIHISPANIC_LATINO");
		BIAS_MAP.put("43", "ANTIHOMOSEXUAL");
		BIAS_MAP.put("24", "ANTIISLAMIC");
		BIAS_MAP.put("29", "ANTIJEHOVAHWITNESS");
		BIAS_MAP.put("21", "ANTIJEWISH");
		BIAS_MAP.put("61", "ANTIMALE");
		BIAS_MAP.put("41", "ANTIMALE HOMOSEXUAL");
		BIAS_MAP.put("52", "ANTIMENTAL DISABILITY");
		BIAS_MAP.put("28", "ANTIMORMON");
		BIAS_MAP.put("15", "ANTIMULTIRACIAL GROUP");
		BIAS_MAP.put("26", "ANTIMULTIRELIGIOUS GROUP");
		BIAS_MAP.put("16", "ANTINATIVEHAWAIIAN_OTHERPACIFICISLANDER");
		BIAS_MAP.put("82", "ANTIOTHER CHRISTIAN");
		BIAS_MAP.put("13", "ANTIOTHER ETHNICITY_NATIONAL ORIGIN");
		BIAS_MAP.put("25", "ANTIOTHER RELIGION");
		BIAS_MAP.put("51", "ANTIPHYSICAL DISABILITY");
		BIAS_MAP.put("23", "ANTIPROTESTANT");
		BIAS_MAP.put("85", "ANTISIKH");
		BIAS_MAP.put("71", "ANTITRANSGENDER");
		BIAS_MAP.put("11", "ANTIWHITE");
		BIAS_MAP.put("88", "NONE");
		BIAS_MAP.put("99", "UNKNOWN");

		ITEM_STATUS_MAP.put("2", "BURNED");
		ITEM_STATUS_MAP.put("3", "COUNTERFEITED");
		ITEM_STATUS_MAP.put("4", "DESTROYED_DAMAGED_VANDALIZED");
		ITEM_STATUS_MAP.put("5", "RECOVERED");
		ITEM_STATUS_MAP.put("6", "SEIZED");
		ITEM_STATUS_MAP.put("7", "STOLEN");
		ITEM_STATUS_MAP.put("1", "NONE");
		ITEM_STATUS_MAP.put("8", "UNKNOWN");

	}

	public Document convertNIBRSSubmissionToDocument(NIBRSSubmission submission, List<NIBRSError> errorList) throws Exception {

		Document ret = XmlUtils.createNewDocument();
		Element root = XmlUtils.appendChildElement(ret, Namespace.NIBRS, "Submission");

		for (AbstractReport report : submission.getReports()) {
			Element reportElement = buildReportElement(report, errorList);
			root.appendChild(ret.adoptNode(reportElement));
		}

		new NibrsNamespaceContext().populateRootNamespaceDeclarations(ret.getDocumentElement());

		return ret;

	}

	public void convertNIBRSSubmissionToStream(NIBRSSubmission submission, OutputStream os, List<NIBRSError> errorList) throws Exception {

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);

		writer.writeStartDocument();
		writer.writeStartElement(Namespace.NIBRS.prefix, "Submission", Namespace.NIBRS.uri);
		for (Namespace n : Namespace.values()) {
			writer.writeNamespace(n.prefix, n.uri);
		}

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		for (AbstractReport incident : submission.getReports()) {
			Element reportElement = buildReportElement(incident, errorList);
			t.transform(new DOMSource(reportElement), new StAXResult(writer));
		}

		writer.flush();
		writer.close();

	}
	
	private Element buildReportElement(AbstractReport report, List<NIBRSError> errorList) throws ParserConfigurationException {
		Element ret = null;
		if (report instanceof GroupAIncidentReport) {
			ret = buildGroupAIncidentReportElement((GroupAIncidentReport) report, errorList);
		} else if (report instanceof GroupBArrestReport) {
			ret = buildGroupBIncidentReportElement((GroupBArrestReport) report, errorList);
		} else {
			ret = buildZeroReportElement((ZeroReport) report, errorList);
		}
		return ret;
	}
	
	private Element buildZeroReportElement(AbstractReport incident, List<NIBRSError> errorList) throws ParserConfigurationException {
		Document temp = XmlUtils.createNewDocument();
		Element reportElement = XmlUtils.appendChildElement(temp, Namespace.NIBRS, "AbstractReport");
		addReportHeaderElement(incident, reportElement);
		return reportElement;
	}
	
	private Element buildGroupBIncidentReportElement(GroupBArrestReport incident, List<NIBRSError> errorList) throws ParserConfigurationException {
		Document temp = XmlUtils.createNewDocument();
		Element reportElement = XmlUtils.appendChildElement(temp, Namespace.NIBRS, "AbstractReport");
		addReportHeaderElement(incident, reportElement);
		addArresteePersonElements(incident, reportElement, errorList);
		addArresteeElements(incident, reportElement);
		addArrestElement(incident, reportElement);
		addArrestSubjectAssociationElements(incident, reportElement);
		return reportElement;
	}

	private Element buildGroupAIncidentReportElement(GroupAIncidentReport incident, List<NIBRSError> errorList) throws ParserConfigurationException {
		Document temp = XmlUtils.createNewDocument();
		Element reportElement = XmlUtils.appendChildElement(temp, Namespace.NIBRS, "AbstractReport");
		addReportHeaderElement(incident, reportElement);
		addIncidentElement(incident, reportElement);
		addOffenseElements(incident, reportElement);
		addLocationElements(incident, reportElement);
		addNonDrugPropertyElements(incident, reportElement, errorList);
		addDrugPropertyElements(incident, reportElement);
		addPersonElements(incident, reportElement, errorList);
		addEnforcementOfficialElements(incident, reportElement);
		addVictimElements(incident, reportElement);
		addSubjectElements(incident, reportElement);
		addArresteeElements(incident, reportElement);
		addArrestElement(incident, reportElement);
		addArrestSubjectAssociationElements(incident, reportElement);
		addOffenseLocationAssociationElements(incident, reportElement);
		addOffenseVictimAssociationElements(incident, reportElement);
		addSubjectVictimAssociationElements(incident, reportElement);
		return reportElement;
	}

	private void addSubjectVictimAssociationElements(GroupAIncidentReport incident, Element reportElement) {
		for (VictimSegment victim : incident.getVictims()) {
			for (int position=0; position < 10; position++) {
				Integer offenderSequenceNumber = victim.getOffenderNumberRelated(position).getValue();
				String relString = victim.getVictimOffenderRelationship(position);
				if (offenderSequenceNumber != null) {
					Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "SubjectVictimAssociation");
					Element e = XmlUtils.appendChildElement(associationElement, Namespace.J, "Subject");
					XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenderSegment-" + offenderSequenceNumber);
					e = XmlUtils.appendChildElement(associationElement, Namespace.J, "VictimSegment");
					XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
					if (relString != null) {
						String relCode = RELATIONSHIP_MAP.get(relString);
						if (relString.equals("BG")) {
							OffenderSegment o = incident.getOffenderForSequenceNumber(offenderSequenceNumber);
							if ("M".equals(o.getSex())) {
								relCode = "Boyfriend";
							} else if ("F".equals(o.getSex())) {
								relCode = "Girlfriend";
							} else {
								relCode = "Relationship Unknown";
							}
						}
						if (relCode != null) {
							XmlUtils.appendChildElement(associationElement, Namespace.J, "VictimToSubjectRelationshipCode").setTextContent(relCode);
						} else {
							// todo: handle via error mechanism
						}
					}
				}
			}
		}
	}

	private void addOffenseVictimAssociationElements(GroupAIncidentReport incident, Element reportElement) {
		for (VictimSegment victim : incident.getVictims()) {
			for (int position=0; position < 10; position++) {
				String ucrCode = victim.getUcrOffenseCodeConnection(position);
				if (ucrCode != null) {
					Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "OffenseVictimAssociation");
					Element e = XmlUtils.appendChildElement(associationElement, Namespace.J, "OffenseSegment");
					XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenseSegment-" + ucrCode);
					e = XmlUtils.appendChildElement(associationElement, Namespace.J, "VictimSegment");
					XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
				}
			}
		}
	}

	private void addArrestSubjectAssociationElements(AbstractReport incident, Element reportElement) {
		for (ArresteeSegment arrestee : incident.getArrestees()) {
			Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "ArrestSubjectAssociation");
			Element e = XmlUtils.appendChildElement(associationElement, Namespace.NC, "Activity");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "Arrest-" + arrestee.getArresteeSequenceNumber());
			e = XmlUtils.appendChildElement(associationElement, Namespace.J, "Subject");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "Arrestee-" + arrestee.getArresteeSequenceNumber());
		}
	}

	private void addArrestElement(AbstractReport incident, Element reportElement) {
		for (ArresteeSegment arrestee : incident.getArrestees()) {
			Element arrestElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "Arrest");
			XmlUtils.addAttribute(arrestElement, Namespace.S, "id", "Arrest-" + arrestee.getArresteeSequenceNumber());
			Element e = XmlUtils.appendChildElement(arrestElement, Namespace.NC, "ActivityIdentification");
			appendElementAndValueIfNotNull(e, Namespace.NC, "IdentificationID", arrestee.getArrestTransactionNumber());
			e = XmlUtils.appendChildElement(arrestElement, Namespace.NC, "ActivityDate");
			ParsedObject<LocalDate> arrestDate = arrestee.getArrestDate();
			if (!arrestDate.isInvalid() && !arrestDate.isMissing()) {
				XmlUtils.appendChildElement(e, Namespace.NC, "Date").setTextContent(DATE_FORMAT.format(arrestDate.getValue()));
			}
			e = XmlUtils.appendChildElement(arrestElement, Namespace.J, "ArrestCharge");
			appendElementAndValueIfNotNull(e, Namespace.NIBRS, "ChargeUCRCode", arrestee.getUcrArrestOffenseCode());
			appendElementAndValueIfNotNull(arrestElement, Namespace.J, "ArrestCategoryCode", arrestee.getTypeOfArrest());
			appendElementAndValueIfNotNull(arrestElement, Namespace.J, "ArrestSubjectCountCode", arrestee.getMultipleArresteeSegmentsIndicator());
		}		
	}

	private void appendElementAndValueIfNotNull(Element parentElement, Namespace elementNamespace, String elementName, String value) {
		if (value != null) {
			XmlUtils.appendChildElement(parentElement, elementNamespace, elementName).setTextContent(value);
		}
	}

	private void addArresteeElements(AbstractReport incident, Element reportElement) {
		for (ArresteeSegment arrestee : incident.getArrestees()) {
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "Arrestee");
			XmlUtils.addAttribute(arresteeElement, Namespace.S, "id", "ArresteeObject-" + arrestee.getArresteeSequenceNumber());
			Element e = XmlUtils.appendChildElement(arresteeElement, Namespace.NC, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "Arrestee-" + arrestee.getArresteeSequenceNumber());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.J, "ArrestSequenceID", String.valueOf(arrestee.getArresteeSequenceNumber()));
		}
	}

	private void addSubjectElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenderSegment offender : incident.getOffenders()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "Subject");
			Element e = XmlUtils.appendChildElement(offenderElement, Namespace.NC, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenderSegment-" + offender.getOffenderSequenceNumber());
			appendElementAndValueIfNotNull(offenderElement, Namespace.J, "OffenderSequenceNumberText", String.valueOf(offender.getOffenderSequenceNumber()));
		}
	}

	private void addVictimElements(GroupAIncidentReport incident, Element reportElement) {
		for (VictimSegment victim : incident.getVictims()) {
			Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "VictimSegment");
			Element e = XmlUtils.appendChildElement(victimElement, Namespace.NC, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
			appendElementAndValueIfNotNull(victimElement, Namespace.J, "VictimSequenceNumberText", String.valueOf(victim.getVictimSequenceNumber()));
			appendElementAndValueIfNotNull(victimElement, Namespace.J, "VictimCategoryCode", victim.getTypeOfVictim());
			for (int i=0;i < 2;i++) {
				appendElementAndValueIfNotNull(victimElement, Namespace.J, "VictimAggravatedAssaultHomicideFactorCode", victim.getAggravatedAssaultHomicideCircumstances(i));
			}
			appendElementAndValueIfNotNull(victimElement, Namespace.J, "VictimJustifiableHomicideFactorCode", victim.getAdditionalJustifiableHomicideCircumstances());
		}		
	}

	private void addEnforcementOfficialElements(GroupAIncidentReport incident, Element reportElement) {
		for (VictimSegment victim : incident.getVictims()) {
			String victimType = victim.getTypeOfVictim();
			String officerOtherJurisdictionORI = victim.getOfficerOtherJurisdictionORI();
			if ("L".equals(victimType)) {
				Element enforcementOfficialElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "EnforcementOfficial");
				Element e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.NC, "RoleOfPerson");
				XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
				appendElementAndValueIfNotNull(enforcementOfficialElement, Namespace.J, "EnforcementOfficialActivityCategoryCode", victim.getTypeOfOfficerActivityCircumstance());
				appendElementAndValueIfNotNull(enforcementOfficialElement, Namespace.J, "EnforcementOfficialAssignmentCategoryCode", victim.getOfficerAssignmentType());
				if (officerOtherJurisdictionORI != null) {
					e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.J, "EnforcementOfficialUnit");
					e = XmlUtils.appendChildElement(e, Namespace.J, "OrganizationAugmentation");
					e = XmlUtils.appendChildElement(e, Namespace.J, "OrganizationORIIdentification");
					XmlUtils.appendChildElement(e, Namespace.NC, "IdentificationID").setTextContent(officerOtherJurisdictionORI);
				}
			}
		}
	}

	private void addPersonElements(GroupAIncidentReport incident, Element reportElement, List<NIBRSError> errorList) {
		addVictimPersonElements(incident, reportElement, errorList);
		addOffenderPersonElements(incident, reportElement, errorList);
		addArresteePersonElements(incident, reportElement, errorList);
	}

	private void addArresteePersonElements(AbstractReport incident, Element reportElement, List<NIBRSError> errorList) {
		for (ArresteeSegment arrestee : incident.getArrestees()) {
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "AbstractPersonSegment");
			XmlUtils.addAttribute(arresteeElement, Namespace.S, "id", "Arrestee-" + arrestee.getArresteeSequenceNumber());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.NC, "PersonEthnicityCode", arrestee.getEthnicity());
			NIBRSAge age = arrestee.getAge();
			if (age.getError() != null) {
				errorList.add(age.getError());
			} else if (age.getAgeMin() != null) {
				Element e = XmlUtils.appendChildElement(arresteeElement, Namespace.NC, "PersonAgeMeasure");
				if (!age.isAgeRange()) {
					XmlUtils.appendChildElement(e, Namespace.NC, "MeasureIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
				} else {
					e = XmlUtils.appendChildElement(e, Namespace.NC, "MeasureRangeValue");
					XmlUtils.appendChildElement(e, Namespace.NC, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.getAgeMax()));
					XmlUtils.appendChildElement(e, Namespace.NC, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
				}
			}
			appendElementAndValueIfNotNull(arresteeElement, Namespace.J, "PersonRaceNDExCode", arrestee.getRace());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.J, "PersonResidentCode", arrestee.getResidentStatus());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.J, "PersonSexCode", arrestee.getSex());
		}
	}

	private void addOffenderPersonElements(GroupAIncidentReport incident, Element reportElement, List<NIBRSError> errorList) {
		for (OffenderSegment offender : incident.getOffenders()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "AbstractPersonSegment");
			XmlUtils.addAttribute(offenderElement, Namespace.S, "id", "OffenderSegment-" + offender.getOffenderSequenceNumber());
			appendElementAndValueIfNotNull(offenderElement, Namespace.NC, "PersonEthnicityCode", offender.getEthnicity());
			NIBRSAge age = offender.getAge();
			if (age != null) {
				if (age.getError() != null) {
					errorList.add(age.getError());
				} else if (age.getAgeMin() != null) {
					Element e = XmlUtils.appendChildElement(offenderElement, Namespace.NC, "PersonAgeMeasure");
					if (!age.isAgeRange()) {
						XmlUtils.appendChildElement(e, Namespace.NC, "MeasureIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					} else {
						e = XmlUtils.appendChildElement(e, Namespace.NC, "MeasureRangeValue");
						XmlUtils.appendChildElement(e, Namespace.NC, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.getAgeMax()));
						XmlUtils.appendChildElement(e, Namespace.NC, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					}
				}
			}
			appendElementAndValueIfNotNull(offenderElement, Namespace.J, "PersonRaceNDExCode", offender.getRace());
			appendElementAndValueIfNotNull(offenderElement, Namespace.J, "PersonSexCode", offender.getSex());
		}
	}

	private void addVictimPersonElements(GroupAIncidentReport incident, Element reportElement, List<NIBRSError> errorList) {
		for (VictimSegment victim : incident.getVictims()) {
			String victimType = victim.getTypeOfVictim();
			if ("L".equals(victimType) || "I".equals(victimType)) {
				Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "AbstractPersonSegment");
				XmlUtils.addAttribute(victimElement, Namespace.S, "id", "VictimSegment-" + victim.getVictimSequenceNumber());
				appendElementAndValueIfNotNull(victimElement, Namespace.NC, "PersonEthnicityCode", victim.getEthnicity());
				NIBRSAge age = victim.getAge();
				if (age.getError() != null) {
					errorList.add(age.getError());
				} else if (age.getAgeMin() != null) {
					Element e = XmlUtils.appendChildElement(victimElement, Namespace.NC, "PersonAgeMeasure");
					if (!age.isAgeRange()) {
						XmlUtils.appendChildElement(e, Namespace.NC, "MeasureIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					} else {
						e = XmlUtils.appendChildElement(e, Namespace.NC, "MeasureRangeValue");
						XmlUtils.appendChildElement(e, Namespace.NC, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.getAgeMax()));
						XmlUtils.appendChildElement(e, Namespace.NC, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					}
				}
				for (int i = 0; i < 5; i++) {
					String injury = victim.getTypeOfInjury(i);
					if (injury != null) {
						Element injuryElement = XmlUtils.appendChildElement(victimElement, Namespace.NC, "PersonInjury");
						XmlUtils.appendChildElement(injuryElement, Namespace.J, "InjuryCategoryCode").setTextContent(injury);
					}
				}
				appendElementAndValueIfNotNull(victimElement, Namespace.J, "PersonRaceNDExCode", victim.getRace());
				appendElementAndValueIfNotNull(victimElement, Namespace.J, "PersonResidentCode", victim.getResidentStatus());
				appendElementAndValueIfNotNull(victimElement, Namespace.J, "PersonSexCode", victim.getSex());
				String ageCode = age.getNonNumericAge();
				if (ageCode != null) {
					Element e = XmlUtils.appendChildElement(victimElement, Namespace.J, "PersonAugmentation");
					XmlUtils.appendChildElement(e, Namespace.NIBRS, "PersonAgeCode").setTextContent(ageCode);
				}
			}
		}
	}

	private void addDrugPropertyElements(GroupAIncidentReport incident, Element reportElement) {
		for (PropertySegment property : incident.getProperties()) {
			for (int i = 0; i < 10; i++) {
				String description = property.getPropertyDescription(i);
				if ("10".equals(description)) {
					String suspectedDrugType = property.getSuspectedDrugType(i);
					if (suspectedDrugType != null) {
						Element substanceElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Substance");
						XmlUtils.appendChildElement(substanceElement, Namespace.J, "DrugCategoryCode").setTextContent(suspectedDrugType);
						Element e = XmlUtils.appendChildElement(substanceElement, Namespace.NC, "SubstanceQuantityMeasure");
						appendElementAndValueIfNotNull(e, Namespace.NC, "MeasureDecimalValue", String.valueOf(property.getEstimatedDrugQuantity(i)));
						appendElementAndValueIfNotNull(e, Namespace.J, "SubstanceUnitCode", String.valueOf(property.getTypeDrugMeasurement(i)));
					}
				}
			}
		}

	}

	private void addNonDrugPropertyElements(GroupAIncidentReport incident, Element reportElement, List<NIBRSError> errorList) {
		for (PropertySegment property : incident.getProperties()) {
			for (int i = 0; i < 10; i++) {
				String description = property.getPropertyDescription(i);
				if (description != null && !"10".equals(description)) {
					Element e;
					Element itemElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Item");
					String typeOfPropertyLoss = property.getTypeOfPropertyLoss();
					if (typeOfPropertyLoss != null) {
						String mappedLossType = ITEM_STATUS_MAP.get(typeOfPropertyLoss);
						if (mappedLossType != null) {
							e = XmlUtils.appendChildElement(itemElement, Namespace.NC, "ItemStatus");
							XmlUtils.appendChildElement(e, Namespace.CJIS, "ItemStatusCode").setTextContent(mappedLossType);
						} else {
							NIBRSError error = new NIBRSError();
							error.setNIBRSErrorCode(NIBRSErrorCode._404);
							error.setValue(typeOfPropertyLoss);
							errorList.add(error);
						}
					}
					String value = String.valueOf(property.getValueOfProperty(i));
					if (value != null) {
						Element itemValueElement = XmlUtils.appendChildElement(itemElement, Namespace.NC, "ItemValue");
						e = XmlUtils.appendChildElement(itemValueElement, Namespace.NC, "ItemValueAmount");
						XmlUtils.appendChildElement(e, Namespace.NC, "Amount").setTextContent(value);
						LocalDate dateRecovered = property.getDateRecovered(i).getValue();
						if (dateRecovered != null) {
							e = XmlUtils.appendChildElement(itemValueElement, Namespace.NC, "ItemValueDate");
							XmlUtils.appendChildElement(e, Namespace.NC, "Date").setTextContent(DATE_FORMAT.format(dateRecovered));
						}
					}
					appendElementAndValueIfNotNull(itemElement, Namespace.J, "ItemCategoryNIBRSPropertyCategoryCode", description);
					Integer rmv = property.getNumberOfRecoveredMotorVehicles().getValue();
					Integer smv = property.getNumberOfStolenMotorVehicles().getValue();
					if (rmv != null || smv != null) {
						XmlUtils.appendChildElement(itemElement, Namespace.NC, "ItemQuantity").setTextContent(String.valueOf(rmv != null ? rmv : smv));
					}
				}
			}
		}

	}

	private void addOffenseLocationAssociationElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenseSegment offense : incident.getOffenses()) {
			Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "OffenseLocationAssociation");
			Element e = XmlUtils.appendChildElement(associationElement, Namespace.J, "OffenseSegment");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenseSegment-" + offense.getUcrOffenseCode());
			e = XmlUtils.appendChildElement(associationElement, Namespace.NC, "Location");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "Location-" + offense.getUcrOffenseCode());
		}
	}

	private void addLocationElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenseSegment offense : incident.getOffenses()) {
			Element locationElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Location");
			XmlUtils.addAttribute(locationElement, Namespace.S, "id", "Location-" + offense.getUcrOffenseCode());
			appendElementAndValueIfNotNull(locationElement, Namespace.J, "LocationCategoryCode", offense.getLocationType());
		}
	}

	private void addOffenseElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenseSegment offense : incident.getOffenses()) {
			Element offenseElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "OffenseSegment");
			XmlUtils.addAttribute(offenseElement, Namespace.S, "id", "OffenseSegment-" + offense.getUcrOffenseCode());
			appendElementAndValueIfNotNull(offenseElement, Namespace.NIBRS, "OffenseUCRCode", offense.getUcrOffenseCode());
			for (int i = 0; i < 3; i++) {
				appendElementAndValueIfNotNull(offenseElement, Namespace.NIBRS, "CriminalActivityCategoryCode", offense.getTypeOfCriminalActivity(i));
			}
			for (int i = 0; i < 5; i++) {
				String biasMotivation = offense.getBiasMotivation(i);
				if (biasMotivation != null) {
					String mappedValue = BIAS_MAP.get(biasMotivation);
					if (mappedValue != null) {
						XmlUtils.appendChildElement(offenseElement, Namespace.J, "OffenseFactorBiasMotivationCode").setTextContent(mappedValue);
					} else {
						// todo: handle via error mechanism
					}
				}
			}
			appendElementAndValueIfNotNull(offenseElement, Namespace.J, "OffenseStructuresEnteredQuantity", String.valueOf(offense.getNumberOfPremisesEntered()));
			for (int i = 0; i < 3; i++) {
				String offenderSuspectedOfUsing = offense.getOffendersSuspectedOfUsing(i);
				if (offenderSuspectedOfUsing != null) {
					Element e = XmlUtils.appendChildElement(offenseElement, Namespace.J, "OffenseFactor");
					XmlUtils.appendChildElement(e, Namespace.J, "OffenseFactorCode").setTextContent(offenderSuspectedOfUsing);
				}
			}
			String methodOfEntry = offense.getMethodOfEntry();
			if (methodOfEntry != null) {
				Element e = XmlUtils.appendChildElement(offenseElement, Namespace.J, "OffenseEntryPoint");
				XmlUtils.appendChildElement(e, Namespace.J, "PassagePointMethodCode").setTextContent(methodOfEntry);
			}
			for (int i = 0; i < 3; i++) {
				String typeWeaponForce = offense.getTypeOfWeaponForceInvolved(i);
				if (typeWeaponForce != null) {
					Element e = XmlUtils.appendChildElement(offenseElement, Namespace.J, "OffenseForce");
					XmlUtils.appendChildElement(e, Namespace.J, "ForceCategoryCode").setTextContent(typeWeaponForce);
				}
			}
			appendElementAndValueIfNotNull(offenseElement, Namespace.J, "OffenseAttemptedIndicator", String.valueOf(offense.getOffenseAttemptedIndicator()));
		}
	}

	private void addIncidentElement(GroupAIncidentReport incident, Element reportElement) {
		Element e;
		Element incidentElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Incident");
		String incidentNumber = incident.getIncidentNumber();
		if (incidentNumber != null) {
			e = XmlUtils.appendChildElement(incidentElement, Namespace.NC, "ActivityIdentification");
			XmlUtils.appendChildElement(e, Namespace.NC, "IdentificationID").setTextContent(incidentNumber);
		}
		ParsedObject<LocalDate> incidentDatePO = incident.getIncidentDate();
		if (!incidentDatePO.isInvalid() && !incidentDatePO.isMissing()) {
			e = XmlUtils.appendChildElement(incidentElement, Namespace.NC, "ActivityDate");
			e = XmlUtils.appendChildElement(e, Namespace.NC, "DateTime");
			e.setTextContent(DATETIME_FORMAT.format(incidentDatePO.getValue().atStartOfDay().atOffset(ZoneOffset.MIN)));
		}
		Element augElement = XmlUtils.appendChildElement(incidentElement, Namespace.CJIS, "IncidentAugmentation");
		appendElementAndValueIfNotNull(augElement, Namespace.CJIS, "IncidentReportDateIndicator", incident.getReportDateIndicator());
		appendElementAndValueIfNotNull(augElement, Namespace.J, "OffenseCargoTheftIndicator", String.valueOf(incident.getCargoTheftIndicator()));
		augElement = XmlUtils.appendChildElement(incidentElement, Namespace.J, "IncidentAugmentation");
		appendElementAndValueIfNotNull(augElement, Namespace.J, "IncidentExceptionalClearanceCode", incident.getExceptionalClearanceCode());
		ParsedObject<LocalDate> exceptionalClearanceDate = incident.getExceptionalClearanceDate();
		if (!exceptionalClearanceDate.isInvalid() && !exceptionalClearanceDate.isMissing()) {
			e = XmlUtils.appendChildElement(augElement, Namespace.J, "IncidentExceptionalClearanceDate");
			e = XmlUtils.appendChildElement(e, Namespace.NC, "Date");
			e.setTextContent(DATE_FORMAT.format(exceptionalClearanceDate.getValue()));
		}
	}

	private void addReportHeaderElement(AbstractReport report, Element reportElement) {
		Element reportHeaderElement = XmlUtils.appendChildElement(reportElement, Namespace.NIBRS, "ReportHeader");
		Element e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "NIBRSReportCategoryCode");
		String reportType = null;
		if (report instanceof GroupAIncidentReport) {
			reportType = "GROUP A INCIDENT REPORT";
			GroupAIncidentReport ga = (GroupAIncidentReport) report;
			if (ga.includesLeoka()) {
				reportType += "_LEOKA";
			}
		} else if (report instanceof GroupBArrestReport) {
			reportType = "GROUP B ARREST REPORT";
		} else {
			reportType = "ZERO REPORT";
		}
		e.setTextContent(reportType);
		XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "ReportActionCategoryCode").setTextContent("" + report.getReportActionType());
		e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "ReportDate");
		e = XmlUtils.appendChildElement(e, Namespace.NC, "YearMonthDate");
		e.setTextContent(report.getYearOfTape() + "-" + MONTH_NUMBER_FORMAT.format(report.getMonthOfTape()));
		String ori = report.getOri();
		if (ori != null) {
			e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "ReportingAgency");
			e = XmlUtils.appendChildElement(e, Namespace.J, "OrganizationAugmentation");
			e = XmlUtils.appendChildElement(e, Namespace.J, "OrganizationORIIdentification");
			XmlUtils.appendChildElement(e, Namespace.NC, "IdentificationID").setTextContent(ori);
		}
	}

}
