import type { Metadata } from './metadata.types';
export interface BaseRecord {
    metadata?: Metadata;
}
export interface InstantaneousRecord extends BaseRecord {
    time: string;
}
export interface IntervalRecord extends BaseRecord {
    startTime: string;
    endTime: string;
}
export type TimeRangeFilter = {
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
export interface Temperature {
    value: number;
    unit: 'celsius' | 'fahrenheit';
}
export interface Pressure {
    value: number;
    unit: 'millimetersOfMercury';
}
export interface PressureResult {
    inMillimetersOfMercury: number;
}
export interface Mass {
    value: number;
    unit: 'grams' | 'kilograms' | 'milligrams' | 'micrograms' | 'ounces' | 'pounds';
}
export interface MassResult {
    inGrams: number;
    inKilograms: number;
    inMilligrams: number;
    inMicrograms: number;
    inOunces: number;
    inPounds: number;
}
interface BaseSample {
    time: string;
}
export interface HeartRateSample extends BaseSample {
    beatsPerMinute: number;
}
export interface SleepStage {
    startTime: string;
    endTime: string;
    stage: number;
}
export type HealthUnit = 'celsius' | 'fahrenheit' | 'mmhg' | 'kg' | 'pound';
export {};
//# sourceMappingURL=base.types.d.ts.map