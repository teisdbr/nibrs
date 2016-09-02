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

#' @import openxlsx
#' @import dplyr
loadCodeTables <- function() {

  codeTableFileName <- "NIBRSCodeTables.xlsx"

  ret = list()

  spreadsheetFile <- system.file("raw", codeTableFileName, package=getPackageName())
  sheetNames <- getSheetNames(spreadsheetFile)

  for (codeTableName in sheetNames) {
    ct <- read.xlsx(spreadsheetFile, sheet = codeTableName, colNames = TRUE)
    ct <- data.table::data.table(ct)
    l <- list(ct)
    names(l) <- c(codeTableName)
    ret <- c(ret, l)
  }

  ret

}
