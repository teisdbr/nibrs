package org.search.nibrs.ff_conversion.exporter;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.search.nibrs.model.Arrestee;
import org.search.nibrs.model.Incident;
import org.search.nibrs.model.NIBRSSubmission;
import org.search.nibrs.model.Offender;
import org.search.nibrs.model.Offense;
import org.search.nibrs.model.Property;
import org.search.nibrs.model.Victim;
import org.search.nibrs.xml.NibrsNamespaceContext;
import org.search.nibrs.xml.NibrsNamespaceContext.Namespace;
import org.search.nibrs.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * 
 * TODO:
 * 
 * handle actions other than add ("I")
 * handle Group B
 * log4j info
 * error handling
 * finish streaming of output
 * 
 * IEPD issues for FBI:
 * --inclusion of RoleOfOrganization when no Organization possible
 * --in cjis.xml, SubjectAugmenatation misspelling
 * --would be cleaner to put the NIBRS PersonAgeCode element in the nc:PersonAgeMeasure substitution group, rather than j:PersonAgeCode.  That way,
 *    PersonAge is always represented in the same place in the Person structure.  As it is, a developer has to look in two places for Victim age.
 * 
 */

public class ReportConverter {

	static final NumberFormat MONTH_NUMBER_FORMAT = new DecimalFormat("00");
	static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	static final BidiMap<String, String> BIAS_MAP = new DualHashBidiMap<String, String>();
	static final BidiMap<String, String> ITEM_STATUS_MAP = new DualHashBidiMap<String, String>();

	static {
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

	public Document convertNIBRSSubmissionToDocument(NIBRSSubmission submission) throws Exception {

		Document ret = XmlUtils.createNewDocument();
		Element root = XmlUtils.appendChildElement(ret, Namespace.nibrs, "Submission");

		for (Incident incident : submission.getIncidents()) {
			Element reportElement = buildReportElement(incident);
			root.appendChild(ret.adoptNode(reportElement));
		}

		new NibrsNamespaceContext().populateRootNamespaceDeclarations(ret.getDocumentElement());

		return ret;

	}

	public void convertNIBRSSubmissionToStream(NIBRSSubmission submission, OutputStream os) throws Exception {

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);

		writer.writeStartDocument();
		writer.writeStartElement(Namespace.nibrs.prefix, "Submission", Namespace.nibrs.uri);
		writer.writeNamespace(Namespace.nibrs.prefix, Namespace.nibrs.uri);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		for (Incident incident : submission.getIncidents()) {
			Element reportElement = buildReportElement(incident);
			// t.transform(new DOMSource(reportElement), new
			// StAXResult(writer));
		}

		writer.writeEndElement();
		writer.flush();
		writer.close();

	}

	private Element buildReportElement(Incident incident) throws ParserConfigurationException {
		Document temp = XmlUtils.createNewDocument();
		Element reportElement = XmlUtils.appendChildElement(temp, Namespace.nibrs, "Report");
		addReportHeaderElement(incident, reportElement);
		addIncidentElement(incident, reportElement);
		addOffenseElements(incident, reportElement);
		addLocationElements(incident, reportElement);
		addNonDrugPropertyElements(incident, reportElement);
		addDrugPropertyElements(incident, reportElement);

		addPersonElements(incident, reportElement);
		addEnforcementOfficialElements(incident, reportElement);
		
		addVictimElements(incident, reportElement);
		
		addSubjectElements(incident, reportElement);
		
		addArresteeElements(incident, reportElement);

		addOffenseLocationAssociationElements(incident, reportElement);
		return reportElement;
	}

	private void addArresteeElements(Incident incident, Element reportElement) {
		for (Arrestee arrestee : incident.getArrestees()) {
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "Arrestee");
			XmlUtils.addAttribute(arresteeElement, Namespace.s, "id", "ArresteeObject-" + arrestee.getArresteeSequenceNumber());
			Element e = XmlUtils.appendChildElement(arresteeElement, Namespace.nc, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Arrestee-" + arrestee.getArresteeSequenceNumber());
			XmlUtils.appendChildElement(arresteeElement, Namespace.j, "ArrestSequenceID").setTextContent(String.valueOf(arrestee.getArresteeSequenceNumber()));
		}
	}

	private void addSubjectElements(Incident incident, Element reportElement) {
		for (Offender offender : incident.getOffenders()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "Subject");
			Element e = XmlUtils.appendChildElement(offenderElement, Namespace.nc, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Offender-" + offender.getOffenderSequenceNumber());
			XmlUtils.appendChildElement(offenderElement, Namespace.j, "OffenderSequenceNumberText").setTextContent(String.valueOf(offender.getOffenderSequenceNumber()));
		}
	}

	private void addVictimElements(Incident incident, Element reportElement) {
		for (Victim victim : incident.getVictims()) {
			Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "Victim");
			Element e = XmlUtils.appendChildElement(victimElement, Namespace.nc, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Victim-" + victim.getVictimSequenceNumber());
			XmlUtils.appendChildElement(victimElement, Namespace.j, "VictimSequenceNumberText").setTextContent(String.valueOf(victim.getVictimSequenceNumber()));
			XmlUtils.appendChildElement(victimElement, Namespace.j, "VictimCategoryCode").setTextContent(victim.getTypeOfVictim());
			for (int i=0;i < 2;i++) {
				String agg = victim.getAggravatedAssaultHomicideCircumstances(i);
				if (agg != null) {
					XmlUtils.appendChildElement(victimElement, Namespace.j, "VictimAggravatedAssaultHomicideFactorCode").setTextContent(agg);
				}
			}
			String just = victim.getAdditionalJustifiableHomicideCircumstances();
			if (just != null) {
				XmlUtils.appendChildElement(victimElement, Namespace.j, "VictimJustifiableHomicideFactorCode").setTextContent(just);
			}
		}		
	}

	private void addEnforcementOfficialElements(Incident incident, Element reportElement) {
		for (Victim victim : incident.getVictims()) {
			String victimType = victim.getTypeOfVictim();
			String officerAssignmentType = victim.getOfficerAssignmentType();
			String typeOfficerCircumstances = victim.getTypeOfOfficerActivityCircumstance();
			String officerOtherJurisdictionORI = victim.getOfficerOtherJurisdictionORI();
			if ("L".equals(victimType)) {
				Element enforcementOfficialElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "EnforcementOfficial");
				Element e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.nc, "RoleOfPerson");
				XmlUtils.addAttribute(e, Namespace.s, "ref", "Victim-" + victim.getVictimSequenceNumber());
				if (typeOfficerCircumstances != null) {
					XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.j, "EnforcementOfficialActivityCategoryCode").setTextContent(typeOfficerCircumstances);
				}
				if (officerAssignmentType != null) {
					XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.j, "EnforcementOfficialAssignmentCategoryCode").setTextContent(officerAssignmentType);
				}
				if (officerOtherJurisdictionORI != null) {
					e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.j, "EnforcementOfficialUnit");
					e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationAugmentation");
					e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationORIIdentification");
					XmlUtils.appendChildElement(e, Namespace.nc, "IdentificationID").setTextContent(officerOtherJurisdictionORI);
				}
			}
		}
	}

	private void addPersonElements(Incident incident, Element reportElement) {
		addVictimPersonElements(incident, reportElement);
		addOffenderPersonElements(incident, reportElement);
		addArresteePersonElements(incident, reportElement);
	}

	private void addArresteePersonElements(Incident incident, Element reportElement) {
		for (Arrestee arrestee : incident.getArrestees()) {
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Person");
			XmlUtils.addAttribute(arresteeElement, Namespace.s, "id", "Arrestee-" + arrestee.getArresteeSequenceNumber());
			String ethnicity = arrestee.getEthnicityOfArrestee();
			if (ethnicity != null) {
				XmlUtils.appendChildElement(arresteeElement, Namespace.nc, "PersonEthnicityCode").setTextContent(ethnicity);
			}
			NIBRSAge age = new NIBRSAge(arrestee.getAgeOfArresteeString());
			if (age.ageMin != null) {
				Element e = XmlUtils.appendChildElement(arresteeElement, Namespace.nc, "PersonAgeMeasure");
				if (age.ageMax == null) {
					XmlUtils.appendChildElement(e, Namespace.nc, "MeasureIntegerValue").setTextContent(String.valueOf(age.ageMin));
				} else {
					e = XmlUtils.appendChildElement(e, Namespace.nc, "MeasureRangeValue");
					XmlUtils.appendChildElement(e, Namespace.nc, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.ageMax));
					XmlUtils.appendChildElement(e, Namespace.nc, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.ageMin));
				}
			}
			XmlUtils.appendChildElement(arresteeElement, Namespace.j, "PersonRaceNDExCode").setTextContent(arrestee.getRaceOfArrestee());
			String residentStatusOfArrestee = arrestee.getResidentStatusOfArrestee();
			if (residentStatusOfArrestee != null) {
				XmlUtils.appendChildElement(arresteeElement, Namespace.j, "PersonResidentCode").setTextContent(residentStatusOfArrestee);
			}
			XmlUtils.appendChildElement(arresteeElement, Namespace.j, "PersonSexCode").setTextContent(arrestee.getSexOfArrestee());
		}
	}

	private void addOffenderPersonElements(Incident incident, Element reportElement) {
		for (Offender offender : incident.getOffenders()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Person");
			XmlUtils.addAttribute(offenderElement, Namespace.s, "id", "Offender-" + offender.getOffenderSequenceNumber());
			String ethnicity = offender.getEthnicityOfOffender();
			if (ethnicity != null) {
				XmlUtils.appendChildElement(offenderElement, Namespace.nc, "PersonEthnicityCode").setTextContent(ethnicity);
			}
			NIBRSAge age = new NIBRSAge(offender.getAgeOfOffenderString());
			if (age.ageMin != null) {
				Element e = XmlUtils.appendChildElement(offenderElement, Namespace.nc, "PersonAgeMeasure");
				if (age.ageMax == null) {
					XmlUtils.appendChildElement(e, Namespace.nc, "MeasureIntegerValue").setTextContent(String.valueOf(age.ageMin));
				} else {
					e = XmlUtils.appendChildElement(e, Namespace.nc, "MeasureRangeValue");
					XmlUtils.appendChildElement(e, Namespace.nc, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.ageMax));
					XmlUtils.appendChildElement(e, Namespace.nc, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.ageMin));
				}
			}
			XmlUtils.appendChildElement(offenderElement, Namespace.j, "PersonRaceNDExCode").setTextContent(offender.getRaceOfOffender());
			XmlUtils.appendChildElement(offenderElement, Namespace.j, "PersonSexCode").setTextContent(offender.getSexOfOffender());
		}
	}

	private static final class NIBRSAge {

		public Integer ageMin;
		public Integer ageMax;
		public String nonNumericAge;

		public NIBRSAge(String ageString) {
			if (ageString != null) {
				String ageStringTrim = ageString.trim();
				if (ageStringTrim.length() == 4) {
					try {
						ageMin = Integer.parseInt(ageStringTrim.substring(0, 2));
					} catch (NumberFormatException nfe) {
						// TODO: handle per error mechanism
					}
					try {
						ageMax = Integer.parseInt(ageStringTrim.substring(2, 4));
					} catch (NumberFormatException nfe) {
						// TODO: handle per error mechanism
					}
				} else {
					if ("NN".equals(ageStringTrim) || "NB".equals(ageStringTrim) || "BB".equals(ageStringTrim) || "00".equals(ageStringTrim)) {
						nonNumericAge = ageStringTrim;
					} else {
						try {
							ageMin = Integer.parseInt(ageStringTrim.substring(0, 2));
						} catch (NumberFormatException nfe) {
							// TODO: handle per error mechanism
						}
					}
				}
			}
		}

	}

	private void addVictimPersonElements(Incident incident, Element reportElement) {
		for (Victim victim : incident.getVictims()) {
			String victimType = victim.getTypeOfVictim();
			if ("L".equals(victimType) || "I".equals(victimType)) {
				Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Person");
				XmlUtils.addAttribute(victimElement, Namespace.s, "id", "Victim-" + victim.getVictimSequenceNumber());
				String ethnicity = victim.getEthnicityOfVictim();
				if (ethnicity != null) {
					XmlUtils.appendChildElement(victimElement, Namespace.nc, "PersonEthnicityCode").setTextContent(ethnicity);
				}
				NIBRSAge age = new NIBRSAge(victim.getAgeOfVictimString());
				if (age.ageMin != null) {
					Element e = XmlUtils.appendChildElement(victimElement, Namespace.nc, "PersonAgeMeasure");
					if (age.ageMax == null) {
						XmlUtils.appendChildElement(e, Namespace.nc, "MeasureIntegerValue").setTextContent(String.valueOf(age.ageMin));
					} else {
						e = XmlUtils.appendChildElement(e, Namespace.nc, "MeasureRangeValue");
						XmlUtils.appendChildElement(e, Namespace.nc, "RangeMaximumIntegerValue").setTextContent(String.valueOf(age.ageMax));
						XmlUtils.appendChildElement(e, Namespace.nc, "RangeMinimumIntegerValue").setTextContent(String.valueOf(age.ageMin));
					}
				}
				for (int i = 0; i < 5; i++) {
					String injury = victim.getTypeOfInjury(i);
					if (injury != null) {
						Element injuryElement = XmlUtils.appendChildElement(victimElement, Namespace.nc, "PersonInjury");
						XmlUtils.appendChildElement(injuryElement, Namespace.j, "InjuryCategoryCode").setTextContent(injury);
					}
				}
				XmlUtils.appendChildElement(victimElement, Namespace.j, "PersonRaceNDExCode").setTextContent(victim.getRaceOfVictim());
				String residentStatusOfVictim = victim.getResidentStatusOfVictim();
				if (residentStatusOfVictim != null) {
					XmlUtils.appendChildElement(victimElement, Namespace.j, "PersonResidentCode").setTextContent(residentStatusOfVictim);
				}
				XmlUtils.appendChildElement(victimElement, Namespace.j, "PersonSexCode").setTextContent(victim.getSexOfVictim());
				String ageCode = age.nonNumericAge;
				if (ageCode != null) {
					Element e = XmlUtils.appendChildElement(victimElement, Namespace.j, "PersonAugmentation");
					XmlUtils.appendChildElement(e, Namespace.nibrs, "PersonAgeCode").setTextContent(ageCode);
				}
			}
		}
	}

	private void addDrugPropertyElements(Incident incident, Element reportElement) {
		for (Property property : incident.getProperties()) {
			for (int i = 0; i < 10; i++) {
				String description = property.getPropertyDescription(i);
				if ("10".equals(description)) {
					Element substanceElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Substance");
					XmlUtils.appendChildElement(substanceElement, Namespace.j, "DrugCategoryCode").setTextContent(property.getSuspectedDrugType(i));
					Element e = XmlUtils.appendChildElement(substanceElement, Namespace.nc, "SubstanceQuantityMeasure");
					XmlUtils.appendChildElement(e, Namespace.nc, "MeasureDecimalValue").setTextContent(String.valueOf(property.getEstimatedDrugQuantity(i)));
					XmlUtils.appendChildElement(e, Namespace.j, "SubstanceUnitCode").setTextContent(String.valueOf(property.getTypeDrugMeasurement(i)));
				}
			}
		}

	}

	private void addNonDrugPropertyElements(Incident incident, Element reportElement) {
		for (Property property : incident.getProperties()) {
			for (int i = 0; i < 10; i++) {
				String description = property.getPropertyDescription(i);
				if (description != null && !"10".equals(description)) {
					Element itemElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Item");
					Element e = XmlUtils.appendChildElement(itemElement, Namespace.nc, "ItemStatus");
					XmlUtils.appendChildElement(e, Namespace.cjis, "ItemStatusCode").setTextContent(ITEM_STATUS_MAP.get(property.getTypeOfPropertyLoss()));
					Element itemValueElement = XmlUtils.appendChildElement(itemElement, Namespace.nc, "ItemValue");
					e = XmlUtils.appendChildElement(itemValueElement, Namespace.nc, "ItemValueAmount");
					XmlUtils.appendChildElement(e, Namespace.nc, "Amount").setTextContent(String.valueOf(property.getValueOfProperty(i)));
					Date dateRecovered = property.getDateRecovered(i);
					if (dateRecovered != null) {
						e = XmlUtils.appendChildElement(itemValueElement, Namespace.nc, "ItemValueDate");
						XmlUtils.appendChildElement(e, Namespace.nc, "Date").setTextContent(DATE_FORMAT.format(dateRecovered));
					}
					XmlUtils.appendChildElement(itemElement, Namespace.j, "ItemCategoryNIBRSPropertyCategoryCode").setTextContent(description);
					Integer rmv = property.getNumberOfRecoveredMotorVehicles();
					Integer smv = property.getNumberOfStolenMotorVehicles();
					if (rmv != null || smv != null) {
						XmlUtils.appendChildElement(itemElement, Namespace.nc, "ItemQuantity").setTextContent(String.valueOf(rmv != null ? rmv : smv));
					}
				}
			}
		}

	}

	private void addOffenseLocationAssociationElements(Incident incident, Element reportElement) {
		for (Offense offense : incident.getOffenses()) {
			Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "OffenseLocationAssociation");
			Element e = XmlUtils.appendChildElement(associationElement, Namespace.j, "Offense");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Offense-" + offense.getUcrOffenseCode());
			e = XmlUtils.appendChildElement(associationElement, Namespace.nc, "Location");
			XmlUtils.addAttribute(e, Namespace.s, "ref", "Location-" + offense.getUcrOffenseCode());
		}
	}

	private void addLocationElements(Incident incident, Element reportElement) {
		for (Offense offense : incident.getOffenses()) {
			Element locationElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Location");
			XmlUtils.addAttribute(locationElement, Namespace.s, "id", "Location-" + offense.getUcrOffenseCode());
			XmlUtils.appendChildElement(locationElement, Namespace.j, "LocationCategoryCode").setTextContent(offense.getLocationType());
		}
	}

	private void addOffenseElements(Incident incident, Element reportElement) {
		for (Offense offense : incident.getOffenses()) {
			Element offenseElement = XmlUtils.appendChildElement(reportElement, Namespace.j, "Offense");
			XmlUtils.addAttribute(offenseElement, Namespace.s, "id", "Offense-" + offense.getUcrOffenseCode());
			XmlUtils.appendChildElement(offenseElement, Namespace.nibrs, "OffenseUCRCode").setTextContent(offense.getUcrOffenseCode());
			for (int i = 0; i < 3; i++) {
				String typeOfCriminalActivity = offense.getTypeOfCriminalActivity(i);
				if (typeOfCriminalActivity != null) {
					XmlUtils.appendChildElement(offenseElement, Namespace.nibrs, "CriminalActivityCategoryCode").setTextContent(typeOfCriminalActivity);
				}
			}
			for (int i = 0; i < 5; i++) {
				String biasCode = offense.getBiasMotivation(i);
				String biasNdexCode = BIAS_MAP.get(biasCode);
				if (biasCode != null) {
					XmlUtils.appendChildElement(offenseElement, Namespace.j, "OffenseFactorBiasMotivationCode").setTextContent(biasNdexCode);
				}
			}
			Integer numberOfPremisesEntered = offense.getNumberOfPremisesEntered();
			if (numberOfPremisesEntered != null) {
				XmlUtils.appendChildElement(offenseElement, Namespace.j, "OffenseStructuresEnteredQuantity").setTextContent(String.valueOf(numberOfPremisesEntered));
			}
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
			XmlUtils.appendChildElement(offenseElement, Namespace.j, "OffenseAttemptedIndicator").setTextContent(String.valueOf(offense.getOffenseAttemptedIndicator()));
		}
	}

	private void addIncidentElement(Incident incident, Element reportElement) {
		Element incidentElement = XmlUtils.appendChildElement(reportElement, Namespace.nc, "Incident");
		Element e = XmlUtils.appendChildElement(incidentElement, Namespace.nc, "ActivityIdentification");
		e = XmlUtils.appendChildElement(e, Namespace.nc, "IdentificationID");
		e.setTextContent(incident.getIncidentNumber());
		e = XmlUtils.appendChildElement(incidentElement, Namespace.nc, "ActivityDate");
		e = XmlUtils.appendChildElement(e, Namespace.nc, "DateTime");
		e.setTextContent(DATETIME_FORMAT.format(incident.getIncidentDate()));
		Element augElement = XmlUtils.appendChildElement(incidentElement, Namespace.cjis, "IncidentAugmentation");
		e = XmlUtils.appendChildElement(augElement, Namespace.cjis, "IncidentReportDateIndicator");
		e.setTextContent(String.valueOf(incident.getReportDateIndicator()));
		Boolean cargoTheftIndicator = incident.getCargoTheftIndicator();
		if (cargoTheftIndicator != null) {
			e = XmlUtils.appendChildElement(augElement, Namespace.j, "OffenseCargoTheftIndicator");
			e.setTextContent(String.valueOf(cargoTheftIndicator));
		}
		augElement = XmlUtils.appendChildElement(incidentElement, Namespace.j, "IncidentAugmentation");
		e = XmlUtils.appendChildElement(augElement, Namespace.j, "IncidentExceptionalClearanceCode");
		e.setTextContent(incident.getExceptionalClearanceCode());
		Date exceptionalClearanceDate = incident.getExceptionalClearanceDate();
		if (exceptionalClearanceDate != null) {
			e = XmlUtils.appendChildElement(augElement, Namespace.j, "IncidentExceptionalClearanceDate");
			e = XmlUtils.appendChildElement(e, Namespace.nc, "Date");
			e.setTextContent(DATE_FORMAT.format(exceptionalClearanceDate));
		}
	}

	private void addReportHeaderElement(Incident incident, Element reportElement) {
		Element reportHeaderElement = XmlUtils.appendChildElement(reportElement, Namespace.nibrs, "ReportHeader");
		Element e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "NIBRSReportCategoryCode");
		e.setTextContent("GROUP A INCIDENT REPORT");
		e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "ReportActionCategoryCode");
		e.setTextContent("I");
		e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "ReportDate");
		e = XmlUtils.appendChildElement(e, Namespace.nc, "YearMonthDate");
		e.setTextContent(incident.getYearOfTape() + "-" + MONTH_NUMBER_FORMAT.format(incident.getMonthOfTape()));
		e = XmlUtils.appendChildElement(reportHeaderElement, Namespace.nibrs, "ReportingAgency");
		e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationAugmentation");
		e = XmlUtils.appendChildElement(e, Namespace.j, "OrganizationORIIdentification");
		e = XmlUtils.appendChildElement(e, Namespace.nc, "IdentificationID");
		e.setTextContent(incident.getOri());
	}

}
