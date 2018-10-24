rm -f ./GDELTProducer/segment/*
kafka-streams-application-reset --application-id "lab3-gdelt-stream"
kafka-topics --zookeeper localhost:2181 --delete --topic "gdelt.*"
