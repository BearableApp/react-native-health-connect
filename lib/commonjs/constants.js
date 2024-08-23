"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.SleepStageType = exports.SdkAvailabilityStatus = exports.ResultRecordTypes = exports.RecordTypes = exports.BloodPressureMeasurementLocation = exports.BloodPressureBodyPosition = void 0;
const SdkAvailabilityStatus = {
  SDK_UNAVAILABLE: 1,
  SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED: 2,
  SDK_AVAILABLE: 3
};
exports.SdkAvailabilityStatus = SdkAvailabilityStatus;
const BloodPressureBodyPosition = {
  UNKNOWN: 0,
  STANDING_UP: 1,
  SITTING_DOWN: 2,
  LYING_DOWN: 3,
  RECLINING: 4
};
exports.BloodPressureBodyPosition = BloodPressureBodyPosition;
const BloodPressureMeasurementLocation = {
  UNKNOWN: 0,
  LEFT_WRIST: 1,
  RIGHT_WRIST: 2,
  LEFT_UPPER_ARM: 3,
  RIGHT_UPPER_ARM: 4
};
exports.BloodPressureMeasurementLocation = BloodPressureMeasurementLocation;
const SleepStageType = {
  UNKNOWN: 0,
  AWAKE: 1,
  SLEEPING: 2,
  OUT_OF_BED: 3,
  LIGHT: 4,
  DEEP: 5,
  REM: 6
};
exports.SleepStageType = SleepStageType;
const RecordTypes = {
  BLOOD_PRESSURE: 'BloodPressure',
  BODY_TEMPERATURE: 'BodyTemperature',
  HEART_RATE: 'HeartRate',
  RESTING_HEART_RATE: 'RestingHeartRate',
  STEPS: 'Steps',
  HEART_RATE_VARIABILITY: 'HeartRateVariabilityRmssd',
  WEIGHT: 'Weight',
  SLEEP: 'SleepSession'
};
exports.RecordTypes = RecordTypes;
const ResultRecordTypes = {
  BODY_TEMPERATURE: 'BODY_TEMPERATURE',
  HEART_RATE_VARIABILITY: 'HEART_RATE_VARIABILITY',
  HEART: 'HEART',
  PRESSURE: 'PRESSURE',
  RESTING_HEART_RATE: 'RESTING_HEART_RATE',
  SLEEP: 'SLEEP',
  STEPS: 'STEPS',
  WEIGHT: 'WEIGHT'
};
exports.ResultRecordTypes = ResultRecordTypes;
//# sourceMappingURL=constants.js.map