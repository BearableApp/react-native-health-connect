export class HealthConnectError extends Error {
  constructor(message, method) {
    super(`HealthConnect.${method}: ${message}`);
  }
}
//# sourceMappingURL=errors.js.map