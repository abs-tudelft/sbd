## OpenStreetMap

[OpenStreetMap] is a project where you can download free geographic data from
the whole world. Such data will be used as the only data source for our queries
in this lab. The project has an excellent [Wiki] where a lot of information
about the structure of the dataset may be found.

OpenStreetMap data knows three map elements:

- [Nodes] : a single point on the map, e.g. to mark the location of a [brewery].
- [Ways] : an ordered lists of nodes, e.g. to represent (a part of) a [street].
- [Relations] : a group of other elements, e.g. to represent the [boundary] and
  center of a city.

All map elements may have associated [tags] that expose more information about
what they represent. For example, we may look at [some node], and find that it
represents the Delft central station, with the following tags:

| Key              | Value            |
| ---------------- | ---------------- |
| name             | Delft            |
| public_transport | station          |
| railway          | station          |
| railway:ref      | Dt               |
| train            | yes              |
| wheelchair       | yes              |
| wikidata         | Q800653          |
| wikipedia        | nl:Station Delft |

Many tags have an explanation on the Wiki, for example the [wheelchair] tag,
where its value describes an indication of the level of accessibility for people
in wheelchairs to, in this case, the station.

Feel free to browse around the wiki to discover other tags. We can spoil that
[this] page contains some useful tags that you may want to use for your lab
assignments.

### Preparing the dataset for Lab 1

We will now explain how to prepare our dataset for lab 1. Because the query that
we are interested in is too large to process during our initial short
development iterations of implementing and debugging, its useful to start off
with a small subset of the data (in lab 1), until your implementation is stable
enough to be moved to a large cluster (in lab 2), to process the whole thing!

Furthermore, to be able to leverage SparkSQL, we must convert the OpenStreetMap
data to a tabular format. We will use the ORC format for this. Follow the steps
below to end up with an ORC file for lab 1.

Luckily, some people have already chopped up the whole world into manageable
pieces. In our case, we will start off with just the province of Zuid-Holland,
where Delft is located. Also, some people have written [a conversion tool] for
us already, that helps us convert the `.osm.pbf` file into an `.orc` file.

- Download the [province of Zuid-Holland]. You need to get the `.osm.pbf` file.

- Download and extract v.0.5.5 of [osm2orc] (click the link to download
  immediately).

- Run osm2orc to obtain the ORC file of Zuid-Holland. osm2orc can be found in
  the `bin/` folder of the tool. Example usage in Linux:

```console
./osm2orc /path/to/zuid-holland-latest.osm.pbf /destination/for/zuid-holland.orc
```

You can also use the Dockerfile provided in the lab 1 repository to build an image
that includes this tool:

- First build the `osm2orc` image (from the root of the lab 1 repository):

```bash
docker build --target osm2orc -t osm2orc .
```

- Run the conversion tool in a container. Make sure the source `.osm.pbf` file is
  downloaded in your current working directory.

```bash
docker run -it --rm -v "`pwd`":/io osm2orc /io/zuid-holland-latest.osm.pbf /io/zuid-holland.orc
```

You will now have the `zuid-holland.orc` file somewhere on your machine. We will
use this file as our input. Make sure you understand [how to load the file into
Spark].

### OpenStreetMap schema

Once you have loaded the data set, it's possible to print out the schema of the
data, for example, on the Spark shell. Note that this assumes you have loaded
the data set as a Spark DataFrame called `df`.

```
scala> df.printSchema()
root
 |-- id: long (nullable = true)
 |-- type: string (nullable = true)
 |-- tags: map (nullable = true)
 |    |-- key: string
 |    |-- value: string (valueContainsNull = true)
 |-- lat: decimal(9,7) (nullable = true)
 |-- lon: decimal(10,7) (nullable = true)
 |-- nds: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- ref: long (nullable = true)
 |-- members: array (nullable = true)
 |    |-- element: struct (containsNull = true)
 |    |    |-- type: string (nullable = true)
 |    |    |-- ref: long (nullable = true)
 |    |    |-- role: string (nullable = true)
 |-- changeset: long (nullable = true)
 |-- timestamp: timestamp (nullable = true)
 |-- uid: long (nullable = true)
 |-- user: string (nullable = true)
 |-- version: long (nullable = true)
 |-- visible: boolean (nullable = true)
```

And we may look at a tiny part of the data, say the first three rows:

```
scala> df.show(3)
+-------+----+----+----------+---------+---+-------+---------+-------------------+---+----+-------+-------+
|     id|type|tags|       lat|      lon|nds|members|changeset|          timestamp|uid|user|version|visible|
+-------+----+----+----------+---------+---+-------+---------+-------------------+---+----+-------+-------+
|2383199|node|  []|52.0972000|4.2997000| []|     []|        0|2011-10-31 12:45:53|  0|    |      3|   true|
|2383215|node|  []|52.0990543|4.3002070| []|     []|        0|2020-01-08 20:50:29|  0|    |      6|   true|
|2716646|node|  []|52.0942524|4.3354580| []|     []|        0|2011-12-26 23:12:28|  0|    |      3|   true|
+-------+----+----+----------+---------+---+-------+---------+-------------------+---+----+-------+-------+
```

Here we can observe that the first three rows are elements of the `"node"` type.
Note that there are two other types of elements, as explained previously. We
also have the `"way"` and `"relation"`. There are all flattened into this single
table by the conversion tool.

How big is this dataframe?

```
scala> df.count()
res1: Long = 18426861
```

It has over 18 million rows already, and this is just the province of
Zuid-Holland!

[openstreetmap]: (https://www.openstreetmap.org)
[wiki]: https://wiki.openstreetmap.org/wiki/Main_Page
[ways]: https://wiki.openstreetmap.org/wiki/Way
[nodes]: https://wiki.openstreetmap.org/wiki/Node
[relations]: https://wiki.openstreetmap.org/wiki/Relation
[tags]: https://wiki.openstreetmap.org/wiki/Tags
[boundary]: https://www.openstreetmap.org/relation/47798
[some node]: https://www.openstreetmap.org/node/3376839743
[wheelchair]: https://wiki.openstreetmap.org/wiki/Key:wheelchair
[this]: https://wiki.openstreetmap.org/wiki/Brewery
[street]: https://www.openstreetmap.org/way/7624546
[brewery]: https://www.openstreetmap.org/node/4829046021
[province of zuid-holland]: https://download.geofabrik.de/europe/netherlands.html
[a conversion tool]: https://github.com/mojodna/osm2orc
[osm2orc]: https://github.com/mojodna/osm2orc/releases/download/v0.5.5/osm2orc-0.5.5.tar.gz
[how to load the file into spark]: http://spark.apache.org/docs/2.4.6/sql-data-sources-load-save-functions.html
