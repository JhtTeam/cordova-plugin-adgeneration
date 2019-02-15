//
//  ADGMediaView.h
//  ADG
//
//  Copyright © 2017年 adgeneration. All rights reserved.
//

#import "ADGNativeAd.h"

@interface ADGMediaView : UIView

/**
 * Please set a native ad object. The necessary to display the media file.
 */
@property (nonatomic, nonnull) ADGNativeAd *nativeAd;

/**
 * Please set current frontmost ViewController. The necessary to modal presentation.
 */
@property (nonatomic, nonnull) UIViewController *viewController;

/**
 * Determines whether full screen video player enabled.
 */
@property (nonatomic) BOOL fullscreenVideoPlayerEnabled;

/**
 * Used in a native banner template.
 */
@property (nonatomic) BOOL isTiny;

/**
 * Start loading the media file.
 */
- (void)load;

@end
