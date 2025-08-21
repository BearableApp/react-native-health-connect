package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.bridge.WritableNativeArray

interface ReactHealthRecordImpl<T : Record> {
  fun getResultType(): String
  fun parseRecord(record: T): WritableNativeMap
  fun getAggregateRequest(record: ReadableMap): AggregateRequest
  fun getAggregateGroupByDurationRequest(record: ReadableMap): AggregateGroupByDurationRequest
  fun getAggregateGroupByPeriodRequest(record: ReadableMap): AggregateGroupByPeriodRequest
  fun parseAggregationResult(record: AggregationResult): WritableNativeMap
  fun getBucketedRequest(options: ReadableMap): AggregateGroupByDurationRequest
  fun parseBucketedResult(records: List<AggregationResultGroupedByDuration>, options: ReadableMap): WritableNativeArray
  fun parseManuallyBucketedResult(records: List<T>, options: ReadableMap): WritableNativeArray
  fun parseAggregationResultGroupedByDuration(record: List<AggregationResultGroupedByDuration>): WritableNativeArray
  fun parseAggregationResultGroupedByPeriod(record: List<AggregationResultGroupedByPeriod>): WritableNativeArray
}
