#This is an image for NIBRS's SP.  This protects the NIBRS web app and requires users to log in.
#Note, this image requires mounting /opt/certs for the SSL cert
FROM ojbc/portal
MAINTAINER Open Justice Broker Consortium "http://www.ojbc.org"

COPY files/proxy.conf /etc/apache2/conf.d/proxy.conf
COPY files/httpd.conf /etc/apache2/
COPY files/ssl.conf /etc/apache2/conf.d
COPY files/sp-cert.pem /etc/ssl/apache2/
COPY files/sp-key.pem /etc/ssl/apache2/
COPY files/nibrs-ctf.xml /opt/shibboleth-sp/etc/shibboleth/nibrs-ctf.xml
COPY files/shibboleth2.xml /opt/shibboleth-sp/etc/shibboleth/