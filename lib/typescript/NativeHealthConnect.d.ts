import type { TurboModule } from 'react-native';
import type { Permission } from './types';
type ReadRecordsOptions = {
    startTime: string;
    endTime: string;
    dataOriginFilter?: string[];
    ascendingOrder?: boolean;
    pageSize?: number;
    pageToken?: string;
};
export interface Spec extends TurboModule {
    getSdkStatus(providerPackageName: string): Promise<number>;
    initialize(providerPackageName: string): Promise<boolean>;
    openHealthConnectSettings: () => void;
    openHealthConnectDataManagement: (providerPackageName?: string) => void;
    requestPermission(permissions: Permission[], providerPackageName: string): Promise<Permission[]>;
    getGrantedPermissions(): Promise<Permission[]>;
    revokeAllPermissions(): Promise<void>;
    readRecords(recordType: string, options: ReadRecordsOptions): Promise<{}>;
    aggregateRecord(record: {
        recordType: string;
        startTime: string;
        endTime: string;
    }): Promise<{}>;
    getChanges(request: {
        changesToken?: string;
        recordTypes?: string[];
        dataOriginFilters?: string[];
    }): Promise<{}>;
}
declare const _default: Spec;
export default _default;
//# sourceMappingURL=NativeHealthConnect.d.ts.map