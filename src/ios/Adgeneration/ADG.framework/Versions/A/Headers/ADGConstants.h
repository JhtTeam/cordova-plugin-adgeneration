#ifndef ADGConstants_h
#define ADGConstants_h

#define kADGSlideAnimationDuration 0.5

#define kADGAdSize_Sp_Width 320
#define kADGAdSize_Sp_Height 50

#define kADGAdSize_Large_Width 320
#define kADGAdSize_Large_Height 100

#define kADGAdSize_Rect_Width 300
#define kADGAdSize_Rect_Height 250

#define kADGAdSize_Tablet_Width 728
#define kADGAdSize_Tablet_Height 90

#define kADGLimitCounterNoLimit 0

#define kADGNonExistentCoordinate 999

typedef NS_ENUM(NSUInteger, ADGAdType) {
    kADG_AdType_Sp = 0,
    kADG_AdType_Large,
    kADG_AdType_Rect,
    kADG_AdType_Tablet,
    kADG_AdType_Free
};

typedef enum { kADG_Mw_None = 0, kADG_Mw_Other, kADG_Mw_Unity, kADG_Mw_Titanium, kADG_Mw_Cocos2dx } ADGMiddleware;

typedef NS_ENUM(NSUInteger, kADGErrorCode) {
    kADGErrorCodeUnknown,
    kADGErrorCodeCommunicationError,
    kADGErrorCodeReceivedFiller,
    kADGErrorCodeNoAd,
    kADGErrorCodeNeedConnection,
    kADGErrorCodeExceedLimit,
    kADGErrorCodeTemplateFailed
};

typedef NS_ENUM(NSUInteger, ADGHeaderBiddingParamKeys) {
    ADGHeaderBiddingParamKeysAmznBidID,
    ADGHeaderBiddingParamKeysAmznHostName,
    ADGHeaderBiddingParamKeysAmznSlots
};
#define ADGHeaderBiddingParamKeysStrings @[@"hb_amzn_b", @"hb_amzn_h", @"hb_amznslots"]

#endif

@interface ADGConstants : NSObject
+ (NSString *)kADGErrorCodetoString:(kADGErrorCode)code;
@end
