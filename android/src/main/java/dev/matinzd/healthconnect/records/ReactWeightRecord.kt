package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.units.Mass
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*

class ReactWeightRecord : ReactHealthRecordImpl<WeightRecord> {
  override fun getResultType(): String {
    return "WEIGHT"
  }

  override fun parseRecord(record: WeightRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putMap("weight", massToJsMap(record.weight))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        WeightRecord.WEIGHT_AVG,
        WeightRecord.WEIGHT_MAX,
        WeightRecord.WEIGHT_MIN,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putMap("WEIGHT_AVG", massToJsMap(record[WeightRecord.WEIGHT_AVG]))
      putMap("WEIGHT_MAX", massToJsMap(record[WeightRecord.WEIGHT_MAX]))
      putMap("WEIGHT_MIN", massToJsMap(record[WeightRecord.WEIGHT_MIN]))
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  override fun getBucketedRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    // get the bucket period - defaults to 1 day
    val bucketPeriod = record.getPeriod("bucketPeriod")

    return AggregateGroupByDurationRequest(
      metrics = setOf(
        WeightRecord.WEIGHT_AVG
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter")),
      timeRangeSlicer = bucketPeriod
    )
  }

  override fun parseBucketedResult(records: List<AggregationResultGroupedByDuration>, options: ReadableMap): WritableNativeArray {
    val units = options.getUnits()

    return WritableNativeArray().apply {
      for (daysRecord in records) {
        // The result may be null if no data is available in the time range
        val avgWeight = daysRecord.result[WeightRecord.WEIGHT_AVG]

        if (avgWeight != null) {
          val value = convertMassToValue(avgWeight, units)
          val record = formatRecord(daysRecord.startTime, getResultType(), value)
          pushMap(record)
        }
      }
    }
  }

  override fun parseManuallyBucketedResult(records: List<WeightRecord>, options: ReadableMap): WritableNativeArray {
    throw AggregationNotSupported()
  }

  private fun convertMassToValue(mass: Mass, unit: String?): String {
    var value: Double = when (unit) {
      "kg" -> mass.inKilograms
      "pound" -> mass.inPounds
      else -> mass.inKilograms
    }

    return formatDoubleAsString(value)
  }
}
