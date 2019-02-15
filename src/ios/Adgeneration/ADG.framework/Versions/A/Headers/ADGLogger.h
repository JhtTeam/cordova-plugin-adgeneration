//
//  ADGLogger.h
//  ADG
//
//  Copyright © 2016年 adgeneration. All rights reserved.
//

typedef NS_ENUM(NSInteger, ADGLogLevel) {
    kADGLogLevelDebug = 0,
    kADGLogLevelInfo,
    kADGLogLevelWarning,
    kADGLogLevelError
};

@interface ADGLogger : NSObject

+ (ADGLogLevel)logLevel;
+ (void)setLogLevel:(ADGLogLevel)logLevel;
+ (void)outputDebug:(NSString *)str;
+ (void)outputInfo:(NSString *)str;
+ (void)outputWarning:(NSString *)str;
+ (void)outputError:(NSString *)str;
+ (void)outputDebug:(NSString *)str cls:(Class)cls method:(SEL)sel;
+ (void)outputInfo:(NSString *)str cls:(Class)cls method:(SEL)sel;
+ (void)outputWarning:(NSString *)strb cls:(Class)cls method:(SEL)sel;
+ (void)outputError:(NSString *)str cls:(Class)cls method:(SEL)sel;
+ (void)outputDebug:(NSString *)str error:(NSObject *)error cls:(Class)cls method:(SEL)sel;
+ (void)outputError:(NSString *)str error:(NSObject *)error cls:(Class)cls method:(SEL)sel;

FOUNDATION_EXPORT void ADGLogd(NSString *format, ...) NS_FORMAT_FUNCTION(1,2) NS_NO_TAIL_CALL;
FOUNDATION_EXPORT void ADGLogi(NSString *format, ...) NS_FORMAT_FUNCTION(1,2) NS_NO_TAIL_CALL;
FOUNDATION_EXPORT void ADGLogw(NSString *format, ...) NS_FORMAT_FUNCTION(1,2) NS_NO_TAIL_CALL;
FOUNDATION_EXPORT void ADGLoge(NSString *format, ...) NS_FORMAT_FUNCTION(1,2) NS_NO_TAIL_CALL;
FOUNDATION_EXPORT void ADGLogD(Class cls, SEL sel, NSString *format, ...) NS_FORMAT_FUNCTION(3,4) NS_NO_TAIL_CALL;
FOUNDATION_EXPORT void ADGLogI(Class cls, SEL sel, NSString *format, ...) NS_FORMAT_FUNCTION(3,4) NS_NO_TAIL_CALL;
FOUNDATION_EXPORT void ADGLogW(Class cls, SEL sel, NSString *format, ...) NS_FORMAT_FUNCTION(3,4) NS_NO_TAIL_CALL;
FOUNDATION_EXPORT void ADGLogE(Class cls, SEL sel, NSString *format, ...) NS_FORMAT_FUNCTION(3,4) NS_NO_TAIL_CALL;

@end
