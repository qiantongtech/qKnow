#!/bin/bash
# ==================================================================
# 文件名: reset.sh
# 功能: 重置 qKnow开源版 系统演示数据
# 作者: qKnow
# 日期: 2026-06-05
# 注意: 必须在项目根目录下运行此脚本（即 docker/ 目录）
# ==================================================================

set -e  # 遇错立即退出

# docker-compose 项目的路径
COMPOSE_DIR="/home/qtt/opt/qKnow-demo/docker"

cd "$COMPOSE_DIR"

echo "🔄 开始执行系统重置流程（共 4 个阶段）..."
echo "========================================"

# ==================================================================
# 阶段 A: 停止所有相关服务
# ==================================================================
echo "🛑 阶段 A: 停止所有服务"

# 停止 qKnow API 容器（忽略已停止的情况）
echo "   → 停止 qKnow API 服务..."
docker stop qknow-demo-api-1

# 停止 Neo4j 容器
echo "   → 停止 neo4j 服务..."
docker stop  qknow-demo-neo4j-1

# 停止 weaviate 服务
echo "   → 停止 weaviate 服务..."
docker stop qknow-demo-weaviate-1


# ==================================================================
# 阶段 B: 重置 weaviate 数据
# ==================================================================
echo "🔄 阶段 B: 重置 weaviate 数据目录"

sudo rm -rf ./qKnow/weaviate/*

# 重置向量库数据
sudo cp -r ./qKnow/weaviate-init/* ./qKnow/weaviate/
sudo chown -R $USER:$USER ./qKnow/weaviate

sudo rm -rf ./qKnow/indexDir/*

# 重置向量库检索数据
sudo cp -r ./qKnow/indexDir-init/* ./qKnow/indexDir/
sudo chown -R $USER:$USER ./qKnow/indexDir

docker start qknow-demo-weaviate-1


echo "✅ 阶段 B 完成：weaviate 已重置并启动"
echo "----------------------------------------"

# ==================================================================
# 阶段 C: 重置 Neo4j 环境
# ==================================================================
echo "🔄 阶段 C: 重置 Neo4j 数据目录"

# 清理 Neo4j 数据
sudo rm -rf ./qKnow/neo4j/*

# 恢复初始数据（如配置文件、初始库等）
cp -r ./qKnow/neo4j-init/* ./qKnow/neo4j/
sudo chown -R $USER:$USER ./qKnow/neo4j

# 启动 neo4j 服务
docker start qknow-demo-neo4j-1

echo "✅ 阶段 C 完成：Neo4j 已重置并启动"
echo "----------------------------------------"

# ==================================================================
# 阶段 D: 重置 MySQL 数据 + 启动 qKnow API
# ==================================================================
echo "🔄 阶段 D: 初始化 MySQL 并启动 qKnow API"

# 执行 SQL 初始化脚本（密码含特殊字符，用单引号包裹）
echo "   → 执行 MySQL 初始化脚本 init-show.sql..."
docker exec -i qknow-demo-mysql57-1 mysql -uroot -p'lJ7gP0cA5dD2dA1hA3bJ' < ./qKnow/db/init/init-show.sql

echo "✅ 阶段 D 完成：MySQL 已初始化"
echo "----------------------------------------"

# 启动 qKnow API 容器（忽略已停止的情况）
docker start qknow-demo-api-1

# 启动 qKnow nginx 容器（忽略已停止的情况）
docker start qknow-demo-nginx-1
# ==================================================================
# 完成提示
# ==================================================================
echo "🎉 系统重置成功！所有组件已恢复初始状态。"
