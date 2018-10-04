FROM rocker/tidyverse:3.5.1

LABEL maintainer="SEARCH" \
  org.label-schema.description="Image that loads NIBRS data from source datasets into mariadb"

RUN apt-get update && apt-get install -y libmariadbclient-dev && R -e 'install.packages("RMariaDB")'

COPY files/nibrs_1.0.0.tar.gz /tmp/
RUN R -e 'install.packages("/tmp/nibrs_1.0.0.tar.gz", repos = NULL, type="source")'
