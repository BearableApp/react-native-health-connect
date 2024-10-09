package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap

interface ReactHealthRecordImpl<T : Record> {
  fun getResultType(): String
  fun parseRecord(record: T): WritableNativeMap
  fun getAggregateRequest(record: ReadableMap): AggregateRequest
  fun parseAggregationResult(record: AggregationResult): WritableNativeMap
  fun getBucketedRequest(record: ReadableMap): AggregateGroupByDurationRequest
  fun parseBucketedResult(records: List<AggregationResultGroupedByDuration>): WritableNativeArray
}
