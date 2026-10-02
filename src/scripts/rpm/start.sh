#!/bin/bash

nohup java -jar /opt/insurance-integration/bin/insurance-integration.jar \
 --spring.config.location=/etc/insurance-integration/insurance-integration.properties \
 >> /var/log/insurance-integration/insurance-integration.log \
 2>&1 &