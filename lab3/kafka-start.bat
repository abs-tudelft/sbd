SET logdir="logs/"

if not exist %logdir% mkdir %logdir%

START %kafka_home%/bin/windows/zookeeper-server-start.bat %kafka_home%/config/zookeeper.properties > %logdir%
TIMEOUT 5 
START %kafka_home%/bin/windows/kafka-server-start.bat %kafka_home%/config/server.properties > %logdir%
TIMEOUT 5
