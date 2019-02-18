package com.rjfun.cordova.plugin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

    /** Common tag used for logging statements. */
    private static final String LOGTAG = "AdGeneration";
    private static final String DEFAULT_PLACEMENT_ID = EMPTY;

    private static final boolean CORDOVA_MIN_4 = Integer.valueOf(CordovaWebView.CORDOVA_VERSION.split("\\.")[0]) >= 4;

    /** Cordova Actions. */
    private static final String ACTION_SET_OPTIONS = "setOptions";

    private static final String ACTION_CREATE_VAMP_VIEW = "createVampView";
    private static final String ACTION_DESTROY_VAMP= "destroyVamp";
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

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    /**
     * This is the main method for the AdGeneration plugin.  All API calls go through here.
     * This method determines the action, and executes the appropriate call.
     *
     * @param action The action that the plugin should execute.
     * @param inputs The input parameters for the action.
     * @param callbackContext The callback context.
     * @return A PluginResult representing the result of the provided action.  A
     *         status of INVALID_ACTION is returned if the action is not recognized.
     */
    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;
// private static final String ACTION_CREATE_VAMP_VIEW = "createVampView";
//     private static final String ACTION_DESTROY_VAMP= "destroyVamp";
//     private static final String ACTION_LOAD_VAMP = "loadVamp";
//     private static final String ACTION_SHOW_VAMP_AD = "showVampAd";
        if (ACTION_SET_OPTIONS.equals(action)) {
            JSONObject options = inputs.optJSONObject(0);
            result = executeSetOptions(options, callbackContext);
        } else if (ACTION_CREATE_VAMP_VIEW.equals(action)) {
            JSONObject options = (inputs.length > 0) ? inputs.optJSONObject(0) : null;
            result = executeCreateVampView(options, callbackContext);
        } else if (ACTION_DESTROY_VAMP.equals(action)) {
            result = executeDestroyVamp(options, callbackContext);
        } else if (ACTION_LOAD_VAMP.equals(action)) {
            result = executeLoadVamp( callbackContext);
        } else if (ACTION_SHOW_VAMP_AD.equals(action)) {
            JSONObject options = (inputs.length > 0) ? inputs.optJSONObject(0) : null;
            result = executeShowVampAd(options, callbackContext);
        }

        if(result != null) callbackContext.sendPluginResult( result );

        return true;
    }

    private void createVamp() {
        vamp = VAMP.getVampInstance(this, placementId);
        vamp.setVAMPListener(new AdListener());      // VAMPListenerをセット
        vamp.setAdvancedListener(new AdvListener()); // AdvancedListenerをセット

        VAMP.setTestMode(isTestMode);  // テストモードを有効にする
        VAMP.setDebugMode(isDebugMode); // デバッグモードを有効にする

        // 広告取得
        vamp.load();
    }

    private PluginResult executeSetOptions(JSONObject options, CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeSetOptions");

        this.setOptions( options );

        callbackContext.success();
        return null;
    }

    private void setOptions( JSONObject options ) {
        if(options == null) return;

        if(options.has(OPT_PLACEMENT_ID)) this.placementId = options.optString( OPT_PLACEMENT_ID );
        if(options.has(OPT_AUTO_SHOW)) this.autoShow  = options.optBoolean( OPT_AUTO_SHOW );
    }

    /**
     * Parses the create vamp view input parameters and runs the create vamp
     * view action on the UI thread.  If this request is successful, the developer
     * should make the requestAd call to request an ad for the banner.
     *
     * @param inputs The JSONArray representing input parameters.  This function
     *        expects the first object in the array to be a JSONObject with the
     *        input parameters.
     * @return A PluginResult representing whether or not the banner was created
     *         successfully.
     */
    private PluginResult executeCreateVampView(JSONObject options, CallbackContext callbackContext) {
        final CallbackContext delayCallback = callbackContext;
        this.setOptions( options );

        if(placementId==null || placementId.equals(EMPTY)){
            Log.e(LOGTAG, "Please put your placement id into the javascript code. No ad to display.");
            return null;
        }
        cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                if(vamp == null) {
                    this.createVamp();
                }
                if(autoShow) {
                    executeShowAd(true, null);
                }
                if(delayCallback!=null)
                  delayCallback.success();
            }
        });

        return null;
    }

    private PluginResult executeLoadVamp(CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeLoadVamp");
        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(vamp == null) {
                    this.createVamp();
                } else {
                    vamp.load();
                }
                if(delayCallback!=null)
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
     * @param inputs The JSONArray representing input parameters.  This function
     *        expects the first object in the array to be a JSONObject with the
     *        input parameters.
     * @return A PluginResult representing whether or not the banner was created
     *         successfully.
     */
    private PluginResult executeDestroyVamp(JSONObject options, CallbackContext callbackContext) {
        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                if (vamp != null) {
                    vamp.setVAMPListener(null);      // VAMPListenerをセット
                    vamp.setAdvancedListener(null);
                    vamp = null;
                }

                if(delayCallback!=null)
                  delayCallback.success();

            }
        });
        return null;
    }

    private PluginResult executeShowVampAd(JSONObject options, CallbackContext callbackContext) {
        this.setOptions( options );

        if(vamp == null) {
            callbackContext.error("vamp ad is null, call createVampView first");
            return null;
        }

        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (vamp != null) {
                    vamp.show();
                }
                delayCallback.success();
            }
        });

        return null;
    }

//    @Override
//    public void onPause(boolean multitasking) {
//        if (adView != null) {
//            adView.pause();
//        }
//        super.onPause(multitasking);
//    }
//
//    @Override
//    public void onResume(boolean multitasking) {
//        super.onResume(multitasking);
//        isGpsAvailable = (GooglePlayServicesUtil.isGooglePlayServicesAvailable(cordova.getActivity()) == ConnectionResult.SUCCESS);
//        if (adView != null) {
//            adView.resume();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//            adView = null;
//        }
//        if (adViewLayout != null) {
//            ViewGroup parentView = (ViewGroup)adViewLayout.getParent();
//            if(parentView != null) {
//                parentView.removeView(adViewLayout);
//            }
//            adViewLayout = null;
//        }
//        super.onDestroy();
//    }

    private static class AdListener implements VAMPListener {
        @Override
        public void onReceive(String placementId, String adnwName) {
            // 広告表示の準備完了
            Log.d(LOGTAG, "onReceive(" + adnwName + ")");
            // 広告表示
            vamp.show();
        }

        @Override
        public void onFail(String placementId, VAMPError error) {
            // 広告準備 or 表示失敗
            // このメソッドは廃止予定です.
            // 代わりにonFailedToLoadおよびonFailedToShowメソッドを使用してください
        }

        @Override
        public void onFailedToLoad(VAMPError error, String placementId) {
            // 広告準備に失敗
            Log.e(LOGTAG, "onFailedToLoad() " + error);
        }

        @Override
        public void onFailedToShow(VAMPError error, String placementId) {
            // 動画の表示に失敗
            Log.e(LOGTAG, "onFailedToShow() " + error);
        }

        @Override
        public void onComplete(String placementId, String adnwName) {
            // 動画再生正常終了（インセンティブ付与可能）
            Log.d(LOGTAG, "onComplete(" + adnwName + ")");
        }

        @Override
        public void onClose(String placementId, String adnwName) {
            // 動画プレーヤーやエンドカードが表示終了
            // ＜注意：ユーザキャンセルなども含むので、インセンティブ付与はonCompleteで判定すること＞
            Log.d(LOGTAG, "onClose(" + adnwName + ")");
        }

        @Override
        public void onExpired(String placementId) {
            // 有効期限オーバー
            // ＜注意：onReceiveを受けてからの有効期限が切れました。showするには再度loadを行う必要が有ります＞
            Log.d(LOGTAG, "onExpired()");
        }
    }

    private static class AdvListener implements AdvancedListener {
        @Override
        public void onLoadStart(String placementId, String adnwName) {
            // 優先順位順にアドネットワークごとの広告取得を開始
            Log.d(LOGTAG, "onLoadStart(" + adnwName + ")");
        }

        @Override
        public void onLoadResult(String placementId, boolean success, String adnwName, String message) {
            // アドネットワークごとの広告取得結果
            Log.d(LOGTAG, "onLoadResult(" + adnwName + ") " + message);
        }
    }
}
