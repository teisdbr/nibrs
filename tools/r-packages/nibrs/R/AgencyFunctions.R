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

#' @importFrom DBI dbWriteTable dbSendQuery dbClearResult
#' @importFrom readr read_fwf fwf_positions
#' @import dplyr
loadAgencies <- function(conn, file) {

  dbClearResult(dbSendQuery(conn, "truncate Agency"))

  columnSpecsFile <- system.file("raw", "AgencyFileFormat.txt", package=getPackageName())
  columnSpecs <- getColumnSpecs(columnSpecsFile)

  Agency <- read_fwf(file=file,
                     col_positions=fwf_positions(
                       start=columnSpecs$start, end=columnSpecs$end, col_names=columnSpecs$name),
                     col_types=paste(columnSpecs$type, collapse='')) %>%
    select(AgencyName=NAME,
           AgencyORI=ORI9,
           UCRPopulation=U_TPOP,
           CensusPopulation=LG_POPULATION,
           StateCode=FIPS_ST,
           CountyCode=FIPS_COUNTY,
           StateName=STATENAME,
           CountyName=COUNTYNAME) %>%
    mutate(CensusPopulation=ifelse(CensusPopulation==888888888, NA, CensusPopulation),
           UCRPopulation=ifelse(UCRPopulation==-1 | UCRPopulation==0, NA, UCRPopulation)) %>%
    mutate(AgencyID=row_number())

  writeLines(paste0("Writing ", nrow(Agency), " Agency rows to database"))

  dbWriteTable(conn=conn, name="Agency", value=Agency, append=TRUE, row.names = FALSE)

  attr(Agency, 'type') <- 'FT'

  Agency

}
