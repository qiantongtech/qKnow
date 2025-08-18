#!/bin/sh
# filename: publish-server.sh
# 部署千知平台

targetPath="/home/$USER/src/qKnow/qknow-server/target"
# 运行目录
runPath="/home/$USER/opt/qknow-server"

# publish
echo "Publishing qKnow admin ... The installation directory is $runPath"
cd $targetPath
cp qknow-server.jar $runPath
echo "Publish qKnow success! Now you can go to the installation directory($runPath) and restart the server."

cd $runPath
sh restart.sh

tail -f logs/info.log
