import type { AggregateRequest, AggregateResult, AggregateGroupByDurationRequest, AggregateGroupByPeriodRequest, AggregationGroupResult, AggregateResultRecordType, Permission, ReadRecordsOptions, RecordType, ReadRecordsResult, GetChangesRequest, GetChangesResults, BucketedRequestOptions, BucketedRecordsResult, RevokeAllPermissionsResponse } from './types';
/**
 * Gets the status of the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns the status of the SDK - check SdkAvailabilityStatus constants
 */
export declare function getSdkStatus(providerPackageName?: string): Promise<number>;
/**
 * Initializes the Health Connect SDK
 * @param providerPackageName the package name of the Health Connect provider
 * @returns true if the SDK was initialized successfully
 */
export declare function initialize(providerPackageName?: string): Promise<boolean>;
/**
 * Opens Health Connect settings app
 */
export declare function openHealthConnectSettings(): void;
/**
 * Opens Health Connect data management screen
 */
export declare function openHealthConnectDataManagement(providerPackageName?: string): void;
/**
 * Request permissions to access Health Connect data
 * @param permissions list of permissions to request
 * @returns granted permissions, including special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission
 */
export declare function requestPermission(permissions: Permission[]): Promise<Permission[]>;
/**
 * Returns a set of all health permissions granted by the user to the calling app.
 * This includes regular permissions as well as special permissions like WriteExerciseRoutePermission and BackgroundAccessPermission.
 * @returns A promise that resolves to an array of granted permissions
 */
export declare function getGrantedPermissions(): Promise<Permission[]>;
/**
 * Revokes all previously granted permissions by the user to the calling app.
 * On Android 14+, permissions are not immediately revoked. They will be revoked when the app restarts.
 * @returns A promise that resolves to a RevokeAllPermissionsResponse object containing information about the revocation status,
 * or void for backward compatibility with older versions
 */
export declare function revokeAllPermissions(): Promise<RevokeAllPermissionsResponse | void>;
export declare function readRecords<T extends RecordType>(recordType: T, options: ReadRecordsOptions): Promise<ReadRecordsResult<T>>;
export declare function aggregateRecord<T extends AggregateResultRecordType>(request: AggregateRequest<T>): Promise<AggregateResult<T>>;
export declare function aggregateGroupByDuration<T extends AggregateResultRecordType>(request: AggregateGroupByDurationRequest<T>): Promise<AggregationGroupResult<T>[]>;
export declare function aggregateGroupByPeriod<T extends AggregateResultRecordType>(request: AggregateGroupByPeriodRequest<T>): Promise<AggregationGroupResult<T>[]>;
export declare function getChanges(request: GetChangesRequest): Promise<GetChangesResults>;
export declare function readBucketedRecords(recordType: RecordType, options: BucketedRequestOptions): Promise<BucketedRecordsResult>;
export * from './constants';
export * from './types';
//# sourceMappingURL=index.d.ts.map