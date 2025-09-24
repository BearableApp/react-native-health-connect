package dev.matinzd.healthconnect

import com.facebook.react.bridge.*

class HealthConnectModule internal constructor(context: ReactApplicationContext) :
  HealthConnectSpec(context) {

  private val manager = HealthConnectManager(context)

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  override fun getSdkStatus(providerPackageName: String, promise: Promise) {
    return manager.getSdkStatus(providerPackageName, promise)
  }

  @ReactMethod
  override fun openHealthConnectSettings() {
    manager.openHealthConnectSettings()
  }

  @ReactMethod
  override fun openHealthConnectDataManagement(providerPackageName: String?) {
    manager.openHealthConnectDataManagement(providerPackageName)
  }

  @ReactMethod
  override fun initialize(providerPackageName: String, promise: Promise) {
    return manager.initialize(providerPackageName, promise)
  }

  @ReactMethod
  override fun requestPermission(
    permissions: ReadableArray,
    promise: Promise
  ) {
    return manager.requestPermission(permissions, promise)
  }

  @ReactMethod
  override fun getGrantedPermissions(promise: Promise) {
    return manager.getGrantedPermissions(promise)
  }

  @ReactMethod
  override fun revokeAllPermissions(promise: Promise) {
    return manager.revokeAllPermissions(promise)
  }

  @ReactMethod
  override fun readRecords(recordType: String, options: ReadableMap, promise: Promise) {
    return manager.readRecords(recordType, options, promise)
  }

  @ReactMethod
  override fun aggregateRecord(record: ReadableMap, promise: Promise) {
    return manager.aggregateRecord(record, promise)
  }

  @ReactMethod
  override fun aggregateGroupByDuration(record: ReadableMap, promise: Promise) {
    return manager.aggregateGroupByDuration(record, promise)
  }

  @ReactMethod
  override fun aggregateGroupByPeriod(record: ReadableMap, promise: Promise) {
    return manager.aggregateGroupByPeriod(record, promise)
  }

  @ReactMethod
  override fun getChanges(options: ReadableMap, promise: Promise) {
    return manager.getChanges(options, promise)
  }

  @ReactMethod
  override fun readBucketedRecords(recordType: String, options: ReadableMap, promise: Promise) {
    return manager.readBucketedRecords(recordType, options, promise)
  }

  @ReactMethod
  fun readManuallyBucketedRecords(recordType: String, options: ReadableMap, promise: Promise) {
    return manager.readManuallyBucketedRecords(recordType, options, promise)
  }

  companion object {
    const val NAME = "HealthConnect"
  }
}
