package com.smartidea.plugin;

import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.lang.ref.WeakReference;

import jp.supership.vamp.AdvancedListener;
import jp.supership.vamp.VAMP;
import jp.supership.vamp.VAMPError;
import jp.supership.vamp.VAMPListener;

/**
 * This class represents the native implementation for the AdGeneration Cordova plugin.
 * This plugin can be used to request AdGeneration ads natively via the Google AdGeneration SDK.
 * The Google AdGeneration SDK is a dependency for this plugin.
 */
public class AdGeneration extends CordovaPlugin {

    private static final String EMPTY = "";

    /**
     * Common tag used for logging statements.
     */
    private static final String LOGTAG = "AdGeneration";
    private static final String DEFAULT_PLACEMENT_ID = EMPTY;

    /**
     * Cordova Actions.
     */
    private static final String ACTION_SET_OPTIONS = "setOptions";

    private static final String ACTION_CREATE_VAMP_VIEW = "createVampView";
    private static final String ACTION_DESTROY_VAMP = "destroyVamp";
    private static final String ACTION_LOAD_VAMP = "loadVamp";
    private static final String ACTION_SHOW_VAMP_AD = "showVampAd";

    /* options */
    private static final String OPT_PLACEMENT_ID = "placementId";
    private static final String OPT_AUTO_SHOW = "autoShow";
    private static final String OPT_TEST_MODE = "testMode";
    private static final String OPT_DEBUG_MODE = "debugMode";

    private VAMP vamp;

    private String placementId = DEFAULT_PLACEMENT_ID;
    private boolean autoShow = true;
    private boolean isTestMode = false;
    private boolean isDebugMode = false;
    private boolean isComplete = false;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    /**
     * This is the main method for the AdGeneration plugin.  All API calls go through here.
     * This method determines the action, and executes the appropriate call.
     *
     * @param action          The action that the plugin should execute.
     * @param inputs          The input parameters for the action.
     * @param callbackContext The callback context.
     * @return A PluginResult representing the result of the provided action.  A
     * status of INVALID_ACTION is returned if the action is not recognized.
     */
    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;
        if (ACTION_SET_OPTIONS.equals(action)) {
            JSONObject options = inputs.optJSONObject(0);
            result = executeSetOptions(options, callbackContext);
        } else if (ACTION_CREATE_VAMP_VIEW.equals(action)) {
            JSONObject options = (inputs.length() > 0) ? inputs.optJSONObject(0) : null;
            result = executeCreateVampView(options, callbackContext);
        } else if (ACTION_DESTROY_VAMP.equals(action)) {
            result = executeDestroyVamp(null, callbackContext);
        } else if (ACTION_LOAD_VAMP.equals(action)) {
            result = executeLoadVamp(callbackContext);
        } else if (ACTION_SHOW_VAMP_AD.equals(action)) {
            JSONObject options = (inputs.length() > 0) ? inputs.optJSONObject(0) : null;
            result = executeShowVampAd(options, callbackContext);
        }

        if (result != null) callbackContext.sendPluginResult(result);

        return true;
    }

    private void createVamp() {
        if (isDebugMode) Log.w(LOGTAG, "createVamp");
        vamp = VAMP.getVampInstance(cordova.getActivity(), placementId);
        vamp.setVAMPListener(new AdListener(vamp));      // VAMPListenerをセット
        vamp.setAdvancedListener(new AdvListener()); // AdvancedListenerをセット

        VAMP.setTestMode(isTestMode);  // テストモードを有効にする
        VAMP.setDebugMode(isDebugMode); // デバッグモードを有効にする

        // 広告取得
        vamp.load();
    }

    private PluginResult executeSetOptions(JSONObject options, CallbackContext callbackContext) {
        if (isDebugMode) Log.w(LOGTAG, "executeSetOptions");

        this.setOptions(options);

        callbackContext.success();
        return null;
    }

    private void setOptions(JSONObject options) {
        if (options == null) return;

        if (options.has(OPT_PLACEMENT_ID)) this.placementId = options.optString(OPT_PLACEMENT_ID);
        if (options.has(OPT_AUTO_SHOW)) this.autoShow = options.optBoolean(OPT_AUTO_SHOW);
        if (options.has(OPT_TEST_MODE)) this.isTestMode = options.optBoolean(OPT_TEST_MODE);
        if (options.has(OPT_DEBUG_MODE)) this.isDebugMode = options.optBoolean(OPT_DEBUG_MODE);
    }

    /**
     * Parses the create vamp view input parameters and runs the create vamp
     * view action on the UI thread.  If this request is successful, the developer
     * should make the requestAd call to request an ad for the banner.
     *
     * @param options The JSONArray representing input parameters.  This function
     *                expects the first object in the array to be a JSONObject with the
     *                input parameters.
     * @return A PluginResult representing whether or not the banner was created
     * successfully.
     */
    private PluginResult executeCreateVampView(JSONObject options, CallbackContext callbackContext) {
        if (isDebugMode) Log.w(LOGTAG, "executeCreateVampView");
        final CallbackContext delayCallback = callbackContext;
        this.setOptions(options);

        if (placementId == null || placementId.equals(EMPTY)) {
            if (isDebugMode) Log.e(LOGTAG, "Please put your placement id into the javascript code. No ad to display.");
            return null;
        }
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (vamp == null) {
                    createVamp();
                }
                if (autoShow) {
                    executeShowVampAd(null, null);
                }
                if (delayCallback != null)
                    delayCallback.success();
            }
        });

        return null;
    }

    private PluginResult executeLoadVamp(CallbackContext callbackContext) {
        if (isDebugMode) Log.w(LOGTAG, "executeLoadVamp");
        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (vamp == null) {
                    createVamp();
                } {
                    vamp.load();
                }
                if (delayCallback != null)
                    delayCallback.success();
            }
        });

        return null;
    }


    /**
     * Parses the create interstitial view input parameters and runs the create interstitial
     * view action on the UI thread.  If this request is successful, the developer
     * should make the requestAd call to request an ad for the banner.
     *
     * @param options The JSONArray representing input parameters.  This function
     *                expects the first object in the array to be a JSONObject with the
     *                input parameters.
     * @return A PluginResult representing whether or not the banner was created
     * successfully.
     */
    private PluginResult executeDestroyVamp(JSONObject options, CallbackContext callbackContext) {
        if (isDebugMode) Log.w(LOGTAG, "executeDestroyVamp");
        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (vamp != null) {
                    vamp.setVAMPListener(null);      // VAMPListenerをセット
                    vamp.setAdvancedListener(null);
                    vamp = null;
                }

                if (delayCallback != null)
                    delayCallback.success();

            }
        });
        return null;
    }

    private PluginResult executeShowVampAd(JSONObject options, CallbackContext callbackContext) {
        if (isDebugMode) Log.w(LOGTAG, "executeShowVampAd");
        this.setOptions(options);

        if (vamp == null) {
            this.createVamp();
        } else {
            vamp.load();
        }

        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (vamp != null && vamp.isReady()) {
                    boolean show = vamp.show();
                    if (isDebugMode) Log.w(LOGTAG, "executeShowVampAd ... show: " + show);
                }
                delayCallback.success();
            }
        });

        return null;
    }

    private void fireEvent(final String eventName, final String jsonStr) {
        cordova.getActivity().runOnUiThread(() -> {
            if (isDebugMode) Log.d(LOGTAG, "fireEvent... eventName: " + eventName + " -- jsonStr: " + jsonStr);
            if (jsonStr != null && jsonStr.length() > 0) {
                webView.loadUrl(String.format("javascript:cordova.fireDocumentEvent('%s', %s);", eventName, jsonStr));
            } else {
                webView.loadUrl(String.format("javascript:cordova.fireDocumentEvent('%s');", eventName));
            }
        });
    }

    @Override
    public void onDestroy() {
        if (vamp != null) {
            vamp.setVAMPListener(null);      // VAMPListenerをセット
            vamp.setAdvancedListener(null);
            vamp = null;
        }
        super.onDestroy();
    }

    private class AdListener implements VAMPListener {
        WeakReference<VAMP> _vamp;

        public AdListener(VAMP vamp) {
            _vamp = new WeakReference<>(vamp);
        }

        @Override
        public void onReceive(String placementId, String adnwName) {
            // 広告表示の準備完了
            if (isDebugMode) Log.d(LOGTAG, "onReceive(" + adnwName + ")");
            // 広告表示
            fireEvent("vampDidReceive", null);
            if (_vamp.get() != null && _vamp.get().isReady()) _vamp.get().show();
        }

        @Override
        public void onFail(String placementId, VAMPError error) {
            // 広告準備 or 表示失敗
            // このメソッドは廃止予定です.
            // 代わりにonFailedToLoadおよびonFailedToShowメソッドを使用してください
            if (isDebugMode) Log.e(LOGTAG, "onFail() " + error);
            fireEvent("vampDidFail", null);
        }

        @Override
        public void onFailedToLoad(VAMPError error, String placementId) {
            // 広告準備に失敗
            if (isDebugMode) Log.e(LOGTAG, "onFailedToLoad() " + error);
            String jsonStr = String.format("{ 'error': '%s' }", error.toString());
            fireEvent("didFailToLoadWithError", jsonStr);
            if (_vamp.get() != null) {
                _vamp.get().load();
            }
        }

        @Override
        public void onFailedToShow(VAMPError error, String placementId) {
            // 動画の表示に失敗
            if (isDebugMode) Log.e(LOGTAG, "onFailedToShow() " + error);
            String jsonStr = String.format("{ 'error': '%s' }", error.toString());
            fireEvent("didFailToLoadWithError", jsonStr);
            if (_vamp.get() != null) {
                _vamp.get().load();
            }
        }

        @Override
        public void onComplete(String placementId, String adnwName) {
            // 動画再生正常終了（インセンティブ付与可能）
            if (isDebugMode) Log.d(LOGTAG, "onComplete(" + adnwName + ")");
            isComplete = true;
            fireEvent("vampDidComplete", null);
        }

        @Override
        public void onClose(String placementId, String adnwName) {
            if (isDebugMode) Log.d(LOGTAG, "onClose(" + adnwName + ")");
            // 動画プレーヤーやエンドカードが表示終了
            // ＜注意：ユーザキャンセルなども含むので、インセンティブ付与はonCompleteで判定すること＞
            String jsonStr = String.format("{ 'adnwName': '%s', 'complete': %b }", adnwName, isComplete);
            fireEvent("vampDidClose", jsonStr);
        }

        @Override
        public void onExpired(String placementId) {
            // 有効期限オーバー
            // ＜注意：onReceiveを受けてからの有効期限が切れました。showするには再度loadを行う必要が有ります＞
            if (isDebugMode) Log.d(LOGTAG, "onExpired()");
            fireEvent("vampDidExpired", null);
        }
    }

    private class AdvListener implements AdvancedListener {
        @Override
        public void onLoadStart(String placementId, String adnwName) {
            // 優先順位順にアドネットワークごとの広告取得を開始
            if (isDebugMode) Log.d(LOGTAG, "onLoadStart(" + adnwName + ")");
            fireEvent("vampLoadStart", null);
        }

        @Override
        public void onLoadResult(String placementId, boolean success, String adnwName, String message) {
            // アドネットワークごとの広告取得結果
            if (isDebugMode) Log.d(LOGTAG, "onLoadResult(" + adnwName + ") " + message);
            String jsonStr = String.format("{ 'adnwName': '%s', 'success': %b }", adnwName, success);
            fireEvent("vampLoadResult", jsonStr);
        }
    }
}
