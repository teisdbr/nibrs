<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exchange="http://gralab.org/IEPD/Exchange/ArrestWarrant/1.0/" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.0/" xmlns:scr="http://release.niem.gov/niem/domains/screening/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:template match="/Warrant">
		<!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-->
		<exchange:ArrestWarrant>
			<xsl:attribute name="xsi:schemaLocation">http://gralab.org/IEPD/Exchange/ArrestWarrant/1.0/ ../xsd/Arrest_Warrant.xsd</xsl:attribute>
			<xsl:apply-templates select="Warrant_Image[normalize-space(.) != '']"/>
			<xsl:apply-templates select="Document_ID"/>
			<nc:DocumentTitleText>Arrest Warrant Document</nc:DocumentTitleText>
			<xsl:apply-templates select="Arrest_Warrant"/>
			<xsl:apply-templates mode="address" select="Arrest_Warrant/Party[normalize-space(.) != '']"/>
			<xsl:call-template name="Residence-Association"/>
		</exchange:ArrestWarrant>
	</xsl:template>
	<!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-->
	<xsl:template match="Arrest_Warrant">
		<j:ArrestWarrant>
			<xsl:apply-templates mode="person" select="Party[normalize-space(.) != '']"/>
			<xsl:apply-templates select="Court[normalize-space(.) != '']"/>
			<xsl:apply-templates select="Warrant_Date[normalize-space(.) != '']"/>
			<xsl:apply-templates select="Judge[normalize-space(.) != '']"/>
			<xsl:apply-templates select="Bond[normalize-space(.) != '']"/>
			<scr:WarrantAugmentation>
				<xsl:apply-templates select="Charge[normalize-space(.) != '']"/>
				<xsl:apply-templates select="Warrant_Number[normalize-space(.) != '']"/>
			</scr:WarrantAugmentation>
		</j:ArrestWarrant>
	</xsl:template>
	<xsl:template match="Party" mode="person">
		<j:CourtOrderDesignatedSubject>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<nc:RoleOfPerson>
				<xsl:apply-templates select="Birth_Date[normalize-space(.) != '']"/>
				<xsl:apply-templates select="EyeColor[normalize-space(.) != '']"/>
				<xsl:apply-templates select="HairColor[normalize-space(.) != '']"/>
				<xsl:apply-templates select="Height[normalize-space(.) != '']"/>
				<nc:PersonName>
					<xsl:apply-templates select="First_Name[normalize-space(.) != '']"/>
					<xsl:apply-templates select="Middle_Name[normalize-space(.) != '']"/>
					<xsl:apply-templates select="Last_Name[normalize-space(.) != '']"/>
					<xsl:apply-templates select="Name_Suffix[normalize-space(.) != '']"/>
				</nc:PersonName>
				<xsl:apply-templates select="Sex[normalize-space(.) != '']"/>
				<xsl:apply-templates select="SSN[normalize-space(.) != '']"/>
				<xsl:apply-templates select="Weight[normalize-space(.) != '']"/>
			</nc:RoleOfPerson>
			<xsl:apply-templates select="Violent_Offender[normalize-space(.) != '']"/>
		</j:CourtOrderDesignatedSubject>
	</xsl:template>
	<xsl:template match="Court">
		<j:CourtOrderIssuingCourt>
			<nc:OrganizationIdentification>
				<xsl:apply-templates select="Court_ID[normalize-space(.) != '']"/>
				<nc:IdentificationCategoryText>Court Number</nc:IdentificationCategoryText>
			</nc:OrganizationIdentification>
			<nc:OrganizationLocation>
				<nc:Address>
					<xsl:apply-templates select="Court_Address[normalize-space(.) != '']"/>
					<xsl:apply-templates select="County_Code[normalize-space(.) != '']"/>
				</nc:Address>
			</nc:OrganizationLocation>
			<xsl:apply-templates select="Court_Name[normalize-space(.) != '']"/>
		</j:CourtOrderIssuingCourt>
	</xsl:template>
	<xsl:template match="Warrant_Date">
		<j:CourtOrderIssuingDate>
			<xsl:call-template name="ConvertDate"/>
		</j:CourtOrderIssuingDate>
	</xsl:template>
	<xsl:template match="Judge">
		<j:CourtOrderIssuingJudicialOfficial>
			<nc:RoleOfPerson>
				<xsl:apply-templates select="Judge_Signature[normalize-space(.) != '']"/>
				<xsl:apply-templates select="Judge_Name[normalize-space(.) != '']"/>
			</nc:RoleOfPerson>
		</j:CourtOrderIssuingJudicialOfficial>
	</xsl:template>
	<xsl:template match="Bond">
		<j:WarrantAppearanceBail>
			<j:BailSetAmount>
				<nc:Amount>
					<xsl:value-of select="."/>
				</nc:Amount>
			</j:BailSetAmount>
		</j:WarrantAppearanceBail>
	</xsl:template>
	<xsl:template match="Charge">
		<j:Charge>
			<j:ChargeStatute>
				<j:StatuteCodeSectionIdentification>
					<xsl:apply-templates select="Charge_Code[normalize-space(.) != '']"/>
					<xsl:apply-templates select="Charge_Code_Jurisdiction[normalize-space(.) != '']"/>
				</j:StatuteCodeSectionIdentification>
				<xsl:apply-templates select="Charge_Description[normalize-space(.) != '']"/>
			</j:ChargeStatute>
		</j:Charge>
	</xsl:template>
	<xsl:template match="Warrant_Number">
		<scr:WarrantIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
			<nc:IdentificationCategoryText>Warrant Number</nc:IdentificationCategoryText>
		</scr:WarrantIdentification>
	</xsl:template>
	<xsl:template match="Arrest_Warrant/Party" mode="address">
		<nc:Location>
			<xsl:attribute name="structures:id"><xsl:value-of select="generate-id(Home_Address)"/></xsl:attribute>
			<nc:Address>
				<xsl:apply-templates select="Home_Address[normalize-space(.) != '']"/>
				<xsl:apply-templates select="County_Code[normalize-space(.) != '']"/>
			</nc:Address>
		</nc:Location>
	</xsl:template>
	<!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-->
	<xsl:template match="Warrant_Image">
		<nc:DocumentBinary>
			<nc:Base64BinaryObject>
				<xsl:value-of select="."/>
			</nc:Base64BinaryObject>
		</nc:DocumentBinary>
	</xsl:template>
	<xsl:template match="Document_ID">
		<nc:DocumentIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
			<nc:IdentificationCategoryText>Document ID</nc:IdentificationCategoryText>
		</nc:DocumentIdentification>
	</xsl:template>
	<xsl:template match="Birth_Date">
		<nc:PersonBirthDate>
			<xsl:call-template name="ConvertDate"/>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="EyeColor">
		<j:PersonEyeColorCode>
			<xsl:value-of select="."/>
		</j:PersonEyeColorCode>
	</xsl:template>
	<xsl:template match="HairColor">
		<j:PersonHairColorCode>
			<xsl:value-of select="."/>
		</j:PersonHairColorCode>
	</xsl:template>
	<xsl:template match="Height">
		<nc:PersonHeightMeasure>
			<nc:MeasureValueText>
				<xsl:value-of select="."/>
			</nc:MeasureValueText>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="First_Name">
		<nc:PersonGivenName>
			<xsl:value-of select="."/>
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="Middle_Name">
		<nc:PersonMiddleName>
			<xsl:value-of select="."/>
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="Last_Name">
		<nc:PersonSurName>
			<xsl:value-of select="."/>
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="Name_Suffix">
		<nc:PersonNameSuffixText>
			<xsl:value-of select="."/>
		</nc:PersonNameSuffixText>
	</xsl:template>
	<xsl:template match="Sex">
		<nc:PersonSexText>
			<xsl:value-of select="."/>
		</nc:PersonSexText>
	</xsl:template>
	<xsl:template match="SSN">
		<nc:PersonSSNIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="Weight">
		<nc:PersonWeightMeasure>
			<nc:MeasureValueText>
				<xsl:value-of select="."/>
			</nc:MeasureValueText>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="Violent_Offender">
		<j:SubjectViolentOffenderIndicator>
			<xsl:call-template name="ConvertBoolean"/>
		</j:SubjectViolentOffenderIndicator>
	</xsl:template>
	<xsl:template match="Court_ID">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="Court_Address">
		<nc:AddressFullText>
			<xsl:value-of select="."/>
		</nc:AddressFullText>
	</xsl:template>
	<xsl:template match="Court_Name">
		<nc:OrganizationName>
			<xsl:value-of select="."/>
		</nc:OrganizationName>
	</xsl:template>
	<xsl:template match="Judge_Signature">
		<nc:PersonDigitizedSignatureImage>
			<nc:Base64BinaryObject>
				<xsl:value-of select="."/>
			</nc:Base64BinaryObject>
		</nc:PersonDigitizedSignatureImage>
	</xsl:template>
	<xsl:template match="Judge_Name">
		<nc:PersonName>
			<nc:PersonFullName>
				<xsl:value-of select="."/>
			</nc:PersonFullName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="Charge_Code">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="Charge_Code_Jurisdiction">
		<nc:IdentificationJurisdiction>
			<j:LocationStateNCICRESCode>
				<xsl:value-of select="."/>
			</j:LocationStateNCICRESCode>
		</nc:IdentificationJurisdiction>
	</xsl:template>
	<xsl:template match="Charge_Description">
		<j:StatuteDescriptionText>
			<xsl:value-of select="."/>
		</j:StatuteDescriptionText>
	</xsl:template>
	<xsl:template match="Home_Address">
		<nc:AddressFullText>
			<xsl:value-of select="."/>
		</nc:AddressFullText>
	</xsl:template>
	<xsl:template match="County_Code">
		<nc:LocationCountyCode>
			<xsl:value-of select="."/>
		</nc:LocationCountyCode>
	</xsl:template>
	<xsl:template name="Residence-Association">
		<nc:PersonResidenceAssociation>
			<nc:Person>
				<xsl:attribute name="structures:ref"><xsl:value-of select="generate-id(Arrest_Warrant/Party)"/></xsl:attribute>
			</nc:Person>
			<nc:Location>
				<xsl:attribute name="structures:ref"><xsl:value-of select="generate-id(Arrest_Warrant/Party/Home_Address)"/></xsl:attribute>
			</nc:Location>
		</nc:PersonResidenceAssociation>
	</xsl:template>
	<xsl:template name="ConvertDate">
		<nc:Date>
			<xsl:value-of select="substring(.,7)"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="substring(.,1,2)"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="substring(.,4,2)"/>
		</nc:Date>
	</xsl:template>
	<xsl:template name="ConvertBoolean">
		<xsl:choose>
			<xsl:when test=".='yes'">
				<xsl:value-of select="'true'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'false'"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
