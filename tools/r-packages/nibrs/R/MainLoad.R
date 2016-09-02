# Unless explicitly acquired and licensed from Licensor under another license, the contents of
# this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
# versions as allowed by the RPL, and You may not copy or use this file in either source code
# or executable form, except in compliance with the terms and conditions of the RPL
# All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
# WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
# WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
# PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
# governing rights and limitations under the RPL.
#
# http://opensource.org/licenses/RPL-1.5
#
# Copyright 2012-2016 SEARCH--The National Consortium for Justice Information and Statistics

# agencyFile="/opt/data/ICPSR_35158/DS0001/35158-0001-Data.txt"
# incidentFile="/opt/data/NIBRS/2013/ICPSR_36121/DS0001/Ohio.txt"

#' Loads data from NIBRS extract files maintained by ICPSR.
#' @title Loads data from ICPSR NIBRS files
#' @param agencyFile file containing list of Agency ORIs and attributes (ICPSR "study" 35158)
#' @param incidentFiles the level DS0001 file from ICPSR (character vector)
#' @param versionYears the years covered by the incident files (allows the format to change from year to year...which it does)
#' @export
loadICPSRData <- function(agencyFile, incidentFiles, versionYears, maxRecords = -1) {

  ret <- list()

  agencies <- loadAgencyFile(agencyFile)
  Agency <- buildAgencyTable(agencies)

  idx <- 1

  for (incidentFile in incidentFiles) {

    versionYear <- versionYears[idx]
    idx <- idx + 1

    dfs <- list()

    writeLines(paste0("Processing NIBRS data from incident file ", incidentFile))

    rawIncidents <- loadIncidentFile(incidentFile, versionYear, maxRecords=maxRecords)

    writeLines(paste0(nrow(rawIncidents), " raw incident records processed."))

    AdministrativeSegmentID <- 1:(nrow(rawIncidents))
    rawIncidents$AdministrativeSegmentID <- AdministrativeSegmentID

    AdministrativeSegment <- buildAdministrativeSegment(rawIncidents, 9, Agency)
    dfs$AdministrativeSegment <- AdministrativeSegment
    OffenseSegment <- buildOffenseSegment(rawIncidents, 9)
    dfs$OffenseSegment <- OffenseSegment %>% select(-OffenseCode)
    OffenderSuspectedOfUsing <- buildOffenderSuspectedOfUsing(OffenseSegment, rawIncidents)
    dfs$OffenderSuspectedOfUsing <- OffenderSuspectedOfUsing
    TypeCriminalActivity <- buildTypeCriminalActivity(OffenseSegment, rawIncidents)
    dfs$TypeCriminalActivity <- TypeCriminalActivity
    TypeOfWeaponForceInvolved <- buildTypeOfWeaponForceInvolved(OffenseSegment, rawIncidents)
    dfs$TypeOfWeaponForceInvolved <- TypeOfWeaponForceInvolved
    PropertySegment <- buildPropertySegment(rawIncidents, 9)
    dfs$PropertySegment <- PropertySegment
    SuspectedDrugType <- buildSuspectedDrugType(PropertySegment, rawIncidents)
    dfs$SuspectedDrugType <- SuspectedDrugType
    OffenderSegment <- buildOffenderSegment(rawIncidents, 9)
    dfs$OffenderSegment <- OffenderSegment
    VictimSegment <- buildVictimSegment(rawIncidents, 9)
    dfs$VictimSegment <- VictimSegment
    TypeInjury <- buildVictimTypeInjury(VictimSegment, rawIncidents)
    dfs$TypeInjury <- TypeInjury
    VictimOffenseAssociation <- buildVictimOffenseAssociation(VictimSegment, OffenseSegment, rawIncidents)
    dfs$VictimOffenseAssociation <- VictimOffenseAssociation
    VictimOffenderAssociation <- buildVictimOffenderAssociation(VictimSegment, OffenderSegment, rawIncidents)
    dfs$VictimOffenderAssociation <- VictimOffenderAssociation
    AggravatedAssaultHomicideCircumstances <- buildAggravatedAssaultHomicideCircumstances(VictimSegment, rawIncidents)
    dfs$AggravatedAssaultHomicideCircumstances <- AggravatedAssaultHomicideCircumstances

    if (length(ret) == 0) {
      ret <- dfs
    } else {
      ret <- combineLists(ret, dfs)
    }

  }

  ret <- c(ret, loadCodeTables())
  ret$Agency <- Agency

  ret

}

#' @import RMySQL
#' @importFrom data.table data.table
#' @export
writeDataFramesToDatabase <- function(dfs) {

  conn <- dbConnect(MySQL(), host="localhost", dbname="nibrs_analytics", username="root")

  tryCatch({

    dbClearResult(dbSendQuery(conn, "set foreign_key_checks=0"))

    dbClearResult(dbSendQuery(conn, "truncate Agency"))
    dbClearResult(dbSendQuery(conn, "truncate AdministrativeSegment"))
    dbClearResult(dbSendQuery(conn, "truncate OffenseSegment"))
    dbClearResult(dbSendQuery(conn, "truncate OffenderSuspectedOfUsing"))
    dbClearResult(dbSendQuery(conn, "truncate TypeCriminalActivity"))
    dbClearResult(dbSendQuery(conn, "truncate TypeOfWeaponForceInvolved"))
    dbClearResult(dbSendQuery(conn, "truncate PropertySegment"))
    dbClearResult(dbSendQuery(conn, "truncate SuspectedDrugType"))
    dbClearResult(dbSendQuery(conn, "truncate OffenderSegment"))
    dbClearResult(dbSendQuery(conn, "truncate VictimSegment"))
    dbClearResult(dbSendQuery(conn, "truncate VictimOffenderAssociation"))
    dbClearResult(dbSendQuery(conn, "truncate VictimOffenseAssociation"))
    dbClearResult(dbSendQuery(conn, "truncate AggravatedAssaultHomicideCircumstances"))

    writeTableToDatabase(conn, dfs$Agency, "Agency")
    writeTableToDatabase(conn, dfs$AdministrativeSegment, "AdministrativeSegment")
    writeTableToDatabase(conn, dfs$OffenseSegment, "OffenseSegment")
    writeTableToDatabase(conn, dfs$OffenderSuspectedOfUsing, "OffenderSuspectedOfUsing")
    writeTableToDatabase(conn, dfs$TypeCriminalActivity, "TypeCriminalActivity")
    writeTableToDatabase(conn, dfs$TypeOfWeaponForceInvolved, "TypeOfWeaponForceInvolved")
    writeTableToDatabase(conn, dfs$PropertySegment, "PropertySegment")
    writeTableToDatabase(conn, dfs$SuspectedDrugType, "SuspectedDrugType")
    writeTableToDatabase(conn, dfs$OffenderSegment, "OffenderSegment")
    writeTableToDatabase(conn, dfs$VictimSegment, "VictimSegment")
    writeTableToDatabase(conn, dfs$TypeInjury, "TypeInjury")
    writeTableToDatabase(conn, dfs$VictimOffenseAssociation, "VictimOffenseAssociation")
    writeTableToDatabase(conn, dfs$VictimOffenderAssociation, "VictimOffenderAssociation")
    writeTableToDatabase(conn, dfs$AggravatedAssaultHomicideCircumstances, "AggravatedAssaultHomicideCircumstances")
    writeTableToDatabase(conn, dfs$UCROffenseCodeType, "UCROffenseCodeType")

    for (ctn in names(dfs)[grepl(".+Type$", names(dfs)) & !(names(dfs) %in% c('SuspectedDrugType', 'UCROffenseCodeType'))]) {
      # we had to shorten these names to get them to fit on Excel tabs
      dbCtn <- ctn
      if (ctn == 'AggAssaultHomicideCircType') {
        dbCtn <- 'AggravatedAssaultHomicideCircumstancesType'
      } else if (ctn == 'AddlJustHomicideCircType') {
        dbCtn <- 'AdditionalJustifiableHomicideCircumstancesType'
      } else if (ctn == 'VORelationshipType') {
        dbCtn <- 'VictimOffenderRelationshipType'
      } else if (ctn == 'MultipleArresteeIndicatorType') {
        dbCtn <- 'MultipleArresteeSegmentsIndicatorType'
      } else if (ctn == 'DispoOfArresteeUnder18Type') {
        dbCtn <- 'DispositionOfArresteeUnder18Type'
      }
      dbClearResult(dbSendQuery(conn, paste0("truncate ", dbCtn)))
      df <- dfs[[ctn]]
      nameWithoutType <- gsub("(.+)Type$", "\\1", dbCtn)
      colnames(df) <- c(paste0(dbCtn, "ID"), paste0(nameWithoutType, "Code"), paste0(nameWithoutType, "Description"))
      writeTableToDatabase(conn, df, dbCtn)
    }

  }, finally = {

    dbDisconnect(conn)

  })

}

#' @import RMySQL
writeTableToDatabase <- function(conn, df, tableName) {
  writeLines(paste0("Writing ", nrow(df), " ", tableName, " rows to database"))
  dbWriteTable(conn=conn, name=tableName, value=data.table(df), append=TRUE, row.names = FALSE)
  invisible()
}

#' Combines data frame lists from multiple years
#' @import dplyr
#' @export
combineLists <- function(dfs1, dfs2) {

  dfs1[['AdministrativeSegment']] <- bind_rows(dfs1[['AdministrativeSegment']], dfs2[['AdministrativeSegment']])
  dfs1[['OffenseSegment']] <- bind_rows(dfs1[['OffenseSegment']], dfs2[['OffenseSegment']])
  dfs1[['OffenderSuspectedOfUsing']] <- bind_rows(dfs1[['OffenderSuspectedOfUsing']], dfs2[['OffenderSuspectedOfUsing']])
  dfs1[['TypeCriminalActivity']] <- bind_rows(dfs1[['TypeCriminalActivity']], dfs2[['TypeCriminalActivity']])
  dfs1[['TypeOfWeaponForceInvolved']] <- bind_rows(dfs1[['TypeOfWeaponForceInvolved']], dfs2[['TypeOfWeaponForceInvolved']])
  dfs1[['PropertySegment']] <- bind_rows(dfs1[['PropertySegment']], dfs2[['PropertySegment']])
  dfs1[['SuspectedDrugType']] <- bind_rows(dfs1[['SuspectedDrugType']], dfs2[['SuspectedDrugType']])
  dfs1[['OffenderSegment']] <- bind_rows(dfs1[['OffenderSegment']], dfs2[['OffenderSegment']])
  dfs1[['VictimSegment']] <- bind_rows(dfs1[['VictimSegment']], dfs2[['VictimSegment']])
  dfs1[['VictimOffenderAssociation']] <- bind_rows(dfs1[['VictimOffenderAssociation']], dfs2[['VictimOffenderAssociation']])
  dfs1[['VictimOffenseAssociation']] <- bind_rows(dfs1[['VictimOffenseAssociation']], dfs2[['VictimOffenseAssociation']])
  dfs1[['AggravatedAssaultHomicideCircumstances']] <- bind_rows(dfs1[['AggravatedAssaultHomicideCircumstances']], dfs2[['AggravatedAssaultHomicideCircumstances']])

  dfs1

}
