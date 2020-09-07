### Dataframe and Dataset

Our previous example is quite a typical use case for Spark. We have a big data
store of some structured (tabular) format (be it csv, JSON, parquet, or
something else) that we would like to analyse, typically in some SQL-like
fashion. Manually applying operations to rows like this is both labour
intensive, and inefficient, as we have knowledge of the 'schema' of data. This
is where DataFrames originate from. Spark has an optimized SQL query engine
that can optimize the compute path as well as provide a more efficient
representation of the rows when given a schema. From the
[Spark SQL, DataFrames and Datasets
Guide](https://spark.apache.org/docs/2.4.6/sql-programming-guide.html#overview):

> Spark SQL is a Spark module for structured data processing. Unlike the basic
> Spark RDD API, the interfaces provided by Spark SQL provide Spark with more
> information about the structure of both the data and the computation being
> performed. Internally, Spark SQL uses this extra information to perform extra
> optimizations. There are several ways to interact with Spark SQL including
> SQL and the Dataset API. When computing a result the same execution engine is
> used, independent of which API/language you are using to express the
> computation. This unification means that developers can easily switch back
> and forth between different APIs based on which provides the most natural way
> to express a given transformation.

Under the hood, these are still immutable distributed collections of data (with
the same compute graph semantics, only now Spark can apply extra
optimizations because of the (structured) format.

Let's do the same analysis as last time using this API. First we will define a
schema. Let's take a look at a single row of the csv:

```
COHUTTA,3/10/14:1:01,10.27,1.73,881,1.56,85,1.94
```

So first a string field, a date, a timestamp, and some numeric information.
We can thus define the schema as such:

```scala
val schema =
  StructType(
    Array(
      StructField("sensorname", StringType, nullable=false),
      StructField("timestamp", TimestampType, nullable=false),
      StructField("numA", DoubleType, nullable=false),
      StructField("numB", DoubleType, nullable=false),
      StructField("numC", LongType, nullable=false),
      StructField("numD", DoubleType, nullable=false),
      StructField("numE", LongType, nullable=false),
      StructField("numF", DoubleType, nullable=false)
    )
  )
```

If we import types first, and then enter this in our interactive shell we get
the following:

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)
import org.apache.spark.sql.types._

val schema =
  StructType(
    Array(
      StructField("sensorname", StringType, nullable=false),
      StructField("timestamp", TimestampType, nullable=false),
      StructField("numA", DoubleType, nullable=false),
      StructField("numB", DoubleType, nullable=false),
      StructField("numC", LongType, nullable=false),
      StructField("numD", DoubleType, nullable=false),
      StructField("numE", LongType, nullable=false),
      StructField("numF", DoubleType, nullable=false)
    )
  )


// Exiting paste mode, now interpreting.

import org.apache.spark.sql.types._
schema: org.apache.spark.sql.types.StructType =
StructType(StructField(sensorname,StringType,false),
StructField(timestamp,TimestampType,false), StructField(numA,DoubleType,false),
StructField(numB,DoubleType,false), StructField(numC,LongType,false),
StructField(numD,DoubleType,false), StructField(numE,LongType,false),
StructField(numF,DoubleType,false))
```

An overview of the different [Spark SQL types](https://spark.apache.org/docs/2.4.6/api/scala/#org.apache.spark.sql.types.package)
can be found online. For the timestamp field we need to specify the format
according to the [Javadate format](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
—in our case `MM/dd/yy:hh:mm`. Tying this all together we can build a Dataframe
like so.

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)
val df = spark.read
              .schema(schema)
              .option("timestampFormat", "MM/dd/yy:hh:mm")
              .csv("./sensordata.csv")
// Exiting paste mode, now interpreting.
df: org.apache.spark.sql.DataFrame =
        [sensorname: string, timestamp: date ... 6 more fields]

scala> df.printSchema
root
 |-- sensorname: string (nullable = true)
 |-- timestamp: timestamp (nullable = true)
 |-- numA: double (nullable = true)
 |-- numB: double (nullable = true)
 |-- numC: long (nullable = true)
 |-- numD: double (nullable = true)
 |-- numE: long (nullable = true)
 |-- numF: double (nullable = true

scala> df.take(5).foreach(println)
[COHUTTA,2014-03-10 01:01:00.0,10.27,1.73,881,1.56,85,1.94]
[COHUTTA,2014-03-10 01:02:00.0,9.67,1.731,882,0.52,87,1.79]
[COHUTTA,2014-03-10 01:03:00.0,10.47,1.732,882,1.7,92,0.66]
[COHUTTA,2014-03-10 01:05:00.0,9.56,1.734,883,1.35,99,0.68]
[COHUTTA,2014-03-10 01:06:00.0,9.74,1.736,884,1.27,92,0.73]
```

We will now continue to perform the same filtering operation as previously
performed on the RDD.
There are three ways in which we could do this:

1. By supplying an SQL query string to Spark SQL, operating on the *untyped*
   `DataFrame`.
2. By using the Scala API for the *untyped* `DataFrame`.
3. By using the Scala API for the *strongly-typed* `DataSet`. 

#### SQL query string
We can use really error prone SQL queries, like so:
```scala
scala> df.createOrReplaceTempView("sensor")

scala> val dfFilter = spark.sql("SELECT * FROM sensor
WHERE timestamp=TIMESTAMP(\"2014-03-10 01:01:00\")")
// I think the newline in the multiline string breaks it if you paste it
dfFilter: org.apache.spark.sql.DataFrame =
            [sensorname: string, timestamp: timestamp ... 6 more fields]

scala> dfFilter.collect.foreach(println)
[COHUTTA,2014-03-10 01:01:00.0,10.27,1.73,881,1.56,85,1.94]
[NANTAHALLA,2014-03-10 01:01:00.0,10.47,1.712,778,1.96,76,0.78]
[THERMALITO,2014-03-10 01:01:00.0,10.24,1.75,777,1.25,80,0.89]
...
```

As you can see, we're simply providing an SQL string to the `spark.sql` method.
The string is not checked by the Scala compiler, but only during run-time by
the Spark SQL library. Any errors will only show up during run-time.
These errors may include both typos in 
* the SQL keywords, and
* the timestamp.

This is not recommended unless you absolutely love SQL and like debugging these
command strings. (This took me about 20 minutes to get right!)

#### DataFrame

A slightly more sane and type-safe way would be to do the following:

```scala
scala> val dfFilter = df.filter("timestamp = TIMESTAMP(\"2014-03-10 01:01:00\")")
dfFilter: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] =
                    [sensorname: string, timestamp: timestamp ... 6 more fields]

scala> dfFilter.collect.foreach(println)
[COHUTTA,2014-03-10 01:01:00.0,10.27,1.73,881,1.56,85,1.94]
[NANTAHALLA,2014-03-10 01:01:00.0,10.47,1.712,778,1.96,76,0.78]
[THERMALITO,2014-03-10 01:01:00.0,10.24,1.75,777,1.25,80,0.89]
...
```

We have now replaced the SQL query of the form:
```sql
SELECT field WHERE predicate
```

... with the `filter()` method of a Spark DataFrame. This is already a bit
better, since the *Scala compiler* and *not the Spark SQL run-time library* can
now check the the existence of the `filter()` method for the DataFrame class.
If we made a typo, we would get a compiler error, before running the code!

Also, the methods supported by DataFrames look much like those of 
[Scala's parallel collections](https://docs.scala-lang.org/overviews/parallel-collections/overview.html),
just like with RDDs, but there are also some SQL-like database-oriented methods
such as `join()`. As such, the Scala API for DataFrames combines the best of
both worlds.

Still, this approach is still error-prone, since it is allowed to write the
filter predicate as an SQL predicate, retaining the problem of potential errors
in the timestamp.

#### DataSet

Luckily, there is also the DataSet abstraction. 
It is a sort of middle ground between DataFrames and RDDs, where you get some of
the type safety of RDDs by operating on a Scala [case
class](https://docs.scala-lang.org/tour/case-classes.html) (also known as
product type).

This allows even more compile-time type checking on the product types, while
still allowing Spark to optimize the query and storage of the data by making use
of schemas.

We do have to write a bit more Scala to be able to use the strongly-typed
DataSet.

```scala
scala> import java.sql.Timestamp
import java.sql.Timestamp

scala> :paste
// Entering paste mode (ctrl-D to finish)

case class SensorData (
    sensorName: String,
    timestamp: Timestamp,
    numA: Double,
    numB: Double,
    numC: Long,
    numD: Double,
    numE: Long,
    numF: Double
)

// Exiting paste mode, now interpreting.

defined class SensorData
```

Now we can convert a Dataframe (which actually is just an untyped DataSet) to a
typed DataSet using the `as` method.

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)

val ds = spark.read
              .schema(schema)
              .option("timestampFormat", "MM/dd/yy:hh:mm")
              .csv("./sensordata.csv")
              .as[SensorData]

// Exiting paste mode, now interpreting.

ds: org.apache.spark.sql.Dataset[SensorData] =
            [sensorname: string, timestamp: timestamp ... 6 more fields]
```

Now we can apply compile-time type-checked operations:

```scala
scala> val dsFilter = ds.filter(a => a.timestamp ==
                                new Timestamp(2014 - 1900, 2, 10, 1, 1, 0, 0))
dsFilter: org.apache.spark.sql.Dataset[SensorData] =
                [sensorname: string, timestamp: timestamp ... 6 more fields]

scala> dsFilter.collect.foreach(println)
SensorData(COHUTTA,2014-03-10 01:01:00.0,10.27,1.73,881,1.56,85,1.94)
SensorData(NANTAHALLA,2014-03-10 01:01:00.0,10.47,1.712,778,1.96,76,0.78)
SensorData(THERMALITO,2014-03-10 01:01:00.0,10.24,1.75,777,1.25,80,0.89)
...
```

Note that the SQL-like predicate used in the DataFrame implementation is now
replaced with the constructor of the Timestamp class. This is more type safe,
since any typos will be detected by the Scala compiler.

We now have an setup where the Scala compiler will check all methods used to
build up the directed acyclic graph (DAG) of our computation exist at every
intermediate resulting DataFrame. We can't make mistakes in the SQL
constructs/keywords anymore, as well as mistakes in the data types thrown into
the filter. This provides us with more guarantees that are queries are valid (at
least on a type level).

DataBricks has published [an excellent
blog](https://databricks.com/blog/2016/07/14/a-tale-of-three-apache-spark-apis-rdds-dataframes-and-datasets.html)
about why DataSets were introduced, next to RDDs. While DataSets don't replace
RDDs, they are nowadays most often used, because they have some more nice
properties as explained before. Read the blog to get to know the details!

This was a brief overview of the 2 (or 3) different Spark APIs. You can always
find more information on the programming guides for
[RDDs](https://spark.apache.org/docs/2.4.6/rdd-programming-guide.html) and
[Dataframes/Datasets](https://spark.apache.org/docs/2.4.6/sql-programming-guide.html)
and in the [Spark
documentation](https://spark.apache.org/docs/2.4.6/api/scala/index.html#package).
