"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.RecordingMethod = exports.DeviceType = void 0;
let DeviceType = exports.DeviceType = /*#__PURE__*/function (DeviceType) {
  DeviceType[DeviceType["TYPE_UNKNOWN"] = 0] = "TYPE_UNKNOWN";
  DeviceType[DeviceType["TYPE_PHONE"] = 2] = "TYPE_PHONE";
  DeviceType[DeviceType["TYPE_SCALE"] = 3] = "TYPE_SCALE";
  DeviceType[DeviceType["TYPE_RING"] = 4] = "TYPE_RING";
  DeviceType[DeviceType["TYPE_HEAD_MOUNTED"] = 5] = "TYPE_HEAD_MOUNTED";
  DeviceType[DeviceType["TYPE_FITNESS_BAND"] = 6] = "TYPE_FITNESS_BAND";
  DeviceType[DeviceType["TYPE_CHEST_STRAP"] = 7] = "TYPE_CHEST_STRAP";
  DeviceType[DeviceType["TYPE_SMART_DISPLAY"] = 8] = "TYPE_SMART_DISPLAY";
  return DeviceType;
}({});
let RecordingMethod = exports.RecordingMethod = /*#__PURE__*/function (RecordingMethod) {
  /** Unknown recording method. */
  RecordingMethod[RecordingMethod["RECORDING_METHOD_UNKNOWN"] = 0] = "RECORDING_METHOD_UNKNOWN";
  /**
   * For actively recorded data by the user.
   *
   * For e.g. An exercise session actively recorded by the user using a phone or a watch
   * device.
   */
  RecordingMethod[RecordingMethod["RECORDING_METHOD_ACTIVELY_RECORDED"] = 1] = "RECORDING_METHOD_ACTIVELY_RECORDED";
  /**
   * For passively recorded data by the app.
   *
   * For e.g. Steps data recorded by a watch or phone without the user starting a session.
   */
  RecordingMethod[RecordingMethod["RECORDING_METHOD_AUTOMATICALLY_RECORDED"] = 2] = "RECORDING_METHOD_AUTOMATICALLY_RECORDED";
  /**
   * For manually entered data by the user.
   *
   * For e.g. Nutrition or weight data entered by the user.
   */
  RecordingMethod[RecordingMethod["RECORDING_METHOD_MANUAL_ENTRY"] = 3] = "RECORDING_METHOD_MANUAL_ENTRY";
  return RecordingMethod;
}({});
//# sourceMappingURL=metadata.types.js.map