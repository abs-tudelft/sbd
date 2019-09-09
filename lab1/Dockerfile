FROM openjdk:8 as base
ARG SPARK_VERSION=2.4.4
ARG SPARK_HADOOP_VERSION=2.7
ARG SPARK_LOG_DIRECTORY=/spark-events
ENV SPARK_LOG_DIRECTORY=${SPARK_LOG_DIRECTORY}
WORKDIR /spark
RUN curl -L http://apache.mirror.triple-it.nl/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${SPARK_HADOOP_VERSION}.tgz | tar xz --strip-components=1

FROM base as spark-history-server
RUN echo "spark.history.fs.logDirectory file:${SPARK_LOG_DIRECTORY}" > conf/spark-defaults.conf
ENV SPARK_NO_DAEMONIZE=1
EXPOSE 18080
ENTRYPOINT ["./sbin/start-history-server.sh"]

FROM base as spark-submit
RUN echo "spark.eventLog.enabled true" > conf/spark-defaults.conf
RUN echo "spark.eventLog.dir file:${SPARK_LOG_DIRECTORY}" >> conf/spark-defaults.conf
WORKDIR /io
ENTRYPOINT ["/spark/bin/spark-submit"]

FROM base as spark-shell
WORKDIR /io
ENTRYPOINT ["/spark/bin/spark-shell"]
