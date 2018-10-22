SET logdir="logs\"

if not exist %logdir% mkdir %logdir%

START cmd /c %KAFKA_HOME%\bin\windows\zookeeper-server-start.bat %KAFKA_HOME%\config\zookeeper.properties ^> %logdir%zookeeper.log
TIMEOUT 5 
START cmd /c %KAFKA_HOME%\bin\windows\kafka-server-start.bat %KAFKA_HOME%\config\server.properties ^> %logdir%kafka.log
TIMEOUT 5
