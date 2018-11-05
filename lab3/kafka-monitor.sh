#!/usr/bin/env bash
set -e
[[ -v KAFKA_HOME ]] || echo "Warning: \$KAFKA_HOME variable not set"

$KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic $1 --property print.key=true --property key.separator=-
