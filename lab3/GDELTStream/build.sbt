scalaVersion := "2.12.10"

name := "SBD 2018 - Assignment 3 - Stream"

fork := true

libraryDependencies ++= Seq(
    "org.apache.kafka" %% "kafka-streams-scala" % "2.3.0"
)

