#import <UIKit/UIKit.h>
#import "ADGAdWebView.h"
#import "ADGConstants.h"

@interface ADGManagerViewController : UIViewController <ADGAdWebViewDelegate>
@property (nonatomic, copy) NSString *locationid;
@property (nonatomic, weak) id delegate;
@property (nonatomic, weak) UIViewController *rootViewController;
@property (nonatomic, assign, setter=setAdType:) ADGAdType adType;
@property (nonatomic, assign, setter=setAdSize:) CGSize adSize;
@property (nonatomic, assign, setter=setAdOrigin:) CGPoint adOrigin;
@property (nonatomic, assign, setter=setAdScale:) float adScale;
@property (nonatomic, assign) BOOL adgAdView;
@property (nonatomic, assign) BOOL closeOriginInter;
@property (nonatomic, assign) BOOL preventAccidentClick;
@property (nonatomic, assign) BOOL usePartsResponse;
@property (nonatomic, assign) BOOL informationIconViewDefault;
@property (nonatomic, assign, setter=setWebViewScrollEnabled:, getter=isWebViewScrollEnabled) BOOL webViewScrollEnabled;
@property (nonatomic, assign) BOOL expandFrame;

// Internal property. Please don't update from app.
@property (nonatomic, assign) BOOL isInterstitial;

///Unavailable initializers
- (instancetype)initWithNibName:(nullable NSString *)nibNameOrNil bundle:(nullable NSBundle *)nibBundleOrNil UNAVAILABLE_ATTRIBUTE;
- (instancetype)initWithCoder:(nonnull NSCoder *)aDecoder UNAVAILABLE_ATTRIBUTE;

- (id)initWithAdParams:(NSDictionary *)params adView:(UIView *)parentView DEPRECATED_MSG_ATTRIBUTE("Use -initWithLocationID:adType:rootViewController: instead");
- (instancetype)initWithLocationID:(NSString *)locationID adType:(ADGAdType)adType rootViewController:(nullable UIViewController *)rootViewController;
- (void)setDelegate:(id)delegate failedLimit:(int)failedLimit;
- (void)setFrame:(CGRect)rect;
- (void)setFlexibleWidth:(float)percentage;
- (void)setBackGround:(UIColor *)color opaque:(BOOL)opaque;
- (void)updateView;
- (void)addAdContainerView:(nonnull UIView *)adContainerView;
- (void)loadRequest;
- (void)resumeRefresh;
- (void)resumeRefreshTimer;
- (void)pauseRefresh;
- (void)setMiddleware:(ADGMiddleware)mw;
- (void)setFillerRetry:(BOOL)retry __attribute__((deprecated));
- (void)delegateViewManagement:(UIView *)view __attribute__((deprecated));
- (void)delegateViewManagement:(UIView *)view nativeAd:(id)nativeAd __attribute__((deprecated));
- (void)setAutomaticallyRemoveOnReload:(UIView *)view;

- (void)finishMediation;
- (void)waitShowing;
- (void)show;
- (void)setLat:(double)lat __attribute__((deprecated));
- (void)setLon:(double)lon __attribute__((deprecated));
+ (void)setLat:(double)lat __attribute__((deprecated));
+ (void)setLon:(double)lon __attribute__((deprecated));
- (void)addMediationNativeAdView:(UIView *)mediationNativeAdView __attribute__((deprecated));
- (void)setEnableSound:(BOOL)enableSound;
- (void)setEnableTestMode:(BOOL)isTest;
- (void)setSSLMode:(BOOL)isSSL;
+ (void)setDefaultSSLMode:(BOOL)isDefaultSSL;
- (void)setFillerLimit:(int)limit __attribute__((deprecated));
- (void)disableCallingNativeAdTrackers;
- (NSString *)getBeacon;

- (void)enableRetryingOnFailedMediation;
- (void)setPreLoad:(BOOL)preLoad __attribute__((deprecated));
- (void)stopAutomaticLoad __attribute__((deprecated));
- (void)setDivideShowing:(BOOL)devide;

- (void)disableCheckingWebViewFillerAd __attribute__((deprecated));

- (BOOL)isReadyForInterstitial;

- (void)addHeaderBiddingParamsWithAmznAdResponse:(id)adResponse;
- (void)addHeaderBiddingParamWithKey:(ADGHeaderBiddingParamKeys)key value:(NSString *)value;
- (void)addRequestOptionParamWithCustomKey:(NSString *)key value:(NSString *)value;

// deprecated methods
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wmissing-selector-name"
- (nonnull id)initWithAdParams:(nonnull NSDictionary *)params:(nullable UIView *)parentView __attribute__((deprecated));
- (nonnull NSString *)getLocationid __attribute__((deprecated));
- (void)setBackGround:(nonnull UIColor *)color:(BOOL)opaque __attribute__((deprecated));
#pragma clang diagnostic pop
@end

@protocol ADGManagerViewControllerDelegate
@optional
- (void)ADGManagerViewControllerReceiveAd:(nonnull ADGManagerViewController *)adgManagerViewController;
- (void)ADGManagerViewControllerReceiveAd:(nonnull ADGManagerViewController *)adgManagerViewController
                        mediationNativeAd:(nonnull id)mediationNativeAd;
- (void)ADGManagerViewControllerReceiveAd:(nonnull ADGManagerViewController *)adgManagerViewController
                        mediationNativeAds:(nonnull NSArray *)mediationNativeAds;
- (void)ADGManagerViewControllerFailedToReceiveAd:(nonnull ADGManagerViewController *)adgManagerViewController
                                             code:(kADGErrorCode)code;
- (void)ADGManagerViewControllerDidTapAd:(nonnull ADGManagerViewController *)adgManagerViewController;
- (void)ADGManagerViewControllerFinishImpression:(nonnull ADGManagerViewController *)adgManagerViewController;
- (void)ADGManagerViewControllerFailInImpression:(nonnull ADGManagerViewController *)adgManagerViewController;
- (void)ADGManagerViewControllerReadyMediation:(nonnull ADGManagerViewController *)adgManagerViewController
                                     mediation:(nonnull id)mediation;
// deprecated delegates
- (void)ADGManagerViewControllerOpenUrl:(nonnull ADGManagerViewController *)adgManagerViewController __attribute__((deprecated));
@end
