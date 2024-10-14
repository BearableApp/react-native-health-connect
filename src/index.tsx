import { NativeModules, Platform } from 'react-native';
import type {
  AggregateRequest,
  AggregateResult,
  AggregateResultRecordType,
  Permission,
  ReadRecordsOptions,
  RecordType,
  ReadRecordsResult,
  GetChangesRequest,
  GetChangesResults,
  BucketedRequestOptions,
  BucketedRecordsResult,
} from './types';

const LINKING_ERROR =
  `The package 'react-native-health-connect' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const PLATFORM_NOT_SUPPORTED_ERROR = `Platform not supported. This package only supports Android.`;

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const moduleProxy = (message: string) =>
  new Proxy(
    {},
    {
      get() {
        throw new Error(message);
      },
    }
  );

const HealthConnectModule = Platform.select({
  android: isTurboModuleEnabled
    ? require('./NativeHealthConnect').default
    : NativeModules.HealthConnect,
  ios: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR),
  default: moduleProxy(PLATFORM_NOT_SUPPORTED_ERROR),
});

const HealthConnect = HealthConnectModule
  ? HealthConnectModule
  : moduleProxy(LINKING_ERROR);

const DEFAULT_PROVIDER_PACKAGE_NAME = 'com.google.android.apps.healthdata';

/**
 * Gets the status of the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns the status of the SDK - check SdkAvailabilityStatus constants
 */
export function getSdkStatus(
  providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME
): Promise<number> {
  return HealthConnect.getSdkStatus(providerPackageName);
}

/**
 * Initializes the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns true if the SDK was initialized successfully
 */
export function initialize(
  providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME
): Promise<boolean> {
  return HealthConnect.initialize(providerPackageName);
}

/**
 * Opens Health Connect settings app
 */
export function openHealthConnectSettings(): void {
  return HealthConnect.openHealthConnectSettings();
}

/**
 * Opens Health Connect data management screen
 */
export function openHealthConnectDataManagement(
  providerPackageName?: string
): void {
  return HealthConnect.openHealthConnectDataManagement(providerPackageName);
}

/**
 * Request permissions to access Health Connect data
 * @param permissions list of permissions to request
 * @returns granted permissions
 */
export function requestPermission(
  permissions: Permission[],
  providerPackageName = DEFAULT_PROVIDER_PACKAGE_NAME
): Promise<Permission[]> {
  return HealthConnect.requestPermission(permissions, providerPackageName);
}

export function getGrantedPermissions(): Promise<Permission[]> {
  return HealthConnect.getGrantedPermissions();
}

export function revokeAllPermissions(): void {
  return HealthConnect.revokeAllPermissions();
}

export function readRecords<T extends RecordType>(
  recordType: T,
  options: ReadRecordsOptions
): Promise<ReadRecordsResult<T>> {
  return HealthConnect.readRecords(recordType, options);
}

export function aggregateRecord<T extends AggregateResultRecordType>(
  request: AggregateRequest<T>
): Promise<AggregateResult<T>> {
  // TODO: Handle blood pressure aggregate only available on Android 15+
  return HealthConnect.aggregateRecord(request);
}

export function getChanges(
  request: GetChangesRequest
): Promise<GetChangesResults> {
  return HealthConnect.getChanges(request);
}

export function readBucketedRecords(
  recordType: RecordType,
  options: BucketedRequestOptions
): Promise<BucketedRecordsResult> {
  // These types aren't currently supported by the aggregateByDuration sdk method
  // TODO: Handle blood pressure aggregate only available on Android 15+
  if (
    [
      'BloodPressure',
      'BodyTemperature',
      'HeartRateVariabilityRmssd',
      'SleepSession',
    ].includes(recordType)
  ) {
    return HealthConnect.readManuallyBucketedRecords(recordType, options);
  }

  return HealthConnect.readBucketedRecords(recordType, options);
}

export * from './constants';
export * from './types';
