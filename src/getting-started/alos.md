## [ALOS Global Digital Surface Model]

This dataset contains elevation information about the whole surface of the
earth. The data set consist of a bunch of files that are formatted using the
GeoTIFF format. The data is split up over tiles, where each tile covers a unit
area of 1 degree latitude and longitude of the earths surface.

Because Spark does not know how to work with such files out of the box, we
provide you with the [aw3d30-parquet] tool that helps us pre-process this data.
The tool converts the GeoTIFF data to an Apache Parquet file. Apache Parquet
files hold tabular datastructures. This makes it easier to work with when using
various Spark APIs.

```
message schema {
  REQUIRED DOUBLE lat;
  REQUIRED DOUBLE lon;
  REQUIRED INT32 elevation;
}
```

Coordinates are in the [WGS84] reference schema and elevation is in meters.

To produce a Parquet file from the data, first build a Docker image or build and install the tool from [source](https://github.com/mbrobbel/aw3d30-parquet).

```
docker build -t aw3d30 https://github.com/mbrobbel/aw3d30-parquet.git#main
```

Once you've build the image, you can run it as follows:

```
docker run -it --rm -v `pwd`:/io aw3d30 -t /io/tif -p /io/parquet <set>
```

Or when built from source:

```
aw3d30-parquet <set>
```

Where `<set>` is one of `netherlands`, `europe` or `world`.

[ALOS Global Digital Surface Model]: https://www.eorc.jaxa.jp/ALOS/en/aw3d30/index.htm
[aw3d30-parquet]: https://github.com/mbrobbel/aw3d30-parquet
[WGS84]: https://en.wikipedia.org/wiki/World_Geodetic_System#WGS84
