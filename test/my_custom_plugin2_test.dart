import 'package:flutter_test/flutter_test.dart';
import 'package:my_custom_plugin2/my_custom_plugin2.dart';
import 'package:my_custom_plugin2/my_custom_plugin2_platform_interface.dart';
import 'package:my_custom_plugin2/my_custom_plugin2_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMyCustomPlugin2Platform
    with MockPlatformInterfaceMixin
    implements MyCustomPlugin2Platform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final MyCustomPlugin2Platform initialPlatform = MyCustomPlugin2Platform.instance;

  test('$MethodChannelMyCustomPlugin2 is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMyCustomPlugin2>());
  });

  test('getPlatformVersion', () async {
    MyCustomPlugin2 myCustomPlugin2Plugin = MyCustomPlugin2();
    MockMyCustomPlugin2Platform fakePlatform = MockMyCustomPlugin2Platform();
    MyCustomPlugin2Platform.instance = fakePlatform;

    expect(await myCustomPlugin2Plugin.getPlatformVersion(), '42');
  });
}
