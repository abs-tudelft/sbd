## Apache Kafka

Apache Kafka is a distributed streaming platform. The core abstraction is that
of a message queue, to which you can both publish and subscribe to streams of
records. Each queue is named by means of a topic. Apache Kafka is:

- Resilient by means of replication;
- Scalable on a cluster;
- High-throughput and low-latency; and
- A persistent store.

Kafka consists of 4 APIs, from the Kafka docs:

### The Producer API
Allows an application to publish a stream of records to one or more Kafka
topics.

### The Consumer API
Allows an application to subscribe to one or more topics and process the
stream of records produced to them.

### The Streams API
Allows an application to act as a stream processor, consuming an input
stream from one or more topics and producing an output stream to one or
more output topics, effectively transforming the input streams to output
streams.

### The Connector API
Allows building and running reusable producers or consumers that connect
Kafka topics to existing applications or data systems. For example, a
connector to a relational database might capture every change to a table.

Before you start with the lab, please read the [Introduction to Kafka on the Kafka
website](https://kafka.apache.org/intro), to become familiar with the Apache
Kafka abstraction and internals. A good introduction to the [Kafka stream API can be found
here](https://docs.confluent.io/current/streams/quickstart.html). We recommend
you go through the code and examples.

We will again be using Scala for this assignment. Although Kafka's API is
completely written in Java, the streams API has been wrapped in a Scala API for
convenience. You can find the Scala KStreams documentation
[here](https://developer.lightbend.com/docs/api/kafka-streams-scala/0.2.1/com/lightbend/kafka/scala/streams/KStreamS.html),
for API docs on the different parts of Kafka, like `StateStores`, please refer
to [this link](https://kafka.apache.org/23/javadoc/overview-summary.html).
