
@interface ADGVideo : NSObject

@property (nonatomic, strong, readonly, nullable) NSString *vasttag;
@property (nonatomic, assign, readonly, getter=isValid) BOOL isValid;

- (nonnull instancetype)initWithDictionary:(nullable NSDictionary *)dict;
+ (nonnull instancetype)videoWithDictionary:(nullable NSDictionary *)dict;

@end
