<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccq-res-doc="http://ojbc.org/IEPD/Exchange/CourtCaseQueryResults/1.0" xmlns:ccq-res-ext="http://ojbc.org/IEPD/Extensions/CourtCaseQueryResultsExtension/1.0" xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0" xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0" xmlns:srm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/3.1/" xmlns:cyfs="http://release.niem.gov/niem/domains/cyfs/3.1/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<!--xsl:variable name="lexsDigest" select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest"/>
	<xsl:variable name="ndexiaReport" select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport"/-->
	<xsl:template match="/CourtCaseQueryResults">
		<ccq-res-doc:CourtCaseQueryResults>
			<xsl:apply-templates select="DocumentCreationDate[normalize-space(.)]"/>
			<xsl:apply-templates select="DocumentIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="SystemIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="Case[normalize-space(.)]"/>
			<xsl:apply-templates select="Identity[normalize-space(.)]"/>
			<xsl:apply-templates select="Citation[normalize-space(.)]"/>
			<xsl:apply-templates select="Fee[normalize-space(.)]"/>
			<xsl:apply-templates select="Vehicle[normalize-space(.)]"/>
			<xsl:apply-templates select="DisciplinaryActionRestitution[normalize-space(.)]"/>
			<xsl:apply-templates select="Person[normalize-space(.)]"/>
			<xsl:apply-templates select="Location[normalize-space(.)]"/>
		</ccq-res-doc:CourtCaseQueryResults>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="DocumentCreationDate">
		<nc:DocumentCreationDate>
			<xsl:apply-templates select="DateTime[normalize-space(.)]"/>
		</nc:DocumentCreationDate>
	</xsl:template>
	<xsl:template match="DocumentIdentification">
		<nc:DocumentIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</nc:DocumentIdentification>
	</xsl:template>
	<xsl:template match="SystemIdentification">
		<intel:SystemIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
			<xsl:apply-templates select="SystemName[normalize-space(.)]"/>
		</intel:SystemIdentification>
	</xsl:template>
	<xsl:template match="Case">
		<nc:Case>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="ActivityIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityStatus[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseCategoryText[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseGeneralCategoryText[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseTrackingID[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseDocketID[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseFiling[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseAugmentation[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseAugmentation[normalize-space(.)]" mode="CAE"/>
		</nc:Case>
	</xsl:template>
	<xsl:template match="Identity">
		<nc:Identity>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="IdentityPersonRepresentation[normalize-space(.)]"/>
		</nc:Identity>
	</xsl:template>
	<xsl:template match="Citation">
		<j:Citation>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="ActivityIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="CitationViolation[normalize-space(.)]"/>
			<xsl:apply-templates select="CitationAugmentation[normalize-space(.)]"/>
		</j:Citation>
	</xsl:template>
	<xsl:template match="Fee">
		<nc:Fee>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="ObligationPaidDate[normalize-space(.)]"/>
		</nc:Fee>
	</xsl:template>
	<xsl:template match="Vehicle">
		<nc:Vehicle>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="ConveyanceColorPrimaryCode[normalize-space(.)]"/>
			<xsl:apply-templates select="ConveyanceAugmentation[normalize-space(.)]"/>
			<xsl:apply-templates select="VehicleCMVIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="VehicleIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="VehicleMakeCode[normalize-space(.)]"/>
			<xsl:apply-templates select="VehicleModelCode[normalize-space(.)]"/>
		</nc:Vehicle>
	</xsl:template>
	<xsl:template match="DisciplinaryActionRestitution">
		<nc:DisciplinaryActionRestitution>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="ObligationDueAmount[normalize-space(.)]"/>
			<xsl:apply-templates select="ObligationPaidAmount[normalize-space(.)]"/>
		</nc:DisciplinaryActionRestitution>
	</xsl:template>
	<xsl:template match="Person">
		<nc:Person>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="PersonBirthDate[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonEthnicityText[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonEyeColorCode[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonHairColorCode[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonHeightMeasure[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonName[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonOtherIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonPhysicalFeature[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonPrimaryLanguage[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonRaceText[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonSexText[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonSSNIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonWeightMeasure[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonAugmentation[normalize-space(.)]" mode="cyfs"/>
			<xsl:apply-templates select="PersonAugmentation[normalize-space(.)]" mode="jxdm"/>
			<xsl:apply-templates select="PersonInmateIdentification[normalize-space(.)]"/>
		</nc:Person>
	</xsl:template>
	<xsl:template match="Location">
		<nc:Location>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="Address[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationCategoryText[normalize-space(.)]"/>
		</nc:Location>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="ActivityStatus">
		<nc:ActivityStatus>
			<xsl:apply-templates select="StatusCommentText[normalize-space(.)]"/>
			<xsl:apply-templates select="StatusDate[normalize-space(.)]"/>
			<xsl:apply-templates select="StatusDescriptionText[normalize-space(.)]"/>
		</nc:ActivityStatus>
	</xsl:template>
	<xsl:template match="CaseFiling">
		<nc:CaseFiling>
			<xsl:apply-templates select="DocumentCreationDate[normalize-space(.)]"/>
		</nc:CaseFiling>
	</xsl:template>
	<xsl:template match="CaseAugmentation">
		<j:CaseAugmentation>
			<xsl:apply-templates select="CaseAmendedCharge[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseCharge[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseCourt[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseCourtEvent[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseDefendantSelfRepresentationIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseDefendantParty[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseDomesticViolenceIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseHearing[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseJudge[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseLineageCase/CaseTrackingID[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseOtherIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseTrial[normalize-space(.)]"/>
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="CaseAugmentation" mode="CAE">
		<ccq-res-ext:CaseAugmentation>
			<xsl:apply-templates select="CaseSealedIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="RemandDate[normalize-space(.)]"/>
			<xsl:apply-templates select="CaseUnderAdvisementDate[normalize-space(.)]"/>
			<xsl:apply-templates select="JuryRequestedIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="JuryVerdictIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="FailureToPayHoldIndefiniteIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="FailureToPayHoldDate[normalize-space(.)]"/>
			<xsl:apply-templates select="FailureToAppearHoldDate[normalize-space(.)]"/>
			<xsl:apply-templates select="FailureToComplyHoldDate[normalize-space(.)]"/>
			<xsl:apply-templates select="FailureToPayVictimHoldDate[normalize-space(.)]"/>
		</ccq-res-ext:CaseAugmentation>
	</xsl:template>
	<xsl:template match="IdentityPersonRepresentation[normalize-space(.)]">
		<nc:IdentityPersonRepresentation>
			<xsl:apply-templates select="PersonBirthDate[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonName[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonSSNIdentification[normalize-space(.)]"/>
		</nc:IdentityPersonRepresentation>
	</xsl:template>
	<xsl:template match="CitationViolation">
		<j:CitationViolation>
			<xsl:apply-templates select="ActivityDate[normalize-space(.)]"/>
			<xsl:apply-templates select="IncidentLocation[normalize-space(.)]"/>
			<xsl:apply-templates select="ViolationAugmentation[normalize-space(.)]"/>
		</j:CitationViolation>
	</xsl:template>
	<xsl:template match="CitationAugmentation">
		<ccq-res-ext:CitationAugmentation>
			<xsl:apply-templates select="CourtAppearanceDate[normalize-space(.)]"/>
			<xsl:apply-templates select="OffenseDomesticViolenceIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="VehicleOverweightMeasure/MeasureValueText[normalize-space(.)]"/>
			<xsl:apply-templates select="CourtAppearanceRequiredIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="InjuryIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="SchoolZoneIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="ConstructionZoneIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="LicenseSurrenderedIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="FatalityIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="HazardousMaterialsIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="NuclearMaterialsIndicator[normalize-space(.)]"/>
		</ccq-res-ext:CitationAugmentation>
	</xsl:template>
	<xsl:template match="ConveyanceAugmentation">
		<j:ConveyanceAugmentation>
			<xsl:apply-templates select="ModelYearRange[normalize-space(.)]"/>
		</j:ConveyanceAugmentation>
	</xsl:template>
	<xsl:template match="PersonAugmentation" mode="cyfs">
		<cyfs:PersonAugmentation>
			<xsl:apply-templates select="StudentIdentification[normalize-space(.)]"/>
		</cyfs:PersonAugmentation>
	</xsl:template>
	<xsl:template match="PersonAugmentation" mode="jxdm">
		<j:PersonAugmentation>
			<xsl:apply-templates select="DriverLicense[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonFBIIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonStateFingerprintIdentification[normalize-space(.)]"/>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="Address">
		<nc:Address>
			<xsl:apply-templates select="AddressSecondaryUnitText[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationStreet[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationCityName[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationStateName[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationPostalCode[normalize-space(.)]"/>
		</nc:Address>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="CaseAmendedCharge">
		<j:CaseAmendedCharge>
			<xsl:apply-templates select="ChargeCountQuantity[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeFilingDate[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeStatute[normalize-space(.)]"/>
		</j:CaseAmendedCharge>
	</xsl:template>
	<xsl:template match="CaseCharge">
		<j:CaseCharge>
			<xsl:apply-templates select="ChargeCountQuantity[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeDisposition[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeFilingDate[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargePlea[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeSentence[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeSequenceID[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeStatute[normalize-space(.)]"/>
		</j:CaseCharge>
	</xsl:template>
	<xsl:template match="CaseCourt">
		<j:CaseCourt>
			<xsl:apply-templates select="OrganizationAugmentation[normalize-space(.)]"/>
		</j:CaseCourt>
	</xsl:template>
	<xsl:template match="CaseCourtEvent">
		<j:CaseCourtEvent>
			<xsl:apply-templates select="ActivityIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityDate[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityName[normalize-space(.)]"/>
			<xsl:apply-templates select="CourtEventJudge/RoleOfPerson/PersonName/PersonFullName[normalize-space(.)]"/>
			<xsl:apply-templates select="CommentsForCourtClerk[normalize-space(.)]"/>
			<xsl:apply-templates select="FirstCourtAppearance/CourtAppearanceDate[normalize-space(.)]"/>
			<xsl:apply-templates select="CourtEventCommentsText[normalize-space(.)]"/>
		</j:CaseCourtEvent>
	</xsl:template>
	<xsl:template match="CaseDefendantParty">
		<xsl:apply-templates select="EntityOrganization[normalize-space(.)]"/>
		<xsl:apply-templates select="EntityPerson[normalize-space(.)]"/>
	</xsl:template>
	<xsl:template match="CaseHearing">
		<j:CaseHearing>
			<xsl:apply-templates select="ActivityIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityCategoryText[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityDateRange[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityName[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityReasonText[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityDisposition[normalize-space(.)]"/>
			<xsl:apply-templates select="CourtEventAppearance/CourtAppearanceCourt/CourtName[normalize-space(.)]"/>
		</j:CaseHearing>
	</xsl:template>
	<xsl:template match="CaseJudge">
		<j:CaseJudge>
			<xsl:apply-templates select="RoleOfPerson/PersonName/PersonFullName[normalize-space(.)]"/>
			<xsl:apply-templates select="JudicialOfficialBarMembership[normalize-space(.)]"/>
		</j:CaseJudge>
	</xsl:template>
	<xsl:template match="CaseOtherIdentification">
		<j:CaseOtherIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
			<xsl:apply-templates select="IdentificationCategoryDescriptionText[normalize-space(.)]"/>
		</j:CaseOtherIdentification>
	</xsl:template>
	<xsl:template match="CaseTrial">
		<j:CaseTrial>
			<xsl:apply-templates select="TrialByText[normalize-space(.)]"/>
			<xsl:apply-templates select="SpeedyTrialDate[normalize-space(.)]"/>
		</j:CaseTrial>
	</xsl:template>
	<xsl:template match="IncidentLocation">
		<nc:IncidentLocation>
			<xsl:apply-templates select="Address[normalize-space(.)]"/>
			<xsl:apply-templates select="AddressCrossStreet/CrossStreetDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationName[normalize-space(.)]"/>
		</nc:IncidentLocation>
	</xsl:template>
	<xsl:template match="ViolationAugmentation">
		<ccq-res-ext:ViolationAugmentation>
			<xsl:apply-templates select="DrivingPointNumberText[normalize-space(.)]"/>
			<xsl:apply-templates select="DrivingPointNumberReducedText[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonBloodAlcoholContentTestRefusedIndicator[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonBloodAlcoholContentNumberText[normalize-space(.)]"/>
		</ccq-res-ext:ViolationAugmentation>
	</xsl:template>
	<xsl:template match="ModelYearRange">
		<j:ModelYearRange>
			<xsl:apply-templates select="StartDate[normalize-space(.)]"/>
			<xsl:apply-templates select="EndDate[normalize-space(.)]"/>
		</j:ModelYearRange>
	</xsl:template>
	<xsl:template match="DriverLicense">
		<xsl:apply-templates select="DriverLicenseIdentification[normalize-space(.)]"/>
		<xsl:apply-templates select="DriverLicenseExpirationDate[normalize-space(.)]"/>
		<xsl:apply-templates select="DriverLicenseIssuedDate[normalize-space(.)]"/>
		<xsl:apply-templates select="DriverLicenseCommercialClassText[normalize-space(.)]"/>
		<xsl:apply-templates select="DriverLicenseNonCommercialClassText[normalize-space(.)]"/>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="StatusDate">
		<nc:StatusDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</nc:StatusDate>
	</xsl:template>
	<xsl:template match="ChargeFilingDate">
		<j:ChargeFilingDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</j:ChargeFilingDate>
	</xsl:template>
	<xsl:template match="ChargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="StatuteCodeIdentification[normalize-space(.)]"/>
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="ChargeDisposition">
		<j:ChargeDisposition>
			<xsl:apply-templates select="DispositionDate[normalize-space(.)]"/>
			<xsl:apply-templates select="DispositionDescriptionText[normalize-space(.)]"/>
			<xsl:apply-templates select="ChargeDispositionOtherText[normalize-space(.)]"/>
		</j:ChargeDisposition>
	</xsl:template>
	<xsl:template match="ChargePlea">
		<j:ChargePlea>
			<xsl:apply-templates select="ActivityDate[normalize-space(.)]"/>
			<xsl:apply-templates select="PleaCategoryCode[normalize-space(.)]"/>
		</j:ChargePlea>
	</xsl:template>
	<xsl:template match="ChargeSentence">
		<j:ChargeSentence>
			<xsl:apply-templates select="ActivityIdentification[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityDate[normalize-space(.)]"/>
			<xsl:apply-templates select="ActivityStatus[normalize-space(.)]"/>
			<xsl:apply-templates select="SentenceCharge[normalize-space(.)]"/>
			<xsl:apply-templates select="SupervisionFineAmount[normalize-space(.)]"/>
			<xsl:apply-templates select="SentenceDeferredDate[normalize-space(.)]"/>
			<xsl:apply-templates select="SentenceConfinementCategoryText[normalize-space(.)]"/>
			<xsl:apply-templates select="SentenceConfinementComment[normalize-space(.)]"/>
			<xsl:apply-templates select="SentenceWorkReleaseIndicator[normalize-space(.)]"/>
		</j:ChargeSentence>
	</xsl:template>
	<xsl:template match="OrganizationAugmentation">
		<j:OrganizationAugmentation>
			<xsl:apply-templates select="OrganizationJurisdiction[normalize-space(.)]"/>
		</j:OrganizationAugmentation>
	</xsl:template>
	<xsl:template match="OrganizationAugmentation">
		<j:OrganizationAugmentation>
			<xsl:apply-templates select="OrganizationJurisdiction[normalize-space(.)]"/>
		</j:OrganizationAugmentation>
	</xsl:template>
	<xsl:template match="FirstCourtAppearance/CourtAppearanceDate">
		<ccq-res-ext:FirstCourtAppearance>
			<j:CourtAppearanceDate>
				<xsl:apply-templates select="Date[normalize-space(.)]"/>
			</j:CourtAppearanceDate>
		</ccq-res-ext:FirstCourtAppearance>
	</xsl:template>
	<xsl:template match="ActivityDateRange">
		<nc:ActivityDateRange>
			<xsl:apply-templates select="StartDate[normalize-space(.)]"/>
			<xsl:apply-templates select="EndDate[normalize-space(.)]"/>
		</nc:ActivityDateRange>
	</xsl:template>
	<xsl:template match="ActivityDisposition">
		<nc:ActivityDisposition>
			<xsl:apply-templates select="DispositionDescriptionText[normalize-space(.)]"/>
		</nc:ActivityDisposition>
	</xsl:template>
	<xsl:template match="SpeedyTrialDate">
		<ccq-res-ext:SpeedyTrialDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:SpeedyTrialDate>
	</xsl:template>
	<xsl:template match="Address">
		<nc:Address>
			<xsl:apply-templates select="AddressSecondaryUnitText[normalize-space(.)]"/>
			<xsl:apply-templates select="AddressDeliveryPointID[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationStreet[normalize-space(.)]"/>
			<xsl:apply-templates select="LocationCountyName[normalize-space(.)]"/>
		</nc:Address>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="StatuteCodeIdentification">
		<j:StatuteCodeIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
			<xsl:apply-templates select="IdentificationCategoryDescriptionText[normalize-space(.)]"/>
		</j:StatuteCodeIdentification>
	</xsl:template>
	<xsl:template match="DispositionDate">
		<nc:DispositionDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</nc:DispositionDate>
	</xsl:template>
	<xsl:template match="ActivityDate">
		<nc:ActivityDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="SentenceCharge">
		<j:SentenceCharge>
			<xsl:apply-templates select="ChargeStatute[normalize-space(.)]"/>
		</j:SentenceCharge>
	</xsl:template>
	<xsl:template match="LocationStreet">
		<nc:LocationStreet>
			<xsl:apply-templates select="StreetName[normalize-space(.)]"/>
			<xsl:apply-templates select="StreetCategoryText[normalize-space(.)]"/>
		</nc:LocationStreet>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="DateTime">
		<nc:DateTime>
			<xsl:value-of select="."/>
		</nc:DateTime>
	</xsl:template>
	<xsl:template match="IdentificationID">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="SystemName">
		<nc:SystemName>
			<xsl:value-of select="."/>
		</nc:SystemName>
	</xsl:template>
	<xsl:template match="ActivityIdentification">
		<nc:ActivityIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="StatusCommentText">
		<nc:StatusCommentText>
			<xsl:value-of select="."/>
		</nc:StatusCommentText>
	</xsl:template>
	<xsl:template match="Date[normalize-space(.)]">
		<nc:Date>
			<xsl:value-of select="."/>
		</nc:Date>
	</xsl:template>
	<xsl:template match="StatusDescriptionText">
		<nc:StatusDescriptionText>
			<xsl:value-of select="."/>
		</nc:StatusDescriptionText>
	</xsl:template>
	<xsl:template match="CaseCategoryText">
		<nc:CaseCategoryText>
			<xsl:value-of select="."/>
		</nc:CaseCategoryText>
	</xsl:template>
	<xsl:template match="CaseGeneralCategoryText">
		<nc:CaseGeneralCategoryText>
			<xsl:value-of select="."/>
		</nc:CaseGeneralCategoryText>
	</xsl:template>
	<xsl:template match="CaseTrackingID">
		<nc:CaseTrackingID>
			<xsl:value-of select="."/>
		</nc:CaseTrackingID>
	</xsl:template>
	<xsl:template match="CaseDocketID">
		<nc:CaseDocketID>
			<xsl:value-of select="."/>
		</nc:CaseDocketID>
	</xsl:template>
	<xsl:template match="ChargeCountQuantity">
		<j:ChargeCountQuantity>
			<xsl:value-of select="."/>
		</j:ChargeCountQuantity>
	</xsl:template>
	<xsl:template match="ChargeDescriptionText">
		<j:ChargeDescriptionText>
			<xsl:value-of select="."/>
		</j:ChargeDescriptionText>
	</xsl:template>
	<xsl:template match="IdentificationCategoryDescriptionText">
		<nc:IdentificationCategoryDescriptionText>
			<xsl:value-of select="."/>
		</nc:IdentificationCategoryDescriptionText>
	</xsl:template>
	<xsl:template match="DispositionDescriptionText">
		<nc:DispositionDescriptionText>
			<xsl:value-of select="."/>
		</nc:DispositionDescriptionText>
	</xsl:template>
	<xsl:template match="ChargeDispositionOtherText">
		<j:ChargeDispositionOtherText>
			<xsl:value-of select="."/>
		</j:ChargeDispositionOtherText>
	</xsl:template>
	<xsl:template match="PleaCategoryCode">
		<j:PleaCategoryCode>
			<xsl:value-of select="."/>
		</j:PleaCategoryCode>
	</xsl:template>
	<xsl:template match="SupervisionFineAmount">
		<j:SupervisionFineAmount>
			<xsl:apply-templates select="Amount[normalize-space(.)]"/>
		</j:SupervisionFineAmount>
	</xsl:template>
	<xsl:template match="SentenceDeferredDate">
		<ccq-res-ext:SentenceDeferredDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:SentenceDeferredDate>
	</xsl:template>
	<xsl:template match="SentenceConfinementCategoryText">
		<ccq-res-ext:SentenceConfinementCategoryText>
			<xsl:value-of select="."/>
		</ccq-res-ext:SentenceConfinementCategoryText>
	</xsl:template>
	<xsl:template match="SentenceConfinementComment">
		<ccq-res-ext:SentenceConfinementComment>
			<xsl:value-of select="."/>
		</ccq-res-ext:SentenceConfinementComment>
	</xsl:template>
	<xsl:template match="SentenceWorkReleaseIndicator">
		<ccq-res-ext:SentenceWorkReleaseIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:SentenceWorkReleaseIndicator>
	</xsl:template>
	<xsl:template match="ChargeSequenceID">
		<j:ChargeSequenceID>
			<xsl:value-of select="."/>
		</j:ChargeSequenceID>
	</xsl:template>
	<xsl:template match="OrganizationJurisdiction">
		<j:OrganizationJurisdiction>
			<nc:JurisdictionText>
				<xsl:value-of select="."/>
			</nc:JurisdictionText>
		</j:OrganizationJurisdiction>
	</xsl:template>
	<xsl:template match="ActivityDescriptionText">
		<nc:ActivityDescriptionText>
			<xsl:value-of select="."/>
		</nc:ActivityDescriptionText>
	</xsl:template>
	<xsl:template match="ActivityName">
		<nc:ActivityName>
			<xsl:value-of select="."/>
		</nc:ActivityName>
	</xsl:template>
	<xsl:template match="CourtEventJudge">
		<j:CourtEventJudge>
			<xsl:apply-templates select="RoleOfPerson/PersonName/PersonFullName[normalize-space(.)]"/>
		</j:CourtEventJudge>
	</xsl:template>
	<xsl:template match="CommentsForCourtClerk">
		<ccq-res-ext:CommentsForCourtClerk>
			<xsl:value-of select="."/>
		</ccq-res-ext:CommentsForCourtClerk>
	</xsl:template>
	<xsl:template match="CourtEventCommentsText">
		<ccq-res-ext:CourtEventCommentsText>
			<xsl:value-of select="."/>
		</ccq-res-ext:CourtEventCommentsText>
	</xsl:template>
	<xsl:template match="CaseDefendantSelfRepresentationIndicator">
		<j:CaseDefendantSelfRepresentationIndicator>
			<xsl:value-of select="."/>
		</j:CaseDefendantSelfRepresentationIndicator>
	</xsl:template>
	<xsl:template match="EntityOrganization">
		<nc:EntityOrganization>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</nc:EntityOrganization>
	</xsl:template>
	<xsl:template match="EntityPerson">
		<nc:EntityPerson>
			<xsl:attribute name="structures:ref"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
		</nc:EntityPerson>
	</xsl:template>
	<xsl:template match="CaseDomesticViolenceIndicator">
		<j:CaseDomesticViolenceIndicator>
			<xsl:value-of select="."/>
		</j:CaseDomesticViolenceIndicator>
	</xsl:template>
	<xsl:template match="ActivityCategoryText">
		<nc:ActivityCategoryText>
			<xsl:value-of select="."/>
		</nc:ActivityCategoryText>
	</xsl:template>
	<xsl:template match="StartDate">
		<nc:StartDate>
			<nc:DateTime>
				<xsl:value-of select="."/>
			</nc:DateTime>
		</nc:StartDate>
	</xsl:template>
	<xsl:template match="EndDate">
		<nc:EndDate>
			<nc:DateTime>
				<xsl:value-of select="."/>
			</nc:DateTime>
		</nc:EndDate>
	</xsl:template>
	<xsl:template match="ActivityReasonText">
		<nc:ActivityReasonText>
			<xsl:value-of select="."/>
		</nc:ActivityReasonText>
	</xsl:template>
	<xsl:template match="CourtEventAppearance/CourtAppearanceCourt/CourtName">
		<j:CourtEventAppearance>
			<j:CourtAppearanceCourt>
				<j:CourtName>
					<xsl:value-of select="."/>
				</j:CourtName>
			</j:CourtAppearanceCourt>
		</j:CourtEventAppearance>
	</xsl:template>
	<xsl:template match="RoleOfPerson/PersonName/PersonFullName">
		<nc:RoleOfPerson>
			<nc:PersonName>
				<nc:PersonFullName>
					<xsl:value-of select="."/>
				</nc:PersonFullName>
			</nc:PersonName>
		</nc:RoleOfPerson>
	</xsl:template>
	<xsl:template match="JudicialOfficialBarMembership">
		<j:JudicialOfficialBarMembership>
			<j:JudicialOfficialBarIdentification>
				<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
			</j:JudicialOfficialBarIdentification>
		</j:JudicialOfficialBarMembership>
	</xsl:template>
	<xsl:template match="CaseLineageCase/CaseTrackingID">
		<j:CaseLineageCase>
			<nc:CaseTrackingID>
				<xsl:value-of select="."/>
			</nc:CaseTrackingID>
		</j:CaseLineageCase>
	</xsl:template>
	<xsl:template match="IdentificationCategoryDescriptionText">
		<nc:IdentificationCategoryDescriptionText>
			<xsl:value-of select="."/>
		</nc:IdentificationCategoryDescriptionText>
	</xsl:template>
	<xsl:template match="TrialByText">
		<ccq-res-ext:TrialByText>
			<xsl:value-of select="."/>
		</ccq-res-ext:TrialByText>
	</xsl:template>
	<xsl:template match="CaseSealedIndicator">
		<ccq-res-ext:CaseSealedIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:CaseSealedIndicator>
	</xsl:template>
	<xsl:template match="RemandDate">
		<ccq-res-ext:RemandDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:RemandDate>
	</xsl:template>
	<xsl:template match="CaseUnderAdvisementDate">
		<ccq-res-ext:CaseUnderAdvisementDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:CaseUnderAdvisementDate>
	</xsl:template>
	<xsl:template match="JuryRequestedIndicator">
		<ccq-res-ext:JuryRequestedIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:JuryRequestedIndicator>
	</xsl:template>
	<xsl:template match="JuryVerdictIndicator">
		<ccq-res-ext:JuryVerdictIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:JuryVerdictIndicator>
	</xsl:template>
	<xsl:template match="FailureToPayHoldIndefiniteIndicator">
		<ccq-res-ext:FailureToPayHoldIndefiniteIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:FailureToPayHoldIndefiniteIndicator>
	</xsl:template>
	<xsl:template match="FailureToPayHoldDate">
		<ccq-res-ext:FailureToPayHoldDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:FailureToPayHoldDate>
	</xsl:template>
	<xsl:template match="FailureToAppearHoldDate">
		<ccq-res-ext:FailureToAppearHoldDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:FailureToAppearHoldDate>
	</xsl:template>
	<xsl:template match="FailureToComplyHoldDate">
		<ccq-res-ext:FailureToComplyHoldDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:FailureToComplyHoldDate>
	</xsl:template>
	<xsl:template match="FailureToPayVictimHoldDate">
		<ccq-res-ext:FailureToPayVictimHoldDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</ccq-res-ext:FailureToPayVictimHoldDate>
	</xsl:template>
	<xsl:template match="PersonBirthDate">
		<nc:PersonBirthDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="PersonGivenName[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonMiddleName[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonSurName[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonNameSuffixText[normalize-space(.)]"/>
			<xsl:apply-templates select="PersonFullName[normalize-space(.)]"/>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="PersonGivenName">
		<nc:PersonGivenName>
			<xsl:value-of select="."/>
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="PersonMiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="."/>
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="PersonSurName">
		<nc:PersonSurName>
			<xsl:value-of select="."/>
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="PersonNameSuffixText">
		<nc:PersonNameSuffixText>
			<xsl:value-of select="."/>
		</nc:PersonNameSuffixText>
	</xsl:template>
	<xsl:template match="PersonFullName">
		<nc:PersonFullName>
			<xsl:value-of select="."/>
		</nc:PersonFullName>
	</xsl:template>
	<xsl:template match="PersonSSNIdentification">
		<nc:PersonSSNIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="AddressSecondaryUnitText">
		<nc:AddressSecondaryUnitText>
			<xsl:value-of select="."/>
		</nc:AddressSecondaryUnitText>
	</xsl:template>
	<xsl:template match="AddressDeliveryPointID">
		<nc:AddressDeliveryPointID>
			<xsl:value-of select="."/>
		</nc:AddressDeliveryPointID>
	</xsl:template>
	<xsl:template match="StreetName">
		<nc:StreetName>
			<xsl:value-of select="."/>
		</nc:StreetName>
	</xsl:template>
	<xsl:template match="StreetCategoryText">
		<nc:StreetCategoryText>
			<xsl:value-of select="."/>
		</nc:StreetCategoryText>
	</xsl:template>
	<xsl:template match="LocationCountyName">
		<nc:LocationCountyName>
			<xsl:value-of select="."/>
		</nc:LocationCountyName>
	</xsl:template>
	<xsl:template match="AddressCrossStreet/CrossStreetDescriptionText">
		<nc:AddressCrossStreet>
			<nc:CrossStreetDescriptionText>
				<xsl:value-of select="."/>
			</nc:CrossStreetDescriptionText>
		</nc:AddressCrossStreet>
	</xsl:template>
	<xsl:template match="LocationDescriptionText">
		<nc:LocationDescriptionText>
			<xsl:value-of select="."/>
		</nc:LocationDescriptionText>
	</xsl:template>
	<xsl:template match="LocationName">
		<nc:LocationName>
			<xsl:value-of select="."/>
		</nc:LocationName>
	</xsl:template>
	<xsl:template match="DrivingPointNumberText">
		<ccq-res-ext:DrivingPointNumberText>
			<xsl:value-of select="."/>
		</ccq-res-ext:DrivingPointNumberText>
	</xsl:template>
	<xsl:template match="DrivingPointNumberReducedText">
		<ccq-res-ext:DrivingPointNumberReducedText>
			<xsl:value-of select="."/>
		</ccq-res-ext:DrivingPointNumberReducedText>
	</xsl:template>
	<xsl:template match="PersonBloodAlcoholContentTestRefusedIndicator">
		<ccq-res-ext:PersonBloodAlcoholContentTestRefusedIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:PersonBloodAlcoholContentTestRefusedIndicator>
	</xsl:template>
	<xsl:template match="PersonBloodAlcoholContentNumberText">
		<j:PersonBloodAlcoholContentNumberText>
			<xsl:value-of select="."/>
		</j:PersonBloodAlcoholContentNumberText>
	</xsl:template>
	<xsl:template match="CourtAppearanceDate">
		<j:CourtAppearanceDate>
			<xsl:apply-templates select="DateTime[normalize-space(.)]"/>
		</j:CourtAppearanceDate>
	</xsl:template>
	<xsl:template match="OffenseDomesticViolenceIndicator">
		<j:OffenseDomesticViolenceIndicator>
			<xsl:value-of select="."/>
		</j:OffenseDomesticViolenceIndicator>
	</xsl:template>
	<xsl:template match="VehicleOverweightMeasure/MeasureValueText">
		<ccq-res-ext:VehicleOverweightMeasure>
			<xsl:apply-templates select="MeasureValueText[normalize-space(.)]"/>
		</ccq-res-ext:VehicleOverweightMeasure>
	</xsl:template>
	<xsl:template match="CourtAppearanceRequiredIndicator">
		<ccq-res-ext:CourtAppearanceRequiredIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:CourtAppearanceRequiredIndicator>
	</xsl:template>
	<xsl:template match="InjuryIndicator">
		<ccq-res-ext:InjuryIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:InjuryIndicator>
	</xsl:template>
	<xsl:template match="SchoolZoneIndicator">
		<ccq-res-ext:SchoolZoneIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:SchoolZoneIndicator>
	</xsl:template>
	<xsl:template match="ConstructionZoneIndicator">
		<ccq-res-ext:ConstructionZoneIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:ConstructionZoneIndicator>
	</xsl:template>
	<xsl:template match="LicenseSurrenderedIndicator">
		<ccq-res-ext:LicenseSurrenderedIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:LicenseSurrenderedIndicator>
	</xsl:template>
	<xsl:template match="FatalityIndicator">
		<ccq-res-ext:FatalityIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:FatalityIndicator>
	</xsl:template>
	<xsl:template match="HazardousMaterialsIndicator">
		<ccq-res-ext:HazardousMaterialsIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:HazardousMaterialsIndicator>
	</xsl:template>
	<xsl:template match="NuclearMaterialsIndicator">
		<ccq-res-ext:NuclearMaterialsIndicator>
			<xsl:value-of select="."/>
		</ccq-res-ext:NuclearMaterialsIndicator>
	</xsl:template>
	<xsl:template match="ConveyanceColorPrimaryCode">
		<j:ConveyanceColorPrimaryCode>
			<xsl:value-of select="."/>
		</j:ConveyanceColorPrimaryCode>
	</xsl:template>
	<xsl:template match="VehicleCMVIndicator">
		<nc:VehicleCMVIndicator>
			<xsl:value-of select="."/>
		</nc:VehicleCMVIndicator>
	</xsl:template>
	<xsl:template match="VehicleIdentification">
		<nc:VehicleIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:VehicleIdentification>
	</xsl:template>
	<xsl:template match="VehicleMakeCode">
		<j:VehicleMakeCode>
			<xsl:value-of select="."/>
		</j:VehicleMakeCode>
	</xsl:template>
	<xsl:template match="VehicleModelCode">
		<j:VehicleModelCode>
			<xsl:value-of select="."/>
		</j:VehicleModelCode>
	</xsl:template>
	<xsl:template match="ObligationDueAmount">
		<nc:ObligationDueAmount>
			<xsl:apply-templates select="Amount[normalize-space(.)]"/>
		</nc:ObligationDueAmount>
	</xsl:template>
	<xsl:template match="ObligationPaidAmount">
		<nc:ObligationPaidAmount>
			<xsl:apply-templates select="Amount[normalize-space(.)]"/>
		</nc:ObligationPaidAmount>
	</xsl:template>
	<xsl:template match="PersonDescriptionText">
		<nc:PersonDescriptionText>
			<xsl:value-of select="."/>
		</nc:PersonDescriptionText>
	</xsl:template>
	<xsl:template match="PersonEthnicityText">
		<nc:PersonEthnicityText>
			<xsl:value-of select="."/>
		</nc:PersonEthnicityText>
	</xsl:template>
	<xsl:template match="PersonEyeColorCode">
		<j:PersonEyeColorCode>
			<xsl:value-of select="."/>
		</j:PersonEyeColorCode>
	</xsl:template>
	<xsl:template match="PersonHairColorCode">
		<j:PersonHairColorCode>
			<xsl:value-of select="."/>
		</j:PersonHairColorCode>
	</xsl:template>
	<xsl:template match="PersonHeightMeasure">
		<nc:PersonHeightMeasure>
			<xsl:apply-templates select="MeasureUnitText[normalize-space(.)]"/>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="PersonOtherIdentification">
		<nc:PersonOtherIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</nc:PersonOtherIdentification>
	</xsl:template>
	<xsl:template match="PhysicalFeatureDescriptionText">
		<nc:PersonPhysicalFeature>
			<nc:PhysicalFeatureDescriptionText>
				<xsl:value-of select="."/>
			</nc:PhysicalFeatureDescriptionText>
		</nc:PersonPhysicalFeature>
	</xsl:template>
	<xsl:template match="PersonPrimaryLanguage">
		<nc:PersonPrimaryLanguage>
			<nc:LanguageName>
				<xsl:value-of select="."/>
			</nc:LanguageName>
		</nc:PersonPrimaryLanguage>
	</xsl:template>
	<xsl:template match="PersonRaceText">
		<nc:PersonRaceText>
			<xsl:value-of select="."/>
		</nc:PersonRaceText>
	</xsl:template>
	<xsl:template match="PersonRaceText">
		<nc:PersonSexText>
			<xsl:value-of select="."/>
		</nc:PersonSexText>
	</xsl:template>
	<xsl:template match="PersonWeightMeasure">
		<nc:PersonWeightMeasure>
			<xsl:apply-templates select="MeasureValueText[normalize-space(.)]"/>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="DriverLicenseIdentification">
		<j:DriverLicenseIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
			<xsl:apply-templates select="IdentificationSourceText[normalize-space(.)]"/>
		</j:DriverLicenseIdentification>
	</xsl:template>
	<xsl:template match="DriverLicenseExpirationDate">
		<j:DriverLicenseExpirationDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</j:DriverLicenseExpirationDate>
	</xsl:template>
	<xsl:template match="DriverLicenseIssuedDate">
		<j:DriverLicenseIssueDate>
			<xsl:apply-templates select="Date[normalize-space(.)]"/>
		</j:DriverLicenseIssueDate>
	</xsl:template>
	<xsl:template match="DriverLicenseCommercialClassText">
		<j:DriverLicenseCommercialClassText>
			<xsl:value-of select="."/>
		</j:DriverLicenseCommercialClassText>
	</xsl:template>
	<xsl:template match="DriverLicenseNonCommercialClassText">
		<j:DriverLicenseNonCommercialClassText>
			<xsl:value-of select="."/>
		</j:DriverLicenseNonCommercialClassText>
	</xsl:template>
	<xsl:template match="IdentificationSourceText">
		<nc:IdentificationSourceText>
			<xsl:value-of select="."/>
		</nc:IdentificationSourceText>
	</xsl:template>
	<xsl:template match="PersonFBIIdentification">
		<j:PersonFBIIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</j:PersonFBIIdentification>
	</xsl:template>
	<xsl:template match="PersonFBIIdentification">
		<j:PersonFBIIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</j:PersonFBIIdentification>
	</xsl:template>
	<xsl:template match="PersonStateFingerprintIdentification">
		<j:PersonStateFingerprintIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</j:PersonStateFingerprintIdentification>
	</xsl:template>
	<xsl:template match="StudentIdentification">
		<cyfs:StudentIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</cyfs:StudentIdentification>
	</xsl:template>
	<xsl:template match="PersonInmateIdentification">
		<ccq-res-ext:PersonInmateIdentification>
			<xsl:apply-templates select="IdentificationID[normalize-space(.)]"/>
		</ccq-res-ext:PersonInmateIdentification>
	</xsl:template>
	<xsl:template match="AddressSecondaryUnitText">
		<nc:AddressSecondaryUnitText>
			<xsl:value-of select="."/>
		</nc:AddressSecondaryUnitText>
	</xsl:template>
	<xsl:template match="LocationStreet">
		<nc:LocationStreet>
			<xsl:apply-templates select="StreetFullText[normalize-space(.)]"/>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="StreetFullText">
		<nc:StreetFullText>
			<xsl:value-of select="."/>
		</nc:StreetFullText>
	</xsl:template>
	<xsl:template match="LocationCityName">
		<nc:LocationCityName>
			<xsl:value-of select="."/>
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="LocationStateName">
		<nc:LocationStateName>
			<xsl:value-of select="."/>
		</nc:LocationStateName>
	</xsl:template>
	<xsl:template match="LocationPostalCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="."/>
		</nc:LocationPostalCode>
	</xsl:template>
	<xsl:template match="LocationCategoryText">
		<nc:LocationCategoryText>
			<xsl:value-of select="."/>
		</nc:LocationCategoryText>
	</xsl:template>
	<xsl:template match="MeasureValueText">
		<nc:MeasureValueText>
			<xsl:value-of select="."/>
		</nc:MeasureValueText>
	</xsl:template>
	<xsl:template match="MeasureUnitText">
		<nc:MeasureUnitText>
			<xsl:value-of select="."/>
		</nc:MeasureUnitText>
	</xsl:template>
	<xsl:template match="Amount">
		<nc:Amount>
			<xsl:value-of select="."/>
		</nc:Amount>
	</xsl:template>
	<xsl:template match="ObligationPaidDate">
		<nc:ObligationPaidDate>
			<xsl:apply-templates select="DateTime[normalize-space(.)]"/>
		</nc:ObligationPaidDate>
	</xsl:template>
</xsl:stylesheet>
