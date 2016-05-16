<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:nibrs="http://fbi.gov/cjis/nibrs/4.0"
	xmlns:nibrs-ext="http://nibrs.org/IEPD/Extensions/NIBRSStructuredPayload/1.0"
	xmlns:nibrscodes="http://fbi.gov/cjis/nibrs/nibrs-codes/4.0"
	xmlns:cjis="http://fbi.gov/cjis/1.0" xmlns:cjiscodes="http://fbi.gov/cjis/cjis-codes/1.0"
	xmlns:i="http://release.niem.gov/niem/appinfo/3.0/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" xmlns:niem-xsd="http://release.niem.gov/niem/proxy/xsd/3.0/"
	xmlns:s="http://release.niem.gov/niem/structures/3.0/" xmlns:nc20="http://niem.gov/niem/niem-core/2.0"
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1"
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:s20="http://niem.gov/niem/structures/2.0"
	xmlns:j40="http://niem.gov/niem/domains/jxdm/4.0" xmlns:j41="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1" xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:variable name="DIGEST"
		select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest" />
	<xsl:variable name="NDEXIA"
		select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport" />
	<xsl:variable name="NDEX-EXT"
		select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/nibrs-ext:IncidentReport" />
	<xsl:template match="/">
		<nibrs:Report>
			<xsl:apply-templates select="." mode="header" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityActivity/nc20:Activity[nc20:ActivityCategoryText='Incident']"
				mode="incident" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityActivity/nc20:Activity[nc20:ActivityCategoryText='Offense']"
				mode="offense" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityLocation/nc20:Location" mode="offense_location" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityTangibleItem/nc20:TangibleItem"
				mode="item" />
			<xsl:apply-templates select="$DIGEST/lexsdigest:EntityDrug/nc20:Drug"
				mode="substance" />
			<xsl:apply-templates select="$DIGEST/lexsdigest:EntityPerson"
				mode="person" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityPerson/j40:EnforcementOfficial"
				mode="official" />
			<xsl:apply-templates select="$DIGEST/lexsdigest:EntityPerson/j40:Victim"
				mode="victim" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityPerson/j40:IncidentSubject" mode="subject" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityPerson/j40:ArrestSubject" mode="arrestee" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityActivity/nc20:Activity[nc20:ActivityCategoryText='Arrest']"
				mode="arrest" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:Associations/lexsdigest:ArrestSubjectAssociation"
				mode="arrest_subject_association" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:Associations/lexsdigest:OffenseLocationAssociation"
				mode="offense_location_association" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:Associations/lexsdigest:OffenseVictimPersonAssociation"
				mode="offense_victim_association" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:Associations/lexsdigest:SubjectVictimAssociation"
				mode="subject_victim_association" />
		</nibrs:Report>
	</xsl:template>
	<!-- Header -->
	<xsl:template match="/" mode="header">
		<nibrs:ReportHeader>
			<xsl:apply-templates select="$NDEXIA/ndexia:NIBRSReportCategoryCode" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityOrganization/lexsdigest:Metadata/nc20:ReportedDate/nc20:YearMonth" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Organization/j40:OrganizationAugmentation/j40:OrganizationORIIdentification"
				mode="reporting" />
		</nibrs:ReportHeader>
	</xsl:template>
	<!-- Incident -->
	<xsl:template match="nc20:Activity" mode="incident">
		<xsl:variable name="activityID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<nc:Incident>
			<!--xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute -->
			<!-- Element 2, Incident Number -->
			<xsl:apply-templates select="nc20:ActivityIdentification" />
			<!-- Element 3, Incident Date and Hour -->
			<xsl:apply-templates select="nc20:ActivityDate/nc20:DateTime" />
			<xsl:apply-templates select="$NDEXIA/ndexia:Offense"
				mode="cargo" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Incident[ndexia:IncidentAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/j40:IncidentAugmentation"
				mode="incident_aug" />
		</nc:Incident>
	</xsl:template>
	<!-- Offense -->
	<xsl:template match="nc20:Activity" mode="offense">
		<xsl:variable name="activityID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<j:Offense>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/ndexia:OffenseCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/ndexia:CriminalActivityCategoryCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/ndexia:OffenseBiasMotivationCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/j40:IncidentStructuresEnteredQuantity" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/j40:IncidentFactor/j40:IncidentFactorCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/ndexia:OffenseEntryPoint/j40:PassagePointMethodCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/j40:IncidentForce/j40:ForceCategoryCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$activityID]/ndexia:OffenseCompletedIndicator" />
		</j:Offense>
	</xsl:template>
	<!-- Location -->
	<xsl:template match="nc20:Location" mode="offense_location">
		<xsl:variable name="locationID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<nc:Location>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Location[ndexia:LocationAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$locationID]/ndexia:LocationCategoryCode" />
		</nc:Location>
	</xsl:template>
	<!-- Item -->
	<xsl:template match="lexsdigest:EntityTangibleItem/nc20:TangibleItem"
		mode="item">
		<xsl:variable name="itemID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<nc:Item>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:TangibleItem[ndexia:TangibleItemAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$itemID]/ndexia:TangibleItemAugmentation/ndexia:ItemQuantityStatusValue/ndexia:ItemStatus/ndexia:ItemStatusAugmentation/ndexia:ItemStatusCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:TangibleItem[ndexia:TangibleItemAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$itemID]/ndexia:TangibleItemAugmentation/ndexia:ItemQuantityStatusValue" />
			<xsl:apply-templates
				select="$NDEX-EXT/nibrs-ext:Property/j41:PropertyCategoryNIBRSPropertyCategoryCode" />
<xsl:apply-templates
				select="$NDEXIA/ndexia:TangibleItem[ndexia:TangibleItemAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$itemID]/ndexia:TangibleItemAugmentation/ndexia:ItemQuantityStatusValue/nc20:ItemQuantity" />
		</nc:Item>
	</xsl:template>
	<!-- Substance -->
	<xsl:template match="lexsdigest:EntityDrug/nc20:Drug"
		mode="substance">
		<xsl:variable name="drugID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<nc:Substance>
			<xsl:apply-templates select="j40:DrugDEACode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Drug[ndexia:DrugAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$drugID]/ndexia:DrugAugmentation/ndexia:ItemQuantityStatusValue/nc20:SubstanceQuantityMeasure" />
		</nc:Substance>
	</xsl:template>
	<!-- PERSONS -->
	<xsl:template match="lexsdigest:EntityPerson" mode="person">
		<xsl:apply-templates select="lexsdigest:Person[../j40:Victim]"
			mode="person" />
		<xsl:apply-templates select="lexsdigest:Person[../j40:IncidentSubject]"
			mode="person" />
		<xsl:apply-templates select="lexsdigest:Person[../j40:ArrestSubject]"
			mode="person" />
	</xsl:template>
	<!-- Enforcement Official -->
	<xsl:template match="j40:EnforcementOfficial" mode="official">
		<xsl:variable name="officerID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<j:EnforcementOfficial>
			<nc:RoleOfPerson>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id(../lexsdigest:Person)" /></xsl:attribute>
			</nc:RoleOfPerson>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:EnforcementOfficial[ndexia:EnforcementOfficialAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$officerID]/ndexia:EnforcementOfficialActivityCategoryCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:EnforcementOfficial[ndexia:EnforcementOfficialAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$officerID]/j40:EnforcementOfficialAssignmentCategoryCode" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityOrganization/nc20:Organization"
				mode="unit" />
		</j:EnforcementOfficial>
	</xsl:template>
	<!-- Victim -->
	<xsl:template match="j40:Victim" mode="victim">
		<xsl:variable name="personID">
			<xsl:value-of select="nc20:RoleOfPersonReference/@s20:ref" />
		</xsl:variable>
		<j:Victim>
			<nc:RoleOfPerson>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id(../lexsdigest:Person)" /></xsl:attribute>
			</nc:RoleOfPerson>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Victim[ndexia:VictimAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:VictimAugmentation/ndexia:VictimSequenceNumberText" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Victim[ndexia:VictimAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/j40:VictimCategoryCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Victim[ndexia:VictimAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:VictimAugmentation/ndexia:VictimAggravatedAssaultHomicideFactorCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Victim[ndexia:VictimAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/j40:VictimJustifiableHomicideFactorCode" />
		</j:Victim>
	</xsl:template>
	<!-- Incident Subject -->
	<xsl:template match="j40:IncidentSubject" mode="subject">
		<xsl:variable name="personID">
			<xsl:value-of select="nc20:RoleOfPersonReference/@s20:ref" />
		</xsl:variable>
		<j:Subject>
			<nc:RoleOfPerson>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id(../lexsdigest:Person)" /></xsl:attribute>
			</nc:RoleOfPerson>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Subject[ndexia:SubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:SubjectAugmentation/ndexia:SubjectSequenceNumberText" />
		</j:Subject>
	</xsl:template>
	<!-- Arrest Subject -->
	<xsl:template match="j40:ArrestSubject" mode="arrestee">
		<xsl:variable name="personID">
			<xsl:value-of select="nc20:RoleOfPersonReference/@s20:ref" />
		</xsl:variable>
		<j:Arrestee>
			<nc:RoleOfPerson>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id(../lexsdigest:Person)" /></xsl:attribute>
			</nc:RoleOfPerson>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:ArrestSubjectAugmentation/j40:ArrestSequenceID" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:ArrestSubjectAugmentation/ndexia:ArresteeClearanceIndicator" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:ArrestSubjectAugmentation/ndexia:ArresteeArmedWithCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:ArrestSubjectAugmentation/ndexia:ArresteeJuvenileDispositionCode" />
		</j:Arrestee>
	</xsl:template>
	<!-- Arrest -->
	<xsl:template match="nc20:Activity" mode="arrest">
		<xsl:variable name="activityID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<j:Arrest>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<!-- Element 41, Arrest Transaction Number -->
			<xsl:apply-templates select="nc20:ActivityIdentification" />
			<!-- Element 42, Arrest Date -->
			<xsl:apply-templates select="nc20:ActivityDate" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityPerson/j40:ArrestSubject" mode="arrest" />
		</j:Arrest>
	</xsl:template>
	<!-- Arrest - Arrest Subject -->
	<xsl:template match="j40:ArrestSubject" mode="arrest">
		<xsl:variable name="personID">
			<xsl:value-of select="nc20:RoleOfPersonReference/@s20:ref" />
		</xsl:variable>
		<xsl:apply-templates
			select="$NDEXIA/ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:ArrestSubjectAugmentation/j40:ChargeUCRCode" />
		<xsl:apply-templates
			select="$NDEXIA/ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/../ndexia:Arrest/j40:ArrestCategoryCode" />
		<xsl:apply-templates
			select="$NDEXIA/ndexia:ArrestSubject[ndexia:ArrestSubjectAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:ArrestSubjectAugmentation/j40:ArrestSubjectCountCode" />
	</xsl:template>
	<!-- Associations -->
	<xsl:template match="lexsdigest:ArrestSubjectAssociation"
		mode="arrest_subject_association">
		<j:ArrestSubjectAssociation>
			<xsl:variable name="activityID">
				<xsl:value-of select="nc20:ActivityReference/@s20:ref" />
			</xsl:variable>
			<xsl:variable name="personID">
				<xsl:value-of select="nc20:PersonReference/@s20:ref" />
			</xsl:variable>
			<nc:Activity>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityActivity/nc20:Activity[@s20:id=$activityID])" /></xsl:attribute>
			</nc:Activity>
			<j:Subject>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityPerson/lexsdigest:Person[@s20:id=$personID])" /></xsl:attribute>
			</j:Subject>
		</j:ArrestSubjectAssociation>
	</xsl:template>
	<xsl:template match="lexsdigest:OffenseLocationAssociation"
		mode="offense_location_association">
		<xsl:variable name="activityID">
			<xsl:value-of select="nc20:ActivityReference/@s20:ref" />
		</xsl:variable>
		<xsl:variable name="locationID">
			<xsl:value-of select="nc20:LocationReference/@s20:ref" />
		</xsl:variable>
		<j:OffenseLocationAssociation>
			<j:Offense>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityActivity/nc20:Activity[@s20:id=$activityID])" /></xsl:attribute>
			</j:Offense>
			<nc:Location>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityLocation/nc20:Location[@s20:id=$locationID])" /></xsl:attribute>
			</nc:Location>
		</j:OffenseLocationAssociation>
	</xsl:template>
	<xsl:template match="lexsdigest:OffenseVictimPersonAssociation"
		mode="offense_victim_association">
		<xsl:variable name="activityID">
			<xsl:value-of select="nc20:ActivityReference/@s20:ref" />
		</xsl:variable>
		<xsl:variable name="personID">
			<xsl:value-of select="nc20:PersonReference/@s20:ref" />
		</xsl:variable>
		<j:OffenseVictimAssociation>
			<j:Offense>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityActivity/nc20:Activity[@s20:id=$activityID])" /></xsl:attribute>
			</j:Offense>
			<j:Victim>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityPerson/lexsdigest:Person[@s20:id=$personID])" /></xsl:attribute>
			</j:Victim>
		</j:OffenseVictimAssociation>
	</xsl:template>
	<xsl:template match="lexsdigest:SubjectVictimAssociation"
		mode="subject_victim_association">
		<xsl:variable name="svaID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<xsl:variable name="subjectID">
			<xsl:value-of select="lexsdigest:SubjectPersonReference/@s20:ref" />
		</xsl:variable>
		<xsl:variable name="victimID">
			<xsl:value-of select="lexsdigest:VictimPersonReference/@s20:ref" />
		</xsl:variable>
		<j:SubjectVictimAssociation>
			<j:Subject>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityPerson/lexsdigest:Person[@s20:id=$subjectID])" /></xsl:attribute>
			</j:Subject>
			<j:Victim>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id($DIGEST/lexsdigest:EntityPerson/lexsdigest:Person[@s20:id=$victimID])" /></xsl:attribute>
			</j:Victim>
			<xsl:apply-templates
				select="$NDEXIA/ndexia:SubjectVictimAssociation[ndexia:SubjectVictimAssociationAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$svaID]/ndexia:SubjectVictimAssociationAugmentation/ndexia:VictimToSubjectRelationshipCode" />
		</j:SubjectVictimAssociation>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="lexsdigest:Person" mode="person">
		<xsl:variable name="personID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<nc:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates select="nc20:PersonAgeMeasure" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Person[ndexia:PersonAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/nc20:PersonEthnicityCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Victim[ndexia:VictimAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:VictimInjury/ndexia:InjuryAugmentation/ndexia:InjuryCategoryCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Person[ndexia:PersonAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/ndexia:PersonAugmentation/ndexia:PersonRaceCode" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Person[ndexia:PersonAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/nc20:PersonResidentCode" />
			<xsl:apply-templates select="nc20:PersonSexCode" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="ndexia:Offense" mode="cargo">
		<cjis:IncidentAugmentation>
			<xsl:apply-templates select="." mode="date_indicator" />
			<xsl:apply-templates select="ndexia:OffenseCargoTheftIndicator" />
		</cjis:IncidentAugmentation>
	</xsl:template>
	<xsl:template match="j40:IncidentAugmentation" mode="incident_aug">
		<j:IncidentAugmentation>
			<xsl:apply-templates select="j40:IncidentExceptionalClearanceCode" />
			<xsl:apply-templates select="j40:IncidentExceptionalClearanceDate" />
		</j:IncidentAugmentation>
	</xsl:template>
	<xsl:template match="ndexia:ItemQuantityStatusValue">
		<nc:ItemValue>
			<xsl:apply-templates select="ndexia:ItemValue/nc20:ItemValueAmount" />
			<xsl:apply-templates select="ndexia:ItemStatus/nc20:StatusDate" />
		</nc:ItemValue>
	</xsl:template>
	<xsl:template match="nc20:PersonAgeMeasure">
		<nc:PersonAgeMeasure>
			<xsl:apply-templates select="nc20:MeasurePointValue" />
			<xsl:apply-templates select="nc20:MeasureRangeValue" />
		</nc:PersonAgeMeasure>
	</xsl:template>
	<xsl:template match="nc20:SubstanceQuantityMeasure">
		<nc:SubstanceQuantityMeasure>
			<xsl:apply-templates select="nc20:MeasurePointValue"
				mode="quantity" />
			<xsl:apply-templates select="nc20:SubstanceUnitCode" />
		</nc:SubstanceQuantityMeasure>
	</xsl:template>
	<xsl:template match="nc20:Organization" mode="unit">
		<xsl:variable name="orgID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<xsl:apply-templates
			select="$NDEXIA/ndexia:EnforcementUnit[ndexia:EnforcementUnitAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$orgID]/j40:OrganizationAugmentation/j40:OrganizationORIIdentification"
			mode="unit" />
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="nc20:MeasureRangeValue">
		<nc:MeasureRangeValue>
			<xsl:apply-templates select="nc20:RangeMinimumValue" />
			<xsl:apply-templates select="nc20:RangeMaximumValue" />
		</nc:MeasureRangeValue>
	</xsl:template>
	<xsl:template match="ndexia:Offense" mode="date_indicator">
		<xsl:variable name="mesg_date">
			<xsl:value-of
				select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:PackageMetadata/lexs:DataItemDate" />
		</xsl:variable>
		<xsl:variable name="incid_date">
			<xsl:value-of
				select="$DIGEST/lexsdigest:EntityActivity/nc:Activity/nc:ActivityDate/nc:DateTime" />
		</xsl:variable>
		<xsl:if
			test="substring-before($mesg_date,'T')=substring-before($incid_date,'T')">
			<cjis:IncidentReportDateIndicator>true
			</cjis:IncidentReportDateIndicator>
		</xsl:if>
		<!-- FIX with 'else -->
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="ndexia:NIBRSReportCategoryCode">
		<nibrs:NIBRSReportCategoryCode>
			<xsl:value-of select="upper-case(.)" />	
		</nibrs:NIBRSReportCategoryCode>
		<nibrs:ReportActionCategoryCode>I</nibrs:ReportActionCategoryCode>
	</xsl:template>
	<xsl:template match="nc20:YearMonth">
		<nibrs:ReportDate>
			<nc:YearMonthDate>
				<xsl:value-of select="." />
			</nc:YearMonthDate>
		</nibrs:ReportDate>
	</xsl:template>
	<xsl:template match="j40:OrganizationORIIdentification"
		mode="reporting">
		<!-- Element 1, ORI Code -->
		<nibrs:ReportingAgency>
			<j:OrganizationAugmentation>
				<j:OrganizationORIIdentification>
					<xsl:apply-templates select="nc20:IdentificationID" />
				</j:OrganizationORIIdentification>
			</j:OrganizationAugmentation>
		</nibrs:ReportingAgency>
	</xsl:template>
	<xsl:template match="nc20:ActivityDate/nc20:DateTime">
		<nc:ActivityDate>
			<nc:DateTime>
				<xsl:value-of select="." />
			</nc:DateTime>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="nc20:IdentificationID">
		<nc:IdentificationID>
			<xsl:value-of select="." />
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="j40:IncidentExceptionalClearanceCode">
		<!-- Element 4, Cleared Exceptionally -->
		<j:IncidentExceptionalClearanceCode>
			<xsl:value-of select="." />
		</j:IncidentExceptionalClearanceCode>
	</xsl:template>
	<xsl:template match="j40:IncidentExceptionalClearanceDate">
		<!-- Element 5, Exceptional Clearance Date -->
		<j:IncidentExceptionalClearanceDate>
			<xsl:apply-templates select="nc20:Date" />
		</j:IncidentExceptionalClearanceDate>
	</xsl:template>
	<xsl:template match="nc20:Date">
		<nc:Date>
			<xsl:value-of select="." />
		</nc:Date>
	</xsl:template>
	<xsl:template match="ndexia:OffenseCode">
		<!-- Element 6, Offense Code -->
		<nibrs:OffenseUCRCode>
			<xsl:if test=".='Abduction_No ransom or assault'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Abortifacient'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Abortion'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Abortional act on other'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Abortional act on self'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Abortional Act_Submission To'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Abscond while on parole'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Abscond while on probation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Abuse_nonviolent'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Accessory_after the fact'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Accessory_before the fact'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Accidental death'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Accosting'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Adulterated_food_drug_cosmetics'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Adultery'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_family_gun'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_family_strongarm'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_family_weapon'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_gun'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_nonfamily_gun'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_nonfamily_strongarm'">
				<xsl:value-of select="' 13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_nonfamily_weapon'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_police officer_gun'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_police officer_strongarm'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_police officer_weapon'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_public official_gun'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_public official_strongarm'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_public official_weapon'">
				<xsl:value-of select="' 13A'" />
			</xsl:if>
			<xsl:if test=".='Aggravated Assault_weapon'">
				<xsl:value-of select="'13A'" />
			</xsl:if>
			<xsl:if test=".='Aiding and Abetting'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Aiding Prisoner Escape'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Anarchism'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Antitrust'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Arson'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_burning of'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_business'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_business_defraud insurer'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_business_endangered life'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_public_building'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_public_building_endangered life'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_residential'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_residential_defraud insurer'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Arson_residential_endangered life'">
				<xsl:value-of select="'200'" />
			</xsl:if>
			<xsl:if test=".='Assault'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Assembly_Unlawful'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Bad Checks'">
				<xsl:value-of select="'90A'" />
			</xsl:if>
			<xsl:if test=".='Bail_personal recognizance'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Bail_secured bond'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Bestiality'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Betting_Wagering'">
				<xsl:value-of select="'39A'" />
			</xsl:if>
			<xsl:if test=".='Bigamy'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Blue Law Violations'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Boating Law Violations'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Bribe'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribe_giving'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribe_offering'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribe_receiving'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Gratuity'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Gratuity_giving'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Gratuity_offering'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Gratuity_receiving'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Kickback'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Kickback_giving'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Kickback_offering'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Bribery_Kickback_receiving'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Buggery'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Burglary'">
				<xsl:value-of select="'220'" />
			</xsl:if>
			<xsl:if test=".='Burglary Tools Possession'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Burglary_banking type institution'">
				<xsl:value-of select="'220'" />
			</xsl:if>
			<xsl:if test=".='Burglary_forced entry_nonresidence'">
				<xsl:value-of select="'220'" />
			</xsl:if>
			<xsl:if test=".='Burglary_forced entry_residence'">
				<xsl:value-of select="'220'" />
			</xsl:if>
			<xsl:if test=".='Burglary_no forced entry_nonresidence'">
				<xsl:value-of select="'220'" />
			</xsl:if>
			<xsl:if test=".='Burglary_no forced entry_residence'">
				<xsl:value-of select="'220'" />
			</xsl:if>
			<xsl:if test=".='Burglary_safe_vault'">
				<xsl:value-of select="'220'" />
			</xsl:if>
			<xsl:if test=".='Canvassing_illegal'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Carjacking_armed'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Civil Rights'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Combinations in Restraint of Trade'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Commercial Sex'">
				<xsl:value-of select="'40B'" />
			</xsl:if>
			<xsl:if test=".='Commercial Sex_homosexual prostitution'">
				<xsl:value-of select="'40A'" />
			</xsl:if>
			<xsl:if test=".='Commercialized Vice'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Compounding Crime'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Computer_Physical Harm to Persons or Property'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Computer_Interference With Government Function'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conditional Release Violation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conflict Of Interest'">
				<xsl:value-of select="'510'" />
			</xsl:if>
			<xsl:if test=".='Consensual Sodomy'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conservation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conservation_animals'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conservation_birds'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conservation_environment'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conservation_fish'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conservation_license_stamp'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Conspiracy to Commit'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Contempt of Congress'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Contempt of Court'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Contempt of Legislature'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Contributing Delinquency Minor'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Corrupt Conduct by Juror'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cosmetics Misbranded'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cosmetics_adulterated'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cosmetics_health or safety'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Counterfeited_Pass'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Counterfeited_Possessing'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Counterfeited_Transporting'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Counterfeiting'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Counterfeiting Of'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Counterfeiting_Forgery'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Crimes Against Person'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Criminal Defamation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Criminal Libel'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Criminal Slander'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Crossing Police Lines'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cruelty To Animals'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cruelty Toward Disabled'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cruelty Toward Elderly'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Curfew_Loitering_Vagrancy Violations'">
				<xsl:value-of select="'90B'" />
			</xsl:if>
			<xsl:if test=".='Damage Property'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Damage Property_business'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Damage Property_business_with explosive'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Damage Property_private'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Damage Property_private_with explosive'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Damage Property_public'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Damage Property_public_with explosive'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Desecrating Flag'">
				<xsl:value-of select="'90C'" />
			</xsl:if>
			<xsl:if test=".='Destruction_Damage_Vandalism of Property'">
				<xsl:value-of select="'290'" />
			</xsl:if>
			<xsl:if test=".='Disinterment Unlawful'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Disorderly Conduct'">
				<xsl:value-of select="'90C'" />
			</xsl:if>
			<xsl:if test=".='Divulge Eavesdrop Order'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Divulge Message Contents'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Divulge_Eavesdrop Information'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Driving Under Influence'">
				<xsl:value-of select="'90D'" />
			</xsl:if>
			<xsl:if test=".='Driving Under Influence_drugs'">
				<xsl:value-of select="'90D'" />
			</xsl:if>
			<xsl:if test=".='Driving Under Influence_liquor'">
				<xsl:value-of select="'90D'" />
			</xsl:if>
			<xsl:if test=".='Drug_Equipment Violations'">
				<xsl:value-of select="'35B'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic Equipment_possession'">
				<xsl:value-of select="'35B'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic Violations'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Amphetamine'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Amphetamine Manufacture'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Amphetamine Possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Amphetamine Selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Barbiturate'">
				<xsl:value-of select="' 35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Barbiturate Manufacture'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Barbiturate Possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Barbiturate Selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Cocaine'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Cocaine Possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Cocaine Selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Cocaine Smuggle'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Dangerous Drugs'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Hallucinogen'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Hallucinogen_distributing'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Hallucinogen_manufacturing'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Hallucinogen_possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Hallucinogen_selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Heroin'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Heroin_possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Heroin_selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Heroin_smuggle'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Marijuana'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Marijuana_possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Marijuana_producing'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Marijuana_selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Marijuana_smuggle'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Opium or Derivative'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Opium or Derivative_possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Opium or Derivative_selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Opium or Derivative_smuggle'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Synthetic Narcotic'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Synthetic Narcotic_possession'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Synthetic Narcotic_selling'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drug_Narcotic_Synthetic Narcotic_smuggle'">
				<xsl:value-of select="'35A'" />
			</xsl:if>
			<xsl:if test=".='Drugs Adulterated'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Drugs Health or Safety'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Drugs Misbranded'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Drunkenness'">
				<xsl:value-of select="'90E'" />
			</xsl:if>
			<xsl:if test=".='Eavesdrop Equipment'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Eavesdropping'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Ecology Law Violations'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Election Laws'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Embezzlement'">
				<xsl:value-of select="'270'" />
			</xsl:if>
			<xsl:if test=".='Embezzlement_banking_type institution'">
				<xsl:value-of select="'270'" />
			</xsl:if>
			<xsl:if test=".='Embezzlement_business property'">
				<xsl:value-of select="'270'" />
			</xsl:if>
			<xsl:if test=".='Embezzlement_interstate shipment'">
				<xsl:value-of select="'270'" />
			</xsl:if>
			<xsl:if test=".='Embezzlement_postal'">
				<xsl:value-of select="'270'" />
			</xsl:if>
			<xsl:if test=".='Embezzlement_public property'">
				<xsl:value-of select="'270'" />
			</xsl:if>
			<xsl:if test=".='Enticement of Minor for Indecent Purposes'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if
				test=".='Enticement of Minor for Indecent Purposes_telecommunications'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Enticement of Minor for Prostitution'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Environment Law Violations'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Escape'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Escape From Custody'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Espionage'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Evidence Destroying'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Exploitation_Enticement'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Explosives Possessing'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Explosives Teaching Use'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Explosives Transporting'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Explosives Using'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Extortion Threat_accuse person of crime'">
				<xsl:value-of select="'210'" />
			</xsl:if>
			<xsl:if test=".='Extortion Threat_damage property'">
				<xsl:value-of select="'210'" />
			</xsl:if>
			<xsl:if test=".='Extortion Threat_injure person'">
				<xsl:value-of select="'210'" />
			</xsl:if>
			<xsl:if test=".='Extortion Threat_injure reputation'">
				<xsl:value-of select="'210'" />
			</xsl:if>
			<xsl:if test=".='Extortion Threat_of informing of violation'">
				<xsl:value-of select="'210'" />
			</xsl:if>
			<xsl:if test=".='Extortion Blackmail'">
				<xsl:value-of select="'210'" />
			</xsl:if>
			<xsl:if test=".='Facilitation of'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Failing to Move On'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Failure Report Crime'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Failure To Appear'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Failure to Register As A Sex Offender'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='False Arrest'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='False Citizenship'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='False Fire Alarm'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='False Imprisonment_minor_nonparent'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='False Imprisonment_minor_parent'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='False Report'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='False Statement'">
				<xsl:value-of select="'26A'" />
			</xsl:if>
			<xsl:if test=".='Family Offense'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Family Offenses_Cruelty Toward Child'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Family Offenses_Cruelty Toward Wife'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Family Offenses_Neglect Child'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Family Offenses_Neglect Family'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Family Offenses_Nonpayment Of Alimony'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Family Offenses_Nonsupport Of Parent'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Family Offenses_nonviolent'">
				<xsl:value-of select="'90F'" />
			</xsl:if>
			<xsl:if test=".='Federal_Material Witness'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Fish and Game Law Violations'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Flight To Avoid'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Flight_Escape'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Food Adulterated'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Food Health or Safety'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Food Misbranded'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Forcible Fondling'">
				<xsl:value-of select="'11D'" />
			</xsl:if>
			<xsl:if test=".='Forcible Purse Snatching'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Forcible Rape'">
				<xsl:value-of select="'11A'" />
			</xsl:if>
			<xsl:if test=".='Forcible Sodomy'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Forged_Pass'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Forged_Possessing'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Forged_Transporting'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Forgery'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Forgery Of'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Forgery Of Checks'">
				<xsl:value-of select="'250'" />
			</xsl:if>
			<xsl:if test=".='Fornication_consensual'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Fraud'">
				<xsl:value-of select="'26A'" />
			</xsl:if>
			<xsl:if test=".='Fraud and Abuse_computer'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Fraud_By Wire'">
				<xsl:value-of select="'26E'" />
			</xsl:if>
			<xsl:if test=".='Fraud_confidence game'">
				<xsl:value-of select="'26A'" />
			</xsl:if>
			<xsl:if test=".='Fraud_Credit Card_Automatic Teller Machine'">
				<xsl:value-of select="'26B'" />
			</xsl:if>
			<xsl:if test=".='Fraud_False Pretenses_Swindle_Confidence Game'">
				<xsl:value-of select="'26A'" />
			</xsl:if>
			<xsl:if test=".='Fraud_false statement'">
				<xsl:value-of select="'26A'" />
			</xsl:if>
			<xsl:if test=".='Fraud_impersonation'">
				<xsl:value-of select="'26C'" />
			</xsl:if>
			<xsl:if test=".='Fraud_insufficient funds check'">
				<xsl:value-of select="'90A'" />
			</xsl:if>
			<xsl:if test=".='Fraud_Mail Fraud'">
				<xsl:value-of select="'26A'" />
			</xsl:if>
			<xsl:if test=".='Fraud_swindle'">
				<xsl:value-of select="'26A'" />
			</xsl:if>
			<xsl:if test=".='Fraud_Welfare Fraud'">
				<xsl:value-of select="'26D'" />
			</xsl:if>
			<xsl:if test=".='Frequent House Ill Fame'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Gambling'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Gambling Device'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling Device_not registered'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling Device_possession'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling Device_transport'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling Equipment Violation'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling Goods'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling Goods_possession'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling Goods_transport'">
				<xsl:value-of select="'39C'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Bookmaking'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Card Game'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Card Game_operating'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Dice Game'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Dice Game Operating'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Establish Gambling Place'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Lottery'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Lottery_operating'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Lottery_runner'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Operating_Promoting_Assisting'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Gambling_Transmit Wager Information'">
				<xsl:value-of select="'39B'" />
			</xsl:if>
			<xsl:if test=".='Harassing Communication'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Harassment'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Harboring Escapee_Fugitive'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Health_safety'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Hit And Run'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homicide'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homicide_John or Jane Doe_no warrant'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homicide_Negligent Manslaughter_vehicle'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homicide_Negligent Manslaughter_weapon'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_family_gun'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_family_weapon'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_gun'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_nonfamily_gun'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_nonfamily_weapon'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_police officer_gun'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_police officer-weapon'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_public official_gun'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_public official_weapon'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homicide_Willful Killing_weapon'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Homosexual Act'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homosexual Act_with boy'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homosexual Act_with girl'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homosexual Act_with man'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Homosexual Act_with woman'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Illegal Arrest'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Illegal Entry'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Immigration'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Incendiary Device_possession'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Incendiary Device_teaching use'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Incendiary Device_using'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Incest'">
				<xsl:value-of select="'36A'" />
			</xsl:if>
			<xsl:if test=".='Incest With Adult'">
				<xsl:value-of select="'36A'" />
			</xsl:if>
			<xsl:if test=".='Incest With Minor'">
				<xsl:value-of select="'36A'" />
			</xsl:if>
			<xsl:if test=".='Income Tax'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Indecent Exposure'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Indecent Exposure to Adult'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Indecent Exposure to Minor'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Interstate Transportation Stolen Vehicle'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Intimidation'">
				<xsl:value-of select="'13C'" />
			</xsl:if>
			<xsl:if test=".='Invade Privacy'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Jury Tampering'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Justifiable Homicide'">
				<xsl:value-of select="'09C'" />
			</xsl:if>
			<xsl:if test=".='Keeping House Ill Fame'">
				<xsl:value-of select="'40B'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Adult'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Adult For Ransom'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Adult To Sexually Assault'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Hostage For Escape'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Minor'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Minor For Ransom'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Minor To Sexually Assault'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Minor_nonparent'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Kidnapping Minor_parent'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Kidnapping_hijack aircraft'">
				<xsl:value-of select="'100'" />
			</xsl:if>
			<xsl:if test=".='Larceny'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Larceny_Aircraft Theft'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Larceny_All Other'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from auto'">
				<xsl:value-of select="'23F'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from banking type institution'">
				<xsl:value-of select="'23D'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from building'">
				<xsl:value-of select="'23D'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from coin operated machine'">
				<xsl:value-of select="'23E'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from interstate shipment'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from mails'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from shipment'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Larceny_from yards'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Larceny_On US Government Reservation'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Larceny_parts from vehicle'">
				<xsl:value-of select="'23G'" />
			</xsl:if>
			<xsl:if test=".='Larceny_postal'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Lewd or Lascivious Acts with Minor'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Liquor Law Violations'">
				<xsl:value-of select="'90G'" />
			</xsl:if>
			<xsl:if test=".='Liquor Tax'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Liquor_manufacturing'">
				<xsl:value-of select="'90G'" />
			</xsl:if>
			<xsl:if test=".='Liquor_possession'">
				<xsl:value-of select="'90G'" />
			</xsl:if>
			<xsl:if test=".='Liquor_selling'">
				<xsl:value-of select="'90G'" />
			</xsl:if>
			<xsl:if test=".='Liquor_transporting'">
				<xsl:value-of select="'90G'" />
			</xsl:if>
			<xsl:if test=".='Littering'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Making False Report'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Mandatory Release Violation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Military Desertion'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Military Law Violation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Misappropriation of Government Funds'">
				<xsl:value-of select="'270'" />
			</xsl:if>
			<xsl:if test=".='Misconduct_Judicial Officer'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Molestation of Minor'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Money Laundering'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Monopoly in Restraint of Trade'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Morals_Decency Crimes'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Motor Vehicle Theft'">
				<xsl:value-of select="'240'" />
			</xsl:if>
			<xsl:if test=".='Murder and Nonnegligent Manslaughter'">
				<xsl:value-of select="'09A'" />
			</xsl:if>
			<xsl:if test=".='Neglect Disabled'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Neglect Elderly'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Negligent Manslaughter'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Obscene Communication'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Obscene Material_distributing'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obscene Material_mailing'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obscene Material_manufacturing'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obscene Material_Pornography'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obscene Material_possession'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obscene Material_selling'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obscene Material_transporting'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obscenity'">
				<xsl:value-of select="'370'" />
			</xsl:if>
			<xsl:if test=".='Obstruct'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Obstruct Correspondence'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Obstruct Court Order'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Obstruct Criminal Investigation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Obstruct Justice'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Obstruct Police'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Opening Sealed Communication'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Parole Violation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Peeping Tom'">
				<xsl:value-of select="'90H'" />
			</xsl:if>
			<xsl:if test=".='Perjury'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Perjury_subornation of'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Pocket Picking'">
				<xsl:value-of select="'23A'" />
			</xsl:if>
			<xsl:if test=".='Polygamy'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Possessing Tools For Forgery_Counterfeiting'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Privacy_Invasion of'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Probation Violation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Procure For Prostitute'">
				<xsl:value-of select="'40B'" />
			</xsl:if>
			<xsl:if test=".='Procure for Prostitute Who Is A Minor'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Procure for Prostitute Who Is An Adult'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Property Crimes'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Prostitution'">
				<xsl:value-of select="'40A'" />
			</xsl:if>
			<xsl:if test=".='Prostitution_Assisting or Promoting'">
				<xsl:value-of select="'40B'" />
			</xsl:if>
			<xsl:if test=".='Public Order Crimes'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Public Peace'">
				<xsl:value-of select="'90C'" />
			</xsl:if>
			<xsl:if test=".='Purse Snatching_no force'">
				<xsl:value-of select="'23B'" />
			</xsl:if>
			<xsl:if test=".='Quarantine Violation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Racketeering'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Rape'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Rape With Weapon'">
				<xsl:value-of select="'11A'" />
			</xsl:if>
			<xsl:if test=".='Rape_disabled'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Rape_drug induced'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Rape_elderly'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Rape_gun'">
				<xsl:value-of select="'11A'" />
			</xsl:if>
			<xsl:if test=".='Rape_Statutory'">
				<xsl:value-of select="'36B'" />
			</xsl:if>
			<xsl:if test=".='Rape_strongarm'">
				<xsl:value-of select="'11A'" />
			</xsl:if>
			<xsl:if test=".='Reckless Endangerment'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Reckless Operation of Aircraft'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Refusing to Aid Officer'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Release Violation_conditional'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Release Violation_mandatory'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Resisting Officer'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Revenue Law Violations'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Riot'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Riot_engaging In'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Riot_inciting'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Riot_interfere firearm'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Riot_interfere officer'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Robbery'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_banking type institution'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_business_gun'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_business_strongarm'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_business_weapon'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_residence_gun'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_residence_strongarm'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_residence_weapon'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_street_gun'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_street_strongarm'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Robbery_street_weapon'">
				<xsl:value-of select="'120'" />
			</xsl:if>
			<xsl:if test=".='Rout'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Runaway'">
				<xsl:value-of select="'90I'" />
			</xsl:if>
			<xsl:if test=".='Sabotage'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sales Tax'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sanitation Law Violations'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Scalping_tickets'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sedition'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Seduction'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Seduction Of Adult'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Selective Service'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Assault'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_carnal abuse'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_disabled'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_elderly'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_boy_gun'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_boy_strongarm'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_boy_weapon'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_girl_gun'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_girl_strongarm'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_girl_weapon'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_man_gun'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_man_strongarm'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_man_weapon'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_woman_gun'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_woman_strongarm'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Assault_sodomy_woman_weapon'">
				<xsl:value-of select="'11B'" />
			</xsl:if>
			<xsl:if test=".='Sex Offender Registration Violation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Offense'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Offense_against child_fondling'">
				<xsl:value-of select="'11D'" />
			</xsl:if>
			<xsl:if test=".='Sex Offense_disabled'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sex Offense_elderly'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Assault with an Object'">
				<xsl:value-of select="'11C'" />
			</xsl:if>
			<xsl:if test=".='Sexual Assault_drug_induced'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Exploitation of Minor_exhibition of minor'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Exploitation of Minor_material_film'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Exploitation of Minor_material_photograph'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Exploitation of Minor_material_transporting'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Exploitation of Minor_prostitution'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Exploitation of Minor_sex performance'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexual Exploitation of Minor_via telecommunications'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sexually Violate Human Remains_Necrophilia'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Shoplifting'">
				<xsl:value-of select="'23C'" />
			</xsl:if>
			<xsl:if test=".='Simple Assault'">
				<xsl:value-of select="'13B'" />
			</xsl:if>
			<xsl:if test=".='Smuggle Contraband'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Smuggle Contraband Into Prison'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Smuggle To Avoid Paying Duty'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Smuggling'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Smuggling Aliens'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Solicitation to Commit Felony'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sovereignty'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Sports Tampering'">
				<xsl:value-of select="'39D'" />
			</xsl:if>
			<xsl:if test=".='State_Local_material witness'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Stolen Property Offenses'">
				<xsl:value-of select="'280'" />
			</xsl:if>
			<xsl:if test=".='Stolen Property_Conceal'">
				<xsl:value-of select="' 280'" />
			</xsl:if>
			<xsl:if test=".='Stolen Property_Possessing'">
				<xsl:value-of select="'280'" />
			</xsl:if>
			<xsl:if test=".='Stolen Property_Receive'">
				<xsl:value-of select="'280'" />
			</xsl:if>
			<xsl:if test=".='Stolen Property_Sale'">
				<xsl:value-of select="'280'" />
			</xsl:if>
			<xsl:if test=".='Stolen Property_Transporting Interstate'">
				<xsl:value-of select="'280'" />
			</xsl:if>
			<xsl:if test=".='Stolen Vehicle'">
				<xsl:value-of select="'240'" />
			</xsl:if>
			<xsl:if test=".='Stolen Vehicle_Possessing'">
				<xsl:value-of select="'240'" />
			</xsl:if>
			<xsl:if test=".='Stolen Vehicle_Receive'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Strip Stolen Vehicle'">
				<xsl:value-of select="'23G'" />
			</xsl:if>
			<xsl:if test=".='Suicide'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Tax Revenue'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Theft And Sale Vehicle'">
				<xsl:value-of select="'240'" />
			</xsl:if>
			<xsl:if test=".='Theft And Strip Vehicle'">
				<xsl:value-of select="'240'" />
			</xsl:if>
			<xsl:if test=".='Theft And Use Vehicle Other Crime'">
				<xsl:value-of select="'240'" />
			</xsl:if>
			<xsl:if test=".='Theft from Building'">
				<xsl:value-of select="'23D'" />
			</xsl:if>
			<xsl:if test=".='Theft of Motor Vehicle Parts or Accessories'">
				<xsl:value-of select="'23G'" />
			</xsl:if>
			<xsl:if test=".='Theft Of US Government Property'">
				<xsl:value-of select="'23H'" />
			</xsl:if>
			<xsl:if test=".='Theft Vehicle By Bailee'">
				<xsl:value-of select="'240'" />
			</xsl:if>
			<xsl:if test=".='Threat To Bomb'">
				<xsl:value-of select="'13C'" />
			</xsl:if>
			<xsl:if test=".='Threat To Burn'">
				<xsl:value-of select="'13C'" />
			</xsl:if>
			<xsl:if test=".='Threat_federal protectees'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Threat_terroristic_state offenses'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Traffic Offense'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Transporting Dangerous Material'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Transporting Female Interstate for Immoral Purposes'">
				<xsl:value-of select="'40B'" />
			</xsl:if>
			<xsl:if test=".='Transporting Interstate for Commercialized Sex'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Transporting Interstate for Sexual Activity'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Transporting Tools For Forgery_Counterfeiting'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Treason'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Treason Misprision'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Trespassing'">
				<xsl:value-of select="'90J'" />
			</xsl:if>
			<xsl:if test=".='Tresspass of Real Property'">
				<xsl:value-of select="'90J'" />
			</xsl:if>
			<xsl:if test=".='Unauthorized Communication With Prisoner'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Unauthorized Use Of Vehicle'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Vehicular Manslaughter'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Violation of a Court Order'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Violation of Restraining Order'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Voyeurism'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Weapon Law Violations'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Law_Altering Identification On Weapon'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Law_Carrying Concealed Weapon'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Law_Carrying Prohibited Weapon'">
				<xsl:value-of select="' 520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Law_Firing Weapon'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Law_Licensing_registration weapon'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Law_Possessing Of Weapon'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Law_Selling Weapon'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Offense'">
				<xsl:value-of select="'520'" />
			</xsl:if>
			<xsl:if test=".='Weapon Trafficking'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Wiretap_failure to report'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Witness_deceiving'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Witness_dissuading'">
				<xsl:value-of select="''" />
			</xsl:if>
		</nibrs:OffenseUCRCode>
	</xsl:template>
	<xsl:template match="ndexia:CriminalActivityCategoryCode">
		<!-- Element 12, Type Criminal Activity/Gang Information -->
		<nibrs:CriminalActivityCategoryCode>
			<xsl:if test=".='Assisting'">
				<xsl:value-of select="'O'" />
			</xsl:if>
			<xsl:if test=".='Buying'">
				<xsl:value-of select="'B'" />
			</xsl:if>
			<xsl:if test=".='Buying_Receiving'">
				<xsl:value-of select="'B'" />
			</xsl:if>
			<xsl:if test=".='Concealing'">
				<xsl:value-of select="'P'" />
			</xsl:if>
			<xsl:if test=".='Consuming'">
				<xsl:value-of select="'U'" />
			</xsl:if>
			<xsl:if test=".='Cultivating'">
				<xsl:value-of select="'C'" />
			</xsl:if>
			<xsl:if test=".='Cultivating_Manufacturing_Publishing'">
				<xsl:value-of select="'C'" />
			</xsl:if>
			<xsl:if test=".='Distributing'">
				<xsl:value-of select="'D'" />
			</xsl:if>
			<xsl:if test=".='Distributing_Selling'">
				<xsl:value-of select="'D'" />
			</xsl:if>
			<xsl:if test=".='Exploiting Children'">
				<xsl:value-of select="'E'" />
			</xsl:if>
			<xsl:if test=".='Importing'">
				<xsl:value-of select="'T'" />
			</xsl:if>
			<xsl:if test=".='Manufacturing'">
				<xsl:value-of select="'C'" />
			</xsl:if>
			<xsl:if test=".='Operating'">
				<xsl:value-of select="'O'" />
			</xsl:if>
			<xsl:if test=".='Operating_Promoting_Assisting'">
				<xsl:value-of select="'O'" />
			</xsl:if>
			<xsl:if test=".='Possessing'">
				<xsl:value-of select="'P'" />
			</xsl:if>
			<xsl:if test=".='Possessing_Concealing'">
				<xsl:value-of select="'P'" />
			</xsl:if>
			<xsl:if test=".='Promoting'">
				<xsl:value-of select="'O'" />
			</xsl:if>
			<xsl:if test=".='Publishing'">
				<xsl:value-of select="'C'" />
			</xsl:if>
			<xsl:if test=".='Receiving'">
				<xsl:value-of select="'B'" />
			</xsl:if>
			<xsl:if test=".='Selling'">
				<xsl:value-of select="'D'" />
			</xsl:if>
			<xsl:if test=".='Transmitting'">
				<xsl:value-of select="'T'" />
			</xsl:if>
			<xsl:if test=".='Transporting'">
				<xsl:value-of select="'T'" />
			</xsl:if>
			<xsl:if test=".='Transporting_Transmitting_Importing'">
				<xsl:value-of select="'T'" />
			</xsl:if>
			<xsl:if test=".='Using'">
				<xsl:value-of select="'U'" />
			</xsl:if>
			<xsl:if test=".='Using_Consuming'">
				<xsl:value-of select="'U'" />
			</xsl:if>
			<xsl:if test=".='None'">
				<xsl:value-of select="'N'" />
			</xsl:if>
			<xsl:if test=".='Unknown'">
				<xsl:value-of select="'N'" />
			</xsl:if>
		</nibrs:CriminalActivityCategoryCode>
	</xsl:template>
	<xsl:template match="ndexia:OffenseBiasMotivationCode">
		<!-- Element 8A, Bias Motivation -->
		<j:OffenseFactorBiasMotivationCode>
			<xsl:value-of select="upper-case(.)" />	
		</j:OffenseFactorBiasMotivationCode>
	</xsl:template>
	<xsl:template match="j40:IncidentStructuresEnteredQuantity">
		<!-- Element 10, Number Of Premises Entered -->
		<j:OffenseStructuresEnteredQuantity>
			<xsl:value-of select="." />
		</j:OffenseStructuresEnteredQuantity>
	</xsl:template>
	<xsl:template match="j40:IncidentFactor/j40:IncidentFactorCode">
		<!-- Element 8, Offender(s) Suspected Of Using -->
		<j:OffenseFactor>
			<j:OffenseFactorCode>
				<xsl:value-of select="." />
			</j:OffenseFactorCode>
		</j:OffenseFactor>
	</xsl:template>
	<xsl:template match="ndexia:OffenseEntryPoint/j40:PassagePointMethodCode">
		<!-- Element 11, Method Of Entry -->
		<j:OffenseEntryPoint>
			<j:PassagePointMethodCode>
				<xsl:value-of select="." />
			</j:PassagePointMethodCode>
		</j:OffenseEntryPoint>
	</xsl:template>
	<xsl:template match="j40:IncidentForce/j40:ForceCategoryCode">
		<!-- Element 13, Type Weapon/Force Involved -->
		<j:OffenseForce>
			<j:ForceCategoryCode>
				<xsl:value-of select="." />
			</j:ForceCategoryCode>
		</j:OffenseForce>
	</xsl:template>
	<xsl:template match="ndexia:OffenseCompletedIndicator">
		<!-- Element 7, Attempted/Completed -->
		<j:OffenseAttemptedIndicator>
			<xsl:value-of select="." />
		</j:OffenseAttemptedIndicator>
	</xsl:template>
	<xsl:template match="ndexia:LocationCategoryCode">
		<!-- Element 9, Location Type -->
		<j:LocationCategoryCode>
			<xsl:if test=".='Airport_Terminal_Commercial'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_Concourse'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_Freight'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_Hangar'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_International'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_Military'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_Private'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_Ramp'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Airport_Terminal_Runway'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Assembly Line'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='ATM'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Auto shop'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Bar'">
				<xsl:value-of select="'03'" />
			</xsl:if>
			<xsl:if test=".='Bar_Lounge'">
				<xsl:value-of select="'03'" />
			</xsl:if>
			<xsl:if test=".='Bar_Nightclub'">
				<xsl:value-of select="'03'" />
			</xsl:if>
			<xsl:if test=".='Bar_Strip Club'">
				<xsl:value-of select="'03'" />
			</xsl:if>
			<xsl:if test=".='Barn'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Boarding House'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Bridge'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Bridge_Overpass'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Bridge_Trestle'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Bridge_Viaduct'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Business Office'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Cabin'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Camp'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Campground'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Campus'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cargo Container'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Cargo Container_Rental Storage'">
				<xsl:value-of select="'19'" />
			</xsl:if>
			<xsl:if test=".='Casino'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Casino_Race Track'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Cemetery'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Check Point_Border Crossing'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Commercial Building'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Commercial Building_Vacant'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Community Center'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Community Center_Senior Citizens'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Construction Site'">
				<xsl:value-of select="'06'" />
			</xsl:if>
			<xsl:if test=".='Convalescent_Home'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Correctional Facility'">
				<xsl:value-of select="'15'" />
			</xsl:if>
			<xsl:if test=".='Correctional Facility_Jail'">
				<xsl:value-of select="'15'" />
			</xsl:if>
			<xsl:if test=".='Correctional Facility_Juvenile Detention'">
				<xsl:value-of select="'15'" />
			</xsl:if>
			<xsl:if test=".='Correctional Facility_Penitentiary'">
				<xsl:value-of select="'15'" />
			</xsl:if>
			<xsl:if test=".='Correctional Facility_Prison'">
				<xsl:value-of select="'15'" />
			</xsl:if>
			<xsl:if test=".='Daycare'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Dealership'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Dealership_Boat'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Dealership_Vehicle'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Desert'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Factory'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Fairgrounds'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm_Barn'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm_Garden'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm_Grain Elevator'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm_Shed'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm_Silo'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm_Stable'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Farm_Tack Room'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Field'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Field_Woods'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Financial Institution'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Financial Institution_Automated Teller Machine'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Financial Institution_Bank'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Financial Institution_Bank_Drive Thru'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Financial Institution_Credit Union'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Financial Institution_Mortgage Company'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Financial Institution_Savings Loan'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Fire Station'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Foothills'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Garage'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Garden'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Golf Course'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Government Building'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Government Building_City'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Government Building_County'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Government Building_Federal'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Government Building_Military'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Government Building_Municipal'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Government Building_State'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Government Building_Town'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Gym'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Hotel'">
				<xsl:value-of select="'14'" />
			</xsl:if>
			<xsl:if test=".='Hotel_Motel_Inn'">
				<xsl:value-of select="'14'" />
			</xsl:if>
			<xsl:if test=".='House_Vacant'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Inn'">
				<xsl:value-of select="'14'" />
			</xsl:if>
			<xsl:if test=".='Landfill_Dump'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Laundromat'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Law Enforcement Facility'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Law Enforcement Facility_City'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Law Enforcement Facility_County'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Law Enforcement Facility_Federal'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Law Enforcement Facility_State'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Law Enforcement Facility_Tribal'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Library'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Lot'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Lot_Vacant'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Mall_Retail Complex'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Medical Center'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Dentist'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Doctor Office'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Doctor Office_Clinic'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Drug_Store'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Hospital'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Hospital_Convalescent'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Office'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Medical Center_Supply Building'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Meeting Hall_Community Center'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Mill'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Mine'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Monument'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Motel'">
				<xsl:value-of select="'14'" />
			</xsl:if>
			<xsl:if test=".='Mountains'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Movie Theater'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Museum'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Nursing Home'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Office Building'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Oil Field'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Open Field'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Outbuilding'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Park'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Park_City'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Park_Federal'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Park_National'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Park_Playground'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Park_Roadside'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Park_State'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Parking_Garage'">
				<xsl:value-of select="'18'" />
			</xsl:if>
			<xsl:if test=".='Parking Lot'">
				<xsl:value-of select="'18'" />
			</xsl:if>
			<xsl:if test=".='Parking Lot_Garage'">
				<xsl:value-of select="'18'" />
			</xsl:if>
			<xsl:if test=".='Pipeline'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Plant'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Public Works Facility'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Quarry'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Rail Yard'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility_Amusement Park'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility_Civic Center'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility_Race Track'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility_Sports Arena'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility_Stadium'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility_Swimming Pool'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Recreation Facility_Zoo'">
				<xsl:value-of select="'11'" />
			</xsl:if>
			<xsl:if test=".='Refinery'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Religious Building'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Religious Building_Chapel'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Religious Building_Church'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Religious Building_Mosque'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Religious Building_Synagogue'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Religious Building_Temple'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Reservation'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Reservoir'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Residential_ Apartment Complex'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Airplane_Affixed'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Apartment'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Boat_Affixed'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Condominium'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Dormitory'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Duplex'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Flat'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_House'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Mobilie Home'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_RV_Affixed'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Tenement'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Townhouse'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Residential_Van_Affixed'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Restaurant'">
				<xsl:value-of select="'21'" />
			</xsl:if>
			<xsl:if test=".='Restaurant_Cafeteria'">
				<xsl:value-of select="'21'" />
			</xsl:if>
			<xsl:if test=".='Restaurant_Family Dining'">
				<xsl:value-of select="'21'" />
			</xsl:if>
			<xsl:if test=".='Restaurant_Fast Food'">
				<xsl:value-of select="'21'" />
			</xsl:if>
			<xsl:if test=".='Restaurant_Snack Bar'">
				<xsl:value-of select="'21'" />
			</xsl:if>
			<xsl:if test=".='Right of Way'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Right of Way_Railbed_Train'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Right of Way_Road'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Right of Way_Utility'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Roadway'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Alley'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_City'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_County'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Crosswalk'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Cul_de_sac'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Dirt Road'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Fire Road'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Freeway'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Highway'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Interchange'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Interstate'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Rest Area'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Roadway_Road'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Service Road'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_State'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Street'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Roadway_Toll Road'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='School Combined K_12_Private_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_College'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Combined K_12'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Combined K_12_Private'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Combined K_12_Public'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Elementary'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Elementary_Private'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Elementary_Private_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Elementary_Public'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_High'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_High_Private'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_High_Private_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_High_Public'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Middle'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Middle_Private'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Middle_Private_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Middle_Public'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Preschool'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Preschool_Private'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Preschool_Private_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Preschool_Public'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Private'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Private_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Public'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Technical'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Technical_Private'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Technical_Private_Parochial'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Technical_Public'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='School_Vocational'">
				<xsl:value-of select="'22'" />
			</xsl:if>
			<xsl:if test=".='Shelter'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Shelter_Family'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Shelter_Womans'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Ship Yard'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Shopping Center'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Sidewalk'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Sky'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Stables'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Storage Facility'">
				<xsl:value-of select="'19'" />
			</xsl:if>
			<xsl:if test=".='Storage Facility_Tank'">
				<xsl:value-of select="'19'" />
			</xsl:if>
			<xsl:if test=".='Storage Facility_ Locker'">
				<xsl:value-of select="'19'" />
			</xsl:if>
			<xsl:if test=".='Storage Facility_Unit'">
				<xsl:value-of select="'19'" />
			</xsl:if>
			<xsl:if test=".='Store'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Store_Arcade'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Store_Auto Parts'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Candy'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Coin'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Convenience'">
				<xsl:value-of select="'07'" />
			</xsl:if>
			<xsl:if test=".='Store_Department_Discount'">
				<xsl:value-of select="'07'" />
			</xsl:if>
			<xsl:if test=".='Store_Department'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Store_Discount'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Store_Doll'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Dress'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Drug'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Store_Electronic'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Fur'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Gas Station'">
				<xsl:value-of select="'23'" />
			</xsl:if>
			<xsl:if test=".='Store_Gas Station_Service Station'">
				<xsl:value-of select="'23'" />
			</xsl:if>
			<xsl:if test=".='Store_Grocery'">
				<xsl:value-of select="'12'" />
			</xsl:if>
			<xsl:if test=".='Store_Grocery_Supermarket'">
				<xsl:value-of select="'12'" />
			</xsl:if>
			<xsl:if test=".='Store_Jewelry'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Liquor'">
				<xsl:value-of select="'17'" />
			</xsl:if>
			<xsl:if test=".='Store_Market'">
				<xsl:value-of select="'12'" />
			</xsl:if>
			<xsl:if test=".='Store_Minimart'">
				<xsl:value-of select="'07'" />
			</xsl:if>
			<xsl:if test=".='Store_Music'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Pawn Shop'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Store_Pharmacy'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Store_Second Hand'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Store_Service Station'">
				<xsl:value-of select="'23'" />
			</xsl:if>
			<xsl:if test=".='Store_Sex'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Specialty'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Stamp'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Strip Mall_Retail Complex'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Store_Supermarket'">
				<xsl:value-of select="'12'" />
			</xsl:if>
			<xsl:if test=".='Store_TV'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Store_Video'">
				<xsl:value-of select="'24'" />
			</xsl:if>
			<xsl:if test=".='Storehouse'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Terminal'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Airport'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Boat'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Bus'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Bus Stop'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Nonpassenger'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Nonpassenger_Airport'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Nonpassenger_Boat'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Nonpassenger_Train'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Nonpassenger_Truck'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Passenger_Airport'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Passenger_Boat'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Passenger_Bus'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Passenger_Train'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Terminal_Passenger_Train_Subway'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Theater_Community'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Trailer Park'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Tunnel'">
				<xsl:value-of select="'13'" />
			</xsl:if>
			<xsl:if test=".='Unknown'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Utility_Station_Plant'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Utility_Station_Plant_Electric'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Utility_Station_Plant_Natural Gas'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Utility_Station_Plant_Nuclear Reactor'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Utility_Station_Plant_Sewer'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Utility_Station_Plant_Water'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Utility_Station_Plant_Windmill'">
				<xsl:value-of select="'25'" />
			</xsl:if>
			<xsl:if test=".='Warehouse'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Waterway'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Aqueduct'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Beach'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Canal'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Dam'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Waterway_Dock'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Waterway_Harbor'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Lake'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Lock'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Ocean'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_River'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Stream'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Swamp'">
				<xsl:value-of select="'16'" />
			</xsl:if>
			<xsl:if test=".='Waterway_Wharf'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Woods'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Yard'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Yard_Junk'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Yard_Pipe'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Yard_Rail'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Yard_Ship'">
				<xsl:value-of select="''" />
			</xsl:if>
		</j:LocationCategoryCode>
	</xsl:template>
	<xsl:template match="ndexia:ItemStatusCode">
		<!-- Element 14, Type Property Loss/etc Substituted for nc:ItemStatus -->
		<nc:ItemStatus>
			<cjis:ItemStatusCode>
				<xsl:value-of select="upper-case(.)" />	
			</cjis:ItemStatusCode>
		</nc:ItemStatus>
	</xsl:template>
	<xsl:template match="ndexia:ItemValue/nc20:ItemValueAmount">
		<!-- Element 16, Value of Property in US Dollars -->
		<nc:ItemValueAmount>
			<nc:Amount>
				<xsl:value-of select="." />
			</nc:Amount>
		</nc:ItemValueAmount>
	</xsl:template>
	<xsl:template match="ndexia:ItemStatus/nc20:StatusDate">
		<!-- Element 17, Date Recovered -->
		<nc:ItemValueDate>
			<xsl:apply-templates select="nc20:Date" />
		</nc:ItemValueDate>
	</xsl:template>
	<xsl:template match="nc20:ItemQuantity">
		<nc:ItemQuantity>
			<xsl:value-of select="." />
		</nc:ItemQuantity>
	</xsl:template>
	<xsl:template match="j41:PropertyCategoryNIBRSPropertyCategoryCode">
		<!-- Element 15, Property Description -->
		<j:ItemCategoryNIBRSPropertyCategoryCode>
			<xsl:value-of select="." />
		</j:ItemCategoryNIBRSPropertyCategoryCode>
	</xsl:template>
	<xsl:template match="nc20:Drug/j40:DrugDEACode">
		<!-- Element 20, Suspected Involved Drug Type -->
		<j:DrugCategoryCode>
			<xsl:if test=".='C3'">
				<xsl:value-of select="'A'" />
			</xsl:if>
			<xsl:if test=".='C1' or .='C2' or .='C4' or .='C5'">
				<xsl:value-of select="'B'" />
			</xsl:if>
			<xsl:if test=".='M4' or .='M5'">
				<xsl:value-of select="'C'" />
			</xsl:if>
			<xsl:if test=".='H1'">
				<xsl:value-of select="'D'" />
			</xsl:if>
			<xsl:if test=".='M1' or .='M2'">
				<xsl:value-of select="'E'" />
			</xsl:if>
			<xsl:if test=".='H3'">
				<xsl:value-of select="'F'" />
			</xsl:if>
			<xsl:if test=".='H2'">
				<xsl:value-of select="'G'" />
			</xsl:if>
			<xsl:if
				test=".='H4' or .='N1' or .='N2' or .='N3' or .='N5' or .='N8' or .='N9' or .='R1'">
				<xsl:value-of select="'H'" />
			</xsl:if>
			<xsl:if test=".='L1'">
				<xsl:value-of select="'I'" />
			</xsl:if>
			<xsl:if test=".='L2'">
				<xsl:value-of select="'J'" />
			</xsl:if>
			<xsl:if test=".='G2' or .='L3' or .='L4' or .='L5'">
				<xsl:value-of select="'K'" />
			</xsl:if>
			<xsl:if test=".='A1' or .='A2' or .='A3' or .='A4' or .='A7'">
				<xsl:value-of select="'L'" />
			</xsl:if>
			<xsl:if
				test=".='A5' or .='A6' or .='A8' or .='B1' or .='B2' or .='S1' or .='S2'">
				<xsl:value-of select="'M'" />
			</xsl:if>
			<!-- No N-DEX values for NIBRS "N" -->
			<xsl:if test=".='D1' or .='D2' or .='D4' or .='G1' or .='N6'">
				<xsl:value-of select="'O'" />
			</xsl:if>
			<xsl:if test=".='G3' or .='N4' or .='N7'">
				<xsl:value-of select="'P'" />
			</xsl:if>
			<xsl:if test=".='Z1'">
				<xsl:value-of select="'U'" />
			</xsl:if>
			<!-- No N-DEX values for NIBRS "X" -->
		</j:DrugCategoryCode>
	</xsl:template>
	<xsl:template match="nc20:MeasurePointValue" mode="quantity">
		<!-- Element 21, Estimated Quantity/Fraction -->
		<nc:MeasureDecimalValue>
			<xsl:value-of select="." />
		</nc:MeasureDecimalValue>
	</xsl:template>
	<xsl:template match="nc20:SubstanceUnitCode">
		<!-- Element 22, Drug Measurement Type -->
		<j:SubstanceUnitCode>
			<xsl:value-of select="." />
		</j:SubstanceUnitCode>
	</xsl:template>
	<xsl:template match="nc20:MeasurePointValue">
		<!-- Element 26, Age -->
		<nc:MeasureIntegerValue>
			<xsl:value-of select="." />
		</nc:MeasureIntegerValue>
	</xsl:template>
	<xsl:template match="nc20:RangeMinimumValue">
		<!-- Element 26, Age -->
		<nc:RangeMinimumValue>
			<xsl:value-of select="." />
		</nc:RangeMinimumValue>
	</xsl:template>
	<xsl:template match="nc20:RangeMaximumValue">
		<!-- Element 26, Age -->
		<nc:RangeMaximumValue>
			<xsl:value-of select="." />
		</nc:RangeMaximumValue>
	</xsl:template>
	<xsl:template match="nc20:PersonEthnicityCode">
		<!-- Element 29, Ethnicity of Victim -->
		<j:PersonEthnicityCode>
			<xsl:value-of select="." />
		</j:PersonEthnicityCode>
	</xsl:template>
	<xsl:template match="ndexia:InjuryCategoryCode">
		<!-- Element 33, Type Injury -->
		<nc:PersonInjury>
			<j:InjuryCategoryCode>
				<xsl:if test=".='Apparent Broken Bones'">
					<xsl:value-of select="'B'" />
				</xsl:if>
				<xsl:if test=".='Apparent Minor Injury'">
					<xsl:value-of select="'M'" />
				</xsl:if>
				<xsl:if test=".='Loss of Teeth'">
					<xsl:value-of select="'T'" />
				</xsl:if>
				<xsl:if test=".='Other Major Injury'">
					<xsl:value-of select="'O'" />
				</xsl:if>
				<xsl:if test=".='Possible Internal Injury'">
					<xsl:value-of select="'I'" />
				</xsl:if>
				<xsl:if test=".='Severe Laceration'">
					<xsl:value-of select="'L'" />
				</xsl:if>
				<xsl:if test=".='Unconsciousness'">
					<xsl:value-of select="'U'" />
				</xsl:if>
				<xsl:if test=".='None'">
					<xsl:value-of select="'N'" />
				</xsl:if>
				<xsl:if test=".='Undetermined'">
					<xsl:value-of select="''" />
				</xsl:if>
				<xsl:if test=".='Other'">
					<xsl:value-of select="''" />
				</xsl:if>
			</j:InjuryCategoryCode>
		</nc:PersonInjury>
	</xsl:template>
	<xsl:template match="ndexia:PersonRaceCode">
	<!-- Element 28, Person RaceCode -->
		<j:PersonRaceNDExCode>
			<xsl:value-of select="." />
		</j:PersonRaceNDExCode>
	</xsl:template>
	<xsl:template match="nc20:PersonResidentCode">
		<!-- Element 30, Resident Status -->
		<j:PersonResidentCode>
			<xsl:value-of select="." />
		</j:PersonResidentCode>
	</xsl:template>
	<xsl:template match="nc20:PersonSexCode">
		<!-- Element 27, Sex of Victim -->
		<j:PersonSexCode>
			<xsl:value-of select="." />
		</j:PersonSexCode>
	</xsl:template>
	<xsl:template match="ndexia:EnforcementOfficialActivityCategoryCode">
		<!-- Element 25A - Type of Activity (Officer)/ Circumstance -->
		<j:EnforcementOfficialActivityCategoryCode>
			<xsl:if test=".='Responding to disturbance call'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Burglary in progress or pursuing'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Robbery in progress or pursuing'">
				<xsl:value-of select="'03'" />
			</xsl:if>
			<xsl:if test=".='Attempting other arrest'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Civil disorder'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Domestic Disturbance'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Handling_Transporting prisoner'">
				<xsl:value-of select="'06'" />
			</xsl:if>
			<xsl:if test=".='Investigation_Suspicious person'">
				<xsl:value-of select="'07'" />
			</xsl:if>
			<xsl:if test=".='Ambush_No warning'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Mentally deranged assailant'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Traffic Pursuit_Stop'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Traffic Pursuit'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Traffic Stop'">
				<xsl:value-of select="''" />
			</xsl:if>
			<xsl:if test=".='Other'">
				<xsl:value-of select="'11'" />
			</xsl:if>
		</j:EnforcementOfficialActivityCategoryCode>
	</xsl:template>
	<xsl:template match="j40:EnforcementOfficialAssignmentCategoryCode">
		<!-- Element 25B, Assignment Type (Officer) -->
		<j:EnforcementOfficialAssignmentCategoryCode>
			<xsl:value-of select="." />
		</j:EnforcementOfficialAssignmentCategoryCode>
	</xsl:template>
	<xsl:template match="j40:OrganizationORIIdentification"
		mode="unit">
		<j:EnforcementOfficialUnit>
			<j:OrganizationAugmentation>
				<j:OrganizationORIIdentification>
					<!-- Element 25C, ORI-Other Jurisdiction (Officer) -->
					<xsl:apply-templates select="nc20:IdentificationID" />
				</j:OrganizationORIIdentification>
			</j:OrganizationAugmentation>
		</j:EnforcementOfficialUnit>
	</xsl:template>
	<xsl:template match="ndexia:VictimSequenceNumberText">
		<!-- Element 23, Victim Sequence Number -->
		<j:VictimSequenceNumberText>
			<xsl:value-of select="." />
		</j:VictimSequenceNumberText>
	</xsl:template>
	<xsl:template match="j40:VictimCategoryCode">
		<!-- Element 25, Type of Victim -->
		<j:VictimCategoryCode>
			<xsl:value-of select="." />
		</j:VictimCategoryCode>
	</xsl:template>
	<xsl:template match="ndexia:VictimAggravatedAssaultHomicideFactorCode">
		<!-- Element 31, Aggravated Assault/Homicide Circumstances -->
		<j:VictimAggravatedAssaultHomicideFactorCode>
			<xsl:if test=".='Aggravated stalking'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Ambush'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Argument'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Argument over boyfriend'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Argument over girlfriend'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Argument over money'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Argument over weapon'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Arson'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Assault on Law Enforcement Officer'">
				<xsl:value-of select="'02'" />
			</xsl:if>
			<xsl:if test=".='Barroom brawl'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Bootlegging'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Burglary attempt'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Child abuse'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Child Playing With Weapon'">
				<xsl:value-of select="'30'" />
			</xsl:if>
			<xsl:if test=".='Criminal Killed by Police Officer'">
				<xsl:value-of select="'21'" />
			</xsl:if>
			<xsl:if test=".='Criminal Killed by Private Citizen'">
				<xsl:value-of select="'20'" />
			</xsl:if>
			<xsl:if test=".='Domestic argument'">
				<xsl:value-of select="'01'" />
			</xsl:if>
			<xsl:if test=".='Drive by shooting assault'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Drive by shooting homicide'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Drug Dealing'">
				<xsl:value-of select="'03'" />
			</xsl:if>
			<xsl:if test=".='Fight assault'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Fight homicide'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Fleeing suspect_police assault'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Fleeing suspect_police homicide'">
				<xsl:value-of select="'21'" />
			</xsl:if>
			<xsl:if test=".='Gambling assault'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Gambling homicide'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Gangland'">
				<xsl:value-of select="'04'" />
			</xsl:if>
			<xsl:if test=".='Gun_Cleaning Accident'">
				<xsl:value-of select="'31'" />
			</xsl:if>
			<xsl:if test=".='Homicide_suicide'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Hunting Accident'">
				<xsl:value-of select="'32'" />
			</xsl:if>
			<xsl:if test=".='Juvenile Gang'">
				<xsl:value-of select="'05'" />
			</xsl:if>
			<xsl:if test=".='Kidnap_murder'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Kidnap_noncustodial parent'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Kidnap_stranger'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Lovers Quarrel'">
				<xsl:value-of select="'06'" />
			</xsl:if>
			<xsl:if test=".='Mental problem assault'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Mental problem homicide'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Mercy Killing'">
				<xsl:value-of select="'07'" />
			</xsl:if>
			<xsl:if test=".='Mugging assault'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Mugging homicide'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Murder for hire'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Other Circumstances'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Other Felony Involved'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Other Negligent Killings'">
				<xsl:value-of select="'34'" />
			</xsl:if>
			<xsl:if test=".='Other Negligent Weapon Handling'">
				<xsl:value-of select="'33'" />
			</xsl:if>
			<xsl:if test=".='Playing with weapon assault'">
				<xsl:value-of select="'33'" />
			</xsl:if>
			<xsl:if test=".='Playing with weapon homicide'">
				<xsl:value-of select="'33'" />
			</xsl:if>
			<xsl:if test=".='Police officer killed'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Race related assault'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Race related homicide'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Rape attempt_homicide'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Rape_Homicide'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Revenge assault'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Revenge homicide'">
				<xsl:value-of select="'09'" />
			</xsl:if>
			<xsl:if test=".='Robber killed by victim'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Self defense assault'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Self defense homicide'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Stalking'">
				<xsl:value-of select="'08'" />
			</xsl:if>
			<xsl:if test=".='Unknown assault circumstance'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Unknown Circumstances'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Unknown homicide circumstance'">
				<xsl:value-of select="'10'" />
			</xsl:if>
			<xsl:if test=".='Victim killed by robber'">
				<xsl:value-of select="'08'" />
			</xsl:if>
		</j:VictimAggravatedAssaultHomicideFactorCode>
	</xsl:template>
	<xsl:template match="j40:VictimJustifiableHomicideFactorCode">
		<!-- Element 32, Additional Justifiable Homicide Circumstances -->
		<j:VictimJustifiableHomicideFactorCode>
			<xsl:value-of select="." />
		</j:VictimJustifiableHomicideFactorCode>
	</xsl:template>
	<xsl:template match="ndexia:SubjectSequenceNumberText">
		<!-- Element 36, Offender Sequence Number -->
		<j:SubjectSequenceNumberText>
			<xsl:value-of select="." />
		</j:SubjectSequenceNumberText>
	</xsl:template>
	<xsl:template match="j40:ArrestSequenceID">
		<!-- Element 40, Arrestee Sequence Number -->
		<j:ArrestSequenceID>
			<xsl:value-of select="." />
		</j:ArrestSequenceID>
	</xsl:template>
	<xsl:template match="ndexia:ArresteeClearanceIndicator">
		<j:ArresteeClearanceIndicator>
			<xsl:value-of select="." />
		</j:ArresteeClearanceIndicator>
	</xsl:template>
	<xsl:template match="ndexia:ArresteeArmedWithCode">
		<!-- Element 46, Arrestee Was Armed With -->
		<j:ArresteeArmedWithCode>
			<xsl:value-of select="." />
		</j:ArresteeArmedWithCode>
	</xsl:template>
	<xsl:template match="ndexia:ArresteeJuvenileDispositionCode">
		<!-- Element 52, Disposition of Arrestee Under 18 -->
		<j:ArresteeJuvenileDispositionCode>
			<xsl:value-of select="." />
		</j:ArresteeJuvenileDispositionCode>
	</xsl:template>
	<xsl:template match="nc20:ActivityIdentification">
		<nc:ActivityIdentification>
			<xsl:apply-templates select="nc20:IdentificationID" />
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="nc20:ActivityDate">
		<!-- Element 42, Arrest Date -->
		<nc:ActivityDate>
			<xsl:apply-templates select="nc20:Date" />
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="j40:ChargeUCRCode">
		<!-- Element 45, UCR Arrest Offense Code -->
		<j:ArrestCharge>
			<nibrs:ChargeUCRCode>
				<xsl:value-of select="." />
			</nibrs:ChargeUCRCode>
		</j:ArrestCharge>
	</xsl:template>
	<xsl:template match="j40:ArrestCategoryCode">
		<!-- Element 43, Type Of Arrest -->
		<j:ArrestCategoryCode>
			<xsl:value-of select="." />
		</j:ArrestCategoryCode>
	</xsl:template>
	<xsl:template match="j40:ArrestSubjectCountCode">
		<!-- Element 44, Multiple Arrestee Segments Indicator -->
		<j:ArrestSubjectCountCode>
			<xsl:value-of select="." />
		</j:ArrestSubjectCountCode>
	</xsl:template>
	<xsl:template match="ndexia:Organization" mode="enforcement_unit">
		<!-- Element 44, Multiple Arrestee Segments Indicator -->
		<j:ArrestSubjectCountCode>
			<xsl:value-of select="." />
		</j:ArrestSubjectCountCode>
	</xsl:template>
	<xsl:template match="ndexia:OffenseCargoTheftIndicator">
		<!-- Element 2A, Cargo Theft Indicator: True/False -->
		<j:OffenseCargoTheftIndicator>
			<xsl:value-of select="." />
		</j:OffenseCargoTheftIndicator>
	</xsl:template>
	<xsl:template match="ndexia:VictimToSubjectRelationshipCode">
		<!-- Element 24, Victim Connected to UCR Offense Code -->
		<j:VictimToSubjectRelationshipCode>
			<xsl:value-of select="." />
		</j:VictimToSubjectRelationshipCode>
	</xsl:template>
</xsl:stylesheet>
