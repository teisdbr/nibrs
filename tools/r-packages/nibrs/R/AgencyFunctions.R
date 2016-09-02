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

# functions related to Agency data manipulation

#' @import dplyr
#' @import tidyr
buildAgencyTable <- function(rawAgencyDataFrame) {

  Agency <- rawAgencyDataFrame %>%
    select(AgencyName=NAME,
           AgencyORI=ORI9,
           UCRPopulation=U_TPOP,
           CensusPopulation=LG_POPULATION,
           StateCode=FIPS_ST,
           CountyCode=FIPS_COUNTY,
           StateName=STATENAME,
           CountyName=COUNTYNAME) %>%
    mutate(CensusPopulation=ifelse(CensusPopulation==888888888,NA,CensusPopulation),
           UCRPopulation=ifelse(UCRPopulation==-1 | UCRPopulation==0,NA,UCRPopulation))

  Agency$AgencyID <- 1:nrow(Agency)

  Agency

}
