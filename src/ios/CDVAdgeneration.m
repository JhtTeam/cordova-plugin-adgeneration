#import "CDVAdgeneration.h"

@implementation CDVAdgeneration

- (void)showInterstitial:(CDVInvokedUrlCommand *)command
{
    _interstitial = [[ADGInterstitial alloc] init];
    _interstitial.delegate = self;
    [_interstitial setLocationId:@"10723"]; // アドジェネが用意しているテスト用広告枠ID
    [_interstitial show];
}

- (void)ADGManagerViewControllerReceiveAd:(ADGManagerViewController *)adgManagerViewController
{
    NSLog(@"%@", @"ADGManagerViewControllerReceiveAd");
}

- (void)ADGManagerViewControllerFailedToReceiveAd:(ADGManagerViewController *)adgManagerViewController code:(kADGErrorCode)code {
    NSLog(@"%@", @"ADGManagerViewControllerFailedToReceiveAd");
    switch (code) {
        case kADGErrorCodeExceedLimit:
        case kADGErrorCodeNeedConnection:
        break;
        default:
            [adgManagerViewController loadRequest];
            break;
    }
}

- (void)ADGManagerViewControllerOpenUrl:(ADGManagerViewController *)adgManagerViewController{
    NSLog(@"%@", @"ADGManagerViewControllerOpenUrl");
}


- (void)ADGInterstitialClose
{
    NSLog(@"%@", @"ADGInterstitialClose");
}

@end