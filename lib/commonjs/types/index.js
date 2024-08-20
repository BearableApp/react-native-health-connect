"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
var _records = require("./records.types");
Object.keys(_records).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (key in exports && exports[key] === _records[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _records[key];
    }
  });
});
var _results = require("./results.types");
Object.keys(_results).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (key in exports && exports[key] === _results[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _results[key];
    }
  });
});
var _aggregate = require("./aggregate.types");
Object.keys(_aggregate).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (key in exports && exports[key] === _aggregate[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _aggregate[key];
    }
  });
});
var _changes = require("./changes.types");
Object.keys(_changes).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (key in exports && exports[key] === _changes[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _changes[key];
    }
  });
});
var _metadata = require("./metadata.types");
Object.keys(_metadata).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (key in exports && exports[key] === _metadata[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _metadata[key];
    }
  });
});
//# sourceMappingURL=index.js.map