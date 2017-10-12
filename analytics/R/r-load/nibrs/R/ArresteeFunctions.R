# Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#' @importFrom readr read_fwf
loadArresteeFile <- function(file, maxRecords = -1) {
  columnSpecsFile <- system.file("raw", "ArresteeFileFormat.txt", package=getPackageName())
  columnSpecs <- getColumnSpecs(columnSpecsFile)
  read_fwf(file=file, col_positions = fwf_positions(start = columnSpecs$start, end = columnSpecs$end, col_names = columnSpecs$name),
           col_types=paste(columnSpecs$type, collapse=""), n_max = maxRecords) %>%
    filter(SEGMENT=='07') %>%
    mutate(ArrestReportSegmentID=row_number())
}

#' @importFrom DBI dbClearResult dbSendQuery
truncateArresteeSegments <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate ArresteeSegment"))
}

#' @importFrom DBI dbClearResult dbSendQuery
truncateArrestReportSegments <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate ArrestReportSegment"))
}

#' @import dplyr
#' @importFrom DBI dbWriteTable
#' @importFrom lubridate month year ymd
writeArresteeSegments <- function(conn, rawIncidentsDataFrame, segmentActionTypeTypeID) {

  ArresteeSegment <- bind_cols(

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60061:V60063) %>%
      gather(V_ArresteeSequenceNumber, ArresteeSequenceNumber, V60061:V60063),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60071:V60073) %>%
      gather(V_ArrestTransactionNumber, ArrestTransactionNumber, V60071:V60073) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60081:V60083) %>%
      gather(V_ArrestDate, ArrestDate, V60081:V60083) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60091:V60093) %>%
      gather(V_TypeOfArrest, TypeOfArrestTypeID, V60091:V60093) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60101:V60103) %>%
      gather(V_MultipleArresteeSegmentsIndicator, MultipleArresteeSegmentsIndicatorTypeID, V60101:V60103) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60141:V60143) %>%
      gather(V_ArresteeAge, AgeOfArresteeMin, V60141:V60143) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60151:V60153) %>%
      gather(V_ArresteeSex, SexOfPersonTypeID, V60151:V60153) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60161:V60163) %>%
      gather(V_ArresteeRace, RaceOfPersonTypeID, V60161:V60163) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60171:V60173) %>%
      gather(V_ArresteeEthnicity, EthnicityOfPersonTypeID, V60171:V60173) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60181:V60183) %>%
      gather(V_ArresteeResidentStatus, ResidentStatusOfPersonTypeID, V60181:V60183) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60191:V60193) %>%
      gather(V_ArresteeDisp18, DispositionOfArresteeUnder18TypeID, V60191:V60193) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V60111:V60113) %>%
      gather(V_OffenseCode, UCROffenseCodeTypeID, V60111:V60113) %>%
      select(-AdministrativeSegmentID)

  ) %>%
    filter(ArresteeSequenceNumber > 0) %>% select(-starts_with("V_")) %>%
    mutate(ArrestDate=ifelse(ArrestDate %in% c(-5,-8), NA, ArrestDate)) %>%
    mutate(ArrestDate=ymd(ArrestDate), ArrestDateID=createKeyFromDate(ArrestDate)) %>%
    mutate(AgeOfArresteeMin=ifelse(AgeOfArresteeMin < 0, NA, AgeOfArresteeMin),
           AgeOfArresteeMax=AgeOfArresteeMin,
           SexOfPersonTypeID=ifelse(SexOfPersonTypeID < 0, 9, SexOfPersonTypeID+1),
           RaceOfPersonTypeID=ifelse(RaceOfPersonTypeID < 0, 9, RaceOfPersonTypeID),
           EthnicityOfPersonTypeID=ifelse(EthnicityOfPersonTypeID < 0, 9, EthnicityOfPersonTypeID),
           ResidentStatusOfPersonTypeID=ifelse(ResidentStatusOfPersonTypeID < 0, 9, ResidentStatusOfPersonTypeID+1),
           DispositionOfArresteeUnder18TypeID=ifelse(DispositionOfArresteeUnder18TypeID < 0, 9, DispositionOfArresteeUnder18TypeID+1),
           UCROffenseCodeTypeID=ifelse(UCROffenseCodeTypeID < 0, 999, UCROffenseCodeTypeID),
           MultipleArresteeSegmentsIndicatorTypeID=ifelse(MultipleArresteeSegmentsIndicatorTypeID < 0, 9, MultipleArresteeSegmentsIndicatorTypeID),
           SegmentActionTypeTypeID=segmentActionTypeTypeID) %>%
    ungroup() %>%
    mutate(ArresteeSegmentID=row_number())

  writeLines(paste0("Writing ", nrow(ArresteeSegment), " arrestee segments to database"))

  dbWriteTable(conn=conn, name="ArresteeSegment", value=ArresteeSegment, append=TRUE, row.names = FALSE)

  ArresteeSegment

}

#' @import dplyr
#' @importFrom DBI dbWriteTable
writeArresteeSegmentWasArmedWith <- function(conn, arresteeSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_rows(
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, ArresteeSequenceNumber=V60061, V60121:V60123) %>%
      filter(ArresteeSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V60121:V60123) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, ArresteeSequenceNumber=V60062, V60131:V60133) %>%
      filter(ArresteeSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V60131:V60133) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0)
  )

  ArresteeSegmentWasArmedWith <- left_join(tempDf,
                                         select(arresteeSegmentDataFrame, AdministrativeSegmentID, ArresteeSequenceNumber, ArresteeSegmentID),
                                         by=c("AdministrativeSegmentID", "ArresteeSequenceNumber")) %>%
    select(ArresteeSegmentID, ArresteeWasArmedWithTypeID=Pivot)

  # TypeOfWeaponForceInvolvedTranslationDf defined in CommonFunctions.R
  ArresteeSegmentWasArmedWith <- left_join(ArresteeSegmentWasArmedWith, TypeOfWeaponForceInvolvedTranslationDf,
                                         by=c("ArresteeWasArmedWithTypeID"="icpsrCode")) %>%
    mutate(ArresteeWasArmedWithTypeID=ifelse(is.na(ArresteeWasArmedWithTypeID), 999, ArresteeWasArmedWithTypeID))

  missingSegmentIDs <- setdiff(arresteeSegmentDataFrame$ArresteeSegmentID, ArresteeSegmentWasArmedWith$ArresteeSegmentID)

  ArresteeSegmentWasArmedWith <- bind_rows(ArresteeSegmentWasArmedWith,
                                         tibble(ArresteeSegmentID=missingSegmentIDs,
                                                ArresteeWasArmedWithTypeID=rep(x=990, times=length(missingSegmentIDs)),
                                                AutomaticWeaponIndicator=rep(x="N", times=length(missingSegmentIDs)))) %>%
    mutate(ArresteeSegmentWasArmedWithID=row_number())

  writeLines(paste0("Writing ", nrow(ArresteeSegmentWasArmedWith), " ArresteeSegmentWasArmedWith association rows to database"))

  dbWriteTable(conn=conn, name="ArresteeSegmentWasArmedWith", value=ArresteeSegmentWasArmedWith, append=TRUE, row.names = FALSE)

  ArresteeSegmentWasArmedWith

}

#' @import dplyr
#' @importFrom DBI dbWriteTable
#' @importFrom lubridate month year ymd
writeArrestReportSegments <- function(conn, rawArresteeDataFrame, segmentActionTypeTypeID, agencyDataFrame) {

  currentMonth <- formatC(month(Sys.Date()), width=2, flag="0")
  currentYear <- year(Sys.Date()) %>% as.integer()

  ArrestReportSegment <- rawArresteeDataFrame %>%
    processingMessage('Group B Arrest') %>%
    select(ArresteeSequenceNumber=V6006,
           ArrestTransactionNumber=V6007,
           ArrestDate=V6008,
           TypeOfArrestTypeID=V6009,
           UCROffenseCodeTypeID=V6011,
           AgeOfArresteeMin=V6014,
           SexOfPersonTypeID=V6015,
           RaceOfPersonTypeID=V6016,
           EthnicityOfPersonTypeID=V6017,
           ResidentStatusOfPersonTypeID=V6018,
           DispositionOfArresteeUnder18TypeID=V6019,
           ORI,
           ArrestReportSegmentID
           ) %>%
    mutate(ArrestDate=ifelse(ArrestDate %in% c(-5,-8), NA, ArrestDate)) %>%
    mutate(ArrestDate=ymd(ArrestDate), ArrestDateID=createKeyFromDate(ArrestDate)) %>%
    mutate(AgeOfArresteeMin=ifelse(AgeOfArresteeMin < 0, NA, AgeOfArresteeMin),
           AgeOfArresteeMax=AgeOfArresteeMin,
           SexOfPersonTypeID=ifelse(SexOfPersonTypeID < 0, 9, SexOfPersonTypeID+1),
           RaceOfPersonTypeID=ifelse(RaceOfPersonTypeID < 0, 9, RaceOfPersonTypeID),
           EthnicityOfPersonTypeID=ifelse(EthnicityOfPersonTypeID < 0, 9, EthnicityOfPersonTypeID),
           ResidentStatusOfPersonTypeID=ifelse(ResidentStatusOfPersonTypeID < 0, 9, ResidentStatusOfPersonTypeID+1),
           DispositionOfArresteeUnder18TypeID=ifelse(DispositionOfArresteeUnder18TypeID < 0, 9, DispositionOfArresteeUnder18TypeID+1),
           UCROffenseCodeTypeID=ifelse(UCROffenseCodeTypeID < 0, 999, UCROffenseCodeTypeID),
           MonthOfTape=currentMonth, YearOfTape=currentYear, CityIndicator=NA, SegmentActionTypeTypeID=segmentActionTypeTypeID
    ) %>%
    ungroup() %>%
    left_join(agencyDataFrame %>% select(AgencyID, ORI=AgencyORI), by='ORI')

  writeLines(paste0("Writing ", nrow(ArrestReportSegment), " arrest report segments to database"))

  dbWriteTable(conn=conn, name="ArrestReportSegment", value=ArrestReportSegment, append=TRUE, row.names = FALSE)

  ArrestReportSegment

}

#' @import dplyr
#' @importFrom DBI dbWriteTable
writeArrestReportSegmentWasArmedWith <- function(conn, arrestReportSegmentDataFrame, rawArresteeDataFrame) {

  ArrestReportSegmentWasArmedWith <- rawArresteeDataFrame %>%
    select(ArrestReportSegmentID, V6012:V6013) %>%
    gather(V_Pivot, ArresteeWasArmedWithTypeID, V6012:V6013) %>%
    select(-V_Pivot) %>%
    filter(ArresteeWasArmedWithTypeID > 0)

  # TypeOfWeaponForceInvolvedTranslationDf defined in CommonFunctions.R
  ArrestReportSegmentWasArmedWith <- left_join(ArrestReportSegmentWasArmedWith, TypeOfWeaponForceInvolvedTranslationDf,
                                           by=c("ArresteeWasArmedWithTypeID"="icpsrCode")) %>%
    mutate(ArresteeWasArmedWithTypeID=ifelse(is.na(ArresteeWasArmedWithTypeID), 999, ArresteeWasArmedWithTypeID))

  missingSegmentIDs <- setdiff(arrestReportSegmentDataFrame$ArrestReportSegmentID, ArrestReportSegmentWasArmedWith$ArrestReportSegmentID)

  if (length(missingSegmentIDs) > 0) {
    ArrestReportSegmentWasArmedWith <- bind_rows(ArrestReportSegmentWasArmedWith,
                                                 tibble(ArrestReportSegmentID=missingSegmentIDs,
                                                        ArresteeWasArmedWithTypeID=rep(x=990, times=length(missingSegmentIDs)),
                                                        AutomaticWeaponIndicator=rep(x="N", times=length(missingSegmentIDs))))
  }

  ArrestReportSegmentWasArmedWith <- ArrestReportSegmentWasArmedWith %>%
    mutate(ArrestReportSegmentWasArmedWithID=row_number())

  writeLines(paste0("Writing ", nrow(ArrestReportSegmentWasArmedWith), " ArrestReportSegmentWasArmedWith association rows to database"))

  dbWriteTable(conn=conn, name="ArrestReportSegmentWasArmedWith", value=ArrestReportSegmentWasArmedWith, append=TRUE, row.names = FALSE)

  ArrestReportSegmentWasArmedWith

}


