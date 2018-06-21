FROM ojbc/mysql
MAINTAINER Open Justice Broker Consortium "http://www.ojbc.org"

COPY files/server-config.sh /tmp
COPY files/lportal.sql.gz /tmp
RUN chmod a+x /tmp/server-config.sh
RUN /tmp/server-config.sh