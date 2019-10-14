scalaVersion := "2.12.10"

name := "SBD 2018 - Assignment 3 - Consumer"

fork := true

libraryDependencies ++= Seq(
    "org.apache.kafka" %% "kafka-streams-scala" % "2.3.0",
    "javax.servlet" % "javax.servlet-api" % "4.0.1",
    "javax.websocket" % "javax.websocket-api" % "1.1",
    "org.eclipse.jetty" % "jetty-server" % "9.4.12.v20180830",
    "org.eclipse.jetty.websocket" % "javax-websocket-server-impl" % "9.4.12.v20180830",
    "org.eclipse.jetty" % "jetty-servlet" % "9.4.12.v20180830"
)
