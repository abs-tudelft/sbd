### Packaging your application using SBT

We showed how to run Spark in interactive mode. Now we will explain how to
build applications that can be submitted using the `spark-submit` command.

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

```
name := "Example"

scalaVersion := "2.12.12"
```

This specifies the Scala version of the project (2.12.12) and the name of the
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

Start a `scala-sbt` container in the root folder (the one where `build.sbt` is located). This puts
you in interactive mode of SBT. We can compile the sources by writing the
`compile` command.

```
$ docker run -it --rm -v "`pwd`":/root scala-sbt sbt
Getting org.scala-sbt sbt 1.2.8  (this may take some time)...
...
[info] Loading settings for project root from build.sbt ...
[info] Set current project to Example (in build file:/root/)
[info] sbt server started at local:///root/.sbt/1.0/server/27dc1aa3fdf4049b492d/sock
sbt:Example> compile
...
[info] Done compiling.
[success] Total time: 0 s, completed Sep 8, 2019 2:17:14 PM
```

We can try to run the application by typing `run`.

```
sbt:Example> run
[info] Running example.Example
Hello world!
[success] Total time: 1 s, completed Sep 8, 2019 2:18:18 PM
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
[info] Compiling 1 Scala source to ...
[info] Done compiling.
[info] Packaging ...
[info] Done packaging.
[info] Running example.Example
Hello world!
(a,2)
[success] Total time: 1 s, completed Sep 8, 2019 2:19:03 PM
1. Waiting for source changes in project hello... (press enter to interrupt)
```

We can also open an interactive session using SBT.

```
sbt:Example> console
[info] Starting scala interpreter...
Welcome to Scala 2.11.12 (OpenJDK 64-Bit Server VM, Java 1.8.0_222).
Type in expressions for evaluation. Or try :help.

scala> example.Example.addOne('a', 1)
res1: (Char, Int) = (a,2)

scala> println("Interactive environment")
Interactive environment
```

To build Spark applications with SBT we need to include dependencies (Spark
most notably) to build the project. Modify your `build.sbt` file like so

```
name := "Example"

scalaVersion := "2.12.12"

val sparkVersion = "2.4.6"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)
```

We can now use Spark in the script. Modify `example.scala`.

```{.scala}
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
                  .option("timestampFormat", "MM/dd/yy:hh:mm")
                  .csv("./sensordata.csv")
                  .as[SensorData]

    val dsFilter = ds.filter(a => a.timestamp ==
        new Timestamp(2014 - 1900, 2, 10, 1, 1, 0, 0))

    dsFilter.collect.foreach(println)

    spark.stop
  }
}
```

You can build a JAR using the `package` command in SBT. This JAR will be
located in the `target/scala-version/project_name_version.jar`.

You can run the JAR via a `spark-submit` container (which will run on local
mode). By mounting the `spark-events` directory the event log of the
application run is stored to be inspected later using the Spark history server.

```
$ docker run -it --rm -v "`pwd`":/io -v "`pwd`"/spark-events:/spark-events
    spark-submit target/scala-2.11/example_2.11-0.1.0.jar
INFO:...
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
INFO:...
```

By default, Spark's logging is quite assertive. You can change the [log levels
to warn](https://stackoverflow.com/questions/27781187/how-to-stop-info-messages-displaying-on-spark-console)
to reduce the output.

For development purposes you can also try running the application from SBT
using the `run` command. You might run into some trouble with threads here,
which can be solved by running the application in a forked process, which can be
enabled by setting `fork in run := true` in `build.sbt`. You will also have to
set to change the log levels programmatically, if desired.

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
$ docker run -it --rm -v "`pwd`"/spark-events/:/spark-events -p 18080:18080
    spark-history-server
starting org.apache.spark.deploy.history.HistoryServer, logging to
/spark/logs/spark--org.apache.spark.deploy.history.HistoryServer-1-d5dfa4949b86.out
Spark Command: /usr/local/openjdk-8/bin/java -cp /spark/conf/:/spark/jars/*
  -Xmx1g org.apache.spark.deploy.history.HistoryServer
========================================
Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
19/09/08 14:25:33 INFO HistoryServer: Started daemon with process name:
  14@d5dfa4949b86
19/09/08 14:25:33 INFO SignalUtils: Registered signal handler for TERM
19/09/08 14:25:33 INFO SignalUtils: Registered signal handler for HUP
19/09/08 14:25:33 INFO SignalUtils: Registered signal handler for INT
19/09/08 14:25:34 WARN NativeCodeLoader: Unable to load native-hadoop library
  for your platform... using builtin-java classes where applicable
19/09/08 14:25:34 INFO SecurityManager: Changing view acls to: root
19/09/08 14:25:34 INFO SecurityManager: Changing modify acls to: root
19/09/08 14:25:34 INFO SecurityManager: Changing view acls groups to:
19/09/08 14:25:34 INFO SecurityManager: Changing modify acls groups to:
19/09/08 14:25:34 INFO SecurityManager: SecurityManager: authentication
  disabled; ui acls disabled; users  with view permissions: Set(root); groups
     with view permissions: Set(); users  with modify permissions: Set(root);
      groups with modify permissions: Set()
19/09/08 14:25:34 INFO FsHistoryProvider: History server ui acls disabled;
  users with admin permissions: ; groups with admin permissions
19/09/08 14:25:35 INFO FsHistoryProvider:
  Parsing file:/spark-events/local-1567952519905 for listing data...
19/09/08 14:25:35 INFO Utils: Successfully started service on port 18080.
19/09/08 14:25:35 INFO HistoryServer: Bound HistoryServer to 0.0.0.0,
  and started at http://d5dfa4949b86:18080
19/09/08 14:25:36 INFO FsHistoryProvider: Finished parsing
  file:/spark-events/local-1567952519905
```

Navigate to [http://localhost:18080](localhost:18080) to view detailed
information about your jobs.
After analysis you can shutdown the Spark history server using ctrl+C.

```
$ ^C
19/09/08 14:27:18 ERROR HistoryServer: RECEIVED SIGNAL INT
19/09/08 14:27:18 INFO ShutdownHookManager: Shutdown hook called
```

Be sure to explore the history server thoroughly! You can use it to gain an
understanding of how Spark executes your application, as well as to debug and
time your code, which is important for both lab 1 and 2.
