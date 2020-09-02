## Goal of this Lab

The goal of this lab is to:

- familiarize yourself with Apache Spark, the MapReduce programming model,
  and Scala as a programming language;
- learn how to characterize your big data problem analytically and
  practically and what machines best fit this profile;
- get hands-on experience with cloud-based systems;
- learn about the existing infrastructure for big data and the difficulties
  with these; and
- learn how an existing application should be modified to function in a
  streaming data context.

You will work in groups of two. In this lab manual we will introduce a big data
pipeline for identifying important events from the GDELT Global Knowledge Graph
(GKG).

In lab 1, you will start by writing a Spark application that processes the
GDELT dataset. You will run this application on a small subset of data on
your local computer. You will use this to

1.  get familiar with the Spark APIs,
2.  analyze the application's scaling behavior, and
3.  draw some conclusions on how to run it efficiently in the cloud.

It is up to you how you want to define _efficiently_, which can be in terms of
performance, cost, or a combination of the two.

You may have noticed that the first lab does not contain any supercomputing,
let alone big data. For lab 2, you will deploy your code on AWS, in an actual
big data cluster, in an effort to scale up your application to process the
complete dataset, which measures several terabytes. It is up to you to find the
configuration that will get you the most efficiency, as per your definition in
lab 1.

For the final lab, we will modify the code from lab 1 to work in a streaming
data context. You will attempt to rewrite the application to process events in
real-time, in a way that is still scalable over many machines.
