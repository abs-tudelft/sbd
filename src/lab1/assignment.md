## Assignment

Your task is to write a Spark application that reads the OpenStreetMap `.orc`
dataset the and outputs a sorted `.orc` file with the following schema:

```scala
import org.apache.spark.sql.types._

val schema = StructType(
      Array(
        StructField("city", StringType),
        StructField("breweries", IntegerType)
      )
    )
```

where `city` is the name of a city, and `breweries` is the number of breweries
in this city. For your report please mention the top 10 cities (in terms of
brewery count).

We will use the [Zuid-Holland](https://download.geofabrik.de/europe/netherlands/zuid-holland-latest.osm.pbf)
subset provided by [Geofabrik](geofabrik.de/). The `.osm.pbf` files can be
converted to `.orc` files using the tool mentioned in the introduction.

Please note that using other datasets for this assignment is **not** allowed.

Try to keep the following in mind when building your application:

- Functionality: providing a correct answer to this query is not trivial, attempt
  to make your application as robust as possible.
- Scalable: your application should keep functioning correctly when the size of
  the input data set grows larger.
