import java.util.concurrent.{
  LinkedBlockingQueue,
  ScheduledThreadPoolExecutor,
  TimeUnit
};
import java.io.{File, BufferedOutputStream, DataOutputStream, FileOutputStream};
import java.net.URL
import java.text.{DecimalFormat, SimpleDateFormat};
import java.time.Duration;
import java.util.{Calendar, TimeZone};

import scala.sys.process._

case class Download(
  process: Process,
  outputStream: DataOutputStream,
  fileSize: Long,
)

object GDELTProducer {

  val interval = 15
  val baseUrl = "http://data.gdeltproject.org/gdeltv2/"
  val extension = ".gkg.csv.zip"

  val timeFormat = new SimpleDateFormat("HH:mm")
  val timeStampFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
  var fileNameFormat = new SimpleDateFormat("yyyyMMddHHmm00")
  var lastFile: String = "20150218230000"
  fileNameFormat.setTimeZone(TimeZone.getTimeZone("UTC"))

  def getFileList(window: Int): Array[String] = {
    var time = Calendar.getInstance
    time.set(Calendar.SECOND, 0)
    time.set(Calendar.MILLISECOND, 0)

    val minutes = time.get(Calendar.MINUTE) % interval
    time.add(Calendar.MINUTE, -(interval + minutes))

    println("Checking for downloads up to " + timeFormat.format(time.getTime()))
    val lastSegment = time.clone().asInstanceOf[Calendar]

    time.add(Calendar.MINUTE, interval - window)
    var segment = time.clone().asInstanceOf[Calendar]

    var files: Array[String] = Array()

    while (segment.compareTo(lastSegment) <= 0) {
      val filename = fileNameFormat.format(segment.getTime()) + extension
      files = files :+ filename
      segment.add(Calendar.MINUTE, interval)
    }

    return files
  }

  def getDownloads(
      fileNames: Array[String],
      localDir: String,
      fileQueue: LinkedBlockingQueue[File]
  ): (Array[File], Array[Download]) = {
    var downloads: Array[Download] = Array()
    var files: Array[File] = Array()

    new File(localDir).mkdirs()

    for (fileName <- fileNames) {
      val localFile = new File(localDir + fileName)
      files = files :+ localFile
      if (!localFile.exists) {
        val url = new URL(baseUrl + fileName)
        val conn = url.openConnection()
        val length = conn.getContentLengthLong()
        val out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(localFile)))

        val download = Download(
          (url #> out).run(),
          out,
          length
        )

        downloads = downloads :+ download
      } else {
        if (lastFile < localFile.getName) {
          fileQueue.offer(localFile)
          lastFile = localFile.getName
        }
      }
    }

    return (files, downloads)
  }

  def blockOnDownloads(
      files: Array[File],
      downloads: Array[Download],
      fileQueue: LinkedBlockingQueue[File]
  ) {
    if (downloads.size > 0) {
      val msg = "Downloading " + downloads.size.toString + " file(s)... "
      print(msg)

      val df = new DecimalFormat("##0.0")
      for (download <- downloads) {
        while (download.process.isAlive) {
          var progress = 0d
          for (download2 <- downloads) {
            progress += download2.outputStream.size() / download2.fileSize.toDouble
          }
          print("\r" + msg + df.format(progress / downloads.size * 100) + "%")
          Thread.sleep(100)
        }
      }
      for (file <- files) {
        if (lastFile < file.getName) {
          fileQueue.offer(file)
          lastFile = file.getName
        }
      }
      print("\r" + msg + "Done. \n")
    } else {
      println("No downloads needed!")
    }
  }

  def download(
      window: Int,
      localDir: String,
      fileQueue: LinkedBlockingQueue[File]
  ) {
    val fileNames = getFileList(window)
    val (files, downloads) = getDownloads(fileNames, localDir, fileQueue)
    blockOnDownloads(files, downloads, fileQueue)
  }

  def main(args: Array[String]): Unit = {
    val window = 60
    val localDir = "segment/"
    val kafkaServer = args(0)

    val fileQueue: LinkedBlockingQueue[File] = new LinkedBlockingQueue()
    val kafka = new KafkaSupplier(kafkaServer, fileQueue)
    val sup = new Thread(kafka)
    println("Starting Kafka producer.")
    sup.start

    val downloadSched = new java.util.concurrent.ScheduledThreadPoolExecutor(1)
    val downloadTask = new Runnable {
      def run() = {
        download(window, localDir, fileQueue)
      }
    }

    println("Performing the initial downloads now.\n")
    download(window, localDir, fileQueue)

    val now = Calendar.getInstance
    val nextInterval = HelperFunctions.nextMinuteInterval(interval)
    val delay = nextInterval.getTimeInMillis() - now
      .getTimeInMillis() + Duration.ofSeconds(5).toMillis()

    println(
      "\nFinished initial download. Scheduling the next download at " + timeFormat.format(
        nextInterval.getTime
      ) + " and every following " + interval.toString + " minutes..."
    )
    // println("Press enter at any time to cancel...\n")
    downloadSched.scheduleAtFixedRate(
      downloadTask,
      delay,
      interval * 60 * 1000,
      java.util.concurrent.TimeUnit.MILLISECONDS
    )

    sys.ShutdownHookThread {
      downloadSched.shutdownNow
      sup.interrupt()
    }
  }
}
