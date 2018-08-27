FROM  snasello/liferay-6.2
MAINTAINER Open Justice Broker Consortium "http://www.ojbc.org"
# Start MySQL image
#
# docker run --name nibrs-mysql -e MYSQL_ROOT_PASSWORD=mysecretpassword -e MYSQL_USER=lportal -e MYSQL_PASSWORD=lportal -e MYSQL_DATABASE=lportal -d mysql:5.6
#
# Start Liferay image with a link to the MySQL database
#
# docker run --name nibrs-liferay --rm -it -p 8080:8080 --link nibrs-mysql:db_lep -e DB_TYPE=MYSQL nibrs-liferay
#
COPY files/portal-ext.properties /opt/liferay-portal-6.2-ce-ga6/tomcat-7.0.62/webapps/ROOT/WEB-INF/classes
COPY files/create_account.jsp /opt/liferay-portal-6.2-ce-ga6/tomcat-7.0.62/webapps/ROOT/html/portlet/login/navigation
#