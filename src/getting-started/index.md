# Getting Started

In this chapter, we will cover some of the concepts and technologies that are
used during the course.

## Example repository

The examples in this chapter are accompanied by some code. You can download this
code from its [online repository]. Unless stated otherwise, we usually run
commands in the root folder of this repository. To get this code and go into the
root folder, you could run:

```bash
git clone https://github.com/abs-tudelft/sbd-example.git
cd sbd-example
```

For command-line commands, we're going to assume we're using Linux with Bash. If
you're on Windows or Mac, you have to figure out how to do stuff yourself, or
perhaps use a virtual machine or container.

This chapter will continue to introduce the following topics:

## Docker

An application that allows the user to package and run software (like Spark and
Kafka and the programs we write for them) in an isolated environment: a
container.

## Scala

A programming language that runs on the Java Virtual Machine (JVM). This is our
(mandatory!) language of choice during the lab assignments. We will use it to
program for both Apache Spark and Apache Kafka.

## Apache Spark

A framework for processing large amounts of data on multiple machines in a
robust way. We will build our application for labs 1 and 2 using Spark.

## Amazon Web Services

AWS, which provide theoretically unlimited compute infrastructure, allowing us
to process a large dataset in lab 2.

## Apache Kafka

A framework for building so-called data pipelines, in which potentially many
producers and consumers process real-time, streaming data. In lab 3, we will
take the application from labs 1 and 2 and modify it to process data in
real-time, using Kafka.

## OpenStreetMap

An open source project capturing geographic data from all over the world. The
assignments of this lab are based on (parts of) this data set.

## ASTER Global Digital Elevation Map V3

An open data set with elevation levels of the entire planet (except oceans).

[online repository]: https://github.com/abs-tudelft/sbd-example
