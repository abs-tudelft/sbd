ThisBuild / scalaVersion := "2.12.7"

lazy val example = (project in file("."))
  .settings(
    name := "SBD 2018 - Assignment 3 - Stream",
    fork in run := true,
    outputStrategy := Some(StdoutOutput),
    connectInput in run := true,

    libraryDependencies ++= Seq(
        {
        sys.props += "packaging.type" -> "jar"
        "org.apache.kafka" %% "kafka-streams-scala" % "2.0.0"
        },
    )
  )

