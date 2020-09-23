## Assignment

Your task is to run your Spark application on the entire [OpenStreetMap data
set](https://registry.opendata.aws/osm/). The [entire
planet](https://open.quiltdata.com/b/osm-pds) is available on Amazon S3.
Processing the entire planet requires a significant amount of resources, which
is why an iterative process of improving your application and running it on
increasing input data set sizes is required.

Start by downloading [the
Netherlands](https://download.geofabrik.de/europe/netherlands-latest.osm.pbf)
and converting it to an ORC file locally. Before running your application on
Amazon EMR you should run your application locally on the Netherlands. **This
helps you catch performance and scalability issues early-on and prevents you
from wasting your AWS credits**.

Use the Spark [tuning guide](https://spark.apache.org/docs/2.4.7/tuning.html)
and the [SQL performance tuning
page](https://spark.apache.org/docs/2.4.7/sql-performance-tuning.html) to
understand how you can improve the scalability and performance of your
application.

Understanding how Spark executes your application is the first step towards
optimizing it. Use the
[`.explain`](<https://spark.apache.org/docs/2.4.7/api/scala/index.html#org.apache.spark.sql.Dataset@explain():Unit>)
method on your resulting dataset and check the [SQL
tab](https://spark.apache.org/docs/latest/web-ui.html#sql-tab) in your Spark
History Server.

### Running your application on Amazon EMR

To run Spark on multiple nodes we are using EMR for this assignment. Selecting
suitable instance types and cluster configurations to efficiently map resource
requirements from your application is an important part of this assignment. Next
to modifying your application, understanding resource utilization and using
different cluster configurations are part of the iterative process to
efficiently scale your application.

When you feel confident your application is ready to process a bigger input data
set (the Netherlands should run in minutes on a modern laptop), you can package
your application for execution on EMR. The
[sbt-assembly](https://github.com/sbt/sbt-assembly) plugin (to build fat JARs -
that include dependencies you can use to optimize your implementation) is
available in your project so run the `assembly` command to package your
application.

Please note:

- When using Amazon's services, please **use the N.Virgina region (`us-east-1`).
  This is where the S3 buckets with the data sets are hosted.** Create your
  buckets and clusters in this region. This is the only region that should be 
  used during this course.

- Always start with a small number of small instance types e.g. 1 master node
  (`c5.xlarge`) and 5 core nodes (`c5.xlarge`). Make sure your application is
  scalable before spinning up large clusters (and/or more expensive instance
  types) to prevent wasting credits.

- Check the following links for information about configuring Spark on EMR:
  - https://docs.aws.amazon.com/emr/latest/ReleaseGuide/emr-spark-configure.html
  - https://aws.amazon.com/blogs/big-data/best-practices-for-successfully-managing-memory-for-apache-spark-applications-on-amazon-emr/

- You don't have to specify a master in your Spark Session builder or in the
  arguments of spark-submit.

- Write the resulting ORC file to your S3 bucket.
  (`s3://<your-bucket>/output.orc`)

- Scalability comes at a cost, you can't ignore a proper trade-off between 
  runtime and cost. For example,decreasing the run time by 10% while increasing 
  the monetary cost by 500% is typically not acceptable.

### Data sets

The following data sets of increasing size are available on S3 and can be used
in your iterative development process:

1. Netherlands (1.2 GB) - `s3://abs-tudelft-sbd20/netherlands.orc`
2. United States (8.8 GB) - `s3://abs-tudelft-sbd20/us.orc`
3. Europe (27.7 GB) - `s3://abs-tudelft-sbd20/europe.orc`
4. Planet (75.8 GB) - `s3://osm-pds/planet/planet-latest.orc`

### Runtime indications

The following runtime indications are available to prevent you from wasting your
credits. Make sure to experiment with other instance types and cluster
configurations as well.

These results are from an implementation built by the TAs. Using 1 master node
(`c5.xlarge`) and 5 core nodes (`c5.xlarge`):

1. Netherlands - 1.5 min - aim for less than 5 minutes
2. United States - 6.9 min - aim for less than 15 minutes
3. Europe - 22 min - aim for less than 1 hour
4. Planet - 56 min - aim for less than 2 hours
