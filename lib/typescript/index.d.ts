import type { AggregateRequest, AggregateResult, AggregateResultRecordType, Permission, ReadRecordsOptions, RecordType, ReadRecordsResult, GetChangesRequest, GetChangesResults } from './types';
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
 * @returns granted permissions
 */
export declare function requestPermission(permissions: Permission[], providerPackageName?: string): Promise<Permission[]>;
export declare function getGrantedPermissions(): Promise<Permission[]>;
export declare function revokeAllPermissions(): void;
export declare function readRecords<T extends RecordType>(recordType: T, options: ReadRecordsOptions): Promise<ReadRecordsResult<T>>;
export declare function aggregateRecord<T extends AggregateResultRecordType>(request: AggregateRequest<T>): Promise<AggregateResult<T>>;
export declare function getChanges(request: GetChangesRequest): Promise<GetChangesResults>;
export * from './constants';
export * from './types';
//# sourceMappingURL=index.d.ts.map