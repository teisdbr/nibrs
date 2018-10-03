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
truncateVictim <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate VictimSegment"))
  dbClearResult(dbSendQuery(conn, "truncate VictimOffenderAssociation"))
  dbClearResult(dbSendQuery(conn, "truncate VictimOffenseAssociation"))
  dbClearResult(dbSendQuery(conn, "truncate AggravatedAssaultHomicideCircumstances"))
  dbClearResult(dbSendQuery(conn, "truncate TypeInjury"))
}

#' @import dplyr
#' @import tidyr
#' @importFrom DBI dbWriteTable
writeVictims <- function(conn, rawIncidentsDataFrame, segmentActionTypeTypeID) {

  VictimSegment <- bind_cols(

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40061:V40063) %>%
      gather(V_VictimSequenceNumber, VictimSequenceNumber, V40061:V40063),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40171:V40173) %>%
      gather(V_TypeOfVictimTypeID, TypeOfVictimTypeID, V40171:V40173) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V4017A1:V4017A3) %>%
      gather(V_OfficerActivityCircumstanceTypeID, OfficerActivityCircumstanceTypeID, V4017A1:V4017A3) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V4017B1:V4017B3) %>%
      gather(V_OfficerAssignmentTypeTypeID, OfficerAssignmentTypeTypeID, V4017B1:V4017B3) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40191:V40193) %>%
      gather(V_SexOfPersonTypeID, SexOfPersonTypeID, V40191:V40193) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40181:V40183) %>%
      gather(V_AgeRaw, AgeRaw, V40181:V40183) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40201:V40203) %>%
      gather(V_RaceOfPersonTypeID, RaceOfPersonTypeID, V40201:V40203) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40211:V40213) %>%
      gather(V_EthnicityOfPersonTypeID, EthnicityOfPersonTypeID, V40211:V40213) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40221:V40223) %>%
      gather(V_ResidentStatusOfPersonTypeID, ResidentStatusOfPersonTypeID, V40221:V40223) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V40251:V40253) %>%
      gather(V_AdditionalJustifiableHomicideCircumstancesID, AdditionalJustifiableHomicideCircumstancesTypeID, V40251:V40253) %>%
      select(-AdministrativeSegmentID)

  )  %>% filter(VictimSequenceNumber > 0) %>% select(-starts_with("V_")) %>%
    mutate(AgeOfVictimMin=as.integer(ifelse(AgeRaw < 1, NA, AgeRaw)),
           AgeOfVictimMax=AgeOfVictimMin,
           AgeNeonateIndicator=ifelse(AgeRaw == 0.1, 1, 0),
           AgeFirstWeekIndicator=ifelse(AgeRaw == 0.2, 1, 0),
           AgeFirstYearIndicator=ifelse(AgeRaw == 0.5, 1, 0),
           SexOfPersonTypeID=ifelse(SexOfPersonTypeID < 0, 99999, SexOfPersonTypeID+1),
           TypeOfVictimTypeID=ifelse(TypeOfVictimTypeID < 0, 99999, TypeOfVictimTypeID),
           RaceOfPersonTypeID=ifelse(RaceOfPersonTypeID < 0, 99999, RaceOfPersonTypeID),
           EthnicityOfPersonTypeID=ifelse(EthnicityOfPersonTypeID < 0, 99999, EthnicityOfPersonTypeID),
           ResidentStatusOfPersonTypeID=ifelse(ResidentStatusOfPersonTypeID < 0, 99999, ResidentStatusOfPersonTypeID+1),
           OfficerActivityCircumstanceTypeID=ifelse(OfficerActivityCircumstanceTypeID < 0, 99999, OfficerActivityCircumstanceTypeID),
           OfficerAssignmentTypeTypeID=ifelse(OfficerAssignmentTypeTypeID < 0, 99999, OfficerAssignmentTypeTypeID),
           AdditionalJustifiableHomicideCircumstancesTypeID=ifelse(AdditionalJustifiableHomicideCircumstancesTypeID < 0, 99999, AdditionalJustifiableHomicideCircumstancesTypeID),
           SegmentActionTypeTypeID=segmentActionTypeTypeID)  %>%
    select(-AgeRaw) %>%
    mutate(VictimSegmentID=row_number())

  writeLines(paste0("Writing ", nrow(VictimSegment), " victim segments to database"))

  dbWriteTable(conn=conn, name="VictimSegment", value=VictimSegment, append=TRUE, row.names = FALSE)

  attr(VictimSegment, 'type') <- 'FT'

  VictimSegment

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeAggravatedAssaultHomicideCircumstances <- function(conn, victimSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_rows(
    rawIncidentsDataFrame %>%
      select(
        AdministrativeSegmentID, VictimSequenceNumber = V40061, V40231, V40241, V40251
      ) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V40231, V40241, V40251) %>%
      mutate(V_Pivot = as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(
        AdministrativeSegmentID, VictimSequenceNumber = V40062, V40232, V40242, V40252
      ) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V40232, V40242, V40252) %>%
      mutate(V_Pivot = as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(
        AdministrativeSegmentID, VictimSequenceNumber = V40063, V40233, V40243, V40253
      ) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V40233, V40243, V40253) %>%
      mutate(V_Pivot = as.character(V_Pivot)) %>%
      filter(Pivot > 0)
  )

  AggravatedAssaultHomicideCircumstances <- left_join(
    tempDf,
    select(
      victimSegmentDataFrame, AdministrativeSegmentID, VictimSequenceNumber, VictimSegmentID
    ),
    by = c("AdministrativeSegmentID", "VictimSequenceNumber")
  ) %>%
    select(VictimSegmentID, AggravatedAssaultHomicideCircumstancesTypeID = Pivot)

  missingSegmentIDs <- setdiff(victimSegmentDataFrame$VictimSegmentID, AggravatedAssaultHomicideCircumstances$VictimSegmentID)

  AggravatedAssaultHomicideCircumstances <- bind_rows(AggravatedAssaultHomicideCircumstances,
                                         tibble(VictimSegmentID=missingSegmentIDs,
                                                    AggravatedAssaultHomicideCircumstancesTypeID=rep(x=99999, times=length(missingSegmentIDs)))) %>%
    mutate(AggravatedAssaultHomicideCircumstancesID=row_number())

  writeLines(paste0("Writing ", nrow(AggravatedAssaultHomicideCircumstances), " AggravatedAssaultHomicideCircumstances association rows to database"))

  dbWriteTable(conn=conn, name="AggravatedAssaultHomicideCircumstances", value=AggravatedAssaultHomicideCircumstances, append=TRUE, row.names = FALSE)

  attr(AggravatedAssaultHomicideCircumstances, 'type') <- 'AT'

  AggravatedAssaultHomicideCircumstances

}

#' @import dplyr
#' @import tidyr
#' @importFrom DBI dbWriteTable
writeVictimOffenderAssociation <- function(conn, victimSegmentDataFrame, offenderSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_cols(
    bind_rows(
      rawIncidentsDataFrame %>%
        select(
          AdministrativeSegmentID, VictimSequenceNumber = V40061, V40311, V40331, V40351, V40371, V40391,
          V40411, V40431, V40451, V40471, V40491
        ) %>%
        filter(VictimSequenceNumber > 0) %>%
        gather(
          V_Pivot, OffenderSequenceNumber, V40311, V40331, V40351, V40371, V40391,
          V40411, V40431, V40451, V40471, V40491
        ) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(
          AdministrativeSegmentID, VictimSequenceNumber = V40062, V40312, V40332, V40352, V40372, V40392,
          V40412, V40432, V40452, V40472, V40492
        ) %>%
        filter(VictimSequenceNumber > 0) %>%
        gather(
          V_Pivot, OffenderSequenceNumber, V40312, V40332, V40352, V40372, V40392,
          V40412, V40432, V40452, V40472, V40492
        ) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(
          AdministrativeSegmentID, VictimSequenceNumber = V40063, V40313, V40333, V40353, V40373, V40393,
          V40413, V40433, V40453, V40473, V40493
        ) %>%
        filter(VictimSequenceNumber > 0) %>%
        gather(
          V_Pivot, OffenderSequenceNumber, V40313, V40333, V40353, V40373, V40393,
          V40413, V40433, V40453, V40473, V40493
        ) %>%
        mutate(V_Pivot = as.character(V_Pivot))
    ) %>% select(-V_Pivot),
    bind_rows(
      rawIncidentsDataFrame %>%
        select(
          AdministrativeSegmentID, VictimSequenceNumber = V40061, V40321, V40341, V40361, V40381, V40401,
          V40421, V40441, V40461, V40481, V40501
        ) %>%
        filter(VictimSequenceNumber > 0) %>%
        gather(
          V_Pivot, VictimOffenderRelationshipTypeID, V40321, V40341, V40361, V40381, V40401,
          V40421, V40441, V40461, V40481, V40501
        ) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(
          AdministrativeSegmentID, VictimSequenceNumber = V40062, V40322, V40342, V40362, V40382, V40402,
          V40422, V40442, V40462, V40482, V40502
        ) %>%
        filter(VictimSequenceNumber > 0) %>%
        gather(
          V_Pivot, VictimOffenderRelationshipTypeID, V40322, V40342, V40362, V40382, V40402,
          V40422, V40442, V40462, V40482, V40502
        ) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(
          AdministrativeSegmentID, VictimSequenceNumber = V40063, V40323, V40343, V40363, V40383, V40403,
          V40423, V40443, V40463, V40483, V40503
        ) %>%
        filter(VictimSequenceNumber > 0) %>%
        gather(
          V_Pivot, VictimOffenderRelationshipTypeID, V40323, V40343, V40363, V40383, V40403,
          V40423, V40443, V40463, V40483, V40503
        ) %>%
        mutate(V_Pivot = as.character(V_Pivot))
    ) %>% select(-V_Pivot, -VictimSequenceNumber, -AdministrativeSegmentID)
  ) %>% filter(OffenderSequenceNumber > 0)

  VictimOffenderAssociation <- left_join(
    tempDf,
    select(
      victimSegmentDataFrame, AdministrativeSegmentID, VictimSequenceNumber, VictimSegmentID
    ),
    by = c("AdministrativeSegmentID", "VictimSequenceNumber")
  ) %>%
    select(VictimSegmentID, OffenderSequenceNumber, VictimOffenderRelationshipTypeID, AdministrativeSegmentID)

  VictimOffenderAssociation <- left_join(
    VictimOffenderAssociation,
    select(
      offenderSegmentDataFrame, AdministrativeSegmentID, OffenderSequenceNumber, OffenderSegmentID
    ),
    by = c("AdministrativeSegmentID", "OffenderSequenceNumber")
  ) %>%
    select(VictimSegmentID, OffenderSegmentID, VictimOffenderRelationshipTypeID) %>%
    ungroup() %>%
    mutate(VictimOffenderAssociationID=row_number())

  writeLines(paste0(
    "Writing ", nrow(VictimOffenderAssociation), " VictimOffenderAssociation association rows to database"
  ))

  dbWriteTable(
    conn = conn, name = "VictimOffenderAssociation", value = VictimOffenderAssociation, append =
      TRUE, row.names = FALSE
  )

  attr(VictimOffenderAssociation, 'type') <- 'AT'

  VictimOffenderAssociation

}

#' @import dplyr
#' @import tidyr
#' @importFrom DBI dbWriteTable
writeVictimOffenseAssociation <- function(conn, victimSegmentDataFrame, offenseSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_rows(
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, VictimSequenceNumber=V40061, V40071, V40081, V40091, V40101, V40111,
             V40121, V40131, V40141, V40151, V40161) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, OffenseCode, V40071, V40081, V40091, V40101, V40111,
             V40121, V40131, V40141, V40151, V40161) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(OffenseCode > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, VictimSequenceNumber=V40062, V40072, V40082, V40092, V40102, V40112,
             V40122, V40132, V40142, V40152, V40162) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, OffenseCode, V40072, V40082, V40092, V40102, V40112,
             V40122, V40132, V40142, V40152, V40162) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(OffenseCode > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, VictimSequenceNumber=V40063, V40073, V40083, V40093, V40103, V40113,
             V40123, V40133, V40143, V40153, V40163) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, OffenseCode, V40073, V40083, V40093, V40103, V40113,
             V40123, V40133, V40143, V40153, V40163) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(OffenseCode > 0)
  )

  VictimOffenseAssociation <- left_join(
    tempDf,
    select(
      victimSegmentDataFrame, AdministrativeSegmentID, VictimSequenceNumber, VictimSegmentID
    ),
    by = c("AdministrativeSegmentID", "VictimSequenceNumber")
  ) %>%
    select(VictimSegmentID, OffenseCode, AdministrativeSegmentID)

  VictimOffenseAssociation <- left_join(
    VictimOffenseAssociation,
    select(
      offenseSegmentDataFrame, AdministrativeSegmentID, OffenseCode, OffenseSegmentID
    ),
    by = c("AdministrativeSegmentID", "OffenseCode")
  ) %>%
    select(VictimSegmentID, OffenseSegmentID) %>%
    mutate(VictimOffenseAssociationID=row_number())

  writeLines(paste0("Writing ", nrow(VictimOffenseAssociation), " VictimOffenseAssociation association rows to database"))

  dbWriteTable(conn=conn, name="VictimOffenseAssociation", value=VictimOffenseAssociation, append=TRUE, row.names = FALSE)

  attr(VictimOffenseAssociation, 'type') <- 'AT'

  VictimOffenseAssociation

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeVictimTypeInjury <- function(conn, victimSegmentDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_rows(
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, VictimSequenceNumber=V40061, V40261, V40271, V40281, V40291, V40301) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V40261, V40271, V40281, V40291, V40301) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, VictimSequenceNumber=V40062, V40262, V40272, V40282, V40292, V40302) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V40262, V40272, V40282, V40292, V40302) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0),
    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, VictimSequenceNumber=V40063, V40263, V40273, V40283, V40293, V40303) %>%
      filter(VictimSequenceNumber > 0) %>%
      gather(V_Pivot, Pivot, V40263, V40273, V40283, V40293, V40303) %>%
      mutate(V_Pivot=as.character(V_Pivot)) %>%
      filter(Pivot > 0)
  )

  TypeInjury <- left_join(tempDf,
                          select(victimSegmentDataFrame, AdministrativeSegmentID, VictimSequenceNumber, VictimSegmentID),
                          by=c("AdministrativeSegmentID", "VictimSequenceNumber")) %>%
    select(VictimSegmentID, TypeInjuryTypeID=Pivot)

  missingSegmentIDs <- setdiff(victimSegmentDataFrame$VictimSegmentID, TypeInjury$VictimSegmentID)

  TypeInjury <- bind_rows(TypeInjury,
                          tibble(VictimSegmentID=missingSegmentIDs,
                                     TypeInjuryTypeID=rep(x=1, times=length(missingSegmentIDs)))) %>%
    mutate(TypeInjuryID=row_number())

  writeLines(paste0("Writing ", nrow(TypeInjury), " TypeInjury association rows to database"))

  dbWriteTable(conn=conn, name="TypeInjury", value=TypeInjury, append=TRUE, row.names = FALSE)

  attr(TypeInjury, 'type') <- 'AT'

  TypeInjury

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeRawVictimSegmentTables <- function(conn, inputDfList, tableList) {

  dfName <- load(inputDfList[5])
  victimSegmentDf <- get(dfName) %>%  mutate_if(is.factor, as.character)
  rm(list=dfName)

  if ('ORI' %in% colnames(victimSegmentDf)) {
    victimSegmentDf <- victimSegmentDf %>% rename(V4003=ORI, V4004=INCNUM)
  }

  victimSegmentDf <- victimSegmentDf %>%
    inner_join(tableList$Agency %>% select(AgencyORI), by=c('V4003'='AgencyORI')) %>%
    inner_join(tableList$AdministrativeSegment %>% select(ORI, IncidentNumber, AdministrativeSegmentID), by=c('V4003'='ORI', 'V4004'='IncidentNumber')) %>%
    mutate(VictimSegmentID=row_number(), SegmentActionTypeTypeID=99998L)

  VictimSegment <- victimSegmentDf %>%
    mutate(VictimSequenceNumber=as.integer(V4006),
           V4017A=gsub(x=V4017A, pattern='\\(([0-9]+)\\).+', replacement='\\1'),
           V4023=gsub(x=V4023, pattern='\\(([0-9]+)\\).+', replacement='\\1'),
           V4024=gsub(x=V4024, pattern='\\(([0-9]+)\\).+', replacement='\\1'),
           V4017A=trimws(V4017A),
           V4018=trimws(V4018),
           AgeOfVictimMin=case_when(
             V4018 %in% c('NB','NN','BB') ~ '0',
             V4018=='' | is.na(V4018) ~ NA_character_,
             TRUE ~ V4018
           ),
           AgeOfVictimMin=as.integer(AgeOfVictimMin),
           AgeOfVictimMax=AgeOfVictimMin,
           AgeNeonateIndicator=ifelse(is.na(V4018) | V4018==' ', NA_integer_, V4018=='NN'),
           AgeFirstWeekIndicator=ifelse(is.na(V4018) | V4018==' ', NA_integer_, V4018=='NB'),
           AgeFirstYearIndicator=ifelse(is.na(V4018) | V4018==' ', NA_integer_, V4018=='BB')
    ) %>%
    left_join(tableList$TypeOfVictimType %>% select(TypeOfVictimTypeID, StateCode), by=c('V4017'='StateCode')) %>%
    left_join(tableList$OfficerActivityCircumstanceType %>% select(OfficerActivityCircumstanceTypeID, StateCode),
              by=c('V4017A'='StateCode')) %>%
    left_join(tableList$OfficerAssignmentTypeType %>% select(OfficerAssignmentTypeTypeID, StateCode), by=c('V4017B'='StateCode')) %>%
    left_join(tableList$SexOfPersonType %>% select(SexOfPersonTypeID, StateCode), by=c('V4019'='StateCode')) %>%
    left_join(tableList$RaceOfPersonType %>% select(RaceOfPersonTypeID, StateCode), by=c('V4020'='StateCode')) %>%
    left_join(tableList$EthnicityOfPersonType %>% select(EthnicityOfPersonTypeID, StateCode), by=c('V4021'='StateCode')) %>%
    left_join(tableList$ResidentStatusOfPersonType %>% select(ResidentStatusOfPersonTypeID, StateCode),
              by=c('V4022'='StateCode')) %>%
    left_join(tableList$AdditionalJustifiableHomicideCircumstancesType %>%
                select(AdditionalJustifiableHomicideCircumstancesTypeID, StateCode),
              by=c('V4025'='StateCode')) %>%
    mutate(VictimSegmentID=row_number(), OfficerActivityCircumstanceTypeID=case_when(is.na(OfficerActivityCircumstanceTypeID) ~ 99998L, TRUE ~ OfficerActivityCircumstanceTypeID)) %>% as_tibble()

  VictimOffenseAssociation <- VictimSegment %>%
    select(AdministrativeSegmentID, VictimSegmentID, V4007:V4016) %>%
    gather(key='index', value='UCROffenseCode', V4007:V4016) %>%
    select(-index) %>%
    filter(trimws(UCROffenseCode) != '') %>%
    inner_join(tableList$OffenseSegment %>% select(AdministrativeSegmentID, OffenseSegmentID, UCROffenseCode), by=c('AdministrativeSegmentID', 'UCROffenseCode')) %>%
    mutate(VictimOffenseAssociationID=row_number()) %>%
    select(VictimOffenseAssociationID, VictimSegmentID, OffenseSegmentID)

  AggravatedAssaultHomicideCircumstances <- VictimSegment %>%
    select(VictimSegmentID, V4023, V4024) %>%
    gather(key='index', value='StateCode', V4023, V4024) %>%
    select(-index) %>%
    filter(trimws(StateCode) != '') %>%
    inner_join(tableList$AggravatedAssaultHomicideCircumstancesType %>%
                 select(AggravatedAssaultHomicideCircumstancesTypeID, StateCode),
               by='StateCode') %>%
    select(VictimSegmentID, AggravatedAssaultHomicideCircumstancesTypeID) %>%
    mutate(AggravatedAssaultHomicideCircumstancesID=row_number())

  TypeInjury <- VictimSegment %>%
    select(VictimSegmentID, V4026:V4030) %>%
    gather(key='index', value='StateCode', V4026:V4030) %>%
    select(-index) %>%
    filter(trimws(StateCode) != '') %>%
    inner_join(tableList$TypeInjuryType %>% select(TypeInjuryTypeID, StateCode), by='StateCode') %>%
    select(VictimSegmentID, TypeInjuryTypeID) %>%
    mutate(TypeInjuryID=row_number())

  VictimOffenderAssociation <- VictimSegment %>%
    select(AdministrativeSegmentID, VictimSegmentID, V4031:V4050) %>%
    gather(key='var', value='value', V4031:V4050) %>%
    filter(trimws(value) != '') %>%
    mutate(varnum=as.integer(str_sub(var, 2, 5))) %>%
    mutate(
      var=case_when(
        varnum %% 2 == 1 ~ 'OffenderSequenceNumber',
        TRUE ~ 'VictimOffenderRelationshipCode'
      ),
      seqnum=case_when(
        var=='VictimOffenderRelationshipCode' ~ varnum-1L,
        TRUE ~ varnum
      ),
      seqnum=seqnum-4030-(seqnum-4030-1)/2) %>%
    select(-varnum) %>%
    spread(key=var, value=value) %>%
    mutate(OffenderSequenceNumber=as.integer(OffenderSequenceNumber)) %>%
    inner_join(tableList$OffenderSegment %>% select(AdministrativeSegmentID, OffenderSegmentID, OffenderSequenceNumber),
               by=c('AdministrativeSegmentID', 'OffenderSequenceNumber')) %>%
    left_join(tableList$VictimOffenderRelationshipType %>% select(VictimOffenderRelationshipTypeID, StateCode),
              by=c('VictimOffenderRelationshipCode'='StateCode')) %>%
    select(VictimSegmentID, OffenderSegmentID, VictimOffenderRelationshipTypeID) %>%
    mutate(VictimOffenderAssociationID=row_number()) %>%
    mutate(VictimOffenderRelationshipTypeID=ifelse(is.na(VictimOffenderRelationshipTypeID), 99998L, VictimOffenderRelationshipTypeID))

  VictimSegment <- VictimSegment %>%
    select(VictimSegmentID, AdministrativeSegmentID, SegmentActionTypeTypeID, VictimSequenceNumber, TypeOfVictimTypeID,
           OfficerActivityCircumstanceTypeID, OfficerAssignmentTypeTypeID, AgeOfVictimMin, AgeOfVictimMax, AgeNeonateIndicator, AgeFirstWeekIndicator,
           AgeFirstYearIndicator, SexOfPersonTypeID, RaceOfPersonTypeID, EthnicityOfPersonTypeID, ResidentStatusOfPersonTypeID,
           AdditionalJustifiableHomicideCircumstancesTypeID)

  rm(victimSegmentDf)

  writeLines(paste0("Writing ", nrow(VictimSegment), " VictimSegment association rows to database"))
  writeDataFrameToDatabase(conn, VictimSegment, 'VictimSegment', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(VictimSegment, 'type') <- 'FT'

  writeLines(paste0("Writing ", nrow(VictimOffenseAssociation), " VictimOffenseAssociation association rows to database"))
  writeDataFrameToDatabase(conn, VictimOffenseAssociation, 'VictimOffenseAssociation', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(VictimOffenseAssociation, 'type') <- 'AT'

  writeLines(paste0("Writing ", nrow(VictimOffenderAssociation), " VictimOffenderAssociation association rows to database"))
  writeDataFrameToDatabase(conn, VictimOffenderAssociation, 'VictimOffenderAssociation', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(VictimOffenderAssociation, 'type') <- 'AT'

  writeLines(paste0("Writing ", nrow(AggravatedAssaultHomicideCircumstances), " AggravatedAssaultHomicideCircumstances association rows to database"))
  writeDataFrameToDatabase(conn, AggravatedAssaultHomicideCircumstances, 'AggravatedAssaultHomicideCircumstances', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(AggravatedAssaultHomicideCircumstances, 'type') <- 'AT'

  writeLines(paste0("Writing ", nrow(TypeInjury), " TypeInjury association rows to database"))
  writeDataFrameToDatabase(conn, TypeInjury, 'TypeInjury', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(TypeInjury, 'type') <- 'AT'

  tableList$VictimSegment <- VictimSegment
  tableList$VictimOffenseAssociation <- VictimOffenseAssociation
  tableList$VictimOffenderAssociation <- VictimOffenderAssociation
  tableList$AggravatedAssaultHomicideCircumstances <- AggravatedAssaultHomicideCircumstances
  tableList$TypeInjury <- TypeInjury

  tableList

}
