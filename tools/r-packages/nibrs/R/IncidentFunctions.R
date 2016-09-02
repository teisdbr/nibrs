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
# Copyright 2012-2015 Open Justice Broker Consortium

# functions related to Incident data manipulation

#' @importFrom lubridate year month
#' @import dplyr
#' @import stringr
buildAdministrativeSegment <- function(rawIncidentsDataFrame, segmentActionTypeTypeID, agencyDataFrame) {

  currentMonth <- formatC(month(Sys.Date()), width=2, flag="0")
  currentYear <- year(Sys.Date())

  AdministrativeSegment <- rawIncidentsDataFrame %>%
    select(AdministrativeSegmentID, ORI, IncidentNumber=INCNUM, INCDATE, IncidentHour=V1007,
           ClearedExceptionallyTypeID=V1013,
           ReportDateIndicator=V1006) %>%
    mutate(IncidentDate=as.Date(ifelse(INCDATE==-5, NA, as.Date(as.character(INCDATE), format="%Y%m%d")), origin="1970-01-01"),
           MonthOfTape=currentMonth, YearOfTape=currentYear, CityIndicator=NA, SegmentActionTypeTypeID=segmentActionTypeTypeID,
           ClearedExceptionallyTypeID=ifelse(ClearedExceptionallyTypeID==-6, 6, ClearedExceptionallyTypeID),
           IncidentHour=ifelse(IncidentHour < 0, NA, IncidentHour)) %>%
    select(-INCDATE)

  ORI_IDmap <- agencyDataFrame %>% select(AgencyID, ORI=AgencyORI)
  AdministrativeSegment <- left_join(AdministrativeSegment, ORI_IDmap, by=c("ORI"="ORI"))
  AdministrativeSegment

}
