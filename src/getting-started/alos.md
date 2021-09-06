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
<schema goes here>
```

To produce a Parquet file from the data, first install the tool or build it from
source. Follow the instructions of the README of the [aw3d30-parquet]
repository.

Once you've installed the tool, you can run it as follows:

```
<how to run the tool goes here>
```

[ALOS Global Digital Surface Model]: https://www.eorc.jaxa.jp/ALOS/en/aw3d30/index.htm

[aw3d30-parquet]: https://github.com/mbrobbel/aw3d30-parquet