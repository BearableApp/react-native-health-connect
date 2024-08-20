"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.HealthConnectError = void 0;
class HealthConnectError extends Error {
  constructor(message, method) {
    super(`HealthConnect.${method}: ${message}`);
  }
}
exports.HealthConnectError = HealthConnectError;
//# sourceMappingURL=errors.js.map