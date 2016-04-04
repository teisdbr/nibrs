<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:nibrs="http://fbi.gov/cjis/nibrs/4.0" xmlns:nibrscodes="http://fbi.gov/cjis/nibrs/nibrs-codes/4.0" xmlns:cjis="http://fbi.gov/cjis/1.0" xmlns:cjiscodes="http://fbi.gov/cjis/cjis-codes/1.0" xmlns:i="http://release.niem.gov/niem/appinfo/3.0/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/" xmlns:niem-xsd="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:s="http://release.niem.gov/niem/structures/3.0/" xmlns:nc20="http://niem.gov/niem/niem-core/2.0" xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1" xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:s20="http://niem.gov/niem/structures/2.0" xmlns:j40="http://niem.gov/niem/domains/jxdm/4.0" xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1" xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:variable name="lexsDigest" select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest"/>
	<xsl:variable name="ndexiaReport" select="/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/ndexia:IncidentReport"/>
	<xsl:template match="/">
		<nibrs:Report>
			<xsl:apply-templates select="." mode="header"/>
			<xsl:apply-templates select="." mode="incident"/>
		</nibrs:Report>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="/" mode="header">
		<nibrs:ReportHeader>
			<xsl:apply-templates select="$ndexiaReport/ndexia:NIBRSReportCategoryCode"/>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityOrganization/lexsdigest:Metadata/nc20:ReportedDate/nc20:YearMonth"/>
			<xsl:apply-templates select="$ndexiaReport/ndexia:Organization/j40:OrganizationAugmentation/j40:OrganizationORIIdentification/nc20:IdentificationID" mode="reporting"/>
		</nibrs:ReportHeader>
	</xsl:template>
	<xsl:template match="/" mode="incident">
		<nc:incident>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityActivity/nc20:Activity[nc20:ActivityCategoryText='Incident']/nc20:ActivityIdentification/nc20:IdentificationID" mode="incident_id"/>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityActivity/nc20:Activity[nc20:ActivityCategoryText='Incident']/nc20:ActivityDate/nc20:DateTime" mode="incident_date"/>
			<xsl:apply-templates select="$lexsDigest/lexsdigest:EntityActivity/nc20:Activity[nc20:ActivityCategoryText='Incident']/nc20:ActivityDate/nc20:DateTime" mode="incident_date"/>
			

			
			
		</nc:incident>
	</xsl:template>
	<!-- SECTION -->
	<xsl:template match="ndexia:NIBRSReportCategoryCode">
		<nibrs:NIBRSReportCategoryCode>
			<xsl:value-of select="."/>
		</nibrs:NIBRSReportCategoryCode>
	</xsl:template>
	<xsl:template match="nc20:YearMonth">
		<nibrs:ReportDate>
			<nc:YearMonthDate>
				<xsl:value-of select="."/>
			</nc:YearMonthDate>
		</nibrs:ReportDate>
	</xsl:template>
	<xsl:template match="j40:OrganizationORIIdentification/nc20:IdentificationID" mode="reporting">
		<nibrs:ReportingAgency>
			<j:OrganizationAugmentation>
				<j:OrganizationORIIdentification>
					<nc:IdentificationID>
						<xsl:value-of select="."/>
					</nc:IdentificationID>
				</j:OrganizationORIIdentification>
			</j:OrganizationAugmentation>
		</nibrs:ReportingAgency>
	</xsl:template>
	
	<xsl:template match="nc20:ActivityIdentification/nc20:IdentificationID" mode="incident_id">
		<nc:ActivityIdentification>
			<!-- Element 2, Incident Number -->
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	
		<xsl:template match="nc20:ActivityDate/nc20:DateTime" mode="incident_date">
<nc:ActivityDate>
  <!-- Element 3, Incident Date and Hour-->
                <nc:DateTime>
         <xsl:value-of select="."/>
                </nc:DateTime>
            </nc:ActivityDate>
	</xsl:template>
	
	
	
	
</xsl:stylesheet>
