import * as React from 'react';
import moment from 'moment';

import { Alert, Button, ScrollView, StyleSheet, Text } from 'react-native';
import {
  aggregateRecord,
  getGrantedPermissions,
  initialize,
  getSdkStatus,
  readRecords,
  requestPermission,
  revokeAllPermissions,
  SdkAvailabilityStatus,
  openHealthConnectSettings,
  openHealthConnectDataManagement,
  Permission,
  RecordType,
  AggregateResultRecordType,
  readBucketedRecords,
  HealthUnit,
} from 'react-native-health-connect';

const getLastWeekDate = (): Date => {
  return new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000);
};

const getLastTwoWeeksDate = (): Date => {
  return new Date(new Date().getTime() - 2 * 7 * 24 * 60 * 60 * 1000);
};

const getTodayDate = (): Date => {
  return new Date();
};

const availableRecordTypes: Permission[] = [
  {
    recordType: 'BloodPressure',
    accessType: 'read',
  },
  {
    recordType: 'BodyTemperature',
    accessType: 'read',
  },
  {
    recordType: 'HeartRate',
    accessType: 'read',
  },
  {
    recordType: 'RestingHeartRate',
    accessType: 'read',
  },
  {
    recordType: 'Steps',
    accessType: 'read',
  },
  {
    recordType: 'HeartRateVariabilityRmssd',
    accessType: 'read',
  },
  {
    recordType: 'Weight',
    accessType: 'read',
  },
  {
    recordType: 'SleepSession',
    accessType: 'read',
  },
];

const availableAggregateRecordTypes: AggregateResultRecordType[] = [
  'BloodPressure',
  'HeartRate',
  'RestingHeartRate',
  'Steps',
  'Weight',
  'SleepSession',
];

const availableBucketedTypes: { type: RecordType; units?: HealthUnit }[] = [
  { type: 'Steps' },
  { type: 'HeartRate' },
  { type: 'RestingHeartRate' },
  { type: 'Weight', units: 'kg' },
  { type: 'Weight', units: 'pound' },
  { type: 'SleepSession' },
  { type: 'BodyTemperature', units: 'celsius' },
  { type: 'BodyTemperature', units: 'fahrenheit' },
  { type: 'HeartRateVariabilityRmssd' },
  { type: 'BloodPressure' },
];

export default function App() {
  const initializeHealthConnect = async () => {
    const result = await initialize();
    console.log({ result });
  };

  const checkAvailability = async () => {
    const status = await getSdkStatus();
    if (status === SdkAvailabilityStatus.SDK_AVAILABLE) {
      console.log('SDK is available');
    }

    if (status === SdkAvailabilityStatus.SDK_UNAVAILABLE) {
      console.log('SDK is not available');
    }

    if (
      status === SdkAvailabilityStatus.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
    ) {
      console.log('SDK is not available, provider update required');
    }
  };

  const readSampleData = (recordType: RecordType) => {
    readRecords(recordType, {
      timeRangeFilter: {
        operator: 'between',
        startTime: getLastTwoWeeksDate().toISOString(),
        endTime: getTodayDate().toISOString(),
      },
    })
      .then((result) => {
        console.log('Retrieved records: ', JSON.stringify({ result }, null, 2));
      })
      .catch((err) => {
        console.error('Error reading records ', { err });
      });
  };

  const aggregateSampleData = (recordType: AggregateResultRecordType) => {
    aggregateRecord({
      recordType,
      timeRangeFilter: {
        operator: 'between',
        startTime: getLastWeekDate().toISOString(),
        endTime: getTodayDate().toISOString(),
      },
    }).then((result) => {
      console.log('Aggregated record: ', { result });
    });
  };

  const requestSamplePermissions = () => {
    requestPermission(availableRecordTypes).then((permissions) => {
      console.log('Granted permissions on request ', { permissions });
    });
  };

  const grantedPermissions = () => {
    getGrantedPermissions().then((permissions) => {
      console.log('Granted permissions ', { permissions });
    });
  };

  const getBucketedRecords = async (
    recordType: RecordType,
    unit?: HealthUnit
  ) => {
    try {
      const startTime = moment().subtract(1, 'week').startOf('day');
      const endTime = moment().endOf('day');

      const result = await readBucketedRecords(recordType, {
        timeRangeFilter: {
          operator: 'between',
          startTime:
            recordType === 'SleepSession'
              ? startTime.subtract(12, 'h').toISOString()
              : startTime.toISOString(),
          endTime:
            recordType === 'SleepSession'
              ? endTime.subtract(12, 'h').toISOString()
              : endTime.toISOString(),
        },
        unit,
      });

      console.log('result', result);
      Alert.alert(
        'Bucketed records',
        result.map((r) => `${r.dateKey} - ${r.entry.value}`).join('\n')
      );
    } catch (error) {
      console.log('error', error);
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text>Setup</Text>

      <Button title="Initialize" onPress={initializeHealthConnect} />
      <Button
        title="Open Health Connect settings"
        onPress={openHealthConnectSettings}
      />
      <Button
        title="Open Health Connect data management"
        onPress={() => openHealthConnectDataManagement()}
      />
      <Button title="Check availability" onPress={checkAvailability} />
      <Button
        title="Request sample permissions"
        onPress={requestSamplePermissions}
      />
      <Button title="Get granted permissions" onPress={grantedPermissions} />
      <Button title="Revoke all permissions" onPress={revokeAllPermissions} />

      <Text>Reading bucketed data</Text>

      {availableBucketedTypes.map(({ type, units }) => (
        <Button
          key={`${type}-${units}`}
          title={`Read bucketed ${type}${units ? `(${units})` : ''}`}
          onPress={() => getBucketedRecords(type, units)}
        />
      ))}

      <Text>Reading data</Text>

      {availableRecordTypes.map(({ recordType }) => (
        <Button
          key={recordType}
          title={`Read ${recordType} sample data`}
          onPress={() => readSampleData(recordType)}
        />
      ))}

      <Text>Reading aggregated data</Text>

      {availableAggregateRecordTypes.map((recordType) => (
        <Button
          key={recordType}
          title={`Aggregate ${recordType} sample data`}
          onPress={() => aggregateSampleData(recordType)}
        />
      ))}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingVertical: 40,
    alignItems: 'center',
    justifyContent: 'center',
    rowGap: 16,
  },
});
