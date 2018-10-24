package lab3

import java.util.Properties
import java.util.concurrent.{TimeUnit, LinkedBlockingQueue}

import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.processor._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.state._
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsConfig, Topology}


object GDELTConsumer extends App {
  import Serdes._

  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "gdelt-consumer")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put("auto.offset.reset", "latest")
    p
  }

  val builder: StreamsBuilder = new StreamsBuilder
  val histogramStoreBuilder = Stores.keyValueStoreBuilder(
    Stores.inMemoryKeyValueStore("ReducedHistogramStore"),
    Serdes.String,
    Serdes.Long
  )
  .withLoggingDisabled
  builder.addStateStore(histogramStoreBuilder)
  val counts: KStream[String, Long] = builder.stream[String, Long]("gdelt-histogram")
  counts.process(() => new HistogramProcessor, "ReducedHistogramStore")

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
  streams.cleanUp()
  streams.start()

  val serverThread = new Thread(new WebSocketServer)
  serverThread.start()

  println("Server running.")

  sys.ShutdownHookThread {
    println("Closing streams and stopping server.")
    streams.close(10, TimeUnit.SECONDS)
    serverThread.interrupt()
  }

  System.in.read()
  System.exit(0)
}

class HistogramProcessor extends Processor[String, Long] {
  var context: ProcessorContext = _
  var reduced: KeyValueStore[String, Long] = _

  def init(context: ProcessorContext) {
    this.context = context
    this.reduced = context.getStateStore("ReducedHistogramStore").asInstanceOf[KeyValueStore[String, Long]]
  }

  def process(name: String, count: Long) {
    WebSocket.messenger.messageQueue.synchronized {
      val existing = Option(this.reduced.get(name))

      if (existing == None) {
        if (this.reduced.approximateNumEntries >= 100) {
          val minimum = this.getMinimum
          if (count > minimum.value) {
            // Replace current lowest value with new record
            this.deleteReducedHist(minimum.key)
            this.putReducedHist(name, count)
          }
        }
        else {
          // Add new record because buffer is not full yet
          this.putReducedHist(name, count)
        }
      }
      else {
        // Update existing record
        this.putReducedHist(name, count)
      }
    }
  }

  private def putReducedHist(name: String, count: Long) {
    this.reduced.put(name, count)
    WebSocket.messenger.messageQueue.put(name, count)
    WebSocket.messenger.histogram.put(name, count)
  }

  private def deleteReducedHist(name: String) {
    this.reduced.delete(name)
    WebSocket.messenger.messageQueue.put(name, 0L)
    WebSocket.messenger.histogram.put(name, 0L)
  }

  private def getMinimum(): KeyValue[String, Long] = {
    var minimum = new KeyValue("", Long.MaxValue)

    val iter = this.reduced.all
    while (iter.hasNext) {
      val entry = iter.next()
      if (entry.value < minimum.value) {
        minimum = entry
      }
    }
    iter.close
    minimum
  }

  def close() {

  }
}

