import scala.math.{ceil, floor}
import java.util.{Calendar, TimeZone}
import java.time.Duration


object HelperFunctions {
  def nextMinuteInterval(interval: Long): Calendar = {
    var time = Calendar.getInstance
    nextTimeIntervalFromTime(time, interval)
  }

  def nextTimeIntervalFromTime(currentTime: Calendar, interval: Long) = {
    val time: Calendar = currentTime.clone().asInstanceOf[Calendar]
    val next = ceil((time.get(Calendar.MINUTE) + 1) / interval.toDouble) * interval
    val roundedMinutes = (next.toInt % 60)
    val addHour = floor(next.toInt / 60).toInt
    time.set(Calendar.HOUR, time.get(Calendar.HOUR) + addHour)
    time.set(Calendar.MINUTE, roundedMinutes)
    time.set(Calendar.SECOND, 0)
    time.set(Calendar.MILLISECOND, 0)
    time
  }
  
  def buildTimeStamps(lines: Seq[String], interval: Long) = {
    val timeStamp = lines.head.split("\t", -1).head
    val year = timeStamp.substring(0,4).toInt
    val month = timeStamp.substring(4,6).toInt
    val day = timeStamp.substring(6,8).toInt
    val hour = timeStamp.substring(8,10).toInt
    val minute = timeStamp.substring(10,12).toInt
    val start = new Calendar.Builder()
                        .setTimeZone(TimeZone.getTimeZone("UTC"))
                        .setDate(year, month-1, day)
                        .setTimeOfDay(hour, minute, 0)
                        .build()
    val noRecords = lines.length 
    val numberOfMillis = Duration.ofMinutes(interval).toMillis
    val incrementPerRecord: Long = numberOfMillis / noRecords

    lazy val timeStamps: Stream[Long] = {
      def f(a: Long): Stream[Long] = a #:: f(a + incrementPerRecord)
      f(start.getTimeInMillis)
    }

    timeStamps.zip(lines)
  }
}

