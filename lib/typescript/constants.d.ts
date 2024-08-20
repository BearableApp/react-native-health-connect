import type { RecordType } from './types';
export declare const SdkAvailabilityStatus: {
    readonly SDK_UNAVAILABLE: 1;
    readonly SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED: 2;
    readonly SDK_AVAILABLE: 3;
};
export declare const BloodPressureBodyPosition: {
    readonly UNKNOWN: 0;
    readonly STANDING_UP: 1;
    readonly SITTING_DOWN: 2;
    readonly LYING_DOWN: 3;
    readonly RECLINING: 4;
};
export declare const BloodPressureMeasurementLocation: {
    readonly UNKNOWN: 0;
    readonly LEFT_WRIST: 1;
    readonly RIGHT_WRIST: 2;
    readonly LEFT_UPPER_ARM: 3;
    readonly RIGHT_UPPER_ARM: 4;
};
export declare const SleepStageType: {
    readonly UNKNOWN: 0;
    readonly AWAKE: 1;
    readonly SLEEPING: 2;
    readonly OUT_OF_BED: 3;
    readonly LIGHT: 4;
    readonly DEEP: 5;
    readonly REM: 6;
};
export declare const RecordTypes: Record<string, RecordType>;
//# sourceMappingURL=constants.d.ts.map