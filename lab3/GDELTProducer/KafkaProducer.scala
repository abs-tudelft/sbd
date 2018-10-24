import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, ProducerConfig}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerConfig}
import org.apache.kafka.common.{KafkaException, TopicPartition}
import org.apache.kafka.common.serialization.{StringSerializer, StringDeserializer};

import scala.io.{Codec, Source}
import scala.collection.mutable.Queue
import scala.math.max;
import scala.util.{Try, Success, Failure}

import java.util.{Calendar, Properties};
import java.io.File;
import java.nio.charset.{Charset, CodingErrorAction};
import java.util.concurrent.{LinkedBlockingQueue}
import scala.collection.JavaConversions._



class KafkaSupplier(queue: LinkedBlockingQueue[File]) extends Runnable {
  val groupId = "producer_check"
  val server = "localhost:9092"
  val topic = "gdelt"
  val fileUploadInterval = 15
  val pollDelay = 1000
  val consumerProps: Properties = {
    val p = new Properties()
    p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server)
    p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    p.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    p
  }

  val producerProps: Properties = {
    val p = new Properties()
    p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server)
    p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    p
  }
  val stream = new KafkaProducer[String, String](producerProps)

  def findLastKey() : String = {
    val consumer = new KafkaConsumer[String, String](consumerProps)
    consumer.subscribe(List(topic))
    consumer.poll(0) //need heartbeat from zookeeper
    val topicPart = consumer.assignment().toSeq(0)
    consumer.seekToEnd(consumer.assignment())

    val position = consumer.position(topicPart)

    val newPos = position - 1
    if (newPos >= 0) {
      consumer.seek(topicPart, newPos)
      val lastRecord = consumer.poll(pollDelay).records(topic).toSeq.last
      consumer.close()
      lastRecord.key
    } else {
      consumer.close()
      return "20150218230000-0"
    }

  }

  def parseTimeStamp(ts: String) : (Long, Long) = {
    val parse = Try( {
      val Array(a, b) = ts.split("-").map(_.toLong)
      (a, b)
    })
    parse match {
      case Success(a) => a
      case Failure(_) => (0, 0)
    }
  }


  def run () {
    import scala.math.Ordering.Implicits._
    var linesToFeed: Queue[(Long, String)] = Queue()

    try {
      while (true) {
        var files : Array[File] = Array()
        var file : File = queue.poll
        while (file != null) {
          files = files :+ file
          file = queue.poll
        }
        var codec = new Codec(Charset.forName("UTF-8"))
        codec.onMalformedInput(CodingErrorAction.IGNORE)
        linesToFeed ++= files
          .flatMap(a => HelperFunctions.buildTimeStamps(Source.fromFile(a)(codec).getLines.toSeq,
                                                        fileUploadInterval))

        val now = Calendar.getInstance
        val nextQuarter = HelperFunctions.nextMinuteInterval(fileUploadInterval)
        val remainingSecs: Long = max((nextQuarter.getTimeInMillis - now.getTimeInMillis) / 1000, 1)
        val noElems: Int = linesToFeed.length / remainingSecs.toInt

        val (toKafka, b) = linesToFeed.splitAt(noElems)
        linesToFeed = b
        toKafka.map{case (ts, a) => new ProducerRecord(topic, 0, ts, a.split("\t", -1).head, a) }
          .foreach((a) => {
                 stream.send(a)
                 })

        Thread.sleep(1000)
      }
    } catch {
      case iex: InterruptedException => {
        println("Received interrupt...")
      }
      case ex: Throwable => {
        println("Caught an exception with the following stack trace:")
        ex.printStackTrace
      }
    } finally {
      println("Closing Kafka producer.")
      stream.flush()
      stream.close()
    }
  }
}
