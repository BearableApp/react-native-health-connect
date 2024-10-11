package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*

class ReactStepsRecord : ReactHealthRecordImpl<StepsRecord> {
  override fun getResultType(): String {
    return "STEPS"
  }

  override fun parseRecord(record: StepsRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putDouble("count", record.count.toDouble())
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        StepsRecord.COUNT_TOTAL
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("COUNT_TOTAL", record[StepsRecord.COUNT_TOTAL]?.toDouble() ?: 0.0)
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  override fun getBucketedRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    // get the bucket period - defaults to 1 day
    val bucketPeriod = record.getPeriod("bucketPeriod")

    return AggregateGroupByDurationRequest(
        metrics = setOf(
          StepsRecord.COUNT_TOTAL
        ),
        timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
        dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter")),
        timeRangeSlicer = bucketPeriod
      )
  }

  override fun parseBucketedResult(records: List<AggregationResultGroupedByDuration>): WritableNativeArray {
    return WritableNativeArray().apply {
      for (daysRecord in records) {
        // The result may be null if no data is available in the time range
        val totalSteps = daysRecord.result[StepsRecord.COUNT_TOTAL]

        if (totalSteps != null) {
          val value = formatLongAsString(totalSteps)
          val record = formatRecord(daysRecord.startTime, getResultType(), value)
          pushMap(record)
        }
      }
    }
  }
}
