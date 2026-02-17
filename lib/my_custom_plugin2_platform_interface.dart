import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'my_custom_plugin2_method_channel.dart';

abstract class MyCustomPlugin2Platform extends PlatformInterface {
  /// Constructs a MyCustomPlugin2Platform.
  MyCustomPlugin2Platform() : super(token: _token);

  static final Object _token = Object();

  static MyCustomPlugin2Platform _instance = MethodChannelMyCustomPlugin2();

  /// The default instance of [MyCustomPlugin2Platform] to use.
  ///
  /// Defaults to [MethodChannelMyCustomPlugin2].
  static MyCustomPlugin2Platform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MyCustomPlugin2Platform] when
  /// they register themselves.
  static set instance(MyCustomPlugin2Platform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<List<dynamic>> scanDevices() {
  throw UnimplementedError('scanDevices() has not been implemented.');
}

  Future<List<dynamic>> getBondedDevices() {
    throw UnimplementedError('getBondedDevices() has not been implemented.');
  }

Stream<Map<dynamic, dynamic>> get bondStream {
  throw UnimplementedError('bondStream has not been implemented.');
}

Future<bool> connectDevice(String address, bool autoConnect) {
  throw UnimplementedError('connectDevice() has not been implemented.');
}

Future<bool> disconnectDevice() {
  throw UnimplementedError('disconnectDevice() has not been implemented.');
}


}
