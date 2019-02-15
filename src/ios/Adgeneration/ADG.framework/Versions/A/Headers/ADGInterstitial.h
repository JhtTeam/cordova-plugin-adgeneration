#import "ADGManagerViewController.h"

@interface ADGInterstitial : NSObject <ADGManagerViewControllerDelegate> {
}
@property (nonatomic, weak) id delegate;
@property (nonatomic, weak, setter=setRootViewController:) UIViewController *rootViewController;
@property (nonatomic, assign, setter=setIsFullscreen:) BOOL isFullscreen;
- (void)setWindowBackground:(UIColor *)color;

- (void)setTemplateType:(int)type;
- (void)setPortraitTemplateType:(int)type;
- (void)setLandscapeTemplateType:(int)type;

- (void)setBackgroundType:(int)designType;
- (void)setPortraitBackgroundType:(int)designType;
- (void)setLandscapeBackgroundType:(int)designType;

- (void)setCloseButtonType:(int)designType;
- (void)setPortraitCloseButtonType:(int)designType;
- (void)setLandscapeCloseButtonType:(int)designType;

- (void)setOrientationReload:(BOOL)orientationReload;
- (void)setSpan:(int)span;
- (void)setSpan:(int)span isPercentage:(BOOL)isPercentage;
- (void)removeBackgroundTapGesture;

- (void)addHeaderBiddingParamsWithAmznAdResponse:(id)adResponse;
- (void)addHeaderBiddingParamWithKey:(ADGHeaderBiddingParamKeys)key value:(NSString *)value;
- (void)addRequestOptionParamWithCustomKey:(NSString *)key value:(NSString *)value;
- (void)setLocationId:(NSString *)locationId;
- (void)setAdType:(ADGAdType)adType;
- (void)setAdBackGroundColor:(UIColor *)color;
- (void)setAdScale:(double)scale;
- (void)setMiddleware:(ADGMiddleware)middleware;
- (void)setPreventAccidentClick:(BOOL)preventAccidentClick;
- (void)setLat:(double)lat __attribute__((deprecated));
- (void)setLon:(double)lon __attribute__((deprecated));
- (void)setEnableSound:(BOOL)enableSound;
- (void)setEnableTestMode:(BOOL)isTest;
- (void)setSSLMode:(BOOL)isSSL;
+ (void)setDefaultSSLMode:(BOOL)isDefaultSSL;
- (void)setFillerLimit:(int)limit __attribute__((deprecated));

- (void)preload;
- (BOOL)show;
- (void)dismiss;
@end

@protocol ADGInterstitialDelegate <ADGManagerViewControllerDelegate>
@optional
- (void)ADGInterstitialClose;
@end
