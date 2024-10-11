package dev.matinzd.healthconnect

import android.content.Intent
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.changes.DeletionChange
import androidx.health.connect.client.changes.UpsertionChange
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import dev.matinzd.healthconnect.permissions.HealthConnectPermissionDelegate
import dev.matinzd.healthconnect.permissions.PermissionUtils
import dev.matinzd.healthconnect.records.ReactHealthRecord
import dev.matinzd.healthconnect.utils.ClientNotInitialized
import dev.matinzd.healthconnect.utils.convertChangesTokenRequestOptionsFromJS
import dev.matinzd.healthconnect.utils.rejectWithException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthConnectManager(private val applicationContext: ReactApplicationContext) {
  private lateinit var healthConnectClient: HealthConnectClient
  private val coroutineScope = CoroutineScope(Dispatchers.IO)

  private val isInitialized get() = this::healthConnectClient.isInitialized

  private inline fun throwUnlessClientIsAvailable(promise: Promise, block: () -> Unit) {
    if (!isInitialized) {
      return promise.rejectWithException(ClientNotInitialized())
    }
    block()
  }

  fun openHealthConnectSettings() {
    val intent = Intent(HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS)
    applicationContext.currentActivity?.startActivity(intent)
  }

  fun openHealthConnectDataManagement(providerPackageName: String?) {
    val intent = providerPackageName?.let {
      HealthConnectClient.getHealthConnectManageDataIntent(applicationContext, it)
    } ?: HealthConnectClient.getHealthConnectManageDataIntent(applicationContext)
    applicationContext.currentActivity?.startActivity(intent)
  }

  fun getSdkStatus(providerPackageName: String, promise: Promise) {
    val status = HealthConnectClient.getSdkStatus(applicationContext, providerPackageName)
    return promise.resolve(status)
  }

  fun initialize(providerPackageName: String, promise: Promise) {
    try {
      healthConnectClient = HealthConnectClient.getOrCreate(applicationContext, providerPackageName)
      promise.resolve(true)
    } catch (e: Exception) {
      promise.rejectWithException(e)
    }
  }

  fun requestPermission(
    reactPermissions: ReadableArray, providerPackageName: String, promise: Promise
  ) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        val granted = HealthConnectPermissionDelegate.launch(PermissionUtils.parsePermissions(reactPermissions))
        promise.resolve(PermissionUtils.mapPermissionResult(granted))
      }
    }
  }

  fun revokeAllPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        healthConnectClient.permissionController.revokeAllPermissions()
      }
    }
  }

  fun getGrantedPermissions(promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        promise.resolve(PermissionUtils.getGrantedPermissions(healthConnectClient.permissionController))
      }
    }
  }

  fun readRecords(recordType: String, options: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val request = ReactHealthRecord.parseReadRequest(recordType, options)
          val response = healthConnectClient.readRecords(request)
          promise.resolve(ReactHealthRecord.parseRecords(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun aggregateRecord(record: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val recordType = record.getString("recordType") ?: ""
          val response = healthConnectClient.aggregate(
            ReactHealthRecord.getAggregateRequest(
              recordType, record
            )
          )
          promise.resolve(ReactHealthRecord.parseAggregationResult(recordType, response))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun getChanges(options: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val changesToken =
            options.getString("changesToken") ?: healthConnectClient.getChangesToken(convertChangesTokenRequestOptionsFromJS(options))
          val changesResponse = healthConnectClient.getChanges(changesToken)

          promise.resolve(WritableNativeMap().apply {
            val upsertionChanges = WritableNativeArray()
            val deletionChanges = WritableNativeArray()

            for (change in changesResponse.changes) {
              when (change) {
                is UpsertionChange -> {
                  upsertionChanges.pushMap(WritableNativeMap().apply {
                    val record = ReactHealthRecord.parseRecord(change.record)
                    putMap("record", record)
                  })
                }

                is DeletionChange -> {
                  deletionChanges.pushMap(WritableNativeMap().apply {
                    putString("recordId", change.recordId)
                  })
                }
              }
            }

            putArray("upsertionChanges", upsertionChanges)
            putArray("deletionChanges", deletionChanges)
            putString("nextChangesToken", changesResponse.nextChangesToken)
            putBoolean("hasMore", changesResponse.hasMore)
            putBoolean("changesTokenExpired", changesResponse.changesTokenExpired)
          })
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }

  fun readBucketedRecords(recordType: String, options: ReadableMap, promise: Promise) {
    throwUnlessClientIsAvailable(promise) {
      coroutineScope.launch {
        try {
          val request = ReactHealthRecord.getBucketedRequest(recordType, options)
          val response = healthConnectClient.aggregateGroupByDuration(request)
          promise.resolve(ReactHealthRecord.parseBucketedResult(recordType, response, options))
        } catch (e: Exception) {
          promise.rejectWithException(e)
        }
      }
    }
  }
}

