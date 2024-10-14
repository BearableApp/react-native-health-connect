package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.units.Pressure
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*

class ReactBloodPressureRecord : ReactHealthRecordImpl<BloodPressureRecord> {
  override fun getResultType(): String {
    return "PRESSURE"
  }

  override fun parseRecord(record: BloodPressureRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("time", record.time.toString())
      putInt("measurementLocation", record.measurementLocation)
      putInt("bodyPosition", record.bodyPosition)
      putMap("systolic", bloodPressureToJsMap(record.systolic))
      putMap("diastolic", bloodPressureToJsMap(record.diastolic))
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        BloodPressureRecord.SYSTOLIC_AVG,
        BloodPressureRecord.SYSTOLIC_MIN,
        BloodPressureRecord.SYSTOLIC_MAX,
        BloodPressureRecord.DIASTOLIC_AVG,
        BloodPressureRecord.DIASTOLIC_MIN,
        BloodPressureRecord.DIASTOLIC_MAX
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {

      putMap(
        "SYSTOLIC_AVG",
        getPressureMap(record[BloodPressureRecord.SYSTOLIC_AVG]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "SYSTOLIC_MIN",
        getPressureMap(record[BloodPressureRecord.SYSTOLIC_MIN]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "DIASTOLIC_AVG",
        getPressureMap(record[BloodPressureRecord.DIASTOLIC_AVG]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "DIASTOLIC_MIN",
        getPressureMap(record[BloodPressureRecord.DIASTOLIC_MIN]?.inMillimetersOfMercury ?: 0.0)
      )
      putMap(
        "DIASTOLIC_MAX",
        getPressureMap(record[BloodPressureRecord.DIASTOLIC_MAX]?.inMillimetersOfMercury ?: 0.0)
      )
      putArray("dataOrigins", convertDataOriginsToJsArray(record.dataOrigins))
    }
  }

  override fun getBucketedRequest(record: ReadableMap): AggregateGroupByDurationRequest {
    throw AggregationNotSupported()
  }

  override fun parseBucketedResult(records: List<AggregationResultGroupedByDuration>, options: ReadableMap): WritableNativeArray {
    throw AggregationNotSupported()
  }

  override fun parseManuallyBucketedResult(records: List<BloodPressureRecord>, options: ReadableMap): WritableNativeArray {
    var recordsByDate: MutableMap<String, MutableList<BloodPressureRecord>> = mutableMapOf()

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
        val pressureRecords = recordsForDate.value
        val totalSystolic = pressureRecords.fold(0.0) { acc: Double, record: BloodPressureRecord -> acc + record.systolic.inMillimetersOfMercury }
        val totalDiastolic = pressureRecords.fold(0.0) { acc: Double, record: BloodPressureRecord -> acc + record.diastolic.inMillimetersOfMercury }

        val valueSystolic = formatNumberAsString(totalSystolic / pressureRecords.size)
        val valueDiastolic = formatNumberAsString(totalDiastolic / pressureRecords.size)

        val value = "$valueSystolic/$valueDiastolic"

        val record = formatRecord(dateKey, getResultType(), value)
        pushMap(record)
      }
    }
  }

  private fun bloodPressureToJsMap(pressure: Pressure): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inMillimetersOfMercury", pressure.inMillimetersOfMercury)
    }
  }

  private fun getPressureMap(inMillimetersOfMercury: Double): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble("inMillimetersOfMercury", inMillimetersOfMercury)
    }
  }
}
