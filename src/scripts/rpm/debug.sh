#!/bin/bash

DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8030,server=y,suspend=n"

nohup java -jar ${DEBUG_OPTS} /opt/insurance-integration/bin/insurance-integration.jar \
 --spring.config.location=/etc/insurance-integration/insurance-integration.properties \
 >> /var/log/insurance-integration/insurance-integration.log \
 2>&1 &