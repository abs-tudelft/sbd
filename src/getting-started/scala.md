## Scala

Apache Spark, our big data framework of choice for this lab, is implemented in
Scala, a compiled language on the JVM that supports a mix between functional
and object-oriented programming. It is compatible with Java libraries. Some
reasons why Spark was written in Scala are:

1.  Compiling to the JVM makes the codebase extremely portable and deploying
    applications as easy as sending the Java bytecode (typically packaged in a
    **J**ava **AR**chive format, or JAR). This simplifies deploying to cloud
    provider big data platforms as we don't need specific knowledge of the
    operating system, or even the underlying architecture.
2.  Compared to Java, Scala has some advantages in supporting more complex
    types, type inference, and anonymous functions (Since Java 8, Java also supports anonymous functions, or
    lambda expression, but this version wasn't released at the time of Spark's
    initial release.). Matei
    Zaharia, Apache Spark's original author, has said the following about why
    Spark was implemented in Scala in a [Reddit AMA](https://www.reddit.com/r/IAmA/comments/31bkue/im_matei_zaharia_creator_of_spark_and_cto_at/):

    > At the time we started, I really wanted a PL that supports a
    > language-integrated interface (where people write functions inline, etc),
    > because I thought that was the way people would want to program these
    > applications after seeing research systems that had it (specifically
    > Microsoft's DryadLINQ). However, I also wanted to be on the JVM in order to
    > easily interact with the Hadoop filesystem and data formats for that. Scala
    > was the only somewhat popular JVM language then that offered this kind of
    > functional syntax and was also statically typed (letting us have some control
    > over performance), so we chose that. Today there might be an argument to make
    > the first version of the API in Java with Java 8, but we also benefitted from
    > other aspects of Scala in Spark, like type inference, pattern matching, actor
    > libraries, etc.

Apache Spark provides interfaces to Scala, R, Java and Python, but we will be
using Scala to program in this lab. An introduction to Scala can be found on
the [Scala language site](https://docs.scala-lang.org/tour/tour-of-scala.html).
You can have a brief look at it, but you can also pick up topics as you go through the lab.
