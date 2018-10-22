ThisBuild / scalaVersion := "2.12.7"
lazy val example = (project in file("."))
  .settings(
    connectInput in run := true,
    fork in run := true,
    outputStrategy := Some(StdoutOutput),
    name := "SBD 2018 - Assignment 3 - Producer",

    libraryDependencies ++= Seq(
        {
        sys.props += "packaging.type" -> "jar"
        "org.apache.kafka" %% "kafka-streams-scala" % "2.0.0"
        },
        "com.amazonaws" % "aws-java-sdk-s3" % "1.11.432"
      )
  )

