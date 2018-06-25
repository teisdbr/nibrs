# 

FROM ojbc/idp

MAINTAINER Open Justice Broker Consortium "http://www.ojbc.org"


WORKDIR /tmp

RUN cp /opt/tomcat/lib/tomcat-jdbc.jar /opt/shibboleth-idp/edit-webapp/WEB-INF/lib
COPY files/JAAS_relational_login.jar /opt/shibboleth-idp/edit-webapp/WEB-INF/lib
COPY files/mysql-connector-java-8.0.11.jar /opt/shibboleth-idp/edit-webapp/WEB-INF/lib
COPY files/mysql-connector-java-8.0.11.jar /opt/tomcat/lib
COPY files/jaas.config /opt/shibboleth-idp/conf/authn
COPY files/password-authn-config.xml /opt/shibboleth-idp/conf/authn
COPY files/attribute-resolver.xml /opt/shibboleth-idp/conf/attribute-resolver.xml
COPY files/attribute-filter.xml /opt/shibboleth-idp/conf/attribute-filter.xml
COPY files/idp.properties /opt/shibboleth-idp/conf/idp.properties
COPY files/idp-signing.* /opt/shibboleth-idp/credentials/
COPY files/login.vm /opt/shibboleth-idp/views
COPY files/nibrs-logo.png /opt/shibboleth-idp/edit-webapp/images
COPY files/messages.properties /opt/shibboleth-idp/messages
COPY files/main.css /opt/shibboleth-idp/edit-webapp/css
COPY files/nibrs-ctf.xml /opt/shibboleth-idp/metadata/nibrs-ctf.xml
COPY files/metadata-providers.xml /opt/shibboleth-idp/conf
RUN  /opt/shibboleth-idp/bin/build.sh -DLOCATION=/opt/shibboleth-idp
