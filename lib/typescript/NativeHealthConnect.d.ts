import type { TurboModule } from 'react-native';
import type { AggregateRecordResult, BucketedRecordsResult, GetChangesResults, Permission, ReadRecordsResult, RecordType } from './types';
export interface Spec extends TurboModule {
    getSdkStatus(providerPackageName: string): Promise<number>;
    initialize(providerPackageName: string): Promise<boolean>;
    openHealthConnectSettings: () => void;
    openHealthConnectDataManagement: (providerPackageName?: string) => void;
    requestPermission(permissions: Permission[]): Promise<Permission[]>;
    getGrantedPermissions(): Promise<Permission[]>;
    revokeAllPermissions(): Promise<void>;
    readRecords(recordType: string, options: {
        timeRangeFilter: {
            operator: 'between';
            startTime: string;
            endTime: string;
        } | {
            operator: 'after';
            startTime: string;
        } | {
            operator: 'before';
            endTime: string;
        };
        dataOriginFilter?: string[];
        ascendingOrder?: boolean;
        pageSize?: number;
        pageToken?: string;
    }): Promise<ReadRecordsResult<RecordType>>;
    aggregateRecord(record: {
        recordType: string;
        startTime: string;
        endTime: string;
    }): Promise<AggregateRecordResult>;
    aggregateGroupByDuration(record: {
        recordType: string;
        startTime: string;
        endTime: string;
        timeRangeSlicer: Object;
    }): Promise<[]>;
    aggregateGroupByPeriod(record: {
        recordType: string;
        startTime: string;
        endTime: string;
        timeRangeSlicer: Object;
    }): Promise<[]>;
    getChanges(request: {
        changesToken?: string;
        recordTypes?: string[];
        dataOriginFilters?: string[];
    }): Promise<GetChangesResults>;
    readBucketedRecords(recordType: string, options: {
        timeRangeFilter: {
            operator: 'between';
            startTime: string;
            endTime: string;
        } | {
            operator: 'after';
            startTime: string;
        } | {
            operator: 'before';
            endTime: string;
        };
        bucketPeriod?: 'day';
        unit?: 'celsius' | 'fahrenheit' | 'kg' | 'pound';
    }): Promise<BucketedRecordsResult>;
    readManuallyBucketedRecords(recordType: string, options: {
        timeRangeFilter: {
            operator: 'between';
            startTime: string;
            endTime: string;
        } | {
            operator: 'after';
            startTime: string;
        } | {
            operator: 'before';
            endTime: string;
        };
        bucketPeriod?: 'day';
        unit?: 'celsius' | 'fahrenheit' | 'kg' | 'pound';
    }): Promise<BucketedRecordsResult>;
}
declare const _default: Spec;
export default _default;
//# sourceMappingURL=NativeHealthConnect.d.ts.map