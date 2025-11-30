import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'music_platform_interface.dart';

class MethodChannelMusic extends MusicPlatform {
  final methodChannel = const MethodChannel('music');
  final eventChannel = const EventChannel('music_events');

  @override
  void loadLocalSongs() {
    methodChannel.invokeMethod('loadLocal');
  }

  @override
  Future<void> playAtIndex(int index) async {
    await methodChannel.invokeMethod('playAtIndex', {'index': index});
  }

  @override
  Stream<dynamic> get songStream => eventChannel.receiveBroadcastStream();
}
