import type { RecordType } from './records.types';
export interface RecordPermission {
    accessType: 'read' | 'write';
    recordType: RecordType;
}
export interface BackgroundAccessPermission {
    accessType: 'read';
    recordType: 'BackgroundAccessPermission';
}
export interface ReadHealthDataHistoryPermission {
    accessType: 'read';
    recordType: 'ReadHealthDataHistory';
}
export type Permission = RecordPermission | BackgroundAccessPermission | ReadHealthDataHistoryPermission;
/**
 * Response from revokeAllPermissions function
 */
export interface RevokeAllPermissionsResponse {
    /**
     * Whether the revocation request was successful
     */
    revoked: boolean;
    /**
     * On Android 14+, indicates that the app needs to be restarted for the revocation to take effect
     */
    requiresRestart?: boolean;
    /**
     * Additional information about the revocation status
     */
    message?: string;
}
export * from './records.types';
export * from './results.types';
export * from './aggregate.types';
export * from './changes.types';
export * from './metadata.types';
//# sourceMappingURL=index.d.ts.map