# Lab 1

In this lab, we will design and develop an application to process [OpenStreetMap](https://www.openstreetmap.org)
data. For a small subset of the planet (the Zuid-Holland provice of the
Netherlands), the application should output a list of cities and [brewery](https://wiki.openstreetmap.org/wiki/Brewery)
counts, ordered by the number of breweries.

You will run this application on a small subset of data on your local computer.
You will use this to:

1.  get familiar with the Spark APIs,
2.  analyze the application's scaling behavior, and
3.  draw some conclusions on how to run it efficiently in the cloud.

It is up to you how you want to define _efficiently_, which can be in terms of
performance, cost, or a combination of the two.

You may have noticed that the first lab does not contain any supercomputing,
let alone big data. For lab 2, you will deploy your code on AWS, in an actual
big data cluster, in an effort to scale up your application to process a larger
dataset. It is up to you to find the configuration that will get you the most
efficiency, as per your definition.
