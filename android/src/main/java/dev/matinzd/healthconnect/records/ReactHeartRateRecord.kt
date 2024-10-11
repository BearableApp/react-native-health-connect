package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*

class ReactHeartRateRecord : ReactHealthRecordImpl<HeartRateRecord> {
  override fun getResultType(): String {
    return "HEART"
  }

  override fun parseRecord(record: HeartRateRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      val array = WritableNativeArray().apply {
        record.samples.map {
          val map = WritableNativeMap()
          map.putString("time", it.time.toString())
          map.putDouble("beatsPerMinute", it.beatsPerMinute.toDouble())
          this.pushMap(map)
        }
      }
      putArray("samples", array)
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        HeartRateRecord.BPM_AVG,
        HeartRateRecord.BPM_MAX,
        HeartRateRecord.BPM_MIN,
        HeartRateRecord.MEASUREMENTS_COUNT
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("BPM_AVG", record[HeartRateRecord.BPM_AVG]?.toDouble() ?: 0.0)
      putDouble("BPM_MAX", record[HeartRateRecord.BPM_MAX]?.toDouble() ?: 0.0)
      putDouble("BPM_MIN", record[HeartRateRecord.BPM_MIN]?.toDouble() ?: 0.0)
      putDouble("MEASUREMENTS_COUNT", record[HeartRateRecord.MEASUREMENTS_COUNT]?.toDouble() ?: 0.0)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  override fun getBucketedRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    // get the bucket period - defaults to 1 day
    val bucketPeriod = record.getPeriod("bucketPeriod")

    return AggregateGroupByDurationRequest(
      metrics = setOf(
        HeartRateRecord.BPM_AVG,
        HeartRateRecord.BPM_MAX,
        HeartRateRecord.BPM_MIN,
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
        val hrMin = daysRecord.result[HeartRateRecord.BPM_MIN]
        val hrAvg = daysRecord.result[HeartRateRecord.BPM_AVG]
        val hrMax = daysRecord.result[HeartRateRecord.BPM_MAX]

        if (hrMin != null && hrAvg != null && hrMax != null) {
          val value = "${formatLongAsString(hrMin)}/${formatLongAsString(hrAvg)}/${formatLongAsString(hrMax)}"
          val record = formatRecord(daysRecord.startTime, getResultType(), value)
          pushMap(record)
        }
      }
    }
  }
}
