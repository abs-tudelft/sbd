scalaVersion := "2.12.10"

name := "SBD 2018 - Assignment 3 - Producer"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka-streams-scala" % "2.3.0",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.648"
)
