#import "RNAppirater.h"
#import "Appirater.h"

@implementation RNAppirater

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setAppId:(NSString*)appId)
{
  [Appirater setAppId:appId];
}

RCT_EXPORT_METHOD(setDaysUntilPrompt:(double)value)
{
  [Appirater setDaysUntilPrompt:value];
}

RCT_EXPORT_METHOD(setUsesUntilPrompt:(NSInteger)value)
{
  [Appirater setUsesUntilPrompt:value];
}

RCT_EXPORT_METHOD(setSignificantEventsUntilPrompt:(NSInteger)value)
{
  [Appirater setSignificantEventsUntilPrompt:value];
}

RCT_EXPORT_METHOD(setTimeBeforeReminding:(double)value)
{
  [Appirater setTimeBeforeReminding:value];
}

RCT_EXPORT_METHOD(setDebug:(BOOL)debug)
{
  [Appirater setDebug:debug];
}

RCT_EXPORT_METHOD(appLaunched)
{
  [Appirater appLaunched:YES];
}

RCT_EXPORT_METHOD(appEnteredForeground)
{
  [Appirater appEnteredForeground:YES];
}

RCT_EXPORT_METHOD(userDidSignificantEvent)
{
  [Appirater userDidSignificantEvent:YES];
}

@end
