FROM openjdk:8 as base

ARG HADOOP_VERSION=3.2.1
ENV HADOOP_HOME=/hadoop
ENV HADOOP_CLASSPATH=${HADOOP_CLASSPATH}:/hadoop/share/hadoop/tools/lib/*
WORKDIR /hadoop
RUN curl -L http://ftp.tudelft.nl/apache/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz | tar xz --strip-components=1

ARG SPARK_VERSION=2.4.6
ARG SPARK_LOG_DIRECTORY=/spark-events
ENV SPARK_LOG_DIRECTORY=${SPARK_LOG_DIRECTORY}
WORKDIR /spark
RUN curl -L http://ftp.tudelft.nl/apache/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-without-hadoop-scala-2.12.tgz | tar xz --strip-components=1 && \
    echo "export SPARK_DIST_CLASSPATH=$(/hadoop/bin/hadoop classpath)" >> conf/spark-env.sh && \
    echo "spark.hadoop.fs.s3a.aws.credentials.provider org.apache.hadoop.fs.s3a.AnonymousAWSCredentialsProvider" > conf/spark-defaults.conf

FROM base as spark-history-server
ENV SPARK_NO_DAEMONIZE=1
RUN echo "spark.history.fs.logDirectory file:${SPARK_LOG_DIRECTORY}" >> conf/spark-defaults.conf
EXPOSE 18080
ENTRYPOINT ["./sbin/start-history-server.sh"]

FROM base as spark-submit
RUN echo "spark.eventLog.enabled true" >> conf/spark-defaults.conf && \
    echo "spark.eventLog.dir file:${SPARK_LOG_DIRECTORY}" >> conf/spark-defaults.conf
WORKDIR /io
ENTRYPOINT ["/spark/bin/spark-submit"]

FROM base as spark-shell
WORKDIR /io
ENTRYPOINT ["/spark/bin/spark-shell"]
