---
title: readBucketedRecords
---

# `readBucketedRecords`

Retrieves a collection of records for a record type and buckets it into intervals from the start date provided.

## Available record types

| Record Type            | Value Format                       | Units (default first)     |
| ---------------------- | ---------------------------------- | ------------------------- |
| STEPS                  | `'{total}'`                        | `count`                   |
| WEIGHT                 | `'{latest}'`                       | `kg` or `pound`           |
| HEART                  | `'{min}/{avg}/{max}'`              | `bpm`                     |
| BLOOD_PRESSURE         | `'{systolic_avg}/{diastolic_avg}'` | `mmhg`                    |
| RESTING_HEART_RATE     | `'{avg}'`                          | `bpm`                     |
| BODY_TEMPERATURE       | `'{avg}'`                          | `celsius` or `fahrenheit` |
| HEART_RATE_VARIABILITY | `'{avg}'`                          | `ms`                      |
| SLEEP                  | `'{hours}:{mins}'` (see example)   |                           |

# Method

```ts
function readBucketedRecords<T extends RecordType>(
  // record type e.g activeCaloriesBurned
  recordType: T,

  // read options such as time range filter, data origin filter, ordering and pagination
  options: ReadRecordsOptions
): Promise<ReadRecordsResult<T>>;
```

# Example - Weight

```ts
import { RecordTypes, readBucketedRecords } from 'react-native-health-connect';

const readSampleData = () => {
  readBucketedRecords(RecordTypes.WEIGHT, {
    timeRangeFilter: {
      operator: 'between',
      startTime: '2023-01-09T12:00:00.405Z', // Local time
      endTime: '2023-01-09T23:53:15.405Z', // Local time
    },
    unit: 'pound',
  }).then(({ records }) => {
    console.log('Retrieved records: ', JSON.stringify({ records }, null, 2));
    // Retrieved records:
    // [
    //   {
    //     dateKey: '20240101',
    //     entry: {
    //       type: 'WEIGHT',
    //       value: '134', // See table above for expected value format and units
    //       family: 'HEALTH',
    //     },
    //   },
    // ]
  });
};
```

# Example - Sleep

```ts
import { RecordTypes, readBucketedRecords } from 'react-native-health-connect';

const readSampleData = () => {
  readBucketedRecords(RecordTypes.SLEEP, {
    timeRangeFilter: {
      operator: 'between',
      startTime: '2023-01-09T12:00:00.405Z', // Local time
      endTime: '2023-01-09T23:53:15.405Z', // Local time
    },
  }).then(({ records }) => {
    console.log('Retrieved records: ', JSON.stringify({ records }, null, 2));
    // Retrieved records:
    // [
    //   {
    //     dateKey: '20240101',
    //     entry: {
    //       type: 'SLEEP',
    //       value: '7:30',
    //       family: 'HEALTH',
    //       sleepTimes: {
    //         fellAsleepAt: '2024-01-01 00:30:00.000',
    //         wokeUpAt: '2024-01-01 08:00:00.000',
    //       }
    //       timesInBed: {
    //         inBedAt: '2023-12-31 23:55:00.000',
    //         outOfBedAt: '2024-01-01 09:30:00.000',
    //       },
    //     },
    //   },
    // ]
  });
};
```

# Adding a New Record Type

The `readBucketedRecords` can be extended easily to add new record types. These record types should correspond to a [health connect record type](https://developer.android.com/reference/kotlin/androidx/health/connect/client/records/package-summary?_gl=1*bfr5fs*_up*MQ..*_ga*MTI2MDY1NDg3Ni4xNzI5MDc1OTYy*_ga_6HH9YJMN9M*MTcyOTA3NTk2Mi4xLjAuMTcyOTA3NTk2Mi4wLjAuODkyODEzMzY1).

1. Update the `RecordTypes` with the new health type in _src/constants.ts_
2. Create a new record type in _src/types/records.types.ts_
3. Create a new record class called `React{HealthType}Record.kt`. Make sure it extends the _ReactHealthRecordImpl.kt_ class with a corresponding health kit record type and implements the following functions.
   1. `getResultType` - this should return the health care type used in bearable
   2. `parseRecord` - this should return a react native object based on a health kit record type
   3. `getAggregateRequest` - this should return a request for aggregating the record, if you don't need this functionality throw an aggregation not supported error.
   4. `parseAggregationResult` - this should return a react native object based on the aggregated result, if you don't need this functionality throw an aggregation not supported error.
   5. `getBucketedRequest` - this should return the `AggregateGroupByDurationRequest` based on the record type, if no aggregates are supported via health connect throw an aggregation not supported error and use the `parseManuallyBucketedResult` function instead.
   6. `parseBucketedResult` - this should return a react native array with objects formatted correct, use the `formatRecord` helper function for this.
   7. `parseManuallyBucketedResult` - this function can be used to manually bucket all records if the record type doesn't support any aggregates. If it's not needed then throw an aggregation not supported error.
