# Rubric for Lab 3

## Course Learning Objectives
For your convenience, we repeat the course learning objectives.

{{#include ../learning-objectives.md}}

## Criteria

Lab 3 is graded by the following criteria:
* [Functionality](#functionality)
* [Streaming](#streaming)
* [Analysis](#analysis)

The grade for this lab is expressed as:

```
Grade = Functionality + Streaming + Analysis
```

We list indicators for specific grades below. Please note these are indicators
only. Under specific unexpected circumstances, TA's may use other indicators,
that are not written down here, to modify the grade. This means that these
indicators should not be used as an exhaustive check-list for the grade, but do
provide a strong recommendation for a specific grade.

### Functionality 
* Weight: 30%
* Related Learning Objectives: L2, L5

The student has implemented an application that functions according to the 
specifications given in the lab manual.

With regard to the functionality of the transformer application, we stipulate
the following:
* For every incoming record on the `events` stream, an update is produced.
* When event records fall outside the window of interest and cause a change in 
  any stateful data structure related to the `updates` stream, an update is also
  produced. Observe the example carefully.

| Grade          | Indicators                                                  |
|----------------|-------------------------------------------------------------|
| 0 (fail)       | The program does not compile.                               |
|                | The program exits with errors not due to the user.          |
|                | The program does not produce the correct output.            |
| 6 (adequate)   | The program compiles.                                       |
|                | The program exits without errors, unless the user does something wrong. |
|                | The program produces the correct updates for every event.   |
|                | The program does not produce the correct updates when stateful data structures change due to events falling outside the window of interest. |
| 10 (excellent) | The program compiles.                                       |
|                | The program exits without errors. When the user does something wrong, a descriptive text of how to correct their input is returned. |
|                | The program does produces the correct output.               |

### Streaming 
* Weight: 40%
* Related Learning Objectives: L2, L5

The student makes use of Kafka's ability to implement streamable low-latency
applications. To this end, the student avoids micro-batching as much as
possible, and applies stateless processing methods as much as possible. Of
course, there may be processing steps that do require state.

An example of micro-batching is as follows. Consider some input stream with some
integers: `{0, 1, 2, 3}` and an application that has to produce an output stream
with the squared value of the integers `{0, 1, 4, 9}`. (Here, assume every value
is a discrete Kafka record).  A student would be applying micro-batching when
they would place the integers in e.g. a Kafka store, and trigger the squaring
operation and producing output records e.g. periodically or e.g. when the amount
of integers reaches some threshold, processing all input records in the store at
once. The student should avoid micro-batching by using stateless
transformations, since this specific functionality does not require state.

| Grade          | Indicators                                                  |
|----------------|-------------------------------------------------------------|
| 0  (fail)      | The transformer application is in more than one place implemented with stateful methods where they are not required or desired. |
|                | Micro-batching is applied in more than one place.           |
| 6  (adequate)  | The transformer application is in at most one place implemented with a stateful method where it is not required or desired. |
|                | Micro-batching is applied in at most one place.             |
| 10 (excellent) | The transformer application uses stateless methods as much as possible, avoiding stateful methods where not required or desired. |
|                | The transformer application produces an update for every incoming record without applying micro-batching. |  

### Analysis
 * Weight: (30%)
 * Related Learning Objectives: L1, L2, L3, L5
 
The student shows a thorough understanding of the produced code and its behavior
in the context of Apache Kafka. This is conveyed through the code, comments in
the code, and the report.

| Grade          | Indicators                                                  |
|----------------|-------------------------------------------------------------|
| 0 (fail)       | The student shows a lack of understanding of the constructs used in the solution. |
|                | The code does not contain any descriptive comments.         |
|                | There report provided cannot be considered a reasonable attempt. |
| 6 (adequate)   | The student shows a decent understanding of the constructs used in their solution, but often makes minor mistakes. |
|                | The student has explained most non-trivial constructs in their solution. |
| 8 (good)       | The student shows a decent understanding of the constructs used in their solution, and only makes minor to negligible mistakes in their analysis. |
|                | The student has explained all non-trivial constructs in their solution. |
| 10 (excellent) | The student shows a thorough understanding of the constructs used in their solution, without making any mistakes. |
|                | The student has explained all non-trivial constructs in their solution. |
