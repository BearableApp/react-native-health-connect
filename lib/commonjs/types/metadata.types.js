"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.RecordingMethod = exports.DeviceType = void 0;
let DeviceType;
exports.DeviceType = DeviceType;
(function (DeviceType) {
  DeviceType[DeviceType["TYPE_UNKNOWN"] = 0] = "TYPE_UNKNOWN";
  DeviceType[DeviceType["TYPE_PHONE"] = 2] = "TYPE_PHONE";
  DeviceType[DeviceType["TYPE_SCALE"] = 3] = "TYPE_SCALE";
  DeviceType[DeviceType["TYPE_RING"] = 4] = "TYPE_RING";
  DeviceType[DeviceType["TYPE_HEAD_MOUNTED"] = 5] = "TYPE_HEAD_MOUNTED";
  DeviceType[DeviceType["TYPE_FITNESS_BAND"] = 6] = "TYPE_FITNESS_BAND";
  DeviceType[DeviceType["TYPE_CHEST_STRAP"] = 7] = "TYPE_CHEST_STRAP";
  DeviceType[DeviceType["TYPE_SMART_DISPLAY"] = 8] = "TYPE_SMART_DISPLAY";
})(DeviceType || (exports.DeviceType = DeviceType = {}));
let RecordingMethod;
exports.RecordingMethod = RecordingMethod;
(function (RecordingMethod) {
  RecordingMethod[RecordingMethod["RECORDING_METHOD_UNKNOWN"] = 0] = "RECORDING_METHOD_UNKNOWN";
  RecordingMethod[RecordingMethod["RECORDING_METHOD_ACTIVELY_RECORDED"] = 1] = "RECORDING_METHOD_ACTIVELY_RECORDED";
  RecordingMethod[RecordingMethod["RECORDING_METHOD_AUTOMATICALLY_RECORDED"] = 2] = "RECORDING_METHOD_AUTOMATICALLY_RECORDED";
  RecordingMethod[RecordingMethod["RECORDING_METHOD_MANUAL_ENTRY"] = 3] = "RECORDING_METHOD_MANUAL_ENTRY";
})(RecordingMethod || (exports.RecordingMethod = RecordingMethod = {}));
//# sourceMappingURL=metadata.types.js.map