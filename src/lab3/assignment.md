## Assignment

As mentioned before, for this assignment, we will no longer batch process the
GDELT Global Knowledge Graph, but rather stream it into a pipeline that
computes a histogram of the last hour. This pipeline is depicted in the figure below.
We will give a small description of the individual parts below.

![GDELT streaming pipeline](../assets/images/kafka_pipeline.png)

### Producer

The producer, contained in the `GDELTProducer` Scala project, starts by
downloading all segments of the previous hour (minus a 15 minute offset), and
immediately start streaming records (rows) to a Kafka topic called `gdelt`.
Simultaneously, it will schedule a new download step at the next quarter of the
hour. The frequency by which the records are streamed is determined as the current
amount of queued records over the time left until new data is downloaded from
S3.

### Transformer

The transformer receives GDELT records on the `gdelt` topic and should use
them to construct a histogram of the names from the "allNames" column of the
dataset, but only for the last hour. This is very similar to the application
you wrote in Lab 1, but it happens in real-time and you should take care to
also decrement/remove names that are older than an hour (relative to your input
data). Finally, the transformer's output should appear on a Kafka topic called
`gdelt-histogram`.

### Consumer

The consumer finally acts as a _sink_, and will process the incoming
histogram updates from the transformer into a smaller histogram of only the 100
most occurring names for display (It might turn out that this is too much for your browser to
handle. If this is the case, you may change it manually in the
`HistogramProcessor` contained in `GDELTConsumer.scala`.). It will finally stream this
histogram to our visualizer over a WebSocket connection.

You are now tasked with writing an implementation of the histogram transformer.
In the file `GDELTStream/GDELTStream.scala` you will have to implement the
following

### GDELT row processing

In the main function you will first have to write a function that filters
the GDELT lines to a stream of allNames column. You can achieve this using
the high-level API of Kafka Streams, on the `KStream` object.

### HistogramTransformer

You will have to implement the `HistogramTransformer` using the
processor/transformer API of kafka streams, to convert the stream of
allNames into a histogram of the last hour. We suggest you look at [state
stores for Kafka streaming](https://kafka.apache.org/23/documentation/streams/developer-guide/processor-api.html).

You will have to write the result of this stream to a new topic called
`gdelt-histogram`.
This stream should consist of records (key-value pairs) of the form `(name, count)` and type `(String, Long)`, where the value of name was extracted from
the "allNames" column.

This means that whenever the transformer reads a name from the "allNames"
column, it should publish an updated, i.e. incremented, (name, count) pair on
the output topic, so that the visualizer can update accordingly. When you
decrement a name, because its occurrence is older than an hour, remember to
publish an update as well!

Note that you are only allowed to modify the `GDELTStream.scala` file. We
should be able to compile and run your code without any other modifications.
