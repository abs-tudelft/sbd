### Packaging your application using SBT

We showed how to run Spark in interactive mode. Now we will explain how to build
applications that can be submitted using the `spark-submit` command.

First, we will explain how to structure a Scala project, using the [SBT build
tool](https://www.scala-sbt.org). The typical project structure is

```
├── build.sbt
├── project
│   └── build.properties
└── src
    └── main
        └── scala
            └── example.scala
```

This is typical for JVM languages. More directories are added under the `scala`
folder to resemble the package structure.

The project's name, dependencies, and versioning is defined in the `build.sbt`
file. An example `build.sbt` file is

```scala
name := "Example"
version := "0.1.0"
scalaVersion := "2.12.14"
```

This specifies the Scala version of the project (2.12.14) and the name of the
project.

If you run `sbt` in this folder it will generate the project directory and
`build.properties`. `build.properties` contains the SBT version that is
used to build the project with, for backwards compatibility.

Open `example.scala` and add the following

```scala
package example


object Example {
  def main(args: Array[String]) {
    println("Hello world!")
  }
}
```

Start the `sbt` container in the root folder (the one where `build.sbt` is
located). This puts you in interactive mode of SBT. We can compile the sources
by writing the `compile` command.

```
$ docker run -it --rm -v "`pwd`":/root sbt sbt
copying runtime jar...
[info] welcome to sbt 1.5.5 (Oracle Corporation Java 11.0.12)
[info] loading project definition from /root/project
[info] loading settings for project root from build.sbt ...
[info] set current project to Example (in build file:/root/)
[info] sbt server started at local:///root/.sbt/1.0/server/27dc1aa3fdf4049b492d/sock
[info] started sbt server
sbt:Example>
```

We can now type `compile`.

```
sbt:Example> compile
[info] compiling 1 Scala source to /root/target/scala-2.12/classes ...
...
[info] Non-compiled module 'compiler-bridge_2.12' for Scala 2.12.14. Compiling...
[info]   Compilation completed in 10.128s.
[success] Total time: 14 s, completed Aug 26, 2021, 3:03:34 PM
```

We can try to run the application by typing `run`.

```
sbt:Example> run
[info] running example.Example
Hello world!
[success] Total time: 1 s, completed Aug 26, 2021, 3:05:29 PM
```

Now let's add a function to `example.scala`.

```scala
object Example {
  def addOne(tuple: (Char, Int)) : (Char, Int) = tuple match {
    case (chr, int) => (chr, int+1)
  }
  def main(args: Array[String]) {
    println("Hello world!")
    println(addOne('a', 1))
  }
}
```

In your SBT session we can prepend any command with a tilde (`~`) to make them
run automatically on source changes.

```
sbt:Example> ~run
[info] Compiling 1 Scala source to /root/target/scala-2.12/classes ...
[info] running example.Example
Hello world!
(a,2)
[success] Total time: 0 s, completed Sep 7, 2020 10:40:56 AM
[info] 1. Monitoring source files for root/run...
[info]    Press <enter> to interrupt or '?' for more options.

```

We can also open an interactive session using SBT.

```
sbt:Example> console
[info] Starting scala interpreter...
Welcome to Scala 2.12.14 (OpenJDK 64-Bit Server VM, Java 11.0.12).
Type in expressions for evaluation. Or try :help.

scala> example.Example.addOne('a', 1)
res1: (Char, Int) = (a,2)

scala> println("Interactive environment")
Interactive environment
```

To build Spark applications with SBT we need to include dependencies (Spark
most notably) to build the project. Modify your `build.sbt` file like so

```scala
name := "Example"
version := "0.1.0"
scalaVersion := "2.12.14"

val sparkVersion = "3.1.2"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)
```

If you still have the SBT shell opened, you must use `reload` to make sure your
`build.sbt` is updated.

We could now use Spark in the script (after running `compile`).

Let's implement a Spark application.
Modify `example.scala` as follows, but don't run the code yet!

```scala
package example

import org.apache.spark.sql.types._
import org.apache.spark.sql._
import java.sql.Timestamp


object ExampleSpark {
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
  def main(args: Array[String]) {
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

    val spark = SparkSession
      .builder
      .appName("Example")
      .getOrCreate()
    val sc = spark.sparkContext // If you need SparkContext object

    import spark.implicits._

    val ds = spark.read
                  .schema(schema)
                  .option("timestampFormat", "M/d/y:h:m")
                  .csv("./sensordata.csv")
                  .as[SensorData]

    val dsFilter = ds.filter(a => a.timestamp ==
        new Timestamp(2014 - 1900, 2, 10, 1, 1, 0, 0))

    dsFilter.collect.foreach(println)

    spark.stop
  }
}
```

We will not run this code, but submit it to a local Spark "cluster" (on your
machine). To do so, we require a JAR. You can build a JAR using the `package`
command in SBT. This JAR will be located in the
`target/scala-version/project_name_version.jar`.

You can run the JAR via a `spark-submit` container (which will run on local
mode). By mounting the `spark-events` directory the event log of the
application run is stored to be inspected later using the Spark history server.

```
$ docker run -it --rm -v "`pwd`":/io -v "`pwd`"/spark-events:/spark-events spark-submit target/scala-2.12/example_2.12-0.1.0.jar
```

The output should look as follows:

```
2020-09-07 11:07:28,890 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
2020-09-07 11:07:29,068 INFO spark.SparkContext: Running Spark version 3.1.2
2020-09-07 11:07:29,087 INFO spark.SparkContext: Submitted application: Example

...

SensorData(COHUTTA,2014-03-10 01:01:00.0,10.27,1.73,881,1.56,85,1.94)
SensorData(NANTAHALLA,2014-03-10 01:01:00.0,10.47,1.712,778,1.96,76,0.78)
SensorData(THERMALITO,2014-03-10 01:01:00.0,10.24,1.75,777,1.25,80,0.89)
SensorData(BUTTE,2014-03-10 01:01:00.0,10.12,1.379,777,1.58,83,0.67)
SensorData(CARGO,2014-03-10 01:01:00.0,9.93,1.903,778,0.55,76,1.44)
SensorData(LAGNAPPE,2014-03-10 01:01:00.0,9.59,1.602,777,0.09,88,1.78)
SensorData(CHER,2014-03-10 01:01:00.0,10.17,1.653,777,1.89,96,1.57)
SensorData(ANDOUILLE,2014-03-10 01:01:00.0,10.26,1.048,777,1.88,94,1.66)
SensorData(MOJO,2014-03-10 01:01:00.0,10.47,1.828,967,0.36,77,1.75)
SensorData(BBKING,2014-03-10 01:01:00.0,10.03,0.839,967,1.17,80,1.28)

...

2020-09-07 11:07:33,694 INFO util.ShutdownHookManager: Shutdown hook called
2020-09-07 11:07:33,694 INFO util.ShutdownHookManager: Deleting directory /tmp/spark-757daa7c-c317-428e-934f-aaa9e74bf808
2020-09-07 11:07:33,696 INFO util.ShutdownHookManager: Deleting directory /tmp/spark-a38554ba-18fc-46aa-aa1e-0972e24a4cb0
```

By default, Spark's logging is quite verbose. You can change the [log levels
to warn](https://stackoverflow.com/questions/27781187/how-to-stop-info-messages-displaying-on-spark-console)
to reduce the output.

For development purposes you can also try running the application from SBT
using the `run` command. Make sure to set the Spark master to `local` in your
code. You might run into some trouble with threads here, which can be solved
by running the application in a forked process, which can be enabled by
setting `fork in run := true` in `build.sbt`. You will also have to set to
change the log levels programmatically, if desired.

```scala
import org.apache.log4j.{Level, Logger}
...


def main(args: Array[String]) {
    ...
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    ...
}
```

You can also use this logger to log your application which might be helpful for
debugging on the AWS cluster later on.

You can inspect the event log from the application run using the Spark history
server. Start a `spark-history-server` container from the project root folder
and mount the `spark-events` folder in the container.

```
$ docker run -it --rm -v "`pwd`"/spark-events/:/spark-events -p 18080:18080 spark-history-server
```

The output will look as follows:

```
starting org.apache.spark.deploy.history.HistoryServer, logging to /spark/logs/spark--org.apache.spark.deploy.history.HistoryServer-1-5b5de5805769.out

...

2020-09-07 11:10:23,020 INFO history.FsHistoryProvider: Parsing file:/spark-events/local-1599477015931 for listing data... 2020-09-07
11:10:23,034 INFO history.FsHistoryProvider: Finished parsing file:/spark-events/local-1599477015931
```

Navigate to [http://localhost:18080](localhost:18080) to view detailed
information about your jobs.
After analysis you can shutdown the Spark history server using ctrl+C.

```
^C
2020-09-07 11:13:21,619 ERROR history.HistoryServer: RECEIVED SIGNAL INT
2020-09-07 11:13:21,630 INFO server.AbstractConnector: Stopped Spark@70219bf{HTTP/1.1,[http/1.1]}{0.0.0.0:18080}
2020-09-07 11:13:21,633 INFO util.ShutdownHookManager: Shutdown hook called
```

Be sure to explore the history server thoroughly! You can use it to gain an
understanding of how Spark executes your application, as well as to debug and
time your code, which is important for both lab 1 and 2.
