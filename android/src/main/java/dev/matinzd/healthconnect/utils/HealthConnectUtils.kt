package dev.matinzd.healthconnect.utils

import androidx.health.connect.client.records.*
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.records.metadata.Device
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.*
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.records.*
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

fun <T : Record> convertReactRequestOptionsFromJS(
  recordType: KClass<T>, options: ReadableMap
): ReadRecordsRequest<T> {
  // Move to refined version
  // inline fun <reified T : Record> ReadRecordsRequest()
  // link: https://android-review.googlesource.com/#/q/If58a5c2c9acea1c22b322537daa4fa513065e393
  return ReadRecordsRequest(
    recordType,
    timeRangeFilter = options.getTimeRangeFilter("timeRangeFilter"),
    dataOriginFilter = convertJsToDataOriginSet(options.getArray("dataOriginFilter")),
    ascendingOrder = options.getSafeBoolean("ascendingOrder", true),
    pageSize = options.getSafeInt("pageSize", 1000),
    pageToken = if (options.hasKey("pageToken")) options.getString("pageToken") else null,
  )
}

fun convertDataOriginsToJsArray(dataOrigin: Set<DataOrigin>): WritableNativeArray {
  return WritableNativeArray().apply {
    dataOrigin.forEach {
      pushString(it.packageName)
    }
  }
}

fun convertJsToDataOriginSet(readableArray: ReadableArray?): Set<DataOrigin> {
  if (readableArray == null) {
    return emptySet()
  }

  return readableArray.toArrayList().mapNotNull { DataOrigin(it.toString()) }.toSet()
}

fun convertJsToRecordTypeSet(readableArray: ReadableArray?): Set<KClass<out Record>> {
  if (readableArray == null) {
    return emptySet()
  }

  return readableArray.toArrayList().mapNotNull { reactRecordTypeToClassMap[it.toString()] }.toSet()
}

fun ReadableArray.toMapList(): List<ReadableMap> {
  val list = mutableListOf<ReadableMap>()
  for (i in 0 until size()) {
    list.add(getMap(i))
  }
  return list
}

fun ReadableMap.getSafeInt(key: String, default: Int): Int {
  return if (this.hasKey(key)) this.getInt(key) else default
}

fun ReadableMap.getSafeBoolean(key: String, default: Boolean): Boolean {
  return if (this.hasKey(key)) this.getBoolean(key) else default
}

fun ReadableMap.getSafeString(key: String, default: String): String {
  return if (this.hasKey(key)) this.getString(key) ?: default else default
}

fun ReadableMap.getSafeDouble(key: String, default: Double): Double {
  return if (this.hasKey(key)) this.getDouble(key) else default
}

fun ReadableMap.getTimeRangeFilter(key: String? = null): TimeRangeFilter {
  val timeRangeFilter = if (key != null) this.getMap(key)
    ?: throw Exception("Time range filter should be provided") else this

  val operator = timeRangeFilter.getString("operator")

  val startTime =
    if (timeRangeFilter.hasKey("startTime")) Instant.parse(timeRangeFilter.getString("startTime")) else null

  val endTime =
    if (timeRangeFilter.hasKey("endTime"))  Instant.parse(timeRangeFilter.getString("endTime")) else null

  when (operator) {
    "between" -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
    "after" -> {
      if (startTime == null) {
        throw Exception("Start time should be provided")
      }

      return TimeRangeFilter.after(startTime)
    }
    "before" -> {
      if (endTime == null) {
        throw Exception("End time should be provided")
      }

      return TimeRangeFilter.before(endTime)
    }
    else -> {
      if (startTime == null || endTime == null) {
        throw Exception("Start time and end time should be provided")
      }

      return TimeRangeFilter.between(startTime, endTime)
    }
  }
}

fun ReadableMap.getPeriod(key: String): Duration {
  if (!this.hasKey("bucketPeriod")) {
    return Duration.ofDays(1)
  }
  val period = this.getString(key)
  if (period.isNullOrEmpty()) {
    return Duration.ofDays(1)
  }

  return when (period) {
    "day" -> Duration.ofDays(1)
    // In future might want to add 'month' & 'year'
    else -> throw Exception("Invalid period type")
  }
}

fun ReadableMap.getUnits(key: String? = null): String? {
  val optionKey = key ?: "unit"
  if (!this.hasKey(optionKey)) {
    return null
  }
  return this.getString(optionKey)
}

fun ReadableMap.getHourFromTimeRange(key: String? = null): Int {
  val optionKey = key ?: "startTime"
  val timeRangeFilter = this.getMap("timeRangeFilter") ?: throw Exception("Time range filter should be provided")
  val startTime =
    if (timeRangeFilter.hasKey(optionKey)) timeRangeFilter.getString(optionKey) else throw Exception("Time range filter should have $optionKey provided")

  val date = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME)
  return date.hour
}

fun convertMetadataToJSMap(meta: Metadata): WritableNativeMap {
  return WritableNativeMap().apply {
    putString("id", meta.id)
    putString("clientRecordId", meta.clientRecordId)
    putDouble("clientRecordVersion", meta.clientRecordVersion.toDouble())
    putString("dataOrigin", meta.dataOrigin.packageName)
    putString("lastModifiedTime", meta.lastModifiedTime.toString())
    putMap("device", convertDeviceToJSMap(meta.device))
    putInt("recordingMethod", meta.recordingMethod)
  }
}

fun convertDeviceToJSMap(device: Device?): WritableNativeMap? {
  if (device == null) {
    return null
  }

  return WritableNativeMap().apply {
    putInt("type", device.type)
    putString("manufacturer", device.manufacturer)
    putString("model", device.model)
  }
}

fun formatDateKey(instant: Instant): String {
  val zoneId = ZoneOffset.systemDefault()
  val zoneTime = ZonedDateTime.ofInstant(instant, zoneId)

  val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
  return zoneTime.format(dateFormatter)
}

// Format to YYYY-MM-DD HH:mm:ss.SSS as local time
fun formatLocalString(instant: Instant): String {
  val zoneId = ZoneOffset.systemDefault()
  val zoneTime = ZonedDateTime.ofInstant(instant, zoneId)

  val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  return zoneTime.format(dateFormatter)
}

fun formatSleepDateKey(instant: Instant, cutOff: Int): String {
  var finalInstant = instant
  val date = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault())
  if (date.hour > cutOff) {
    finalInstant = finalInstant.plus(Duration.ofDays(1))
  }

  return formatDateKey(finalInstant)
}

fun formatNumberAsString(value: Long): String {
  val formatter = DecimalFormat("#.##")
  return formatter.format(value)
}

fun formatNumberAsString(value: Double): String {
  val formatter = DecimalFormat("#.##")
  return formatter.format(value)
}

fun formatDuration(seconds: Double): String {
  val hours = seconds.toInt() / 3600
  val secondsAfterHours = seconds.toInt() % 3600
  val minutes = secondsAfterHours / 60

  return String.format("%d:%02d", hours, minutes)
}

fun formatRecord(date: Instant, type: String, value: String): WritableNativeMap {
  return WritableNativeMap().apply {
    putString("dateKey", formatDateKey(date))
    putMap("entry", WritableNativeMap().apply {
      putString("type", type)
      putString("value", value)
      putString("family", "HEALTH")
    })
  }
}

fun formatRecord(date: String, type: String, value: String): WritableNativeMap {
  return WritableNativeMap().apply {
    putString("dateKey", date)
    putMap("entry", WritableNativeMap().apply {
      putString("type", type)
      putString("value", value)
      putString("family", "HEALTH")
    })
  }
}

fun formatSleepRecord(date: String, type: String, sleepValue: SleepValue): WritableNativeMap {
  val value = formatDuration(sleepValue.duration)
  var entry = WritableNativeMap().apply {
    putString("type", type)
    putString("value", value)
    putString("family", "HEALTH")
  }

  var timesInBed = WritableNativeMap()
  var sleepTimes = WritableNativeMap()

  sleepValue.inBed?.let { inBedAt ->
    timesInBed.putString("inBedAt", formatLocalString(inBedAt))
  }
  sleepValue.outOfBed?.let { outOfBedAt ->
    timesInBed.putString("outOfBedAt", formatLocalString(outOfBedAt))
  }

  sleepValue.fellAsleep?.let { fellAsleepAt ->
    sleepTimes.putString("fellAsleepAt", formatLocalString(fellAsleepAt))
  }
  sleepValue.wokeUp?.let { wokeUpAt ->
    sleepTimes.putString("wokeUpAt", formatLocalString(wokeUpAt))
  }

  if (timesInBed.hasKey("inBedAt") || timesInBed.hasKey("outOfBedAt")) {
    entry.putMap("timesInBed", timesInBed)
  }
  if (sleepTimes.hasKey("fellAsleepAt") || sleepTimes.hasKey("wokeUpAt")) {
    entry.putMap("sleepTimes", sleepTimes)
  }

  return WritableNativeMap().apply {
    putString("dateKey", date)
    putMap("entry", entry)
  }
}

val reactRecordTypeToClassMap: Map<String, KClass<out Record>> = mapOf(
  "BloodPressure" to BloodPressureRecord::class,
  "BodyTemperature" to BodyTemperatureRecord::class,
  "HeartRate" to HeartRateRecord::class,
  "HeartRateVariabilityRmssd" to HeartRateVariabilityRmssdRecord::class,
  "RestingHeartRate" to RestingHeartRateRecord::class,
  "SleepSession" to SleepSessionRecord::class,
  "Steps" to StepsRecord::class,
  "Weight" to WeightRecord::class,
)

val reactRecordTypeToReactClassMap: Map<String, Class<out ReactHealthRecordImpl<*>>> = mapOf(
  "BloodPressure" to ReactBloodPressureRecord::class.java,
  "BodyTemperature" to ReactBodyTemperatureRecord::class.java,
  "HeartRate" to ReactHeartRateRecord::class.java,
  "HeartRateVariabilityRmssd" to ReactHeartRateVariabilityRmssdRecord::class.java,
  "RestingHeartRate" to ReactRestingHeartRateRecord::class.java,
  "SleepSession" to ReactSleepSessionRecord::class.java,
  "Steps" to ReactStepsRecord::class.java,
  "Weight" to ReactWeightRecord::class.java,
)

val reactClassToReactTypeMap = reactRecordTypeToReactClassMap.entries.associateBy({ it.value }) { it.key }

val healthConnectClassToReactClassMap = mapOf(
  BloodPressureRecord::class.java to ReactBloodPressureRecord::class.java,
  BodyTemperatureRecord::class.java to ReactBodyTemperatureRecord::class.java,
  HeartRateRecord::class.java to ReactHeartRateRecord::class.java,
  HeartRateVariabilityRmssdRecord::class.java to ReactHeartRateVariabilityRmssdRecord::class.java,
  RestingHeartRateRecord::class.java to ReactRestingHeartRateRecord::class.java,
  SleepSessionRecord::class.java to ReactSleepSessionRecord::class.java,
  StepsRecord::class.java to ReactStepsRecord::class.java,
  WeightRecord::class.java to ReactWeightRecord::class.java,
)

fun massToJsMap(mass: Mass?): WritableNativeMap {
  return WritableNativeMap().apply {
    putDouble("inGrams", mass?.inGrams ?: 0.0)
    putDouble("inKilograms", mass?.inKilograms ?: 0.0)
    putDouble("inMilligrams", mass?.inMilligrams ?: 0.0)
    putDouble("inMicrograms", mass?.inMicrograms ?: 0.0)
    putDouble("inOunces", mass?.inOunces ?: 0.0)
    putDouble("inPounds", mass?.inPounds ?: 0.0)
  }
}

fun convertChangesTokenRequestOptionsFromJS(options: ReadableMap): ChangesTokenRequest {
  return ChangesTokenRequest(
    recordTypes = convertJsToRecordTypeSet(options.getArray("recordTypes")),
    dataOriginFilters = convertJsToDataOriginSet(options.getArray("dataOriginFilters")),
  )
}
