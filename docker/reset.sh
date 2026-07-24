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

echo "🔄 开始执行系统重置流程（共 5 个阶段）..."
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
# 阶段 D: 重置 MySQL 数据
# ==================================================================
echo "🔄 阶段 D: 初始化 MySQL"

# 执行 SQL 初始化脚本
echo "   → 执行 MySQL 初始化脚本 init-show.sql..."
# 注意：这里假设 mysql 容器已经处于运行状态，如果上面停止了 mysql，这里需要先 docker start mysql
docker exec -i qknow-demo-mysql57-1 mysql -uroot -p'lJ7gP0cA5dD2dA1hA3bJ' < ./qKnow/db/init/init-show.sql

echo "✅ 阶段 D-1 完成：MySQL 已初始化"
echo "----------------------------------------"


# ==================================================================
# 阶段 E: 重置 upload 数据
# ==================================================================
echo "🔄 阶段 E: 初始化 upload"

# 清理 upload 数据
sudo rm -rf ./qKnow/server/upload/*

# 恢复初始数据
cp -r ./qKnow/server/upload-init/* ./qKnow/server/upload/
sudo chown -R $USER:$USER ./qKnow/server/upload

echo "✅ 阶段 E 完成：upload 已初始化"
echo "----------------------------------------"

# ==================================================================
# 启动 qKnow API + 智能等待 + 启动 Nginx
# ==================================================================

# 2. 启动 qKnow API 容器
echo "   → 启动 qKnow API 服务..."
docker start qknow-demo-api-1

# 3. 智能等待 API 就绪 - 检测任何响应
MAX_WAIT=60
COUNT=0

echo "   → 正在等待 API 服务完全启动 (最多 ${MAX_WAIT} 秒)..."

# 先给容器一些启动时间
sleep 10

until docker exec qknow-demo-api-1 curl -s -o /dev/null -w "%{http_code}" http://localhost:8090/actuator/health 2>/dev/null | grep -q "200\|401"; do
    sleep 3
    COUNT=$((COUNT + 3))

    if [ $COUNT -ge $MAX_WAIT ]; then
        echo "⚠️ 警告: API 服务在 ${MAX_WAIT} 秒内未完全响应，尝试继续启动 Nginx..."
        echo "   → 最近的 API 日志："
        docker logs qknow-demo-api-1 --tail 10 2>&1 | sed 's/^/        /'
        break
    fi
    echo "      ... API 尚未就绪，已等待 ${COUNT} 秒"
done

if [ $COUNT -lt $MAX_WAIT ]; then
    echo "   ✅ API 已就绪，等待 5 秒确保完全加载..."
    sleep 5
fi

echo "✅ 阶段完成：检测到 API 服务已响应"
echo "----------------------------------------"

# 4. 最后启动 Nginx
echo "   → 启动 Nginx 网关..."
docker start qknow-demo-nginx-1

# ==================================================================
# 完成提示
# ==================================================================
echo "🎉 系统重置成功！所有组件已恢复初始状态。"
