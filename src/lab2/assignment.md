## Assignment

For this lab, we would like you to process the entire dataset, meaning all
segments, with 20 `c4.8xlarge` core nodes, in under half an hour, using your
solution from lab 1. This should cost you less than 12 dollars and is the
minimum requirement.

Note that this means that you should evaluate whether you application scales
well enough to achieve this before attempting to run it on the entire dataset.
Your answers to the questions in lab 1 should help you to determine this, but
to reiterate: consider e.g. how long it will take to run

1. 1000 segments compared to 10000 segments?
2. on 4 virtual cores compared to 8, and what about 32?

If your application is not efficient enough right away, you should analyze its
bottlenecks and modify it accordingly, or try to gain some extra performance by
modifying the way Spark is configured.
You can try a couple of runs on the entire dataset when you have a good
understanding of what might happen on the entire dataset.

For extra points, we challenge you to come up with an even better solution
according to the metric you defined in lab 1. You are free to change anything,
but some suggestions are:

- Find additional bottlenecks using Apache Ganglia (need more network I/O, or
  more CPU?, more memory?)
- Tuning the kind and number of machines you use on AWS, based on these
  bottlenecks
- Modifying the application to increase performance
- Tuning Yarn/Spark configuration flags to best match the problem

There is a [guide to Spark performance] tuning on the Spark website.

[guide to spark performance]: https://spark.apache.org/docs/latest/tuning.html
