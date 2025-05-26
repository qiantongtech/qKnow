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

