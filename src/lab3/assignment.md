## Assignment

The scenario of the first lab has worsened so much that the whole world is about
to get flooded. The people of Waterworld have united under the banner of the
United Mariners (UM) and coordinate their efforts through a number of massive 
ships (called UM motherships), temporarily sheltering those who seek refuge in 
Waterworld from the dangers of the ocean.

To coordinate the UM effort, they have released an app for refugees that have
satellite phones. The app can send Waterworld entry events to notify a number of
refugees from some city have boarded some vessel and are now seeking refuge in
one of the motherships.

The Waterworld entry events enter the application via the `events` topic input
stream and are formatted as [JSON documents], and have the following schema:

| Field       | JSON Type | Description                                          |
| ----------- | --------- | ---------------------------------------------------- |
| `timestamp` | number    | The Unix timestamp of the check-in.                  |
| `city_id`   | number    | A unique ID of the city where the check-in was made. |
| `city_name` | string    | The name of the city where the check-in was made.    |
| `refugees`  | number    | The number of refugees entering the vessel.          |

*Disclaimer:* While technically not required, we include the city name to
facilitate debugging. This goes for some requirements below as well.

The requirements of the Kafka application that you must implement based on this
description are described below in three tiers ("adequate", "good" and "
excellent"). These tiers correspond to one of the grading criteria (explained on
the rubric page). To pass the lab, you must at least achieve the "adequate"
tier. "Good" and "excellent" tier applications contain **additional**
requirements (and may supersede previous requirements where applicable) that the
application needs to adhere to.

### "Adequate" application requirements

1. The application outputs a Kafka stream on the topic `updates` with the total
   number of refugees in Waterworld, whenever an incoming entry event is pulled
   from the `events` input stream. The `update` data is serialized as a JSON
   document with the following schema:
   
   | Field       | JSON Type | Description                   |
   | ----------- | --------- | ----------------------------- |
   | `refugees`  | number    | The total number of refugees. |

- Example:

On the input stream, the following events are received:
```json
{"timestamp":1, "city_id":1, "city_name":"Delft", "refugees":10}
{"timestamp":3, "city_id":1, "city_name":"Delft", "refugees":20}
{"timestamp":4, "city_id":2, "city_name":"Rotterdam", "refugees":30}
```
On the output stream, the following updates are required:
```json
{"refugees":10}
{"refugees":30}
{"refugees":60}
```

### "Good" application requirements

2. Superseding requirement 1, the application outputs the total number of
   refugees in Waterworld ***for each city***. The `update` data is serialized
   as a JSON document with the following schema:

   | Field       | JSON Type | Description                   | 
   | ----------- | --------- | ----------------------------- |
   | `city_id`   | number    | The total number of refugees. |
   | `city_name` | string    | The name of the city.         |
   | `refugees`  | number    | The total number of refugees in the respecitve city. |

- Example:

On the input stream, the following events are received:
```json
{"timestamp":1, "city_id":1, "city_name":"Delft", "refugees":10}
{"timestamp":3, "city_id":1, "city_name":"Delft", "refugees":20}
{"timestamp":4, "city_id":2, "city_name":"Rotterdam", "refugees":30}
```
On the output stream, the following updates are required:
```json
{"city_id":1, "city_name":"Delft", "refugees":10}
{"city_id":1, "city_name":"Delft", "refugees":30}
{"city_id":2, "city_name":"Rotterdam", "refugees":30}
```

### "Excellent" application requirements

3. The applications keeps track of the events within a window of N seconds, and,
   superseding requirement 2, also sends out the change in refugee count within 
   that window.
   
   | Field       | JSON Type | Description                                     | 
   | ----------- | --------- | ----------------------------------------------- |
   | `city_id`   | number    | The total number of refugees.                   |
   | `city_name` | string    | The name of the city.                           |
   | `refugees`  | number    | The total number of refugees in the respective city. |
   | `change`    | number    | The total number of refugees entering Waterworld from the respective city in the last N seconds. |

- Example:

On the input stream, the following events are received:
```json
{"timestamp":1, "city_id":1, "city_name":"Delft",     "refugees":10}
{"timestamp":2, "city_id":1, "city_name":"Delft",     "refugees":20}
{"timestamp":3, "city_id":2, "city_name":"Rotterdam", "refugees":30}
{"timestamp":4, "city_id":1, "city_name":"Delft",     "refugees":40}
{"timestamp":5, "city_id":2, "city_name":"Rotterdam", "refugees":12}
```

When N = 2, on the output stream, the following updates are required:
```json
{"city_id":1, "city_name":"Delft",     "refugees":10, "change": 10}  // At timestamp 1
{"city_id":1, "city_name":"Delft",     "refugees":30, "change": 30}  // At timestamp 2
{"city_id":1, "city_name":"Delft",     "refugees":30, "change": 20}  // At timestamp 3
{"city_id":2, "city_name":"Rotterdam", "refugees":30, "change": 30}  // At timestamp 3
{"city_id":1, "city_name":"Delft",     "refugees":70, "change": 40}  // At timestamp 4
{"city_id":2, "city_name":"Rotterdam", "refugees":42, "change": 12}  // At timestamp 5
{"city_id":1, "city_name":"Delft",     "refugees":70, "change":  0}  // At timestamp 6
{"city_id":2, "city_name":"Rotterdam", "refugees":42, "change":  0}  // At timestamp 7
```

### Supplying the time window

The time window will be supplied on the command-line as the first argument
representing the time window size in seconds.

### General hints and recommended approach

- Streaming frameworks like Kafka usually work with very strong notions of
  **stateless** and **stateful** operations.
    - An example of a **stateless** operation is to filter records of a stream
      based on their content.
    - An example of a **stateful** operation is to update some existing data
      structure based on streamed records.
- Keeping track of total number of evacuees (or that have been included in your
  current window of interest) is **stateful**, and can be done in an abstraction
  called a **state store**.
    - You can operate on state stores whenever a new record arrives using so
      called **stateful transformations** of records.
    - It is recommended to use the [Processor API] for this.
      Check [this documentation] on how to apply processors and transformers.
      Consider the differences between [processors] and [transformers] and pick
      the one that best suits this application.
    - While it is technically possible to use Kafka's Windowing abstractions, it
      is **not recommended**, because for requirement 3 it does not exactly
      match our use-case of sending the change "0" update.
- What is slightly similar to building up DAGs in Spark is what in Kafka is
  called building up the stream Topology.
    - This is also lazily evaluated and only starts doing its thing when you
      call
      `.start()` on a `KafkaStreams`.
    - You can obtain a Topology description for debugging after using e.g.
      `val topology = builder.build()` and then `println(topology.describe())`.
    - If you copy-paste the description [in this tool], you can visualize it.
- You want to convert the JSONs to a Scala case class to be able to process the
  data in a type-safe manner. It is recommended to use one of the options from
  [this repository]. `circe` is a good option that worked for the TA's.

[JSON documents]: https://en.wikipedia.org/wiki/JSON
[Processor API]: https://kafka.apache.org/26/documentation/streams/developer-guide/processor-api.html
[this documentation]: https://kafka.apache.org/26/documentation/streams/developer-guide/dsl-api.html#applying-processors-and-transformers-processor-api-integration
[processors]: https://kafka.apache.org/26/javadoc/org/apache/kafka/streams/kstream/KStream.html#process-org.apache.kafka.streams.processor.ProcessorSupplier-java.lang.String...-
[transformers]: https://kafka.apache.org/26/javadoc/org/apache/kafka/streams/kstream/KStream.html#transform-org.apache.kafka.streams.kstream.TransformerSupplier-java.lang.String...-
[in this tool]: https://zz85.github.io/kafka-streams-viz/
[this repository]: https://github.com/azhur/kafka-serde-scala