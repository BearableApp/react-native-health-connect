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
export function getSdkStatus() {
  let providerPackageName = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : DEFAULT_PROVIDER_PACKAGE_NAME;
  return HealthConnect.getSdkStatus(providerPackageName);
}

/**
 * Initializes the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns true if the SDK was initialized successfully
 */
export function initialize() {
  let providerPackageName = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : DEFAULT_PROVIDER_PACKAGE_NAME;
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
 * @returns granted permissions
 */
export function requestPermission(permissions) {
  let providerPackageName = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : DEFAULT_PROVIDER_PACKAGE_NAME;
  return HealthConnect.requestPermission(permissions, providerPackageName);
}
export function getGrantedPermissions() {
  return HealthConnect.getGrantedPermissions();
}
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
export function getChanges(request) {
  return HealthConnect.getChanges(request);
}
export function readBucketedRecords(recordType, options) {
  return HealthConnect.readBucketedRecords(recordType, options);
}
export * from './constants';
export * from './types';
//# sourceMappingURL=index.js.map