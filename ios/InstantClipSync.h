
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNInstantClipSyncSpec.h"

@interface InstantClipSync : NSObject <NativeInstantClipSyncSpec>
#else
#import <React/RCTBridgeModule.h>

@interface InstantClipSync : NSObject <RCTBridgeModule>
#endif

@end
