package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*

class ReactRestingHeartRateRecord : ReactHealthRecordImpl<RestingHeartRateRecord> {
  override fun getResultType(): String {
    return "RESTING_HEART_RATE"
  }

  override fun parseRecord(record: RestingHeartRateRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putDouble("beatsPerMinute", record.beatsPerMinute.toDouble())
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        RestingHeartRateRecord.BPM_AVG,
        RestingHeartRateRecord.BPM_MAX,
        RestingHeartRateRecord.BPM_MIN,
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("BPM_AVG", record[RestingHeartRateRecord.BPM_AVG]?.toDouble() ?: 0.0)
      putDouble("BPM_MAX", record[RestingHeartRateRecord.BPM_MAX]?.toDouble() ?: 0.0)
      putDouble("BPM_MIN", record[RestingHeartRateRecord.BPM_MIN]?.toDouble() ?: 0.0)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  override fun getBucketedRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    // get the bucket period - defaults to 1 day
    val bucketPeriod = record.getPeriod("bucketPeriod")

    return AggregateGroupByDurationRequest(
        metrics = setOf(
          RestingHeartRateRecord.BPM_AVG
        ),
        timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
        dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter")),
        timeRangeSlicer = bucketPeriod
      )
  }

  override fun parseBucketedResult(records: List<AggregationResultGroupedByDuration>, options: ReadableMap): WritableNativeArray {
    return WritableNativeArray().apply {
      for (daysRecord in records) {
        // The result may be null if no data is available in the time range
        val restingHRAvg = daysRecord.result[RestingHeartRateRecord.BPM_AVG]

        if (restingHRAvg != null) {
          val value = formatLongAsString(restingHRAvg)
          val record = formatRecord(daysRecord.startTime, getResultType(), value)
          pushMap(record)
        }
      }
    }
  }

  override fun parseManuallyBucketedResult(records: List<RestingHeartRateRecord>, options: ReadableMap): WritableNativeArray {
    throw AggregationNotSupported()
  }
}
