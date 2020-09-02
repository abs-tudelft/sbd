## Assignment

We will use the GDELT version 2 GKG files. As mentioned, these files are
formatted in tab-separated values. The schema is listed below. The columns that
are most relevant are `DATE` and `AllNames`.

To use the schema for Spark's `DataFrameReader`, first make sure to import the
required Spark SQL types:

```scala
import org.apache.spark.sql.types._
```

The schema can then be specified as a Spark `StructType`, as listed below.

```scala
val schema = StructType(
      Array(
        StructField("GKGRECORDID", StringType),
        StructField("DATE", DateType),
        StructField("SourceCollectionIdentifier", IntegerType),
        StructField("SourceCommonName", StringType),
        StructField("DocumentIdentifier", StringType),
        StructField("Counts", StringType),
        StructField("V2Counts", StringType),
        StructField("Themes", StringType),
        StructField("V2Themes", StringType),
        StructField("Locations",StringType),
        StructField("V2Locations",StringType),
        StructField("Persons",StringType),
        StructField("V2Persons",StringType),
        StructField("Organizations",StringType),
        StructField("V2Organizations",StringType),
        StructField("V2Tone", StringType),
        StructField("Dates",StringType),
        StructField("GCAM", StringType),
        StructField("SharingImage", StringType),
        StructField("RelatedImages",StringType),
        StructField("SocialImageEmbeds",StringType),
        StructField("SocialVideoEmbeds",StringType),
        StructField("Quotations", StringType),
        StructField("AllNames", StringType),
        StructField("Amounts",StringType),
        StructField("TranslationInfo",StringType),
        StructField("Extras", StringType)
      )
```

In the `data/` folder you will also find a script called `get_data` and its
Powershell equivalent `get_data.ps1`, for use on Windows. This script will
download a number of GKG segments to your computer, and generate a file
`local_index.txt` containing the paths to the downloaded files.

```bash
$ ./get_data 4
...
wget downloading
...

$ cat local_index.txt
/path/to/this/repo/lab1/data/segment/20150218230000.gkg.csv
/path/to/this/repo/lab1/data/segment/20150218231500.gkg.csv
/path/to/this/repo/lab1/data/segment/20150218233000.gkg.csv
/path/to/this/repo/lab1/data/segment/20150218234500.gkg.csv
```

The script will put all downloaded files in the `segment` folder. `wget`
adds timestamps to downloads, so files will not be needlessly downloaded again.

You can use these local files for the first lab assignment to test your
application, and build some understanding of the scaling behaviour on a single
machine.

Your program should output a structure that maps dates to a list of 10 tuples
containing the most mentioned topics and the amount of times they were
mentioned. An example output of this system based on 10 segments would be:

```
DateResult(2015-02-19,List((United States,1497), (Islamic State,1233), (New
York,1058), (United Kingdom,735), (White House,723), (Los Angeles,620), (New
Zealand,590), (Associated Press,498), (San Francisco,479), (Practice Wrestling
Room,420)))
DateResult(2015-02-18,List((Islamic State,1787), (United States,1210), (New
York,727), (White House,489), (Los Angeles,424), (Associated Press,385), (New
Zealand,353), (United Kingdom,325), (Jeb Bush,298), (Practice Wrestling
Room,280)))
```

Or in JSON:

```json
{"data":"2015-02-19","result":[{"topic":"United
States","count":1497},{"topic":"Islamic State","count":1233},{"topic":"New
York","count":1058},{"topic":"United Kingdom","count":735},{"topic":"White
House","count":723},{"topic":"Los Angeles","count":620},{"topic":"New
Zealand","count":590},{"topic":"Associated Press","count":498},{"topic":"San
Francisco","count":479},{"topic":"Practice Wrestling Room","count":420}]}
{"data":"2015-02-18","result":[{"topic":"Islamic
State","count":1787},{"topic":"United States","count":1210},{"topic":"New
York","count":727},{"topic":"White House","count":489},{"topic":"Los
Angeles","count":424},{"topic":"Associated Press","count":385},{"topic":"New
Zealand","count":353},{"topic":"United Kingdom","count":325},{"topic":"Jeb
Bush","count":298},{"topic":"Practice Wrestling Room","count":280}]}
```

The exact counts can vary depending on how you count, for instance if a name
is mentioned multiple times per article, do you count it once or multiple
times? Something in between? Do you filter out some names that are false
positives ("ParentCategory" seems to be a particular common one)? You are free
to implement it whatever you think is best, and are encouraged to experiment
with this. Document your choices in your report.
