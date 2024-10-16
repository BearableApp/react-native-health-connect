---
sidebar_position: 1
title: Overview
---

## Available methods

| **Method**                      | **Description**                                                                                                                                                                                           |
| ------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| getSdkStatus                    | Determines whether an implementation of HealthConnectClient is available on the device at the moment. If none is available, apps may choose to redirect to package installers to find suitable providers. |
| initialize                      | Initialize the health connect client.                                                                                                                                                                     |
| openHealthConnectSettings       | Opens Health Connect app's settings app.                                                                                                                                                                  |
| openHealthConnectDataManagement | Opens Health Connect data management screen app.                                                                                                                                                          |
| requestPermission               | Request permission for specified record types and access types.                                                                                                                                           |
| getGrantedPermissions           | Returns a set of all health permissions granted by the user to the calling provider app.                                                                                                                  |
| revokeAllPermissions            | Revokes all previously granted permissions by the user to the calling app.                                                                                                                                |
| readRecords                     | Retrieves a collection of records.                                                                                                                                                                        |
| aggregateRecord                 | Reads aggregated results according to requested read criteria, for e.g, data origin filter and within a time range.                                                                                       |
| readBucketedRecords             | Reads records between a start and end time that are bucketed into days.                                                                                                                                   |
