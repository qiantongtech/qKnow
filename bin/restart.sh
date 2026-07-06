# Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
#
# This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
#
# qKnow is licensed under Apache License 2.0 with additional qKnow terms.
# You may use qKnow for commercial purposes, but you may not remove, hide,
# modify, or replace the qKnow logo, copyright notices, license notices,
# or attribution information without a separate commercial license.
#
# White-label use, OEM distribution, rebranding, or presenting qKnow as
# another product requires separate commercial authorization from
# Jiangsu Qiantong Technology Co., Ltd.
#
# Business License: https://community.qknow.ai/business/policy.html
# See the LICENSE file in the project root for full license information.

!/bin/sh
#filename: restart.sh

echo "stoping ..."
pid=`jps -l | grep 'qknow-server*' | awk '{print $1}'`
if [ ! $pid ]; then
    echo "no such service."
else
    echo "find service. pid=$pid"
    kill -9 $pid
    echo "stop success."
fi

echo "starting ..."
nohup java  -Xms256m -Xmx1024m -server -jar -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod qknow-server.jar >>/dev/null &

# nohup java  -Xms64m -Xmx128m -jar -Djava.security.egd=file:/dev/./urandom qknow-server.jar >>/dev/null &
pid=`jps -l | grep 'qknow-server*' | awk '{print $1}'`
echo "start success. pid=$pid"

