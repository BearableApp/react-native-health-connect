package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.units.Temperature
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.formatDateKey
import dev.matinzd.healthconnect.utils.formatNumberAsString
import dev.matinzd.healthconnect.utils.formatRecord
import dev.matinzd.healthconnect.utils.getUnits

class ReactBodyTemperatureRecord : ReactHealthRecordImpl<BodyTemperatureRecord> {
  override fun getResultType(): String {
    return "BODY_TEMPERATURE"
  }

  override fun parseRecord(record: BodyTemperatureRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putInt("measurementLocation", record.measurementLocation)
      putMap("temperature", temperatureToJsMap(record.temperature))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    throw AggregationNotSupported()
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    throw AggregationNotSupported()
  }

  override fun getBucketedRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    throw AggregationNotSupported()
  }

  override fun parseBucketedResult(records: List<AggregationResultGroupedByDuration>, options: ReadableMap): WritableNativeArray {
    throw AggregationNotSupported()
  }

  override fun parseManuallyBucketedResult(records: List<BodyTemperatureRecord>, options: ReadableMap): WritableNativeArray {
    var recordsByDate: MutableMap<String, MutableList<BodyTemperatureRecord>> = mutableMapOf()
    val units = options.getUnits()

    // Group by date
    for (record in records) {
      val dateKey = formatDateKey(record.time)
      val recordsForDate = recordsByDate.getOrPut(dateKey) { mutableListOf() }
      recordsForDate.add(record)
    }

    return WritableNativeArray().apply {
      // Create aggregate value
      for (recordsForDate in recordsByDate.entries) {
        val dateKey = recordsForDate.key
        val tempRecords = recordsForDate.value
        val totalTemp = tempRecords.fold(0.0) { acc: Double, record: BodyTemperatureRecord -> acc + convertTempToValue(record.temperature, units) }
        val value = formatNumberAsString(totalTemp / tempRecords.size)

        val record = formatRecord(dateKey, getResultType(), value)
        pushMap(record)
      }
    }
  }

  private fun convertTempToValue(temp: Temperature, unit: String?): Double {
    return when (unit) {
      "celsius" -> temp.inCelsius
      "fahrenheit" -> temp.inFahrenheit
      else -> temp.inCelsius
    }
  }

  private fun temperatureToJsMap(temperature: Temperature): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inFahrenheit", temperature.inFahrenheit)
      putDouble("inCelsius", temperature.inCelsius)
    }
  }
}
