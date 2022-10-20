# Introduction

[Sea levels are rising]. If this continues, areas that are now land will become
part of the oceans. Potential inhabitants must relocate or live on the oceans,
just like Kevin Kostner and Jeanne Tripplehorn in [Waterworld (1995)]. In this
lab, we want to answer the question of what a good relocation plan would be for
inhabitants of flooded areas given a specific amount of sea level rise.

It turns out that we can achieve all the learning objectives of the course by
attempting to answer this question and some variants on it. To this end, we
limit ourselves to a context where we can only use the [OpenStreetMap]
and [ALOS Global Digital Surface Model] datasets, together with various
industry-standard frameworks and tools, such as the [Scala programming language]
to write our programs, [Apache Spark] to process batches of data, [Apache Kafka]
to process streaming data, and the [Amazon Web Services]
(AWS) to scale out our solutions in terms of performance (but also cost!). In
this context, we will face several challenges that are similar to what you could
find in the industry - and we will learn to deal with them efficiently.

## Using this manual

You can browse through the manual by using the table of contents on the left. To
go to the next page, you can also click on the `>`'s on the right of this page.
This manual is generated by [mdBook], and the sources can be found on [GitHub].
Feel free to kindly report issues and/or make pull requests to suggest or
implement improvements! If there are any major changes, we will notify everyone
on Brightspace and Discord.

## Disclaimer

The priority of this lab is to allow you to achieve all learning objectives of
the course. This becomes less boring and more effective if we do not choose the
assignments to be completely abstract and detached from reality (e.g. when we
ask you to join data set A with fields W and X to dataset B with fields Y and Z,
without any further context). The priority of this lab is not to teach you about
earth sciences or policymaking. The contents of this lab manual may include
oversimplifications of these subjects in order to allow us to achieve the
learning goals related to distributed computing on big data more effectively.

[Sea levels are rising]: https://en.wikipedia.org/wiki/Sea_level_rise
[Waterworld (1995)]: https://www.imdb.com/title/tt0114898/
[OpenStreetMap]: https://www.openstreetmap.org
[ALOS Global Digital Surface Model]: https://www.eorc.jaxa.jp/ALOS/en/aw3d30/index.htm
[Scala programming language]: https://www.scala-lang.org/
[Apache Spark]: https://spark.apache.org
[Apache Kafka]: https://kafka.apache.org
[Amazon Web Services]: https://aws.amazon.com
[mdBook]: https://github.com/rust-lang/mdBook
[GitHub]: https://github.com/abs-tudelft/sbd