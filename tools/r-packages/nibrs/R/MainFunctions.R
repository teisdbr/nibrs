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

### Stuff to link up compiled C++ filtering code
#' @export
#' @useDynLib nibrs
NULL
### End C++ stuff

#' Load NIBRS records from ICPSR extract files
#'
#' Load NIBRS records from the ICPSR extract files, write them to the staging database (structured per the model provided in this package's same
#' GitHub repo), and return the loaded tables in a list of tibbles.
#'
#' Note that the extract files are limited in important ways.  For details, consult the ICPSR website (and in particular, the extract file codebooks).
#' @importFrom RMySQL dbClearResult dbSendQuery
#' @import tidyverse
#' @param conn the database connection to use
#' @param incidentFile path to the ICPSR NIBRS incident-level file
#' @param arresteeFile path to the ICPSR NIBRS arrestee-level file
#' @param versionYear the year covered by the incident and arrest files (ICPSR changes the format of the files each year)
#' @param incidentRecords number of records to read from the incident file, or -1 to read all records
#' @param arresteeRecords number of records to read from the arrestee file, or -1 to read all records
#' @return a list of tibbles, where the name of each list member is the name of the table in the database
#' @export
loadICPSRExtract <- function(conn=DBI::dbConnect(RMySQL::MySQL(), host="localhost", dbname="nibrs_analytics", username="root"),
                      incidentFile, arresteeFile, versionYear, incidentRecords=-1, arresteeRecords=-1) {

  tryCatch({

    dbClearResult(dbSendQuery(conn, "set foreign_key_checks=0"))

    spreadsheetFile <- system.file("raw", "NIBRSCodeTables.xlsx", package=getPackageName())

    ret <- loadCodeTables(spreadsheetFile, conn)

    truncateIncidents(conn)
    truncateOffenses(conn)
    truncateProperty(conn)
    truncateOffender(conn)
    truncateVictim(conn)

    rawIncidents <- loadIncidentFile(incidentFile, versionYear, incidentRecords)
    rawArrestees <- loadArresteeFile(arresteeFile, versionYear, arresteeRecords)

    agencies <- loadAgencies(conn, rawIncidents, rawArrestees)
    ret$Agency <- agencies

    ret$AdministrativeSegment <- writeIncidents(conn, rawIncidents, 9L, agencies)

    ret$OffenseSegment <- writeOffenses(conn, rawIncidents, 9L)
    ret$OffenderSuspectedOfUsing <- writeOffenderSuspectedOfUsing(conn, ret$OffenseSegment, rawIncidents)
    ret$TypeCriminalActivity <- writeTypeCriminalActivity(conn, ret$OffenseSegment, rawIncidents)
    ret$TypeOfWeaponForceInvolved <- writeTypeOfWeaponForceInvolved(conn, ret$OffenseSegment, rawIncidents)

    ret <- c(ret, writeProperty(conn, rawIncidents, 9L))
    ret$SuspectedDrugType <- writeSuspectedDrugType(conn, ret$PropertySegment, ret$PropertyType, rawIncidents)

    ret$OffenderSegment <- writeOffenders(conn, rawIncidents, 9L)

    ret$VictimSegment <- writeVictims(conn, rawIncidents, 9L)
    ret$TypeInjury <- writeVictimTypeInjury(conn, ret$VictimSegment, rawIncidents)
    ret$VictimOffenseAssociation <- writeVictimOffenseAssociation(conn, ret$VictimSegment, ret$OffenseSegment, rawIncidents)
    ret$VictimOffenderAssociation <- writeVictimOffenderAssociation(conn, ret$VictimSegment, ret$OffenderSegment, rawIncidents)
    ret$AggravatedAssaultHomicideCircumstances <- writeAggravatedAssaultHomicideCircumstances(conn, ret$VictimSegment, rawIncidents)

    ret$ArresteeSegment <- writeArresteeSegments(conn, rawIncidents, 9L)
    ret$ArresteeSegmentWasArmedWith <- writeArresteeSegmentWasArmedWith(conn, ret$ArresteeSegment, rawIncidents)

    ret$ArrestReportSegment <- writeArrestReportSegments(conn, rawArrestees, 9L, agencies)
    ret$ArrestReportSegmentWasArmedWith <- writeArrestReportSegmentWasArmedWith(conn, ret$ArrestReportSegment, rawArrestees)

    allDates <- c(
      ret$AdministrativeSegment$IncidentDate,
      ret$PropertyType$RecoveredDate,
      ret$ArresteeSegment$ArrestDate,
      ret$ArrestReportSegment$ArrestDate
    ) %>% unique()

    minDate <- min(allDates, na.rm=TRUE)
    maxDate <- max(allDates, na.rm=TRUE)

    ret$Date <- writeDateDimensionTable(conn, minDate, maxDate)

    ret$OffenseSegment <- select(ret$OffenseSegment, -OffenseCode)

    materializedViewsSqlFile=system.file("raw", 'MaterializedViews.sql', package=getPackageName())
    createMaterializedViews(conn, materializedViewsSqlFile)

    ret

  }, finally = {
    writeLines("Disconnecting from db...")
    DBI::dbDisconnect(conn)
  })

}

#' @importFrom readr read_file
#' @importFrom stringr str_split str_length
#' @importFrom DBI dbClearResult dbSendStatement
createMaterializedViews <- function(conn, sqlFile) {

  writeLines('Creating materialized views...')

  sql <- read_file(sqlFile)
  sql <- gsub(x=sql, pattern='\\/\\*.+\\*\\/(.+)', replacement='\\1')
  sql <- str_split(sql, ";")[[1]]
  count <- 0
  for (s in sql) {
    if (str_length(trimws(s))) {
      dbClearResult(dbSendStatement(conn, s))
      count <- count + 1
    }
  }

  writeLines(paste0(count, ' materialized view sql statements executed.'))

}

UNKNOWN_DATE_VALUE <- 99999L
BLANK_DATE_VALUE <- 99998L

createKeyFromDate <- function(d) {
  as.integer(format(d, "%Y%m%d"))
}

#' @importFrom lubridate as_date year quarter month wday day
#' @import dplyr
#' @import tibble
writeDateDimensionTable <- function(conn, minDate, maxDate, datesToExclude=as_date(x = integer(0))) {
  minDate <- as_date(minDate)
  maxDate <- as_date(maxDate)
  writeLines(paste0("Building date dimension, earliest date=", minDate, ", latestDate=", maxDate))
  DateDf <- tibble(CalendarDate=seq(minDate, maxDate, by="days")) %>%
    mutate(DateID=createKeyFromDate(CalendarDate),
           Year=year(CalendarDate),
           YearLabel=as.character(Year),
           CalendarQuarter=quarter(CalendarDate),
           Month=month(CalendarDate),
           MonthName=as.character(month(CalendarDate, label=TRUE, abbr=FALSE)),
           FullMonth=format(CalendarDate, paste0(Year, "-", Month)),
           Day=day(CalendarDate),
           DayOfWeek=as.character(wday(CalendarDate, label=TRUE, abbr=FALSE)),
           DayOfWeekSort=wday(CalendarDate),
           DateMMDDYYYY=format(CalendarDate, "%m%d%Y")
    ) %>%
    bind_rows(tibble(CalendarDate=as_date("1899-01-01"),
                     DateID=UNKNOWN_DATE_VALUE,
                     Year=0,
                     YearLabel='Unk',
                     CalendarQuarter=0,
                     Month=0,
                     MonthName='Unknown',
                     FullMonth='Unknown',
                     Day=0,
                     DayOfWeek='Unknown',
                     DayOfWeekSort=0,
                     DateMMDDYYYY='Unknown'),
              tibble(CalendarDate=as_date("1899-01-01"),
                     DateID=BLANK_DATE_VALUE,
                     Year=0,
                     YearLabel='Blk',
                     CalendarQuarter=0,
                     Month=0,
                     MonthName='Blank',
                     FullMonth='Blank',
                     Day=0,
                     DayOfWeek='Blank',
                     DayOfWeekSort=0,
                     DateMMDDYYYY='Blank'))
  DateDf <- DateDf %>% filter(!(CalendarDate %in% datesToExclude)) %>%
    rename(DateTypeID=DateID)
  writeLines(paste0("Adding ", nrow(DateDf), " new dates to the Date dimension"))

  writeLines(paste0("Writing ", nrow(DateDf), " Date rows to database"))
  dbWriteTable(conn=conn, name="DateType", value=DateDf, append=TRUE, row.names = FALSE)

  attr(DateDf, 'type') <- 'CT'
  DateDf

}

#' Load NIBRS records from ICPSR "raw" files
#'
#' Load NIBRS records from the ICPSR raw files, write them to the staging database (structured per the model provided in this package's same
#' GitHub repo), and return the loaded tables in a list of tibbles.
#'
#' @importFrom RMySQL dbClearResult dbSendQuery
#' @import tidyverse
#' @importFrom stringr str_sub
#' @param conn the database connection to use
#' @param dataDir path to the root directory containing serialized R data frames in subdirectories
#' @param state two-letter state code of data to extract
#' @param records number of records to read for the specified state, or -1 to extract all records
#' @return a list of tibbles, where the name of each list member is the name of the table in the database
#' @export
loadICPSRRaw <- function(conn=DBI::dbConnect(RMySQL::MySQL(), host="localhost", dbname="nibrs_analytics", username="root"),
                             dataDir, state, records=-1) {

  tryCatch({

    dbClearResult(dbSendQuery(conn, "set foreign_key_checks=0"))

    spreadsheetFile <- system.file("raw", "NIBRSCodeTables.xlsx", package=getPackageName())

    ret <- loadCodeTables(spreadsheetFile, conn)

    truncateIncidents(conn)
    truncateOffenses(conn)
    truncateProperty(conn)
    truncateOffender(conn)
    truncateVictim(conn)

    inputDfList <- sort(list.files(path=dataDir, pattern='*.rda', full.names=TRUE, recursive=TRUE))

    ret <- writeRawAgencyTables(conn, inputDfList, state, ret)
    ret <- writeRawAdministrativeSegmentTables(conn, inputDfList, ret)
    ret <- writeRawOffenseSegmentTables(conn, inputDfList, ret)
    ret <- writeRawPropertySegmentTables(conn, inputDfList, ret)
    ret <- writeRawOffenderSegmentTables(conn, inputDfList, ret)
    ret <- writeRawVictimSegmentTables(conn, inputDfList, ret)
    ret <- writeRawArresteeSegmentTables(conn, inputDfList, ret)
    ret <- writeRawArrestReportSegmentTables(conn, inputDfList, ret)

    allDates <- c(
      ret$AdministrativeSegment$IncidentDate,
      ret$PropertyType$RecoveredDate,
      ret$ArresteeSegment$ArrestDate,
      ret$ArrestReportSegment$ArrestDate
    ) %>% unique()

    minDate <- min(allDates, na.rm=TRUE)
    maxDate <- max(allDates, na.rm=TRUE)

    ret$Date <- writeDateDimensionTable(conn, minDate, maxDate)

    ret$OffenseSegment <- select(ret$OffenseSegment, -UCROffenseCode)

    materializedViewsSqlFile=system.file("raw", 'MaterializedViews.sql', package=getPackageName())
    createMaterializedViews(conn, materializedViewsSqlFile)

    ret

  }, finally = {
    writeLines("Disconnecting from db...")
    DBI::dbDisconnect(conn)
  })

  ret

}
