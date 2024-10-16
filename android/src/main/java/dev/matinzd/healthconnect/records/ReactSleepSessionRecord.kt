package dev.matinzd.healthconnect.records

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateRequest
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.utils.*
import java.time.Duration
import java.time.Instant

enum class SleepType {
  IN_BED, ASLEEP, AWAKE
}

data class SimpleSleepSample(
  val startDate: Instant,
  val endDate: Instant,
  val type: SleepType
)

data class SleepValue(
  var duration: Double,
  var inBed: Instant? = null,
  var outOfBed: Instant? = null,
  var fellAsleep: Instant? = null,
  var wokeUp: Instant? = null
)

class ReactSleepSessionRecord : ReactHealthRecordImpl<SleepSessionRecord> {
  override fun getResultType(): String {
    return "SLEEP"
  }

  override fun parseRecord(record: SleepSessionRecord): WritableNativeMap {
    return WritableNativeMap().apply {
      putString("startTime", record.startTime.toString())
      putString("endTime", record.endTime.toString())
      putString("title", record.title)
      putString("notes", record.notes)
      putArray("stages", WritableNativeArray().apply {
        record.stages.map {
          val map = WritableNativeMap()
          map.putString("startTime", it.startTime.toString())
          map.putString("endTime", it.endTime.toString())
          map.putDouble("stage", it.stage.toDouble())
          this.pushMap(map)
        }
      })
      putMap("metadata", convertMetadataToJSMap(record.metadata))
    }
  }

  override fun getAggregateRequest(record: ReadableMap): AggregateRequest {
    return AggregateRequest(
      metrics = setOf(
        SleepSessionRecord.SLEEP_DURATION_TOTAL
      ),
      timeRangeFilter = record.getTimeRangeFilter("timeRangeFilter"),
      dataOriginFilter = convertJsToDataOriginSet(record.getArray("dataOriginFilter"))
    )
  }

  override fun parseAggregationResult(record: AggregationResult): WritableNativeMap {
    return WritableNativeMap().apply {
      putDouble(
        "SLEEP_DURATION_TOTAL",
        record[SleepSessionRecord.SLEEP_DURATION_TOTAL]?.seconds?.toDouble() ?: 0.0
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

  override fun parseManuallyBucketedResult(records: List<SleepSessionRecord>, options: ReadableMap): WritableNativeArray {
    var sampleDict: MutableMap<String, MutableMap<SleepType, MutableList<SimpleSleepSample>>> = mutableMapOf()
    val cutOffHour = options.getHourFromTimeRange()

    for (daysRecord in records) {
      var stages: List<SimpleSleepSample> = mutableListOf()
      for (stage in daysRecord.stages) {
        stages = stages.plus(SimpleSleepSample(
          startDate = stage.startTime,
          endDate = stage.endTime,
          type = convertRecordToSleepType(stage.stage)
        ))
      }

      // Can't use sleep stage class as they're only available API 34+ so looping and converting types first
      // The in bed time is consider the whole record (any overlap will be handled below)
      // If no stages then a manual entry has been made and this is considered in bed
      stages = stages.plus(SimpleSleepSample(
        startDate = daysRecord.startTime,
        endDate = daysRecord.endTime,
        type = SleepType.IN_BED
      ))

      for (stage in stages) {
        val dateKey = formatSleepDateKey(stage.startDate, cutOffHour)
        val sleepType = stage.type
        if (sleepType == SleepType.AWAKE) {
          continue
        }

        val sleepTypeDict = sampleDict.getOrPut(dateKey) { mutableMapOf() }
        val samplesForType = sleepTypeDict.getOrPut(sleepType) { mutableListOf() }
        var newSample = stage

        // Check if there's an overlap with the last sample in the list
        if (samplesForType.isNotEmpty()) {
          val lastSample = samplesForType.last()

          // If the last sample's endDate is greater than the new sample's startDate, we have an overlap
          if (lastSample.endDate.isAfter(stage.startDate)) {
            // Full overlap: skip if the last sample's endDate is greater than or equal to the new sample's endDate
            if (lastSample.endDate.isAfter(stage.endDate) || lastSample.endDate == stage.endDate) {
              continue
            }

            // Partial overlap: Adjust the new sample's startDate to the last sample's startDate
            newSample = stage.copy(startDate = lastSample.startDate)
            samplesForType.removeLast()
          }
        }
        samplesForType.add(newSample)
        sleepTypeDict[sleepType] = samplesForType
        sampleDict[dateKey] = sleepTypeDict
      }
    }

    return WritableNativeArray().apply {
      for (dayEntry in sampleDict.entries) {
        val dateKey = dayEntry.key
        var sleepValue = SleepValue(
          duration = 0.0,
          inBed = null,
          outOfBed = null,
          fellAsleep = null,
          wokeUp = null,
        )

        for (entries in dayEntry.value.entries) {
          for (entry in entries.value) {
            when (entries.key) {
              SleepType.IN_BED -> {
                sleepValue.inBed = if (sleepValue.inBed != null && sleepValue.inBed!!.isBefore(entry.startDate)) {
                  sleepValue.inBed
                } else {
                  entry.startDate
                }
                sleepValue.outOfBed = if (sleepValue.outOfBed != null && sleepValue.outOfBed!!.isBefore(entry.endDate)) {
                  sleepValue.outOfBed
                } else {
                  entry.endDate
                }
              }
              SleepType.ASLEEP -> {
                sleepValue.fellAsleep = if (sleepValue.fellAsleep != null && sleepValue.fellAsleep!!.isBefore(entry.startDate)) {
                  sleepValue.fellAsleep
                } else {
                  entry.startDate
                }
                sleepValue.wokeUp = if (sleepValue.wokeUp != null && sleepValue.wokeUp!!.isAfter(entry.endDate)) {
                  sleepValue.wokeUp
                } else {
                  entry.endDate
                }
                sleepValue.duration += Duration.between(entry.startDate, entry.endDate).seconds
              }
              else -> {}
            }
          }
        }

        // If the user has no asleep values then the main value will be the time taken from the in bed times
        if (sleepValue.fellAsleep == null && sleepValue.wokeUp == null && sleepValue.inBed != null && sleepValue.outOfBed != null) {
          sleepValue.duration += Duration.between(sleepValue.inBed, sleepValue.outOfBed).seconds
        }

        val record = formatSleepRecord(dateKey, getResultType(), sleepValue)
        pushMap(record)
      }
    }
  }

  private fun convertRecordToSleepType(stage: Int): SleepType {
    return when (stage) {
      SleepSessionRecord.STAGE_TYPE_AWAKE -> SleepType.AWAKE
      SleepSessionRecord.STAGE_TYPE_OUT_OF_BED -> SleepType.AWAKE
      SleepSessionRecord.STAGE_TYPE_UNKNOWN -> SleepType.AWAKE
      SleepSessionRecord.STAGE_TYPE_AWAKE_IN_BED -> SleepType.IN_BED
      SleepSessionRecord.STAGE_TYPE_DEEP -> SleepType.ASLEEP
      SleepSessionRecord.STAGE_TYPE_LIGHT -> SleepType.ASLEEP
      SleepSessionRecord.STAGE_TYPE_REM -> SleepType.ASLEEP
      SleepSessionRecord.STAGE_TYPE_SLEEPING -> SleepType.ASLEEP
      else -> SleepType.ASLEEP
    }
  }
}
