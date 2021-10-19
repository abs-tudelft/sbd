## Assignment

Your task is to run your Spark application on the entire [OpenStreetMap data
set]. The [entire planet] is available on Amazon S3. Processing the entire 
planet requires a significant amount of resources, which is why an iterative 
process of improving your application and running it on increasing input data 
set sizes is required.

Start by downloading a bigger country from [Geofabrik] and convert it to an ORC 
file locally. Before running your application on Amazon EMR you should run your 
application locally on a bigger data set. **This helps you catch performance 
and scalability issues early-on and prevents you from wasting AWS credits**.

Use the Spark [tuning guide] and the [SQL performance tuning page] to
understand how you can improve the scalability and performance of your
application.

### Running your application on Amazon EMR

Please review the information presented 
[in the getting started guide](../getting-started/amazon-web-services.md), where
it is explained how to spawn your own cluster and how to upload your application
to it.

To run Spark on multiple nodes we are using EMR for this assignment. Selecting
suitable instance types and cluster configurations to efficiently map resource
requirements from your application is an important part of this assignment. Next
to modifying your application, understanding resource utilization and using
different cluster configurations are part of the iterative process to
efficiently scale your application.

When you feel confident your application is ready to process a bigger input data
set (the Netherlands should run in minutes on a modern laptop), you can package
your application for execution on EMR. The [sbt-assembly] plugin (to build fat 
JARs - that include dependencies you can use to optimize your implementation) is
available in your project so run the `assembly` command to package your 
application.

Please note:

- When using Amazon's services, **ONLY use the N.Virgina region (`us-east-1`).
  This is where the S3 buckets with the data sets are hosted.** Create your
  buckets and clusters in this region. **This is the only region that should be
  used during this course.**

- Always **start with a small number of small instance types** e.g. 1 master
  node  (`c5.xlarge`) and 5 core nodes (`c5.xlarge`). Make sure your application
  is scalable before spinning up large clusters (and/or more expensive instance
  types) to prevent wasting credits.

- Check the following links for information about configuring Spark on EMR:

  - https://docs.aws.amazon.com/emr/latest/ReleaseGuide/emr-spark-configure.html
  - https://aws.amazon.com/blogs/big-data/best-practices-for-successfully-managing-memory-for-apache-spark-applications-on-amazon-emr/

- You don't have to specify a master in your Spark Session builder or in the
  arguments of spark-submit.

- Write the resulting ORC file to your S3 bucket.
  (`s3://<your-bucket>/output.orc`)

- Scalability comes at a cost, you can't ignore a proper trade-off between
  runtime and cost. For example, decreasing the run time by 10% while increasing
  the monetary cost by 500% is typically not acceptable.

### Data sets on S3

The following data sets of increasing size are available on S3 and can be used
in your iterative development process:

#### OpenStreetMap:
1. France (1.2 GB) - `s3://abs-tudelft-sbd-2021/france.orc`
2. United States (8.8 GB) - `s3://abs-tudelft-sbd-2021/north-america.orc`
3. Europe (27.7 GB) - `s3://abs-tudelft-sbd-2021/europe.orc`
4. Planet (75.8 GB) - `s3://osm-pds/planet/planet-latest.orc`

#### ALOS:
- Parquet files are in: `s3://abs-tudelft-sbd-2021/ALPSMLC30.parquet/`
- The Parquet files contain statistics that help reduce the amount of time it
  takes to load a file. For example, for a so-called row-group, it stores the
  minimum and maximum values of numeric columns. If you apply a latitude and
  longitude range filter on the ALOS data set immediately after loading, Spark
  will push down this filter to the Parquet reader. This causes only row groups
  within a certain latitude or longitude range to be loaded from storage, rather
  than loading all ALOS points for the whole world. **This will save you a
  significant amount of time.**
  
[OpenStreetMap data set]: https://registry.opendata.aws/osm/
[entire planet]: https://open.quiltdata.com/b/osm-pds
[Geofabrik]: https://download.geofabrik.de/europe/
[tuning guide]: https://spark.apache.org/docs/3.1.2/tuning.html
[SQL performance tuning page]: https://spark.apache.org/docs/3.1.2/sql-performance-tuning.html
[sbt-assembly]: https://github.com/sbt/sbt-assembly