## NIBRS Toolkit Repository

This repository contains source code artifacts for SEARCH's open source
National Incident-Based Reporting System (NIBRS) toolkit. The purpose of this
toolkit is to support cost-effective participation in the NIBRS program
through the use of open source components that implement key phases of the
NIBRS reporting lifecycle.  These phases (and the associated) components
include:

* [Source code](https://github.com/SEARCH-NCJIS/nibrs/tree/master/web/nibrs-web)
for the NIBRS Pre-Certification Tool (PCT) web application, hosted at http://nibrs.search.org/nibrs-web
* Components of the PCT that are reusable in any Java application (see `nibrs-*` packages
  under https://github.com/SEARCH-NCJIS/nibrs/tree/master/tools):
  * NIBRS object model that represents the NIBRS submission data structure as
  Java objects
  * NIBRS "legacy" flatfile parser that consumes NIBRS submission files and creates
  an object model graph
  * Validation API that implements each of the 400+ NIBRS edit rules
  * Relational database persistence API that saves object model graphs to a relational
  database that conforms to the toolkit's [database model](https://github.com/SEARCH-NCJIS/nibrs/tree/master/analytics/db)
* An [R](http://r-project.org) [package](https://github.com/SEARCH-NCJIS/nibrs/tree/master/tools/r-packages/nibrs)
that processes historical NIBRS extracts in
the [ICPSR NIBRS Repository](http://www.icpsr.umich.edu/icpsrweb/NACJD/series/128) and
loads data into a relational
database that conforms to the toolkit's [database model](https://github.com/SEARCH-NCJIS/nibrs/tree/master/analytics/db)
* A [Mondrian](https://community.hds.com/docs/DOC-1009853)
[schema](https://github.com/SEARCH-NCJIS/nibrs/tree/master/analytics/mondrian)
that enables an
[olap4j](http://www.olap4j.org/) interface, via the Mondrian API, in front of the
toolkit's standard database model
* [Docker](https://www.docker.com/) image for the [PCT](https://github.com/SEARCH-NCJIS/nibrs/tree/master/web/docker/nibrs-web)
* Docker images for a basic Mondrian OLAP demonstration application using
[Saiku](https://community.meteorite.bi/); image definitions are here:
[Saiku application](https://github.com/SEARCH-NCJIS/nibrs/tree/master/analytics/docker/saiku),
[MySQL DB](https://github.com/SEARCH-NCJIS/nibrs/tree/master/analytics/docker/db)

The toolkit team welcomes involvement from the community.  Feel free to fork the
repo and submit PRs. Before merging the PR, we will follow-up with the contributing developer to confirm wording for the copyright acknowledgemnt in the header of the modified file(s).  We also welcome questions or suggestions via
[email](mailto:nibrs@search.org).
