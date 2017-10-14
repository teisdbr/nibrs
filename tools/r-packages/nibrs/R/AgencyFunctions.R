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
loadAgencies <- function(conn, rawIncidents, rawArrestees) {

  dbClearResult(dbSendQuery(conn, "truncate Agency"))

  Agency <- rawIncidents %>%
    select(AgencyORI=ORI,
           AgencyName=BH007,
           BH012,
           StateCode=BH008) %>%
    bind_rows(
      rawArrestees %>%
        select(AgencyORI=ORI,
               AgencyName=BH007,
               BH012,
               StateCode=BH008)
    ) %>%
    mutate(StateName=stateLookup[StateCode],
           AgencyTypeID=ifelse(is.na(BH012), 99, BH012+1)) %>%
    select(-BH012) %>%
    distinct() %>%
    mutate(AgencyID=row_number()) %>%
    mutate(AgencyName=case_when(
      AgencyTypeID==3 ~ paste0(AgencyName, ' COUNTY SO'),
      AgencyTypeID==2 ~ paste0(AgencyName, ' PD'),
      TRUE ~ AgencyName
    ))

  writeLines(paste0("Writing ", nrow(Agency), " Agency rows to database"))

  dbWriteTable(conn=conn, name="Agency", value=Agency, append=TRUE, row.names = FALSE)

  attr(Agency, 'type') <- 'FT'

  Agency

}

stateLookup <- c(
  'AL'='Alabama',
  'AK'='Alaska',
  'AZ'='Arizona',
  'AR'='Arkansas',
  'CA'='California',
  'CO'='Colorado',
  'CT'='Connecticut',
  'DE'='Delaware',
  'DC'='District of Columbia',
  'FL'='Florida',
  'GA'='Georgia',
  'HI'='Hawaii',
  'ID'='Idaho',
  'IL'='Illinois',
  'IN'='Indiana',
  'IA'='Iowa',
  'KS'='Kansas',
  'KY'='Kentucky',
  'LA'='Louisiana',
  'ME'='Maine',
  'MD'='Maryland',
  'MA'='Massachusetts',
  'MI'='Michigan',
  'MN'='Minnesota',
  'MS'='Mississippi',
  'MO'='Missouri',
  'MT'='Montana',
  'NE'='Nebraska',
  'NV'='Nevada',
  'NH'='New Hampshire',
  'NJ'='New Jersey',
  'NM'='New Mexico',
  'NY'='New York',
  'NC'='North Carolina',
  'ND'='North Dakota',
  'OH'='Ohio',
  'OK'='Oklahoma',
  'OR'='Oregon',
  'PA'='Pennsylvania',
  'RI'='Rhode Island',
  'SC'='South Carolina',
  'SD'='South Dakota',
  'TN'='Tennessee',
  'TX'='Texas',
  'UT'='Utah',
  'VT'='Vermont',
  'VA'='Virginia',
  'WA'='Washington',
  'WV'='West Virginia',
  'WI'='Wisconsin',
  'WY'='Wyoming',
  'AS'='American Samoa',
  'GU'='Guam',
  'MP'='Northern Mariana Islands',
  'PR'='Puerto Rico',
  'UM'='U.S. Minor Outlying Islands',
  'VI'='U.S. Virgin Islands'
)
