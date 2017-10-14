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

#' @importFrom DBI dbClearResult dbSendQuery dbWriteTable
#' @importFrom openxlsx read.xlsx
#' @import dplyr
#' @import purrr
#' @import tibble
loadCodeTables <- function(spreadsheetFile, conn) {

  TOC <- read.xlsx(spreadsheetFile, sheet='TOC')

  map2(TOC$Table, TOC$Tab, function(codeTableName, tabName) {
    ct <- read.xlsx(spreadsheetFile, sheet=tabName) %>%
      select(-starts_with('X_')) %>% as_tibble()
    writeLines(paste0("Loading code table: ", codeTableName))
    dbClearResult(dbSendQuery(conn, paste0("truncate ", codeTableName)))
    if (codeTableName=='UCROffenseCodeType') {
      colnames(ct) <- c('UCROffenseCodeTypeID', 'UCROffenseCode', 'UCROffenseCodeDescription', 'OffenseCategory1',
                        'OffenseCategory2', 'OffenseCategory3', 'OffenseCategory4')
    } else if (codeTableName=='BiasMotivationType') {
      colnames(ct) <- c('BiasMotivationTypeID', 'BiasMotivationCode', 'BiasMotivationDescription', 'BiasMotivationCategory')
    } else if (codeTableName=='AgencyType') {
      colnames(ct) <- c('AgencyTypeID', 'AgencyTypeCode', 'AgencyTypeDescription')
    } else {
      colnames(ct) <- c(paste0(codeTableName, 'ID'), paste0(gsub(x=codeTableName, pattern='(.+)Type', replacement='\\1'), c('Code','Description')))
    }
    dbWriteTable(conn=conn, value=ct, name=codeTableName, append=TRUE, row.names = FALSE)
    attr(ct, 'type') <- 'CT'
    ct
  }) %>% set_names(TOC$Table)

}
