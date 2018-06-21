#This is an image for NIBRS's SP, it just copies in a new set of SAML metadata 
FROM ojbc/portal
MAINTAINER Open Justice Broker Consortium "http://www.ojbc.org"
#
#RUN printf "\nInclude /usr/local/apache2/conf/proxy.conf\n" >> /usr/local/apache2/conf/httpd.conf

COPY files/proxy.conf /usr/local/apache2/proxy.conf
#COPY files/httpd.conf /etc/apache2/
#COPY files/ssl.conf /etc/apache2/conf.d
COPY files/sp-cert.pem /etc/ssl/apache2/
COPY files/sp-key.pem /etc/ssl/apache2/
COPY files/nibrs-ctf.xml /opt/shibboleth-sp/etc/shibboleth/nibrs-ctf.xml
COPY files/shibboleth2.xml /opt/shibboleth-sp/etc/shibboleth/