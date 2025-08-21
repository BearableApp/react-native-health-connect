import { NativeModules, Platform } from 'react-native';
import type {
  AggregateRequest,
  AggregateResult,
  AggregateGroupByDurationRequest,
  AggregateGroupByPeriodRequest,
  AggregationGroupResult,
  AggregateResultRecordType,
  Permission,
  ReadRecordsOptions,
  RecordType,
  ReadRecordsResult,
  GetChangesRequest,
  GetChangesResults,
  BucketedRequestOptions,
  BucketedRecordsResult,
  ReadHealthDataHistoryPermission,
  BackgroundAccessPermission,
  RevokeAllPermissionsResponse,
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
 * @returns granted permissions, including special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission
 */
export function requestPermission(
  permissions: (
    | Permission
    | BackgroundAccessPermission
    | ReadHealthDataHistoryPermission
  )[]
): Promise<
  (Permission | ReadHealthDataHistoryPermission | BackgroundAccessPermission)[]
> {
  return HealthConnect.requestPermission(permissions);
}

/**
 * Returns a set of all health permissions granted by the user to the calling app.
 * This includes regular permissions as well as special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission.
 * @returns A promise that resolves to an array of granted permissions
 */
export function getGrantedPermissions(): Promise<
  (Permission | BackgroundAccessPermission)[]
> {
  return HealthConnect.getGrantedPermissions();
}

/**
 * Revokes all previously granted permissions by the user to the calling app.
 * On Android 14+, permissions are not immediately revoked. They will be revoked when the app restarts.
 * @returns A promise that resolves to a RevokeAllPermissionsResponse object containing information about the revocation status,
 * or void for backward compatibility with older versions
 */
export function revokeAllPermissions(): Promise<RevokeAllPermissionsResponse | void> {
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

export function aggregateGroupByDuration<T extends AggregateResultRecordType>(
  request: AggregateGroupByDurationRequest<T>
): Promise<AggregationGroupResult<T>[]> {
  return HealthConnect.aggregateGroupByDuration(request);
}

export function aggregateGroupByPeriod<T extends AggregateResultRecordType>(
  request: AggregateGroupByPeriodRequest<T>
): Promise<AggregationGroupResult<T>[]> {
  return HealthConnect.aggregateGroupByPeriod(request);
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
