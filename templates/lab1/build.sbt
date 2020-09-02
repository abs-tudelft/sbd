name := "Spark Scala Application template"

version := "1.0"

scalaVersion := "2.12.12"

val sparkVersion = "2.4.6"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
)
