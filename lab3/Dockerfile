FROM openjdk:8 as base
ARG KAFKA_VERSION=2.3.0
ARG KAFKA_SCALA_VERSION=2.12
WORKDIR /kafka
RUN curl -L http://ftp.tudelft.nl/apache/kafka/${KAFKA_VERSION}/kafka_${KAFKA_SCALA_VERSION}-${KAFKA_VERSION}.tgz | tar xz --strip-components=1

FROM base as zookeeper-server
EXPOSE 2181
ENTRYPOINT ["bin/zookeeper-server-start.sh", "config/zookeeper.properties"]

FROM base as kafka-server
EXPOSE 9092
ENTRYPOINT ["bin/kafka-server-start.sh", "config/server.properties"]

FROM base as kafka-console-consumer
ENTRYPOINT ["bin/kafka-console-consumer.sh"]

FROM node:12-alpine as visualizer
EXPOSE 1234
EXPOSE 12345
WORKDIR /src
RUN npm install -g parcel-bundler
ENTRYPOINT ["npm", "run", "serve"]
