var exec = require('cordova/exec');

exports.showInterstitial = function (locationId, success, error) {
    exec(success, error, "adgeneration", "showInterstitial", [locationId]);
};
