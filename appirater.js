var {RNAppirater} = require('react-native').NativeModules;

module.exports = {
  setAppId: function (appId) {
    return RNAppirater.setAppId(appId);
  },
  setDaysUntilPrompt: function (value) {
    return RNAppirater.setDaysUntilPrompt(value);
  },
  setUsesUntilPrompt: function (value) {
    return RNAppirater.setUsesUntilPrompt(value);
  },
  setSignificantEventsUntilPrompt: function (value) {
    return RNAppirater.setSignificantEventsUntilPrompt(value);
  },
  setTimeBeforeReminding: function (value) {
    return RNAppirater.setTimeBeforeReminding(value);
  },
  setDebug: function (debug) {
    return RNAppirater.setDebug(debug);
  },
  appLaunched: function () {
    return RNAppirater.appLaunched();
  },
  appEnteredForeground: function () {
    return RNAppirater.appEnteredForeground();
  },
  userDidSignificantEvent: function () {
    return RNAppirater.userDidSignificantEvent();
  },
}
