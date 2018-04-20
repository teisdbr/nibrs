
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

package org.search.nibrs.stagingdata.service.xml;

import static org.search.nibrs.xml.NibrsNamespaceContext.Namespace.CJIS;
import static org.search.nibrs.xml.NibrsNamespaceContext.Namespace.J;
import static org.search.nibrs.xml.NibrsNamespaceContext.Namespace.NC;
import static org.search.nibrs.xml.NibrsNamespaceContext.Namespace.NIBRS;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.codes.PropertyDescriptionCode;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.AdditionalJustifiableHomicideCircumstancesType;
import org.search.nibrs.stagingdata.model.AggravatedAssaultHomicideCircumstancesType;
import org.search.nibrs.stagingdata.model.ArresteeSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.BiasMotivationType;
import org.search.nibrs.stagingdata.model.MethodOfEntryType;
import org.search.nibrs.stagingdata.model.OffenderSuspectedOfUsingType;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.SuspectedDrugType;
import org.search.nibrs.stagingdata.model.TypeInjuryType;
import org.search.nibrs.stagingdata.model.TypeOfCriminalActivityType;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolved;
import org.search.nibrs.stagingdata.model.VictimOffenderAssociation;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenderSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;
import org.search.nibrs.stagingdata.model.segment.VictimSegment;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentService;
import org.search.nibrs.xml.NibrsNamespaceContext;
import org.search.nibrs.xml.NibrsNamespaceContext.Namespace;
import org.search.nibrs.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class XmlReportGenerator {

	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());
	
	static final NumberFormat MONTH_NUMBER_FORMAT = new DecimalFormat("00");
	static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	AdministrativeSegmentService administrativeSegmentService;
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	private AppProperties appProperties;


	public Document createGroupAIncidentReport(AdministrativeSegment administrativeSegment) throws ParserConfigurationException {
		Document document = XmlUtils.createNewDocument();
		Element submissionElement = XmlUtils.appendChildElement(document, NIBRS, "Submission");
		
		addMessageMetadataElement(administrativeSegment, submissionElement);
		
		Element reportElement = XmlUtils.appendChildElement(submissionElement, NIBRS, "report"); 
		addReportHeaderElement(administrativeSegment, reportElement);
		addIncidentElement(administrativeSegment, reportElement);
		addOffenseElements(administrativeSegment, reportElement);
		addLocationElements(administrativeSegment, reportElement);
		addItemElements(administrativeSegment, reportElement);
		addSubstanceElements(administrativeSegment, reportElement);
		addPersonElements(administrativeSegment, reportElement);
		addEnforcementOfficialElements(administrativeSegment, reportElement);
		addVictimElements(administrativeSegment, reportElement);
		addSubjectElements(administrativeSegment, reportElement);
		addArresteeElements(administrativeSegment, reportElement);
		addArrestElement(administrativeSegment, reportElement);
		addArrestSubjectAssociationElements(administrativeSegment, reportElement);
		addOffenseLocationAssociationElements(administrativeSegment, reportElement);
		addOffenseVictimAssociationElements(administrativeSegment, reportElement);
		addSubjectVictimAssociationElements(administrativeSegment, reportElement);
		
		NibrsNamespaceContext namespaceContext = new NibrsNamespaceContext();
		namespaceContext.populateRootNamespaceDeclarations(document.getDocumentElement());
		return document;
	}

	private void addIncidentElement(AdministrativeSegment administrativeSegment, Element reportElement) {
		Element incidentElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Incident");
		
		appendIdentificationIdElement(incidentElement, NC, "ActivityIdentification", administrativeSegment.getIncidentNumber());
		
		Date incidentDate = administrativeSegment.getIncidentDate();
		if (incidentDate != null) {
			Element activityDate = XmlUtils.appendChildElement(incidentElement, Namespace.NC, "ActivityDate");
			Element e = XmlUtils.appendChildElement(activityDate, Namespace.NC, "Date");
			e.setTextContent(DATE_FORMAT.format(incidentDate));
		}
		
		Element incidentAugmentation = XmlUtils.appendChildElement(incidentElement, Namespace.CJIS, "IncidentAugmentation");
		XmlUtils.appendElementAndValue(incidentAugmentation, Namespace.CJIS, "IncidentReportDateIndicator", 
				administrativeSegment.getReportDateIndicator());
		Element jIncidentAugElement = XmlUtils.appendChildElement(incidentElement, Namespace.J, "IncidentAugmentation");
		XmlUtils.appendElementAndValue(jIncidentAugElement, Namespace.J, "IncidentExceptionalClearanceCode", 
				administrativeSegment.getClearedExceptionallyType().getClearedExceptionallyCode());
		
		Date exceptionalClearanceDate = administrativeSegment.getExceptionalClearanceDate();
		if (exceptionalClearanceDate != null) {
			Element incidentExceptionalClearanceDate = XmlUtils.appendChildElement(jIncidentAugElement, Namespace.J, "IncidentExceptionalClearanceDate");
			Element e = XmlUtils.appendChildElement(incidentExceptionalClearanceDate, Namespace.NC, "Date");
			e.setTextContent(DATE_FORMAT.format(exceptionalClearanceDate));
		}
	}

	private void addMessageMetadataElement(AdministrativeSegment administrativeSegment, Element submissionElement) {
		Element messageMetadata = XmlUtils.appendChildElement(submissionElement, CJIS, "MessageMetadata");
		Element messageDateTime = XmlUtils.appendChildElement(messageMetadata, CJIS, "MessageDateTime");
		messageDateTime.setTextContent(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss")));

		appendIdentificationIdElement(messageMetadata, CJIS, "MessageIdentification", 
				"GroupAIncident"+ administrativeSegment.getAdministrativeSegmentId());
		
		Element messageImplementationVersion = XmlUtils.appendChildElement(messageMetadata, CJIS, "MessageImplementationVersion"); 
		messageImplementationVersion.setTextContent("4.2");
		
		Element messageSubmittingOrganization = XmlUtils.appendChildElement(messageMetadata, CJIS, "MessageSubmittingOrganization"); 
		Element organizationAugmentation = XmlUtils.appendChildElement(messageSubmittingOrganization, J, "OrganizationAugmentation");
		
		appendIdentificationIdElement(organizationAugmentation, J, "OrganizationORIIdentification", appProperties.getSubmittingAgencyOri());
	}

	private void appendIdentificationIdElement(Element parent, Namespace wrapperNamespace, String wrapperName, String id) {
		
		if (StringUtils.isNotBlank(id)){
			Element wrapperElement = XmlUtils.appendChildElement(parent, wrapperNamespace, wrapperName);
			Element identificationId = XmlUtils.appendChildElement(wrapperElement, NC, "IdentificationID");
			identificationId.setTextContent(id);
		}
	}

	private void addReportHeaderElement(AdministrativeSegment administrativeSegment, Element reportElement) {
		Element reportHeaderElement = XmlUtils.appendChildElement(reportElement, Namespace.NIBRS, "ReportHeader");
		Element nibrsReportCategoryCode = XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "NIBRSReportCategoryCode");
		nibrsReportCategoryCode.setTextContent("GROUP A INCIDENT REPORT");
		XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "ReportActionCategoryCode")
			.setTextContent(administrativeSegment.getSegmentActionType().getSegmentActionTypeCode());
		
		Element reportDate = XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "ReportDate");
		Element yearMonthDate = XmlUtils.appendChildElement(reportDate, Namespace.NC, "YearMonthDate");
		yearMonthDate.setTextContent(administrativeSegment.getYearOfTape() + "-" 
				+ administrativeSegment.getMonthOfTape());
		
		String ori = administrativeSegment.getOri();
		if (ori != null) {
			Element reportingAgency = XmlUtils.appendChildElement(reportHeaderElement, Namespace.NIBRS, "ReportingAgency");
			Element organizationAugmentation = XmlUtils.appendChildElement(reportingAgency, Namespace.J, "OrganizationAugmentation");
			appendIdentificationIdElement(
					organizationAugmentation, J, "OrganizationORIIdentification", administrativeSegment.getOri());
		}
	}

	private void addOffenseElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (OffenseSegment offense : administrativeSegment.getOffenseSegments()) {
			Element offenseElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "OffenseSegment");
			XmlUtils.addAttribute(offenseElement, Namespace.S, "id", "OffenseSegment-" + offense.getUcrOffenseCodeType().getUcrOffenseCode());
			XmlUtils.appendElementAndValue(offenseElement, Namespace.NIBRS, "OffenseUCRCode", offense.getUcrOffenseCodeType().getUcrOffenseCode());
			
			for (TypeOfCriminalActivityType criminalActivityType: offense.getTypeOfCriminalActivityTypes()) {
				XmlUtils.appendElementAndValue(offenseElement, NIBRS, "CriminalActivityCategoryCode", criminalActivityType.getTypeOfCriminalActivityCode());
			}
			
			for (BiasMotivationType biasMotivationType: offense.getBiasMotivationTypes()) {
				if (StringUtils.isNotBlank(biasMotivationType.getBiasMotivationCode())){
					XmlUtils.appendElementAndValue(offenseElement, J, "OffenseFactorBiasMotivationCode", biasMotivationType.getBiasMotivationDescription());
				}
			}
			
			XmlUtils.appendElementAndValue(offenseElement, J, "OffenseStructuresEnteredQuantity", 
					Optional.ofNullable(offense.getNumberOfPremisesEntered()).map(String::valueOf).orElse(null));
			
			for (OffenderSuspectedOfUsingType offenderSuspectedOfUsingType: offense.getOffenderSuspectedOfUsingTypes()) {
				Element e = XmlUtils.appendChildElement(offenseElement, Namespace.J, "OffenseFactor");
				XmlUtils.appendChildElement(e, Namespace.J, "OffenseFactorCode").setTextContent(offenderSuspectedOfUsingType.getOffenderSuspectedOfUsingCode());
			}
			
			String methodOfEntry = Optional.ofNullable(offense.getMethodOfEntryType()).map(MethodOfEntryType::getMethodOfEntryCode)
					.orElse(null);
			if (StringUtils.isNotBlank(methodOfEntry)) {
				Element e = XmlUtils.appendChildElement(offenseElement, Namespace.J, "OffenseEntryPoint");
				XmlUtils.appendChildElement(e, Namespace.J, "PassagePointMethodCode").setTextContent(methodOfEntry);
			}
			
			for (TypeOfWeaponForceInvolved typeOfWeaponForceInvolved: offense.getTypeOfWeaponForceInvolveds()) {
				Element e = XmlUtils.appendChildElement(offenseElement, Namespace.J, "OffenseForce");
				XmlUtils.appendElementAndValue(e, Namespace.J, "ForceCategoryCode", 
						typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getTypeOfWeaponForceInvolvedCode());
			}
			
			XmlUtils.appendElementAndValue(offenseElement, Namespace.J, "OffenseAttemptedIndicator", 
					BooleanUtils.toStringTrueFalse("A".equals(offense.getOffenseAttemptedCompleted())));
		}
	}
	
//	<!-- Element 9, Location Type -->
//	<nc:Location s:id="Location1">
//		<nibrs:LocationCategoryCode>16</nibrs:LocationCategoryCode>
//	</nc:Location>
	private void addLocationElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (OffenseSegment offense : administrativeSegment.getOffenseSegments()) {
			Element locationElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Location");
			XmlUtils.addAttribute(locationElement, Namespace.S, "id", "Location-" + offense.getUcrOffenseCodeType().getUcrOffenseCode());
			XmlUtils.appendElementAndValue(locationElement, Namespace.J, "LocationCategoryCode", offense.getLocationType().getLocationTypeCode());
		}
	}

	private void addItemElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (PropertySegment property : administrativeSegment.getPropertySegments()) {
			if ("NONE".equalsIgnoreCase(property.getTypePropertyLossEtcType().getTypePropertyLossEtcDescription())){
			
				Element itemElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Item");
				Element itemStatus = XmlUtils.appendChildElement(itemElement, Namespace.NC, "ItemStatus");
				XmlUtils.appendElementAndValue(itemStatus, Namespace.CJIS, "ItemStatusCode", "NONE");
				continue;
			}
			
			for (PropertyType propertyType: property.getPropertyTypes()) {
				String description = propertyType.getPropertyDescriptionType().getPropertyDescriptionCode();
				if (description != null && !"10".equals(description)) {
					Element itemElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Item");
					
					addItemStatus(property, itemElement);
						
					addItemValueAndAmount(propertyType, itemElement);
					
					XmlUtils.appendElementAndValue(itemElement, Namespace.J, "ItemCategoryNIBRSPropertyCategoryCode", description);
					
					if (PropertyDescriptionCode.isMotorVehicleCode(description)){
						Integer rmv = property.getNumberOfRecoveredMotorVehicles();
						Integer smv = property.getNumberOfStolenMotorVehicles();
						if (rmv != null || smv != null) {
							XmlUtils.appendElementAndValue(itemElement, Namespace.NC, "ItemQuantity", String.valueOf(rmv != null ? rmv : smv));
						}
					}
				}
			}
		}

	}

	private void addItemValueAndAmount(PropertyType propertyType, Element parent) {
		String value = Optional.ofNullable(propertyType.getValueOfProperty())
				.map(String::valueOf).orElse(null);
		if (value != null) {
			Element itemValue = XmlUtils.appendChildElement(parent, Namespace.NC, "ItemValue");
			Element itemValueAmount = XmlUtils.appendChildElement(itemValue, Namespace.NC, "ItemValueAmount");
			XmlUtils.appendElementAndValue(itemValueAmount, Namespace.NC, "Amount", value);
			
			Date dateRecovered = propertyType.getRecoveredDate();
			if (dateRecovered != null) {
				Element itemValueDate = XmlUtils.appendChildElement(itemValue, Namespace.NC, "ItemValueDate");
				XmlUtils.appendElementAndValue(itemValueDate, Namespace.NC, "Date", DATE_FORMAT.format(dateRecovered));
			}
		}
	}

	private void addItemStatus(PropertySegment property, Element parent) {
		Element itemStatus = XmlUtils.appendChildElement(parent, Namespace.NC, "ItemStatus");
		String typeOfPropertyLoss = property.getTypePropertyLossEtcType().getTypePropertyLossEtcDescription();
		XmlUtils.appendElementAndValue(itemStatus, Namespace.CJIS, "ItemStatusCode", typeOfPropertyLoss);
	}
	
	private void addSubstanceElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (PropertySegment property : administrativeSegment.getPropertySegments()) {
			for (PropertyType propertyType : property.getPropertyTypes()) {
				String description = propertyType.getPropertyDescriptionType().getPropertyDescriptionCode();
				if ("10".equals(description)) {
					
					for (SuspectedDrugType suspectedDrugType : property.getSuspectedDrugTypes()){
						Element substanceElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Substance");
						
						addItemStatus(property, substanceElement);
						addItemValueAndAmount(propertyType, substanceElement);
						XmlUtils.appendElementAndValue(substanceElement, Namespace.J, "ItemCategoryNIBRSPropertyCategoryCode", description);

						XmlUtils.appendElementAndValue(substanceElement, J, "DrugCategoryCode", 
								suspectedDrugType.getSuspectedDrugTypeType().getSuspectedDrugTypeCode());
						
						if (suspectedDrugType.getEstimatedDrugQuantity()!= null || (suspectedDrugType.getTypeDrugMeasurementType() != null && 
								suspectedDrugType.getTypeDrugMeasurementType().getTypeDrugMeasurementTypeId() != 99998)){
							Element substanceQuantityMeasure = XmlUtils.appendChildElement(substanceElement, Namespace.NC, "SubstanceQuantityMeasure");
							XmlUtils.appendElementAndValue(substanceQuantityMeasure, Namespace.NC, "MeasureDecimalValue", 
									Optional.ofNullable(suspectedDrugType.getEstimatedDrugQuantity()).map(String::valueOf).orElse(null));
							XmlUtils.appendElementAndValue(substanceQuantityMeasure, Namespace.J, "SubstanceUnitCode",
									suspectedDrugType.getTypeDrugMeasurementType().getTypeDrugMeasurementCode());
						}
					}
				}
			}
		}

	}

	private void addPersonElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		addVictimPersonElements(administrativeSegment, reportElement);
		addOffenderPersonElements(administrativeSegment, reportElement);
		addArresteePersonElements(administrativeSegment, reportElement);
	}

	private void addVictimPersonElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (VictimSegment victim : administrativeSegment.getVictimSegments()) {
			String victimType = victim.getTypeOfVictimType().getTypeOfVictimCode();
			if ("L".equals(victimType) || "I".equals(victimType)) {
				Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Person");
				XmlUtils.addAttribute(victimElement, Namespace.S, "id", "VictimSegment-" + victim.getVictimSequenceNumber());
				Integer ageMin = victim.getAgeOfVictimMin();
				Integer ageMax = victim.getAgeOfVictimMax();
				if ( ageMin != null) {
					addPersonAgeMeasure(victimElement, ageMin, ageMax);
				}
				else{
					if (BooleanUtils.toBoolean(victim.getAgeFirstWeekIndicator()) ||
						BooleanUtils.toBoolean(victim.getAgeFirstYearIndicator()) || 
						BooleanUtils.toBoolean(victim.getAgeNeonateIndicator())){
						Element personAgeMeasure = XmlUtils.appendChildElement(victimElement, Namespace.NC, "PersonAgeMeasure");
						
						if (BooleanUtils.toBoolean(victim.getAgeFirstWeekIndicator())){
							XmlUtils.appendElementAndValue(personAgeMeasure, Namespace.NC, "MeasureIntegerValue", "NB");
						}
						else if(BooleanUtils.toBoolean(victim.getAgeFirstYearIndicator())){
							XmlUtils.appendElementAndValue(personAgeMeasure, Namespace.NC, "MeasureIntegerValue", "BB");
						}
						else if(BooleanUtils.toBoolean(victim.getAgeNeonateIndicator())){
							XmlUtils.appendElementAndValue(personAgeMeasure, Namespace.NC, "MeasureIntegerValue", "NN");
						}
					}
				}
				
				XmlUtils.appendElementAndValue(victimElement, Namespace.NC, "PersonEthnicityCode", 
						victim.getEthnicityOfPersonType().getEthnicityOfPersonCode());
				XmlUtils.appendElementAndValue(victimElement, Namespace.J, "PersonRaceNDExCode", 
						victim.getRaceOfPersonType().getRaceOfPersonCode());
				XmlUtils.appendElementAndValue(victimElement, Namespace.J, "PersonResidentCode", 
						victim.getResidentStatusOfPersonType().getResidentStatusOfPersonCode());
				XmlUtils.appendElementAndValue(victimElement, Namespace.J, "PersonSexCode", 
						victim.getSexOfPersonType().getSexOfPersonCode());
			}
		}
	}

	private void addPersonAgeMeasure(Element victimElement, Integer ageMin, Integer ageMax) {
		Element e = XmlUtils.appendChildElement(victimElement, Namespace.NC, "PersonAgeMeasure");
		if ( ageMax == null || (ageMax != null && ageMin.equals(ageMax))) {
			XmlUtils.appendElementAndValue(e, Namespace.NC, "MeasureIntegerValue", String.valueOf(ageMin));
		} else {
			e = XmlUtils.appendChildElement(e, Namespace.NC, "MeasureRangeValue");
			XmlUtils.appendElementAndValue(e, Namespace.NC, "RangeMaximumIntegerValue", String.valueOf(ageMax));
			XmlUtils.appendElementAndValue(e, Namespace.NC, "RangeMinimumIntegerValue", String.valueOf(ageMin));
		}
	}

	private void addOffenderPersonElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (OffenderSegment offender : administrativeSegment.getOffenderSegments()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Person");
			XmlUtils.addAttribute(offenderElement, Namespace.S, "id", "OffenderSegment-" + offender.getOffenderSequenceNumber());
			Integer ageMin = offender.getAgeOfOffenderMin();
			Integer ageMax = offender.getAgeOfOffenderMax();
			if ( ageMin != null) {
				addPersonAgeMeasure(offenderElement, ageMin, ageMax);
			}
			
			XmlUtils.appendElementAndValue(offenderElement, Namespace.J, "PersonRaceNDExCode", 
					offender.getRaceOfPersonType().getRaceOfPersonCode());
			XmlUtils.appendElementAndValue(offenderElement, Namespace.J, "PersonSexCode", 
					offender.getSexOfPersonType().getSexOfPersonCode());
		}
	}

	private void addArresteePersonElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (ArresteeSegment arrestee : administrativeSegment.getArresteeSegments()) {
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.NC, "Person");
			XmlUtils.addAttribute(arresteeElement, Namespace.S, "id", "PersonArrestee-" + arrestee.getArresteeSequenceNumber());
			
			Integer ageMin = arrestee.getAgeOfArresteeMin();
			Integer ageMax = arrestee.getAgeOfArresteeMax();
			if ( ageMin != null) {
				addPersonAgeMeasure(arresteeElement, ageMin, ageMax);
			}
			XmlUtils.appendElementAndValue(arresteeElement, Namespace.NC, "PersonEthnicityCode", 
					arrestee.getEthnicityOfPersonType().getEthnicityOfPersonCode());
			XmlUtils.appendElementAndValue(arresteeElement, Namespace.J, "PersonRaceNDExCode", 
					arrestee.getRaceOfPersonType().getRaceOfPersonCode());
			XmlUtils.appendElementAndValue(arresteeElement, Namespace.J, "PersonResidentCode", 
					arrestee.getResidentStatusOfPersonType().getResidentStatusOfPersonCode());
			XmlUtils.appendElementAndValue(arresteeElement, Namespace.J, "PersonSexCode", 
					arrestee.getSexOfPersonType().getSexOfPersonCode());
		}
	}
	
//<j:EnforcementOfficial>
//	<nc:RoleOfPerson s:ref="PersonVictim1"/>
//	<!-- Element 25A, Type of Activity (Officer)/ Circumstance -->
//	<j:EnforcementOfficialActivityCategoryCode>10</j:EnforcementOfficialActivityCategoryCode>
//	<!-- Element 25B, Assignment Type (Officer) -->
//	<j:EnforcementOfficialAssignmentCategoryCode>G</j:EnforcementOfficialAssignmentCategoryCode>
//	<j:EnforcementOfficialUnit>
//		<j:OrganizationAugmentation>
//			<j:OrganizationORIIdentification>
//				<!-- Element 25C, ORI-Other Jurisdiction (Officer) -->
//				<nc:IdentificationID>WVNDX01</nc:IdentificationID>
//			</j:OrganizationORIIdentification>
//		</j:OrganizationAugmentation>
//	</j:EnforcementOfficialUnit>
//</j:EnforcementOfficial>
	private void addEnforcementOfficialElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (VictimSegment victim : administrativeSegment.getVictimSegments()) {
			String victimType = victim.getTypeOfVictimType().getTypeOfVictimCode();
			if ("L".equals(victimType)) {
				Element enforcementOfficialElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "EnforcementOfficial");
				Element e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.NC, "RoleOfPerson");
				XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
				XmlUtils.appendElementAndValue(enforcementOfficialElement, Namespace.J, "EnforcementOfficialActivityCategoryCode", 
						victim.getOfficerActivityCircumstanceType().getOfficerActivityCircumstanceCode());
				XmlUtils.appendElementAndValue(enforcementOfficialElement, Namespace.J, "EnforcementOfficialAssignmentCategoryCode", 
						victim.getOfficerAssignmentTypeType().getOfficerAssignmentTypeCode());
				
				String officerOtherJurisdictionOri = victim.getOfficerOtherJurisdictionOri();
				if (officerOtherJurisdictionOri != null) {
					e = XmlUtils.appendChildElement(enforcementOfficialElement, Namespace.J, "EnforcementOfficialUnit");
					e = XmlUtils.appendChildElement(e, Namespace.J, "OrganizationAugmentation");
					e = XmlUtils.appendChildElement(e, Namespace.J, "OrganizationORIIdentification");
					XmlUtils.appendElementAndValue(e, Namespace.NC, "IdentificationID", officerOtherJurisdictionOri);
				}
			}
		}
	}

	private void addVictimElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (VictimSegment victim : administrativeSegment.getVictimSegments()) {
			Element victimElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "VictimSegment");
			Element e = XmlUtils.appendChildElement(victimElement, Namespace.NC, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
			XmlUtils.appendElementAndValue(victimElement, Namespace.J, "VictimSequenceNumberText", String.valueOf(victim.getVictimSequenceNumber()));
			
			for (TypeInjuryType typeInjuryType :victim.getTypeInjuryTypes()){
				Element victimInjury = XmlUtils.appendChildElement(victimElement, J, "VictimInjury");
				XmlUtils.appendElementAndValue(victimInjury, J, "InjuryCategoryCode", typeInjuryType.getTypeInjuryCode());
			}
			
			XmlUtils.appendElementAndValue(victimElement, Namespace.J, "VictimCategoryCode", victim.getTypeOfVictimType().getTypeOfVictimCode());
			for (AggravatedAssaultHomicideCircumstancesType homicideCircumstancesType : 
				victim.getAggravatedAssaultHomicideCircumstancesTypes()) {
				XmlUtils.appendElementAndValue(victimElement, Namespace.J, "VictimAggravatedAssaultHomicideFactorCode", 
						homicideCircumstancesType.getAggravatedAssaultHomicideCircumstancesCode());
			}
			
			XmlUtils.appendElementAndValue(victimElement, Namespace.J, "VictimJustifiableHomicideFactorCode", 
					Optional.ofNullable(victim.getAdditionalJustifiableHomicideCircumstancesType())
					.map(AdditionalJustifiableHomicideCircumstancesType::getAdditionalJustifiableHomicideCircumstancesCode)
					.orElse(null));
		}		
	}

	private void addSubjectElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (OffenderSegment offender : administrativeSegment.getOffenderSegments()) {
			Element offenderElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "Subject");
			Element e = XmlUtils.appendChildElement(offenderElement, Namespace.NC, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenderSegment-" + offender.getOffenderSequenceNumber());
			XmlUtils.appendElementAndValue(offenderElement, Namespace.J, "OffenderSequenceNumberText", String.valueOf(offender.getOffenderSequenceNumber()));
		}
	}

//<j:Arrestee s:id="Arrestee1">
//	<nc:RoleOfPerson s:ref="PersonArrestee1"/>
//	<!-- Element 40, Arrestee Sequence Number -->
//	<j:ArrestSequenceID>1</j:ArrestSequenceID>
//	<!-- Element 46, Arrestee Was Armed With -->
//	<j:ArresteeArmedWithCode>12</j:ArresteeArmedWithCode>
//    <j:ArresteeArmedWithCode>13A</j:ArresteeArmedWithCode>
//	<!-- Element 52, Disposition of Arrestee Under 18 -->
//	<j:ArresteeJuvenileDispositionCode>H</j:ArresteeJuvenileDispositionCode>
//	<!-- Element 44, Multiple Arrestee Segments Indicator -->
//	<j:ArrestSubjectCountCode>N</j:ArrestSubjectCountCode>
//</j:Arrestee>
	private void addArresteeElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (ArresteeSegment arrestee : administrativeSegment.getArresteeSegments()) {
			Element arresteeElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "Arrestee");
			XmlUtils.addAttribute(arresteeElement, Namespace.S, "id", "Arrestee-" + arrestee.getArresteeSequenceNumber());
			Element e = XmlUtils.appendChildElement(arresteeElement, Namespace.NC, "RoleOfPerson");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "PersonArrestee-" + arrestee.getArresteeSequenceNumber());
			XmlUtils.appendElementAndValue(arresteeElement, Namespace.J, "ArrestSequenceID", String.valueOf(arrestee.getArresteeSequenceNumber()));
			
			for (ArresteeSegmentWasArmedWith armedWith: arrestee.getArresteeSegmentWasArmedWiths()){
				XmlUtils.appendElementAndValue(arresteeElement, J, "ArresteeArmedWithCode", 
						armedWith.getArresteeWasArmedWithType().getArresteeWasArmedWithCode());
			}
			
			if (arrestee.getDispositionOfArresteeUnder18Type()!= null){
				XmlUtils.appendElementAndValue(arresteeElement, J, "ArresteeJuvenileDispositionCode", 
						arrestee.getDispositionOfArresteeUnder18Type().getDispositionOfArresteeUnder18Code());
			}
			
			if (arrestee.getMultipleArresteeSegmentsIndicatorType() != null){
				XmlUtils.appendElementAndValue(arresteeElement, J, "ArrestSubjectCountCode", 
						arrestee.getMultipleArresteeSegmentsIndicatorType().getMultipleArresteeSegmentsIndicatorCode());
			}
		}
	}
	
//<j:Arrest s:id="Arrest1">
//	<!-- Element 41, Arrest Transaction Number -->
//	<nc:ActivityIdentification>
//		<nc:IdentificationID>12345</nc:IdentificationID>
//	</nc:ActivityIdentification>
//	<!-- Element 42, Arrest Date -->
//	<nc:ActivityDate>
//		<nc:Date>2016-02-28</nc:Date>
//	</nc:ActivityDate>
//	<!-- Element 45, UCR Arrest Offense Code -->
//	<j:ArrestCharge>
//		<nibrs:ChargeUCRCode>64A</nibrs:ChargeUCRCode>
//	</j:ArrestCharge>
//	<!-- Element 43, Type Of Arrest -->
//	<j:ArrestCategoryCode>O</j:ArrestCategoryCode>
//</j:Arrest>
	private void addArrestElement(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (ArresteeSegment arrestee : administrativeSegment.getArresteeSegments()) {
			Element arrestElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "Arrest");
			XmlUtils.addAttribute(arrestElement, Namespace.S, "id", "Arrest-" + arrestee.getArresteeSequenceNumber());
			appendIdentificationIdElement(arrestElement, NC, "ActivityIdentification", arrestee.getArrestTransactionNumber());
			
			Date arrestDate = arrestee.getArrestDate(); 
			
			if (arrestDate != null) {
				Element activityDate = XmlUtils.appendChildElement(arrestElement, Namespace.NC, "ActivityDate");
				XmlUtils.appendElementAndValue(activityDate, NC, "Date", DATE_FORMAT.format(arrestDate));
			}
			
			Element arrestCharge = XmlUtils.appendChildElement(arrestElement, Namespace.J, "ArrestCharge");
			XmlUtils.appendElementAndValue(arrestCharge, Namespace.NIBRS, "ChargeUCRCode", arrestee.getUcrOffenseCodeType().getUcrOffenseCode());
			XmlUtils.appendElementAndValue(arrestElement, Namespace.J, "ArrestCategoryCode", arrestee.getTypeOfArrestType().getTypeOfArrestCode());
		}		
	}
	
//<j:ArrestSubjectAssociation>
//	<nc:Activity s:ref="Arrest1"/>
//	<j:Subject s:ref="Arrestee1"/>
//</j:ArrestSubjectAssociation>
	private void addArrestSubjectAssociationElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (ArresteeSegment arrestee : administrativeSegment.getArresteeSegments()) {
			Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "ArrestSubjectAssociation");
			Element e = XmlUtils.appendChildElement(associationElement, Namespace.NC, "Activity");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "Arrest-" + arrestee.getArresteeSequenceNumber());
			e = XmlUtils.appendChildElement(associationElement, Namespace.J, "Subject");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "Arrestee-" + arrestee.getArresteeSequenceNumber());
		}
	}
	
//<j:OffenseLocationAssociation>
//	<j:Offense s:ref="Offense1"/>
//	<nc:Location s:ref="Location1"/>
//</j:OffenseLocationAssociation>
	private void addOffenseLocationAssociationElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (OffenseSegment offense : administrativeSegment.getOffenseSegments()) {
			Element associationElement = XmlUtils.appendChildElement(reportElement, J, "OffenseLocationAssociation");
			Element e = XmlUtils.appendChildElement(associationElement, J, "Offense");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenseSegment-" + offense.getUcrOffenseCodeType().getUcrOffenseCode());
			e = XmlUtils.appendChildElement(associationElement, Namespace.NC, "Location");
			XmlUtils.addAttribute(e, Namespace.S, "ref", "Location-" + offense.getUcrOffenseCodeType().getUcrOffenseCode());
		}
	}

//<j:OffenseVictimAssociation>
//	<j:Offense s:ref="Offense1"/>
//	<j:Victim s:ref="Victim1"/>
//</j:OffenseVictimAssociation>
	private void addOffenseVictimAssociationElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (VictimSegment victim : administrativeSegment.getVictimSegments()) {
			for (OffenseSegment offense:victim.getOffenseSegments()) {
				String ucrCode = offense.getUcrOffenseCodeType().getUcrOffenseCode();
				Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "OffenseVictimAssociation");
				Element e = XmlUtils.appendChildElement(associationElement, Namespace.J, "Offense");
				XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenseSegment-" + ucrCode);
				e = XmlUtils.appendChildElement(associationElement, Namespace.J, "Victim");
				XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
			}
		}
	}
	
//<j:SubjectVictimAssociation s:id="SubjectVictimAssocSP1">
//	<!-- Element 35, Relationship(s) of Victim To Offender -->
//	<j:Subject s:ref="Subject1"/>
//	<j:Victim s:ref="Victim1"/>
//	<nibrs:VictimToSubjectRelationshipCode>Acquaintance</nibrs:VictimToSubjectRelationshipCode>
//</j:SubjectVictimAssociation>
	private void addSubjectVictimAssociationElements(AdministrativeSegment administrativeSegment, Element reportElement) {
		for (VictimSegment victim : administrativeSegment.getVictimSegments()) {
			for (VictimOffenderAssociation victimOffenderAssociation: victim.getVictimOffenderAssociations()) {
				Integer offenderSequenceNumber = victimOffenderAssociation.getOffenderSegment().getOffenderSequenceNumber();
				String relationshipTypeCode = victimOffenderAssociation.getVictimOffenderRelationshipType().getVictimOffenderRelationshipCode();
				
				Element associationElement = XmlUtils.appendChildElement(reportElement, Namespace.J, "SubjectVictimAssociation");
				Element e = XmlUtils.appendChildElement(associationElement, Namespace.J, "Subject");
				XmlUtils.addAttribute(e, Namespace.S, "ref", "OffenderSegment-" + offenderSequenceNumber);
				e = XmlUtils.appendChildElement(associationElement, Namespace.J, "Victim");
				XmlUtils.addAttribute(e, Namespace.S, "ref", "VictimSegment-" + victim.getVictimSequenceNumber());
				
				if (StringUtils.isNotBlank(relationshipTypeCode)) {
					XmlUtils.appendElementAndValue(associationElement, NIBRS, "VictimToSubjectRelationshipCode", relationshipTypeCode);
				}
			}
		}
	}
	
}
