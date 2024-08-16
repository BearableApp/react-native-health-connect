import type {
  InstantaneousRecord,
  IntervalRecord,
  Pressure,
  Temperature,
  TimeRangeFilter,
  Mass,
  HeartRateSample,
  SleepStage,
} from './base.types';

export interface BloodPressureRecord extends InstantaneousRecord {
  recordType: 'BloodPressure';
  systolic: Pressure;
  diastolic: Pressure;
  // Use BloodPressureBodyPosition constant
  bodyPosition: number;
  // Use BloodPressureMeasurementLocation constant
  measurementLocation: number;
}

export interface BodyTemperatureRecord extends InstantaneousRecord {
  recordType: 'BodyTemperature';
  temperature: Temperature;
  // Use TemperatureMeasurementLocation constant
  measurementLocation?: number;
}

export interface HeartRateRecord extends IntervalRecord {
  recordType: 'HeartRate';
  samples: HeartRateSample[];
}

export interface RestingHeartRateRecord extends InstantaneousRecord {
  recordType: 'RestingHeartRate';
  beatsPerMinute: number;
}

export interface StepsRecord extends IntervalRecord {
  recordType: 'Steps';
  count: number;
}

export interface HeartRateVariabilityRmssdRecord extends InstantaneousRecord {
  recordType: 'HeartRateVariabilityRmssd';
  heartRateVariabilityMillis: number;
}

export interface WeightRecord extends InstantaneousRecord {
  recordType: 'Weight';
  weight: Mass;
}

export interface SleepSessionRecord extends IntervalRecord {
  recordType: 'SleepSession';
  stages?: SleepStage[];
  title?: string;
  notes?: string;
}

export type HealthConnectRecord =
  | BloodPressureRecord
  | BodyTemperatureRecord
  | HeartRateRecord
  | RestingHeartRateRecord
  | StepsRecord
  | HeartRateVariabilityRmssdRecord
  | WeightRecord
  | SleepSessionRecord;

export type RecordType = HealthConnectRecord['recordType'];

export interface ReadRecordsOptions {
  timeRangeFilter: TimeRangeFilter;
  dataOriginFilter?: string[];
  ascendingOrder?: boolean;
  pageSize?: number;
  pageToken?: string;
}
