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

# #' @importFrom readr read_fwf
# loadIncidentFile <- function(file, maxRecords = -1) {
#   columnSpecsFile <- system.file("raw", "IncidentFileFormat.txt", package=getPackageName())
#   columnSpecs <- getColumnSpecs(columnSpecsFile)
#   read_fwf(file=file, col_positions = fwf_positions(start = columnSpecs$start, end = columnSpecs$end, col_names = columnSpecs$name),
#            col_types=paste(columnSpecs$type, collapse=""), n_max = maxRecords) %>% ungroup() %>% mutate(AdministrativeSegmentID=row_number())
# }

#' @importFrom readr read_fwf
loadIncidentFile <- function(file, versionYear, maxRecords = -1) {
  columnSpecsFile <- system.file("raw", paste0('IncidentFileFormat-', versionYear, '.txt'), package=getPackageName())
  columnSpecs <- getColumnSpecs(columnSpecsFile)
  read_fwf(file=file, col_positions = fwf_positions(start = columnSpecs$start, end = columnSpecs$end, col_names = columnSpecs$name),
           col_types=paste(columnSpecs$type, collapse=""), n_max = maxRecords, progress=FALSE) %>%
    ungroup() %>% mutate(AdministrativeSegmentID=row_number())
}

#' @import dplyr
addAdministrativeSegmentID <- function(rawIncidentsDataFrame) {
  rawIncidentsDataFrame %>% ungroup() %>% mutate(AdministrativeSegmentID=row_number())
}

#' @importFrom DBI dbClearResult dbSendQuery
truncateIncidents <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate AdministrativeSegment"))
}

#' @import dplyr
#' @importFrom DBI dbWriteTable
#' @importFrom lubridate month year ymd
writeIncidents <- function(conn, rawIncidentDataFrame, segmentActionTypeTypeID, agencyDataFrame) {

  currentMonth <- formatC(month(Sys.Date()), width=2, flag="0")
  currentYear <- year(Sys.Date()) %>% as.integer()

  AdministrativeSegment <- rawIncidentDataFrame %>%
    processingMessage('Incident') %>%
    select(AdministrativeSegmentID, ORI, IncidentNumber=INCNUM, INCDATE, IncidentHour=V1007,
           ClearedExceptionallyTypeID=V1013,
           ReportDateIndicator=V1006) %>%
    mutate(INCDATE=ifelse(INCDATE==-5, NA, INCDATE)) %>%
    mutate(IncidentDate=ymd(INCDATE),
           IncidentDateID=createKeyFromDate(IncidentDate),
           MonthOfTape=currentMonth, YearOfTape=currentYear, CityIndicator=NA, SegmentActionTypeTypeID=segmentActionTypeTypeID,
           ClearedExceptionallyTypeID=ifelse(ClearedExceptionallyTypeID==-6, 6L, ClearedExceptionallyTypeID),
           CargoTheftIndicatorTypeID=99998L,
           IncidentHour=ifelse(IncidentHour < 0, NA_integer_, IncidentHour)) %>%
    select(-INCDATE) %>%
    left_join(agencyDataFrame %>% select(AgencyID, ORI=AgencyORI), by='ORI')

  writeLines(paste0("Writing ", nrow(AdministrativeSegment), " administrative segments to database"))

  dbWriteTable(conn=conn, name="AdministrativeSegment", value=AdministrativeSegment, append=TRUE, row.names = FALSE)

  attr(AdministrativeSegment, 'type') <- 'FT'

  AdministrativeSegment

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeRawAdministrativeSegmentTables <- function(conn, inputDfList, tableList) {

  dfName <- load(inputDfList[2])
  adminSegmentDf <- get(dfName) %>%  mutate_if(is.factor, as.character) %>%
    inner_join(tableList$Agency %>% select(AgencyORI), by=c('V1003'='AgencyORI'))
  rm(list=dfName)

  currentMonth <- formatC(month(Sys.Date()), width=2, flag="0")
  currentYear <- year(Sys.Date()) %>% as.integer()

  AdministrativeSegment <- adminSegmentDf %>%
    select(ORI=V1003, IncidentNumber=V1004, INCDATE=V1005, IncidentHour=V1007,
           V1013, V1016,
           ReportDateIndicator=V1006) %>%
    mutate(IncidentHour=gsub(x=IncidentHour, pattern='\\(([0-9]+)\\).+', replacement='\\1')) %>%
    mutate(INCDATE=ifelse(trimws(INCDATE)=='' | is.na(INCDATE), NA, INCDATE)) %>%
    mutate(IncidentDate=ymd(INCDATE),
           IncidentDateID=createKeyFromDate(IncidentDate),
           MonthOfTape=currentMonth, YearOfTape=currentYear, CityIndicator=NA_character_, SegmentActionTypeTypeID=99998L,
           V1016=ifelse(trimws(V1016)=='' | is.na(V1016), 99998L, V1016),
           IncidentHour=ifelse(is.na(IncidentHour), NA_integer_, as.integer(IncidentHour))) %>%
    select(-INCDATE) %>%
    mutate(AdministrativeSegmentID=row_number()) %>%
    left_join(tableList$Agency %>% select(AgencyID, ORI=AgencyORI), by='ORI') %>%
    left_join(tableList$ClearedExceptionallyType %>% select(ClearedExceptionallyTypeID, ClearedExceptionallyCode), by=c('V1013'='ClearedExceptionallyCode')) %>%
    left_join(tableList$CargoTheftIndicatorType %>% select(CargoTheftIndicatorTypeID, CargoTheftIndicatorCode), by=c('V1016'='CargoTheftIndicatorCode')) %>%
    mutate(ClearedExceptionallyTypeID=ifelse(is.na(ClearedExceptionallyTypeID), 99998L, ClearedExceptionallyTypeID)) %>%
    mutate(CargoTheftIndicatorTypeID=ifelse(is.na(CargoTheftIndicatorTypeID), 99998L, CargoTheftIndicatorTypeID)) %>%
    select(-V1013, -V1016) %>% as_tibble()

  rm(adminSegmentDf)

  writeLines(paste0("Writing ", nrow(AdministrativeSegment), " administrative segments to database"))
  dbWriteTable(conn=conn, name="AdministrativeSegment", value=AdministrativeSegment, append=TRUE, row.names = FALSE)
  attr(AdministrativeSegment, 'type') <- 'FT'

  tableList$AdministrativeSegment <- AdministrativeSegment

  tableList

}
