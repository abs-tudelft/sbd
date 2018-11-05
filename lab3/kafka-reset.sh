#!/usr/bin/env bash
set -e
[[ -v KAFKA_HOME ]] || echo "Warning: \$KAFKA_HOME variable not set"

rm -f ./GDELTProducer/segment/*
$KAFKA_HOME/bin/kafka-streams-application-reset --application-id "lab3-gdelt-stream"
$KAFKA_HOME/bin/kafka-topics --zookeeper localhost:2181 --delete --topic "gdelt.*"
