#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ADGNativeInterfaceChild : NSObject

@property (nonatomic, weak) id delegate;
@property (nonatomic, copy) NSString *adId;
@property (nonatomic, copy) NSDictionary *param;
@property (nonatomic, copy) NSString *vastXML;
@property (nonatomic, assign) CGRect rect;
@property (nonatomic, weak) UIViewController *viewCon;
@property (nonatomic, weak) UIViewController *rootViewCon;
@property (nonatomic, assign) BOOL isOriginInterstitial;
@property (nonatomic, assign) BOOL enableSound;
@property (nonatomic, assign) BOOL isTest;
@property (nonatomic, assign) BOOL usePartsResponse;
@property (nonatomic, assign) BOOL callNativeAdTrackers;
@property (nonatomic, assign) BOOL expandFrame;
@property (nonatomic, assign) BOOL viewablePayment;
@property (nonatomic, copy) NSString *beacon;
- (void)sendReceiveAd;
- (void)sendReceiveNativeAd:(id)nativeAd;
- (void)sendFailedToReceiveAd;
- (void)sendCompleteMovieAd;
- (void)sendOpenUrl __attribute__((deprecated));
- (void)sendDidTapAd;
- (void)sendCloseInterstitial;
- (void)sendShowInterstitial;
- (void)sendReplayMovieAd;
- (void)sendResumeRefresh;
- (void)sendErrorClassName;
- (void)sendReadyMediation:(id)mediation;
- (BOOL)loadProcess;
- (void)startProcess;
- (void)startADGProcess;
- (void)stopProcess;
- (BOOL)checkOSVersion;
@end

@protocol ADGNativeInterfaceChildDelegate
@optional
- (void)ADGNativeInterfaceChildReceiveAd;
- (void)ADGNativeInterfaceChildReceiveAd:(id)nativeAd;
- (void)ADGNativeInterfaceChildFailedToReceiveAd;
- (void)ADGNativeInterfaceChildCompleteMovieAd;
- (void)ADGNativeInterfaceChildOpenUrl __attribute__((deprecated));
- (void)ADGNativeInterfaceChildDidTapAd;
- (void)ADGNativeInterfaceChildCloseInterstitial;
- (void)ADGNativeInterfaceChildShowInterstitial;
- (void)ADGNativeInterfaceChildReplayMovieAd;
- (void)ADGNativeInterfaceChildResumeRefresh;
- (void)ADGNativeInterfaceChildErrorClassName;
- (void)ADGNativeInterfaceChildReadyMediation:(id)mediation;
@end
