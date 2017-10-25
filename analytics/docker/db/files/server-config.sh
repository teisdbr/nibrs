#! /bin/bash

set -x

cd /tmp

/usr/bin/mysqld_safe --user=root &

until /usr/bin/mysqladmin -u root status > /dev/null 2>&1; do sleep 1; done

# Allow other machines in the docker network to connect, but no others
# NOTE: This is a highly unsecure setup.  This is only for demo purposes.  Don't do this outside of a local demo environment!
echo "CREATE USER 'analytics'@'%' IDENTIFIED BY ''" | mysql -u root
echo "GRANT ALL PRIVILEGES ON *.* TO 'analytics'@'%' WITH GRANT OPTION" | mysql -u root

gunzip nibrs_analytics.sql.gz

echo "create database nibrs_analytics" | mysql -u root
mysql -u root nibrs_analytics < /tmp/nibrs_analytics.sql
rm -f /tmp/nibrs_analytics.sql
