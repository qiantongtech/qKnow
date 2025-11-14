#!/bin/bash
# 删除旧镜像
docker rmi qiantong/qknow:1.0.0

# 重新构建镜像 替换成你的代理或者自行更改国内源
docker build --no-cache \
  --build-arg http_proxy=http://192.168.0.108:10809 \
  --build-arg https_proxy=http://192.168.0.108:10809 \
  -t qiantong/qknow:1.0.0 .

# 启动新容器
docker run -d \
  --name qknow \
  -p 8090:8090 \
  qiantong/qknow:1.0.0
