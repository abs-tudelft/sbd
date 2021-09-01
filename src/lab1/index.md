# Lab 1

In this lab, we will design and develop an application to process
the [OpenStreetMap] and [ASTER GDEM V3] data sets for a small subset of the
planet (the Zuid-Holland province of the Netherlands).

This lab will not yet require significant resources in terms of compute, memory
or storage. It should be able to perform this lab on a laptop. The first lab is
mainly meant to familiarize yourself with the toolchains and the data sets, and
perhaps Scala. We will:

1. get familiar with the Spark APIs,
2. analyze the application's scaling behavior, and
3. draw some conclusions on how to run it efficiently at scale for lab 2

[OpenStreetMap]: https://www.openstreetmap.org
[ASTER GDEM V3]: https://asterweb.jpl.nasa.gov/gdem.asp
