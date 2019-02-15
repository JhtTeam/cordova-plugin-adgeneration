#import <Cordova/CDV.h>        
#import <UIKit/UIKit.h>        
#import <ADG/ADGManagerViewController.h>
#import <ADG/ADGInterstitial.h>

@interface CDVAdgeneration: CDVPlugin <ADGManagerViewControllerDelegate, ADGInterstitialDelegate>

- (void)sayHello:(CDVInvokedUrlCommand*)command;
@property (nonatomic, retain) ADGManagerViewController *adg;
@property (nonatomic, retain) ADGInterstitial *interstitial;

- (void)showInterstitial:(CDVInvokedUrlCommand *)command;

@end