import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'my_custom_plugin2_platform_interface.dart';

/// An implementation of [MyCustomPlugin2Platform] that uses method channels.
class MethodChannelMyCustomPlugin2 extends MyCustomPlugin2Platform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('my_custom_plugin2');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
