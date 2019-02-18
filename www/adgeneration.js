var exec = require('cordova/exec');

var adgenerationExport = {};

// adgenerationExport.showVampAd = function (locationId, success, error) {
//     exec(success, error, "adgeneration", "showVampAd", [locationId]);
// };

adgenerationExport.setOptions =
    function (options, successCallback, failureCallback) {
        console.log("adgenerationExport.setOptions... invoked")
        if (typeof options === 'object'
            && typeof options.placementId === 'string'
            && options.placementId.length > 0) {
            exec(
                successCallback,
                failureCallback,
                'adgeneration',
                'setOptions',
                [options]
            );
        } else {
            if (typeof failureCallback === 'function') {
                failureCallback('options.placementId should be specified.')
            }
        }
    };

adgenerationExport.createVampView =
    function (options, successCallback, failureCallback) {
        if (typeof options === 'undefined' || options == null) options = {};
        exec(
            successCallback,
            failureCallback,
            'adgeneration',
            'createVampView',
            [options]
        );
    };

adgenerationExport.destroyVamp =
    function (options, successCallback, failureCallback) {
        if (typeof options === 'undefined' || options == null) options = {};
        exec(
            successCallback,
            failureCallback,
            'adgeneration',
            'destroyVamp',
            []
        );
    };

adgenerationExport.showVampAd =
    function (show, successCallback, failureCallback) {
        if (show === undefined) {
            show = true;
        }
        exec(
            successCallback,
            failureCallback,
            'adgeneration',
            'showVampAd',
            [show]
        );
    };

adgenerationExport.loadVamp =
    function (show, successCallback, failureCallback) {
        if (show === undefined) {
            show = true;
        }
        exec(
            successCallback,
            failureCallback,
            'adgeneration',
            'loadVamp',
            [show]
        );
    };
module.exports = adgenerationExport;