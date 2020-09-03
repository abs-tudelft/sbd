# Rubric for Lab 1

## Course Learning Objectives
For your convenience, we repeat the course learning objectives.

{{#include ../learning-objectives.md}}

## Lab 1 Rubric

Lab 1 is graded by four criteria:
* [Functionality](#functionality)
* [Scalability](#scalability)
* [Libraries](#libraries)
* [Measurements](#measurements)

### Functionality 
* Weight: (50%)
* Related Learning Objectives: L1, L2, L3

The program functions correctly, delivering an answer to the query described
in the description.

Since the dataset is maintained by volunteers, there may be missing and/or 
inconsistent pieces of information.

Potential solutions know several robustness levels:

1. Using only the directly available information, without augmenting the data 
   with e.g. geospatial calculations, resulting in answers that may not include
   data points that could have been augmented at higher robustness levels.
   
2. Using indirectly related data to augment the data through simple (e.g. 
   geospatial) calculations, resulting in approximate answers.

3. Using indirectly related data to augment the data through geospatial 
   calculations that produce an as-correct-as-possible answer.

| Grade          | Indicators                                                  |
|----------------|-------------------------------------------------------------|
| 0 (fail)       | The program does not compile.                               |
|                | The program exits with errors not due to the user.          |
|                | The program does not produce the correct output.            |
| 6 (adequate)   | The robustness level of the program is 1.                   |
| 8 (good)       | The robustness level of the program is 2.                   |
| 10 (excellent) | The robustness level of the program is 3.                   |

### Scalability 
* Weight: (30%)
* Related Learning Objectives: L1, L2

The program is constructed in such a way that in the case multiple computational
nodes (e.g. more Spark workers) work on the problem concurrently, there is 
potential for the performance of the program to increase.

An example when this is not the case is where a `map()` is not applied to a 
distributed dataset (e.g. an `RDD` or a `DataSet`), but perhaps by mistake
the student has first used `dataset.collect()` (taking the data out of the Spark 
context) follow by a plain Scala `.map()`. The map is now applied on a locally
collected dataset, preventing other nodes from performing useful work.  

| Grade          | Indicators                                                  |
|----------------|-------------------------------------------------------------|
| 0 (fail)       | More than one step is implemented in a non-scalable fashion, where it could have been implemented in a scalable fashion. |
| 6 (adequate)   | There is at most one step implemented in a non-scalable fashion, where it could have been implemented in a scalable fashion. |
| 8 (good)       | All steps are implemented in a scalable fashion where applicable. |
| 10 (excellent) | As with (good), in addition to comments in the code describing for each step that causes a shuffle, that it does so and why. |

### Libraries 
* Weight: (10%)
* Related Learning objectives: L1, L3

The program is constructed by using Spark SQL.

| Grade          | Indicators                                                  |
|----------------|-------------------------------------------------------------|
| 0 (fail)       | The student has not used Spark SQL to implement the specified functionality. |
| 6 (adequate)   | The student has used Spark SQL to implement the specified functionality. |
| 8 (good)       | The student has used Spark SQL and does not construct compute paths by combining more primitive Spark functions into functions that already exist in the Spark (SQL) API. |
| 10 (excellent) | The student has introduced abstractions for re-usability. E.g. they have developed a library on top of the Spark libraries providing an easier-to-use API to make similar queries. |

### Measurements 
 * Weight: (10%)
 * Related Learning Objectives: L5
 
The run-time of various computational steps of the program are profiled (this 
functionality exists in Spark). The run-time of the specified computational
steps is reported. 

This criterion is part of learning objectives: L5

| Grade          | Indicators                                                  |
|----------------|-------------------------------------------------------------|
| 0 (fail)       | The student has not reported any measurements.              |
| 10 (excellent) | The student has reported the run-time of the specified computational steps exposed by Spark's profiling functionality. |
