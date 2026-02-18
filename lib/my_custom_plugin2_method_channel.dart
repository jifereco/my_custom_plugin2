import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'my_custom_plugin2_platform_interface.dart';




/// An implementation of [MyCustomPlugin2Platform] that uses method channels.
class MethodChannelMyCustomPlugin2 extends MyCustomPlugin2Platform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('my_custom_plugin2');


  final EventChannel _bondChannel =
      const EventChannel('my_custom_plugin2/bond_stream');



  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
Future<List<dynamic>> scanDevices() async {
  final result = await methodChannel.invokeMethod('scanDevices');
  return result;
}

@override
Future<List<dynamic>> getBondedDevices() async {
  final result = await methodChannel.invokeMethod('getBondedDevices');
  return result;
}


@override
Stream<Map<dynamic, dynamic>> get bondStream {
  return _bondChannel
      .receiveBroadcastStream()
      .map((event) => Map<dynamic, dynamic>.from(event));
}


  @override
  Future<bool> connectDevice(String address, bool autoConnect) async {
    final result = await methodChannel.invokeMethod<bool>(
      'connectDevice',
      {
        'address': address,
        'autoConnect': autoConnect,
      },
    );
    return result ?? false;
  }

  @override
  Future<bool> disconnectDevice() async {
    final result =
        await methodChannel.invokeMethod<bool>('disconnectDevice');
    return result ?? false;
  }

@override
Future<bool> sendCommand(String command) async {
  final result = await methodChannel.invokeMethod<bool>(
    'sendCommand',
    {'command': command},
  );
  return result ?? false;
}



}
