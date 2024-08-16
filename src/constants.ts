export const SdkAvailabilityStatus = {
  SDK_UNAVAILABLE: 1,
  SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED: 2,
  SDK_AVAILABLE: 3,
} as const;

export const BloodPressureBodyPosition = {
  UNKNOWN: 0,
  STANDING_UP: 1,
  SITTING_DOWN: 2,
  LYING_DOWN: 3,
  RECLINING: 4,
} as const;

export const BloodPressureMeasurementLocation = {
  UNKNOWN: 0,
  LEFT_WRIST: 1,
  RIGHT_WRIST: 2,
  LEFT_UPPER_ARM: 3,
  RIGHT_UPPER_ARM: 4,
} as const;

export const SleepStageType = {
  UNKNOWN: 0,
  AWAKE: 1,
  SLEEPING: 2,
  OUT_OF_BED: 3,
  LIGHT: 4,
  DEEP: 5,
  REM: 6,
} as const;
