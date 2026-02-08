
import 'my_custom_plugin2_platform_interface.dart';


class MyCustomPlugin2 {
  /// Channel único del plugin
  static const MethodChannel _channel =
      MethodChannel('my_custom_plugin2');

  /// -----------------------------
  /// PATRÓN OFICIAL (instancia)
  /// -----------------------------
  Future<String?> getPlatformVersion() {
    return MyCustomPlugin2Platform.instance.getPlatformVersion();
  }

  /// -----------------------------
  /// MÉTODOS PARA FLUTTERFLOW
  /// -----------------------------

  /// Ejemplo batería
  static Future<int> getBatteryLevel() async {
    final int level =
        await _channel.invokeMethod<int>('getBatteryLevel') ?? -1;
    return level;
  }

  /// Ejemplo simple para test
  static Future<bool> ping() async {
    final bool ok =
        await _channel.invokeMethod<bool>('ping') ?? false;
    return ok;
  }
}
