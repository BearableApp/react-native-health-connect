import type {
  MassResult,
  PressureResult,
} from './base.types';
import type {
  BloodPressureRecord,
  BodyTemperatureRecord,
  HeartRateRecord,
  HeartRateVariabilityRmssdRecord,
  RecordType,
  RestingHeartRateRecord,
  SleepSessionRecord,
  StepsRecord,
  WeightRecord,
} from './records.types';

type Identity<T> = { [P in keyof T]: T[P] };

type Replace<T, K extends keyof T, TReplace> = Identity<
  Pick<T, Exclude<keyof T, K>> & {
    [P in K]: TReplace;
  }
>;

interface BloodPressureRecordResult
  extends Omit<BloodPressureRecord, 'systolic' | 'diastolic'> {
  systolic: PressureResult;
  diastolic: PressureResult;
}

interface BodyTemperatureRecordResult
  extends Replace<
    BodyTemperatureRecord,
    'temperature',
    {
      inFahrenheit: number;
      inCelsius: number;
    }
  > {}

interface HeartRateRecordResult extends HeartRateRecord {}

interface RestingHeartRateRecordResult extends RestingHeartRateRecord {}

interface StepsRecordResult extends StepsRecord {}

interface HeartRateVariabilityRmssdRecordResult
  extends HeartRateVariabilityRmssdRecord {}

interface WeightRecordResult
  extends Replace<WeightRecord, 'weight', MassResult> {}

interface SleepSessionRecordResult extends SleepSessionRecord {}

type HealthConnectRecordResult =
  | BloodPressureRecordResult
  | BodyTemperatureRecordResult
  | HeartRateRecordResult
  | RestingHeartRateRecordResult
  | StepsRecordResult
  | HeartRateVariabilityRmssdRecordResult
  | WeightRecordResult
  | SleepSessionRecordResult;

export type RecordResult<T extends RecordType> = Omit<
  Extract<HealthConnectRecordResult, { recordType: T }>,
  'recordType'
>;

export type ReadRecordsResult<T extends RecordType> = {
  records: RecordResult<T>[];
  pageToken?: string;
};
