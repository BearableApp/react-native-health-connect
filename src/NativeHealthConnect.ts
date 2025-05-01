import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type {
  AggregateRecordResult,
  BucketedRecordsResult,
  GetChangesResults,
  HealthUnit,
  Permission,
  ReadRecordsResult,
  RecordType,
} from './types';

export interface Spec extends TurboModule {
  getSdkStatus(providerPackageName: string): Promise<number>;
  initialize(providerPackageName: string): Promise<boolean>;
  openHealthConnectSettings: () => void;
  openHealthConnectDataManagement: (providerPackageName?: string) => void;
  requestPermission(
    permissions: Permission[],
    providerPackageName: string
  ): Promise<Permission[]>;
  getGrantedPermissions(): Promise<Permission[]>;
  revokeAllPermissions(): Promise<void>;
  readRecords(
    recordType: string,
    options: {
      timeRangeFilter:
        | {
            operator: 'between';
            startTime: string;
            endTime: string;
          }
        | {
            operator: 'after';
            startTime: string;
          }
        | {
            operator: 'before';
            endTime: string;
          };
      dataOriginFilter?: string[];
      ascendingOrder?: boolean;
      pageSize?: number;
      pageToken?: string;
    }
  ): Promise<ReadRecordsResult<RecordType>>;
  aggregateRecord(record: {
    recordType: string;
    startTime: string;
    endTime: string;
  }): Promise<AggregateRecordResult>;
  getChanges(request: {
    changesToken?: string;
    recordTypes?: string[];
    dataOriginFilters?: string[];
  }): Promise<GetChangesResults>;
  readBucketedRecords(
    recordType: string,
    options: {
      timeRangeFilter:
        | {
            operator: 'between';
            startTime: string;
            endTime: string;
          }
        | {
            operator: 'after';
            startTime: string;
          }
        | {
            operator: 'before';
            endTime: string;
          };
      bucketPeriod?: 'day'; // In future 'month' | 'year';
      unit?: 'celsius' | 'fahrenheit' | 'kg' | 'pound';
    }
  ): Promise<BucketedRecordsResult>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('HealthConnect');
