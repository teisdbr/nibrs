#! /bin/bash

#set -x

apk --update add jq

sed -i -r -e 's/securerandom.source.+/securerandom.source=file:\/dev\/urandom/' /opt/jdk/jdk1.8.0_66/jre/lib/security/java.security

cd /opt/saiku-server
cp start-saiku.sh temp-start-saiku.sh
sed -i "s/\/opt\/saiku-server\/tomcat\/bin\/catalina.sh run/sh startup.sh/g" /opt/saiku-server/temp-start-saiku.sh
./temp-start-saiku.sh


CURLTEST='curl -s -u admin:admin -m 2 http://localhost/saiku/rest/saiku/api/license'

eval $CURLTEST >> /dev/null 2>&1
while [ $? -ne 0 ]; do
       	sleep 2
       	echo "Testing availability of Saiku server..."
   eval $CURLTEST >> /dev/null 2>&1
done

echo "Tomcat successfully started"

curl -sSl -X POST --data 'language=en&username=admin&password=admin' http://localhost/saiku/rest/saiku/session

echo "Removing default/toy data sources and schemas"

curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/datasources | jq -r '.[] | select(.connectionname=="earthquakes")' | grep id | sed -r -e 's/.*: "(.*)",?/curl -sSl -u admin:admin -X DELETE http:\/\/localhost\/saiku\/rest\/saiku\/admin\/datasources\/\1/g' | xargs xargs > /dev/null 2>&1
curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/datasources | jq -r '.[] | select(.connectionname=="foodmart")' | grep id | sed -r -e 's/.*: "(.*)",?/curl -sSl -u admin:admin -X DELETE http:\/\/localhost\/saiku\/rest\/saiku\/admin\/datasources\/\1/g' | xargs xargs > /dev/null 2>&1

curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/schema | jq -r '.[] | select(.name=="foodmart4.xml")' | grep name | sed -r -e 's/.*: "(.*)",?/curl -sSl -u admin:admin -X DELETE http:\/\/localhost\/saiku\/rest\/saiku\/admin\/schema\/\1/g' | xargs xargs > /dev/null 2>&1
curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/schema | jq -r '.[] | select(.name=="earthquakes.xml")' | grep name | sed -r -e 's/.*: "(.*)",?/curl -sSl -u admin:admin -X DELETE http:\/\/localhost\/saiku\/rest\/saiku\/admin\/schema\/\1/g' | xargs xargs > /dev/null 2>&1

echo "Removing default user smith"

curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/users/ | jq -r '.[] | select(.username=="smith")' | grep id | sed -r -e 's/.*: (.*)/curl -sSl -u admin:admin -X DELETE http:\/\/localhost\/saiku\/rest\/saiku\/admin\/users\/\1/g' | xargs xargs > /dev/null 2>&1

echo "Installing Mondrian schemas..."

curl -s -u admin:admin -F name="Analytics" -F file="@/tmp/NIBRSAnalyticsMondrianSchema.xml;filename=NIBRSAnalyticsMondrianSchema.xml;type=text/xml" http://localhost/saiku/rest/saiku/admin/schema/Analytics/

echo "Installing data sources..."

curl -s -X POST -u admin:admin --header "Content-Type: application/json" -T /tmp/Analytics.connection.json http://localhost/saiku/rest/saiku/admin/datasources

if [ "$1" = "dev" ]
then
  echo "\nGenerating admin user with admin privileges (dev mode)"
else
  echo "\nChanging admin user to be a non-admin..."
  curl -s -X PUT -u admin:admin --header "Content-Type: application/json" --data-binary '{"username":"admin","email":"test@admin.com","password":null,"roles":["ROLE_USER"],"id":1}' 'http://localhost/saiku/rest/saiku/admin/users/admin'
fi

echo "\nFinal setup:"

echo "\nUsers:"
curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/users/

echo "\nSchemas:"
curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/schema

echo "\nData sources:"
curl -sSl -u admin:admin http://localhost/saiku/rest/saiku/admin/datasources

#-H 'Pragma: no-cache' -H 'Origin: http://localhost:8011' -H 'Accept-Encoding: gzip, deflate, br' -H 'Accept-Language: en-US,en;q=0.8' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36' -H 'Content-Type: application/json' -H 'Accept: application/json, text/javascript, */*; q=0.01' -H 'Cache-Control: no-cache' -H 'X-Requested-With: XMLHttpRequest' -H 'Cookie: JSESSIONID=741BDC26D2E7CBCE2A8FCBB9D321C0F0; JSESSIONID=AFD7E37B7FC1A21C72969840989EC363; session=.eJyl0EluAyEQheG71Lolhh4ouEpktRiK0AoJFpBYVpS7B8VbL2xlXd__FvUNe6zUEphoc6MJ9iOAgTk6tyqhZ8WDFE5EETTGhYLHTfkZYQLfatx7eaOP4a2WFh0XTkmOXA69aMUlX9YtrKjiOm6Emx9dLt5mGs0IJzjbV9rT0XqpVzAvkHo_G8b-UCqtG-SoWcuHp_cSKH8ddGF5eDbi-7hblx_GTy0H25IrtoZ_rZ8m-GxUb48W8PMLhJ5-8w.DFvOQw.c7M2HtwqusgsXhQFdOILGrchJYI' -H 'Connection: keep-alive' -H 'Referer: http://localhost:8011/saiku-ui/' --data-binary '{"username":"admin","email":"test@admin.com","password":null,"roles":["ROLE_USER"],"id":1}' --compressed

apk del jq

/opt/saiku-server/stop-saiku.sh

rm temp-start-saiku.sh
