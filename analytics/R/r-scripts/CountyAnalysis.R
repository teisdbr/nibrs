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

# This script produces some analyses and visualizations of NIBRS data to support
# a presentation to the 2016 meeting of ASUCRP in Girdwood, Alaska

library(readr)
library(dplyr)
library(rgeos)
library(sp)
library(rgdal)
library(tidyr)
library(nibrs)
library(geosphere)
library(ggplot2)
library(scales)
library(ggthemes)
library(stringr)
library(httr)
library(rgdal)
library(ggrepel)

buildDataFrames <- function() {
  
  ret <- list()
  
  dailyDf <- buildDailyDetailDf()
  dailySummaryDf <- buildDailySummaryDf(dailyDf)
  monthlySummaryDf <- buildMonthlySummaryDf(dailyDf)
  
  ret$MonthlySummaryDf <- monthlySummaryDf
  ret$DailySummaryDf <- dailySummaryDf
  ret$DailyDetailDf <- dailyDf
  
  ret
  
}

getTheme <- function() {
  ggthemes::theme_hc()
}

getColorScheme <- function() {
  ggthemes::scale_colour_hc()
}

getFillScheme <- function() {
  ggthemes::scale_fill_gdocs()
}

crimeRateGraphics <- function(msdf) {
  ggplot(data=msdf %>% filter(CountyName %in% c('Hocking', 'Summit', 'Thurston')), aes(x=IncidentMonth, y=CrimeRatePer1000)) +
    geom_line(aes(color=CountyName)) +
    scale_x_date(date_breaks="2 months", labels=date_format("%b-%Y")) +
    labs(color="County", y="Offenses per 1000 population", title="Crime Rate for Three Counties, 2013-2014") +
    getTheme() + getColorScheme() + getFillScheme() + theme(axis.title.x=element_blank())
  
}

tempGraphics <- function(dsdf) {
  ggplot(data=dsdf, aes(x=MaxTemp, y=CrimeRatePer10000)) +
    geom_point() +
    geom_smooth(method="lm") + facet_grid(. ~ CountyGroup) +
    labs(x='Maximum Daily Temperature', y="Daily Offenses per 10,000 population",
         title="Crime Rate Variance by Temperature for Urban Counties in Ohio and Washington, 2013-2014") +
    getTheme() + getColorScheme() + getFillScheme()
  # summary(lm(dsdf$CrimeRatePer10000 ~ log(dsdf$MaxTemp)))
  
}

barDensityGraphics <- function(tsdf) {
  print(ggplot(data=tsdf, aes(x=BarDensity, y=CrimeRate)) + geom_point(aes(color=StateName)) + geom_smooth(method="lm") +
    geom_text_repel(data=tsdf %>% filter(BarDensity > 20 | CrimeRate < 100 | CrimeRate > 3000),
                    aes(label=CountyLabel),
                    point.padding=unit(10, "points"), box.padding=unit(2, "points")) +
    labs(x='Bars/Taverns per 100,000 County Residents', y="Offenses in County per year per 10,000 population", color="State:",
         title="Crime Rate and Density of Bars and Taverns, Ohio and Washington Counties, 2013-2014") +
    getTheme() + getColorScheme() + getFillScheme())
  county_shp <- readOGR("/opt/data/Shapefiles/cb_2015_us_county_500k", "cb_2015_us_county_500k")
  county_shp_df <- fortify(county_shp)
  county_shp@data$id <- rownames(county_shp@data)
  wa_shp_df <- inner_join(county_shp_df, county_shp@data %>% filter(STATEFP=='53'), by=c("id"="id")) %>%
    mutate(GEOID=paste0(STATEFP, COUNTYFP)) %>%
    left_join(tsdf, by=c("GEOID"="GEOID"))
  oh_shp_df <- inner_join(county_shp_df, county_shp@data %>% filter(STATEFP=='39'), by=c("id"="id")) %>%
    mutate(GEOID=paste0(STATEFP, COUNTYFP)) %>%
    left_join(tsdf, by=c("GEOID"="GEOID"))
  
  drawMaps <- function(s) {
    print(ggplot(data=s, aes(x=long, y=lat, group=group)) +
      scale_fill_gradient(low = "#f7fbff", high = "#08306b", space = "Lab", na.value = "grey50", guide = "colourbar", name="Bars per 100K\nPopulation") +
      geom_polygon(aes(fill=BarDensity)) + geom_path(color="grey")  +
      coord_map(projection="mercator") +
      theme_few())
    print(ggplot(data=s, aes(x=long, y=lat, group=group)) +
      scale_fill_gradient(low = "#f7fbff", high = "#08306b", space = "Lab", na.value = "grey50", guide = "colourbar", name="Annual Crimes per 10K\nPopulation") +
      geom_polygon(aes(fill=CrimeRate)) + geom_path(color="grey")  +
      coord_map(projection="mercator") +
      theme_few())
  }
  
  drawMaps(wa_shp_df)
  drawMaps(oh_shp_df)
  
  invisible()
  
}

buildTotalSummaryDf <- function(DailyDf) {
  
  DailyDf <- dfs$DailyDetailDf
  
  tsdf <- DailyDf %>%
    mutate(CountyLabel=paste0(CountyName, ", ", recode(StateName, 'Washington'='WA', .default='OH'))) %>%
    group_by(StateName, CountyLabel, GEOID) %>%
    summarize(OffenseCount=sum(OffenseCount), Population=mean(population), BarCount=mean(BarCount)) %>%
    mutate(CrimeRate=(OffenseCount*(10000/2))/Population, BarDensity=(BarCount*100000)/Population) # 2 years worth of data...
  
}

buildDailySummaryDf <- function(DailyDf) {
  dsdf <- DailyDf %>%
    filter(lubridate::day(IncidentDate) > 1) %>%
    group_by(IncidentDate, CountyName, StateName) %>%
    summarize(OffenseCount=sum(OffenseCount), Population=mean(population), MaxTemp=mean(MaxTemp), MinTemp=mean(MinTemp)) %>%
    mutate(CrimeRatePer10000=(OffenseCount*10000)/Population, TempDiff=MaxTemp-MinTemp, TempSum=MaxTemp+MinTemp) %>%
    mutate(CountyGroup=ifelse(
      CountyName %in% c('Franklin', 'Cuyahoga', 'Summit', 'Hamilton', 'Lucas', 'Stark') & StateName=='Ohio', 'Ohio',
      ifelse(CountyName %in% c('King', 'Pierce', 'Clark', 'Thurston') & StateName=='Washington', 'Washington', 'Other'))) %>%
    filter(CountyGroup != 'Other')
  dsdf
}

buildMonthlySummaryDf <- function(DailyDf) {
  mdf <- DailyDf %>%
    group_by(IncidentMonth, CountyName, StateName) %>%
    summarize(OffenseCount=sum(OffenseCount), Population=mean(population)) %>%
    mutate(CrimeRatePer1000=(OffenseCount*1000)/Population)
  mdf
}

buildDailyDetailDf <- function() {
  adf <- buildOffenseDf() %>%
    inner_join(buildCensusDf(), by=c("year"="year", "GEOID"="GEOID")) %>%
    inner_join(buildWeatherDf(), by=c("IncidentDate"="WxDate", "GEOID"="GEOID")) %>%
    left_join(getBarsPerCounty(), by=c("GEOID"="GEOID"))
  adf$IncidentMonth=as.Date(cut(adf$IncidentDate, breaks="month"))
  adf
}

buildCensusDf <- function() {
  popest <- read_csv("/opt/data/Census/CO-EST2015-alldata.csv") %>%
    filter(SUMLEV=='050' & STNAME %in% c('Washington', 'Ohio')) %>%
    select(STATE, COUNTY, POPESTIMATE2013, POPESTIMATE2014, CTYNAME, STNAME) %>%
    mutate(CountyName=gsub(x=CTYNAME, pattern="(.+) County", replacement="\\1"), StateName=STNAME) %>%
    select(-CTYNAME, -STNAME) %>%
    gather(year, population, POPESTIMATE2013, POPESTIMATE2014) %>%
    mutate(year=as.integer(gsub(pattern="POPESTIMATE([0-9]+)", x=year, replacement="\\1")),
           GEOID=paste0(STATE, COUNTY)) %>%
    select(-STATE, -COUNTY)
  popest
}

buildOffenseDf <- function() {
  
  incidentFiles <- c("/tmp/nibrs-oh-2013.txt","/tmp/nibrs-oh-2014.txt","/tmp/nibrs-wa-2013.txt","/tmp/nibrs-wa-2014.txt")
  
  if (!all(file.exists(incidentFiles))) {
    subsetIncidentFile("/opt/data/NIBRS/2013/ICPSR_36121/DS0001/36121-0001-Data.txt", "OH", "/tmp/nibrs-oh-2013.txt")
    subsetIncidentFile("/opt/data/NIBRS/2013/ICPSR_36121/DS0001/36121-0001-Data.txt", "WA", "/tmp/nibrs-wa-2013.txt")
    subsetIncidentFile("/opt/data/NIBRS/2014/ICPSR_36421/DS0001/36421-0001-Data.txt", "OH", "/tmp/nibrs-oh-2014.txt")
    subsetIncidentFile("/opt/data/NIBRS/2014/ICPSR_36421/DS0001/36421-0001-Data.txt", "WA", "/tmp/nibrs-wa-2014.txt")
  }
  
  dfs <- loadICPSRData(agencyFile="/opt/data/ICPSR_35158/DS0001/35158-0001-Data.txt",
                       incidentFiles=incidentFiles,
                       versionYears=c(2013,2014,2013,2014))
  
  OffenseDf <- dfs$OffenseSegment %>%
    select(UCROffenseCodeTypeID, AdministrativeSegmentID) %>%
    inner_join(dfs$AdministrativeSegment %>% select(AdministrativeSegmentID, IncidentDate, AgencyID),
               by=c("AdministrativeSegmentID"="AdministrativeSegmentID")) %>%
    inner_join(dfs$Agency %>% select(AgencyID, StateCode, CountyCode), by=c("AgencyID"="AgencyID")) %>%
    group_by(UCROffenseCodeTypeID, IncidentDate, StateCode, CountyCode) %>%
    summarize(OffenseCount=n()) %>%
    arrange(desc(OffenseCount)) %>%
    mutate(year=lubridate::year(IncidentDate), GEOID=paste0(StateCode, CountyCode)) %>%
    inner_join(dfs$UCROffenseCodeType %>% select(UCROffenseCode, OffenseCategory1, UCROffenseCodeDescription, UCROffenseCodeTypeID),
               by=c("UCROffenseCodeTypeID"="UCROffenseCodeTypeID"))
  
}

buildWeatherDf <- function() {
  
  counties <- read_tsv("/opt/data/Census/Gaz_counties_national.txt") %>%
    select(GEOID, INTPTLAT, INTPTLONG, USPS, NAME) %>%
    filter(USPS %in% c('OH', 'WA')) %>%
    select(-USPS)
  
  stations <- read_fwf("/opt/data/wx/ghcnd-stations.txt",
                       col_positions = fwf_positions(c(1,13,22,32,39,42,73,77,81),
                                                     c(11,20,30,37,40,71,75,79,85),
                                                     col_names=c("StationID","Latitude","Longitude","Elevation", "State", "Name", "GSN", "HCN", "WMO"))) %>%
    filter(grepl("US.+", StationID))
  
  writeLines(paste0("Read ", nrow(stations), " stations from GHCND stations file."))
  
  readGHCND <- function(files) {
    
    wx <- data.frame()
    
    for (file in files) {
      
      writeLines(paste0("Reading GHCND file ", file))
      
      wx <- bind_rows(wx,
                      read_csv(file, col_names=FALSE, progress=FALSE, col_types="cicicccc") %>%
                        filter(grepl("US.+", X1) & X3 %in% c('TMAX', 'TMIN')) %>%
                        select(X1:X4) %>%
                        mutate(X4=X4*9/(5*10) + 32) %>%
                        spread(X3, X4) %>% rename(StationID=X1, MaxTemp=TMAX, MinTemp=TMIN) %>%
                        mutate(WxDate=as.Date(as.character(X2), format="%Y%m%d")) %>% select(-X2))
      
    }
    
    wx
    
  }
  
  ghcndFiles <- c("/opt/data/wx/2013.csv", "/opt/data/wx/2014.csv")
  dailyWeather <- readGHCND(ghcndFiles)
  
  sdf <- dailyWeather %>%
    filter(!is.na(MaxTemp) & !is.na(MinTemp)) %>%
    group_by(StationID) %>%
    summarize(count=n()) %>%
    filter(count==365*length(ghcndFiles))
  
  stations <- stations %>% filter(StationID %in% sdf$StationID)
  
  countiesSP <- SpatialPoints(counties %>% select(INTPTLAT, INTPTLONG))
  stationsSP <- SpatialPoints(stations %>% select(Latitude, Longitude))
  dd <- gDistance(countiesSP, stationsSP, byid=TRUE)
  nearestStation <- apply(dd, 2, which.min)
  nearestStation <- stations[nearestStation, ] %>%
    select(StationID, StationLatitude=Latitude, StationLongitude=Longitude, StationName=Name)
  
  counties <- cbind(counties, nearestStation) %>%
    rename(CentroidLatitude=INTPTLAT, CentroidLongitude=INTPTLONG, Name=NAME)
  
  countiesSP <- SpatialPoints(select(counties, CentroidLatitude, CentroidLongitude))
  countiesSP@proj4string <- CRS("+proj=longlat +datum=WGS84")
  stationsSP <- SpatialPoints(select(counties, StationLatitude, StationLongitude))
  stationsSP@proj4string <- CRS("+proj=longlat +datum=WGS84")
  distances <- spDists(countiesSP, stationsSP, diagonal=TRUE) * .6
  counties$StationDistanceFromCentroid <- distances
  
  writeLines(paste0("Found nearest station in ", nrow(counties), " counties."))
  
  CountyDailyWeather <- dailyWeather %>%
    inner_join(counties %>% select(GEOID, StationID), by=c("StationID"="StationID"))
  
  CountyDailyWeather
  
}

getBarsPerCounty <- function() {
  
  df <- getOSMAmenities()
  
  df %>% group_by(GEOID) %>% summarize(BarCount=n())
  
}

getOSMAmenities <- function() {
  
  df <- data.frame()
  
  for (s in c('Ohio', 'Washington')) {
    writeLines(paste0("Querying OSM for ", s))
    r <- POST(accept_json(), url="http://overpass-api.de/api/interpreter", body=buildOverpassQuery(s))
    writeLines(paste0("Done reading data for ", s))
    c <- content(r, as="text", encoding="UTF-8")
    df <- bind_rows(df, jsonlite::fromJSON(c, flatten=TRUE)$elements)
  }
  writeLines(paste0("Raw data frame rows: ", nrow(df)))
  nodes <- df$nodes
  df <- df[sapply(nodes, is.null),]
  writeLines(paste0("Data frame rows, after removing Ways: ", nrow(df)))
  nodes <- nodes[!sapply(nodes, is.null)]
  writeLines(paste0("Found ", length(nodes), " Ways."))
  tossers <- unlist(sapply(nodes, function(v) {v[-1]}))
  writeLines(paste0("Removing ", length(tossers), " extraneous nodes."))
  
  df <- df %>% select(type, id, lon, lat, feature=tags.amenity, name=tags.name) %>%
    filter(!is.na(lat) & !is.na(lon) & !(id %in% tossers))
  
  writeLines(paste0("Final number of amenities found: ", nrow(df)))
  
  barCoords <- df %>% select(lon, lat)
  barCoords <- SpatialPoints(barCoords)
  usCounties <- readOGR("/opt/data/Shapefiles/tl_2014_us_county/", "tl_2014_us_county")
  proj4string(barCoords) <- proj4string(usCounties)
  geocodedStuff <- over(barCoords, usCounties) %>% select(GEOID)
  cbind(df, geocodedStuff)
  
}

buildOverpassQuery <- function(state) {
  
  q <- read_lines("
                  [out:json][timeout:250];
                  area[name='#state#'][admin_level='4']->.searchArea;
                  (
                  node['amenity'='pub'](area.searchArea);
                  node['amenity'='bar'](area.searchArea);
                  node['amenity'='nightclub'](area.searchArea);
                  way['amenity'='pub'](area.searchArea);
                  way['amenity'='bar'](area.searchArea);
                  way['amenity'='nightclub'](area.searchArea);
                  relation['amenity'='pub'](area.searchArea);
                  relation['amenity'='bar'](area.searchArea);
                  relation['amenity'='nightclub'](area.searchArea);
                  );
                  out body;
                  >;
                  out skel qt;
                  ")
  
  q <- gsub(x=q, pattern="#state#", replacement=state)
  paste0("data=", RCurl::curlEscape(paste0(str_trim(q), collapse="")))
  
}

