package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Instant
import java.time.Period
import java.time.format.DateTimeFormatter

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

  override fun getBucketedRequest(record: ReadableMap): AggregateGroupByPeriodRequest {
    // get the bucket period and default to 1 day if not provided
    val bucketPeriod = if (record.hasKey("bucketPeriod")) record.getPeriod("bucketPeriod") else Period.ofDays(1)

    return AggregateGroupByPeriodRequest(
        metrics = setOf(
          StepsRecord.COUNT_TOTAL
        ),
        timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
        dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter")),
        timeRangeSlicer = bucketPeriod
      )
  }

  override fun parseBucketedResult(records: List<AggregationResultGroupedByPeriod>): WritableNativeArray {
    return WritableNativeArray().apply {
        for (daysRecord in records) {
          // The result may be null if no data is available in the time range
          val totalSteps = daysRecord.result[StepsRecord.COUNT_TOTAL]
          // Parse start time in string format YYYYMMDD
          val date = daysRecord.startTime.format(DateTimeFormatter.BASIC_ISO_DATE)

          if (totalSteps != null) {
            pushMap(WritableNativeMap().apply {
              putString("date", date)
              putMap("entry", WritableNativeMap().apply {
                putString("type", getResultType())
                putString("value", totalSteps.toString())
                putString("family", "HEALTH")
              })
            })
          }
        }
      }
  }
}
