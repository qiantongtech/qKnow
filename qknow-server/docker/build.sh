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
#!/bin/bash
# 删除旧镜像
docker rmi qiantong/qknow:2.2.3

# 重新构建镜像 替换成你的代理或者自行更改国内源
docker build --no-cache \
  --build-arg http_proxy=http://192.168.0.108:10809 \
  --build-arg https_proxy=http://192.168.0.108:10809 \
  -t qiantong/qknow:2.2.3 .

# 启动新容器
docker run -d \
  --name qknow \
  -p 8090:8090 \
  qiantong/qknow:2.2.3
