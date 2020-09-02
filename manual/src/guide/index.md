# Guide

In this first chapter, we will cover some of the concepts and technologies that
are used during the course. We will introduce the following topics (in a
different order):

## The GDELT project

A large database of "human society", constructed of mentions of "people,
organizations, locations, themes, counts, images and emotions" around the
planet. As mentioned before, will use the GDELT database to construct a
histogram of the most important themes during a certain timespan.

## Apache Spark

A framework for processing large amounts of data on multiple machines in a
robust way. We will build our application for labs 1 and 2 using Spark.

## Amazon Web Services

AWS, which provide theoretically unlimited compute infrastructure,
allowing us to process a dataset as large as the entire GDELT database in
lab 2.

## Apache Kafka

A framework for building so-called data pipelines, in which potentially
many producers and consumers process real-time, streaming data. In lab 3,
we will take the application from labs 1 and 2 and modify it to process
data in real-time, using Kafka.

## Scala

A programming language that runs on the Java Virtual Machine (JVM). This is
our (mandatory!) language of choice during the lab assignments. We will use
it to program for both Apache Spark and Apache Kafka.

## Docker

An application that allows the user to package and run software (like Spark
and Kafka and the programs we write for them) in an isolated environment: a
container.
