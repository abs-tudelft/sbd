# Introduction

In this lab we will put the concepts that are central to Supercomputing with
Big Data in some practical context. We will analyze a large open data set and
identify a way of processing it efficiently using [Apache Spark] and the
[Amazon Web Services] (AWS). The data set in question is the [GDELT 2.0 Global
Knowledge Graph] (GKG), which indexes persons, organizations, companies,
locations, themes, and even emotions from live news reports in print, broadcast
and internet sources all over the world. We will use this data to construct a
histogram of the topics that are most popular on a given day, hopefully giving
us some interesting insights into the most important themes in recent history.

Feedback is appreciated! The lab files will be hosted on [GitHub]. You can
also find the most up-to-date version of this manual over there. Feel free to
make issues and/or pull requests to suggest or implement improvements.

[amazon web services]: https://aws.amazon.com
[github]: https://github.com/abs-tudelft/sbd
[gdelt 2.0 global knowledge graph]: https://blog.gdeltproject.org/introducing-gkg-2-0-the-next-generation-of-the-gdelt-global-knowledge-graph/
[apache spark]: https://spark.apache.org
