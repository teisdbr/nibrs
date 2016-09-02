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

# Functions for loading ICPSR NIBRS extract files

#' @import readr
loadIncidentFile <- function(file, versionYear, maxRecords = -1) {
  columnSpecs <- getColumnSpecs(paste0("IncidentFileFormat-", versionYear, ".txt"))
  read_fwf(file=file, col_positions = fwf_positions(start = columnSpecs$start, end = columnSpecs$end, col_names = columnSpecs$name),
           col_types=paste(columnSpecs$type, collapse=""), n_max = maxRecords, progress=FALSE)
}

#' @import readr
loadAgencyFile <- function(file, maxRecords = -1) {
  columnSpecs <- getColumnSpecs("AgencyFileFormat.txt")
  read_fwf(file=file, col_positions = fwf_positions(start = columnSpecs$start, end = columnSpecs$end, col_names = columnSpecs$name),
           col_types=paste(columnSpecs$type, collapse=""), n_max = maxRecords, progress=FALSE)
}

#' @import readr
#' @import stringr
getColumnSpecs <- function(fileName) {

  f <- system.file("raw", fileName, package=getPackageName())
  df <- read_delim(file=f, delim=" ", col_names=c("name", "pos", "type"), col_types="ccc")
  p <- "(.+)\\-(.+)"
  start <- gsub(pattern=p, x = df$pos, replacement="\\1")
  end <- gsub(pattern=p, x = df$pos, replacement="\\2")

  df$start <- as.integer(start)
  df$end <- as.integer(end)

  df

}

#' Subset an ICPSR NIBRS input file for one state's records
#' Note: requires that a system command 'awk' be available
#' @export
subsetIncidentFile <- function(inputFile, state, outputFile) {
  stdin <- inputFile
  stdout <- outputFile
  system2(command="awk", args=paste0("'{ if (substr($0, 101, 2) == \"", state, "\") print $0 }'"), stdin=stdin, stdout=stdout)
  outputFile
}
