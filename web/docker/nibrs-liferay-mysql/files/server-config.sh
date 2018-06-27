#! /bin/bash

#set -x

cd /tmp

/usr/bin/mysqld_safe --user=root &

until /usr/bin/mysqladmin -u root status > /dev/null 2>&1; do sleep 1; done

echo "CREATE USER 'lportal'@'%'  IDENTIFIED BY 'lportal'"| mysql -u root
echo "CREATE DATABASE lportal"| mysql -u root
echo "GRANT ALL PRIVILEGES ON lportal.* TO 'lportal'@'%' WITH GRANT OPTION" | mysql -u root

gunzip lportal.sql.gz
mysql -u root lportal < /tmp/lportal.sql
rm -f /tmp/lportal.sql