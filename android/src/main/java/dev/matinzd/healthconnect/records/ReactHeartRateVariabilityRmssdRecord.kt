package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.AggregationNotSupported
import dev.matinzd.healthconnect.utils.convertMetadataToJSMap
import dev.matinzd.healthconnect.utils.formatDateKey
import dev.matinzd.healthconnect.utils.formatNumberAsString
import dev.matinzd.healthconnect.utils.formatRecord

class ReactHeartRateVariabilityRmssdRecord :
  ReactHealthRecordImpl<HeartRateVariabilityRmssdRecord> {
  override fun getResultType(): String {
    return "HEART_RATE_VARIABILITY"
  }

  override fun parseRecord(record: HeartRateVariabilityRmssdRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putDouble("heartRateVariabilityMillis", record.heartRateVariabilityMillis)
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

  override fun parseManuallyBucketedResult(records: List<HeartRateVariabilityRmssdRecord>, options: ReadableMap): WritableNativeArray {
    var recordsByDate: MutableMap<String, MutableList<HeartRateVariabilityRmssdRecord>> = mutableMapOf()

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
        val variabilityRecords = recordsForDate.value
        val totalVariability = variabilityRecords.fold(0.0) { acc: Double, record: HeartRateVariabilityRmssdRecord -> acc + record.heartRateVariabilityMillis }
        val value = formatNumberAsString(totalVariability / variabilityRecords.size)

        val record = formatRecord(dateKey, getResultType(), value)
        pushMap(record)
      }
    }
  }
}
