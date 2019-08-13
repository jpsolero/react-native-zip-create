#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"


@interface RCT_EXTERN_MODULE(ZipCreate, RCTEventEmitter)

RCT_EXTERN_METHOD(zip:(NSArray<NSURL *>)from
                  destinationPath:(NSURL *)destinationPath
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

@end
