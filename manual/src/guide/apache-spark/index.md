## Apache Spark

Apache Spark provides a programming model for a resilient distributed
shared memory model. To elaborate on this, Spark allows you to program against
a _unified view_ of memory (i.e. RDD or DataFrame), while the processing
happens _distributed_ over _multiple nodes/machines/computers/servers_ being
able to compensate for _failures of these nodes_.

This allows us to define a computation and scale this over multiple machines
without having to think about communication, distribution of data, and
potential failures of nodes. This advantage comes at a cost: All applications
have to comply with Spark's (restricted) programming model.

The programming model Spark exposes is based around the MapReduce paradigm.
This is an important consideration when you would consider using Spark, does my
problem fit into this paradigm?

Modern Spark exposes two APIs around this programming model:

1. Resilient Distributed Datasets
2. Spark SQL Dataframe/Datasets

In the rest of this section, we will demonstrate a simple application with
implementations using both APIs.
