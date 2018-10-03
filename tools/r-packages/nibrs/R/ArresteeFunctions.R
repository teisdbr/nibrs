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

# loadArresteeFile <- function(file, maxRecords = -1) {
#   columnSpecsFile <- system.file("raw", "ArresteeFileFormat.txt", package=getPackageName())
#   columnSpecs <- getColumnSpecs(columnSpecsFile)
#   read_fwf(file=file, col_positions = fwf_positions(start = columnSpecs$start, end = columnSpecs$end, col_names = columnSpecs$name),
#            col_types=paste(columnSpecs$type, collapse=""), n_max = maxRecords) %>%
#     filter(SEGMENT=='07') %>%
#     mutate(ArrestReportSegmentID=row_number())
# }

#' @importFrom readr read_fwf
loadArresteeFile <- function(file, versionYear, maxRecords = -1) {
  columnSpecsFile <- system.file("raw", paste0('ArresteeFileFormat-', versionYear, '.txt'), package=getPackageName())
  columnSpecs <- getColumnSpecs(columnSpecsFile)
  read_fwf(file=file, col_positions = fwf_positions(start = columnSpecs$start, end = columnSpecs$end, col_names = columnSpecs$name),
           col_types=paste(columnSpecs$type, collapse=""), n_max = maxRecords, progress=FALSE)  %>%
    filter(SEGMENT=='07') %>%
    mutate(ArrestReportSegmentID=row_number())
}

#' @importFrom DBI dbClearResult dbSendQuery
truncateArresteeSegments <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate ArresteeSegment"))
  dbClearResult(dbSendQuery(conn, "truncate ArresteeSegmentWasArmedWith"))
}

#' @importFrom DBI dbClearResult dbSendQuery
truncateArrestReportSegments <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate ArrestReportSegment"))
  dbClearResult(dbSendQuery(conn, "truncate ArrestReportSegmentWasArmedWith"))
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
           SexOfPersonTypeID=ifelse(SexOfPersonTypeID < 0, 99999, SexOfPersonTypeID+1),
           RaceOfPersonTypeID=ifelse(RaceOfPersonTypeID < 0, 99999, RaceOfPersonTypeID),
           EthnicityOfPersonTypeID=ifelse(EthnicityOfPersonTypeID < 0, 99999, EthnicityOfPersonTypeID),
           ResidentStatusOfPersonTypeID=ifelse(ResidentStatusOfPersonTypeID < 0, 99999, ResidentStatusOfPersonTypeID+1),
           DispositionOfArresteeUnder18TypeID=ifelse(DispositionOfArresteeUnder18TypeID < 0, 99999, DispositionOfArresteeUnder18TypeID+1),
           UCROffenseCodeTypeID=ifelse(UCROffenseCodeTypeID < 0, 99999, UCROffenseCodeTypeID),
           MultipleArresteeSegmentsIndicatorTypeID=ifelse(MultipleArresteeSegmentsIndicatorTypeID < 0, 99999, MultipleArresteeSegmentsIndicatorTypeID),
           SegmentActionTypeTypeID=segmentActionTypeTypeID) %>%
    ungroup() %>%
    mutate(ArresteeSegmentID=row_number())

  writeLines(paste0("Writing ", nrow(ArresteeSegment), " arrestee segments to database"))

  dbWriteTable(conn=conn, name="ArresteeSegment", value=ArresteeSegment, append=TRUE, row.names = FALSE)

  attr(ArresteeSegment, 'type') <- 'FT'

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
    mutate(ArresteeWasArmedWithTypeID=ifelse(is.na(ArresteeWasArmedWithTypeID), 99999, ArresteeWasArmedWithTypeID))

  missingSegmentIDs <- setdiff(arresteeSegmentDataFrame$ArresteeSegmentID, ArresteeSegmentWasArmedWith$ArresteeSegmentID)

  ArresteeSegmentWasArmedWith <- bind_rows(ArresteeSegmentWasArmedWith,
                                         tibble(ArresteeSegmentID=missingSegmentIDs,
                                                ArresteeWasArmedWithTypeID=rep(x=99998, times=length(missingSegmentIDs)),
                                                AutomaticWeaponIndicator=rep(x="N", times=length(missingSegmentIDs)))) %>%
    mutate(ArresteeSegmentWasArmedWithID=row_number())

  writeLines(paste0("Writing ", nrow(ArresteeSegmentWasArmedWith), " ArresteeSegmentWasArmedWith association rows to database"))

  dbWriteTable(conn=conn, name="ArresteeSegmentWasArmedWith", value=ArresteeSegmentWasArmedWith, append=TRUE, row.names = FALSE)

  attr(ArresteeSegmentWasArmedWith, 'type') <- 'AT'

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
           SexOfPersonTypeID=ifelse(SexOfPersonTypeID < 0, 99999, SexOfPersonTypeID+1),
           RaceOfPersonTypeID=ifelse(RaceOfPersonTypeID < 0, 99999, RaceOfPersonTypeID),
           EthnicityOfPersonTypeID=ifelse(EthnicityOfPersonTypeID < 0, 99999, EthnicityOfPersonTypeID),
           ResidentStatusOfPersonTypeID=ifelse(ResidentStatusOfPersonTypeID < 0, 99999, ResidentStatusOfPersonTypeID+1),
           DispositionOfArresteeUnder18TypeID=ifelse(DispositionOfArresteeUnder18TypeID < 0, 99999, DispositionOfArresteeUnder18TypeID+1),
           UCROffenseCodeTypeID=ifelse(UCROffenseCodeTypeID < 0, 99999, UCROffenseCodeTypeID),
           MonthOfTape=currentMonth, YearOfTape=currentYear, CityIndicator=NA, SegmentActionTypeTypeID=segmentActionTypeTypeID
    ) %>%
    ungroup() %>%
    left_join(agencyDataFrame %>% select(AgencyID, ORI=AgencyORI), by='ORI')

  writeLines(paste0("Writing ", nrow(ArrestReportSegment), " arrest report segments to database"))

  dbWriteTable(conn=conn, name="ArrestReportSegment", value=ArrestReportSegment, append=TRUE, row.names = FALSE)

  attr(ArrestReportSegment, 'type') <- 'FT'

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
    mutate(ArresteeWasArmedWithTypeID=ifelse(is.na(ArresteeWasArmedWithTypeID), 99999, ArresteeWasArmedWithTypeID))

  missingSegmentIDs <- setdiff(arrestReportSegmentDataFrame$ArrestReportSegmentID, ArrestReportSegmentWasArmedWith$ArrestReportSegmentID)

  if (length(missingSegmentIDs) > 0) {
    ArrestReportSegmentWasArmedWith <- bind_rows(ArrestReportSegmentWasArmedWith,
                                                 tibble(ArrestReportSegmentID=missingSegmentIDs,
                                                        ArresteeWasArmedWithTypeID=rep(x=99998, times=length(missingSegmentIDs)),
                                                        AutomaticWeaponIndicator=rep(x="N", times=length(missingSegmentIDs))))
  }

  ArrestReportSegmentWasArmedWith <- ArrestReportSegmentWasArmedWith %>%
    mutate(ArrestReportSegmentWasArmedWithID=row_number())

  writeLines(paste0("Writing ", nrow(ArrestReportSegmentWasArmedWith), " ArrestReportSegmentWasArmedWith association rows to database"))

  dbWriteTable(conn=conn, name="ArrestReportSegmentWasArmedWith", value=ArrestReportSegmentWasArmedWith, append=TRUE, row.names = FALSE)

  attr(ArrestReportSegmentWasArmedWith, 'type') <- 'AT'

  ArrestReportSegmentWasArmedWith

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom stringr str_sub
#' @importFrom DBI dbWriteTable
writeRawArresteeSegmentTables <- function(conn, inputDfList, tableList) {

  dfName <- load(inputDfList[7])
  arresteeSegmentDf <- get(dfName) %>%  mutate_if(is.factor, as.character)
  rm(list=dfName)

  if ('ORI' %in% colnames(arresteeSegmentDf)) {
    arresteeSegmentDf <- arresteeSegmentDf %>% rename(V6003=ORI, V6004=INCNUM)
  }

  arresteeSegmentDf <- arresteeSegmentDf  %>%
    inner_join(tableList$Agency %>% select(AgencyORI), by=c('V6003'='AgencyORI')) %>%
    inner_join(tableList$AdministrativeSegment %>% select(ORI, IncidentNumber, AdministrativeSegmentID), by=c('V6003'='ORI', 'V6004'='IncidentNumber')) %>%
    mutate(ArresteeSegmentID=row_number(), SegmentActionTypeTypeID=99998L)

  ArresteeSegment <- arresteeSegmentDf %>%
    mutate(ArresteeSequenceNumber=as.integer(V6006),
           ArrestTransactionNumber=V6007,
           ArrestDate=as_date(ifelse(is.na(V6008), NA, ymd(V6008))),
           ArrestDateID=createKeyFromDate(ArrestDate),
           AgeOfArresteeMin=as.integer(V6014),
           AgeOfArresteeMax=AgeOfArresteeMin) %>%
    left_join(tableList$MultipleArresteeSegmentsIndicatorType %>% select(MultipleArresteeSegmentsIndicatorTypeID, StateCode),
              by=c('V6010'='StateCode')) %>%
    left_join(tableList$RaceOfPersonType %>% select(RaceOfPersonTypeID, StateCode), by=c('V6016'='StateCode')) %>%
    left_join(tableList$SexOfPersonType %>% select(SexOfPersonTypeID, StateCode), by=c('V6015'='StateCode')) %>%
    left_join(tableList$EthnicityOfPersonType %>% select(EthnicityOfPersonTypeID, StateCode), by=c('V6017'='StateCode')) %>%
    left_join(tableList$ResidentStatusOfPersonType %>% select(ResidentStatusOfPersonTypeID, StateCode), by=c('V6018'='StateCode')) %>%
    left_join(tableList$DispositionOfArresteeUnder18Type %>% select(DispositionOfArresteeUnder18TypeID, StateCode),
              by=c('V6019'='StateCode')) %>%
    left_join(tableList$UCROffenseCodeType %>% select(UCROffenseCodeTypeID, StateCode), by=c('V6011'='StateCode')) %>%
    left_join(tableList$TypeOfArrestType %>% select(TypeOfArrestTypeID, StateCode), by=c('V6009'='StateCode')) %>%
    mutate(RaceOfPersonTypeID=ifelse(is.na(RaceOfPersonTypeID), 99998L, RaceOfPersonTypeID),
           UCROffenseCodeTypeID=ifelse(is.na(UCROffenseCodeTypeID), 99998L, UCROffenseCodeTypeID),
           EthnicityOfPersonTypeID=ifelse(is.na(EthnicityOfPersonTypeID), 99998L, EthnicityOfPersonTypeID),
           TypeOfArrestTypeID=ifelse(is.na(TypeOfArrestTypeID), 99998L, TypeOfArrestTypeID),
           MultipleArresteeSegmentsIndicatorTypeID=ifelse(is.na(MultipleArresteeSegmentsIndicatorTypeID), 99998L, MultipleArresteeSegmentsIndicatorTypeID),
           ResidentStatusOfPersonTypeID=ifelse(is.na(ResidentStatusOfPersonTypeID), 99998L, ResidentStatusOfPersonTypeID),
           DispositionOfArresteeUnder18TypeID=ifelse(is.na(DispositionOfArresteeUnder18TypeID), 99998L, DispositionOfArresteeUnder18TypeID),
           SexOfPersonTypeID=ifelse(is.na(SexOfPersonTypeID), 99998L, SexOfPersonTypeID)) %>%
    mutate(ArresteeSegmentID=row_number()) %>% as_tibble()

  ArresteeSegmentWasArmedWith <- ArresteeSegment %>%
    select(ArresteeSegmentID, V6012, V6013) %>%
    gather(key='index', value='StateCode', V6012, V6013) %>%
    select(-index) %>%
    filter(trimws(StateCode) != '') %>%
    mutate(AutomaticWeaponIndicator=str_sub(StateCode, 3, 3), StateCode=str_sub(StateCode, 1, 2)) %>%
    left_join(tableList$ArresteeWasArmedWithType %>% select(ArresteeWasArmedWithTypeID, StateCode), by='StateCode') %>%
    mutate(ArresteeSegmentWasArmedWithID=row_number()) %>%
    select(ArresteeSegmentWasArmedWithID, ArresteeSegmentID, ArresteeWasArmedWithTypeID, AutomaticWeaponIndicator)

  ArresteeSegment <- ArresteeSegment %>%
    select(ArresteeSegmentID, SegmentActionTypeTypeID, AdministrativeSegmentID, ArresteeSequenceNumber, AgeOfArresteeMin, AgeOfArresteeMax,
           SexOfPersonTypeID, RaceOfPersonTypeID, EthnicityOfPersonTypeID, ResidentStatusOfPersonTypeID, TypeOfArrestTypeID,
           DispositionOfArresteeUnder18TypeID, UCROffenseCodeTypeID, ArrestDate, ArrestDateID, ArrestTransactionNumber,
           MultipleArresteeSegmentsIndicatorTypeID)

  rm(arresteeSegmentDf)

  writeLines(paste0("Writing ", nrow(ArresteeSegment), " arrestee segments to database"))
  writeDataFrameToDatabase(conn, ArresteeSegment, 'ArresteeSegment', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(ArresteeSegment, 'type') <- 'FT'

  writeLines(paste0("Writing ", nrow(ArresteeSegmentWasArmedWith), " ArresteeSegmentWasArmedWith association rows to database"))
  writeDataFrameToDatabase(conn, ArresteeSegmentWasArmedWith, 'ArresteeSegmentWasArmedWith', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(ArresteeSegmentWasArmedWith, 'type') <- 'AT'

  tableList$ArresteeSegment <- ArresteeSegment
  tableList$ArresteeSegmentWasArmedWith <- ArresteeSegmentWasArmedWith

  tableList

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom stringr str_sub
#' @importFrom DBI dbWriteTable
writeRawArrestReportSegmentTables <- function(conn, inputDfList, tableList, records=-1) {

  currentMonth <- formatC(month(Sys.Date()), width=2, flag="0")
  currentYear <- year(Sys.Date()) %>% as.integer()

  dfName <- load(inputDfList[8])
  arresteeSegmentDf <- get(dfName) %>%  mutate_if(is.factor, as.character)
  rm(list=dfName)

  arresteeSegmentDf <- arresteeSegmentDf %>%
    inner_join(tableList$Agency %>% select(AgencyORI), by=c('V7003'='AgencyORI')) %>%
    mutate(ArrestReportSegmentID=row_number(), SegmentActionTypeTypeID=99998L)

  if (records != -1) {
    arresteeSegmentDf <- arresteeSegmentDf %>% head(records)
  }

  ArrestReportSegment <- arresteeSegmentDf %>%
    mutate(ArresteeSequenceNumber=as.integer(V7006),
           ArrestTransactionNumber=V7004,
           ORI=V7003,
           MonthOfTape=currentMonth, YearOfTape=currentYear,
           CityIndicator=NA_character_,
           ArrestDate=as_date(ifelse(is.na(V7005), NA, ymd(V7005))),
           ArrestDateID=createKeyFromDate(ArrestDate),
           AgeOfArresteeMin=as.integer(V7012),
           AgeOfArresteeMax=AgeOfArresteeMin) %>%
    left_join(tableList$Agency %>% select(AgencyID, ORI=AgencyORI), by='ORI') %>%
    left_join(tableList$RaceOfPersonType %>% select(RaceOfPersonTypeID, StateCode), by=c('V7014'='StateCode')) %>%
    left_join(tableList$SexOfPersonType %>% select(SexOfPersonTypeID, StateCode), by=c('V7013'='StateCode')) %>%
    left_join(tableList$EthnicityOfPersonType %>% select(EthnicityOfPersonTypeID, StateCode), by=c('V7015'='StateCode')) %>%
    left_join(tableList$ResidentStatusOfPersonType %>% select(ResidentStatusOfPersonTypeID, StateCode), by=c('V7016'='StateCode')) %>%
    left_join(tableList$DispositionOfArresteeUnder18Type %>% select(DispositionOfArresteeUnder18TypeID, StateCode),
              by=c('V7017'='StateCode')) %>%
    left_join(tableList$UCROffenseCodeType %>% select(UCROffenseCodeTypeID, StateCode), by=c('V7009'='StateCode')) %>%
    left_join(tableList$TypeOfArrestType %>% select(TypeOfArrestTypeID, StateCode), by=c('V7008'='StateCode')) %>%
    mutate(RaceOfPersonTypeID=ifelse(is.na(RaceOfPersonTypeID), 99998L, RaceOfPersonTypeID),
           UCROffenseCodeTypeID=ifelse(is.na(UCROffenseCodeTypeID), 99998L, UCROffenseCodeTypeID),
           EthnicityOfPersonTypeID=ifelse(is.na(EthnicityOfPersonTypeID), 99998L, EthnicityOfPersonTypeID),
           TypeOfArrestTypeID=ifelse(is.na(TypeOfArrestTypeID), 99998L, TypeOfArrestTypeID),
           ResidentStatusOfPersonTypeID=ifelse(is.na(ResidentStatusOfPersonTypeID), 99998L, ResidentStatusOfPersonTypeID),
           DispositionOfArresteeUnder18TypeID=ifelse(is.na(DispositionOfArresteeUnder18TypeID), 99998L, DispositionOfArresteeUnder18TypeID),
           SexOfPersonTypeID=ifelse(is.na(SexOfPersonTypeID), 99998L, SexOfPersonTypeID)) %>% as_tibble()

  ArrestReportSegmentWasArmedWith <- ArrestReportSegment %>%
    select(ArrestReportSegmentID, V7010, V7011) %>%
    gather(key='index', value='StateCode', V7010, V7011) %>%
    select(-index) %>%
    filter(trimws(StateCode) != '') %>%
    mutate(AutomaticWeaponIndicator=str_sub(StateCode, 3, 3), StateCode=str_sub(StateCode, 1, 2)) %>%
    left_join(tableList$ArresteeWasArmedWithType %>% select(ArresteeWasArmedWithTypeID, StateCode), by='StateCode') %>%
    mutate(ArrestReportSegmentWasArmedWithID=row_number()) %>%
    select(ArrestReportSegmentWasArmedWithID, ArrestReportSegmentID, ArresteeWasArmedWithTypeID, AutomaticWeaponIndicator)

  ArrestReportSegment <- ArrestReportSegment %>%
    select(ArrestReportSegmentID, SegmentActionTypeTypeID, ORI, CityIndicator, ArresteeSequenceNumber, AgeOfArresteeMin, AgeOfArresteeMax,
           SexOfPersonTypeID, RaceOfPersonTypeID, EthnicityOfPersonTypeID, ResidentStatusOfPersonTypeID, TypeOfArrestTypeID,
           DispositionOfArresteeUnder18TypeID, UCROffenseCodeTypeID, ArrestDate, ArrestDateID, ArrestTransactionNumber,
           MonthOfTape, YearOfTape, AgencyID)

  rm(arresteeSegmentDf)

  writeLines(paste0("Writing ", nrow(ArrestReportSegment), " arrest report segments to database"))
  writeDataFrameToDatabase(conn, ArrestReportSegment, 'ArrestReportSegment', viaBulk=TRUE, localBulk=FALSE, append=FALSE)
  attr(ArrestReportSegment, 'type') <- 'FT'

  writeLines(paste0("Writing ", nrow(ArrestReportSegmentWasArmedWith), " ArrestReportSegmentWasArmedWith association rows to database"))
  writeDataFrameToDatabase(conn, ArrestReportSegmentWasArmedWith, 'ArrestReportSegmentWasArmedWith', viaBulk=TRUE, localBulk=FALSE, append=FALSE)
  attr(ArrestReportSegmentWasArmedWith, 'type') <- 'AT'

  tableList$ArrestReportSegment <- ArrestReportSegment
  tableList$ArrestReportSegmentWasArmedWith <- ArrestReportSegmentWasArmedWith

  tableList

}
