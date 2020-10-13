## Assignment

Once you have created your lab 3 repository on GitHub Classroom, your job is to
implement the Transformer component in the following file:
`transformer/src/main/scala/Transformer.scala`.

The Transformer application this year will also be a beer-themed assignment. The
producer produces a stream of events that represent "check-ins" of a specific
drink in a specific city (inspired by [Untappd](https://untappd.com/)).

Each check-in is supplied as a [JSON](https://www.json.org/json-en.html) object
on the stream. Check-ins contain the following fields:

| Field       | JSON Type   | Description |
|-------------|-------------|-------------|
| `timestamp` | number      | The Unix timestamp of the check-in. |
| `city_id`   | number      | The ID of the city where the check-in was made. |
| `city_name` | string      | The name of the city where the check-in was made. |
| `style`     | string      | The drink style. |

Suppose we are beer connoisseurs and we are interested to learn where a lot of
check-in activity is coming from. Usually this means there is a beer festival
that we want to visit, and perhaps it is in our city or a city close-by. We do
want to make sure that there is plenty of beer with styles to our preference. So
we can filter out or include some of the more [adventurous
styles](https://en.wikipedia.org/wiki/Gueuze)*.

Your task is to filter check-ins by beer style, and count the number of
check-ins per city within a time window, and produce streaming updates for the
real-time world map of the web interface.

### Supplying beer styles
Beer styles to include are supplied in a file called `beer.styles`.
Beer styles are separated by newline. For example:
```
Amber ale
Stout
Pilsener/Pilsner/Pils 
```
Beer styles in this file match exactly with the names of the producer
implementation found in: `producer/src/main.rs`.

### Supplying the time window
The time window will be supplied on the command-line as the first argument
representing the time window size in seconds.

### Producing updates

The updates are to be sent over a Kafka stream with a key-value-pair `<String,
String>`, where the key is the ID of the city where the check-in was made, and the
value is the number of check-ins within our window.

For example, if we receive the following JSONs on the `events` stream:

```json
{"timestamp":1, "city_id":1, "city_name":"Delft", "style":"Brown ale"}
{"timestamp":2, "city_id":1, "city_name":"Delft", "style":"Bitter"}
{"timestamp":3, "city_id":1, "city_name":"Delft", "style":"Weizenbock"}
{"timestamp":4, "city_id":2, "city_name":"Rotterdam", "style":"Schwarzbier"}
```

And if we would set our recent window to a 3 milliseconds range, we
need to produce the following records on the 'updates' stream:

```C++
 K    V
"1", "1"  // t=1 ms, update Delft with the new recent check-in 
"1", "2"  // t=2 ms, update Delft with the new recent check-in
"1", "3"  // t=3 ms, update Delft with the new recent check-in
"1", "2"  // t=4 ms, update Delft, the oldest check-in went out of the window
"2", "1"  // t=4 ms, update Rotterdam to have 1 check-in recently
"1", "1"  // t=5 ms, update Delft to remove old check-ins
"1", "0"  // t=6 ms, update Delft to remove old check-ins
"2", "0"  // t=7 ms, update Rotterdam to remove old check-ins
```

### General hints and recommended approach

* Streaming frameworks like Kafka usually work with very strong notions of
  **stateless** and **stateful** operations.
  * An example of a **stateless** operation is to filter records of a stream based
    on their content.
  * An example of a **stateful** operation is to update some existing data 
    structure based on streamed records.
* Keeping track of check-ins that have been included in your current window of
  interest is **stateful**, and can be done in an abstraction called a **state
  store**.
  * You can operate on state stores whenever a new record arrives using so called
    **stateful transformations** of records.
  * It is recommended to use the 
    [Processor API](https://kafka.apache.org/26/documentation/streams/developer-guide/processor-api.html)
    for this.  
  * While it is technically possible to use Kafka's Windowing abstractions, it 
    is **not recommended**, because it does not exactly match our use-case.
* What is a little bit similar to building up DAGs in Spark is what in Kafka is
  called building up the stream Topology. 
  *  This is also lazily evaluated and only starts doing its thing when you call
    `.start()` on a `KafkaStreams`. 
  * You can obtain a Topology description for debugging after using e.g. 
    `val topology = builder.build()` and then `println(topology.describe())`.
  * If you copy-paste the description
    [in this tool](https://zz85.github.io/kafka-streams-viz/),
    you can visualize it.
* You probably want to convert the JSONs to a Scala case class to be able to
  process the data in a type-safe manner. It is recommended to use one of the
  options from [this repository](https://github.com/azhur/kafka-serde-scala).
  `circe` is a good option that worked for the TA's. 

### Notes
\* For this particular style, "wild" yeasts are used. Folklore says pigeons
sitting over the water reservoirs at breweries used to provide such yeasts
through their droppings!