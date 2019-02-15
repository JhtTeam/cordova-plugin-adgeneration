//
//  ADGNativeAd.h
//  ADG
//
//  Copyright © 2015年 adgeneration. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "ADGTitle.h"
#import "ADGImage.h"
#import "ADGData.h"
#import "ADGLink.h"
#import "ADGVideo.h"

typedef NS_ENUM(NSInteger, ADGNativeAdType) {
    kUndefined = 0,
    kDefault = 1,
    kGunosyAds = 2,
    kAmebaJointAlliance = 3
};

@interface ADGNativeAd : NSObject

@property (nonatomic, assign, readonly) int ver;

@property (nonatomic, strong, readonly, nullable) ADGTitle *title;
@property (nonatomic, strong, readonly, nullable) ADGImage *mainImage;
@property (nonatomic, strong, readonly, nullable) ADGImage *iconImage;
@property (nonatomic, strong, readonly, nullable) ADGData *sponsored;
@property (nonatomic, strong, readonly, nullable) ADGData *desc;
@property (nonatomic, strong, readonly, nullable) ADGData *ctatext;

@property (nonatomic, strong, readonly, nullable) ADGLink *link;
@property (nonatomic, strong, readonly, nullable) NSArray *imptrackers;
@property (nonatomic, strong, readonly, nullable) NSString *jstracker;
@property (nonatomic, strong, readonly, nullable) NSObject *ext;
@property (nonatomic, assign, readonly) ADGNativeAdType nativeAdType;

@property (nonatomic, assign) BOOL informationIconViewDefault;
@property (nonatomic, strong, readonly, nullable) ADGData *informationIconAccompanyingText;
@property (nonatomic, strong, readonly, nullable) ADGData *informationIconOptoutURL;
@property (nonatomic, strong, readonly, nullable) ADGData *informationIconIconURL;

@property (nonatomic, strong, readonly, nullable) ADGVideo *video;
@property (nonatomic, assign, readonly, getter=canLoadMedia) BOOL canLoadMedia;

@property (nonatomic, weak, nullable) UIViewController *rootViewController;

- (nonnull instancetype)initWithDictionary:(nonnull NSDictionary *)dict;
- (void)callImptrackers;
- (void)callJstracker;
- (void)setTapEvent:(nonnull UIView *)view __attribute__((deprecated));
- (void)setTapEvent:(nonnull UIView *)view handler:(nullable void (^)(void))block;
- (void)callTapHandler;
- (void)callClickTracker;

+ (nonnull instancetype)nativeAdWithDictionary:(nonnull NSDictionary *)dict;
+ (void)callTrackers:(nullable NSArray *)trackers;
+ (void)callTrackers:(nullable NSArray *)trackers isPostRequest:(BOOL)isPostRequest;

@end
