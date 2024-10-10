import type { MassResult, PressureResult, TimeRangeFilter } from './base.types';
interface BaseAggregate {
    dataOrigins: string[];
}
interface BloodPressureAggregateResult extends BaseAggregate {
    recordType: 'BloodPressure';
    SYSTOLIC_AVG: PressureResult;
    SYSTOLIC_MIN: PressureResult;
    DIASTOLIC_AVG: PressureResult;
    DIASTOLIC_MIN: PressureResult;
    DIASTOLIC_MAX: PressureResult;
}
interface HeartRateAggregateResult extends BaseAggregate {
    recordType: 'HeartRate';
    BPM_AVG: number;
    BPM_MAX: number;
    BPM_MIN: number;
    MEASUREMENTS_COUNT: number;
}
interface RestingHeartRateAggregateResult extends BaseAggregate {
    recordType: 'RestingHeartRate';
    BPM_AVG: number;
    BPM_MAX: number;
    BPM_MIN: number;
}
interface StepsAggregateResult extends BaseAggregate {
    recordType: 'Steps';
    COUNT_TOTAL: number;
}
interface WeightAggregateResult extends BaseAggregate {
    recordType: 'Weight';
    WEIGHT_AVG: MassResult;
    WEIGHT_MAX: MassResult;
    WEIGHT_MIN: MassResult;
}
interface SleepSessionAggregateResult extends BaseAggregate {
    recordType: 'SleepSession';
    SLEEP_DURATION_TOTAL: number;
}
export type AggregateRecordResult = BloodPressureAggregateResult | HeartRateAggregateResult | RestingHeartRateAggregateResult | StepsAggregateResult | WeightAggregateResult | SleepSessionAggregateResult;
export type AggregateResultRecordType = AggregateRecordResult['recordType'];
export type AggregateResult<T extends AggregateResultRecordType> = Omit<Extract<AggregateRecordResult, {
    recordType: T;
}>, 'recordType'>;
export interface AggregateRequest<T extends AggregateResultRecordType> {
    recordType: T;
    timeRangeFilter: TimeRangeFilter;
    dataOriginFilter?: string[];
}
export interface BucketedRequestOptions {
    timeRangeFilter: TimeRangeFilter;
    bucketPeriod?: 'day';
}
export {};
//# sourceMappingURL=aggregate.types.d.ts.map