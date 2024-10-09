import * as React from 'react';
import moment from 'moment';

import { Button, ScrollView, StyleSheet, Text } from 'react-native';
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

  const getBucketedRecords = async (recordType: RecordType) => {
    try {
      // Want to keep offset on the iso string to account for timezones
      const startTime = moment()
        .subtract(1, 'week')
        .startOf('day')
        .toISOString();
      const endTime = moment().endOf('day').toISOString();

      const result = await readBucketedRecords(recordType, {
        timeRangeFilter: {
          operator: 'between',
          startTime,
          endTime,
        },
      });

      console.log('result', result);
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

      <Button
        title="Read bucketed steps"
        onPress={() => getBucketedRecords('Steps')}
      />

      {/* Not supported */}
      <Button
        title="Read bucketed heart rate"
        onPress={() => getBucketedRecords('HeartRate')}
      />

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
