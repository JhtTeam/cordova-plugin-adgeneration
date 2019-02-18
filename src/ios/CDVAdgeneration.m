#import "CDVAdgeneration.h"
#import <VAMP/VAMP.h>

#define OPT_PLACEMENT_ID    @"placementId"
#define OPT_AUTO_SHOW       @"autoShow"

@interface CDVAdgeneration () <VAMPDelegate>
@property (assign) BOOL autoShow;
@property (assign) BOOL isComplete;
@property (nonatomic, assign) NSString *placementId;
@property (nonatomic) VAMP *vamp;

- (void) setOptions:(CDVInvokedUrlCommand *)command;
- (void) createVampView:(CDVInvokedUrlCommand *)command;
- (void) destroyVamp:(CDVInvokedUrlCommand *)command;
- (void) showVampAd:(CDVInvokedUrlCommand *)command;
- (void) loadVamp:(CDVInvokedUrlCommand *)command;
@end


@implementation CDVAdgeneration

- (void) setOptions:(CDVInvokedUrlCommand *)command {
    NSLog(@"setOptions");
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    NSArray* args = command.arguments;
    NSUInteger argc = [args count];
    if( argc >= 1 ) {
        NSDictionary* options = [command argumentAtIndex:0 withDefault:[NSNull null]];
        [self __setOptions:options];
    }
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) createVampView:(CDVInvokedUrlCommand *)command {
    NSLog(@"createVampView");
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    NSArray* args = command.arguments;
    NSUInteger argc = [args count];
    if( argc >= 1 ) {
        NSDictionary* options = [command argumentAtIndex:0 withDefault:[NSNull null]];
        [self __setOptions:options];
    }
    if(!self.vamp) {
        [self __createVamp];
    }
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) destroyVamp:(CDVInvokedUrlCommand *)command {
    NSLog(@"destroyVamp");
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    if (self.vamp) {
        self.vamp.delegate = nil;
        [self.vamp setRootViewController:nil];
        self.vamp = nil;
    }
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) showVampAd:(CDVInvokedUrlCommand *)command {
    NSLog(@"showVampAd");
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    if(!self.vamp) {
        [self __createVamp];
    } else if (self.vamp.isReady) {
        [self.vamp show];
    }
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) loadVamp:(CDVInvokedUrlCommand *)command {
    NSLog(@"loadVamp");
    CDVPluginResult *pluginResult;
    NSString *callbackId = command.callbackId;
    if(!self.vamp) {
        [self __createVamp];
    } else {
        [self.vamp load];
    }
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void) __setOptions:(NSDictionary*) options {
    if ((NSNull *)options == [NSNull null]) return;
    NSString* str = nil;
    str = [options objectForKey:OPT_PLACEMENT_ID];
    if(str && [str length]>0) self.placementId = str;
    
    str = [options objectForKey:OPT_AUTO_SHOW];
    if(str) self.autoShow = [str boolValue];
}

- (void) __createVamp {
    self.vamp = [VAMP new];
    self.vamp.delegate = self;
    [self.vamp setPlacementId:self.placementId];
    [self.vamp setRootViewController:self.viewController];
    [self.vamp load];
}

- (void) fireEvent:(NSString *)obj event:(NSString *)eventName withData:(NSString *)jsonStr {
    NSString* js;
    if(obj && [obj isEqualToString:@"window"]) {
        js = [NSString stringWithFormat:@"var evt=document.createEvent(\"UIEvents\");evt.initUIEvent(\"%@\",true,false,window,0);window.dispatchEvent(evt);", eventName];
    } else if(jsonStr && [jsonStr length]>0) {
        js = [NSString stringWithFormat:@"javascript:cordova.fireDocumentEvent('%@',%@);", eventName, jsonStr];
    } else {
        js = [NSString stringWithFormat:@"javascript:cordova.fireDocumentEvent('%@');", eventName];
    }
    [self.commandDelegate evalJs:js];
}

- (void)addLogText:(NSString *)message {
    NSLog(@"%@", message);
}

#pragma mark - VAMPDelegate

// 広告取得完了
// 広告表示が可能になると通知されます
- (void)vampDidReceive:(NSString *)placementId adnwName:(NSString *)adnwName {
    [self addLogText:[NSString stringWithFormat:@"vampDidReceive(%@, %@)", adnwName, placementId]];
    [self fireEvent:@"" event:@"vampDidReceive" withData:nil];
    
    if (self.vamp && self.vamp.isReady) {
        [self.vamp show];
    }
}

// Deprecated 廃止予定
// 代わりにvamp:didFailToLoadWithError:withPlacementId:およびvamp:didFailToShowWithError:withPlacementId:メソッドを使用してください
- (void)vampDidFail:(NSString *)placementId error:(VAMPError *)error {
    NSLog(@"[VAMP]vampDidFail:error");
    NSString* jsonData = [NSString stringWithFormat:@"{ 'error': '%@' }", [error localizedFailureReason]];
    [self fireEvent:@"" event:@"vampDidFail" withData:jsonData];
}

// 広告取得失敗
// 広告が取得できなかったときに通知されます。
// 例）在庫が無い、タイムアウトなど
// @see https://github.com/AdGeneration/VAMP-iOS-SDK/wiki/VAMP-iOS-API-Errors
- (void)vamp:(VAMP *)vamp didFailToLoadWithError:(VAMPError *)error withPlacementId:(NSString *)placementId {
    [self addLogText:[NSString stringWithFormat:@"vampDidFailToLoad(%@, %@)", error.localizedDescription, placementId]];
    VAMPErrorCode code = error.code;
    if(code == VAMPErrorCodeNoAdStock) {
        // 在庫が無いので、再度loadをしてもらう必要があります。
        // 連続で発生する場合、時間を置いてからloadをする必要があります。
        NSLog(@"[VAMP]vampDidFailToLoad(VAMPErrorCodeNoAdStock, %@)", error.localizedDescription);
    } else if(code == VAMPErrorCodeNoAdnetwork) {
        // アドジェネ管理画面でアドネットワークの配信がONになっていない、
        // またはEU圏からのアクセスの場合(GDPR)に発生します。
        NSLog(@"[VAMP]vampDidFailToLoad(VAMPErrorCodeNoAdnetwork, %@)", error.localizedDescription);
    } else if(code == VAMPErrorCodeNeedConnection) {
        // ネットワークに接続できない状況です。
        // 電波状況をご確認ください。
        NSLog(@"[VAMP]vampDidFailToLoad(VAMPErrorCodeNeedConnection, %@)", error.localizedDescription);
    } else if(code == VAMPErrorCodeMediationTimeout) {
        // アドネットワークSDKから返答が得られず、タイムアウトしました。
        NSLog(@"[VAMP]vampDidFailToLoad(VAMPErrorCodeMediationTimeout, %@)", error.localizedDescription);
    }
    
    NSString* jsonData = [NSString stringWithFormat:@"{ 'error': '%@' }", [error localizedFailureReason]];
    [self fireEvent:@"" event:@"didFailToLoadWithError" withData:jsonData];
    
    if (self.vamp) {
        [self.vamp load];
    }
}

// 広告表示失敗
- (void)vamp:(VAMP *)vamp didFailToShowWithError:(VAMPError *)error withPlacementId:(NSString *)placementId {
    [self addLogText:[NSString stringWithFormat:@"vampDidFailToShow(%@, %@)",
                      error.localizedDescription, placementId]];
    if (error.code == VAMPErrorCodeUserCancel) {
        // ユーザが広告再生を途中でキャンセルしました。
        // AdMobは動画再生の途中でユーザーによるキャンセルが可能
        NSLog(@"[VAMP]vampDidFailToShow(VAMPErrorCodeUserCancel, %@)", error.localizedDescription);
    } else if(error.code == VAMPErrorCodeNotLoadedAd) {
        NSLog(@"[VAMP]vampDidFailToShow(VAMPErrorCodeNotLoadedAd, %@)", error.localizedDescription);
    }
    NSString* jsonData = [NSString stringWithFormat:@"{ 'error': '%@' }", [error localizedFailureReason]];
    [self fireEvent:@"" event:@"didFailToShowWithError" withData:jsonData];
    if (self.vamp) {
        [self.vamp load];
    }
}

// インセンティブ付与OK
// インセンティブ付与が可能になったタイミングで通知
// アドネットワークによって通知タイミングが異なる（動画再生完了時、またはエンドカードを閉じたタイミング）
- (void)vampDidComplete:(NSString *)placementId adnwName:(NSString *)adnwName {
    [self addLogText:[NSString stringWithFormat:@"vampDidComplete(%@, %@)", adnwName, placementId]];
    self.isComplete = YES;
    [self fireEvent:@"" event:@"vampDidComplete" withData:nil];
}

// 広告閉じる
// エンドカード閉じる、途中で広告再生キャンセル
- (void)vampDidClose:(NSString *)placementId adnwName:(NSString *)adnwName {
    [self addLogText:[NSString stringWithFormat:@"vampDidClose(%@, %@)", adnwName, placementId]];
    NSString* jsonData = [NSString stringWithFormat:@"{ 'adnwName': '%@', 'complete': %d }", adnwName, self.isComplete];
    [self fireEvent:@"" event:@"vampDidClose" withData:jsonData];
}

// 広告準備完了から55分経つと取得した広告の表示はできてもRTBの収益は発生しない
// この通知を受け取ったら、もう一度loadからやり直す必要あり
- (void)vampDidExpired:(NSString *)placementId {
    [self addLogText:[NSString stringWithFormat:@"vampDidExpired(%@)", placementId]];
    [self fireEvent:@"" event:@"vampDidExpired" withData:nil];
}

// アドネットワークの広告取得が開始されたときに通知
- (void)vampLoadStart:(NSString *)placementId adnwName:(NSString *)adnwName {
    [self addLogText:[NSString stringWithFormat:@"vampLoadStart(%@, %@)", adnwName, placementId]];
    NSString* jsonData = [NSString stringWithFormat:@"{ 'adnwName': '%@' }", adnwName];
    [self fireEvent:@"" event:@"vampLoadStart" withData:jsonData];
}

// アドネットワークの広告取得結果が通知されます。成功時はsuccess=YESとなりロード処理は終了
// success=NOのとき、次位のアドネットワークがある場合はロード処理は継続
- (void)vampLoadResult:(NSString *)placementId success:(BOOL)success adnwName:(NSString *)adnwName message:(NSString *)message {
    if (success) {
        [self addLogText:[NSString stringWithFormat:@"vampLoadResult(%@, %@, success:OK)", adnwName, placementId]];
    }
    else {
        [self addLogText:[NSString stringWithFormat:@"vampLoadResult(%@, %@, success:NG, %@)", adnwName, placementId, message]];
    }
    NSString* jsonData = [NSString stringWithFormat:@"{ 'adnwName': '%@', 'success': %d }", adnwName, success];
    [self fireEvent:@"" event:@"vampLoadStart" withData:jsonData];
}

// VAMPの状態が変化したときの通知されます
- (void)vampDidChangeState:(VAMPState)oldState intoState:(VAMPState)newState
           withPlacementId:(NSString *)placementId {
    
    //    NSString *oldStateStr = [self vampStateString:oldState];
    //    NSString *newStateStr = [self vampStateString:newState];
    //
    //    [self addLogText:[NSString stringWithFormat:@"vampDidChangeState(%@ -> %@, %@)",
    //                      oldStateStr, newStateStr, placementId]];
}

@end
