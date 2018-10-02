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
# functions related to Offense data manipulation

#' @importFrom DBI dbSendQuery dbClearResult
truncateOffenses <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate OffenseSegment"))
  dbClearResult(dbSendQuery(conn, "truncate OffenderSuspectedOfUsing"))
  dbClearResult(dbSendQuery(conn, "truncate TypeCriminalActivity"))
  dbClearResult(dbSendQuery(conn, "truncate TypeOfWeaponForceInvolved"))
  dbClearResult(dbSendQuery(conn, "truncate BiasMotivation"))
}

#' @import dplyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeOffenderSuspectedOfUsing <- function(conn, offenseSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_rows(
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20061, V20081:V20083) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20081:V20083) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20062, V20091:V20093) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20091:V20093) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20063, V20101:V20103) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20101:V20103) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0)
  )

  OffenderSuspectedOfUsing <- left_join(tempDf,
                                        select(offenseSegmentDataFrame, AdministrativeSegmentID, OffenseCode, OffenseSegmentID),
                                        by=c("AdministrativeSegmentID", "OffenseCode")) %>%
    select(OffenseSegmentID, OffenderSuspectedOfUsingTypeID=Pivot)

  missingSegmentIDs <- setdiff(offenseSegmentDataFrame$OffenseSegmentID, OffenderSuspectedOfUsing$OffenseSegmentID)

  OffenderSuspectedOfUsing <- bind_rows(OffenderSuspectedOfUsing,
                                        tibble(OffenseSegmentID=missingSegmentIDs,
                                                   OffenderSuspectedOfUsingTypeID=rep(x=4, times=length(missingSegmentIDs)))) %>%
    mutate(OffenderSuspectedOfUsingID=row_number())

  writeLines(paste0("Writing ", nrow(OffenderSuspectedOfUsing), " OffenderSuspectedOfUsing association rows to database"))

  dbWriteTable(conn=conn, name="OffenderSuspectedOfUsing", value=OffenderSuspectedOfUsing, append=TRUE, row.names = FALSE)

  attr(OffenderSuspectedOfUsing, 'type') <- 'AT'

  OffenderSuspectedOfUsing

}

#' @import dplyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeTypeOfWeaponForceInvolved <- function(conn, offenseSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_rows(
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20061, V20171:V20173) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20171:V20173) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20062, V20181:V20183) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20181:V20183) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20063, V20191:V20193) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20191:V20193) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0)
  )

  TypeOfWeaponForceInvolved <- left_join(tempDf,
                                        select(offenseSegmentDataFrame, AdministrativeSegmentID, OffenseCode, OffenseSegmentID),
                                        by=c("AdministrativeSegmentID", "OffenseCode")) %>%
    select(OffenseSegmentID, TypeOfWeaponForceInvolvedTypeID=Pivot)

  # TypeOfWeaponForceInvolvedTranslationDf defined in CommonFunctions.R
  TypeOfWeaponForceInvolved <- left_join(TypeOfWeaponForceInvolved, TypeOfWeaponForceInvolvedTranslationDf,
                                   by=c("TypeOfWeaponForceInvolvedTypeID"="icpsrCode")) %>%
    mutate(TypeOfWeaponForceInvolvedTypeID=ifelse(is.na(TypeOfWeaponForceInvolvedTypeID), 99999, TypeOfWeaponForceInvolvedTypeID))

  missingSegmentIDs <- setdiff(offenseSegmentDataFrame$OffenseSegmentID, TypeOfWeaponForceInvolved$OffenseSegmentID)

  TypeOfWeaponForceInvolved <- bind_rows(TypeOfWeaponForceInvolved,
                                        tibble(OffenseSegmentID=missingSegmentIDs,
                                                   TypeOfWeaponForceInvolvedTypeID=rep(x=99998, times=length(missingSegmentIDs)),
                                                   AutomaticWeaponIndicator=rep(x="N", times=length(missingSegmentIDs)))) %>%
    mutate(TypeOfWeaponForceInvolvedID=row_number())

  writeLines(paste0("Writing ", nrow(TypeOfWeaponForceInvolved), " TypeOfWeaponForceInvolved association rows to database"))

  dbWriteTable(conn=conn, name="TypeOfWeaponForceInvolved", value=TypeOfWeaponForceInvolved, append=TRUE, row.names = FALSE)

  attr(TypeOfWeaponForceInvolved, 'type') <- 'AT'

  TypeOfWeaponForceInvolved

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeTypeCriminalActivity <- function(conn, offenseSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_rows(
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20061, V20141:V20143) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20141:V20143) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20062, V20151:V20153) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20151:V20153) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, OffenseCode=V20063, V20161:V20163) %>%
      filter(OffenseCode > 0) %>%
      gather(V_Pivot, Pivot, V20161:V20163) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0)
  )

  TypeCriminalActivity <- left_join(tempDf,
                                        select(offenseSegmentDataFrame, AdministrativeSegmentID, OffenseCode, OffenseSegmentID),
                                        by=c("AdministrativeSegmentID", "OffenseCode")) %>%
    select(OffenseSegmentID, TypeOfCriminalActivityTypeID=Pivot)

  missingSegmentIDs <- setdiff(offenseSegmentDataFrame$OffenseSegmentID, TypeCriminalActivity$OffenseSegmentID)

  TypeCriminalActivity <- bind_rows(TypeCriminalActivity,
                                        tibble(OffenseSegmentID=missingSegmentIDs,
                                                   TypeOfCriminalActivityTypeID=rep(x=99999, times=length(missingSegmentIDs)))) %>%
    mutate(TypeCriminalActivityID=row_number())

  writeLines(paste0("Writing ", nrow(TypeCriminalActivity), " TypeCriminalActivity association rows to database"))

  dbWriteTable(conn=conn, name="TypeCriminalActivity", value=TypeCriminalActivity, append=TRUE, row.names = FALSE)

  attr(TypeCriminalActivity, 'type') <- 'AT'

  TypeCriminalActivity

}

#' @import dplyr
#' @importFrom DBI dbWriteTable
writeOffenses <- function(conn, rawIncidentsDataFrame, segmentActionTypeTypeID) {

  OffenseSegment <- bind_cols(

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V20061:V20063) %>%
      gather(V_OffenseCode, OffenseCode, V20061:V20063),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V20071:V20073) %>%
      gather(V_AttemptedCompleted, OffenseAttemptedCompleted, V20071:V20073) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V20111:V20113) %>%
      gather(V_LocationType, LocationTypeTypeID, V20111:V20113) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V20121:V20123) %>%
      gather(V_PremisesEntered, NumberOfPremisesEntered, V20121:V20123) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V20131:V20133) %>%
      gather(V_MethodOfEntry, MethodOfEntryTypeID, V20131:V20133) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V20201:V20203) %>%
      gather(V_BiasMotivation, BiasMotivationTypeID, V20201:V20203) %>%
      select(-AdministrativeSegmentID)

  ) %>%
    filter(OffenseCode != -8) %>% select(-starts_with("V_")) %>%
    mutate(MethodOfEntryTypeID=ifelse(MethodOfEntryTypeID < 0, 99999, MethodOfEntryTypeID),
           NumberOfPremisesEntered=ifelse(NumberOfPremisesEntered < 0, NA, NumberOfPremisesEntered),
           LocationTypeTypeID=ifelse(LocationTypeTypeID < 0, 99999, LocationTypeTypeID),
           SegmentActionTypeTypeID=segmentActionTypeTypeID,
           UCROffenseCodeTypeID=ifelse(OffenseCode < 0, 99999, OffenseCode)) %>%
    mutate(OffenseSegmentID=row_number())

  writeLines(paste0("Writing ", nrow(OffenseSegment), " offense segments to database"))

  dbWriteTable(conn=conn, name="OffenseSegment", value=select(OffenseSegment, -OffenseCode), append=TRUE, row.names = FALSE)

  attr(OffenseSegment, 'type') <- 'FT'

  OffenseSegment

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom stringr str_sub
#' @importFrom DBI dbWriteTable
writeRawOffenseSegmentTables <- function(conn, inputDfList, tableList) {

  dfName <- load(inputDfList[3])
  offenseSegmentDf <- get(dfName) %>%  mutate_if(is.factor, as.character) %>%
    inner_join(tableList$Agency %>% select(AgencyORI), by=c('V2003'='AgencyORI'))
  rm(list=dfName)

  offenseSegmentDf <- offenseSegmentDf %>%
    inner_join(tableList$AdministrativeSegment %>% select(ORI, IncidentNumber, AdministrativeSegmentID), by=c('V2003'='ORI', 'V2004'='IncidentNumber')) %>%
    mutate(OffenseSegmentID=row_number(), SegmentActionTypeTypeID=99998L)

  OffenseSegment <- offenseSegmentDf %>%
    select(-V2001, -V2002, -V2005, -V2021) %>%
    mutate(V2011=gsub(x=V2011, pattern='\\(([0-9]+)\\).+', replacement='\\1'),
           UCROffenseCode=V2006) %>%
    left_join(tableList$UCROffenseCodeType %>% select(UCROffenseCodeTypeID, StateCode), by=c('V2006'='StateCode')) %>%
    left_join(tableList$LocationTypeType %>% select(LocationTypeTypeID, StateCode), by=c('V2011'='StateCode')) %>%
    left_join(tableList$MethodOfEntryType %>% select(MethodOfEntryTypeID, StateCode), by=c('V2013'='StateCode')) %>%
    select(OffenseSegmentID, SegmentActionTypeTypeID, AdministrativeSegmentID, UCROffenseCodeTypeID, OffenseAttemptedCompleted=V2007,
           LocationTypeTypeID, NumberOfPremisesEntered=V2012, MethodOfEntryTypeID, UCROffenseCode) %>% as_tibble()

  OffenderSuspectedOfUsing <- offenseSegmentDf %>%
    select(AdministrativeSegmentID, OffenseSegmentID, V2008:V2010) %>%
    gather(key=index, value=StateCode, V2008:V2010) %>% filter(StateCode != ' ') %>%
    select(-index) %>%
    inner_join(tableList$OffenderSuspectedOfUsingType %>% select(OffenderSuspectedOfUsingTypeID, StateCode), by='StateCode') %>%
    mutate(OffenderSuspectedOfUsingID=row_number()) %>%
    select(OffenderSuspectedOfUsingID, OffenseSegmentID, OffenderSuspectedOfUsingTypeID) %>%
    as_tibble()

  TypeCriminalActivity <- offenseSegmentDf %>%
    select(AdministrativeSegmentID, OffenseSegmentID, V2014:V2016) %>%
    gather(key=index, value=StateCode, V2014:V2016) %>% filter(StateCode != ' ') %>%
    select(-index) %>%
    inner_join(tableList$TypeOfCriminalActivityType %>% select(TypeOfCriminalActivityTypeID, StateCode), by='StateCode') %>%
    mutate(TypeCriminalActivityID=row_number()) %>%
    select(TypeCriminalActivityID, OffenseSegmentID, TypeOfCriminalActivityTypeID) %>%
    as_tibble()

  BiasMotivation <- offenseSegmentDf %>%
    mutate_at(vars(starts_with('V2020')), gsub, pattern='\\(([0-9]+)\\).+', replacement='\\1') %>%
    select(AdministrativeSegmentID, OffenseSegmentID, starts_with('V2020')) %>%
    gather(key=index, value=StateCode, starts_with('V2020')) %>% filter(StateCode != ' ') %>%
    select(-index) %>%
    inner_join(tableList$BiasMotivationType %>% select(BiasMotivationTypeID, StateCode), by='StateCode') %>%
    mutate(BiasMotivationID=row_number()) %>%
    select(BiasMotivationID, OffenseSegmentID, BiasMotivationTypeID) %>%
    as_tibble()

  TypeOfWeaponForceInvolved <- offenseSegmentDf %>%
    select(AdministrativeSegmentID, OffenseSegmentID, V2017:V2019) %>%
    gather(key=index, value=StateCode, V2017:V2019) %>% filter(StateCode != ' ') %>%
    select(-index) %>%
    mutate(AutomaticWeaponIndicator=ifelse(str_length(StateCode)==3, str_sub(StateCode, 3, 3), NA_character_)) %>%
    mutate(StateCode=str_sub(StateCode, 1, 2)) %>%
    inner_join(tableList$TypeOfWeaponForceInvolvedType %>% select(TypeOfWeaponForceInvolvedTypeID, StateCode), by='StateCode') %>%
    mutate(TypeOfWeaponForceInvolvedID=row_number()) %>%
    select(TypeOfWeaponForceInvolvedID, OffenseSegmentID, TypeOfWeaponForceInvolvedTypeID, AutomaticWeaponIndicator) %>%
    as_tibble()

  rm(offenseSegmentDf)

  writeLines(paste0("Writing ", nrow(OffenseSegment), " offense segments to database"))
  dbWriteTable(conn=conn, name="OffenseSegment", value=select(OffenseSegment, -UCROffenseCode), append=TRUE, row.names = FALSE)
  attr(OffenseSegment, 'type') <- 'FT'

  writeLines(paste0("Writing ", nrow(OffenderSuspectedOfUsing), " OffenderSuspectedOfUsing records to database"))
  dbWriteTable(conn=conn, name="OffenderSuspectedOfUsing", value=OffenderSuspectedOfUsing, append=TRUE, row.names = FALSE)
  attr(OffenderSuspectedOfUsing, 'type') <- 'AT'

  writeLines(paste0("Writing ", nrow(TypeCriminalActivity), " TypeCriminalActivity records to database"))
  dbWriteTable(conn=conn, name="TypeCriminalActivity", value=TypeCriminalActivity, append=TRUE, row.names = FALSE)
  attr(TypeCriminalActivity, 'type') <- 'AT'

  writeLines(paste0("Writing ", nrow(BiasMotivation), " BiasMotivation records to database"))
  dbWriteTable(conn=conn, name="BiasMotivation", value=BiasMotivation, append=TRUE, row.names = FALSE)
  attr(BiasMotivation, 'type') <- 'AT'

  writeLines(paste0("Writing ", nrow(TypeOfWeaponForceInvolved), " TypeOfWeaponForceInvolved records to database"))
  dbWriteTable(conn=conn, name="TypeOfWeaponForceInvolved", value=TypeOfWeaponForceInvolved, append=TRUE, row.names = FALSE)
  attr(TypeOfWeaponForceInvolved, 'type') <- 'AT'

  tableList$OffenseSegment <- OffenseSegment
  tableList$OffenderSuspectedOfUsing <- OffenderSuspectedOfUsing
  tableList$TypeCriminalActivity <- TypeCriminalActivity
  tableList$BiasMotivation <- BiasMotivation
  tableList$TypeOfWeaponForceInvolved <- TypeOfWeaponForceInvolved

  tableList

}

