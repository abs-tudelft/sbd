## Questions

General questions:

1.  In typical use, what kind of operation would be more expensive, a narrow
    dependency or a wide dependency? Why? (max. 50 words)
2.  What is the shuffle operation and why is it such an important topic in
    Spark optimization? (max. 100 words)
3.  In what way can Dataframes and Datasets improve performance both in
    compute, but also in the distributing of data compared to RDDs? Under what
    conditions will Dataframes perform better than RDDs? (max. 100 words)
4.  Consider the following scenario. You are running a Spark program on a big
    data cluster with 10 worker nodes and a single master node. One of the
    worker nodes fails. In what way does Spark's programming model help you
    recover the lost work? (Think about the execution plan or DAG) (max. 50
    words)
5.  We might distinguish the following five conceptual parallel programming
    models:

    1.  farmer/worker
    2.  divide and conquer
    3.  data parallelism
    4.  function parallelism
    5.  bulk-synchronous

    Pick one of these models and explain why it does or does not fit Spark's
    programming model. (max. 100 words)

Implementation analysis questions:

1.  Measure the execution times for 10, 20, 50, 100 and 150 segments. Do you
    observe a difference in execution time between your Dataframe and RDD
    implementations? Is this what you expect and why? (max. 50 words)
2.  How will your application scale when increasing the amount of analyzed
    segments? What do you expect the progression in execution time will be for, 100,
    1000, 10000 segments? (max. 50 words)
3.  If you extrapolate the scaling behavior on your machine, using your results
    from question 1, to the entire dataset, how much time will it take to
    process the entire dataset? Is this extrapolation reasonable for a single
    machine? (max. 50 words)
4.  Now suppose you had a cluster of identical machines with that you performed the
    analysis on. How many machines do you think you would need to process the
    entire dataset in under an hour? Do you think this is a valid
    extrapolation? (max. 50 words)
5.  Suppose you would run this analysis for a company. What do you think would
    be an appropriate way to measure the performance? Would it be the time it
    takes to execute? The amount of money it takes to perform the analysis on
    the cluster? A combination of these two, or something else? Pick something
    you think would be an interesting metric, as this is the metric you will be
    optimizing in the 2nd lab! (max. 100 words)
