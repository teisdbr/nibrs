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

getChildSegmentID <- function(incidentID, childSegmentSequenceNumber) {
  # different implementation needed if there were ever more than 10 child sequence numbers in NIBRS...but that's unlikely
  (incidentID*10) + (childSegmentSequenceNumber - 1)
}

processingMessage <- function(df, name) {
  writeLines(paste0('Processing ', nrow(df), ' raw ', name, ' records.'))
  df
}

#' @importFrom readr read_delim
getColumnSpecs <- function(fileName) {

  df <- read_delim(file=fileName, delim=" ", col_names=c("name", "pos", "type"), col_types="ccc")
  p <- "(.+)\\-(.+)"
  start <- gsub(pattern=p, x = df$pos, replacement="\\1")
  end <- gsub(pattern=p, x = df$pos, replacement="\\2")

  df$start <- as.integer(start)
  df$end <- as.integer(end)

  df

}

TypeOfWeaponForceInvolvedTranslationDf <- tibble(
  icpsrCode=c(1,110,111,120,121,130,131,140,141,150,151,200,300,350,400,500,600,650,700,850,900,990),
  AutomaticWeaponIndicator=c("N","N","Y","N","Y","N","Y","N","Y","N","Y","N","N","N","N","N","N","N","N","N","N","N")
)

