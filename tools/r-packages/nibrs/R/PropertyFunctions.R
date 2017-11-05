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

