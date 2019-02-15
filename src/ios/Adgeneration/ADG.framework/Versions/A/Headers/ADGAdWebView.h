#import <UIKit/UIKit.h>
#import <WebKit/WebKit.h>

@protocol ADGAdWebViewDelegate;

@interface ADGAdWebView : UIView

@property (nonatomic, weak) id<ADGAdWebViewDelegate> delegate;
@property (nonatomic, strong, readonly) WKWebView *adWebView;
@property (nonatomic, assign, setter=setScrollEnabled:, getter=isScrollEnabled) BOOL scrollEnabled;
@property (nonatomic, assign) BOOL mraidEnabled;
@property (nonatomic, assign) BOOL isInterstitial;
@property (nonatomic, weak) UIViewController *rootViewController;
@property (nonatomic, assign) BOOL confirmAlertEnabled;

- (void)loadHTMLString:(NSString *)HTML baseURL:(NSURL *)baseURL;
- (void)setFrame:(CGRect)rect;
- (void)stopLoading;
- (void)setWebViewBackgroundColor:(UIColor *)color;
- (void)setWebViewOpaque:(BOOL)opaque;
- (void)setAdScale:(float)scale;
- (void)stopViewability;

@end

@protocol ADGAdWebViewDelegate<NSObject>

@optional
- (void)adgAdWebView:(ADGAdWebView *)adgAdWebView didFinishLoadWithNavigation:(WKNavigation *)navigation;
- (void)adgAdWebView:(ADGAdWebView *)adgAdWebView didFailLoadWithNavigation:(WKNavigation *)navigation error:(NSError *)error;
- (void)adgAdWebView:(ADGAdWebView *)adgAdWebView willOpenURL:(NSURL *)url;
- (void)adgAdWebView:(ADGAdWebView *)adgAdWebView didOpenURL:(NSURL *)url;

@end
