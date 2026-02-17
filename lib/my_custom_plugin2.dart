
import 'my_custom_plugin2_platform_interface.dart';


class MyCustomPlugin2 {
  Future<String?> getPlatformVersion() {
    return MyCustomPlugin2Platform.instance.getPlatformVersion();
  }

    Future<List<dynamic>> scanDevices() {
    return MyCustomPlugin2Platform.instance.scanDevices();
  }

  Future<List<dynamic>> getBondedDevices() {
  return MyCustomPlugin2Platform.instance.getBondedDevices();
  }

Stream<Map<dynamic, dynamic>> get bondStream {
  return MyCustomPlugin2Platform.instance.bondStream;
}

Future<bool> connectDevice(String address,
    {bool autoConnect = false}) {
  return MyCustomPlugin2Platform.instance
      .connectDevice(address, autoConnect);
}

Future<bool> disconnectDevice() {
  return MyCustomPlugin2Platform.instance.disconnectDevice();
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
