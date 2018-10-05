### `nibrs` R package

This directory contains the source code for an R package that supports processing and analysis of
FBI [National Incident Based Reporting System (NIBRS)](https://ucr.fbi.gov/nibrs-overview) data.

The package exposes two R functions:

* `loadICPSRRaw(...)`: Reads data from the "raw" NIBRS extracts hosted at the
University of Michigan's
[Interuniversity Consortium for Political and Social Research (ICPSR)](http://www.icpsr.umich.edu/icpsrweb/NACJD/series/128), transforms the data, and
writes the records into a "staging" database structured to mirror the native NIBRS data structure
* `loadDimensional(...)`: reads data from a NIBRS staging database, transforms them, and writes them into a dimensional database consisting of several
materialized views that support OLAP analysis specified in the NIBRS Mondrian schema (hosted elsewhere in this github repo)

We have created separate staging and dimensional databases to decouple source NIBRS data from the analytical dataset. For demonstration purposes, we provide
the ability to populate the staging database from ICPSR data, but states or local agencies might populate a staging database by extracting from local submissions
or loading from NIBRS submission files. Either way, the staging data can then be transformed into the dimensional structure for analysis.

Each function truncates the contents of its destination (output) database before loading.

#### Installing the package

The package is not available on CRAN. Install instead with `devtools`:

```
devtools::install_github('SEARCH-NCJIS/nibrs/tools/r-packages/nibrs')
```

#### MySQL setup

Both functions in the package require databases to be set up and running.  The `loadICPSRRaw()` function requires the staging database, and the
`loadDimensional()` function requires both staging and dimensional.

The easiest way to run these databases is via Docker, using the images for each database:

* staging: `searchncjis/nibrs-staging-db` [link](https://hub.docker.com/r/searchncjis/nibrs-staging-db/)
* dimensional: `searchncjis/nibrs-analytics-db` [link](https://hub.docker.com/r/searchncjis/nibrs-analytics-db/)

Just run containers from these images, expose the mysql port (3306), and specify the appropriate connection information in the connection objects passed
to each function.

You can also create the staging database in an existing mysql instance from the [DDL](https://github.com/SEARCH-NCJIS/nibrs/blob/master/analytics/db/schema-mysql.sql).

The dimensional database does not have DDL.  The R package creates the table structure as well as the data.

#### Downloading ICPSR data

The package uses the "raw" NIBRS data at ICPSR, not the "Extract Files".  ICPSR publishes the raw data in a variety of formats; the R package uses the
serialized R data frame format.  From the [ICPSR Series Page](https://www.icpsr.umich.edu/icpsrweb/NACJD/series/128#) just visit the page for the year of interest, choosing
the "study" that does *not* have "Extract Files" in the name.  For instance the appropriate study for the 2015 data is [here](https://www.icpsr.umich.edu/icpsrweb/NACJD/studies/36795), not
[this one](https://www.icpsr.umich.edu/icpsrweb/NACJD/studies/36851).  Select the "R" option from the "Download" button above the tabbed area, which is easier than downloading
all the individual files from the "Data & Documentation" tab.  Unzip the resulting zip file; the root directory of the unzipped structure is the directory to pass as the `dataDir=`
parameter to the `nibrs::loadICPSRRaw()` function.

#### Example Run

The following snippets show the output from running the two functions on the ICPSR data from 2015.  Note that the dimensional load only loads a random 5% sample of the records.

```
R version 3.5.1 (2018-07-02) -- "Feather Spray"
Copyright (C) 2018 The R Foundation for Statistical Computing
Platform: x86_64-pc-linux-gnu (64-bit)

R is free software and comes with ABSOLUTELY NO WARRANTY.
You are welcome to redistribute it under certain conditions.
Type 'license()' or 'licence()' for distribution details.

R is a collaborative project with many contributors.
Type 'contributors()' for more information and
'citation()' on how to cite R or R packages in publications.

Type 'demo()' for some demos, 'help()' for on-line help, or
'help.start()' for an HTML browser interface to help.
Type 'q()' to quit R.

> dfs <- nibrs::loadICPSRRaw(conn=DBI::dbConnect(RMariaDB::MariaDB(), host="nibrs-staging-db", dbname="search_nibrs_staging"), dataDir="/nibrs", state="OH")
Loading code table: AdditionalJustifiableHomicideCircumstancesType
Loading code table: AggravatedAssaultHomicideCircumstancesType
Loading code table: ArresteeWasArmedWithType
Loading code table: BiasMotivationType
Loading code table: ClearedExceptionallyType
Loading code table: DispositionOfArresteeUnder18Type
Loading code table: EthnicityOfPersonType
Loading code table: LocationTypeType
Loading code table: MethodOfEntryType
Loading code table: MultipleArresteeSegmentsIndicatorType
Loading code table: OffenderSuspectedOfUsingType
Loading code table: OfficerActivityCircumstanceType
Loading code table: OfficerAssignmentTypeType
Loading code table: PropertyDescriptionType
Loading code table: RaceOfPersonType
Loading code table: VictimOffenderRelationshipType
Loading code table: ResidentStatusOfPersonType
Loading code table: SegmentActionTypeType
Loading code table: SexOfPersonType
Loading code table: SuspectedDrugTypeType
Loading code table: TypeDrugMeasurementType
Loading code table: TypeInjuryType
Loading code table: TypeOfArrestType
Loading code table: TypeOfCriminalActivityType
Loading code table: TypeOfVictimType
Loading code table: TypeOfWeaponForceInvolvedType
Loading code table: TypePropertyLossEtcType
Loading code table: UCROffenseCodeType
Loading code table: AgencyType
Loading code table: CargoTheftIndicatorType
Writing 909 Agency rows to database
Writing 469560 administrative segments to database
Writing 526397 offense segments to database
Writing 531527 OffenderSuspectedOfUsing records to database
Writing 201044 TypeCriminalActivity records to database
Writing 526397 BiasMotivation records to database
Writing 70333 TypeOfWeaponForceInvolved records to database
Writing 406417 property segments to database
Writing 40141 SuspectedDrugType association rows to database
Writing 546604 PropertyType association rows to database
Writing 531362 offender segments to database
> dfs <- nibrs::loadDimensional(stagingConn=DBI::dbConnect(RMariaDB::MariaDB(), host="nibrs-staging-db", dbname="search_nibrs_staging"), dimensionalConn=DBI::dbConnect(RMariaDB::MariaDB(), host="nibrs-analytics-db", dbname="search_nibrs_dimensional", user="analytics", sampleFraction=.05))
Reading dimension tables from staging
Reading fact tables from staging
Creating Incident View
Creating Victim-Offense View
Creating Victim-Offender View
Creating Group A Arrest View
Creating Property View
Creating Group B Arrest View
Writing dimensional db table FullIncidentView
Creating indexes for table FullIncidentView
Writing dimensional db table FullVictimOffenseView
Creating indexes for table FullVictimOffenseView
Writing dimensional db table FullVictimOffenderView
Creating indexes for table FullVictimOffenderView
Writing dimensional db table FullGroupAArrestView
Creating indexes for table FullGroupAArrestView
Writing dimensional db table FullPropertyView
Creating indexes for table FullPropertyView
Writing dimensional db table FullGroupBArrestView
Creating indexes for table FullGroupBArrestView
Writing dimensional db table AdditionalJustifiableHomicideCircumstancesType
Creating indexes for table AdditionalJustifiableHomicideCircumstancesType
Writing dimensional db table AggravatedAssaultHomicideCircumstancesType
Creating indexes for table AggravatedAssaultHomicideCircumstancesType
Writing dimensional db table ArresteeWasArmedWithType
Creating indexes for table ArresteeWasArmedWithType
Writing dimensional db table BiasMotivationType
Creating indexes for table BiasMotivationType
Writing dimensional db table ClearedExceptionallyType
Creating indexes for table ClearedExceptionallyType
Writing dimensional db table DispositionOfArresteeUnder18Type
Creating indexes for table DispositionOfArresteeUnder18Type
Writing dimensional db table EthnicityOfPersonType
Creating indexes for table EthnicityOfPersonType
Writing dimensional db table LocationTypeType
Creating indexes for table LocationTypeType
Writing dimensional db table MethodOfEntryType
Creating indexes for table MethodOfEntryType
Writing dimensional db table OffenderSuspectedOfUsingType
Creating indexes for table OffenderSuspectedOfUsingType
Writing dimensional db table OfficerActivityCircumstanceType
Creating indexes for table OfficerActivityCircumstanceType
Writing dimensional db table OfficerAssignmentTypeType
Creating indexes for table OfficerAssignmentTypeType
Writing dimensional db table PropertyDescriptionType
Creating indexes for table PropertyDescriptionType
Writing dimensional db table RaceOfPersonType
Creating indexes for table RaceOfPersonType
Writing dimensional db table VictimOffenderRelationshipType
Creating indexes for table VictimOffenderRelationshipType
Writing dimensional db table ResidentStatusOfPersonType
Creating indexes for table ResidentStatusOfPersonType
Writing dimensional db table SexOfPersonType
Creating indexes for table SexOfPersonType
Writing dimensional db table SuspectedDrugTypeType
Creating indexes for table SuspectedDrugTypeType
Writing dimensional db table TypeDrugMeasurementType
Creating indexes for table TypeDrugMeasurementType
Writing dimensional db table TypeInjuryType
Creating indexes for table TypeInjuryType
Writing dimensional db table TypeOfArrestType
Creating indexes for table TypeOfArrestType
Writing dimensional db table TypeOfCriminalActivityType
Creating indexes for table TypeOfCriminalActivityType
Writing dimensional db table TypeOfVictimType
Creating indexes for table TypeOfVictimType
Writing dimensional db table TypeOfWeaponForceInvolvedType
Creating indexes for table TypeOfWeaponForceInvolvedType
Writing dimensional db table TypePropertyLossEtcType
Creating indexes for table TypePropertyLossEtcType
Writing dimensional db table UCROffenseCodeType
Creating indexes for table UCROffenseCodeType
Writing dimensional db table DateType
Creating indexes for table DateType
Writing dimensional db table Agency
Creating indexes for table Agency
Writing dimensional db table AgencyType
Creating indexes for table AgencyType
Writing dimensional db table CompletionStatusType
Creating indexes for table CompletionStatusType
Writing dimensional db table ClearanceType
Creating indexes for table ClearanceType
Writing dimensional db table HourType
Creating indexes for table HourType
>
```
