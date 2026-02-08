
import 'my_nrf_plugin23_platform_interface.dart';

class MyNrfPlugin23 {
  Future<String?> getPlatformVersion() {
    return MyNrfPlugin23Platform.instance.getPlatformVersion();
  }
}


/*import 'package:flutter/services.dart';
import 'my_custom_plugin2_platform_interface.dart';

class MyCustomPlugin2 {
  static const MethodChannel _channel =
      MethodChannel('my_custom_plugin2');

  Future<String?> getPlatformVersion() {
    return MyCustomPlugin2Platform.instance.getPlatformVersion();
  }

  static Future<int> getBatteryLevel() async {
    final int level =
        await _channel.invokeMethod<int>('getBatteryLevel') ?? -1;
    return level;
  }
}*/
