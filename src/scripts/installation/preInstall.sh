#!/bin/bash

set -x

service insurance-integration stop || true

rm -rf /opt/insurance-integration/
rm -f /etc/init.d/insurance-integration
rm -rf /etc/insurance-integration/
rm -rf /var/log/insurance-integration/