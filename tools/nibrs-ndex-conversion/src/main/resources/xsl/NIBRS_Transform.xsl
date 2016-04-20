<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:nibrs="http://fbi.gov/cjis/nibrs/4.0"
	xmlns:nibrs-ext="http://ojbc.org/IEPD/Extensions/NIBRSStructuredPayload/1.0"
	xmlns:nibrscodes="http://fbi.gov/cjis/nibrs/nibrs-codes/4.0"
	xmlns:cjis="http://fbi.gov/cjis/1.0" xmlns:cjiscodes="http://fbi.gov/cjis/cjis-codes/1.0"
	xmlns:i="http://release.niem.gov/niem/appinfo/3.0/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" xmlns:niem-xsd="http://release.niem.gov/niem/proxy/xsd/3.0/"
	xmlns:s="http://release.niem.gov/niem/structures/3.0/" xmlns:nc20="http://niem.gov/niem/niem-core/2.0"
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1"
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:s20="http://niem.gov/niem/structures/2.0"
	xmlns:j40="http://niem.gov/niem/domains/jxdm/4.0" xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1"
	xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:variable name="DIGEST"
		select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest" />
	<xsl:variable name="NDEXIA"
		select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport" />
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
	<!-- SECTION -->
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
		<xsl:variable name="OFFENSE"
		select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest" />
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
				select="$NDEXIA/ndexia:TangibleItem[ndexia:TangibleItemAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$itemID]/j40:PropertyCategoryNIBRSPropertyCategoryCode" />
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
			mode="person_victim" />
		<xsl:apply-templates select="lexsdigest:Person[../j40:IncidentSubject]"
			mode="person_subject" />
		<xsl:apply-templates select="lexsdigest:Person[../j40:ArrestSubject]"
			mode="person_arrestee" />
	</xsl:template>
	<!-- Enforcement Official -->
	<xsl:template match="j40:EnforcementOfficial" mode="official">
		<xsl:variable name="officerID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<j:EnforcementOfficial>
			<nc:RoleOfPerson>
				<xsl:attribute name="s:id"><xsl:value-of
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
				<xsl:attribute name="s:id"><xsl:value-of
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
				<xsl:attribute name="s:id"><xsl:value-of
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
				<xsl:attribute name="s:id"><xsl:value-of
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
	<xsl:template match="ndexia:Offense" mode="cargo">
		<cjis:IncidentAugmentation>
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
	<!-- COMMENT -->
	<xsl:template match="lexsdigest:Person" mode="person_victim">
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
				select="	
$NDEXIA/ndexia:Person[ndexia:PersonAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/nc20:PersonRaceCode" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityPerson/lexsdigest:Person[@s20:id=/$DIGEST/lexsdigest:EntityPerson/j40:EnforcementOfficial/nc20:RoleOfPersonReference/@s20:ref]/nc20:PersonResidentCode" />
			<xsl:apply-templates select="nc20:PersonSexCode" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="person_subject">
		<xsl:variable name="personID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<nc:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates select="nc20:PersonAgeMeasure" />
			<xsl:apply-templates
				select="$NDEXIA/ndexia:Person[ndexia:PersonAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/nc20:PersonEthnicityCode" />
			<xsl:apply-templates
				select="	
$NDEXIA/ndexia:Person[ndexia:PersonAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/nc20:PersonRaceCode" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityPerson/lexsdigest:Person[@s20:id=/$DIGEST/lexsdigest:EntityPerson/j40:EnforcementOfficial/nc20:RoleOfPersonReference/@s20:ref]/nc20:PersonResidentCode" />
			<xsl:apply-templates select="nc20:PersonSexCode" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="lexsdigest:Person" mode="person_arrestee">
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
				select="	
$NDEXIA/ndexia:Person[ndexia:PersonAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$personID]/nc20:PersonRaceCode" />
			<xsl:apply-templates
				select="$DIGEST/lexsdigest:EntityPerson/lexsdigest:Person[@s20:id=/$DIGEST/lexsdigest:EntityPerson/j40:EnforcementOfficial/nc20:RoleOfPersonReference/@s20:ref]/nc20:PersonResidentCode" />
			<xsl:apply-templates select="nc20:PersonSexCode" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="nc20:Organization" mode="unit">
		<xsl:variable name="orgID">
			<xsl:value-of select="@s20:id" />
		</xsl:variable>
		<xsl:apply-templates
			select="$NDEXIA/ndexia:EnforcementUnit[ndexia:EnforcementUnitAugmentation/lexslib:SameAsDigestReference/@lexslib:ref=$orgID]/j40:OrganizationAugmentation/j40:OrganizationORIIdentification"
			mode="unit" />
	</xsl:template>
	<!-- COMMENT -->
	<!-- SECTION -->
	<xsl:template match="nc20:MeasureRangeValue">
		<nc:MeasureRangeValue>
			<xsl:apply-templates select="nc20:RangeMinimumValue" />
			<xsl:apply-templates select="nc20:RangeMaximumValue" />
		</nc:MeasureRangeValue>
	</xsl:template>
	<!-- BOTTOM SECTION -->
	<xsl:template match="ndexia:NIBRSReportCategoryCode">
		<nibrs:NIBRSReportCategoryCode>
			<xsl:value-of select="." />
		</nibrs:NIBRSReportCategoryCode>
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
			<xsl:value-of select="." />
		</nibrs:OffenseUCRCode>
	</xsl:template>
	<xsl:template match="ndexia:CriminalActivityCategoryCode">
		<!-- Element 12, Type Criminal Activity/Gang Information -->
		<nibrs:CriminalActivityCategoryCode>
			<xsl:value-of select="." />
		</nibrs:CriminalActivityCategoryCode>
	</xsl:template>
	<xsl:template match="ndexia:OffenseBiasMotivationCode">
		<!-- Element 8A, Bias Motivation -->
		<j:OffenseFactorBiasMotivationCode>
			<xsl:value-of select="." />
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
			<xsl:value-of select="." />
		</j:LocationCategoryCode>
	</xsl:template>
	<xsl:template match="ndexia:ItemStatusCode">
		<!-- Element 14, Type Property Loss/etc Substituted for nc:ItemStatus -->
		<nc:ItemStatus>
			<cjis:ItemStatusCode>
				<xsl:value-of select="." />
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
	<xsl:template match="j40:PropertyCategoryNIBRSPropertyCategoryCode">
		<!-- Element 15, Property Description -->
		<j:ItemCategoryNIBRSPropertyCategoryCode>
			<xsl:value-of select="." />
		</j:ItemCategoryNIBRSPropertyCategoryCode>
	</xsl:template>
	<xsl:template match="nc20:Drug/j40:DrugDEACode">
		<!-- Element 20, Suspected Involved Drug Type -->
		<j:DrugCategoryCode>
			<xsl:value-of select="." />
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
				<xsl:value-of select="." />
			</j:InjuryCategoryCode>
		</nc:PersonInjury>
	</xsl:template>
	<xsl:template match="nc20:PersonRaceCode">
		<!-- Element 28, Race of Victim -->
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
			<xsl:value-of select="." />
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
			<xsl:value-of select="." />
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
