## Questions

Try to be concise with your answers. Some questions have a maximum number of
words you can use, but you are welcome to use fewer if you can.

### General Kafka questions

1.  What is the difference, in terms of data processing, between Kafka and Spark?
2.  What is the difference between replications and partitions?
3.  What is Zookeeper's role in the Kafka cluster? Why do we need a separate
    entity for this? (max. 50 words)
4.  Why does Kafka by default not guarantee _exactly once_ delivery semantics
    on producers? (max. 100 words)
5.  Kafka is a binary protocol (with a reference implementation in Java),
    whereas Spark is a framework. Name two (of the many) advantages of Kafka
    being a binary protocol in the context of Big Data. (max. 100 words)

### Questions specific to the assignment

1.  On average, how many bytes per second does the stream transformer have to
    consume? How many does it produce?
2.  Could you use a Java/Scala data structure instead of a Kafka State Store to
    manage your state in a processor/transformer? Why, or why not?
    (max. 50 words)
3.  Given that the histogram is stored in a Kafka StateStore, how would you
    extract the top 100 topics? Is this efficient? (max. 75 words)
4.  The visualizer draws the histogram in your web browser. A Kafka consumer
    has to communicate the current 'state' of the histogram to this visualizer.
    What do you think is an efficient way of streaming the 'state' of the
    histogram to the webserver?
    (max. 75 words)
5.  What are the two ways you can scale your Kafka implementation over multiple
    nodes? (max. 100 words)
6.  How could you use Kafka's partitioning to compute the histogram in
    parallel? (max. 100 words)
