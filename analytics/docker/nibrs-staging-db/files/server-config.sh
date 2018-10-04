#! /bin/bash

set -x

cd /tmp

/usr/bin/mysqld_safe --user=root &

until /usr/bin/mysqladmin -u root status > /dev/null 2>&1; do sleep 1; done

mysql -u root -e "create database search_nibrs_staging"
mysql -u root search_nibrs_staging < /tmp/schema-mysql.sql
mysql -u root -e "CREATE USER 'root'@'%';"
mysql -u root -e "GRANT ALL PRIVILEGES ON search_nibrs_staging.* TO 'root'@'%';"
