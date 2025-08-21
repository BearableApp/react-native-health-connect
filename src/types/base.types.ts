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

export type TimeRangeFilter =
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

// Duration is a fixed length of time in Java (daylight savings are ignored for DAYS)
export interface DurationRangeSlicer {
  duration: 'MILLIS' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS';
  length: number;
}

// Period is date-based amount of time in Java
export interface PeriodRangeSlicer {
  period: 'DAYS' | 'WEEKS' | 'MONTHS' | 'YEARS';
  length: number;
}

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
  unit:
    | 'grams'
    | 'kilograms'
    | 'milligrams'
    | 'micrograms'
    | 'ounces'
    | 'pounds';
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
  // Heart beats per minute. Validation range: 1-300.
  beatsPerMinute: number;
}

export interface SleepStage {
  startTime: string;
  endTime: string;
  // Use SleepStageType constant
  stage: number;
}
