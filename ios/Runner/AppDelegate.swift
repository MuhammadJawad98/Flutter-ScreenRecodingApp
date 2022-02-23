import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
      
      let channelName = "flutter.native/helper"
      let controller : FlutterViewController = window?.rootViewController as! FlutterViewController
      
      let methodChannel = FlutterMethodChannel(name: channelName, binaryMessenger: controller.binaryMessenger)

      methodChannel.setMethodCallHandler {(call: FlutterMethodCall, result: FlutterResult) -> Void in
          if (call.method == "startScreenRecord") {
              print("start recording");
              result("Start Recording");
          }else if(call.method == "stopScreenRecord"){
              print("stop recording");
              result("Stop Recording");
          }
      }
    GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
