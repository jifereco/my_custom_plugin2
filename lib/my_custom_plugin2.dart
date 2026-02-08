
import 'my_custom_plugin2_platform_interface.dart';

class MyCustomPlugin2 {
  Future<String?> getPlatformVersion() {
    return MyCustomPlugin2Platform.instance.getPlatformVersion();
  }
}
