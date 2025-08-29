import { NativeModules, Platform } from 'react-native';
const LINKING_ERROR = `The package 'react-native-health-connect' doesn't seem to be linked. Make sure: \n\n` + Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
const PLATFORM_NOT_SUPPORTED_ERROR = `Platform not supported. This package only supports Android.`;

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;
const moduleProxy = message => new Proxy({}, {
  get() {
    throw new Error(message);
  }
});
const HealthConnectModule = Platform.select({
  android: isTurboModuleEnabled ? require('./NativeHealthConnect').default : NativeModules.HealthConnect,
  ios: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR),
  default: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR)
});
const HealthConnect = HealthConnectModule ? HealthConnectModule : moduleProxy(LINKING_ERROR);
const DEFAULT_PROVIDER_PACKAGE_NAME = 'com.google.android.apps.healthdata';

/**
 * Gets the status of the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns the status of the SDK - check SdkAvailabilityStatus constants
 */
export function getSdkStatus(providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME) {
  return HealthConnect.getSdkStatus(providerPackageName);
}

/**
 * Initializes the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns true if the SDK was initialized successfully
 */
export function initialize(providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME) {
  return HealthConnect.initialize(providerPackageName);
}

/**
 * Opens Health Connect settings app
 */
export function openHealthConnectSettings() {
  return HealthConnect.openHealthConnectSettings();
}

/**
 * Opens Health Connect data management screen
 */
export function openHealthConnectDataManagement(providerPackageName) {
  return HealthConnect.openHealthConnectDataManagement(providerPackageName);
}

/**
 * Request permissions to access Health Connect data
 * @param permissions list of permissions to request
 * @returns granted permissions, including special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission
 */
export function requestPermission(permissions) {
  return HealthConnect.requestPermission(permissions);
}

/**
 * Returns a set of all health permissions granted by the user to the calling app.
 * This includes regular permissions as well as special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission.
 * @returns A promise that resolves to an array of granted permissions
 */
export function getGrantedPermissions() {
  return HealthConnect.getGrantedPermissions();
}

/**
 * Revokes all previously granted permissions by the user to the calling app.
 * On Android 14+, permissions are not immediately revoked. They will be revoked when the app restarts.
 * @returns A promise that resolves to a RevokeAllPermissionsResponse object containing information about the revocation status,
 * or void for backward compatibility with older versions
 */
export function revokeAllPermissions() {
  return HealthConnect.revokeAllPermissions();
}
export function readRecords(recordType, options) {
  return HealthConnect.readRecords(recordType, options);
}
export function aggregateRecord(request) {
  // TODO: Handle blood pressure aggregate only available on Android 15+
  return HealthConnect.aggregateRecord(request);
}
export function aggregateGroupByDuration(request) {
  return HealthConnect.aggregateGroupByDuration(request);
}
export function aggregateGroupByPeriod(request) {
  return HealthConnect.aggregateGroupByPeriod(request);
}
export function getChanges(request) {
  return HealthConnect.getChanges(request);
}
export function readBucketedRecords(recordType, options) {
  // These types aren't currently supported by the aggregateByDuration sdk method
  // TODO: Handle blood pressure aggregate only available on Android 15+
  if (['BloodPressure', 'BodyTemperature', 'HeartRateVariabilityRmssd', 'SleepSession'].includes(recordType)) {
    return HealthConnect.readManuallyBucketedRecords(recordType, options);
  }
  return HealthConnect.readBucketedRecords(recordType, options);
}
export * from './constants';
export * from './types';
//# sourceMappingURL=index.js.map