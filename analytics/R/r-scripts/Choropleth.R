# Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics

# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

#     http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# This script produces a simple choropleth using NIBRS property crime data

library(RMySQL)
library(rgdal)
library(ggplot2)
library(dplyr)
library(ggthemes)

conn <- dbConnect(MySQL(), host="localhost", dbname="nibrs_analytics", username="root")

# this shapefile is better for states like Ohio that have counties extending into a body of water...

county_shp <- readOGR("/opt/data/Shapefiles/glin_oh_county_boundaries_2000", "oh_county_boundaries_2000")
county_shp_df <- fortify(county_shp)

countyData <- dbGetQuery(conn, "select CountyCode, sum(NumberOfRecoveredMotorVehicles) as recovered, sum(NumberOfStolenMotorVehicles) as stolen from PropertySegment, AdministrativeSegment, Agency where PropertySegment.AdministrativeSegmentID = AdministrativeSegment.AdministrativeSegmentID and AdministrativeSegment.AgencyID = Agency.AgencyID group by CountyCode")
countyData <- countyData %>%
  mutate(PercentRecovered=ifelse(is.na(recovered) | is.na(stolen) | stolen==0, NA, recovered/stolen)) %>%
  rename(county=CountyCode)

county_shp@data$id <- rownames(county_shp@data)
county_shp_df <- left_join(county_shp_df, county_shp@data)
county_shp_df <- left_join(county_shp_df, countyData)

ggplot(data=county_shp_df, aes(x=long, y=lat, group=group)) +
  geom_path(color="grey") +
  scale_fill_gradient(low = "#ffffcc", high = "#ff4444", space = "Lab", na.value = "grey50", guide = "colourbar", name="% Recovered") +
  geom_polygon(aes(fill=PercentRecovered)) + coord_map(projection="mercator") +
  labs(title="Stolen Vehicle Recovery Success (NIBRS Data, 2013)") + theme_few() +
  theme(axis.title.x=element_blank(), axis.title.y=element_blank(),
        axis.ticks.x=element_blank(), axis.ticks.y=element_blank(),
        axis.text.x=element_blank(), axis.text.y=element_blank())

dbDisconnect(conn)
