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

#' @importFrom DBI dbSendQuery dbClearResult
truncateProperty <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate PropertySegment"))
  dbClearResult(dbSendQuery(conn, "truncate PropertyType"))
  dbClearResult(dbSendQuery(conn, "truncate SuspectedDrugType"))
}

#' @import dplyr
#' @import tibble
#' @importFrom DBI dbWriteTable
#' @importFrom lubridate ymd
writeProperty <- function(conn, rawIncidentsDataFrame, segmentActionTypeTypeID) {

  ret <- list()

  PropertySegment <- bind_cols(

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V30061:V30063) %>%
      gather(V_TypeLoss, TypePropertyLossEtcTypeID, V30061:V30063),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V30071:V30073) %>%
      gather(V_Desc, PropertyDescriptionTypeID, V30071:V30073) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V30081:V30083) %>%
      gather(V_Value, ValueOfProperty, V30081:V30083) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V30091:V30093) %>%
      gather(V_DateRecovered, RecoveredDate, V30091:V30093) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V30101:V30103) %>%
      gather(V_StolenMV, NumberOfStolenMotorVehicles, V30101:V30103) %>%
      select(-AdministrativeSegmentID),

    rawIncidentsDataFrame %>%
      select(AdministrativeSegmentID, V30111:V30113) %>%
      gather(V_RecoveredMV, NumberOfRecoveredMotorVehicles, V30111:V30113) %>%
      select(-AdministrativeSegmentID)

  ) %>%
    filter(TypePropertyLossEtcTypeID != -8) %>% select(-starts_with("V_")) %>%
    mutate(RecoveredDate=ifelse(RecoveredDate < 0, NA, RecoveredDate)) %>%
    mutate(TypePropertyLossEtcTypeID=ifelse(TypePropertyLossEtcTypeID < 0, 99999, TypePropertyLossEtcTypeID),
           PropertyDescriptionTypeID=ifelse(PropertyDescriptionTypeID < 0, 99999, PropertyDescriptionTypeID),
           ValueOfProperty=ifelse(ValueOfProperty < 0, NA, ValueOfProperty),
           RecoveredDate=ymd(RecoveredDate),
           RecoveredDateID=createKeyFromDate(RecoveredDate),
           NumberOfStolenMotorVehicles=ifelse(NumberOfStolenMotorVehicles < 0, NA, NumberOfStolenMotorVehicles),
           NumberOfRecoveredMotorVehicles=ifelse(NumberOfRecoveredMotorVehicles < 0, NA, NumberOfRecoveredMotorVehicles),
           SegmentActionTypeTypeID=segmentActionTypeTypeID)

  PropertyType <- PropertySegment %>%
    select(SegmentActionTypeTypeID, AdministrativeSegmentID, TypePropertyLossEtcTypeID,
           PropertyDescriptionTypeID, ValueOfProperty, RecoveredDate, RecoveredDateID) %>%
    mutate(PropertyTypeID=row_number())

  PropertySegment <- PropertySegment %>%
    group_by(SegmentActionTypeTypeID, AdministrativeSegmentID, TypePropertyLossEtcTypeID) %>%
    summarize_at(vars(starts_with('NumberOf')), sum, na.rm=TRUE) %>%
    ungroup() %>%
    mutate(PropertySegmentID=row_number())

  PropertyType <- PropertyType %>%
    inner_join(PropertySegment %>% select(SegmentActionTypeTypeID, AdministrativeSegmentID, TypePropertyLossEtcTypeID, PropertySegmentID),
               by=c('SegmentActionTypeTypeID', 'AdministrativeSegmentID', 'TypePropertyLossEtcTypeID')) %>%
    select(PropertyTypeID, PropertySegmentID, PropertyDescriptionTypeID, ValueOfProperty, RecoveredDate, RecoveredDateID)

  writeLines(paste0("Writing ", nrow(PropertySegment), " property segments to database"))
  writeLines(paste0("Writing ", nrow(PropertyType), " PropertyType rows to database"))

  dbWriteTable(conn=conn, name="PropertySegment", value=PropertySegment, append=TRUE, row.names = FALSE)
  dbWriteTable(conn=conn, name="PropertyType", value=PropertyType, append=TRUE, row.names = FALSE)

  attr(PropertySegment, 'type') <- 'FT'
  attr(PropertyType, 'type') <- 'AT'

  ret <- list()
  ret$PropertySegment <- PropertySegment
  ret$PropertyType <- PropertyType
  ret

}

#' @import dplyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeSuspectedDrugType <- function(conn, propertySegmentDataFrame, propertyTypeDataFrame, rawIncidentsDataFrame) {

  tempDf <- bind_cols(
    bind_rows(
      rawIncidentsDataFrame %>%
        select(AdministrativeSegmentID, PropertyDescription = V30071, V30121:V30123) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, SuspectedDrugTypeTypeID, V30121:V30123) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(AdministrativeSegmentID, PropertyDescription = V30072, V30161:V30163) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, SuspectedDrugTypeTypeID, V30161:V30163) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(AdministrativeSegmentID, PropertyDescription = V30073, V30201:V30203) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, SuspectedDrugTypeTypeID, V30201:V30203) %>%
        mutate(V_Pivot = as.character(V_Pivot))
    ) %>% select(-V_Pivot),
    bind_rows(
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30071, V30131:V30133) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, EstimatedDrugQuantityWholePart, V30131:V30133) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30072, V30171:V30173) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, EstimatedDrugQuantityWholePart, V30171:V30173) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30073, V30211:V30213) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, EstimatedDrugQuantityWholePart, V30211:V30213) %>%
        mutate(V_Pivot = as.character(V_Pivot))
    ) %>% select(-PropertyDescription, -V_Pivot),
    bind_rows(
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30071, V30141:V30143) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, EstimatedDrugQuantityFractionalPart, V30141:V30143) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30072, V30181:V30183) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, EstimatedDrugQuantityFractionalPart, V30181:V30183) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30073, V30221:V30223) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, EstimatedDrugQuantityFractionalPart, V30221:V30223) %>%
        mutate(V_Pivot = as.character(V_Pivot))
    ) %>% select(-PropertyDescription, -V_Pivot),
    bind_rows(
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30071, V30151:V30153) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, TypeMeasurement, V30151:V30153) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30072, V30191:V30193) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, TypeMeasurement, V30191:V30193) %>%
        mutate(V_Pivot = as.character(V_Pivot)),
      rawIncidentsDataFrame %>%
        select(PropertyDescription = V30073, V30231:V30233) %>%
        filter(PropertyDescription == 10) %>%
        gather(V_Pivot, TypeMeasurement, V30231:V30233) %>%
        mutate(V_Pivot = as.character(V_Pivot))
    ) %>% select(-PropertyDescription, -V_Pivot)
  )  %>% filter(SuspectedDrugTypeTypeID > 0) %>% rename(PropertyDescriptionTypeID=PropertyDescription)

  propertySegmentDataFrame <- inner_join(propertySegmentDataFrame, propertyTypeDataFrame, by='PropertySegmentID')

  SuspectedDrugType <- left_join(tempDf,
                                        select(propertySegmentDataFrame, AdministrativeSegmentID,PropertyDescriptionTypeID, PropertySegmentID),
                                        by=c("AdministrativeSegmentID", "PropertyDescriptionTypeID")) %>%
    mutate(EstimatedDrugQuantity=as.numeric(ifelse(EstimatedDrugQuantityWholePart < 0, NA,
                                        EstimatedDrugQuantityWholePart +
                                          (ifelse(EstimatedDrugQuantityFractionalPart < 0, 0,
                                                  as.numeric(EstimatedDrugQuantityFractionalPart/1000))))),
           TypeDrugMeasurementTypeID=as.integer(ifelse(TypeMeasurement < 0, 99999, TypeMeasurement))) %>%
    select(PropertySegmentID, SuspectedDrugTypeTypeID, TypeDrugMeasurementTypeID, EstimatedDrugQuantity) %>%
    mutate(SuspectedDrugTypeID=row_number())

  writeLines(paste0("Writing ", nrow(SuspectedDrugType), " SuspectedDrugType association rows to database"))

  dbWriteTable(conn=conn, name="SuspectedDrugType", value=SuspectedDrugType, append=TRUE, row.names = FALSE)

  attr(SuspectedDrugType, 'type') <- 'AT'

  SuspectedDrugType

}

#' @import dplyr
#' @import tidyr
#' @import tibble
#' @importFrom DBI dbWriteTable
writeRawPropertySegmentTables <- function(conn, inputDfList, tableList) {

  dfName <- load(inputDfList[4])
  propertySegmentDf <- get(dfName) %>%  mutate_if(is.factor, as.character)
  rm(list=dfName)

  if ('ORI' %in% colnames(propertySegmentDf)) {
    propertySegmentDf <- propertySegmentDf %>% rename(V3003=ORI, V3004=INCNUM)
  }

  propertySegmentDf <- propertySegmentDf %>%
    inner_join(tableList$Agency %>% select(AgencyORI), by=c('V3003'='AgencyORI')) %>%
    inner_join(tableList$AdministrativeSegment %>% select(ORI, IncidentNumber, AdministrativeSegmentID), by=c('V3003'='ORI', 'V3004'='IncidentNumber')) %>%
    mutate(SegmentActionTypeTypeID=99998L)

  PropertyType <- propertySegmentDf %>%
    select(AdministrativeSegmentID, V3006:V3011, SegmentActionTypeTypeID, V3012:V3023) %>%
    mutate(
      V3006=gsub(x=V3006, pattern='\\(([0-9]+)\\).+', replacement='\\1'),
      V3007=gsub(x=V3007, pattern='\\(([0-9]+)\\).+', replacement='\\1'),
      ValueOfProperty=V3008,
      V3009=as.character(V3009),
      RecoveredDate=as_date(ifelse(is.na(V3009), NA, ymd(V3009))),
      RecoveredDateID=createKeyFromDate(RecoveredDate)
    ) %>%
    left_join(tableList$TypePropertyLossEtcType %>% select(TypePropertyLossEtcTypeID, StateCode), by=c('V3006'='StateCode')) %>%
    left_join(tableList$PropertyDescriptionType %>% select(PropertyDescriptionTypeID, StateCode), by=c('V3007'='StateCode')) %>%
    mutate(TypePropertyLossEtcTypeID=ifelse(is.na(TypePropertyLossEtcTypeID), 99998L, TypePropertyLossEtcTypeID),
           PropertyDescriptionTypeID=ifelse(is.na(PropertyDescriptionTypeID), 99998L, PropertyDescriptionTypeID),
           RecoveredDateID=ifelse(is.na(RecoveredDateID), BLANK_DATE_VALUE, RecoveredDateID)) %>%
    select(AdministrativeSegmentID, TypePropertyLossEtcTypeID, PropertyDescriptionTypeID, ValueOfProperty, RecoveredDate, RecoveredDateID,
           V3010, V3011, SegmentActionTypeTypeID, V3012:V3023) %>%
    filter(TypePropertyLossEtcTypeID != 99999L) %>%
    mutate(PropertyTypeID=row_number())

  PropertySegment <- PropertyType %>%
    select(AdministrativeSegmentID, TypePropertyLossEtcTypeID, SegmentActionTypeTypeID, NumberOfStolenMotorVehicles=V3010,
           NumberOfRecoveredMotorVehicles=V3011, V3012:V3023) %>%
    group_by(AdministrativeSegmentID, TypePropertyLossEtcTypeID) %>%
    filter(row_number()==1) %>%
    ungroup() %>%
    mutate(PropertySegmentID=row_number())

  SuspectedDrugType <- PropertySegment %>%
    gather(key=index, value=StateCode, V3012, V3016, V3020) %>%
    select(PropertySegmentID, StateCode) %>%
    bind_cols(
      PropertySegment %>%
        gather(key=index, value=TypeDrugMeasurementCode, V3015, V3019, V3023) %>%
        select(TypeDrugMeasurementCode),
      PropertySegment %>%
        gather(key=index, value=qqq, V3013, V3017, V3021) %>%
        select(qqq),
      PropertySegment %>%
        gather(key=index, value=qqqq, V3014, V3018, V3022) %>%
        select(qqqq)
    ) %>%
    filter(trimws(StateCode) != '') %>%
    mutate(EstimatedDrugQuantity=qqq + (qqqq*.001)) %>%
    left_join(tableList$TypeDrugMeasurementType %>% select(TypeDrugMeasurementTypeID, TypeDrugMeasurementCode=StateCode), by='TypeDrugMeasurementCode') %>%
    left_join(tableList$SuspectedDrugTypeType %>% select(SuspectedDrugTypeTypeID, StateCode), by='StateCode') %>%
    mutate(TypeDrugMeasurementTypeID=ifelse(is.na(TypeDrugMeasurementTypeID), 99998L, TypeDrugMeasurementTypeID),
           SuspectedDrugTypeTypeID=ifelse(is.na(SuspectedDrugTypeTypeID), 99998L, SuspectedDrugTypeTypeID)) %>%
    select(PropertySegmentID, SuspectedDrugTypeTypeID, TypeDrugMeasurementTypeID, EstimatedDrugQuantity) %>%
    ungroup() %>%
    mutate(SuspectedDrugTypeID=row_number())

  PropertySegment <- PropertySegment %>% select(-(V3012:V3023))

  PropertyType <- PropertyType %>%
    inner_join(PropertySegment %>% select(PropertySegmentID, AdministrativeSegmentID, TypePropertyLossEtcTypeID),
               by=c('AdministrativeSegmentID', 'TypePropertyLossEtcTypeID')) %>%
    select(PropertyTypeID, PropertySegmentID, PropertyDescriptionTypeID, ValueOfProperty, RecoveredDate, RecoveredDateID)

  rm(propertySegmentDf)

  writeLines(paste0("Writing ", nrow(PropertySegment), " property segments to database"))
  dbWriteTable(conn=conn, name="PropertySegment", value=PropertySegment, append=TRUE, row.names = FALSE)
  attr(PropertySegment, 'type') <- 'FT'

  writeLines(paste0("Writing ", nrow(SuspectedDrugType), " SuspectedDrugType association rows to database"))
  dbWriteTable(conn=conn, name="SuspectedDrugType", value=SuspectedDrugType, append=TRUE, row.names = FALSE)
  attr(SuspectedDrugType, 'type') <- 'AT'

  writeLines(paste0("Writing ", nrow(PropertyType), " PropertyType association rows to database"))
  dbWriteTable(conn=conn, name="PropertyType", value=PropertyType, append=TRUE, row.names = FALSE)
  attr(PropertyType, 'type') <- 'AT'

  tableList$PropertySegment <- PropertySegment
  tableList$PropertyType <- PropertyType
  tableList$SuspectedDrugType <- SuspectedDrugType

  tableList

}

