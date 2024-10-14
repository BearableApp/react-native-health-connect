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
    throw AggregationNotSupported()
  }
}
