package dev.matinzd.healthconnect

import com.facebook.react.bridge.*

abstract class HealthConnectSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  @ReactMethod
  abstract fun getSdkStatus(providerPackageName: String, promise: Promise)

  @ReactMethod
  abstract fun initialize(providerPackageName: String, promise: Promise);

  @ReactMethod
  abstract fun openHealthConnectSettings();

  @ReactMethod
  abstract fun openHealthConnectDataManagement(providerPackageName: String?);

  @ReactMethod
  abstract fun requestPermission(permissions: ReadableArray, providerPackageName: String, promise: Promise);

  @ReactMethod
  abstract fun getGrantedPermissions(promise: Promise);

  @ReactMethod
  abstract fun revokeAllPermissions(promise: Promise);

  @ReactMethod
  abstract fun readRecords(recordType: String, options: ReadableMap, promise: Promise);

  @ReactMethod
  abstract fun aggregateRecord(record: ReadableMap, promise: Promise);

  @ReactMethod
  abstract fun readBucketedRecords(recordType: String, options: ReadableMap, promise: Promise);

  @ReactMethod
  abstract fun readManuallyBucketedRecords(recordType: String, options: ReadableMap, promise: Promise);

  @ReactMethod
  abstract fun getChanges(options: ReadableMap, promise: Promise);
}
