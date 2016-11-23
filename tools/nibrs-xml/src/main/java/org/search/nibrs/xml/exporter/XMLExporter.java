package org.search.nibrs.xml.exporter;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBIncidentReport;
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
 * 
 */
public class XMLExporter {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(XMLExporter.class);

	static final NumberFormat MONTH_NUMBER_FORMAT = new DecimalFormat("00");
	static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
		Element root = XmlUtils.appendChildElement(ret, Namespace.nibrs, "Submission");

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
		writer.writeStartElement(Namespace.nibrs.prefix, "Submission", Namespace.nibrs.uri);
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
		} else if (report instanceof GroupBIncidentReport) {
			ret = buildGroupBIncidentReportElement((GroupBIncidentReport) report, errorList);
		} else {
			ret = buildZeroReportElement((ZeroReport) report, errorList);
		}
		return ret;
	}
	
	private Element buildZeroReportElement(AbstractReport incident, List<NIBRSError> errorList) throws ParserConfigurationException {
		Document temp = XmlUtils.createNewDocument();
		Element reportElement = XmlUtils.appendChildElement(temp, Namespace.nibrs, "AbstractReport");
		addReportHeaderElement(incident, reportElement);
		return reportElement;
	}
	
	private Element buildGroupBIncidentReportElement(GroupBIncidentReport incident, List<NIBRSError> errorList) throws ParserConfigurationException {
		Document temp = XmlUtils.createNewDocument();
		Element reportElement = XmlUtils.appendChildElement(temp, Namespace.nibrs, "AbstractReport");
		addReportHeaderElement(incident, reportElement);
		addArresteePersonElements(incident, reportElement, errorList);
		addArresteeElements(incident, reportElement);
		addArrestElement(incident, reportElement);
		addArrestSubjectAssociationElements(incident, reportElement);
		return reportElement;
	}

	private Element buildGroupAIncidentReportElement(GroupAIncidentReport incident, List<NIBRSError> errorList) throws ParserConfigurationException {
		Document temp = XmlUtils.createNewDocument();
		Element reportElement = XmlUtils.appendChildElement(temp, Namespace.nibrs, "AbstractReport");
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
				Integer offenderSequenceNumber = victim.getOffenderNumberRelated(position);
				String relString = victim.getVictimOffenderRelationship(position);
				if (offenderSequenceNumber != null) {
					Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "SubjectVictimAssociation");
					Element e = XmlUtils.appendChildElement(associationElement, Namespace.j, "Subject");
					XmlUtils.addAttribute(e, Namespace.s, "ref", "OffenderSegment-" + offenderSequenceNumber);
					e = XmlUtils.appendChildElement(associationElement, Namespace.j, "VictimSegment");
					XmlUtils.addAttribute(e, Namespace.s, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
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
							XmlUtils.appendChildElement(associationElement, Namespace.j, "VictimToSubjectRelationshipCode").setTextContent(relCode);
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
					Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "OffenseVictimAssociation");
					Element e = XmlUtils.appendChildElement(associationElement, Namespace.j, "OffenseSegment");
					XmlUtils.addAttribute(e, Namespace.s, "ref", "OffenseSegment-" + ucrCode);
					e = XmlUtils.appendChildElement(associationElement, Namespace.j, "VictimSegment");
					XmlUtils.addAttribute(e, Namespace.s, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
				}
			}
		}
	}

	private void addArrestSubjectAssociationElements(AbstractReport incident, Element reportElement) {
		for (ArresteeSegment arrestee : incident.getArrestees()) {
			Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "ArrestSubjectAssociation");
			Element e = XmlUtils.appendChildElement(associationElement, Namespace.nc, "Activity");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Arrest-" + arrestee.getArresteeSequenceNumber());
			e = XmlUtils.appendChildElement(associationElement, Namespace.j, "Subject");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Arrestee-" + arrestee.getArresteeSequenceNumber());
		}
	}

	private void addArrestElement(AbstractReport incident, Element reportElement) {
		for (ArresteeSegment arrestee : incident.getArrestees()) {
			Element arrestElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "Arrest");
			XmlUtils.addAttribute(arrestElement, Namespace.s, "id", "Arrest-" + arrestee.getArresteeSequenceNumber());
			Element e = XmlUtils.appendChildElement(arrestElement, Namespace.nc, "ActivityIdentification");
			appendElementAndValueIfNotNull(e, Namespace.nc, "IdentificationID", arrestee.getArrestTransactionNumber());
			e = XmlUtils.appendChildElement(arrestElement, Namespace.nc, "ActivityDate");
			Date arrestDate = arrestee.getArrestDate();
			if (arrestDate != null) {
				XmlUtils.appendChildElement(e, Namespace.nc, "Date").setTextContent(DATE_FORMAT.format(arrestDate));
			}
			e = XmlUtils.appendChildElement(arrestElement, Namespace.j, "ArrestCharge");
			appendElementAndValueIfNotNull(e, Namespace.nibrs, "ChargeUCRCode", arrestee.getUcrArrestOffenseCode());
			appendElementAndValueIfNotNull(arrestElement, Namespace.j, "ArrestCategoryCode", arrestee.getTypeOfArrest());
			appendElementAndValueIfNotNull(arrestElement, Namespace.j, "ArrestSubjectCountCode", arrestee.getMultipleArresteeSegmentsIndicator());
		}		
	}

	private void appendElementAndValueIfNotNull(Element parentElement, Namespace elementNamespace, String elementName, String value) {
		if (value != null) {
			XmlUtils.appendChildElement(parentElement, elementNamespace, elementName).setTextContent(value);
		}
	}

	private void addArresteeElements(AbstractReport incident, Element reportElement) {
		for (ArresteeSegment arrestee : incident.getArrestees()) {
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "Arrestee");
			XmlUtils.addAttribute(arresteeElement, Namespace.s, "id", "ArresteeObject-" + arrestee.getArresteeSequenceNumber());
			Element e = XmlUtils.appendChildElement(arresteeElement, Namespace.nc, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Arrestee-" + arrestee.getArresteeSequenceNumber());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.j, "ArrestSequenceID", String.valueOf(arrestee.getArresteeSequenceNumber()));
		}
	}

	private void addSubjectElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenderSegment offender : incident.getOffenders()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "Subject");
			Element e = XmlUtils.appendChildElement(offenderElement, Namespace.nc, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "OffenderSegment-" + offender.getOffenderSequenceNumber());
			appendElementAndValueIfNotNull(offenderElement, Namespace.j, "OffenderSequenceNumberText", String.valueOf(offender.getOffenderSequenceNumber()));
		}
	}

	private void addVictimElements(GroupAIncidentReport incident, Element reportElement) {
		for (VictimSegment victim : incident.getVictims()) {
			Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "VictimSegment");
			Element e = XmlUtils.appendChildElement(victimElement, Namespace.nc, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
			appendElementAndValueIfNotNull(victimElement, Namespace.j, "VictimSequenceNumberText", String.valueOf(victim.getVictimSequenceNumber()));
			appendElementAndValueIfNotNull(victimElement, Namespace.j, "VictimCategoryCode", victim.getTypeOfVictim());
			for (int i=0;i < 2;i++) {
				appendElementAndValueIfNotNull(victimElement, Namespace.j, "VictimAggravatedAssaultHomicideFactorCode", victim.getAggravatedAssaultHomicideCircumstances(i));
			}
			appendElementAndValueIfNotNull(victimElement, Namespace.j, "VictimJustifiableHomicideFactorCode", victim.getAdditionalJustifiableHomicideCircumstances());
		}		
	}

	private void addEnforcementOfficialElements(GroupAIncidentReport incident, Element reportElement) {
		for (VictimSegment victim : incident.getVictims()) {
			String victimType = victim.getTypeOfVictim();
			String officerOtherJurisdictionORI = victim.getOfficerOtherJurisdictionORI();
			if ("L".equals(victimType)) {
				Element enforcementOfficialElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "EnforcementOfficial");
				Element e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.nc, "RoleOfPerson");
				XmlUtils.addAttribute(e, Namespace.s, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
				appendElementAndValueIfNotNull(enforcementOfficialElement, Namespace.j, "EnforcementOfficialActivityCategoryCode", victim.getTypeOfOfficerActivityCircumstance());
				appendElementAndValueIfNotNull(enforcementOfficialElement, Namespace.j, "EnforcementOfficialAssignmentCategoryCode", victim.getOfficerAssignmentType());
				if (officerOtherJurisdictionORI != null) {
					e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.j, "EnforcementOfficialUnit");
					e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationAugmentation");
					e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationORIIdentification");
					XmlUtils.appendChildElement(e, Namespace.nc, "IdentificationID").setTextContent(officerOtherJurisdictionORI);
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
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "AbstractPersonSegment");
			XmlUtils.addAttribute(arresteeElement, Namespace.s, "id", "Arrestee-" + arrestee.getArresteeSequenceNumber());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.nc, "PersonEthnicityCode", arrestee.getEthnicity());
			NIBRSAge age = arrestee.getAge();
			if (age.getError() != null) {
				errorList.add(age.getError());
			} else if (age.getAgeMin() != null) {
				Element e = XmlUtils.appendChildElement(arresteeElement, Namespace.nc, "PersonAgeMeasure");
				if (!age.isAgeRange()) {
					XmlUtils.appendChildElement(e, Namespace.nc, "MeasureIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
				} else {
					e = XmlUtils.appendChildElement(e, Namespace.nc, "MeasureRangeValue");
					XmlUtils.appendChildElement(e, Namespace.nc, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.getAgeMax()));
					XmlUtils.appendChildElement(e, Namespace.nc, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
				}
			}
			appendElementAndValueIfNotNull(arresteeElement, Namespace.j, "PersonRaceNDExCode", arrestee.getRace());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.j, "PersonResidentCode", arrestee.getResidentStatus());
			appendElementAndValueIfNotNull(arresteeElement, Namespace.j, "PersonSexCode", arrestee.getSex());
		}
	}

	private void addOffenderPersonElements(GroupAIncidentReport incident, Element reportElement, List<NIBRSError> errorList) {
		for (OffenderSegment offender : incident.getOffenders()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "AbstractPersonSegment");
			XmlUtils.addAttribute(offenderElement, Namespace.s, "id", "OffenderSegment-" + offender.getOffenderSequenceNumber());
			appendElementAndValueIfNotNull(offenderElement, Namespace.nc, "PersonEthnicityCode", offender.getEthnicity());
			NIBRSAge age = offender.getAge();
			if (age != null) {
				if (age.getError() != null) {
					errorList.add(age.getError());
				} else if (age.getAgeMin() != null) {
					Element e = XmlUtils.appendChildElement(offenderElement, Namespace.nc, "PersonAgeMeasure");
					if (!age.isAgeRange()) {
						XmlUtils.appendChildElement(e, Namespace.nc, "MeasureIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					} else {
						e = XmlUtils.appendChildElement(e, Namespace.nc, "MeasureRangeValue");
						XmlUtils.appendChildElement(e, Namespace.nc, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.getAgeMax()));
						XmlUtils.appendChildElement(e, Namespace.nc, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					}
				}
			}
			appendElementAndValueIfNotNull(offenderElement, Namespace.j, "PersonRaceNDExCode", offender.getRace());
			appendElementAndValueIfNotNull(offenderElement, Namespace.j, "PersonSexCode", offender.getSex());
		}
	}

	private void addVictimPersonElements(GroupAIncidentReport incident, Element reportElement, List<NIBRSError> errorList) {
		for (VictimSegment victim : incident.getVictims()) {
			String victimType = victim.getTypeOfVictim();
			if ("L".equals(victimType) || "I".equals(victimType)) {
				Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "AbstractPersonSegment");
				XmlUtils.addAttribute(victimElement, Namespace.s, "id", "VictimSegment-" + victim.getVictimSequenceNumber());
				appendElementAndValueIfNotNull(victimElement, Namespace.nc, "PersonEthnicityCode", victim.getEthnicity());
				NIBRSAge age = victim.getAge();
				if (age.getError() != null) {
					errorList.add(age.getError());
				} else if (age.getAgeMin() != null) {
					Element e = XmlUtils.appendChildElement(victimElement, Namespace.nc, "PersonAgeMeasure");
					if (!age.isAgeRange()) {
						XmlUtils.appendChildElement(e, Namespace.nc, "MeasureIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					} else {
						e = XmlUtils.appendChildElement(e, Namespace.nc, "MeasureRangeValue");
						XmlUtils.appendChildElement(e, Namespace.nc, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.getAgeMax()));
						XmlUtils.appendChildElement(e, Namespace.nc, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.getAgeMin()));
					}
				}
				for (int i = 0; i < 5; i++) {
					String injury = victim.getTypeOfInjury(i);
					if (injury != null) {
						Element injuryElement = XmlUtils.appendChildElement(victimElement, Namespace.nc, "PersonInjury");
						XmlUtils.appendChildElement(injuryElement, Namespace.j, "InjuryCategoryCode").setTextContent(injury);
					}
				}
				appendElementAndValueIfNotNull(victimElement, Namespace.j, "PersonRaceNDExCode", victim.getRace());
				appendElementAndValueIfNotNull(victimElement, Namespace.j, "PersonResidentCode", victim.getResidentStatus());
				appendElementAndValueIfNotNull(victimElement, Namespace.j, "PersonSexCode", victim.getSex());
				String ageCode = age.getNonNumericAge();
				if (ageCode != null) {
					Element e = XmlUtils.appendChildElement(victimElement, Namespace.j, "PersonAugmentation");
					XmlUtils.appendChildElement(e, Namespace.nibrs, "PersonAgeCode").setTextContent(ageCode);
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
						Element substanceElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Substance");
						XmlUtils.appendChildElement(substanceElement, Namespace.j, "DrugCategoryCode").setTextContent(suspectedDrugType);
						Element e = XmlUtils.appendChildElement(substanceElement, Namespace.nc, "SubstanceQuantityMeasure");
						appendElementAndValueIfNotNull(e, Namespace.nc, "MeasureDecimalValue", String.valueOf(property.getEstimatedDrugQuantity(i)));
						appendElementAndValueIfNotNull(e, Namespace.j, "SubstanceUnitCode", String.valueOf(property.getTypeDrugMeasurement(i)));
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
					Element itemElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Item");
					String typeOfPropertyLoss = property.getTypeOfPropertyLoss();
					if (typeOfPropertyLoss != null) {
						String mappedLossType = ITEM_STATUS_MAP.get(typeOfPropertyLoss);
						if (mappedLossType != null) {
							e = XmlUtils.appendChildElement(itemElement, Namespace.nc, "ItemStatus");
							XmlUtils.appendChildElement(e, Namespace.cjis, "ItemStatusCode").setTextContent(mappedLossType);
						} else {
							NIBRSError error = new NIBRSError();
							error.setNIBRSErrorCode(NIBRSErrorCode._404);
							error.setValue(typeOfPropertyLoss);
							errorList.add(error);
						}
					}
					String value = String.valueOf(property.getValueOfProperty(i));
					if (value != null) {
						Element itemValueElement = XmlUtils.appendChildElement(itemElement, Namespace.nc, "ItemValue");
						e = XmlUtils.appendChildElement(itemValueElement, Namespace.nc, "ItemValueAmount");
						XmlUtils.appendChildElement(e, Namespace.nc, "Amount").setTextContent(value);
						Date dateRecovered = property.getDateRecovered(i);
						if (dateRecovered != null) {
							e = XmlUtils.appendChildElement(itemValueElement, Namespace.nc, "ItemValueDate");
							XmlUtils.appendChildElement(e, Namespace.nc, "Date").setTextContent(DATE_FORMAT.format(dateRecovered));
						}
					}
					appendElementAndValueIfNotNull(itemElement, Namespace.j, "ItemCategoryNIBRSPropertyCategoryCode", description);
					Integer rmv = property.getNumberOfRecoveredMotorVehicles();
					Integer smv = property.getNumberOfStolenMotorVehicles();
					if (rmv != null || smv != null) {
						XmlUtils.appendChildElement(itemElement, Namespace.nc, "ItemQuantity").setTextContent(String.valueOf(rmv != null ? rmv : smv));
					}
				}
			}
		}

	}

	private void addOffenseLocationAssociationElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenseSegment offense : incident.getOffenses()) {
			Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "OffenseLocationAssociation");
			Element e = XmlUtils.appendChildElement(associationElement, Namespace.j, "OffenseSegment");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "OffenseSegment-" + offense.getUcrOffenseCode());
			e = XmlUtils.appendChildElement(associationElement, Namespace.nc, "Location");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Location-" + offense.getUcrOffenseCode());
		}
	}

	private void addLocationElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenseSegment offense : incident.getOffenses()) {
			Element locationElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Location");
			XmlUtils.addAttribute(locationElement, Namespace.s, "id", "Location-" + offense.getUcrOffenseCode());
			appendElementAndValueIfNotNull(locationElement, Namespace.j, "LocationCategoryCode", offense.getLocationType());
		}
	}

	private void addOffenseElements(GroupAIncidentReport incident, Element reportElement) {
		for (OffenseSegment offense : incident.getOffenses()) {
			Element offenseElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "OffenseSegment");
			XmlUtils.addAttribute(offenseElement, Namespace.s, "id", "OffenseSegment-" + offense.getUcrOffenseCode());
			appendElementAndValueIfNotNull(offenseElement, Namespace.nibrs, "OffenseUCRCode", offense.getUcrOffenseCode());
			for (int i = 0; i < 3; i++) {
				appendElementAndValueIfNotNull(offenseElement, Namespace.nibrs, "CriminalActivityCategoryCode", offense.getTypeOfCriminalActivity(i));
			}
			for (int i = 0; i < 5; i++) {
				String biasMotivation = offense.getBiasMotivation(i);
				if (biasMotivation != null) {
					String mappedValue = BIAS_MAP.get(biasMotivation);
					if (mappedValue != null) {
						XmlUtils.appendChildElement(offenseElement, Namespace.j, "OffenseFactorBiasMotivationCode").setTextContent(mappedValue);
					} else {
						// todo: handle via error mechanism
					}
				}
			}
			appendElementAndValueIfNotNull(offenseElement, Namespace.j, "OffenseStructuresEnteredQuantity", String.valueOf(offense.getNumberOfPremisesEntered()));
			for (int i = 0; i < 3; i++) {
				String offenderSuspectedOfUsing = offense.getOffendersSuspectedOfUsing(i);
				if (offenderSuspectedOfUsing != null) {
					Element e = XmlUtils.appendChildElement(offenseElement, Namespace.j, "OffenseFactor");
					XmlUtils.appendChildElement(e, Namespace.j, "OffenseFactorCode").setTextContent(offenderSuspectedOfUsing);
				}
			}
			String methodOfEntry = offense.getMethodOfEntry();
			if (methodOfEntry != null) {
				Element e = XmlUtils.appendChildElement(offenseElement, Namespace.j, "OffenseEntryPoint");
				XmlUtils.appendChildElement(e, Namespace.j, "PassagePointMethodCode").setTextContent(methodOfEntry);
			}
			for (int i = 0; i < 3; i++) {
				String typeWeaponForce = offense.getTypeOfWeaponForceInvolved(i);
				if (typeWeaponForce != null) {
					Element e = XmlUtils.appendChildElement(offenseElement, Namespace.j, "OffenseForce");
					XmlUtils.appendChildElement(e, Namespace.j, "ForceCategoryCode").setTextContent(typeWeaponForce);
				}
			}
			appendElementAndValueIfNotNull(offenseElement, Namespace.j, "OffenseAttemptedIndicator", String.valueOf(offense.getOffenseAttemptedIndicator()));
		}
	}

	private void addIncidentElement(GroupAIncidentReport incident, Element reportElement) {
		Element e;
		Element incidentElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Incident");
		String incidentNumber = incident.getIncidentNumber();
		if (incidentNumber != null) {
			e = XmlUtils.appendChildElement(incidentElement, Namespace.nc, "ActivityIdentification");
			XmlUtils.appendChildElement(e, Namespace.nc, "IdentificationID").setTextContent(incidentNumber);
		}
		Date incidentDate = incident.getIncidentDate();
		if (incidentDate != null) {
			e = XmlUtils.appendChildElement(incidentElement, Namespace.nc, "ActivityDate");
			e = XmlUtils.appendChildElement(e, Namespace.nc, "DateTime");
			e.setTextContent(DATETIME_FORMAT.format(incidentDate));
		}
		Element augElement = XmlUtils.appendChildElement(incidentElement, Namespace.cjis, "IncidentAugmentation");
		appendElementAndValueIfNotNull(augElement, Namespace.cjis, "IncidentReportDateIndicator", incident.getReportDateIndicator());
		appendElementAndValueIfNotNull(augElement, Namespace.j, "OffenseCargoTheftIndicator", String.valueOf(incident.getCargoTheftIndicator()));
		augElement = XmlUtils.appendChildElement(incidentElement, Namespace.j, "IncidentAugmentation");
		appendElementAndValueIfNotNull(augElement, Namespace.j, "IncidentExceptionalClearanceCode", incident.getExceptionalClearanceCode());
		Date exceptionalClearanceDate = incident.getExceptionalClearanceDate();
		if (exceptionalClearanceDate != null) {
			e = XmlUtils.appendChildElement(augElement, Namespace.j, "IncidentExceptionalClearanceDate");
			e = XmlUtils.appendChildElement(e, Namespace.nc, "Date");
			e.setTextContent(DATE_FORMAT.format(exceptionalClearanceDate));
		}
	}

	private void addReportHeaderElement(AbstractReport report, Element reportElement) {
		Element reportHeaderElement = XmlUtils.appendChildElement(reportElement, Namespace.nibrs, "ReportHeader");
		Element e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "NIBRSReportCategoryCode");
		String reportType = null;
		if (report instanceof GroupAIncidentReport) {
			reportType = "GROUP A INCIDENT REPORT";
			GroupAIncidentReport ga = (GroupAIncidentReport) report;
			if (ga.includesLeoka()) {
				reportType += "_LEOKA";
			}
		} else if (report instanceof GroupBIncidentReport) {
			reportType = "GROUP B ARREST REPORT";
		} else {
			reportType = "ZERO REPORT";
		}
		e.setTextContent(reportType);
		XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "ReportActionCategoryCode").setTextContent("" + report.getReportActionType());
		e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "ReportDate");
		e = XmlUtils.appendChildElement(e, Namespace.nc, "YearMonthDate");
		e.setTextContent(report.getYearOfTape() + "-" + MONTH_NUMBER_FORMAT.format(report.getMonthOfTape()));
		String ori = report.getOri();
		if (ori != null) {
			e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "ReportingAgency");
			e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationAugmentation");
			e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationORIIdentification");
			XmlUtils.appendChildElement(e, Namespace.nc, "IdentificationID").setTextContent(ori);
		}
	}

}
