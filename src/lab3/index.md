# Lab 3

In the third and final lab of SBD we will be implementing a streaming
application. As many of you have noted in the first lab questions, Spark is not
well suited for real-time streaming, because of its batch-processing nature.
Therefore, we will be using _Apache Kafka_ for this lab. You will be provided
with a Kafka stream of GDELT records, for which we want you to create a
histogram of the most popular topics of the last hour that will continuously
update. We included another visualizer for this lab that you can see in
the figure.

![Visualizer for the streaming
application](../assets/images/stream_visualizer.png)
