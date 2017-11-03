# 
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
# 

# Dockerfile that extends base saiku to add demo data sources

FROM ojbc/saiku

WORKDIR /tmp

COPY files/server-config.sh /tmp/
COPY files/Analytics.connection.json /tmp/
COPY files/NIBRSAnalyticsMondrianSchema.xml /tmp/

RUN chmod a+x /tmp/server-config.sh
RUN /tmp/server-config.sh

RUN wget -P /opt/saiku-server/tomcat/webapps/saiku/WEB-INF/lib https://downloads.mariadb.com/Connectors/java/connector-java-2.1.2/mariadb-java-client-2.1.2.jar

RUN cd /opt/saiku-server/tomcat

CMD ["/opt/saiku-server/start-saiku.sh"]