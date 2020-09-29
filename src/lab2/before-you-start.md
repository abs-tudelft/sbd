## Before you start

To make sure this assignment is challenging and instructive for all students, we
require all Spark applications to have a level 3 robustness implementation. Please
use the guide below to upgrade your application if needed.

**We assume everybody has access to AWS credits via both the GitHub developer
pack and the AWS classroom in their lab group! If this is not the case, please
ask for help, or send us an email.**

**You pay for cluster per commissioned minute. After you are done working with a
cluster, please terminate the cluster, to avoid unnecessary costs.**

Make sure you have read the introduction on Amazon Web services in the guide
chapter before starting the assignment.

### Level 3 robustness guide

The description here describes how you can upgrade your application to a level 3 robustness implementation (as listed in the rubric of lab 1).

Please be aware that:

- If the method in your application from lab 1 is the same as the one described below, but implemented differently, you are **not required** to modify it.
- If the method in your application from lab 1 is more robust than the one described below, you are **allowed to modify your application to be less robust** if you have scalability or performance issues.

This guide uses relations to construct polygons for city boundaries and then performs [point-in-polygon](https://en.wikipedia.org/wiki/Point_in_polygon) tests to check if a brewery is within a certain city.

1. Read input data set and split into:

- [`nodes`](https://wiki.openstreetmap.org/wiki/Node)
- [`ways`](https://wiki.openstreetmap.org/wiki/Way)
- [`relations`](https://wiki.openstreetmap.org/wiki/Relation)

Make sure you understand the difference between these types and how to dereference IDs in both ways and relations:

- Ways reference nodes in the `nds` field. Typically you [`posexplode`](<https://spark.apache.org/docs/2.4.6/api/scala/index.html#org.apache.spark.sql.functions$@posexplode(e:org.apache.spark.sql.Column):org.apache.spark.sql.Column>) the field with references and then `join` with the `nodes` on ID. Use `posexplode` instead of `explode` because nodes in a way are ordered, and this method allows you to recover that information.

Below is an incomplete example that shows how you could dereference nodes for ways using the Dataframe API. This may look a bit different if you are using the Dataset API.

```scala
val nodes = df
      .filter('type === "node")
      .select(
        'id.as("node_id"),
        ...
      )

val ways = df
      .filter('type === "way")
      .select(
        'id.as("way_id"),
        ...,
        posexplode($"nds.ref")
      )
      .select(
        'way_id,
        ...,
        struct('pos, 'col.as("id")).as("node")
      )
      .join(nodes, $"node.id" === 'node_id)
      .groupBy('way_id)
      .agg(
        array_sort(
          collect_list(
            struct($"node.pos".as("pos"), ...)
          )
        ).as("way")
      )
```

- Relations reference nodes and ways in the `members.ref` field. You can `posexplode` the members field and join with `ways` and `nodes`. You need to `posexplode` and `join` the `ways` again to dereference the nodes that belong to that way. Make sure you understand the data in the `members.role` and `members.type` fields.

To collect the exploded and joined information in single rows you can `groupBy` and `agg` with `collect_list`. For ways you `groupBy` the ID of the way and collect the list of nodes (maintain the order). For relations you first `groupBy` relation ID and way ID to collect the list of nodes belonging to the ways. Then you can `groupBy` relation ID to collect the ways in the relation.

2. Find breweries

Use tag information from the [Brewery wiki](https://wiki.openstreetmap.org/wiki/Brewery) to find coordinates of breweries. Nodes have coordinate information, however ways and relations require dereferencing IDs to get the nodes that make up the way or relation. For breweries you can select any node (coordinate) or average of the coordinates of the nodes that make up the way or relation. This is based on the assumption that breweries (ways and relations) don't cross city boundaries. A more robust implementation would be to recover the geometry and perform contains checks later, but this is not a requirement.

3. Find cities

Use [boundary relations](https://wiki.openstreetmap.org/wiki/Relation:boundary) to construct polygons for city boundaries. The use of third-party libraries for geometry types is encouraged. We recommend [JTS](https://github.com/locationtech/jts). You can use [GeoMesa](https://www.geomesa.org/) [Spark JTS](https://www.geomesa.org/documentation/stable/user/spark/spark_jts.html) for serialization support of geometry types. Hint take a look at the [`Polygonizer`](https://locationtech.github.io/jts/javadoc/org/locationtech/jts/operation/polygonize/Polygonizer.html) class.
You can also use other libraries e.g. [Apache Sedona](https://github.com/apache/incubator-sedona).

4. Check if a brewery is within the boundary of a city

Perform the point-in-polygon check for breweries and cities. Use of the implementation provided your third-party library is strongly recommended.

If you get stuck please contact one of the TAs.
