# Rubric for Lab 2

## Course Learning Objectives

For your convenience, we repeat the course learning objectives.

{{#include ../learning-objectives.md}}

## Criteria

Lab 2 is graded by four criteria:

- [Approach](#approach)
- [Application](#application)
- [Cluster](#cluster)

The grade for this lab is expressed as:

```
Grade = Approach + Application + Cluster
```

We list indicators for specific grades below. Please note these are indicators
only. Under specific unexpected circumstances, TA's may use other indicators,
that are not written down here, to modify the grade. This means that these
indicators should not be used as an exhaustive check-list for the grade, but do
provide a strong recommendation for a specific grade.

### Approach

- Weight: 50%
- Related Learning Objectives: L1

Because the development iterations of big data applications can be large in both
cost and time, the student makes careful, well-informed decisions before
executing the next iteration, and documents their decision-making approach.

Significant iterations that are to be documented include:

1. The first succesful run on the first data set.
2. After significant optimizations are performed and a full run is completed on
   any of the data sets.
3. When going from a smaller dataset to a larger data set results in
   significant changes in where bottlenecks are in the application.

Examples:

- As described in the previous iteration, we discovered a new bottleneck X. This
  was mitigated. After re-running the application, a next bottleneck occurs in
  Y. We have thought of method A, B and C to mitigate this, and have ultimately
  chosen B because of reason Z.
- The query was initially performed on the "The Netherlands" data set, and was
  now run on the USA data set. The USA has significantly more mountain ranges
  so bottleneck X caused by operation Y was relatively increased. We
  have therefore thought of method A, B and C to mitigate this, and have
  ultimately chosen B because of reason Z.

| Grade          | Indicators                                                                                                                                                                                          |
| -------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 0 (fail)       | The student does not explain why they have taken a specific approach before executing a development iteration.                                                                                      |
|                | There is no explicit reasoning behind a specific approach to a next development iteration.                                                                                                          |
| 6 (adequate)   | The student describes their reasoning behind a specific approach to a next development iteration, but their description contains minor mistakes or makes a limited amount of incorrect assumptions. |
| 8 (good)       | The student describes their reasoning behind a specific approach to a next development iteration.                                                                                                   |
| 10 (excellent) | The student describes their reasoning behind multiple (where applicable) alternative approaches (and their selection) to a next development iteration.                                              |

### Application

- Weight: 30%
- Related Learning Objectives: L1, L2, L3, L5

The student selects and executes an appropriate improvement at the application
implementation level to overcome bottlenecks.

An examples where an application implementation improvement is appropriate:

- A dataset is reused multiple times and fits in worker memory, so the student
  has applied `cache()`.

An example where an application implementation is not appropriate:

- A dataset is `repartition()`ed based on some arbitrary value that only works
  well in a specific case (e.g. for a specific dataset), but does not work well
  for other case (e.g. another dataset). Such values should be derived
  dynamically.

| Grade          | Indicators                                                                         |
| -------------- | ---------------------------------------------------------------------------------- |
| 0 (fail)       | The student applies several inappropriate strategies for application improvements. |
| 10 (excellent) | The student applies appropriate strategies for application improvement.            |

### Cluster

- Weight: 20%
- Related Learning Objectives: L1, L4

The student selects and executes an appropriate strategy at the cluster level to
overcome bottlenecks in their implementation, and takes into consideration the
trade-off between cost and performance.

An example where this is done appropriately is: if the application throughput is
bound by network I/O, the student can choose to run the application on instances
that have more network bandwidth.

An example where this is not done appropriately is: the performance of the
application is bound by memory size, but to mitigate this the student moves to
instance types with GPUs to obtain more compute, but not more memory, or the
other way around.

| Grade          | Indicators                                                                     |
| -------------- | ------------------------------------------------------------------------------ |
| 0 (fail)       | The student applies several inappropriate strategies for cluster improvements. |
| 10 (excellent) | The student applies appropriate strategies for cluster improvement.            |
