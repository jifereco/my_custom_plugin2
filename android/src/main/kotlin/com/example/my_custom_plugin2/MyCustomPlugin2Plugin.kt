package com.example.my_custom_plugin2

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MyCustomPlugin2Plugin */
class MyCustomPlugin2Plugin :
    FlutterPlugin,
    MethodCallHandler {
    // The MethodChannel that will the communication between Flutter and native Android
    //
    // This local reference serves to register the plugin with the Flutter Engine and unregister it
    // when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var bleScanner: BleScanner

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "my_custom_plugin2")
        channel.setMethodCallHandler(this)
        bleScanner = BleScanner(channel)
    }

    override fun onMethodCall(
        call: MethodCall,
        result: Result
    ) {

        
if (call.method == "getPlatformVersion") {

    result.success("Android ${android.os.Build.VERSION.RELEASE}")

} else if (call.method == "scanDevices") {

    bleScanner.scanForDevices(result)

} else {

    result.notImplemented()
}




    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
