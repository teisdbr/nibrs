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

#' @importFrom DBI dbClearResult dbSendQuery
truncateOffender <- function(conn) {
  dbClearResult(dbSendQuery(conn, 'truncate OffenderSegment'))
}

#' @import dplyr
#' @import tidyr
#' @importFrom DBI dbWriteTable
writeOffenders <- function(conn, rawIncidentsDataFrame, segmentActionTypeTypeID) {

  OffenderSegment <- bind_cols(

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V50061:V50063) %>%
      gather(V_OffenderSequenceNumber, OffenderSequenceNumber, V50061:V50063),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V50071:V50073) %>%
      gather(V_OffenderAge, AgeOfOffenderMin, V50071:V50073) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V50081:V50083) %>%
      gather(V_OffenderSex, SexOfPersonTypeID, V50081:V50083) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V50091:V50093) %>%
      gather(V_OffenderRace, RaceOfPersonTypeID, V50091:V50093) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V50111:V50113) %>%
      gather(V_OffenderEthnicity, EthnicityOfPersonTypeID, V50111:V50113) %>%
      select(-AdministrativeSegmentID)

  ) %>%
    filter(OffenderSequenceNumber > 0) %>% select(-starts_with('V_')) %>%
    mutate(AgeOfOffenderMin=ifelse(AgeOfOffenderMin < 0, NA, AgeOfOffenderMin),
           AgeOfOffenderMax=AgeOfOffenderMin,
           SexOfPersonTypeID=ifelse(SexOfPersonTypeID < 0, 9, SexOfPersonTypeID+1),
           RaceOfPersonTypeID=ifelse(RaceOfPersonTypeID < 0, 9, RaceOfPersonTypeID),
           EthnicityOfPersonTypeID=ifelse(EthnicityOfPersonTypeID < 0, 9, EthnicityOfPersonTypeID),
           SegmentActionTypeTypeID=segmentActionTypeTypeID) %>%
    ungroup() %>%
    mutate(OffenderSegmentID=row_number())

  writeLines(paste0('Writing ', nrow(OffenderSegment), ' offender segments to database'))

  dbWriteTable(conn=conn, name='OffenderSegment', value=OffenderSegment, append=TRUE, row.names = FALSE)

  attr(OffenderSegment, 'type') <- 'FT'

  OffenderSegment

}
