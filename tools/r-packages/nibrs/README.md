### `nibrs` R package

This directory contains the source code for an R package that supports processing and analysis of
FBI [National Incident Based Reporting System (NIBRS)](https://ucr.fbi.gov/nibrs-overview) data.


#### Installing the package

The package is not available on CRAN. Install instead with `devtools`:

```
devtools::install_github('SEARCH-NCJIS/nibrs/tools/r-packages/nibrs')
```

#### MySQL setup

The package loads data into a MySQL relational database, via a DBI connection object passed into the main loading function.  By default, this
connection object points to a database, running on localhost (standard port 3306), named `nibrs_analytics`.  This database needs to be up and running
prior to running the loading function.  To create the database, forward-engineer the DDL SQL from the SQL Power Architect model in GitHub, at
https://github.com/SEARCH-NCJIS/nibrs/tree/master/analytics/db (save the SQL as, e.g., nibrs_analytics.sql) and:

```
mysql -u root -e 'create database nibrs_analytics'
mysql -u root nibrs_analytics < nibrs_analytics.sql
```

#### Support for loading ICPSR extracts

Currently, the package's main purpose is to read data from the NIBRS extracts hosted at the 
University of Michigan's [Interuniversity Consortium for Political and Social Research (ICPSR)](http://www.icpsr.umich.edu/icpsrweb/NACJD/series/128).
These extracts are available at no cost for public download (after registering at ICPSR and accepting a usage agreement).

The package makes use of the Incident-level file (DS0001) and the Arrestee-level file (DS0004).

Once the data are downloaded, the next step is to filter the (rather large) files for the state of interest.  (The package will support the loading
of data from multiple states, or all of them, but this is usually unnecessary and creates a somewhat unwieldy database.)  In the examples that follow,
we assume you have downloaded and unzipped the extracts covering 2013 to `/opt/data/NIBRS/2013/ICPSR_36121`.  Note that the package only makes use
of the `.txt` file in each zipfile; it is safe to delete the other large datafiles.  Save the `.txt` extract and gunzip it.

To extract data for Ohio (one of the states with relatively complete statewide coverage in the dataset):

```
nibrs::filterICPSRFiles('/opt/data/NIBRS/2013/ICPSR_36121/DS0001/36121-0001-Data.txt', 'OH', '/opt/data/NIBRS/2013/ICPSR_36121/DS0001')
```

Then:

```
dfs <- loadICPSR(incidentFile='/opt/data/NIBRS/2013/ICPSR_36121/DS0001/OH-1.txt',
	arresteeFile='/opt/data/NIBRS/2013/ICPSR_36121/DS0004/OH-1.txt', versionYear=2013)
```

This function returns a list of data frames (tibbles, actually), one tibble corresponding to each table in the relational database. The function writes these tables
to the database as it goes (erasing any data that exist in the tables first).

It's also possible to pass in `incidentRecords=` and `arresteeRecords=` parameters to load only that number of records from the extract files.  This can be useful for
testing.
