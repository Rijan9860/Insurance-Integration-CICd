#!/bin/bash

set -x

USER=bahmni
GROUP=bahmni

id -g ${GROUP} 2>/dev/null
if [ $? -eq 1 ]; then
    groupadd ${GROUP}
fi

id ${USER} 2>/dev/null
if [ $? -eq 1 ]; then
    useradd -g ${USER} ${USER}
fi

usermod -s /usr/sbin/nologin bahmni

mkdir -p /opt/insurance-integration/var/log/
mkdir /etc/insurance-integration/
mkdir /var/log/insurance-integration/

chown -R bahmni:bahmni /opt/insurance-integration/
chmod +x /opt/insurance-integration/bin/insurance-integration

mv /opt/insurance-integration/bin/insurance-integration*.jar /opt/insurance-integration/bin/insurance-integration.jar

ln -sf /opt/insurance-integration/etc/application.properties /etc/insurance-integration/insurance-integration.properties
ln -sf /opt/insurance-integration/etc/log4j.properties /etc/insurance-integration/log4j.properties
ln -sf /opt/insurance-integration/var/log/insurance-integration.log /var/log/insurance-integration/insurance-integration.log
ln -sf /opt/insurance-integration/bin/insurance-integration /etc/init.d/insurance-integration

chkconfig --add insurance-integration